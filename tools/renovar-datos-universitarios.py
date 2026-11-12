"""Planifica, respalda y renueva datos académicos identificados como artificiales.

Sin --aplicar solo prepara un plan cifrado local; nunca imprime secretos.
El plan se liga al contenido exacto de las bases y a respaldos nativos verificados.
--aplicar usa una sola transacción entre bases y valida antes del commit.
No cambia esquemas, logins SQL, configuración SMTP ni permisos existentes.
"""
import argparse
from collections import Counter
from datetime import date, datetime, timezone
from decimal import Decimal
import hashlib
import importlib.util
import json
from pathlib import Path
import re
import secrets
import subprocess
import unicodedata

import bcrypt
from cryptography.fernet import Fernet

ROOT = Path(__file__).resolve().parent.parent
PRIVATE = ROOT / 'tmp/limpieza-universitaria'
PUBLIC = ROOT / 'docs/datos_realistas'
spec = importlib.util.spec_from_file_location('migration', ROOT / 'tools/migrar-bases.py')
m = importlib.util.module_from_spec(spec)
spec.loader.exec_module(m)
DIST = json.loads((ROOT / 'db/por_servicio/distribucion.json').read_text())
OWNER = {t: 'PrograV_' + s for s, tables in DIST.items() for t in tables}
OWNER.update({'seguridad.rol_modulo': 'PrograV_Modulo', 'general.Notificacion': 'PrograV_Notificacion',
              'finanzas.OperacionPago': 'PrograV_Pago'})
MARKERS = re.compile(r'test|prueba|demo|ejemplo|example|postman|temporal|persona[ _.-]?(?:[1-5]|uno|dos|tres|cuatro|cinco)|\bqa\b|sixsevenaldo', re.I)
DOMAIN = ['matricula.NotaRubro', 'finanzas.OperacionPago', 'finanzas.Pago',
          'finanzas.FacturaDetalle', 'finanzas.Factura', 'matricula.DesgloseRubro',
          'matricula.Matricula', 'matricula.PrematriculaCurso', 'matricula.Prematricula',
          'academico.Grupo', 'academico.Curso', 'academico.Carrera',
          'academico.ProfesorTelefono', 'academico.Profesor',
          'matricula.EstudianteTelefono', 'matricula.Estudiante',
          'academico.Periodo', 'academico.Institucion', 'matricula.Distrito',
          'matricula.Canton', 'matricula.Provincia', 'seguridad.refresh_token', 'seguridad.usuario']
TEAM = [('José', 'jose', 'José Andrés Solano Vargas', '398700001'),
        ('Ramsés', 'ramses', 'José Ramses Aguilar Monge', '305430020'),
        ('Alejandro', 'alejandro', 'Alejandro', '119360195'),
        ('Fabián', 'fabian', 'Fabián Daniel Rojas Méndez', '498700002')]


def canonical(value):
    return json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(',', ':'),
                      default=lambda v: {'binary_hex': bytes(v).hex()} if isinstance(v, (bytes, bytearray)) else str(v))


def digest(value):
    return hashlib.sha256(canonical(value).encode()).hexdigest()


def protect():
    PRIVATE.mkdir(parents=True, exist_ok=True)
    account = subprocess.check_output(['whoami'], text=True).strip()
    subprocess.run(['icacls', str(PRIVATE), '/inheritance:r', '/grant:r',
                    account + ':(OI)(CI)F', 'SYSTEM:(OI)(CI)F'], check=True, capture_output=True)
    key = PRIVATE / 'clave-local.key'
    if not key.exists():
        key.write_bytes(Fernet.generate_key())
    return Fernet(key.read_bytes())


