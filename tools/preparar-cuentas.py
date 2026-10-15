"""Crea cuentas SQL de ejecucion con acceso exclusivo a su propia base.

Ejecutar con una cuenta administradora en DB_USERNAME/DB_PASSWORD del proceso.
Las claves de servicios se guardan solo en .env; nunca se imprimen.
Sin --aplicar comprueba permisos y no crea logins.
"""
import argparse
import importlib.util
import json
from pathlib import Path
import secrets
import sys
import re

ROOT=Path(__file__).resolve().parent.parent
spec=importlib.util.spec_from_file_location("migration",ROOT/"tools/migrar-bases.py")
m=importlib.util.module_from_spec(spec);spec.loader.exec_module(m)

def main():
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument("--aplicar",action="store_true");args=parser.parse_args()
    cfg=m.configuration()
    distribution=json.loads((ROOT/"db/por_servicio/distribucion.json").read_text())
    with m.connect(cfg,"master",autocommit=True) as admin:
        cursor=admin.cursor()
        permission=cursor.execute("SELECT HAS_PERMS_BY_NAME(NULL,NULL,'ALTER ANY LOGIN')").fetchval()
        if not permission:
            print("La cuenta actual no tiene ALTER ANY LOGIN. Ejecutar este script con una cuenta administradora; no se modificaron permisos.")
            return 2
        if not args.aplicar:
            print("Permiso comprobado. --aplicar creara 22 cuentas sin acceso a bases ajenas.");return 0
        env_path=ROOT/".env";env=env_path.read_text(encoding="utf-8-sig")
        for key in ["SERVICES_INTERNAL_KEY","JWT_KEY"]:
            if not cfg.get(key):
                cfg[key]=secrets.token_urlsafe(48)
                env+="\n"+key+"="+cfg[key]+"\n"
                env_path.write_text(env,encoding="utf-8")
        # Infraestructura tecnica autorizada, idempotente y sin cambios en filas migradas.
        with m.connect(cfg,"PrograV_Pago",autocommit=False) as payments:
            script=(ROOT/"db/por_servicio/migraciones/001_operaciones_pago.sql").read_text(encoding="utf-8")
            for batch in re.split(r"(?m)^GO\s*$",script):
                if batch.strip():payments.cursor().execute(batch)
            payments.commit()
        for service in distribution:
            login="svc_avatar_"+service.lower();database="PrograV_"+service
            user_key="SERVICE_DB_USER_"+service.upper();password_key="SERVICE_DB_PASSWORD_"+service.upper()
            if cursor.execute("SELECT COUNT(*) FROM sys.server_principals WHERE name=?",login).fetchval():
                if not cfg.get(password_key):raise ValueError("Cuenta existente sin clave local: "+login)
                password=cfg[password_key]
            else:
                password=cfg.get(password_key) or secrets.token_urlsafe(48)
                # Persistir antes del DDL para poder recuperar una ejecucion interrumpida.
                if password_key not in cfg:
                    env+="\n"+user_key+"="+login+"\n"+password_key+"="+password+"\n";env_path.write_text(env,encoding="utf-8");cfg[password_key]=password
                cursor.execute("CREATE LOGIN "+m.quote(login)+" WITH PASSWORD="+m.literal(password)+", DEFAULT_DATABASE="+m.quote(database)+", CHECK_POLICY=ON")
            cursor.execute("USE "+m.quote(database))
            if not cursor.execute("SELECT USER_ID(?)",login).fetchval():cursor.execute("CREATE USER "+m.quote(login)+" FOR LOGIN "+m.quote(login))
            for role in ["db_datareader","db_datawriter"]:
                cursor.execute("ALTER ROLE "+m.quote(role)+" ADD MEMBER "+m.quote(login))
            # No se concede db_owner, DDL ni membresia en otras bases.
            cursor.execute("USE [master]")
        print("Cuentas creadas; claves guardadas solo en .env. Verificar aislamiento antes del arranque.")
        return 0

if __name__=="__main__":
    try:sys.exit(main())
    except m.pyodbc.Error as ex:print("Fallo SQL al preparar cuentas. SQLSTATE: "+str(ex.args[0]),file=sys.stderr);sys.exit(1)
    except ValueError as ex:print(str(ex),file=sys.stderr);sys.exit(1)
