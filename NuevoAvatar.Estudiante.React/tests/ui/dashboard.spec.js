import { test, expect } from '@playwright/test'
import { cerrarBienvenida } from './interacciones-administracion'

test('ADM3 incluye facturas pendientes de períodos cerrados en el indicador', async ({ page }) => {
    let visitas = 0
    await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({
        access_token: 'token-prueba', refresh_token: 'refresh-prueba',
        usuarioID: 'admin@cuc.ac.cr', expires_in: new Date(Date.now() + 3600000).toISOString()
    })))
    await page.route('**/gateway/**', async route => {
        const url = new URL(route.request().url())
        if (url.pathname === '/gateway/menu' && url.searchParams.get('vista') === 'dashboard') visitas++
        if (url.pathname === '/gateway/fin/notificar/historial') {
            expect(url.searchParams.get('vista')).toBe('dashboard')
        }
        const respuestas = {
            '/gateway/auth/sesion': { nombre: 'Ana', rol: 'ADMINISTRADOR' },
            '/gateway/menu': ['ADM3', 'ADM4', 'ADM9', 'ADM10', 'ADM12', 'ADM22'].map((idModulo, orden) =>
                ({ idModulo, nombreModulo: idModulo, activo: true, orden })),
            '/gateway/admin/bitacora': [{ idBitacora: 1, usuario: 'admin@cuc.ac.cr', descripcion: 'Consulta · Usuarios', fechaHora: '2026-11-08T12:00:00Z' }],
            '/gateway/admin/usuario': [{}, {}],
            '/gateway/admin/carrera': [{}],
            '/gateway/admin/curso': [{}, {}, {}],
            '/gateway/admin/periodo': url.searchParams.has('estado')
                ? [{ periodoId: 20, estado: 'ACTIVO' }]
                : [{ periodoId: 20, estado: 'ACTIVO' }, { periodoId: 19, estado: 'CERRADO' }],
            '/gateway/fin/factura': { pendientes: 1 },
            '/gateway/fin/notificar/historial': {
                items: [{ id: 1, asunto: 'Aviso académico', estado: 'ENVIADA', fecha: '2026-11-05T10:00:00Z' }]
            }
        }
        if (!(url.pathname in respuestas)) throw new Error(`Ruta no prevista: ${url.pathname}`)
        if (url.pathname === '/gateway/fin/factura') {
            expect(url.searchParams.get('vista')).toBe('dashboard')
            expect(['19', '20']).toContain(url.searchParams.get('periodoId'))
        }
        await route.fulfill({ contentType: 'application/json', body: JSON.stringify(respuestas[url.pathname]) })
    })
    await page.goto('/inicio')
    await cerrarBienvenida(page)
    await expect(page.getByRole('heading', { name: 'Bienvenido, Ana' })).toBeVisible()
    const metricas = page.getByRole('region', { name: 'Indicadores administrativos' })
    await expect(metricas.getByRole('heading', { name: 'Usuarios' }).locator('..')).toContainText('2')
    await expect(metricas.getByRole('heading', { name: 'Períodos activos' }).locator('..')).toContainText('1')
    await expect(metricas.getByRole('heading', { name: 'Facturas pendientes' }).locator('..')).toContainText('2')
    await expect(page.getByText('Aviso académico', { exact: false })).toBeVisible()
    await expect(page.getByRole('region', { name: 'Bitácoras recientes' }).getByRole('cell', { name: 'Consulta', exact: true })).toBeVisible()
    await expect(page.getByRole('region', { name: 'Bitácoras recientes' }).getByRole('cell', { name: 'Usuarios', exact: true })).toBeVisible()
    expect(visitas).toBe(1)
    await expect(page.getByRole('link', { name: 'Ayuda/soporte' })).toHaveAttribute('href', 'mailto:soportenuevoavatar@gmail.com')
    await expect(page.getByRole('link', { name: 'Mi perfil' })).toHaveAttribute('href', '/perfil')
})

test('ADM3 presenta los grupos activos del profesor', async ({ page }) => {
    await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({
        access_token: 'token-prueba', refresh_token: 'refresh-prueba',
        usuarioID: 'profesor@cuc.ac.cr', expires_in: new Date(Date.now() + 3600000).toISOString()
    })))
    await page.route('**/gateway/**', async route => {
        const path = new URL(route.request().url()).pathname
        const respuestas = {
            '/gateway/auth/sesion': { nombre: 'Luis', rol: 'PROFESOR' },
            '/gateway/menu': [{ idModulo: 'ADM17', nombreModulo: 'Notas', activo: true, orden: 17 }],
            '/gateway/admin/bitacora': [],
            '/gateway/fin/notificar/historial': { items: [] },
            '/gateway/mat/profesor/grupos': [{ grupoId: 1, numeroGrupo: 2, curso: 'Programación V', horario: 'Lunes' }]
        }
        if (!(path in respuestas)) throw new Error(`Ruta no prevista: ${path}`)
        await route.fulfill({ contentType: 'application/json', body: JSON.stringify(respuestas[path]) })
    })
    await page.goto('/inicio')
    await cerrarBienvenida(page)
    await expect(page.getByText('Grupo 2: Programación V', { exact: true })).toBeVisible()
    await expect(page.getByRole('link', { name: 'Consultar y registrar notas' })).toHaveAttribute('href', '/notas')
    await expect(page.getByText('No hay notificaciones recientes.')).toBeVisible()
    await expect(page.getByText('No hay bitácoras recientes.')).toBeVisible()
})


