import { test, expect } from '@playwright/test'

const key = 'nuevo-avatar.sesion'

async function escenario(page, rol = 'PROFESOR') {
    const ahora = Date.now()
    const tokens = {
        access_token: 'token-inicial', refresh_token: 'refresh-inicial',
        usuarioID: rol === 'ESTUDIANTE' ? 'ana@cuc.cr' : 'ana@cuc.ac.cr',
        expires_in: new Date(ahora + 60000).toISOString(),
    }
    const estado = { login: 201, sesion: 200, refresh: 200, logout: 204, menu: 200,
        permiso: true, renovaciones: 0, consultas: 0, auditorias: [] }
    const errores = []
    page.on('pageerror', error => errores.push(error.message))
    await page.route('**/gateway/**', async route => {
        const path = new URL(route.request().url()).pathname
        let status, body
        if (path.endsWith('/login')) {
            status = estado.login; body = tokens
        } else if (path.endsWith('/sesion')) {
            estado.consultas++
            status = estado.sesion; body = { usuarioID: tokens.usuarioID, nombre: 'Ana Mora', rol }
        } else if (path === '/gateway/menu') {
            status = estado.menu
            body = estado.permiso ? [{ idModulo: 'ADM17', nombreModulo: 'Notas', activo: true, orden: 1 }] : []
        } else if (path === '/gateway/auditoria/interfaz') {
            expect(route.request().method()).toBe('POST')
            expect(route.request().headers().authorization).toMatch(/^Bearer (token-inicial|token-renovado)$/)
            const evento = route.request().postDataJSON()
            expect(['navegacion', 'error-ui']).toContain(evento.tipo)
            estado.auditorias.push(evento)
            status = 204
        } else if (path === '/gateway/expediente/propio') {
            status = 404; body = { error: 'No hay expediente vinculado' }
        } else if (path === '/gateway/mat/prematricula/propio') {
            status = 200; body = []
        } else if (path === '/gateway/admin/bitacora' || path === '/gateway/mat/profesor/grupos') {
            status = 200; body = []
        } else if (path === '/gateway/fin/notificar/historial') {
            status = 200; body = { items: [] }
        } else if (path.endsWith('/refresh')) {
            estado.renovaciones++
            status = estado.refresh
            body = { ...tokens, access_token: 'token-renovado', refresh_token: 'refresh-rotado',
                expires_in: new Date(ahora + 3600000).toISOString() }
        } else if (path.endsWith('/logout')) {
            status = estado.logout; body = {}
        } else {
            throw new Error('Ruta inesperada en la integración ADM2: ' + path)
        }
        await route.fulfill({ status, contentType: 'application/json',
            body: status === 204 ? undefined : JSON.stringify(body) })
    })
    return { estado, tokens, errores }
}

async function ingresar(page, email = 'ana@cuc.ac.cr') {
    await page.goto('/login')
    await page.getByLabel('Correo institucional').fill(email)
    await page.getByLabel('Contraseña', { exact: true }).fill('clave-de-prueba')
    await page.getByRole('button', { name: 'Ingresar', exact: true }).click()
}

for (const [rol, titulo] of [
    ['ADMINISTRADOR', 'Inicio de administración'],
    ['PROFESOR', 'Inicio del profesor'],
    ['ESTUDIANTE', 'Inicio del estudiante'],
]) {
    test(`login ${rol} llega al layout con identidad real y restaura al recargar`, async ({ page }) => {
        const { errores, estado } = await escenario(page, rol)
        await ingresar(page, rol === 'ESTUDIANTE' ? 'ana@cuc.cr' : 'ana@cuc.ac.cr')
        await expect(page).toHaveURL(rol === 'ESTUDIANTE' ? /\/estudiante$/ : /\/inicio$/)
        await expect(page.locator('.header-page-title')).toHaveText(rol === 'ESTUDIANTE' ? titulo : 'Inicio')
        await expect(page.locator('.user-name')).toHaveText('Ana Mora')
        await expect(page.locator('.user-role')).toHaveText(rol)
        await expect(page.getByRole('button', { name: 'Salir', exact: true })).toBeEnabled()
        await expect(page.getByRole('navigation', { name: 'Navegación principal' }).getByRole('link')).toHaveCount(rol === 'ESTUDIANTE' ? 4 : rol === 'PROFESOR' ? 2 : 1)
        if (rol === 'ESTUDIANTE') await expect.poll(() => estado.auditorias.some(evento =>
            evento.tipo === 'navegacion' && evento.ruta === '/estudiante')).toBeTruthy()
        expect(await page.evaluate(k => sessionStorage.getItem(k), key)).not.toContain('clave-de-prueba')
        await page.reload()
        await expect(page.locator('.user-name')).toHaveText('Ana Mora')
        expect(errores).toEqual([])
    })
}

