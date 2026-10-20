"""Copia no destructiva de PrograV a bases por servicio en la misma instancia.

Sin argumentos: inspecciona y genera DDL sin datos. --aplicar: crea destinos
vacios y copia/verifica en una transaccion. --verificar: solo compara.
Nunca ejecuta el dump, elimina objetos ni modifica filas de origen.
"""

import argparse
from collections import Counter
from datetime import datetime, timezone
import hashlib
import json
import os
from pathlib import Path
import re
import sys

import pyodbc

ROOT = Path(__file__).resolve().parent.parent
OUTPUT = ROOT / "db" / "por_servicio"


def quote(value):
    return "[" + value.replace("]", "]]") + "]"


def literal(value):
    return "N'" + str(value).replace("'", "''") + "'"


def table_name(value):
    return ".".join(quote(part) for part in value.split("."))


def configuration():
    result = {}
    for line in (ROOT / ".env").read_text(encoding="utf-8-sig").splitlines():
        match = re.match(r"\s*(?:export\s+)?([A-Za-z_][A-Za-z0-9_]*)\s*=\s*(.*?)\s*$", line)
        if match:
            value = match[2]
            if len(value) >= 2 and value[0] == value[-1] and value[0] in "\"'":
                value = value[1:-1]
            result[match[1]] = value
    result.update({k: v for k, v in os.environ.items() if k.startswith("DB_")})
    for key in ["DB_HOST", "DB_NAME", "DB_USERNAME", "DB_PASSWORD"]:
        if not result.get(key):
            raise ValueError(f"Falta {key} en .env o en el entorno.")
    return result


def connect(cfg, database, autocommit=False):
    def escape(value):
        return "{" + str(value).replace("}", "}}") + "}"
    values = {
        "DRIVER": "ODBC Driver 18 for SQL Server",
        "SERVER": cfg["DB_HOST"] + "," + cfg.get("DB_PORT", "1433"),
        "DATABASE": database, "UID": cfg["DB_USERNAME"], "PWD": cfg["DB_PASSWORD"],
        "Encrypt": "yes", "TrustServerCertificate": "yes",
    }
    return pyodbc.connect(";".join(k + "=" + escape(v) for k, v in values.items()),
                          timeout=15, autocommit=autocommit)


def rows(cursor, sql, *parameters):
    cursor.execute(sql, *parameters)
    names = [column[0] for column in cursor.description]
    return [dict(zip(names, row)) for row in cursor.fetchall()]