test('ADM3 informa el fallo de auditoría y de grupos sin ocultar soporte', async ({ page }) => {
    await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({
        access_token: 'token-prueba', refresh_token: 'refresh-prueba',
        usuarioID: 'profesor@cuc.ac.cr', expires_in: new Date(Date.now() + 3600000).toISOString()
    })))
    await page.route('**/gateway/**', async route => {
        const url = new URL(route.request().url())
        if (url.pathname === '/gateway/auditoria/interfaz') {
            await route.fulfill({ status: 204 })
            return
        }
        if (url.pathname === '/gateway/mat/profesor/grupos' || url.searchParams.get('vista') === 'dashboard') {
            await route.fulfill({ status: 503, contentType: 'application/json', body: JSON.stringify({ error: 'Servicio no disponible' }) })
            return
        }
        const respuestas = {
            '/gateway/auth/sesion': { nombre: 'Luis', rol: 'PROFESOR' },
            '/gateway/menu': [{ idModulo: 'ADM17', nombreModulo: 'Notas', activo: true, orden: 17 }]
        }
        if (!(url.pathname in respuestas)) throw new Error(`Ruta no prevista: ${url.pathname}`)
        await route.fulfill({ contentType: 'application/json', body: JSON.stringify(respuestas[url.pathname]) })
    })
    await page.goto('/inicio')
    await cerrarBienvenida(page)
    await expect(page.getByRole('alert').filter({ hasText: 'No se pudo registrar la visualización del dashboard.' })).toBeVisible()
    await expect(page.getByRole('alert').filter({ hasText: 'El servicio no está disponible.' })).toHaveCount(3)
    await expect(page.getByText('Cargando grupos…')).toHaveCount(0)
    await expect(page.getByRole('link', { name: 'Ayuda/soporte' })).toHaveAttribute('href', 'mailto:soportenuevoavatar@gmail.com')
    await expect(page.getByRole('link', { name: 'Mi perfil' })).toHaveAttribute('href', '/perfil')
})


test('ADM3 muestra al profesor los resúmenes del servidor sin controles de filtrado', async ({ page }) => {
    await page.addInitScript(() => sessionStorage.setItem('nuevo-avatar.sesion', JSON.stringify({
        access_token: 'token-prueba', refresh_token: 'refresh-prueba',
        usuarioID: 'profesor@cuc.ac.cr', expires_in: new Date(Date.now() + 3600000).toISOString()
    })))
    await page.route('**/gateway/**', async route => {
        const url = new URL(route.request().url())
        const respuestas = {
            '/gateway/auth/sesion': { nombre: 'Luis', rol: 'PROFESOR' },
            '/gateway/menu': [{ idModulo: 'ADM17', nombreModulo: 'Notas', activo: true, orden: 17 }],
            '/gateway/mat/profesor/grupos': [],
            '/gateway/admin/bitacora': Array.from({ length: 10 }, (_, i) => ({
                idBitacora: i + 1, usuario: 'profesor@cuc.ac.cr', descripcion: 'Actualización · Notas', fechaHora: '2026-11-08T12:00:00Z'
            })),
            '/gateway/fin/notificar/historial': { items: [{ id: 1, asunto: 'Aviso para el profesor', estado: 'ENVIADA', fecha: '2026-11-08T12:00:00Z' }] }
        }
        if (!(url.pathname in respuestas)) throw new Error(`Ruta no prevista: ${url.pathname}`)
        if (url.pathname === '/gateway/admin/bitacora' || url.pathname === '/gateway/fin/notificar/historial')
            expect(url.search).toBe('?vista=dashboard')
        await route.fulfill({ contentType: 'application/json', body: JSON.stringify(respuestas[url.pathname]) })
    })
    await page.goto('/inicio')
    await cerrarBienvenida(page)
    const bitacoras = page.getByRole('region', { name: 'Bitácoras recientes' })
    const notificaciones = page.getByRole('region', { name: 'Notificaciones recientes' })
    await expect(bitacoras.locator('tbody tr')).toHaveCount(10)
    await expect(notificaciones).toContainText('Aviso para el profesor')
    await expect(bitacoras.locator('input, select, button')).toHaveCount(0)
    await expect(notificaciones.locator('input, select, button')).toHaveCount(0)
})
