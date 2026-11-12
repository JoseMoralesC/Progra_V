"""Consolida casos ejecutados sin duplicar dependencias entre integrantes."""
from pathlib import Path
import json
import re
import xml.etree.ElementTree as ET

OUT = Path(__file__).resolve().parent
ROOT = OUT.parents[3]
casos = []

def agregar(responsable, grupo, nombre, estado):
    casos.append(dict(responsable=responsable, grupo=grupo, caso=nombre, estado=estado))

def leer(path):
    data = path.read_bytes()
    return data.decode('utf-16') if data.startswith((b'\xff\xfe', b'\xfe\xff')) else data.decode('utf-8-sig', errors='replace')

for modulo, responsable in [('Usuario', 'Fabian'), ('Profesor', 'Ramses'), ('Carrera', 'Ramses'), ('Expediente', 'Alejandro'), ('Prematricula', 'Alejandro'), ('Notas', 'Jose'), ('Notificacion', 'Ramses')]:
    for archivo in (ROOT / ('NuevoAvatar.' + modulo) / 'target/surefire-reports').glob('TEST-*.xml'):
        for caso in ET.parse(archivo).getroot().findall('testcase'):
            persona = 'Alejandro' if 'UsuarioPerfilPropioTest' in caso.get('classname', '') else responsable
            estado = 'fallida' if caso.find('failure') is not None or caso.find('error') is not None else 'omitida' if caso.find('skipped') is not None else 'aprobada'
            agregar(persona, 'Java ' + modulo, caso.get('classname', '') + '.' + caso.get('name', ''), estado)

for archivo in (ROOT / 'integracion/target/surefire-reports').glob('TEST-*.xml'):
    for caso in ET.parse(archivo).getroot().findall('testcase'):
        agregar('Jose', 'Contratos compartidos', caso.get('name'), 'aprobada' if caso.find('failure') is None and caso.find('error') is None else 'fallida')

ns = {'t': 'http://microsoft.com/schemas/VisualStudio/TeamTest/2010'}
for archivo in (OUT / 'dotnet').glob('*.trx'):
    for caso in ET.parse(archivo).findall('.//t:UnitTestResult', ns):
        nombre = caso.get('testName', '')
        if 'GrupoServiceTest' in nombre or 'GrupoExige' in nombre:
            agregar('Apoyo heredado', '.NET', nombre, caso.get('outcome'))
        else:
            persona = 'Ramses' if any(x in nombre for x in ['Periodo', 'Curso']) and 'Gateway' not in nombre else 'Jose'
            agregar(persona, '.NET', nombre, 'aprobada' if caso.get('outcome') == 'Passed' else 'fallida')

for persona in ['Jose', 'Ramses']:
    texto = leer(OUT / (persona.lower() + '-node.log'))
    for match in re.finditer(r'^ok \d+ - (.+)$', texto, re.M):
        agregar(persona, 'Node', match[1], 'aprobada')
    for match in re.finditer(r'^not ok \d+ - (.+)$', texto, re.M):
        agregar(persona, 'Node', match[1], 'fallida')

reportes = {}
def recorrer(suites, persona, grupo):
    for suite in suites:
        for spec in suite.get('specs', []):
            fichero = spec.get('file', '')
            propietario = persona or ('Ramses' if 'ramses.spec' in fichero else 'Jose' if any(x in fichero for x in ['notas.spec', 'sesion.spec']) else 'Fabian')
            for test in spec.get('tests', []):
                status = test.get('status')
                agregar(propietario, grupo, spec['title'], 'aprobada' if status == 'expected' else 'omitida' if status == 'skipped' else 'fallida')
        recorrer(suite.get('suites', []), persona, grupo)

for archivo, persona in [('principal-ui-final.json', None), ('jose-ui-final.json', 'Jose'), ('ramses-ui-final.json', 'Ramses')]:
    contenido = leer(OUT / archivo)
    try:
        datos = json.loads(contenido[contenido.index('{'):])
        reportes[archivo] = {**datos.get('stats', {}), 'errores_generales': datos.get('errors', [])}
        recorrer(datos.get('suites', []), persona, archivo)
    except (ValueError, json.JSONDecodeError):
        reportes[archivo] = {'estado': 'sin informe completo'}

for caso in json.loads((OUT / 'recorrido-estudiante.json').read_text(encoding='utf-8')):
    agregar('Alejandro', 'Recorrido de auditoría', caso['id'] + ': ' + caso['titulo'], caso['estado'])

resumen = {}
for persona in ['Alejandro', 'Fabian', 'Jose', 'Ramses']:
    propios = [c for c in casos if c['responsable'] == persona]
    estados = {e: sum(c['estado'] == e for c in propios) for e in ['aprobada', 'fallida', 'omitida']}
    total = sum(estados.values())
    resumen[persona] = {**estados, 'total': total, 'porcentaje_aprobado': round(estados['aprobada'] / total * 100, 2) if total else 0}

salida = {'fecha': '2026-11-12', 'resumen': resumen, 'navegador': reportes, 'casos': casos,
          'limite': 'Casos locales ejecutados; no equivale a criterios completos, cobertura ni aceptación real. Apoyo heredado no suma a las barras.'}
(OUT / 'resultados.json').write_text(json.dumps(salida, ensure_ascii=False, indent=2), encoding='utf-8')
print(json.dumps({'resumen': resumen, 'navegador': reportes}, ensure_ascii=False, indent=2))