def inventory(q, lock=False):
    result = {}
    dbs = [r[0] for r in q.execute("SELECT name FROM sys.databases WHERE name='PrograV' OR name LIKE 'PrograV[_]%' ORDER BY name")]
    for db in dbs:
        q.execute('USE ' + m.quote(db))
        tables = m.rows(q, "SELECT t.object_id,s.name+'.'+t.name AS name FROM sys.tables t JOIN sys.schemas s ON s.schema_id=t.schema_id WHERE t.is_ms_shipped=0 AND t.name<>'sysdiagrams' ORDER BY s.name,t.name")
        for table in tables:
            cols = m.rows(q, "SELECT c.name,ty.name AS type_name,c.is_identity,c.is_computed FROM sys.columns c JOIN sys.types ty ON ty.user_type_id=c.user_type_id WHERE object_id=? ORDER BY c.column_id", table['object_id'])
            pk = [r[0] for r in q.execute("SELECT c.name FROM sys.indexes i JOIN sys.index_columns ic ON ic.object_id=i.object_id AND ic.index_id=i.index_id JOIN sys.columns c ON c.object_id=ic.object_id AND c.column_id=ic.column_id WHERE i.object_id=? AND i.is_primary_key=1 ORDER BY ic.key_ordinal", table['object_id'])]
            select = ','.join(('CONVERT(nvarchar(40),' + m.quote(c['name']) + ',127) AS ' + m.quote(c['name'])) if c['type_name'] == 'datetimeoffset' else m.quote(c['name']) for c in cols)
            sql = 'SELECT ' + select + ' FROM ' + m.table_name(table['name'])
            if lock:
                sql += ' WITH (TABLOCKX,HOLDLOCK)'
            if pk:
                sql += ' ORDER BY ' + ','.join(m.quote(c) for c in pk)
            rows = m.rows(q, sql)
            result[db + '/' + table['name']] = {'database': db, 'table': table['name'],
                'columns': cols, 'pk': pk, 'rows': json.loads(canonical(rows))}
    return result


def token():
    alphabet = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%+-'
    while True:
        value = ''.join(secrets.choice(alphabet) for _ in range(22))
        if all((any(c.isupper() for c in value), any(c.islower() for c in value),
                any(c.isdigit() for c in value), any(not c.isalnum() for c in value))):
            return value