def inspect_source(cursor, ownership, technical_tables=()):
    found = rows(cursor, """
        SELECT t.object_id, s.name AS schema_name, t.name,
          t.temporal_type, t.is_memory_optimized, t.is_filetable
        FROM sys.tables t JOIN sys.schemas s ON s.schema_id=t.schema_id
        WHERE t.is_ms_shipped=0 ORDER BY s.name,t.name
        """)
    business = {r["schema_name"] + "." + r["name"]: r for r in found
                if r["schema_name"] in {t.split(".")[0] for t in ownership}
                and r["schema_name"] + "." + r["name"] not in technical_tables}
    if set(business) != set(ownership):
        raise ValueError("El inventario de tablas difiere de distribucion.json: " +
                         str(sorted(set(business) ^ set(ownership))))
    unexpected = [r for r in found if r["schema_name"] + "." + r["name"] not in ownership
                  and (r["schema_name"], r["name"]) != ("dbo", "sysdiagrams")
                  and r["schema_name"] + "." + r["name"] not in technical_tables]
    if unexpected:
        raise ValueError("Hay tablas sin propietario; revisar el inventario antes de migrar.")
    collation = cursor.execute("SELECT CONVERT(varchar(128),DATABASEPROPERTYEX(DB_NAME(),'Collation'))").fetchval()
    model = {"tables": {}, "foreign_keys": [], "collation": collation,
             "preserved_in_source": [r["schema_name"] + "." + r["name"] for r in found
                                     if r["schema_name"] + "." + r["name"] not in ownership]}
    for name, info in business.items():
        oid = info["object_id"]
        if info["temporal_type"] or info["is_memory_optimized"] or info["is_filetable"]:
            raise ValueError("Tipo de tabla no soportado: " + name)
        columns = rows(cursor, """
            SELECT c.column_id,c.name,ty.name AS type_name,c.max_length,c.precision,c.scale,
              c.is_nullable,c.collation_name,c.is_computed,c.is_sparse,c.is_filestream,
              c.generated_always_type,c.encryption_type,c.is_identity,
              CONVERT(varchar(50),ic.seed_value) AS seed_value,
              CONVERT(varchar(50),ic.increment_value) AS increment_value,
              CONVERT(varchar(50),ic.last_value) AS last_value,
              dc.name AS default_name,dc.definition AS default_definition,ty.is_user_defined
            FROM sys.columns c JOIN sys.types ty ON ty.user_type_id=c.user_type_id
            LEFT JOIN sys.identity_columns ic ON ic.object_id=c.object_id AND ic.column_id=c.column_id
            LEFT JOIN sys.default_constraints dc ON dc.object_id=c.default_object_id
            WHERE c.object_id=? ORDER BY c.column_id
            """, oid)
        for column in columns:
            if any(column[k] for k in ["is_computed", "is_sparse", "is_filestream",
                                      "generated_always_type", "encryption_type", "is_user_defined"]) or column["type_name"] in {"timestamp", "rowversion", "xml"}:
                raise ValueError("Columna con caracteristicas no soportadas: " + name + "." + column["name"])
        indexes = rows(cursor, """
            SELECT index_id,name,type_desc,is_unique,is_primary_key,is_unique_constraint,
              has_filter,filter_definition,is_disabled,fill_factor,is_padded,
              ignore_dup_key,allow_row_locks,allow_page_locks
            FROM sys.indexes WHERE object_id=? AND index_id>0 AND is_hypothetical=0
            ORDER BY index_id
            """, oid)
        for index in indexes:
            if index["type_desc"] not in {"CLUSTERED", "NONCLUSTERED"} or index["is_disabled"]:
                raise ValueError("Indice no soportado: " + name + "." + index["name"])
            index["columns"] = rows(cursor, """
                SELECT c.name,ic.key_ordinal,ic.is_descending_key,ic.is_included_column
                FROM sys.index_columns ic JOIN sys.columns c
                 ON c.object_id=ic.object_id AND c.column_id=ic.column_id
                WHERE ic.object_id=? AND ic.index_id=? ORDER BY ic.key_ordinal,ic.index_column_id
                """, oid, index["index_id"])
        if not any(i["is_primary_key"] for i in indexes):
            raise ValueError("Se requiere llave primaria para verificar: " + name)
        checks = rows(cursor, """
            SELECT name,definition,is_disabled,is_not_trusted,is_not_for_replication
            FROM sys.check_constraints WHERE parent_object_id=? ORDER BY name
            """, oid)
        triggers = cursor.execute("SELECT COUNT(*) FROM sys.triggers WHERE parent_id=?", oid).fetchval()
        compression = cursor.execute("SELECT COUNT(*) FROM sys.partitions WHERE object_id=? AND data_compression<>0", oid).fetchval()
        if triggers or compression:
            raise ValueError("Hay triggers o compresion no contemplados: " + name)
        model["tables"][name] = {"columns": columns, "indexes": indexes, "checks": checks}
    fks = rows(cursor, """
        SELECT f.object_id,f.name,ps.name+'.'+p.name AS parent_table,
          rs.name+'.'+r.name AS referenced_table,f.delete_referential_action_desc,
          f.update_referential_action_desc,f.is_disabled,f.is_not_trusted,f.is_not_for_replication
        FROM sys.foreign_keys f JOIN sys.tables p ON p.object_id=f.parent_object_id
        JOIN sys.schemas ps ON ps.schema_id=p.schema_id
        JOIN sys.tables r ON r.object_id=f.referenced_object_id
        JOIN sys.schemas rs ON rs.schema_id=r.schema_id ORDER BY ps.name,p.name,f.name
        """)
    for fk in fks:
        if fk["parent_table"] not in ownership:
            continue
        if fk["referenced_table"] not in ownership:
            raise ValueError("Relacion hacia una tabla sin propietario: " + fk["name"])
        fk["columns"] = rows(cursor, """
            SELECT pc.name AS parent_column,rc.name AS referenced_column
            FROM sys.foreign_key_columns fc JOIN sys.columns pc
              ON pc.object_id=fc.parent_object_id AND pc.column_id=fc.parent_column_id
            JOIN sys.columns rc ON rc.object_id=fc.referenced_object_id AND rc.column_id=fc.referenced_column_id
            WHERE fc.constraint_object_id=? ORDER BY fc.constraint_column_id
            """, fk.pop("object_id"))
        fk["local"] = ownership[fk["parent_table"]] == ownership[fk["referenced_table"]]
        model["foreign_keys"].append(fk)
    return model


