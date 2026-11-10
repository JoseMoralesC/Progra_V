import { test, expect } from '@playwright/test'

const pantallas = [
    ['ADM7', '/modulos', 'Mantenimiento de módulos', 'Nuevo módulo', 'Crear módulo'],
    ['ADM9', '/carreras', 'Mantenimiento de carreras', 'Nueva carrera', 'Crear carrera'],
    ['ADM10', '/cursos', 'Mantenimiento de cursos', 'Nuevo curso', 'Crear curso'],
    ['ADM11', '/profesores', 'Mantenimiento de profesores', 'Nuevo profesor', 'Crear profesor'],
    ['ADM12', '/periodos', 'Mantenimiento de periodos', 'Nuevo periodo', 'Crear periodo'],
    ['ADM22', '/notificaciones', 'Notificaciones', 'Nueva notificación', 'Enviar notificación']
]

async function preparar(page, permisos = pantallas.map(x => x[0])) {
    const consultas = []
    const modulos = Array.from({ length: 16 }, (_, i) => ({ idModulo: 'M' + i, nombreModulo: 'Módulo de prueba', activo: true, orden: i }))
    await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({
        access_token: 'prueba', refresh_token: 'renovacion', usuarioID: 'admin@cuc.ac.cr',
        expires_in: new Date(Date.now() + 3600000).toISOString()
    })))
    await page.route('**/gateway/**', async route => {
        const url = new URL(route.request().url()), path = url.pathname
        consultas.push(path)
        let body = []
        if (path.endsWith('/sesion')) body = { usuarioID: 'admin@cuc.ac.cr', nombre: 'Administración de prueba', rol: 'ADMINISTRADOR' }
        else if (path === '/gateway/menu') body = permisos.map((idModulo, orden) => ({ idModulo, nombreModulo: idModulo, activo: true, orden }))
        else if (path.endsWith('/parametro/TAMPAG')) body = { valor: '2' }
        else if (path.endsWith('/profesor/buscar')) body = { items: [], page: 0, size: 2, total: 0 }
        else if (path.endsWith('/notificar/historial')) body = { items: [], page: 0, size: 2, total: 0 }
        else if (path === '/gateway/admin/modulo') {
            if (route.request().method() === 'POST') {
                const nuevo = route.request().postDataJSON(); modulos.push(nuevo)
                await route.fulfill({ status: 201, json: nuevo }); return
            }
            body = modulos
        } else if (path.startsWith('/gateway/admin/modulo/')) {
            const id = decodeURIComponent(path.split('/').at(-1))
            const index = modulos.findIndex(m => m.idModulo === id)
            if (route.request().method() === 'PUT') {
                modulos[index] = route.request().postDataJSON(); body = modulos[index]
            } else if (route.request().method() === 'DELETE') {
                modulos.splice(index, 1); await route.fulfill({ status: 204 }); return
            }
        }
        await route.fulfill({ contentType: 'application/json', body: JSON.stringify(body) })
    })
    return consultas
}

for (const [id, ruta, titulo, nuevo, modal] of pantallas) {
    test(`${id} abre su mantenimiento y formulario en la sesión compartida`, async ({ page }) => {
        await preparar(page)
        await page.goto(ruta)
        await expect(page.locator('.ramses-integradas').getByRole('heading', { name: titulo, exact: true })).toBeVisible()
        await expect(page.locator('.user-name')).toHaveText('Administración de prueba')
        await page.getByRole('button', { name: nuevo, exact: true }).click()
        const dialogo = page.getByRole('dialog', { name: modal, exact: true })
        await expect(dialogo).toBeVisible()
        await dialogo.getByRole('button', { name: 'Cancelar', exact: true }).click()
        await expect(dialogo).toHaveCount(0)
        await page.getByRole('link', { name: 'Volver al inicio', exact: true }).click()
        await expect(page).toHaveURL(/\/inicio$/)
    })
}

test('ADM7 respeta TAMPAG y permite navegar sin mostrar más de quince filas', async ({ page }) => {
    await preparar(page, ['ADM7'])
    await page.goto('/modulos')
    await expect(page.locator('.ramses-integradas tbody tr')).toHaveCount(2)
    await expect(page.getByText('Página 1 · 16 registros')).toBeVisible()
    await page.getByRole('button', { name: 'Siguiente', exact: true }).click()
    await expect(page.getByText('Página 2 · 16 registros')).toBeVisible()
    await expect(page.locator('.ramses-integradas tbody tr')).toHaveCount(2)
})

test('sin ADM7 la ruta directa no consulta ni ofrece escritura de módulos', async ({ page }) => {
    const consultas = await preparar(page, [])
    await page.goto('/modulos')
    await expect(page.getByRole('heading', { name: 'Acceso restringido' })).toBeVisible()
    await expect(page.getByRole('button', { name: 'Nuevo módulo' })).toHaveCount(0)
    expect(consultas).not.toContain('/gateway/admin/modulo')
})

test('ADM7 conserva el foco al escribir y muestra validaciones dentro del diálogo', async ({ page }) => {
    await preparar(page, ['ADM7'])
    await page.goto('/modulos')
    await page.getByRole('button', { name: 'Nuevo módulo', exact: true }).click()
    const dialogo = page.getByRole('dialog', { name: 'Crear módulo' })
    const nombre = dialogo.getByLabel('Nombre', { exact: true })
    await nombre.click()
    await nombre.pressSequentially('Prueba de modulo')
    await expect(nombre).toBeFocused()
    await expect(nombre).toHaveValue('Prueba de modulo')
    await dialogo.getByRole('button', { name: 'Guardar', exact: true }).click()
    await expect(dialogo.getByRole('alert')).toContainText('identificador')
    await page.keyboard.press('Escape')
    await expect(dialogo).toHaveCount(0)
})

test('ADM7 muestra el registro guardado en su página y confirma su eliminación', async ({ page }) => {
    await preparar(page, ['ADM7'])
    await page.goto('/modulos')
    await page.getByRole('button', { name: 'Nuevo módulo', exact: true }).click()
    const dialogo = page.getByRole('dialog', { name: 'Crear módulo' })
    await dialogo.getByLabel('Identificador').fill('ZZ')
    await dialogo.getByLabel('Nombre', { exact: true }).fill('Nuevo módulo')
    await dialogo.getByRole('button', { name: 'Guardar', exact: true }).click()
    const fila = page.locator('.ramses-integradas tbody tr').filter({ hasText: 'ZZ' })
    await expect(fila).toBeVisible()
    await expect(page.getByText('Página 9 · 17 registros')).toBeVisible()
    await fila.getByRole('button', { name: 'Editar', exact: true }).click()
    const editar = page.getByRole('dialog', { name: 'Editar módulo' })
    await editar.getByLabel('Nombre', { exact: true }).fill('Módulo actualizado')
    await editar.getByRole('button', { name: 'Guardar', exact: true }).click()
    await expect(fila).toContainText('Módulo actualizado')
    await fila.getByRole('button', { name: 'Eliminar', exact: true }).click()
    const confirmar = page.getByRole('dialog', { name: 'Confirmar eliminación' })
    await expect(confirmar).toContainText('¿Realmente desea eliminar el elemento seleccionado?')
    await confirmar.getByRole('button', { name: 'No, cancelar' }).click()
    await expect(fila).toBeVisible()
    await fila.getByRole('button', { name: 'Eliminar', exact: true }).click()
    await page.getByRole('dialog').getByRole('button', { name: 'Sí, eliminar' }).click()
    await expect(fila).toHaveCount(0)
    await expect(page.getByText('Página 8 · 16 registros')).toBeVisible()
})