test('rutas directas sin sesión vuelven al login sin mostrar identidad de ejemplo', async ({ page }) => {
    await escenario(page)
    for (const path of ['/inicio', '/usuarios', '/expediente', '/notas']) {
        await page.goto(path)
        await expect(page).toHaveURL(/\/login$/)
        await expect(page.getByRole('heading', { name: 'Iniciar sesión' })).toBeVisible()
        await expect(page.locator('.user-name')).toHaveCount(0)
    }
})

test('credenciales inválidas no abren el layout', async ({ page }) => {
    const { estado } = await escenario(page)
    estado.login = 401
    await ingresar(page)
    await expect(page.getByRole('alert')).toHaveText('Usuario y/o contraseña incorrectos')
    await expect(page).toHaveURL(/\/login$/)
    expect(await page.evaluate(k => sessionStorage.getItem(k), key)).toBeNull()
})

test('identidad rechazada después del login no conserva tokens ni abre el layout', async ({ page }) => {
    const { estado } = await escenario(page)
    estado.sesion = 403
    await ingresar(page)
    await expect(page.getByRole('alert')).toContainText('No tienes permiso')
    expect(await page.evaluate(k => sessionStorage.getItem(k), key)).toBeNull()
})

test('renovación programada mantiene el layout y rota los tokens sin volver al login', async ({ page }) => {
    const { estado } = await escenario(page)
    await page.clock.install()
    await ingresar(page)
    await expect(page.locator('.user-name')).toHaveText('Ana Mora')
    await page.clock.fastForward(46000)
    await expect.poll(() => estado.renovaciones).toBe(1)
    await expect.poll(() => page.evaluate(k => JSON.parse(sessionStorage.getItem(k))?.refresh_token, key)).toBe('refresh-rotado')
    await expect(page).toHaveURL(/\/inicio$/)
    await expect(page.locator('.user-name')).toHaveText('Ana Mora')
})

test('refresh rechazado retira la identidad y vuelve al login con aviso de expiración', async ({ page }) => {
    const { estado } = await escenario(page)
    await page.clock.install()
    await ingresar(page)
    await expect(page.locator('.user-name')).toBeVisible()
    estado.refresh = 401
    await page.clock.fastForward(46000)
    await expect(page).toHaveURL(/\/login$/)
    await expect(page.getByRole('alert')).toContainText('La sesión venció')
    await expect(page.locator('.user-name')).toHaveCount(0)
    expect(await page.evaluate(k => sessionStorage.getItem(k), key)).toBeNull()
})

test('token vencido guardado se renueva una sola vez al restaurar bajo StrictMode', async ({ page }) => {
    const { estado, tokens } = await escenario(page)
    tokens.expires_in = new Date(Date.now() - 1000).toISOString()
    await page.addInitScript(({ key, tokens }) => sessionStorage.setItem(key, JSON.stringify(tokens)), { key, tokens })
    await page.goto('/inicio')
    await expect(page.locator('.user-name')).toHaveText('Ana Mora')
    expect(estado.renovaciones).toBe(1)
    expect(estado.consultas).toBe(1)
})

test('caída al restaurar conserva tokens, oculta el layout y permite reintentar', async ({ page }) => {
    const { estado } = await escenario(page)
    await ingresar(page)
    await expect(page.locator('.user-name')).toBeVisible()
    estado.sesion = 503
    await page.reload()
    await expect(page.getByRole('heading', { name: 'No se pudo comprobar tu sesión' })).toBeVisible()
    await expect(page.locator('.user-name')).toHaveCount(0)
    expect(await page.evaluate(k => sessionStorage.getItem(k), key)).not.toBeNull()
    estado.sesion = 200
    await page.getByRole('button', { name: 'Reintentar', exact: true }).click()
    await expect(page.locator('.user-name')).toHaveText('Ana Mora')
})

