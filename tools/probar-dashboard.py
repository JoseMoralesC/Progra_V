"""Prueba aislada de la UI: usa datos ficticios y no accede a servicios ni a la DB.
Requiere Playwright para Python y Chromium instalado. Ejecutar desde la raíz.
"""
from pathlib import Path
import subprocess
import time
import urllib.request
from playwright.sync_api import sync_playwright, expect

raiz = Path(__file__).resolve().parent.parent
front = raiz / 'NuevoAvatar.Estudiante.React'
servidor = subprocess.Popen(
    ['node', str(front / 'node_modules/vite/bin/vite.js'), '--host', '127.0.0.1', '--port', '4186', '--strictPort'],
    cwd=front, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL,
    creationflags=getattr(subprocess, 'CREATE_NO_WINDOW', 0))
try:
    url = 'http://127.0.0.1:4186/tests/dashboard/index.html'
    for intento in range(60):
        if servidor.poll() is not None:
            raise RuntimeError('No se pudo iniciar el servidor aislado de pruebas')
        try:
            urllib.request.urlopen(url, timeout=1).close()
            break
        except OSError:
            time.sleep(.25)
    with sync_playwright() as pw:
        navegador = pw.chromium.launch()
        pagina = navegador.new_page(viewport={'width': 1280, 'height': 720})
        pagina.clock.install()
        pagina.goto(url)
        tabla = pagina.locator('table')
        expect(tabla.locator('tbody tr')).to_have_count(25)
        expect(tabla.get_by_role('columnheader', name='Detalle', exact=True)).to_have_count(0)
        pagina.evaluate('window.scrollTo(0, 500); window.tablaOriginal = document.querySelector("table")')
        desplazamiento = pagina.evaluate('window.scrollY')
        pagina.evaluate('window.pruebaDashboard.corte = 26')
        pagina.clock.fast_forward(10000)
        expect(pagina.get_by_text('Hay movimientos nuevos.', exact=False)).to_be_visible()
        assert pagina.evaluate('window.pruebaDashboard.comprobaciones') == 1
        assert pagina.evaluate('document.querySelector("table") === window.tablaOriginal')
        assert abs(pagina.evaluate('window.scrollY') - desplazamiento) <= 1
        expect(tabla.locator('tbody tr').first.locator('td').first).to_have_text('25')
        # Fallar al comprobar novedades tampoco borra la tabla.
        pagina.evaluate('window.pruebaDashboard.fallar = true')
        pagina.clock.fast_forward(10000)
        expect(pagina.get_by_role('alert')).to_have_text('Servicio no disponible')
        expect(tabla.locator('tbody tr')).to_have_count(25)
        pagina.evaluate('window.pruebaDashboard.fallar = false')
        pagina.get_by_role('button', name='Actualizar historial').click()
        expect(tabla.locator('tbody tr').first.locator('td').first).to_have_text('26')
        expect(pagina.get_by_role('alert')).to_have_count(0)
        rutas = pagina.evaluate('window.pruebaDashboard.rutas')
        for ruta in rutas:
            assert 'vista=dashboard' in ruta, ruta
        navegador.close()
        print('Dashboard verificado: cargas identificadas, tabla estable, detalle oculto, scroll conservado y actualización manual.')
finally:
    servidor.terminate()
    servidor.wait(timeout=10)
