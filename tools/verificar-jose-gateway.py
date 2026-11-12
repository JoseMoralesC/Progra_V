"""Comprueba fallos de destinos y JWT vencido sin detener el gateway principal."""
import argparse
import base64
import hashlib
import hmac
import importlib.util
import json
from pathlib import Path
import socket
import subprocess
import time
import urllib.error
import urllib.request

ROOT = Path(__file__).resolve().parent.parent
spec = importlib.util.spec_from_file_location('jose', ROOT / 'tools/verificar-jose-a2.py')
jose = importlib.util.module_from_spec(spec)
spec.loader.exec_module(jose)


def pedir(puerto, ruta, token=None):
    req = urllib.request.Request(f'http://127.0.0.1:{puerto}' + ruta,
        headers={'Authorization': 'Bearer ' + token} if token else {})
    try:
        with urllib.request.urlopen(req, timeout=15) as respuesta:
            return respuesta.status
    except urllib.error.HTTPError as error:
        error.close()
        return error.code


def puerto_libre(puerto):
    with socket.socket() as conexion:
        conexion.settimeout(.2)
        return conexion.connect_ex(('127.0.0.1', puerto)) != 0


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--credenciales', required=True, type=Path)
    args = parser.parse_args()
    recorrido = jose.Recorrido(jose.cuentas_archivo(args.credenciales))
    administrador = next(c for c in recorrido.cuentas if c['rol'] == 'ADMINISTRADOR')
    procesos = []
    salidas_no_confirmadas = 0
    error = None
    try:
        sesion = recorrido.login(administrador)
        token = sesion['access_token']
        clave = recorrido.cfg.get('JWT_KEY')
        if not clave:
            raise RuntimeError('Falta clave de entorno para preparar el JWT vencido')
        header, contenido, firma = token.split('.')
        firma_actual = base64.urlsafe_b64encode(hmac.new(clave.encode(), (header + '.' + contenido).encode(), hashlib.sha256).digest()).decode().rstrip('=')
        if not hmac.compare_digest(firma_actual, firma):
            raise RuntimeError('La clave de prueba no coincide con el emisor activo')
        payload = json.loads(base64.urlsafe_b64decode(contenido + '=' * (-len(contenido) % 4)))
        payload['exp'] = int(time.time()) - 120
        contenido = base64.urlsafe_b64encode(json.dumps(payload, separators=(',', ':')).encode()).decode().rstrip('=')
        firma = base64.urlsafe_b64encode(hmac.new(clave.encode(), (header + '.' + contenido).encode(), hashlib.sha256).digest()).decode().rstrip('=')
        status = pedir(5080, '/gateway/admin/usuario', header + '.' + contenido + '.' + firma)
        recorrido.comprobar('JWT con firma válida y expiración pasada se rechaza', status == 401, http=status)

        if not puerto_libre(65530):
            raise RuntimeError('El destino reservado para fallo está en uso')
        dll = ROOT / 'NuevoAvatar.Gateway/bin/Debug/net8.0/NuevoAvatar.Gateway.dll'
        for puerto, destino, esperado in [(5081, 'Usuario', 502), (5082, 'Autenticacion', 503)]:
            if not puerto_libre(puerto):
                raise RuntimeError('Puerto temporal ocupado')
            proceso = subprocess.Popen(['dotnet', str(dll), '--urls', f'http://127.0.0.1:{puerto}',
                f'--Destinos:{destino}=http://127.0.0.1:65530', '--Logging:LogLevel:Default=Warning'],
                cwd=ROOT / 'NuevoAvatar.Gateway', stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL,
                creationflags=subprocess.CREATE_NO_WINDOW)
            procesos.append(proceso)
            limite = time.monotonic() + 15
            while puerto_libre(puerto) and proceso.poll() is None and time.monotonic() < limite:
                time.sleep(.1)
            status = pedir(puerto, '/gateway/admin/usuario', token)
            recorrido.comprobar('Dependencia real inaccesible: ' + destino, status == esperado, http=status,
                escenario='Gateway temporal con las demás dependencias reales; no se detiene el principal')
    except Exception as exc:
        error = {'tipo': type(exc).__name__, 'detalle': str(exc) if isinstance(exc, AssertionError) else 'Valores reservados omitidos'}
    finally:
        for proceso in procesos:
            if proceso.poll() is None:
                proceso.terminate()
                try:
                    proceso.wait(timeout=5)
                except subprocess.TimeoutExpired:
                    proceso.kill()
                    proceso.wait(timeout=5)
        for token in recorrido.tokens:
            try:
                if recorrido.http('/gateway/auth/logout', 'POST', token=token['access_token'])[0] != 204:
                    salidas_no_confirmadas += 1
            except Exception:
                salidas_no_confirmadas += 1
        reporte = {'responsable': 'Jose', 'pruebas': recorrido.resultados, 'error': error,
            'gateways_temporales_cerrados': all(proceso.poll() is not None for proceso in procesos),
            'salidas_no_confirmadas': salidas_no_confirmadas}
        (jose.OUT / 'gateway-fallos-reales.json').write_text(json.dumps(reporte, ensure_ascii=False, indent=2), encoding='utf-8')
    print(json.dumps({'casos': len(recorrido.resultados), 'error': error, 'procesos_cerrados': reporte['gateways_temporales_cerrados']}, ensure_ascii=False))
    return 1 if error or salidas_no_confirmadas else 0


if __name__ == '__main__':
    raise SystemExit(main())
