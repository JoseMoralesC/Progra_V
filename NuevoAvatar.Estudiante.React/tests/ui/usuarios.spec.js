import { test, expect } from '@playwright/test'

test('ADM4 crea y edita usuarios usando el ID real del rol', async ({ page }) => {
    const usuarios = [
        { email: 'ana@cuc.cr', tipoIdentificacion: 'Cédula', identificacion: '101',
            nombre: 'Ana Mora', idRol: 'ROL_ESTUDIANTE_01', fechaModificacion: '2026-11-05T12:00:00Z' },
        { email: 'beto@cuc.ac.cr', tipoIdentificacion: 'Cédula', identificacion: '102',
            nombre: 'Beto Rojas', idRol: 'ROL_DOCENTE_02', fechaModificacion: '2026-11-05T12:00:00Z' },
        { email: 'carla@cuc.cr', tipoIdentificacion: 'Cédula', identificacion: '103',
            nombre: 'Carla Ruiz', idRol: 'ROL_ESTUDIANTE_01', fechaModificacion: '2026-11-05T12:00:00Z' }
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
        if (path === '/gateway/auth/sesion') body = { usuarioID: 'admin@cuc.ac.cr', nombre: 'Admin', rol: 'ADMINISTRADOR' }
        else if (path === '/gateway/auditoria/interfaz' && method === 'POST') status = 204
        else if (path === '/gateway/menu') body = [{ idModulo: 'ADM4', nombreModulo: 'Usuarios', activo: true, orden: 4 }]
        else if (path === '/gateway/admin/rol') body = [
            { idRol: 'ROL_ESTUDIANTE_01', nombreRol: 'Estudiante' },
            { idRol: 'ROL_DOCENTE_02', nombreRol: 'Profesor' }
        ]
        else if (path === '/gateway/admin/parametro/TAMPAG') body = { valor: '2' }
        else if (path === '/gateway/admin/usuario' && method === 'GET') body = usuarios
        else if (path.startsWith('/gateway/admin/usuario/') && method === 'GET') {
            operaciones.push({ method, path })
            body = usuarios.find(item => item.email === decodeURIComponent(path.split('/').at(-1)))
        }
        else if (path === '/gateway/admin/usuario' && method === 'POST') {
            const datos = request.postDataJSON()
            operaciones.push({ method, datos })
            body = { ...datos, contrasena: undefined, fechaModificacion: '2026-11-05T12:00:00Z' }
            usuarios.push(body)
            status = 201
        } else if (path.startsWith('/gateway/admin/usuario/') && method === 'PUT') {
            const datos = request.postDataJSON()
            operaciones.push({ method, datos })
            body = usuarios.find(item => item.email === decodeURIComponent(path.split('/').at(-1)))
            Object.assign(body, datos)
        } else throw new Error(`Ruta no prevista: ${method} ${path}`)
        await route.fulfill({ status, contentType: 'application/json', body: status === 204 ? undefined : JSON.stringify(body) })
    })

    await page.goto('/usuarios')
    await expect(page.getByRole('row').filter({ hasText: 'ana@cuc.cr' })).toBeVisible()
    await expect(page.getByText('Página 1 de 2')).toBeVisible()
    await page.getByRole('button', { name: 'Siguiente' }).click()
    await expect(page.getByRole('row').filter({ hasText: 'carla@cuc.cr' })).toBeVisible()
    await page.getByRole('button', { name: 'Anterior' }).click()
    await page.getByRole('button', { name: 'Email (ID)' }).click()
    await expect(page.locator('tbody tr').first()).toContainText('carla@cuc.cr')
    await page.getByRole('button', { name: 'Email (ID)' }).click()
    await page.getByLabel('Dominio').selectOption('cuc.ac.cr')
    await page.getByLabel('Rol', { exact: true }).first().selectOption('ROL_DOCENTE_02')
    await page.getByRole('button', { name: 'Filtrar' }).click()
    await expect(page.getByRole('row').filter({ hasText: 'beto@cuc.ac.cr' })).toBeVisible()
    await expect(page.getByRole('row').filter({ hasText: 'ana@cuc.cr' })).toHaveCount(0)
    await page.getByLabel('Dominio').selectOption('')
    await page.getByLabel('Rol', { exact: true }).first().selectOption('')
    await page.getByRole('button', { name: 'Filtrar' }).click()
    await page.getByRole('button', { name: 'Nuevo usuario' }).click()
    await expect(page.getByRole('dialog', { name: 'Nuevo usuario' })).toBeVisible()
    await page.getByLabel('Correo', { exact: true }).fill('profesor@cuc.ac.cr')
    await page.getByLabel('Tipo de identificación', { exact: true }).last().fill('Cédula')
    await page.getByLabel('Identificación', { exact: true }).last().fill('202')
    await page.getByLabel('Nombre', { exact: true }).last().fill('Luis Pérez')
    await page.getByLabel('Rol', { exact: true }).last().selectOption('ROL_DOCENTE_02')
    await page.getByLabel('Contraseña', { exact: true }).fill('clave-de-prueba')
    await page.getByRole('button', { name: 'Guardar' }).click()
    await expect(page.getByRole('row').filter({ hasText: 'profesor@cuc.ac.cr' })).toBeVisible()
    expect(operaciones[0].datos.idRol).toBe('ROL_DOCENTE_02')
    expect(operaciones[0].datos.contrasena).toBe('clave-de-prueba')

    await page.getByRole('row').filter({ hasText: 'profesor@cuc.ac.cr' }).getByRole('button', { name: 'Editar' }).click()
    await expect(page.getByRole('dialog', { name: 'Editar usuario' })).toBeVisible()
    await page.getByLabel('Nombre', { exact: true }).last().fill('Luis Morales')
    await page.getByRole('button', { name: 'Guardar' }).click()
    expect(operaciones[1].datos).not.toHaveProperty('contrasena')
    await expect(page.getByRole('row').filter({ hasText: 'Luis Morales' })).toBeVisible()
    await page.getByRole('row').filter({ hasText: 'Luis Morales' }).getByRole('button', { name: 'Detalle' }).click()
    await expect(page.getByRole('dialog', { name: 'Detalle de usuario' })).toContainText('202')
    await page.getByRole('button', { name: 'Cerrar', exact: true }).click()
    await expect(page.getByRole('dialog')).toHaveCount(0)
    expect(operaciones.at(-1).method).toBe('GET')
    expect(decodeURIComponent(operaciones.at(-1).path)).toBe('/gateway/admin/usuario/profesor@cuc.ac.cr')
})
