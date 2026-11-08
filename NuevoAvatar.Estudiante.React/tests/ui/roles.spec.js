import { test, expect } from '@playwright/test'

test('ADM5 guarda un cambio de permisos para un módulo y vuelve a leerlo', async ({ page }) => {
    const guardados = []
    const roles = [{ idRol: 'ROL_ADMIN', nombreRol: 'Administrador' }, { idRol: 'ROL_DOCENTE', nombreRol: 'Profesor' }]
    const cambios = []
    let asignados = ['ROL_ADMIN']
    let completarGuardado
    const guardarPendiente = new Promise(resolve => { completarGuardado = resolve })
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
            { idModulo: 'ADM5', nombreModulo: 'Roles', activo: true, orden: 5 }
        ]
        else if (path === '/gateway/admin/rol' && method === 'GET') body = roles
        else if (path === '/gateway/admin/rol' && method === 'POST') {
            body = request.postDataJSON()
            roles.push(body)
            cambios.push(method)
            status = 201
        } else if (path.startsWith('/gateway/admin/rol/') && method === 'PUT') {
            body = roles.find(item => item.idRol === path.split('/').at(-1))
            Object.assign(body, request.postDataJSON())
            cambios.push(method)
        }
        else if (path === '/gateway/admin/parametro/TAMPAG') body = { valor: '1' }
        else if (path === '/gateway/admin/modulo') body = [
            { idModulo: 'ADM8', nombreModulo: 'Instituciones', activo: true, orden: 8 }
        ]
        else if (path === '/gateway/admin/modulo/ADM8/roles' && method === 'GET') body = asignados
        else if (path === '/gateway/admin/modulo/ADM8/roles' && method === 'PUT') {
            await guardarPendiente
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
    const permisos = page.getByRole('dialog', { name: 'Permisos de menú' })
    await expect(permisos).toHaveCount(0)
    await page.getByRole('button', { name: 'Permisos de menú' }).click()
    await expect(permisos).toBeVisible()
    const admin = permisos.getByRole('checkbox', { name: 'ADM8 Administrador' })
    const profesor = permisos.getByRole('checkbox', { name: 'ADM8 Profesor' })
    await expect(admin).toBeChecked()
    await expect(profesor).not.toBeChecked()
    await admin.uncheck()
    await profesor.check()
    await permisos.getByRole('button', { name: 'Cerrar' }).click()
    await expect(permisos).toHaveCount(0)
    expect(guardados).toEqual([])
    await page.getByRole('button', { name: 'Permisos de menú' }).click()
    await expect(admin).toBeChecked()
    await expect(profesor).not.toBeChecked()
    await admin.uncheck()
    await profesor.check()
    await permisos.getByRole('button', { name: 'Guardar ADM8' }).click()
    await expect(permisos.getByRole('button', { name: 'Cerrar' })).toBeDisabled()
    await expect(admin).toBeDisabled()
    await page.keyboard.press('Escape')
    await expect(permisos).toBeVisible()
    completarGuardado()
    await expect(permisos.getByRole('status')).toHaveText('Permisos de ADM8 guardados.')
    expect(guardados).toEqual([['ROL_DOCENTE']])
    await expect(admin).not.toBeChecked()
    await expect(profesor).toBeChecked()
    await admin.check()
    await profesor.uncheck()
    await page.keyboard.press('Escape')
    await expect(permisos).toHaveCount(0)
    await page.getByRole('button', { name: 'Permisos de menú' }).click()
    await expect(admin).not.toBeChecked()
    await expect(profesor).toBeChecked()
    expect(guardados).toEqual([['ROL_DOCENTE']])
    await permisos.getByRole('button', { name: 'Cerrar' }).click()
    await page.getByRole('button', { name: 'Nuevo rol' }).click()
    const nuevo = page.getByRole('dialog', { name: 'Nuevo rol' })
    await expect(nuevo).toBeVisible()
    await nuevo.getByLabel('ID', { exact: true }).fill('ROL_PRUEBA')
    await nuevo.getByLabel('Nombre', { exact: true }).fill('Consulta')
    await nuevo.getByRole('button', { name: 'Guardar', exact: true }).click()
    await expect(nuevo).toHaveCount(0)
    await expect(listado.getByText('Consulta', { exact: true })).toBeVisible()
    await listado.getByRole('button', { name: 'Editar' }).click()
    const editar = page.getByRole('dialog', { name: 'Editar rol' })
    await expect(editar.getByLabel('Nombre', { exact: true })).toHaveValue('Consulta')
    await editar.getByLabel('Nombre', { exact: true }).fill('Consulta General')
    await editar.getByRole('button', { name: 'Guardar', exact: true }).click()
    await expect(editar).toHaveCount(0)
    await expect(listado.getByText('Consulta General', { exact: true })).toBeVisible()
    expect(cambios).toEqual(['POST', 'PUT'])
})