test('salir limpia la cuenta aunque Auth falle; volver atrás no recupera el layout', async ({ page }) => {
    const { estado } = await escenario(page)
    await ingresar(page)
    await expect(page.locator('.user-name')).toBeVisible()
    await page.goto('/login')
    await expect(page).toHaveURL(/\/inicio$/)
    estado.logout = 503
    await page.getByRole('button', { name: 'Salir', exact: true }).click()
    await expect(page).toHaveURL(/\/login$/)
    await expect(page.getByRole('alert')).toContainText('se cerró en este dispositivo')
    expect(await page.evaluate(k => sessionStorage.getItem(k), key)).toBeNull()
    await page.goBack()
    await expect(page.getByRole('heading', { name: 'Iniciar sesión' })).toBeVisible()
    await expect(page.locator('.user-name')).toHaveCount(0)
    await page.reload()
    await expect(page.getByRole('heading', { name: 'Iniciar sesión' })).toBeVisible()
})

test('salir mientras se renueva impide que la respuesta tardía restaure la cuenta', async ({ page }) => {
    await escenario(page)
    await page.clock.install()
    let liberar
    await page.route('**/gateway/auth/refresh', async route => {
        await new Promise(resolve => { liberar = resolve })
        await route.fulfill({ json: { access_token: 'tardío', refresh_token: 'tardío-refresh',
            usuarioID: 'ana@cuc.ac.cr', expires_in: new Date(Date.now() + 3600000).toISOString() } })
    })
    await ingresar(page)
    await expect(page.locator('.user-name')).toBeVisible()
    await page.clock.fastForward(46000)
    await expect.poll(() => typeof liberar).toBe('function')
    await page.getByRole('button', { name: 'Salir', exact: true }).click()
    await expect(page).toHaveURL(/\/login$/)
    const respuesta = page.waitForResponse('**/gateway/auth/refresh')
    liberar()
    await respuesta
    await expect(page.getByRole('heading', { name: 'Iniciar sesión' })).toBeVisible()
    expect(await page.evaluate(k => sessionStorage.getItem(k), key)).toBeNull()
})

test('perfil no reconocido en la identidad no accede al layout', async ({ page }) => {
    await escenario(page, 'OTRO')
    await ingresar(page)
    await expect(page.getByRole('alert')).toContainText('no tiene un perfil autorizado')
    await expect(page.locator('.user-name')).toHaveCount(0)
    expect(await page.evaluate(k => sessionStorage.getItem(k), key)).toBeNull()
})

test('el estudiante no abre una ruta administrativa de notas', async ({ page }) => {
    await escenario(page, 'ESTUDIANTE')
    await ingresar(page, 'ana@cuc.cr')
    await expect(page.locator('.user-name')).toBeVisible()
    await page.goto('/notas')
    await expect(page).toHaveURL(/\/estudiante$/)
    await expect(page.getByRole('heading', { name: 'Inicio del estudiante', exact: true })).toBeVisible()
    await expect(page.getByRole('heading', { name: 'Desglose y notas', exact: true })).toHaveCount(0)
})

test('login móvil conserva formulario visible sin desbordamiento horizontal', async ({ page }) => {
    await escenario(page)
    await page.setViewportSize({ width: 390, height: 844 })
    await page.goto('/login')
    await expect(page.getByRole('button', { name: 'Ingresar', exact: true })).toBeVisible()
    expect(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth)).toBeTruthy()
})

test('retiro de ADM17 se refleja en el menú al recargar', async ({ page }) => {
    const { estado } = await escenario(page)
    await ingresar(page)
    await expect(page.getByRole('link', { name: 'Notas', exact: true })).toBeVisible()
    estado.permiso = false
    await page.reload()
    await expect(page.getByText('Sin permiso para consultar grupos.')).toBeVisible()
    await expect(page.getByRole('link', { name: 'Notas', exact: true })).toHaveCount(0)
    await expect(page.getByRole('link', { name: 'Consultar y registrar notas' })).toHaveCount(0)
})

test('caída de permisos durante restauración oculta el layout y permite recuperar', async ({ page }) => {
    const { estado } = await escenario(page)
    await ingresar(page)
    await expect(page.locator('.user-name')).toBeVisible()
    estado.menu = 503
    await page.reload()
    await expect(page.getByRole('heading', { name: 'No se pudo comprobar tu sesión' })).toBeVisible()
    await expect(page.locator('.user-name')).toHaveCount(0)
    estado.menu = 200
    await page.getByRole('button', { name: 'Reintentar', exact: true }).click()
    await expect(page.locator('.user-name')).toBeVisible()
})