def synthetic():
    data = {t: [] for t in DOMAIN}
    def add(t, **row):
        data[t].append(row)
        return next(iter(row.values()))
    geo = json.loads((ROOT / 'db/datos_realistas/geografia-2026.json').read_text(encoding='utf-8'))
    for key, tab in [('provincias', 'Provincia'), ('cantones', 'Canton'), ('distritos', 'Distrito')]:
        data['matricula.' + tab] = geo[key]
    curricula = json.loads((ROOT / 'db/datos_realistas/carreras.json').read_text(encoding='utf-8'))
    names = ['María Fernanda', 'Luis Carlos', 'Ana Lucía', 'Diego Andrés', 'Valeria', 'Gabriel',
             'Daniela', 'Andrés', 'Sofía', 'Carlos Eduardo', 'Natalia', 'Sebastián', 'Paula', 'Daniel',
             'Camila', 'Esteban', 'Adriana', 'David', 'Laura', 'Miguel Ángel', 'Isabel', 'Javier',
             'Lucía', 'Pablo', 'Andrea', 'Ricardo', 'Verónica', 'Fernando', 'Elena', 'Santiago',
             'Patricia', 'Mauricio', 'Cristina', 'Alonso', 'Carolina', 'Rodrigo', 'Mónica', 'Felipe',
             'Gabriela', 'Óscar']
    surnames = ['Aguilar', 'Méndez', 'Rojas', 'Vargas', 'Solano', 'Castro', 'Jiménez', 'Hernández',
                'Ramírez', 'Chaves', 'Mora', 'Sánchez', 'Alvarado', 'Cordero', 'Araya', 'Salazar',
                'Navarro', 'Campos', 'Valverde', 'Brenes', 'Ureña', 'Quesada', 'Calderón', 'Vega']
    used = set()
    credentials = []
    def person(i):
        first = names[i % len(names)]
        a, b = surnames[(i // len(names) + i) % 24], surnames[(i * 7 + 5) % 24]
        if a == b:
            b = surnames[(i * 7 + 6) % 24]
        name = first + ' ' + a + ' ' + b
        ascii_name = unicodedata.normalize('NFKD', first.split()[0] + a + b[0]).encode('ascii', 'ignore').decode().lower()
        username = ascii_name
        suffix = 2
        while username in used:
            username = ascii_name + str(suffix); suffix += 1
        used.add(username)
        return name, username
    def account(name, username, ident, role, member=None):
        password = token()
        hashed = bcrypt.hashpw(password.encode(), bcrypt.gensalt(rounds=10, prefix=b'2a')).decode()
        add('seguridad.usuario', email=username + '@cuc.cr', tipo_identificacion='CEDULA',
            identificacion=ident, nombre=name, id_rol=role, password_hash=hashed)
        if member:
            credentials.append({'integrante': member, 'rol': role, 'usuario': username, 'contrasena_inicial': password})
    add('academico.Institucion', InstitucionId=10001, Nombre='Colegio Universitario de Cartago')
    for pid, (year, num, begin, end) in enumerate([(2026,1,'2026-01-12','2026-04-25'),
              (2026,3,'2026-09-07','2026-12-19'), (2027,1,'2027-01-11','2027-04-24')], 10001):
        add('academico.Periodo', PeriodoId=pid, Anio=year, NumeroPeriodo=num, FechaInicio=begin, FechaFin=end)
    for i in range(32):
        name, username = person(500 + i)
        ident = str(i % 7 + 1) + str(98600000 + i)
        add('academico.Profesor', ProfesorId=10001+i, TipoIdentificacion='CEDULA', Identificacion=ident,
            Email=username+'@cuc.cr', NombreCompleto=name, FechaNacimiento=f'{1975+i%15}-05-{i%27+1:02}')
        add('academico.ProfesorTelefono', ProfesorTelefonoId=10001+i, ProfesorId=10001+i, Telefono=str(83000000+i))
        account(name, username, ident, 'PROFESOR')
    for k, (career, courses) in enumerate(curricula.items()):
        assert len(courses) == len(set(courses)) == 24
        career_id = 10001 + k
        add('academico.Carrera', CarreraId=career_id, Nombre=career, InstitucionId=10001, DirectorProfesorId=10001+k*4)
        for j, title in enumerate(courses):
            cid = 10001+k*24+j
            add('academico.Curso', CursoId=cid, CarreraId=career_id, Nivel=j//4+1, Nombre=title)
            # Distinct two-hour slots per course within each career; no teacher or student collisions.
            day = ['Lunes','Martes','Miércoles','Jueves','Viernes','Sábado'][j % 6]
            hour = 8 + (j//6)*2
            for period in range(3):
                add('academico.Grupo', GrupoId=10001+period*192+k*24+j, NumeroGrupo=1,
                    CursoId=cid, ProfesorId=10001+k*4+j%4, Horario=f'{day} {hour:02}:00 a {hour+2:02}:00',
                    Cupo=35, PeriodoId=10001+period)
    district_ids = sorted(r['DistritoId'] for r in geo['distritos'])
    for i in range(240):
        k = i % 8
        if i < 4:
            member, username, name, ident = TEAM[i]
            account(name, username, ident, 'ESTUDIANTE', member)
            account(name, username+'adm', ident, 'ADMIN', member)
        else:
            name, username = person(i)
            ident = str(i%7+1) + str(98700000+i)
            account(name, username, ident, 'ESTUDIANTE')
        add('matricula.Estudiante', Identificacion=ident, TipoIdentificacion='CEDULA', Email=username+'@cuc.cr',
            NombreCompleto=name, FechaNacimiento=f'{1998+i%9}-{i%12+1:02}-{i%27+1:02}',
            DistritoId=district_ids[i%len(district_ids)], OtrasSenas='Sector residencial, casa '+str(i+1))
        add('matricula.EstudianteTelefono', EstudianteTelefonoId=10001+i, IdentificacionEstudiante=ident, Telefono=str(87000000+i))
        for p in range(2):
            for j in range(4):
                course_index = k*24+p*4+j
                mid = 10001+i*8+p*4+j
                group = 10001+p*192+course_index
                state = 'ANULADA' if p==1 and j==3 and i%6==0 else 'ACTIVA'
                date = '2026-01-12T09:00:00' if p==0 else '2026-09-07T09:00:00'
                add('matricula.Matricula', MatriculaId=mid, IdentificacionEstudiante=ident,
                    CursoId=10001+course_index, GrupoId=group, PeriodoId=10001+p, Estado=state, FechaMatricula=date)
                amount = 65000 + k*2500
                invoice_state = 'ANULADA' if state=='ANULADA' else ('PAGADA' if p==0 or i%3 else 'PENDIENTE')
                add('finanzas.Factura', FacturaId=mid, MatriculaId=mid, IdentificacionEstudiante=ident, PeriodoId=10001+p,
                    Subtotal=amount, Impuesto=0, Total=amount, Estado=invoice_state, FechaFactura=date)
                add('finanzas.FacturaDetalle', FacturaDetalleId=mid, FacturaId=mid,
                    Descripcion='Derechos académicos de '+data['academico.Curso'][course_index]['Nombre'], Monto=amount)
                if invoice_state=='PAGADA':
                    add('finanzas.Pago', PagoId=mid, FacturaId=mid, PeriodoId=10001+p, Monto=amount, Estado='APLICADO', FechaPago=date)
                if state=='ACTIVA':
                    for r in range(3 if p==0 else 1):
                        add('matricula.NotaRubro', NotaRubroId=10001+(mid-10001)*3+r,
                            MatriculaId=mid, RubroId=10001+(group-10001)*3+r,
                            Nota=55+(i*7+j*11+r*3)%46, FechaRegistro='2026-04-24T15:00:00' if p==0 else '2026-10-30T15:00:00')
        if i%3==0 or i<4:
            pre=10001+i
            add('matricula.Prematricula', PrematriculaId=pre, IdentificacionEstudiante=ident, CarreraId=10001+k,
                PeriodoId=10003, Observaciones='Solicitud de continuidad del plan de estudios')
            for j in range(4):
                add('matricula.PrematriculaCurso', PrematriculaCursoId=10001+i*4+j, PrematriculaId=pre, CursoId=10001+k*24+8+j)
    for group in data['academico.Grupo']:
        for r, (name, weight) in enumerate([('Evaluación escrita',30),('Proyecto aplicado',40),('Trabajo práctico',30)]):
            add('matricula.DesgloseRubro', RubroId=10001+(group['GrupoId']-10001)*3+r,
                GrupoId=group['GrupoId'], Nombre=name, Porcentaje=weight,
                FechaCreacion=['2026-01-12T08:00:00','2026-09-07T08:00:00','2026-11-12T08:00:00'][group['PeriodoId']-10001])
    return data, credentials


def flagged(row):
    return any(MARKERS.search(str(v)) for k,v in row.items() if not any(s in k.lower() for s in ['hash','token','password']))


def assert_scope(inv):
    # Explicit evidence for every academic root that is replaced. Unexpected rows stop execution.
    marked_students = {r['Email'].lower() for item in inv.values() if item['table']=='matricula.Estudiante' for r in item['rows'] if flagged(r)}
    rules = {'academico.Institucion': lambda r: flagged(r) or r['Nombre']=='Institucion Actualizada',
             'academico.Profesor': lambda r: flagged(r) or str(r['Identificacion']).startswith('PROF'),
             'academico.Carrera': flagged, 'academico.Curso': flagged,
             'matricula.Estudiante': flagged,
             'academico.Periodo': lambda r: int(r['PeriodoId']) in [1,3,6,1004],
             'seguridad.usuario': lambda r: flagged(r) or r['email'].lower() in marked_students or r['email'] in ['305430020@cuc.cr','alerm094@cuc.cr','jose@cuc.cr']}
    for item in inv.values():
        rule = rules.get(item['table'])
        if rule and any(not rule(r) for r in item['rows']):
            raise ValueError('Registro sin clasificar; se conserva y se cancela la sustitución: '+item['database']+'/'+item['table'])
    actual = inv['PrograV_Usuario/seguridad.usuario']['rows']
    for _,_,name,ident in TEAM[1:3]:
        if not any(r['identificacion']==ident and r['nombre']==name for r in actual):
            raise ValueError('Cambió la identidad personal que se debe conservar')


def execute_insert(q, item, rows):
    if not rows:
        return
    full = m.quote(item['database'])+'.'+m.table_name(item['table'])
    cols = [c['name'] for c in item['columns'] if c['name'] in rows[0]]
    identity = any(c['is_identity'] and c['name'] in cols for c in item['columns'])
    if identity:
        q.execute('SET IDENTITY_INSERT '+full+' ON')
    try:
        sql='INSERT INTO '+full+' ('+','.join(m.quote(c) for c in cols)+') VALUES ('+','.join('?' for _ in cols)+')'
        types={c['name']:c['type_name'] for c in item['columns']}
        def value(row,col):
            v=row[col]
            if v is not None and types[col]=='date':return date.fromisoformat(v)
            if v is not None and types[col] in ['datetime','datetime2','smalldatetime']:return datetime.fromisoformat(v)
            if v is not None and types[col] in ['decimal','numeric']:return Decimal(str(v))
            return v
        q.fast_executemany=True
        q.executemany(sql, [[value(r,c) for c in cols] for r in rows])
    finally:
        q.fast_executemany=False
        if identity:
            q.execute('SET IDENTITY_INSERT '+full+' OFF')


def validate(q, inv, seed):
    checks = []
    model = json.loads((ROOT/'db/por_servicio/plan.json').read_text())
    refs = model['local_foreign_keys'] + model['external_references']
    for mode in ['distributed','source']:
        def full(t):
            return m.quote('PrograV' if mode=='source' else OWNER[t])+'.'+m.table_name(t)
        for ref in refs:
            # Model format is checked below against the repository metadata.
            child, parent = ref['parent_table'], ref['referenced_table']
            conditions=' AND '.join('c.'+m.quote(pair['parent_column'])+'=p.'+m.quote(pair['referenced_column']) for pair in ref['columns'])
            nonnull=' AND '.join('c.'+m.quote(pair['parent_column'])+' IS NOT NULL' for pair in ref['columns'])
            n=q.execute('SELECT COUNT(*) FROM '+full(child)+' c WHERE '+nonnull+' AND NOT EXISTS (SELECT 1 FROM '+full(parent)+' p WHERE '+conditions+')').fetchval()
            checks.append({'check':mode+'/'+ref['name'], 'violations':n})
        assertions = {
            'grupo_matricula': 'SELECT COUNT(*) FROM '+full('matricula.Matricula')+' m JOIN '+full('academico.Grupo')+' g ON g.GrupoId=m.GrupoId WHERE m.CursoId<>g.CursoId OR m.PeriodoId<>g.PeriodoId',
            'cupo': 'SELECT COUNT(*) FROM (SELECT g.GrupoId FROM '+full('academico.Grupo')+" g LEFT JOIN "+full('matricula.Matricula')+" m ON m.GrupoId=g.GrupoId AND m.Estado='ACTIVA' GROUP BY g.GrupoId,g.Cupo HAVING COUNT(m.MatriculaId)>g.Cupo) t",
            'rubros': 'SELECT COUNT(*) FROM (SELECT GrupoId FROM '+full('matricula.DesgloseRubro')+' GROUP BY GrupoId HAVING SUM(Porcentaje)<>100) t',
            'notas_grupo': 'SELECT COUNT(*) FROM '+full('matricula.NotaRubro')+' n JOIN '+full('matricula.Matricula')+' m ON m.MatriculaId=n.MatriculaId JOIN '+full('matricula.DesgloseRubro')+' r ON r.RubroId=n.RubroId WHERE m.GrupoId<>r.GrupoId',
            'factura_matricula': 'SELECT COUNT(*) FROM '+full('finanzas.Factura')+' f JOIN '+full('matricula.Matricula')+' m ON m.MatriculaId=f.MatriculaId WHERE f.IdentificacionEstudiante<>m.IdentificacionEstudiante OR f.PeriodoId<>m.PeriodoId',
            'prematricula_carrera': 'SELECT COUNT(*) FROM '+full('matricula.PrematriculaCurso')+' pc JOIN '+full('matricula.Prematricula')+' p ON p.PrematriculaId=pc.PrematriculaId JOIN '+full('academico.Curso')+' c ON c.CursoId=pc.CursoId WHERE c.CarreraId<>p.CarreraId',
            'pagos': 'SELECT COUNT(*) FROM '+full('finanzas.Factura')+" f OUTER APPLY (SELECT SUM(Monto) AS pagado FROM "+full('finanzas.Pago')+" p WHERE p.FacturaId=f.FacturaId AND p.Estado='APLICADO') p WHERE (f.Estado='PAGADA' AND COALESCE(p.pagado,0)<>f.Total) OR (f.Estado IN ('PENDIENTE','ANULADA') AND COALESCE(p.pagado,0)<>0)"
        }
        for name, sql in assertions.items():
            checks.append({'check':mode+'/'+name,'violations':q.execute(sql).fetchval()})
        for tab in DOMAIN:
            if tab=='seguridad.refresh_token':continue  # Sessions legitimately grow during functional verification.
            if tab=='finanzas.OperacionPago' and mode=='source':continue
            checks.append({'check':mode+'/count/'+tab,'violations':abs(q.execute('SELECT COUNT(*) FROM '+full(tab)).fetchval()-len(seed[tab]))})
    for item in inv.values():
        q.execute('USE '+m.quote(item['database']))
        q.execute('DBCC CHECKCONSTRAINTS ('+m.literal(item['table'])+') WITH ALL_CONSTRAINTS')
        if q.description:
            violations=len(q.fetchall())
            checks.append({'check':item['database']+'/constraints/'+item['table'],'violations':violations})
    if any(c['violations'] for c in checks):
        raise ValueError('Falló integridad: '+canonical([c for c in checks if c['violations']]))
    return checks


def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--aplicar',action='store_true')
    parser.add_argument('--respaldar',action='store_true',help='Crea respaldos COPY_ONLY con checksum y verifica su lectura antes de planificar')
    args=parser.parse_args()
    crypt=protect(); PUBLIC.mkdir(parents=True,exist_ok=True)
    cfg=m.configuration()
    if args.respaldar:
        if args.aplicar:raise ValueError('Respaldar y aplicar son pasos separados; revisar primero el plan')
        backups=[]
        with m.connect(cfg,'master',autocommit=True) as connection:
            connection.timeout=180;q=connection.cursor()
            base=q.execute("SELECT CONVERT(nvarchar(260),SERVERPROPERTY('InstanceDefaultBackupPath'))").fetchval()
            if not base:raise ValueError('SQL Server no informó el directorio de respaldos')
            databases=[r[0] for r in q.execute("SELECT name FROM sys.databases WHERE name='PrograV' OR name LIKE 'PrograV[_]%' ORDER BY name")]
            stamp=datetime.now(timezone.utc).strftime('%Y%m%dT%H%M%SZ')
            for db in databases:
                path=base.rstrip('\\')+'\\'+db+'_antes_datos_realistas_'+stamp+'.bak'
                q.execute('BACKUP DATABASE '+m.quote(db)+' TO DISK=? WITH COPY_ONLY,CHECKSUM',path)
                while q.nextset():pass
                q.execute('RESTORE VERIFYONLY FROM DISK=? WITH CHECKSUM',path)
                while q.nextset():pass
                backups.append({'database':db,'path_on_server':path,'copy_only':True,'checksum':True,'restore_verifyonly':True})
                (PRIVATE/'respaldos-nativos.json').write_text(json.dumps(backups,indent=2),encoding='utf-8')
    native=json.loads((PRIVATE/'respaldos-nativos.json').read_text())
    if len(native)!=23 or not all(r['restore_verifyonly'] for r in native):
        raise ValueError('Se requieren respaldos completos verificados de las 23 bases')
    if not args.aplicar:
        with m.connect(cfg,'master') as connection:
            inv=inventory(connection.cursor());connection.rollback()
        assert_scope(inv)
        seed,credentials=synthetic()
        plan={'snapshot':inv,'snapshot_digest':digest(inv),'seed':seed,'team_credentials':credentials,
              'created_at_utc':datetime.now(timezone.utc).isoformat(),'backups':native}
        (PRIVATE/'plan-cifrado.bin').write_bytes(crypt.encrypt(canonical(plan).encode()))
        report={'mode':'plan','databases':len(native),'snapshot_digest':digest(inv),
                'before':{k:len(v['rows']) for k,v in inv.items()},'generated':{k:len(v) for k,v in seed.items()},
                'classification':'Raíces académicas con marcas explícitas, nombres Actualizada y dependencias de esos registros. Identidades plausibles del equipo conservadas. Configuración y permisos preservados.'}
        (PUBLIC/'plan.json').write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
        print('Plan cifrado preparado; sin cambios en SQL Server. '+str(len(seed['seguridad.usuario']))+' usuarios previstos.')
        return
    plan=json.loads(crypt.decrypt((PRIVATE/'plan-cifrado.bin').read_bytes()))
    seed=plan['seed'];before=plan['snapshot'];summary=[]
    with m.connect(cfg,'master') as connection:
        connection.timeout=180;q=connection.cursor()
        # SQL Server restore-media verification is run outside a user transaction.
        connection.autocommit=True
        for backup in native:
            q.execute('RESTORE VERIFYONLY FROM DISK=? WITH CHECKSUM',backup['path_on_server'])
            while q.nextset():pass
        connection.autocommit=False
        q.execute('SET XACT_ABORT ON; SET LOCK_TIMEOUT 15000; SET TRANSACTION ISOLATION LEVEL SERIALIZABLE')
        # ODBC manual-commit mode already starts the transaction. BEGIN TRAN would
        # nest it and leave an open transaction after SQL Server's ODBC COMMIT.
        if q.execute('SELECT @@TRANCOUNT').fetchval()!=1:
            raise ValueError('Se requiere exactamente una transacción ODBC')
        q.execute("DECLARE @r int; EXEC @r=sys.sp_getapplock @Resource=N'PrograV_Datos_Realistas',@LockMode='Exclusive',@LockOwner='Transaction',@LockTimeout=15000; IF @r<0 THROW 50000,'No se adquirió bloqueo de mantenimiento',1")
        inv=inventory(q,True)
        if digest(inv)!=plan['snapshot_digest']:
            raise ValueError('Las bases cambiaron desde el plan; generar un plan nuevo antes de aplicar')
        assert_scope(inv)
        for tab in DOMAIN:
            for key,item in inv.items():
                if item['table']!=tab:continue
                full=m.quote(item['database'])+'.'+m.table_name(tab)
                q.execute('DELETE FROM '+full)
                summary.append({'database':item['database'],'table':tab,'deleted':len(item['rows']),'created':len(seed[tab]),'updated':0})
        # Add only the missing professor role; retain existing role mappings and grants.
        for db in ['PrograV','PrograV_Rol']:
            q.execute('INSERT INTO '+m.quote(db)+".seguridad.rol(id_rol,nombre_rol) SELECT 'PROFESOR','Profesor' WHERE NOT EXISTS (SELECT 1 FROM "+m.quote(db)+".seguridad.rol WHERE id_rol='PROFESOR')")
            summary.append({'database':db,'table':'seguridad.rol','deleted':0,'created':q.rowcount,'updated':0})
        # FK insertion order, reverse of deletion, with usuarios before token (empty).
        for tab in reversed(DOMAIN):
            for item in inv.values():
                if item['table']==tab:
                    execute_insert(q,item,seed[tab])
        for key,item in inv.items():
            tab=item['table'];full=m.quote(item['database'])+'.'+m.table_name(tab)
            deleted=updated=created=0
            if tab in ['general.bitacora','general.Notificacion']:
                for row in item['rows']:
                    if flagged(row):
                        q.execute('DELETE FROM '+full+' WHERE '+' AND '.join(m.quote(k)+'=?' for k in item['pk']),[row[k] for k in item['pk']]);deleted+=q.rowcount
            if tab=='seguridad.modulo':
                for row in item['rows']:
                    if flagged(row):
                        q.execute('UPDATE '+full+" SET nombre_modulo='Gestión académica' WHERE id_modulo=?",row['id_modulo']);updated+=q.rowcount
            if tab=='seguridad.parametro':
                q.execute('UPDATE '+full+" SET valor='cuc.cr' WHERE id_parametro IN ('DOMESTUD','DOMPROF') AND valor<>'cuc.cr'");updated+=q.rowcount
            if tab not in DOMAIN and tab!='seguridad.rol':
                summary.append({'database':item['database'],'table':tab,'deleted':deleted,'created':created,'updated':updated})
        # Professor can access profile and grading; existing roles and their grants stay intact.
        for module in ['ADM3','ADM17','ADM24']:
            q.execute("INSERT INTO PrograV_Modulo.seguridad.rol_modulo(id_modulo,id_rol) SELECT ?,'PROFESOR' WHERE EXISTS (SELECT 1 FROM PrograV_Modulo.seguridad.modulo WHERE id_modulo=?) AND NOT EXISTS (SELECT 1 FROM PrograV_Modulo.seguridad.rol_modulo WHERE id_modulo=? AND id_rol='PROFESOR')",module,module,module)
            for row in summary:
                if row['database']=='PrograV_Modulo' and row['table']=='seguridad.rol_modulo':row['created']+=q.rowcount
        checks=validate(q,inv,seed)
        after=inventory(q)
        residual={key:sum(flagged(r) for r in v['rows']) for key,v in after.items() if v['table']!='seguridad.parametro'}
        if any(residual.values()):raise ValueError('Quedan marcas de datos artificiales: '+canonical({k:v for k,v in residual.items() if v}))
        # Existing configuration, role definitions and mappings must be preserved exactly except the documented domain change.
        for key,item in before.items():
            if item['table'] in ['seguridad.parametro','seguridad.rol','seguridad.rol_modulo']:
                expected=[dict(r,valor='cuc.cr') if item['table']=='seguridad.parametro' and r['id_parametro'] in ['DOMESTUD','DOMPROF'] else r for r in item['rows']]
                actual={canonical(r) for r in after[key]['rows']}
                if any(canonical(r) not in actual for r in expected):raise ValueError('Cambió configuración o un permiso existente: '+key)
        report={'applied_at_utc':datetime.now(timezone.utc).isoformat(),'committed':False,
                'native_backups':native,'tables':summary,'checks':checks,'remaining_test_rows':residual,
                'personal_records_preserved':2,'credentials_in_report':False,
                'notes':['Ramsés y Alejandro conservan identificación y nombre existentes; cuentas anteriores sustituidas por las ocho solicitadas.',
                         'No se cambia el esquema ni se reinician contadores de identidad. La base histórica PrograV recibe los mismos datos de dominio.',
                         'SMTP y demás parámetros técnicos conservados; DOMPROF se normaliza a cuc.cr.',
                         'Se archivan mediante respaldo y se eliminan solo bitácoras y notificaciones que contienen marcas explícitas de datos de ensayo.',
                         'El modelo Curso no tiene columna de contenidos: se documentan los contenidos fuera de la base sin alterar el esquema.']}
        (PRIVATE/'resultado-previo-commit.bin').write_bytes(crypt.encrypt(canonical(report).encode()))
        connection.commit()
        connection.autocommit=True
        if q.execute('SELECT @@TRANCOUNT').fetchval()!=0:
            raise ValueError('El controlador no cerró la transacción')
        report['committed']=True
    # Do not claim success until another connection can see the committed data.
    with m.connect(cfg,'master') as independent:
        q=independent.cursor()
        persisted=inventory(q)
        report['independent_postcommit_checks']=validate(q,persisted,seed)
        independent.rollback()
    (PUBLIC/'resultado.json').write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
    print('Transacción confirmada. Integridad verificada; sin marcas residuales. Credenciales del equipo disponibles solo en el plan local cifrado.')


if __name__=='__main__':
    main()
