import { test, expect } from '@playwright/test';
test('el modal bloquea el fondo y restaura el foco al cerrar', async ({ page }) => {
  await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({ access_token: 'token', refresh_token: 'refresh', usuarioID: 'admin@cuc.cr', expires_in: '2099-01-01T00:00:00Z' })));
  await page.route('**/gateway/auth/sesion', route => route.fulfill({ json: { rol: 'ADMINISTRADOR', nombre: 'Admin' } }));
  await page.route('**/gateway/admin/modulo', route => route.fulfill({ json: [] }));
  await page.route('**/gateway/admin/rol', route => route.fulfill({ json: [] }));
  await page.goto('/'); const abrir = page.getByRole('button', { name: 'Nuevo módulo' }); await abrir.click();
  await expect(page.getByRole('dialog')).toBeVisible(); await expect(page.locator('body')).toHaveClass(/modal-open/);
  await page.keyboard.press('Escape'); await expect(page.getByRole('dialog')).toBeHidden(); await expect(abrir).toBeFocused();
});

test('abre el mantenimiento de profesores en un modal bloqueante', async ({ page }) => {
  await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({ access_token: 'token', refresh_token: 'refresh', usuarioID: 'admin@cuc.cr', expires_in: '2099-01-01T00:00:00Z' })));
  await page.route('**/gateway/auth/sesion', route => route.fulfill({ json: { rol: 'ADMINISTRADOR', nombre: 'Admin' } }));
  await page.route('**/gateway/admin/modulo', route => route.fulfill({ json: [] }));
  await page.route('**/gateway/admin/rol', route => route.fulfill({ json: [] }));
  await page.route('**/gateway/admin/profesor/buscar**', route => route.fulfill({ json: { items: [], page: 0, size: 10, total: 0 } }));
  await page.goto('/'); await page.getByRole('button', { name: 'Profesores' }).click();
  const abrir = page.getByRole('button', { name: 'Nuevo profesor' }); await abrir.click();
  await expect(page.getByRole('dialog')).toBeVisible(); await expect(page.locator('body')).toHaveClass(/modal-open/);
  await page.keyboard.press('Escape'); await expect(abrir).toBeFocused();
});

test('abre el mantenimiento de carreras con selectores reales', async ({ page }) => {
  await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({ access_token: 'token', refresh_token: 'refresh', usuarioID: 'admin@cuc.cr', expires_in: '2099-01-01T00:00:00Z' })));
  await page.route('**/gateway/auth/sesion', route => route.fulfill({ json: { rol: 'ADMINISTRADOR', nombre: 'Admin' } }));
  await page.route('**/gateway/admin/modulo', route => route.fulfill({ json: [] }));
  await page.route('**/gateway/admin/rol', route => route.fulfill({ json: [] }));
  await page.route('**/gateway/admin/carrera**', route => route.fulfill({ json: [] }));
  await page.route('**/gateway/admin/institucion', route => route.fulfill({ json: [{ id: 1, nombre: 'CUC' }] }));
  await page.route('**/gateway/admin/profesor/buscar**', route => route.fulfill({ json: { items: [{ id: 2, nombreCompleto: 'Ana Mora' }], page: 0, size: 100, total: 1 } }));
  await page.goto('/'); await page.getByRole('button', { name: 'Carreras' }).click(); await page.getByRole('button', { name: 'Nueva carrera' }).click();
  const dialogo = page.getByRole('dialog'); await expect(dialogo).toBeVisible(); await expect(dialogo.getByRole('option', { name: 'CUC' })).toBeAttached(); await expect(dialogo.getByRole('option', { name: 'Ana Mora' })).toBeAttached();
});

test('abre el mantenimiento de cursos con carrera y nivel', async ({ page }) => {
  await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({ access_token: 'token', refresh_token: 'refresh', usuarioID: 'admin@cuc.cr', expires_in: '2099-01-01T00:00:00Z' })));
  await page.route('**/gateway/auth/sesion', route => route.fulfill({ json: { rol: 'ADMINISTRADOR', nombre: 'Admin' } }));
  await page.route('**/gateway/admin/modulo', route => route.fulfill({ json: [] })); await page.route('**/gateway/admin/rol', route => route.fulfill({ json: [] }));
  await page.route('**/gateway/admin/curso**', route => route.fulfill({ json: [] })); await page.route('**/gateway/admin/carrera', route => route.fulfill({ json: [{ id: 1, nombre: 'Ingeniería' }] }));
  await page.goto('/'); await page.getByRole('button', { name: 'Cursos' }).click(); await page.getByRole('button', { name: 'Nuevo curso' }).click();
  const dialogo = page.getByRole('dialog'); await expect(dialogo).toBeVisible(); await expect(dialogo.getByRole('option', { name: 'Ingeniería' })).toBeAttached();
});
