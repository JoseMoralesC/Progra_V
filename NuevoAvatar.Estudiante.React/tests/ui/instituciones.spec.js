import { test, expect } from '@playwright/test'

async function preparar(page, conPermiso = true) {
    const registros = [
        { id: 1, nombre: 'Colegio Central' },
        { id: 2, nombre: 'Instituto Norte' },
        { id: 3, nombre: 'Universidad del Sur' }
    ]
    const llamadas = []
    await page.addInitScript(() => {
        sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({
            access_token: 'token-prueba', refresh_token: 'refresh-prueba',
            usuarioID: 'admin@cuc.ac.cr', expires_in: new Date(Date.now() + 3600000).toISOString()
        }))
    })
    await page.route('**/gateway/**', async route => {
        const peticion = route.request()
        const url = new URL(peticion.url())
        const path = url.pathname
        const metodo = peticion.method()
        llamadas.push({ path, metodo, query: url.searchParams.get('nombre') })
        let status = 200
        let body = null
        if (path === '/gateway/auth/sesion') body = { usuarioID: 'admin@cuc.ac.cr', nombre: 'Ana Mora', rol: 'ADMINISTRADOR' }
        else if (path === '/gateway/menu') body = conPermiso ? [{ idModulo: 'ADM8', nombreModulo: 'Instituciones', activo: true, orden: 8 }] : []
        else if (path === '/gateway/admin/parametro/TAMPAG') body = { valor: '2' }
        else if (path === '/gateway/admin/institucion' && metodo === 'GET') {
            const nombre = url.searchParams.get('nombre')?.toLowerCase()
            body = nombre ? registros.filter(item => item.nombre.toLowerCase().includes(nombre)) : registros
        } else if (path === '/gateway/admin/institucion' && metodo === 'POST') {
            const nombre = peticion.postDataJSON().nombre
            body = { id: 4, nombre }
            registros.push(body)
            status = 201
        } else if (path.startsWith('/gateway/admin/institucion/') && metodo === 'PUT') {
            body = registros.find(item => item.id === Number(path.split('/').at(-1)))
            body.nombre = peticion.postDataJSON().nombre
        } else if (path === '/gateway/admin/institucion/2' && metodo === 'DELETE') {
            status = 409
            body = { mensaje: 'La institución tiene carreras relacionadas' }
        } else if (path.startsWith('/gateway/admin/institucion/') && metodo === 'DELETE') {
            registros.splice(registros.findIndex(item => item.id === Number(path.split('/').at(-1))), 1)
            status = 204
        } else throw new Error(`Ruta no prevista: ${metodo} ${path}`)
        await route.fulfill({ status, contentType: 'application/json', body: status === 204 ? undefined : JSON.stringify(body) })
    })
    return llamadas
}

test('ADM8 permite buscar y mantener instituciones desde la sesión', async ({ page }) => {
    const llamadas = await preparar(page)
    await page.goto('/instituciones')
    await expect(page.locator('.page-head').getByRole('heading', { name: 'Instituciones' })).toBeVisible()
    await expect(page.getByRole('row')).toHaveCount(3)
    await page.getByRole('button', { name: 'Siguiente' }).click()
    await expect(page.getByText('Universidad del Sur')).toBeVisible()
    await page.getByLabel('Buscar por nombre').fill('Colegio')
    await page.getByRole('button', { name: 'Buscar' }).click()
    await expect(page.getByText('Colegio Central')).toBeVisible()
    expect(llamadas.some(item => item.path === '/gateway/admin/institucion' && item.query === 'Colegio')).toBe(true)

    await page.getByRole('button', { name: 'Nueva institución' }).click()
    await expect(page.getByRole('dialog', { name: 'Nueva institución' })).toBeVisible()
    await page.getByLabel('Nombre', { exact: true }).fill('Instituto Nuevo')
    await page.getByRole('button', { name: 'Guardar' }).click()
    await expect(page.getByRole('status').filter({ hasText: 'Institución guardada' })).toBeVisible()
    await page.getByLabel('Buscar por nombre').fill('Nuevo')
    await page.getByRole('button', { name: 'Buscar' }).click()
    await expect(page.getByText('Instituto Nuevo')).toBeVisible()
    await page.getByRole('button', { name: 'Editar' }).click()
    await expect(page.getByRole('dialog', { name: 'Editar institución' })).toBeVisible()
    await page.getByLabel('Nombre', { exact: true }).fill('Instituto Mejorado')
    await page.getByRole('button', { name: 'Guardar' }).click()
    await page.getByLabel('Buscar por nombre').fill('Mejorado')
    await page.getByRole('button', { name: 'Buscar' }).click()
    await expect(page.getByText('Instituto Mejorado')).toBeVisible()
})

test('ADM8 muestra dependencia y no abre la pantalla sin permiso', async ({ page }) => {
    await preparar(page)
    page.on('dialog', dialog => dialog.accept())
    await page.goto('/instituciones')
    await page.getByRole('row').filter({ hasText: 'Instituto Norte' }).getByRole('button', { name: 'Eliminar' }).click()
    await page.getByRole('dialog').getByRole('button', { name: 'Sí', exact: true }).click()
    await expect(page.getByRole('alert')).toContainText('La institución tiene carreras relacionadas')
    await preparar(page, false)
    await page.reload()
    await expect(page.getByRole('alert')).toContainText('Acceso restringido')
    await expect(page.getByRole('button', { name: 'Nueva institución' })).toHaveCount(0)
})


test('ADM8 mantiene el listado detrás del modal, valida y cancela sin guardar', async ({ page }) => {
    const llamadas = await preparar(page)
    await page.goto('/instituciones')
    const nuevo = page.getByRole('button', { name: 'Nueva institución' })
    await nuevo.click()
    const modal = page.getByRole('dialog', { name: 'Nueva institución' })
    await expect(modal).toBeVisible()
    expect(await modal.evaluate(elemento => elemento.matches(':modal'))).toBe(true)
    await expect(page.locator('section[aria-label="Listado de instituciones"]')).toBeVisible()
    await modal.getByLabel('Nombre', { exact: true }).fill('Nombre 123')
    await modal.getByRole('button', { name: 'Guardar', exact: true }).click()
    await expect(modal.getByRole('alert')).toBeVisible()
    await expect(modal).toBeVisible()
    await modal.getByRole('button', { name: 'Cancelar' }).click()
    await expect(modal).toHaveCount(0)
    await expect(nuevo).toBeFocused()
    await nuevo.click()
    await modal.getByLabel('Nombre', { exact: true }).fill('Sin guardar')
    await page.keyboard.press('Escape')
    await expect(modal).toHaveCount(0)
    expect(llamadas.filter(item => item.metodo === 'POST' || item.metodo === 'PUT')).toEqual([])
})
