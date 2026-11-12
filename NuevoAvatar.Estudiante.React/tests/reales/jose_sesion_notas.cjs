// Las cuentas llegan por stdin desde verificar-jose-a2.py y permanecen en memoria.
const fs = require('node:fs');
const path = require('node:path');
const { chromium, expect } = require('@playwright/test');
const root = path.resolve(__dirname, '../..');
const salida = path.resolve(root, '../docs/segundo_avance/pruebas/jose-cierre-auditoria5');
const resultados = [];
async function comprobar(nombre, accion) {
  try { await accion(); resultados.push({ prueba: nombre, estado: 'aprobada' }); }
  catch (error) { resultados.push({ prueba: nombre, estado: 'fallida', tipo: error.name }); throw error; }
}
(async () => {
  let entrada = '';
  for await (const fragmento of process.stdin) entrada += fragmento;
  const { cuentas, grupo } = JSON.parse(entrada);
  entrada = '';
  const { createServer } = await import('vite');
  const servidor = await createServer({ root, clearScreen: false, server: { host: '127.0.0.1', port: 4175, strictPort: true, open: false } });
  let navegador;
  try {
    await servidor.listen();
    navegador = await chromium.launch({ headless: true });
    for (const cuenta of cuentas) {
      const contexto = await navegador.newContext({ baseURL: 'http://127.0.0.1:4175' });
      const pagina = await contexto.newPage();
      pagina.setDefaultTimeout(20000);
      try {
        await comprobar('Ingreso desde la web ' + cuenta.rol, async () => {
          await pagina.goto('/login');
          await pagina.getByLabel('Correo institucional').fill(cuenta.email);
          await pagina.getByLabel('Contraseña', { exact: true }).fill(cuenta.password);
          await pagina.getByRole('button', { name: 'Ingresar', exact: true }).click();
          await expect(pagina.locator('.user-role')).toHaveText(cuenta.rol);
          await expect(pagina).toHaveURL(cuenta.rol === 'ESTUDIANTE' ? /\/estudiante$/ : /\/inicio$/);
        });
        await comprobar('Restauración desde la web ' + cuenta.rol, async () => {
          await pagina.reload();
          await expect(pagina.locator('.user-role')).toHaveText(cuenta.rol);
        });
        await pagina.screenshot({ path: path.join(salida, `inicio-real-${cuenta.rol}.png`), mask: [pagina.locator('.user-name')] });
        if (cuenta.rol === 'PROFESOR') {
          await comprobar('Dashboard conduce a notas del profesor', async () => {
            await pagina.getByRole('link', { name: 'Consultar y registrar notas', exact: true }).click();
            await expect(pagina.getByRole('heading', { name: 'Desglose y notas', exact: true })).toBeVisible();
            await pagina.getByLabel('Grupo del periodo activo').selectOption(String(grupo));
            await expect(pagina.getByText('Ya existen notas: el desglose está bloqueado.')).toBeVisible();
            await expect(pagina.getByLabel('Rubro 1', { exact: true })).toBeDisabled();
          });
          await comprobar('Modificar nota real desde la plantilla', async () => {
            pagina.once('dialog', dialogo => dialogo.accept());
            await pagina.getByLabel('Nota Proyecto', { exact: true }).fill('91');
            await pagina.getByRole('button', { name: 'Modificar nota', exact: true }).click();
            await expect(pagina.getByText('Nota guardada.', { exact: true })).toBeVisible();
            await pagina.reload();
            await pagina.getByLabel('Grupo del periodo activo').selectOption(String(grupo));
            await expect(pagina.getByLabel('Nota Proyecto', { exact: true })).toHaveValue('91');
          });
          await pagina.screenshot({ path: path.join(salida, 'notas-reales-profesor.png'), mask: [pagina.locator('.user-name'), pagina.locator('.notas-integradas article h3'), pagina.locator('#estudiante')] });
          await comprobar('Notas permite regresar al dashboard', async () => {
            await pagina.getByRole('link', { name: 'Volver al inicio', exact: true }).click();
            await expect(pagina).toHaveURL(/\/inicio$/);
          });
        } else {
          await comprobar('URL de notas restringida ' + cuenta.rol, async () => {
            await pagina.goto('/notas');
            if (cuenta.rol === 'ESTUDIANTE') await expect(pagina).toHaveURL(/\/estudiante$/);
            else await expect(pagina.getByRole('heading', { name: 'Acceso restringido' })).toBeVisible();
            await expect(pagina.getByLabel('Grupo del periodo activo')).toHaveCount(0);
          });
        }
        await comprobar('Salida limpia el cliente ' + cuenta.rol, async () => {
          await pagina.getByRole('button', { name: 'Salir', exact: true }).click();
          await expect(pagina).toHaveURL(/\/login$/);
          expect(await pagina.evaluate(() => sessionStorage.getItem('nuevo-avatar.sesion'))).toBeNull();
          await pagina.goBack();
          await expect(pagina.getByRole('heading', { name: 'Iniciar sesión', exact: true })).toBeVisible();
        });
      } finally { await contexto.close(); }
    }
  } catch {
    process.exitCode = 1;
  } finally {
    if (navegador) await navegador.close();
    await servidor.close();
    fs.writeFileSync(path.join(salida, 'navegador-real.json'), JSON.stringify({ respuestas_controladas: false, resultados }, null, 2));
  }
})().catch(() => { process.exitCode = 1; });
