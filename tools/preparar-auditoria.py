"""Prepara únicamente la infraestructura de auditoría con la cuenta administrativa configurada."""
from pathlib import Path
import json
import os
import pyodbc

ROOT = Path(__file__).resolve().parent.parent


def cargar_entorno():
    for line in (ROOT / '.env').read_text(encoding='utf-8-sig').splitlines():
        line = line.strip()
        if not line or line.startswith('#') or '=' not in line:
            continue
        name, value = line.split('=', 1)
        value = value.strip()
        if value[:1] in ('"', "'"):
            value = value[1:].split(value[0], 1)[0]
        else:
            value = value.split('#', 1)[0].strip()
        os.environ.setdefault(name.strip(), value)


def valor_odbc(value):
    return '{' + value.replace('}', '}}') + '}'


def identificador(value):
    return '[' + value.replace(']', ']]') + ']'


def preparar():
    cargar_entorno()
    driver = next(d for d in reversed(pyodbc.drivers()) if 'SQL Server' in d)
    usuario = os.environ.get('AUDITORIA_ADMIN_USER') or os.environ['DB_USERNAME']
    password = os.environ.get('AUDITORIA_ADMIN_PASSWORD') or os.environ['DB_PASSWORD']
    origen = os.environ['DB_HOST'] + ',' + os.environ.get('DB_PORT', '1433')
    servicios = json.loads((ROOT / 'tools/servicios.json').read_text())
    for servicio in servicios:
        if servicio['kind'] not in ('java', 'dotnet') or not servicio.get('database'):
            continue
        cadena = ('DRIVER=' + valor_odbc(driver) + ';SERVER=' + origen + ';DATABASE=' + valor_odbc(servicio['database'])
                  + ';UID=' + valor_odbc(usuario) + ';PWD=' + valor_odbc(password) + ';Encrypt=yes;TrustServerCertificate=yes;')
        with pyodbc.connect(cadena, autocommit=True, timeout=15) as conexion:
            cursor = conexion.cursor()
            receptor = servicio['name'] == 'Bitacora'
            archivo = ROOT / ('db/migraciones/bitacora-entrega-fiable.sql' if receptor else 'integracion/auditoria-pendiente.sql')
            cursor.execute(archivo.read_text(encoding='utf-8'))
            if not receptor:
                login = os.environ.get('SERVICE_DB_USER_' + servicio['name'].upper()) or os.environ['DB_USERNAME']
                principal = cursor.execute('SELECT name FROM sys.database_principals WHERE sid=SUSER_SID(?)', login).fetchone()
                if principal is None:
                    raise RuntimeError('No se encontró el usuario de ejecución de ' + servicio['name'])
                cursor.execute('GRANT SELECT,INSERT,UPDATE,DELETE ON OBJECT::general.auditoria_pendiente TO ' + identificador(principal[0]))
        print('Auditoria preparada:', servicio['name'])


if __name__ == '__main__':
    preparar()
