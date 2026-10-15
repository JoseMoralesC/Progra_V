"""Audita o habilita las cuatro cuentas humanas en las bases separadas.

Sin argumentos solo verifica. --aplicar agrega usuarios, permisos de datos y
ejecucion; no cambia contrasenas, permisos del origen ni roles del servidor.
"""
import argparse
from datetime import datetime, timezone
import importlib.util
import json
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parent.parent
spec = importlib.util.spec_from_file_location("migration", ROOT / "tools/migrar-bases.py")
m = importlib.util.module_from_spec(spec)
spec.loader.exec_module(m)
USERS = ("jose", "ramses", "fabian", "alejandro")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--aplicar", action="store_true")
    args = parser.parse_args()
    distribution = json.loads((ROOT / "db/por_servicio/distribucion.json").read_text())
    cfg = m.configuration()
    checks = []
    logins = []
    login_status = []
    database_tables = {}
    with m.connect(cfg, "master", autocommit=False) as connection:
        cursor = connection.cursor()
        # Verificar todos los destinos y mapeos antes de cambiar permisos.
        for name in USERS:
            principal = cursor.execute(
                "SELECT name,sid,is_disabled FROM sys.server_principals "
                "WHERE LOWER(name)=? AND type='S'", name).fetchone()
            if not principal or principal[2]:
                raise ValueError("Cuenta inexistente o deshabilitada: " + name)
            logins.append((principal[0], principal[1]))
            status = cursor.execute("SELECT CONVERT(int,LOGINPROPERTY(?,'IsLocked')),CONVERT(int,LOGINPROPERTY(?,'IsExpired')),CONVERT(int,LOGINPROPERTY(?,'IsMustChange'))", principal[0], principal[0], principal[0]).fetchone()
            if any(value == 1 for value in status):
                raise ValueError("La cuenta requiere resolver bloqueo o vencimiento de clave: " + name)
            login_status.append({"login": principal[0], "enabled": True, "locked": bool(status[0]), "expired": bool(status[1]), "must_change_password": bool(status[2])})
        for service in distribution:
            database = "PrograV_" + service
            if cursor.execute("SELECT state_desc FROM sys.databases WHERE name=?", database).fetchval() != "ONLINE":
                raise ValueError("Base no disponible: " + database)
            cursor.execute("USE " + m.quote(database))
            # Inventario completo con la cuenta administradora, incluso si un
            # DENY oculta alguna tabla al usuario que vamos a comprobar.
            database_tables[database] = cursor.execute("SELECT s.name,t.name FROM sys.tables t JOIN sys.schemas s ON s.schema_id=t.schema_id WHERE t.is_ms_shipped=0").fetchall()
            for name, sid in logins:
                existing = cursor.execute("SELECT sid FROM sys.database_principals WHERE name=?", name).fetchval()
                if existing is not None and existing != sid:
                    raise ValueError("Usuario existente asociado a otra cuenta: " + database + "/" + name)
                alias = cursor.execute("SELECT name FROM sys.database_principals WHERE sid=? AND name<>?", sid, name).fetchval()
                if alias and alias != "dbo":
                    raise ValueError("La cuenta tiene otro nombre de usuario en " + database + ": " + name)
        if args.aplicar:
            for service in distribution:
                cursor.execute("USE " + m.quote("PrograV_" + service))
                for name, sid in logins:
                    if cursor.execute("SELECT name FROM sys.database_principals WHERE sid=?", sid).fetchval() == "dbo":
                        continue  # El propietario ya tiene acceso completo; conservar su mapeo.
                    if cursor.execute("SELECT USER_ID(?)", name).fetchval() is None:
                        cursor.execute("CREATE USER " + m.quote(name) + " FOR LOGIN " + m.quote(name))
                    for role in ("db_datareader", "db_datawriter"):
                        cursor.execute("ALTER ROLE " + m.quote(role) + " ADD MEMBER " + m.quote(name))
                    cursor.execute("GRANT CONNECT, EXECUTE TO " + m.quote(name))
            connection.commit()
        else:
            connection.rollback()
        # SQL Server evalua el contexto de cada login sin conocer su contrasena.
        for name, _ in logins:
            cursor.execute("USE [master]")
            cursor.execute("EXECUTE AS LOGIN = " + m.literal(name))
            try:
                server_connect = bool(cursor.execute("SELECT HAS_PERMS_BY_NAME(NULL,NULL,'CONNECT SQL')").fetchval())
                for service in distribution:
                    database = "PrograV_" + service
                    access = bool(cursor.execute("SELECT HAS_DBACCESS(?)", database).fetchval())
                    row = {"login": name, "database": database, "server_connect": server_connect, "database_access": access}
                    if access:
                        cursor.execute("USE " + m.quote(database))
                        permissions = cursor.execute("SELECT IS_MEMBER('db_datareader'),IS_MEMBER('db_datawriter'),HAS_PERMS_BY_NAME(DB_NAME(),'DATABASE','EXECUTE'),IS_MEMBER('db_owner'),USER_NAME()").fetchone()
                        row.update(datareader=bool(permissions[0]), datawriter=bool(permissions[1]), execute=bool(permissions[2]), database_owner=bool(permissions[3]), database_user=permissions[4])
                        tables = database_tables[database]
                        row["tables_checked"] = len(tables)
                        missing = []
                        for schema, table in tables:
                            object_name = m.quote(schema) + "." + m.quote(table)
                            for permission in ("SELECT", "INSERT", "UPDATE", "DELETE"):
                                if cursor.execute("SELECT HAS_PERMS_BY_NAME(?,'OBJECT',?)", object_name, permission).fetchval() != 1:
                                    missing.append({"table": schema + "." + table, "permission": permission})
                        row["missing_table_permissions"] = missing
                    row["passed"] = server_connect and access and (row.get("datareader", False) or row.get("database_owner", False)) and (row.get("datawriter", False) or row.get("database_owner", False)) and row.get("execute", False) and not row.get("missing_table_permissions", [])
                    checks.append(row)
                    cursor.execute("USE [master]")
            finally:
                cursor.execute("USE [master]")
                cursor.execute("REVERT")
        connection.rollback()
    report = {"verified_at_utc": datetime.now(timezone.utc).isoformat(), "mode": "apply_and_verify" if args.aplicar else "verify_only", "passed": all(c["passed"] for c in checks), "passwords_changed": False, "business_rows_modified": False, "server_roles_changed": False, "source_permissions_changed": False, "login_status": login_status, "checks": checks}
    destination = ROOT / "docs/pruebas/usuarios-remotos.json"
    destination.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    for name in USERS:
        own = [c for c in checks if c["login"].lower() == name]
        print(name + ": " + str(sum(c["passed"] for c in own)) + "/" + str(len(own)) + " bases verificadas")
    return 0 if report["passed"] else 1


if __name__ == "__main__":
    try:
        sys.exit(main())
    except m.pyodbc.Error as error:
        print("Fallo SQL. SQLSTATE: " + str(error.args[0]) + ". Revisar conectividad y permisos administrativos.", file=sys.stderr)
        sys.exit(1)
    except ValueError as error:
        print(str(error), file=sys.stderr)
        sys.exit(1)
