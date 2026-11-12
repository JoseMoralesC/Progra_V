import { test, expect } from '@playwright/test'
import { cerrarBienvenida, accionar } from './interacciones-administracion'

test('ADM6 usa el servicio para crear, editar y eliminar parámetros', async ({ page }) => {
    const registros = [
        { idParametro: 'TAMPAG', valor: '2' },
        { idParametro: 'DOMESTUD', valor: 'cuc.cr' },
        { idParametro: 'DOMPROF', valor: 'cuc.ac.cr' }
    ]
    const operaciones = []
    await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({
        access_token: 'token-prueba', refresh_token: 'refresh-prueba',
        usuarioID: 'admin@cuc.ac.cr', expires_in: new Date(Date.now() + 3600000).toISOString()
    })))
    await page.route('**/gateway/**', async route => {
        const request = route.request()
        const path = new URL(request.url()).pathname
        const method = request.method()
        let body, status = 200
        if (path === '/gateway/auth/sesion') body = { usuarioID: 'admin@cuc.ac.cr', nombre: 'Ana Mora', rol: 'ADMINISTRADOR' }
        else if (path === '/gateway/auditoria/interfaz' && method === 'POST') status = 204
        else if (path === '/gateway/menu') body = [{ idModulo: 'ADM6', nombreModulo: 'Parámetros', activo: true, orden: 6 }]
        else if (path === '/gateway/admin/parametro/TAMPAG' && method === 'GET') body = registros[0]
        else if (path === '/gateway/admin/parametro' && method === 'GET') body = registros
        else if (path === '/gateway/admin/parametro' && method === 'POST') {
            body = request.postDataJSON()
            registros.push(body)
            status = 201
            operaciones.push({ method, body })
        } else if (path.startsWith('/gateway/admin/parametro/') && method === 'PUT') {
            body = registros.find(item => item.idParametro === path.split('/').at(-1))
            body.valor = request.postDataJSON().valor
            operaciones.push({ method, body: { ...body } })
        } else if (path.startsWith('/gateway/admin/parametro/') && method === 'DELETE') {
            const id = path.split('/').at(-1)
            body = registros.splice(registros.findIndex(item => item.idParametro === id), 1)[0]
            operaciones.push({ method, body })
        } else throw new Error(`Ruta no prevista: ${method} ${path}`)
        await route.fulfill({ status, contentType: 'application/json', body: status === 204 ? undefined : JSON.stringify(body) })
    })
    page.on('dialog', dialog => dialog.accept())
    await page.goto('/parametros')
    await cerrarBienvenida(page)
    await expect(page.getByRole('row')).toHaveCount(3)
    await page.getByRole('button', { name: 'Siguiente' }).click()
    await expect(page.getByText('DOMPROF')).toBeVisible()

    await page.getByRole('button', { name: 'Nuevo parámetro' }).click()
    await expect(page.getByRole('dialog', { name: 'Nuevo parámetro' })).toBeVisible()
    await page.getByLabel('ID', { exact: true }).fill('PRUEBA')
    await page.getByLabel('Valor', { exact: true }).fill('valor de prueba')
    await page.getByRole('button', { name: 'Guardar' }).click()
    await expect(page.getByRole('status').filter({ hasText: 'Parámetro guardado' })).toBeVisible()
    expect(operaciones[0]).toEqual({ method: 'POST', body: { idParametro: 'PRUEBA', valor: 'valor de prueba' } })
    await expect(page.getByRole('row').filter({ hasText: 'PRUEBA' })).toBeVisible()
    await page.getByRole('button', { name: 'Anterior' }).click()

    await accionar(page, page.getByRole('row').filter({ hasText: 'TAMPAG' }), 'Editar')
    await expect(page.getByRole('dialog', { name: 'Editar parámetro' })).toBeVisible()
    await page.getByLabel('Valor', { exact: true }).fill('3')
    await page.getByRole('button', { name: 'Guardar' }).click()
    expect(operaciones.some(item => item.method === 'PUT' && item.body.idParametro === 'TAMPAG' && item.body.valor === '3')).toBe(true)

    await accionar(page, page.getByRole('row').filter({ hasText: 'DOMESTUD' }), 'Eliminar')
    await page.getByRole('dialog').getByRole('button', { name: 'Sí', exact: true }).click()
    await expect(page.getByRole('status').filter({ hasText: 'Parámetro eliminado' })).toBeVisible()
    expect(operaciones.some(item => item.method === 'DELETE' && item.body.idParametro === 'DOMESTUD')).toBe(true)
})
