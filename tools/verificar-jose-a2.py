"""Recorre las HU de Jose con servicios/SQL reales y datos temporales propios.

Recibe las cuentas desde un archivo externo; nunca escribe sus claves o tokens.
Crea un profesor, rol, grupos y matriculas de prueba y los retira en finally.
No modifica notas, cuentas ni asignaciones preexistentes. No envia correos.
"""
import argparse
import concurrent.futures as futures
from datetime import datetime, timezone
import importlib.util
import json
from pathlib import Path
import re
import secrets
import subprocess
import threading
import time
import traceback
import unicodedata
import urllib.error
import urllib.request

import bcrypt

ROOT = Path(__file__).resolve().parent.parent
OUT = ROOT / 'docs/segundo_avance/pruebas/jose-cierre-auditoria5'
spec = importlib.util.spec_from_file_location('migration', ROOT / 'tools/migrar-bases.py')
m = importlib.util.module_from_spec(spec)
spec.loader.exec_module(m)


def cuentas_archivo(path):
    cuentas = []
    # Solo interpreto los campos de acceso; el resto del archivo no se ejecuta.
    for bloque in re.split(r'\n\s*\n', Path(path).read_text(encoding='utf-8-sig')):
        campos = {}
        for linea in bloque.splitlines():
            if ':' not in linea:
                continue
            clave, valor = linea.split(':', 1)
            clave = ''.join(c for c in unicodedata.normalize('NFD', clave.lower()) if not unicodedata.combining(c)).strip()
            campos[clave] = valor.strip()
        correo = re.search(r'[\w.+-]+@[\w.-]+\.[a-zA-Z]+', bloque)
        if correo and campos.get('contrasena') and campos.get('rol'):
            cuentas.append({'email': correo[0], 'password': campos['contrasena'], 'rol': campos['rol'].upper()})
    if not cuentas:
        raise ValueError('No se encontraron campos de acceso reconocidos')
    return cuentas