def column_ddl(column):
    kind = column["type_name"]
    result = quote(column["name"]) + " " + quote(kind)
    if kind in {"varchar", "char", "nvarchar", "nchar", "binary", "varbinary"}:
        length = column["max_length"]
        if length != -1 and kind in {"nvarchar", "nchar"}:
            length //= 2
        result += "(" + ("max" if length == -1 else str(length)) + ")"
    elif kind in {"decimal", "numeric"}:
        result += f"({column['precision']},{column['scale']})"
    elif kind in {"datetime2", "datetimeoffset", "time"}:
        result += f"({column['scale']})"
    if column["collation_name"]:
        result += " COLLATE " + column["collation_name"]
    if column["is_identity"]:
        result += f" IDENTITY({column['seed_value']},{column['increment_value']})"
    result += " NULL" if column["is_nullable"] else " NOT NULL"
    if column["default_name"]:
        result += " CONSTRAINT " + quote(column["default_name"]) + " DEFAULT " + column["default_definition"]
    return result


def schema_statements(model, tables):
    result = ["CREATE SCHEMA " + quote(s) for s in sorted({t.split(".")[0] for t in tables})]
    for table in tables:
        data = model["tables"][table]
        result.append("CREATE TABLE " + table_name(table) + " (\n  " +
                      ",\n  ".join(column_ddl(c) for c in data["columns"]) + "\n)")
        for index in data["indexes"]:
            keys = [quote(c["name"]) + (" DESC" if c["is_descending_key"] else " ASC")
                    for c in index["columns"] if c["key_ordinal"] > 0]
            included = [quote(c["name"]) for c in index["columns"] if c["is_included_column"]]
            settings = {
                "PAD_INDEX": "ON" if index["is_padded"] else "OFF",
                "IGNORE_DUP_KEY": "ON" if index["ignore_dup_key"] else "OFF",
                "ALLOW_ROW_LOCKS": "ON" if index["allow_row_locks"] else "OFF",
                "ALLOW_PAGE_LOCKS": "ON" if index["allow_page_locks"] else "OFF",
            }
            # sys.indexes usa 0 para el valor predeterminado; este motor exige
            # 1..100 cuando FILLFACTOR aparece explicitamente en una PK.
            if index["fill_factor"]:
                settings["FILLFACTOR"] = str(index["fill_factor"])
            options = ", ".join(k + "=" + v for k, v in settings.items())
            if index["is_primary_key"] or index["is_unique_constraint"]:
                statement = "ALTER TABLE " + table_name(table) + " ADD CONSTRAINT " + quote(index["name"])
                statement += " PRIMARY KEY " if index["is_primary_key"] else " UNIQUE "
                statement += index["type_desc"] + " (" + ",".join(keys) + ")"
            else:
                statement = "CREATE " + ("UNIQUE " if index["is_unique"] else "")
                statement += index["type_desc"] + " INDEX " + quote(index["name"]) + " ON " + table_name(table)
                statement += " (" + ",".join(keys) + ")"
                if included:
                    statement += " INCLUDE (" + ",".join(included) + ")"
                if index["has_filter"]:
                    statement += " WHERE " + index["filter_definition"]
            result.append(statement + " WITH (" + options + ")")
        for check in data["checks"]:
            statement = "ALTER TABLE " + table_name(table)
            statement += " WITH NOCHECK" if check["is_not_trusted"] or check["is_disabled"] else " WITH CHECK"
            statement += " ADD CONSTRAINT " + quote(check["name"]) + " CHECK "
            if check["is_not_for_replication"]:
                statement += "NOT FOR REPLICATION "
            result.append(statement + check["definition"])
            if check["is_disabled"]:
                result.append("ALTER TABLE " + table_name(table) + " NOCHECK CONSTRAINT " + quote(check["name"]))
    # Las FK se agregan despues de crear todas las tablas y copiar sus filas.
    return result


