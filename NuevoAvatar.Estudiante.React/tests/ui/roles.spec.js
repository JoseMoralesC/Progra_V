import { test, expect } from '@playwright/test'

test('ADM5 guarda un cambio de permisos para un módulo y vuelve a leerlo', async ({ page }) => {
    const guardados = []
    let asignados = ['ROL_ADMIN']
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
        else if (path === '/gateway/menu') body = [
            { idModulo: 'ADM5', nombreModulo: 'Roles', activo: true, orden: 5 },
            { idModulo: 'ADM7', nombreModulo: 'Módulos', activo: true, orden: 7 }
        ]
        else if (path === '/gateway/admin/rol') body = [
            { idRol: 'ROL_ADMIN', nombreRol: 'Administrador' },
            { idRol: 'ROL_DOCENTE', nombreRol: 'Profesor' }
        ]
        else if (path === '/gateway/admin/parametro/TAMPAG') body = { valor: '1' }
        else if (path === '/gateway/admin/modulo') body = [
            { idModulo: 'ADM8', nombreModulo: 'Instituciones', activo: true, orden: 8 }
        ]
        else if (path === '/gateway/admin/modulo/ADM8/roles' && method === 'GET') body = asignados
        else if (path === '/gateway/admin/modulo/ADM8/roles' && method === 'PUT') {
            asignados = request.postDataJSON()
            guardados.push(asignados)
            status = 204
        } else throw new Error(`Ruta no prevista: ${method} ${path}`)
        await route.fulfill({ status, contentType: 'application/json', body: status === 204 ? undefined : JSON.stringify(body) })
    })

    await page.goto('/roles')
    const listado = page.getByRole('region', { name: 'Listado de roles' })
    await expect(listado.getByText('ROL_ADMIN', { exact: true })).toBeVisible()
    await listado.getByRole('button', { name: 'Siguiente' }).click()
    await expect(listado.getByText('ROL_DOCENTE', { exact: true })).toBeVisible()
    const admin = page.getByRole('checkbox', { name: 'ADM8 Administrador' })
    const profesor = page.getByRole('checkbox', { name: 'ADM8 Profesor' })
    await expect(admin).toBeChecked()
    await expect(profesor).not.toBeChecked()
    await admin.uncheck()
    await profesor.check()
    await page.getByRole('button', { name: 'Guardar ADM8' }).click()
    await expect(page.getByRole('status').filter({ hasText: 'Permisos de ADM8 guardados' })).toBeVisible()
    expect(guardados).toEqual([['ROL_DOCENTE']])
    await expect(admin).not.toBeChecked()
    await expect(profesor).toBeChecked()
})