class Recorrido:
    def __init__(self, cuentas):
        self.cfg = m.configuration()
        self.cuentas = cuentas
        self.resultados = []
        self.fixture = {'grupos': [], 'matriculas': [], 'profesor': None, 'rol': None, 'usuario': None, 'permiso': False}
        self.tokens = []
        self.inicio = datetime.now(timezone.utc).isoformat()

    def sql(self, servicio):
        return m.connect(self.cfg, 'PrograV_' + servicio)

    def ejecutar(self, servicio, sql, *args):
        with self.sql(servicio) as c:
            cursor = c.cursor().execute(sql, *args)
            return cursor.fetchall() if cursor.description else []

    def http(self, path, method='GET', token=None, body=None, headers=None):
        cabeceras = {'Content-Type': 'application/json', **(headers or {})}
        if token:
            cabeceras['Authorization'] = 'Bearer ' + token
        request = urllib.request.Request('http://127.0.0.1:5080' + path, method=method,
            headers=cabeceras, data=json.dumps(body).encode() if body is not None else None)
        try:
            response = urllib.request.urlopen(request, timeout=35)
        except urllib.error.HTTPError as error:
            response = error
        with response:
            raw = response.read()
            try:
                contenido = json.loads(raw) if raw else None
            except ValueError:
                contenido = None
            return response.code, contenido

    def comprobar(self, nombre, condicion, **observado):
        self.resultados.append({'prueba': nombre, 'estado': 'aprobada' if condicion else 'fallida', 'observado': observado})
        if not condicion:
            raise AssertionError(nombre)

    def login(self, cuenta):
        status, respuesta = self.http('/gateway/auth/login', 'POST', headers={
            'usuario': cuenta['email'], 'contrasena': cuenta['password']})
        self.comprobar('Ingreso real ' + cuenta['rol'], status == 201 and bool(respuesta and respuesta.get('access_token')), http=status)
        self.tokens.append(respuesta)
        return respuesta

    def preparar(self):
        sufijo = secrets.token_hex(4)
        self.email = 'qa.jose.' + sufijo + '@cuc.ac.cr'
        self.password = 'QaTemporal1!' + secrets.token_hex(12)
        self.identificacion = 'QAJOSE' + sufijo
        rol = 'QAJOSE_' + sufijo
        # Nunca reutilizo un rol de otros usuarios para ensayar revocaciones.
        existentes = self.ejecutar('Rol', "SELECT COUNT(*) FROM seguridad.rol WHERE UPPER(nombre_rol)='PROFESOR'")[0][0]
        if existentes:
            raise RuntimeError('Ya existe el rol Profesor; se requiere acordar un escenario aislado antes de crear otro')
        self.ejecutar('Rol', 'INSERT seguridad.rol(id_rol,nombre_rol) VALUES (?,?)', rol, 'PROFESOR')
        self.fixture['rol'] = rol
        self.ejecutar('Usuario', '''INSERT seguridad.usuario(email,tipo_identificacion,identificacion,nombre,id_rol,password_hash)
            VALUES (?,?,?,?,?,?)''', self.email, 'CEDULA', self.identificacion, 'Docente Prueba Jose', rol,
            bcrypt.hashpw(self.password.encode(), bcrypt.gensalt()).decode())
        self.fixture['usuario'] = self.email
        profesor = self.ejecutar('Profesor', '''INSERT academico.Profesor
            (TipoIdentificacion,Identificacion,Email,NombreCompleto,FechaNacimiento)
            OUTPUT INSERTED.ProfesorId VALUES (?,?,?,?,?)''', 'CEDULA', self.identificacion,
            self.email, 'Docente Prueba Jose', '1990-01-01')[0][0]
        self.fixture['profesor'] = profesor
        self.ejecutar('Profesor', 'INSERT academico.ProfesorTelefono(ProfesorId,Telefono) VALUES (?,?)', profesor, '8888-8888')
        self.ejecutar('Modulo', 'INSERT seguridad.rol_modulo(id_modulo,id_rol) VALUES (?,?)', 'ADM17', rol)
        self.fixture['permiso'] = True
        periodo = self.ejecutar('Periodo', '''SELECT TOP (1) PeriodoId FROM academico.Periodo
            WHERE FechaInicio<=CONVERT(date,GETDATE()) AND FechaFin>=CONVERT(date,GETDATE()) ORDER BY PeriodoId''')[0][0]
        curso = self.ejecutar('Curso', 'SELECT TOP (1) CursoId FROM academico.Curso ORDER BY CursoId')[0][0]
        estudiante = self.ejecutar('Expediente', 'SELECT TOP (1) Identificacion FROM matricula.Estudiante ORDER BY Identificacion')[0][0]
        self.curso = curso
        for i in range(4):
            numero = int(sufijo[:5], 16) + i + 100
            grupo = self.ejecutar('Grupo', '''INSERT academico.Grupo(NumeroGrupo,CursoId,ProfesorId,Horario,Cupo,PeriodoId)
                OUTPUT INSERTED.GrupoId VALUES (?,?,?,?,?,?)''', numero, curso, profesor, 'Lunes 18:00', 10, periodo)[0][0]
            self.fixture['grupos'].append(grupo)
            matricula = self.ejecutar('Matricula', '''INSERT matricula.Matricula(IdentificacionEstudiante,CursoId,GrupoId,PeriodoId,Estado)
                OUTPUT INSERTED.MatriculaId VALUES (?,?,?,?,'ACTIVA')''', estudiante, curso, grupo, periodo)[0][0]
            self.fixture['matriculas'].append(matricula)
        self.docente = {'email': self.email, 'password': self.password, 'rol': 'PROFESOR'}

    def foto_sql(self, grupo):
        return [tuple(fila) for fila in self.ejecutar('Notas', '''SELECT r.RubroId,r.Nombre,r.Porcentaje,n.NotaRubroId,n.Nota
            FROM matricula.DesgloseRubro r LEFT JOIN matricula.NotaRubro n ON n.RubroId=r.RubroId
            WHERE r.GrupoId=? ORDER BY r.RubroId,n.NotaRubroId''', grupo)]

    def bloquear(self, grupo):
        conexion = self.sql('Notas')
        resultado = conexion.cursor().execute('''SET NOCOUNT ON; BEGIN TRANSACTION;
            DECLARE @r int; EXEC @r=sys.sp_getapplock
            @Resource=?,@LockMode='Exclusive',@LockOwner='Transaction',@LockTimeout=1000; SELECT @r''',
            'NuevoAvatar.Notas.grupo:' + str(grupo)).fetchval()
        if resultado < 0:
            conexion.rollback()
            conexion.close()
            raise RuntimeError('No se pudo preparar el bloqueo temporal')
        return conexion

    def simultaneas(self, grupo, operaciones):
        bloqueo = self.bloquear(grupo)
        inicio = threading.Barrier(3)
        def enviar(operacion):
            inicio.wait(timeout=5)
            return operacion()
        with futures.ThreadPoolExecutor(max_workers=2) as pool:
            pendientes = [pool.submit(enviar, operacion) for operacion in operaciones]
            try:
                inicio.wait(timeout=5)
                time.sleep(.75)
                en_curso = sum(not pendiente.done() for pendiente in pendientes)
            finally:
                bloqueo.rollback()
                bloqueo.close()
            respuestas = [pendiente.result(timeout=35) for pendiente in pendientes]
        self.comprobar('Dos peticiones concurrentes antes de liberar el bloqueo SQL', en_curso == 2, peticiones_en_curso=en_curso,
            limite='No se consultan DMVs: la cuenta no tiene VIEW SERVER PERFORMANCE STATE; las dos conexiones SQL se verifican en BloqueoGrupoNotasSqlServerTest')
        return respuestas

    def recorrer(self):
        self.comprobar('Notas sin token', self.http('/gateway/mat/obtenerdesglose?grupoId=1')[0] == 401)
        self.comprobar('Notas con token inválido', self.http('/gateway/mat/obtenerdesglose?grupoId=1', token='invalido')[0] == 401)
        sesiones = {}
        for cuenta in self.cuentas + [self.docente]:
            tokens = self.login(cuenta)
            sesiones[cuenta['rol']] = tokens
            status, identidad = self.http('/gateway/auth/sesion', token=tokens['access_token'])
            self.comprobar('Identidad real ' + cuenta['rol'], status == 200 and identidad.get('rol') == cuenta['rol'], http=status)
            status, menu = self.http('/gateway/menu', token=tokens['access_token'])
            self.comprobar('Menú real ' + cuenta['rol'], status == 200 and isinstance(menu, list), http=status, modulos=len(menu) if isinstance(menu, list) else None)
            if cuenta['rol'] != 'PROFESOR':
                status, _ = self.http('/gateway/mat/obtenerdesglose?grupoId=' + str(self.fixture['grupos'][0]), token=tokens['access_token'])
                self.comprobar('Notas rechaza perfil ' + cuenta['rol'], status == 403, http=status)

        token = sesiones['PROFESOR']['access_token']
        status, grupos = self.http('/gateway/mat/profesor/grupos', token=token)
        self.comprobar('Profesor ve sus cuatro grupos temporales', status == 200 and {g['grupoId'] for g in grupos} == set(self.fixture['grupos']), http=status)
        ajeno = self.ejecutar('Grupo', 'SELECT TOP (1) GrupoId FROM academico.Grupo WHERE ProfesorId<>? ORDER BY GrupoId', self.fixture['profesor'])[0][0]
        self.comprobar('Profesor no consulta grupo ajeno', self.http('/gateway/mat/obtenerdesglose?grupoId=' + str(ajeno), token=token)[0] == 403)
        rubros = []
        for grupo in self.fixture['grupos']:
            status, respuesta = self.http('/gateway/mat/cargardesglose', 'POST', token,
                {'grupoId': grupo, 'rubros': [{'nombre': 'Proyecto', 'porcentaje': 100}]})
            self.comprobar('Crear desglose temporal', status == 200 and len(respuesta) == 1, http=status)
            rubros.append(respuesta[0]['id'])
        grupo, matricula = self.fixture['grupos'][0], self.fixture['matriculas'][0]
        antes = self.foto_sql(grupo)
        status, _ = self.http('/gateway/mat/cargardesglose', 'POST', token, {'grupoId': grupo,
            'rubros': [{'nombre': 'Duplicado', 'porcentaje': 50}, {'nombre': 'Duplicado', 'porcentaje': 50}]})
        self.comprobar('Rollback conserva desglose al fallar la escritura', status >= 400 and self.foto_sql(grupo) == antes, http=status)
        status, _ = self.http('/gateway/mat/asignarnotarubro', 'POST', token, {'matriculaId': matricula, 'rubroId': rubros[0], 'nota': 85})
        self.comprobar('Asignar primera nota real', status == 200, http=status)
        status, _ = self.http('/gateway/mat/asignarnotarubro', 'PUT', token, {'matriculaId': matricula, 'rubroId': rubros[0], 'nota': 90})
        self.comprobar('Modificar nota real', status == 200, http=status)
        antes = self.foto_sql(grupo)
        status, _ = self.http('/gateway/mat/cargardesglose', 'POST', token, {'grupoId': grupo, 'rubros': [{'nombre': 'Otro', 'porcentaje': 100}]})
        self.comprobar('Nota existente bloquea reemplazo de rubros', status == 409 and self.foto_sql(grupo) == antes, http=status)

        grupo = self.fixture['grupos'][1]
        nota = {'matriculaId': self.fixture['matriculas'][1], 'rubroId': rubros[1], 'nota': 80}
        reemplazo = {'grupoId': grupo, 'rubros': [{'nombre': 'Reemplazo', 'porcentaje': 100}]}
        respuestas = self.simultaneas(grupo, [lambda: self.http('/gateway/mat/asignarnotarubro', 'POST', token, nota), lambda: self.http('/gateway/mat/cargardesglose', 'POST', token, reemplazo)])
        estados = [r[0] for r in respuestas]
        foto = self.foto_sql(grupo)
        coherente = (estados == [200, 409] and len(foto) == 1 and foto[0][3] is not None) or (estados == [422, 200] and len(foto) == 1 and foto[0][3] is None)
        self.comprobar('Primera nota frente a reemplazo concurrente', coherente, http=estados, rubros=len(foto))

        grupo = self.fixture['grupos'][2]
        nota = {'matriculaId': self.fixture['matriculas'][2], 'rubroId': rubros[2], 'nota': 75}
        respuestas = self.simultaneas(grupo, [lambda: self.http('/gateway/mat/asignarnotarubro', 'POST', token, nota), lambda: self.http('/gateway/mat/asignarnotarubro', 'POST', token, nota)])
        estados = sorted(r[0] for r in respuestas)
        foto = self.foto_sql(grupo)
        self.comprobar('Dos altas simultáneas dejan una sola nota', estados == [200, 409] and len(foto) == 1 and foto[0][3] is not None, http=estados)

        grupo = self.fixture['grupos'][3]
        antes = self.foto_sql(grupo)
        bloqueo = self.bloquear(grupo)
        try:
            status, _ = self.http('/gateway/mat/asignarnotarubro', 'POST', token, {'matriculaId': self.fixture['matriculas'][3], 'rubroId': rubros[3], 'nota': 70})
        finally:
            bloqueo.rollback()
            bloqueo.close()
        self.comprobar('Espera agotada no deja cambios parciales', status == 409 and self.foto_sql(grupo) == antes, http=status)
        self.ejecutar('Modulo', "DELETE seguridad.rol_modulo WHERE id_rol=? AND id_modulo='ADM17'", self.fixture['rol'])
        try:
            status, _ = self.http('/gateway/mat/obtenerdesglose?grupoId=' + str(grupo), token=token)
            self.comprobar('Revocar permiso bloquea la siguiente petición', status == 403, http=status)
        finally:
            self.ejecutar('Modulo', 'INSERT seguridad.rol_modulo(id_modulo,id_rol) VALUES (?,?)', 'ADM17', self.fixture['rol'])
        for rol, sesion in sesiones.items():
            status, renovada = self.http('/gateway/auth/refresh', 'POST', body={'refresh_token': sesion['refresh_token']})
            self.comprobar('Renovación real ' + rol, status == 201 and renovada.get('refresh_token') != sesion['refresh_token'], http=status)
            self.tokens.append(renovada)
            self.comprobar('Refresh anterior no se reutiliza ' + rol, self.http('/gateway/auth/refresh', 'POST', body={'refresh_token': sesion['refresh_token']})[0] == 401)
            self.comprobar('Salida real ' + rol, self.http('/gateway/auth/logout', 'POST', token=renovada['access_token'])[0] == 204)
        eventos = self.ejecutar('Bitacora', '''SELECT descripcion FROM general.bitacora WHERE usuario=?''', self.email)
        altas = sum('Alta de nota.' in r[0] and 'JSON nuevo:' in r[0] for r in eventos)
        cambios = sum('Modificacion de nota.' in r[0] and 'JSON anterior:' in r[0] and 'JSON actual:' in r[0] for r in eventos)
        self.comprobar('Bitácoras persistidas de notas', altas >= 2 and cambios >= 1, altas=altas, modificaciones=cambios, eventos=len(eventos))

    def navegador(self):
        entrada = {'cuentas': self.cuentas + [self.docente], 'grupo': self.fixture['grupos'][0]}
        proceso = subprocess.run(['node', str(ROOT / 'NuevoAvatar.Estudiante.React/tests/reales/jose_sesion_notas.cjs')],
            input=json.dumps(entrada), text=True, capture_output=True, cwd=ROOT, timeout=150)
        self.comprobar('Recorrido real de navegador', proceso.returncode == 0,
            resultado='Ver navegador-real.json; no se conserva la entrada de credenciales')
        foto = self.foto_sql(self.fixture['grupos'][0])
        self.comprobar('La nota cambiada desde la web persiste en SQL', len(foto) == 1 and foto[0][4] == 91)

    def limpiar(self):
        for sesion in self.tokens:
            try:
                self.http('/gateway/auth/logout', 'POST', token=sesion['access_token'])
            except Exception:
                pass
        errores = []
        tareas = []
        for grupo in self.fixture['grupos']:
            tareas.extend([('Notas', 'DELETE n FROM matricula.NotaRubro n JOIN matricula.DesgloseRubro r ON r.RubroId=n.RubroId WHERE r.GrupoId=?', grupo),
                ('Notas', 'DELETE matricula.DesgloseRubro WHERE GrupoId=?', grupo)])
        tareas += [('Matricula', 'DELETE matricula.Matricula WHERE MatriculaId=?', i) for i in self.fixture['matriculas']]
        tareas += [('Grupo', 'DELETE academico.Grupo WHERE GrupoId=?', i) for i in self.fixture['grupos']]
        if self.fixture['usuario']:
            tareas += [('Autenticacion', 'DELETE seguridad.refresh_token WHERE email_usuario=?', self.fixture['usuario']),
                ('Usuario', 'DELETE seguridad.usuario WHERE email=?', self.fixture['usuario'])]
        if self.fixture['profesor']:
            tareas += [('Profesor', 'DELETE academico.Profesor WHERE ProfesorId=?', self.fixture['profesor'])]
        if self.fixture['rol']:
            tareas += [('Modulo', 'DELETE seguridad.rol_modulo WHERE id_rol=?', self.fixture['rol']),
                ('Rol', 'DELETE seguridad.rol WHERE id_rol=?', self.fixture['rol'])]
        for servicio, consulta, valor in tareas:
            try:
                self.ejecutar(servicio, consulta, valor)
            except Exception:
                errores.append(servicio)
        return {'completada': not errores, 'servicios_con_error': sorted(set(errores)),
            'grupos_temporales': len(self.fixture['grupos']), 'profesor_temporal': bool(self.fixture['profesor']),
            'bitacoras': 'Conservadas como registro de las operaciones; sin contraseñas ni tokens en este informe'}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--credenciales', required=True, type=Path)
    args = parser.parse_args()
    OUT.mkdir(parents=True, exist_ok=True)
    recorrido = Recorrido(cuentas_archivo(args.credenciales))
    error = None
    try:
        recorrido.preparar()
        recorrido.recorrer()
        recorrido.navegador()
    except Exception as exc:
        # No serializo excepciones de red/SQL que puedan contener datos de acceso.
        ultimo = traceback.extract_tb(exc.__traceback__)[-1]
        error = {'tipo': type(exc).__name__, 'ubicacion': ultimo.name + ':' + str(ultimo.lineno),
            'detalle': str(exc) if isinstance(exc, AssertionError) else 'Revisar el último caso y la configuración local; valores reservados omitidos'}
    finally:
        limpieza = recorrido.limpiar()
        informe = {'inicio_utc': recorrido.inicio, 'responsable': 'Jose', 'entrega': 'Segundo avance',
            'pruebas': recorrido.resultados, 'error': error, 'limpieza': limpieza}
        (OUT / 'servicios-reales.json').write_text(json.dumps(informe, ensure_ascii=False, indent=2), encoding='utf-8')
    print(json.dumps({'casos': len(recorrido.resultados), 'fallos': sum(r['estado'] != 'aprobada' for r in recorrido.resultados), 'error': error, 'limpieza': limpieza}, ensure_ascii=False))
    return 1 if error or not limpieza['completada'] else 0


if __name__ == '__main__':
    raise SystemExit(main())