def foreign_key_ddl(fk):
    statement = "ALTER TABLE " + table_name(fk["parent_table"])
    statement += " WITH NOCHECK" if fk["is_not_trusted"] or fk["is_disabled"] else " WITH CHECK"
    statement += " ADD CONSTRAINT " + quote(fk["name"]) + " FOREIGN KEY ("
    statement += ",".join(quote(c["parent_column"]) for c in fk["columns"]) + ") REFERENCES "
    statement += table_name(fk["referenced_table"]) + " ("
    statement += ",".join(quote(c["referenced_column"]) for c in fk["columns"]) + ")"
    for action in ["delete", "update"]:
        statement += " ON " + action.upper() + " " + fk[action + "_referential_action_desc"].replace("_", " ")
    if fk["is_not_for_replication"]:
        statement += " NOT FOR REPLICATION"
    return statement


def digest(cursor, database, table, metadata):
    pk = next(i for i in metadata["indexes"] if i["is_primary_key"])
    order = ",".join(quote(c["name"]) for c in pk["columns"] if c["key_ordinal"] > 0)
    columns = ",".join("CONVERT(varbinary(max)," + quote(c["name"]) + ")" for c in metadata["columns"])
    cursor.execute("SELECT " + columns + " FROM " + quote(database) + "." + table_name(table) + " ORDER BY " + order)
    checksum = hashlib.sha256()
    count = 0
    while batch := cursor.fetchmany(256):
        for row in batch:
            count += 1
            for value in row:
                if value is None:
                    checksum.update(b"N")
                else:
                    checksum.update(b"V" + len(value).to_bytes(8, "big") + value)
    return {"rows": count, "sha256_binary_values": checksum.hexdigest()}


def verify(cursor, source, manifest, ownership, model):
    result = []
    for table, metadata in model["tables"].items():
        target = "PrograV_" + ownership[table]
        original = digest(cursor, source, table, metadata)
        copied = digest(cursor, target, table, metadata)
        if original != copied:
            raise ValueError("No coincide el contenido binario de " + table + " en " + target)
        result.append({"table": table, "database": target, **copied})
    return result


def verify_schema(cursor, source, manifest, model):
    """Compara definiciones reales, no solo numero de filas o de objetos."""
    result = []
    for service, tables in manifest.items():
        target = "PrograV_" + service
        cursor.execute("USE " + quote(target))
        technical = ("finanzas.OperacionPago",) if service == "Pago" else ()
        actual = inspect_source(cursor, {t: service for t in tables}, technical)
        if actual["collation"] != model["collation"]:
            raise ValueError("No coincide la intercalacion de " + target)
        for table in tables:
            expected = model["tables"][table]
            copied = actual["tables"][table]
            def normalize(data):
                # Los IDs de indice son internos a cada tabla y pueden tener huecos.
                normalized = json.loads(json.dumps(data))
                for col in normalized["columns"]:
                    col.pop("last_value", None)
                for index in normalized["indexes"]:
                    index.pop("index_id", None)
                normalized["indexes"].sort(key=lambda i: i["name"])
                return normalized
            if normalize(expected) != normalize(copied):
                raise ValueError("No coincide la estructura de " + target + "." + table)
            for original, new in zip(expected["columns"], copied["columns"]):
                if not original["is_identity"]:
                    continue
                count = cursor.execute("SELECT COUNT_BIG(*) FROM " + table_name(table)).fetchval()
                next_original = (int(original["last_value"]) + int(original["increment_value"])) if original["last_value"] is not None else int(original["seed_value"])
                # Solo las tablas vacias antes usadas necesitan reseed al siguiente.
                last_expected = original["last_value"]
                if count == 0 and last_expected is not None:
                    last_expected = str(next_original)
                if new["last_value"] != last_expected:
                    raise ValueError("No coincide el contador IDENTITY de " + table)
        expected_fks = [fk for fk in model["foreign_keys"] if fk["local"] and fk["parent_table"] in tables]
        if actual["foreign_keys"] != expected_fks:
            raise ValueError("No coinciden las FK locales de " + target)
        if set(actual["preserved_in_source"]) - set(technical):
            raise ValueError("Hay tablas inesperadas en " + target)
        result.append({"database": target, "table_count": len(tables),
                       "indexes": sum(len(actual["tables"][t]["indexes"]) for t in tables),
                       "checks": sum(len(actual["tables"][t]["checks"]) for t in tables),
                       "local_foreign_keys": len(actual["foreign_keys"]),
                       "structure_matches": True, "identity_matches": True})
    cursor.execute("USE " + quote(source))
    return result


