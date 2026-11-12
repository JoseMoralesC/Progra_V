// Comprobaciones de auditoría con respuestas controladas; no escribe en servicios reales.
const { chromium } = require('../../../../NuevoAvatar.Estudiante.React/node_modules/@playwright/test');
const fs = require('node:fs');
const path = require('node:path');
const assert = require('node:assert/strict');
const resultados = [];
const expediente = { identificacion: '101', tipoIdentificacion: 'Cedula', nombreCompleto: 'Ana Prueba', email: 'ana@cuc.cr', fechaNacimiento: '2000-01-01', telefonos: ['8888-8888'], provinciaId: 1, cantonId: 2, distritoId: 3, otrasSenas: 'Casa de prueba' };
async function escenario(browser, rol = 'ESTUDIANTE', cantidad = 16) {
  const context = await browser.newContext();
  const llamadas = [];
  await context.addInitScript(({ rol }) => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({ access_token: 'auditoria-ficticia', refresh_token: 'renovacion-ficticia', usuarioID: rol === 'ESTUDIANTE' ? 'ana@cuc.cr' : 'admin@cuc.ac.cr', expires_in: new Date(Date.now() + 3600000).toISOString() })), { rol });
  await context.route('**/gateway/**', async route => {
    const req = route.request(), p = new URL(req.url()).pathname;
    llamadas.push({ ruta: p, metodo: req.method() });
    let body = [], status = 200;
    if (p.endsWith('/auth/sesion')) body = { usuarioID: rol === 'ESTUDIANTE' ? 'ana@cuc.cr' : 'admin@cuc.ac.cr', nombre: 'Ana Prueba', rol };
    else if (p === '/gateway/menu') body = [];
    else if (p === '/gateway/auditoria/interfaz' || p.endsWith('/auth/logout')) status = 204;
    else if (p === '/gateway/expediente/propio') body = expediente;
    else if (p === '/gateway/mat/provincias') body = [{ provinciaId: 1, nombre: 'Cartago' }];
    else if (p === '/gateway/mat/cantones/1') body = [{ provinciaId: 1, cantonId: 2, nombre: 'Central' }];
    else if (p === '/gateway/mat/distritos/1/2') body = [{ cantonId: 2, distritoId: 3, nombre: 'Oriental' }];
    else if (p.endsWith('/prematricula/propio/opciones')) body = { carreras: [{ id: 1, nombre: 'Informatica' }], cursos: [{ id: 10, carreraId: 1, nombre: 'Programacion' }], periodos: [{ id: 20, fechaInicio: '2027-01-01' }] };
    else if (p.endsWith('/prematricula/propio')) body = Array.from({ length: cantidad }, (_, i) => ({ id: i + 1, carreraId: 1, cursos: [10], periodoId: 20 }));
    else if (p.endsWith('/parametro/TAMPAG')) body = { valor: '2' };
    else if (p === '/gateway/perfil') body = { email: 'ana@cuc.cr', nombre: 'Ana Prueba', idRol: 'EST', identificacion: '101', tipoIdentificacion: 'Cedula' };
    else if (p.endsWith('/politica-contrasena')) body = { minLength: 8, maxLength: 30, requireUppercase: true, requireNumber: true, maxBcryptUtf8Bytes: 72 };
    else if (p.endsWith('/notificar/historial')) body = { items: [] };
    else if (p.startsWith('/gateway/admin/')) body = [];
    else { status = 501; body = { error: 'Respuesta no preparada: ' + p }; }
    await route.fulfill({ status, contentType: 'application/json', body: status === 204 ? undefined : JSON.stringify(body) });
  });
  const page = await context.newPage();
  page.setDefaultTimeout(6000);
  return { context, page, llamadas };
}
async function caso(id, titulo, run) {
  try { resultados.push({ id, responsable: 'Alejandro', titulo, estado: 'aprobada', observado: await run() }); }
  catch (e) { resultados.push({ id, responsable: 'Alejandro', titulo, estado: 'fallida', observado: e.message.slice(0, 900) }); }
}
(async () => {
  const browser = await chromium.launch({ headless: true });
  try {
    await caso('A-WEB1', 'Inicio con expediente y solicitudes propias', async () => {
      const s = await escenario(browser);
      try { await s.page.goto('http://127.0.0.1:4175/estudiante'); await s.page.getByText('Expediente vinculado', { exact: true }).waitFor(); await s.page.getByText('16 solicitudes', { exact: true }).waitFor(); return 'Expediente y 16 solicitudes de respuestas controladas.'; } finally { await s.context.close(); }
    });
    for (const rol of ['ESTUDIANTE', 'ADMINISTRADOR']) for (const width of [390, 768, 1366]) {
      await caso(`A-ADM1-${rol}-${width}`, `Plantilla ${rol} sin desbordamiento a ${width}px`, async () => {
        const s = await escenario(browser, rol);
        try { await s.page.setViewportSize({ width, height: 900 }); await s.page.goto('http://127.0.0.1:4175/' + (rol === 'ESTUDIANTE' ? 'estudiante' : 'inicio')); await s.page.locator('.user-name').waitFor({ state: 'attached' }); await s.page.screenshot({ path: path.join(__dirname, `plantilla-${rol}-${width}.png`) }); const medidas = await s.page.evaluate(() => ({ contenido: document.documentElement.scrollWidth, ventana: innerWidth })); assert.ok(medidas.contenido <= medidas.ventana, JSON.stringify(medidas)); return medidas; } finally { await s.context.close(); }
      });
    }
    await caso('A-WEB2-confirmacion', 'Expediente espera confirmación y permite volver sin PUT', async () => {
      const s = await escenario(browser);
      try { await s.page.goto('http://127.0.0.1:4175/estudiante/expediente'); await s.page.getByRole('button', { name: 'Editar contacto y dirección' }).click(); await s.page.getByRole('button', { name: 'Revisar cambios' }).click(); await s.page.getByRole('heading', { name: 'Confirmar actualización del expediente' }).waitFor(); assert.ok(!s.llamadas.some(c => c.metodo === 'PUT')); await s.page.getByRole('button', { name: 'Volver al formulario' }).click(); assert.ok(!s.llamadas.some(c => c.metodo === 'PUT')); return 'Confirmación y regreso sin enviar actualización.'; } finally { await s.context.close(); }
    });
    await caso('A-ADM1-ruta-activa', 'Solo una opción del menú estudiantil queda activa en expediente', async () => {
      const s = await escenario(browser);
      try { await s.page.goto('http://127.0.0.1:4175/estudiante/expediente'); await s.page.locator('.user-name').waitFor(); const activas = await s.page.locator('nav[aria-label="Navegación principal"] a.active').allTextContents(); assert.equal(activas.length, 1, 'Opciones activas: ' + activas.join(', ')); return activas; } finally { await s.context.close(); }
    });
    await caso('A-ADM1-identidad-movil', 'La barra de usuario muestra nombre y rol en móvil', async () => {
      const s = await escenario(browser);
      try { await s.page.setViewportSize({ width: 390, height: 900 }); await s.page.goto('http://127.0.0.1:4175/estudiante/expediente'); await s.page.locator('.user-name').waitFor({ state: 'attached' }); assert.ok(await s.page.locator('.user-name').isVisible() && await s.page.locator('.user-role').isVisible(), 'El CSS oculta .user-info en anchos de hasta 600px; nombre y rol no son visibles en la barra.'); return 'Identidad visible'; } finally { await s.context.close(); }
    });
    await caso('A-WEB4-confirmacion', 'Cancelar solicitud requiere confirmar antes de DELETE', async () => {
      const s = await escenario(browser);
      try { await s.page.goto('http://127.0.0.1:4175/estudiante/prematricula'); await s.page.getByRole('button', { name: 'Cancelar solicitud', exact: true }).first().click(); await s.page.getByRole('button', { name: 'Mantener solicitud' }).waitFor(); assert.ok(!s.llamadas.some(c => c.metodo === 'DELETE')); return 'Se ofrece confirmación sin eliminar todavía.'; } finally { await s.context.close(); }
    });
    await caso('A-WEB4-TAMPAG', 'Prematrícula respeta TAMPAG=2', async () => {
      const s = await escenario(browser);
      try { await s.page.goto('http://127.0.0.1:4175/estudiante/prematricula'); await s.page.getByRole('heading', { name: 'Solicitudes registradas' }).waitFor(); const filas = await s.page.getByRole('article', { name: /Prematrícula/ }).count(); assert.equal(filas, 2, `TAMPAG=2 preparado; se muestran ${filas} solicitudes. El componente usa una constante de 10.`); return filas; } finally { await s.context.close(); }
    });
    await caso('A-ADM24-salida', 'Salir limpia la sesión de las pestañas del mismo navegador', async () => {
      const s = await escenario(browser);
      try { await s.page.goto('http://127.0.0.1:4175/estudiante'); await s.page.locator('.user-name').waitFor(); const otra = await s.context.newPage(); await otra.goto('http://127.0.0.1:4175/estudiante'); await otra.locator('.user-name').waitFor(); await s.page.getByRole('button', { name: 'Salir', exact: true }).click(); await otra.waitForURL('**/login'); assert.equal(await otra.evaluate(() => sessionStorage.getItem('nuevo-avatar.sesion')), null); return 'La segunda pestaña solicita login y limpia tokens.'; } finally { await s.context.close(); }
    });
  } finally { await browser.close(); fs.writeFileSync(path.join(__dirname, 'recorrido-estudiante.json'), JSON.stringify(resultados, null, 2)); }
  console.log(JSON.stringify(resultados.map(({ id, estado, observado }) => ({ id, estado, observado })), null, 2));
  process.exitCode = resultados.some(r => r.estado === 'fallida') ? 1 : 0;
})().catch(e => { console.error(e); process.exitCode = 1; });
