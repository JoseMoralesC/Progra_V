import { test, expect } from '@playwright/test';

async function escenario(page, { fallo = false, sinDesglose = false, rol = 'PROFESOR', caida = false } = {}) {
  let notas = [], rubros = sinDesglose ? [] : [{ id: 9, grupoId: 1, nombre: 'Proyecto', porcentaje: 100 }];
  const escrituras = [];
  await page.route('**/gateway/**', async route => {
    const req = route.request(), path = new URL(req.url()).pathname;
    let body = {}, status = 200;
    if (path.endsWith('/login')) {
      body = fallo ? {} : { access_token: 'local-prueba', refresh_token: 'local-refresh', usuarioID: 'p@cuc.ac.cr',
        expires_in: new Date(Date.now() + 600000).toISOString() }; status = fallo ? 401 : 201;
    } else if (path.endsWith('/sesion')) body = { nombre: 'Docente de prueba', rol, profesorId: 1 };
    else if (path.endsWith('/logout')) { await route.fulfill({ status: 204 }); return; }
    else if (path.endsWith('/profesor/grupos')) body = [{ grupoId: 1, numeroGrupo: 1, curso: 'Programación', horario: 'Lunes 18:00' }];
    else if (caida) { body = {}; status = 503; }
    else if (path.endsWith('/obtenerdesglose')) body = rubros;
    else if (path.endsWith('/obtenernotasgrupo')) body = [{ matriculaId: 12, identificacionEstudiante: '123', notas }];
    else if (path.endsWith('/asignarnotarubro')) {
      escrituras.push([req.method(), req.postDataJSON()]);
      notas = [{ ...req.postDataJSON(), id: 8 }]; body = notas[0];
    } else if (path.endsWith('/cargardesglose')) {
      escrituras.push([req.method(), req.postDataJSON()]);
      rubros = req.postDataJSON().rubros.map((r, i) => ({ ...r, id: i + 9, grupoId: 1 })); body = rubros;
    }
    await route.fulfill({ status, contentType: 'application/json', body: JSON.stringify(body) });
  });
  await page.goto('/login');
  await page.getByLabel('Correo institucional').fill('p@cuc.ac.cr');
  await page.getByLabel('Contraseña', { exact: true }).fill('clave-de-prueba');
  await page.getByRole('button', { name: 'Ingresar', exact: true }).click();
  return escrituras;
}

test('login invalido no entra y muestra mensaje requerido', async ({ page }) => {
  await escenario(page, { fallo: true });
  await expect(page.getByRole('alert')).toHaveText('Usuario y/o contraseña incorrectos');
  await expect(page.getByRole('heading', { name: 'Desglose y notas' })).toHaveCount(0);
});

test('profesor crea y modifica nota con verbos distintos y rubros quedan bloqueados', async ({ page }) => {
  const escrituras = await escenario(page);
  await page.getByLabel('Grupo del periodo activo').selectOption('1');
  await expect(page.getByText('Sin notas registradas.')).toBeVisible();
  page.on('dialog', dialog => dialog.accept());
  await page.getByLabel('Nota Proyecto', { exact: true }).fill('85');
  await page.getByRole('button', { name: 'Asignar nota', exact: true }).click();
  await expect(page.getByText('Ya existen notas: el desglose está bloqueado.')).toBeVisible();
  await expect(page.getByLabel('Rubro 1', { exact: true })).toBeDisabled();
  await page.getByLabel('Nota Proyecto', { exact: true }).fill('90');
  await page.getByRole('button', { name: 'Modificar nota', exact: true }).click();
  await expect(page.getByText('Nota guardada.', { exact: true })).toBeVisible();
  expect(escrituras.map(e => e[0])).toEqual(['POST', 'PUT']);
  expect(escrituras[0][1]).toEqual({ matriculaId: 12, rubroId: 9, nota: 85 });
  await page.screenshot({ path: '../../tmp/jose-notas.png', fullPage: true });
});

test('sin desglose permite carga confirmada y refresca resultado', async ({ page }) => {
  const escrituras = await escenario(page, { sinDesglose: true });
  await page.getByLabel('Grupo del periodo activo').selectOption('1');
  await page.getByLabel('Rubro 1', { exact: true }).fill('Proyecto');
  await page.getByLabel('Porcentaje 1', { exact: true }).fill('100');
  page.on('dialog', dialog => dialog.accept());
  await page.getByRole('button', { name: 'Guardar desglose', exact: true }).click();
  await expect(page.getByText('Desglose guardado.', { exact: true })).toBeVisible();
  expect(escrituras[0][1]).toEqual({ grupoId: 1, rubros: [{ nombre: 'Proyecto', porcentaje: 100 }] });
});

test('dependencia caida no muestra vacio ni permite guardar', async ({ page }) => {
  await escenario(page, { caida: true });
  await page.getByLabel('Grupo del periodo activo').selectOption('1');
  await expect(page.getByRole('alert')).toContainText('no está disponible');
  await expect(page.getByRole('button', { name: 'Guardar desglose' })).toHaveCount(0);
});

test('estudiante no obtiene controles de notas y logout limpia almacenamiento', async ({ page }) => {
  await escenario(page, { rol: 'ESTUDIANTE' });
  await expect(page.getByRole('heading', { name: 'Sesión iniciada' })).toBeVisible();
  await expect(page.getByRole('heading', { name: 'Desglose y notas' })).toHaveCount(0);
  await page.getByRole('button', { name: 'Cerrar sesión' }).click();
  await expect(page.getByRole('heading', { name: 'Iniciar sesión' })).toBeVisible();
  expect(await page.evaluate(() => sessionStorage.getItem('nuevo-avatar.sesion'))).toBeNull();
});

test('login utilizable en pantalla movil sin desbordamiento horizontal', async ({ page }) => {
  await page.setViewportSize({ width: 390, height: 844 }); await page.goto('/login');
  await expect(page.getByRole('button', { name: 'Ingresar', exact: true })).toBeVisible();
  expect(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth)).toBeTruthy();
  await page.screenshot({ path: '../../tmp/jose-login-movil.png', fullPage: true });
});