def validate_external_keys(cursor, source, model):
    for fk in model["foreign_keys"]:
        joins = " AND ".join("p." + quote(c["parent_column"]) + "=r." + quote(c["referenced_column"]) for c in fk["columns"])
        not_null = " AND ".join("p." + quote(c["parent_column"]) + " IS NOT NULL" for c in fk["columns"])
        parent = quote(source) + "." + table_name(fk["parent_table"])
        referenced = quote(source) + "." + table_name(fk["referenced_table"])
        sql = f"SELECT COUNT_BIG(*) FROM {parent} p WHERE {not_null} AND NOT EXISTS (SELECT 1 FROM {referenced} r WHERE {joins})"
        if cursor.execute(sql).fetchval():
            raise ValueError("Hay referencias huerfanas en el origen: " + fk["name"])


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    mode = parser.add_mutually_exclusive_group()
    mode.add_argument("--aplicar", action="store_true")
    mode.add_argument("--verificar", action="store_true")
    args = parser.parse_args()
    cfg = configuration()
    source = cfg["DB_NAME"]
    manifest = json.loads((OUTPUT / "distribucion.json").read_text(encoding="utf-8"))
    counts = Counter(t for tables in manifest.values() for t in tables)
    if any(n != 1 for n in counts.values()):
        raise ValueError("Una tabla tiene mas de un propietario.")
    ownership = {t: service for service, tables in manifest.items() for t in tables}
    if source in {"PrograV_" + s for s in manifest}:
        raise ValueError("La base de origen coincide con un destino.")
    with connect(cfg, source, autocommit=True) as connection:
        model = inspect_source(connection.cursor(), ownership)
    OUTPUT.mkdir(exist_ok=True)
    for service, tables in manifest.items():
        statements = schema_statements(model, tables)
        statements += [foreign_key_ddl(f) for f in model["foreign_keys"] if f["local"] and f["parent_table"] in tables]
        text = "-- Estructura del servicio " + service + ". Sin datos ni credenciales.\n"
        text += "-- Crear previamente esta base con la intercalacion del origen: " + model["collation"] + "\n"
        text += "USE " + quote("PrograV_" + service) + "\nGO\n"
        text += "\nGO\n".join(statements) + ("\nGO\n" if statements else "-- Servicio sin tablas propias.\n")
        (OUTPUT / (service + ".sql")).write_text(text, encoding="utf-8")
    create_script = "-- Solo crea las bases que no existen. No elimina ni copia datos.\nUSE [master]\nGO\n"
    for service in manifest:
        target = "PrograV_" + service
        create_script += "IF DB_ID(" + literal(target) + ") IS NULL\n    CREATE DATABASE " + quote(target)
        create_script += " COLLATE " + model["collation"] + ";\nGO\n"
    (OUTPUT / "00_crear_bases.sql").write_text(create_script, encoding="utf-8")
    plan = {"source": source, "databases": {"PrograV_" + k: v for k, v in manifest.items()},
            "collation": model["collation"], "table_count": len(ownership),
            "index_count": sum(len(t["indexes"]) for t in model["tables"].values()),
            "check_count": sum(len(t["checks"]) for t in model["tables"].values()),
            "local_foreign_keys": [f for f in model["foreign_keys"] if f["local"]],
            "external_references": [f for f in model["foreign_keys"] if not f["local"]],
            "preserved_in_source": model["preserved_in_source"]}
    (OUTPUT / "plan.json").write_text(json.dumps(plan, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Plan: {len(manifest)} bases, {len(ownership)} tablas, {plan['index_count']} indices; "
          f"{len(plan['local_foreign_keys'])} FK locales, {len(plan['external_references'])} referencias por API.", flush=True)
    if not args.aplicar and not args.verificar:
        print("Solo inspeccion y archivos DDL. No se modifico SQL Server.")
        return
    if args.aplicar:
        # CREATE DATABASE requiere autocommit. Se rechazan destinos con objetos;
        # si falla la copia, permanecen bases vacias, nunca se borran para reintentar.
        with connect(cfg, "master", autocommit=True) as admin:
            cur = admin.cursor()
            for service in manifest:
                target = "PrograV_" + service
                exists = cur.execute("SELECT DB_ID(?)", target).fetchval()
                if exists:
                    occupied = cur.execute("SELECT COUNT(*) FROM " + quote(target) +
                                           ".sys.objects WHERE is_ms_shipped=0 AND type IN ('U','V','P','FN','IF','TF','SN','SO')").fetchval()
                    if occupied:
                        raise ValueError("Destino con objetos existentes; no se modifica: " + target)
                    collation = cur.execute("SELECT CONVERT(varchar(128),DATABASEPROPERTYEX(?, 'Collation'))", target).fetchval()
                    if collation != model["collation"]:
                        raise ValueError("Destino vacio con intercalacion diferente: " + target)
                else:
                    cur.execute("CREATE DATABASE " + quote(target) + " COLLATE " + model["collation"])
                    print("Creada " + target, flush=True)
    with connect(cfg, source, autocommit=False) as connection:
        cur = connection.cursor()
        cur.execute("SET XACT_ABORT ON; SET LOCK_TIMEOUT 15000; SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;")
        try:
            # Misma conexion y transaccion para 26 lecturas estables y escrituras destino.
            for table in sorted(ownership):
                cur.execute("SELECT COUNT_BIG(*) FROM " + quote(source) + "." + table_name(table) +
                            " WITH (TABLOCK,HOLDLOCK)").fetchval()
            current = inspect_source(cur, ownership)
            if current != model:
                raise ValueError("Cambio el esquema o un contador IDENTITY durante la preparacion. Reintentar.")
            validate_external_keys(cur, source, model)
            if args.aplicar:
                for service, tables in manifest.items():
                    target = "PrograV_" + service
                    cur.execute("USE " + quote(target))
                    for statement in schema_statements(model, tables):
                        cur.execute(statement)
                    for table in tables:
                        metadata = model["tables"][table]
                        columns = ",".join(quote(c["name"]) for c in metadata["columns"])
                        identity = next((c for c in metadata["columns"] if c["is_identity"]), None)
                        if identity:
                            cur.execute("SET IDENTITY_INSERT " + table_name(table) + " ON")
                        cur.execute("INSERT INTO " + table_name(table) + " (" + columns + ") SELECT " + columns +
                                    " FROM " + quote(source) + "." + table_name(table))
                        if identity:
                            cur.execute("SET IDENTITY_INSERT " + table_name(table) + " OFF")
                            if identity["last_value"] is not None:
                                # DBCC con tabla vacia recien creada utiliza el valor como
                                # siguiente ID; en tabla con filas el siguiente es valor+incremento.
                                count = cur.execute("SELECT COUNT_BIG(*) FROM " + table_name(table)).fetchval()
                                reseed = int(identity["last_value"]) + (int(identity["increment_value"]) if count == 0 else 0)
                                cur.execute("DBCC CHECKIDENT (" + literal(table_name(table)) + ", RESEED, " + str(reseed) + ") WITH NO_INFOMSGS")
                    for fk in model["foreign_keys"]:
                        if fk["local"] and fk["parent_table"] in tables:
                            cur.execute(foreign_key_ddl(fk))
                            if fk["is_disabled"]:
                                cur.execute("ALTER TABLE " + table_name(fk["parent_table"]) + " NOCHECK CONSTRAINT " + quote(fk["name"]))
                    print("Copiado " + target, flush=True)
            cur.execute("USE " + quote(source))
            verified = verify(cur, source, manifest, ownership, model)
            structure = verify_schema(cur, source, manifest, model)
            connection.commit()
        except BaseException:
            connection.rollback()
            print("Transaccion revertida. No se modificaron filas del origen.", flush=True)
            raise
    report = {"verified_at_utc": datetime.now(timezone.utc).isoformat(),
              "mode": "copy_and_verify" if args.aplicar else "verify_only", "source": source,
              "source_rows_modified": False, "tables": verified,
              "databases": structure,
              "total_rows": sum(t["rows"] for t in verified),
              "business_table_count": len(verified),
              "source_references_checked": len(model["foreign_keys"]),
              "application_cutover": False,
              "note": "Verifica las 26 tablas migradas. La tabla tecnica OperacionPago se comprueba en la suite de integracion."}
    report_path = OUTPUT / ("resultado-migracion.json" if args.aplicar else "resultado-verificacion.json")
    report_path.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Verificacion binaria correcta: {len(verified)} tablas, {report['total_rows']} filas. Origen conservado.")


if __name__ == "__main__":
    try:
        main()
    except pyodbc.Error as error:
        # Los mensajes ODBC pueden incluir servidor, SQL y valores de datos. No exponerlos.
        print("Operacion SQL fallida. SQLSTATE: " + str(error.args[0]) +
              ". Revisar permisos, conectividad, restricciones o concurrencia; no se imprimen datos sensibles.", file=sys.stderr)
        sys.exit(1)
    except (ValueError, OSError) as error:
        print(str(error), file=sys.stderr)
        sys.exit(1)
