import { test, expect } from '@playwright/test'

async function escenario(page, { rol = 'PROFESOR', sinDesglose = false } = {}) {
    let rubros = sinDesglose ? [] : [{ id: 9, grupoId: 1, nombre: 'Proyecto', porcentaje: 100 }]
    let notas = []
    const estado = { caida: false, expirar: false, sinGrupos: false, permiso: true,
        consultas: [], escrituras: [], auditorias: [], renovaciones: 0 }
    await page.route('**/gateway/**', async route => {
        const request = route.request(), path = new URL(request.url()).pathname
        let status = 200, body
        if (path.endsWith('/login')) {
            body = { access_token: 'prueba-acceso', refresh_token: 'prueba-refresh',
                usuarioID: 'docente@cuc.ac.cr', expires_in: new Date(Date.now() + 600000).toISOString() }
        } else if (path.endsWith('/sesion')) {
            body = { nombre: 'Docente de prueba', rol, profesorId: rol === 'PROFESOR' ? 1 : null }
        } else if (path === '/gateway/menu') {
            body = estado.permiso ? [{ idModulo: 'ADM17', nombreModulo: 'Notas', activo: true, orden: 1 }] : []
        } else if (path === '/gateway/auditoria/interfaz') {
            expect(request.method()).toBe('POST')
            expect(request.headers().authorization).toBe('Bearer prueba-acceso')
            const evento = request.postDataJSON()
            expect(['navegacion', 'error-ui']).toContain(evento.tipo)
            estado.auditorias.push(evento)
            await route.fulfill({ status: 204 }); return
        } else if (path === '/gateway/expediente/propio') {
            status = 404; body = { error: 'No hay expediente vinculado' }
        } else if (path === '/gateway/mat/prematricula/propio') {
            body = []
        } else if (path === '/gateway/admin/bitacora') {
            body = []
        } else if (path === '/gateway/fin/notificar/historial') {
            body = { items: [] }
        } else if (path.endsWith('/logout')) {
            await route.fulfill({ status: 204 }); return
        } else if (path.endsWith('/refresh')) {
            estado.renovaciones++
            status = 401; body = {}
        } else {
            expect(request.headers().authorization).toBe('Bearer prueba-acceso')
            estado.consultas.push(path)
            if (estado.expirar && (path.endsWith('/obtenerdesglose') || path.endsWith('/obtenernotasgrupo'))) {
                status = 401; body = {}
            }
            else if (estado.caida) { status = 503; body = {} }
            else if (path.endsWith('/profesor/grupos')) {
                body = estado.sinGrupos ? [] : [{ grupoId: 1, numeroGrupo: 1, curso: 'Programación', horario: 'Lunes 18:00' }]
            } else if (path.endsWith('/obtenerdesglose')) body = rubros
            else if (path.endsWith('/obtenernotasgrupo')) body = [{ matriculaId: 12, identificacionEstudiante: '123', notas }]
            else if (path.endsWith('/cargardesglose')) {
                estado.escrituras.push({ method: request.method(), body: request.postDataJSON() })
                rubros = request.postDataJSON().rubros.map((r, i) => ({ ...r, id: i + 9, grupoId: 1 }))
                body = rubros
            } else if (path.endsWith('/asignarnotarubro')) {
                estado.escrituras.push({ method: request.method(), body: request.postDataJSON() })
                notas = [{ ...request.postDataJSON(), id: 8 }]; body = notas[0]
            } else throw new Error('Ruta inesperada: ' + path)
        }
        await route.fulfill({ status, contentType: 'application/json', body: JSON.stringify(body) })
    })
    await page.goto('/login')
    await page.getByLabel('Correo institucional').fill(rol === 'ESTUDIANTE' ? 'estudiante@cuc.cr' : 'docente@cuc.ac.cr')
    await page.getByLabel('Contraseña', { exact: true }).fill('clave-de-prueba')
    await page.getByRole('button', { name: 'Ingresar', exact: true }).click()
    await expect(page).toHaveURL(rol === 'ESTUDIANTE' ? /\/estudiante$/ : /\/inicio$/)
    if (rol === 'PROFESOR') await expect(page.getByText('Grupo 1: Programación', { exact: true })).toBeVisible()
    return estado
}

async function abrirNotas(page) {
    await page.getByRole('navigation', { name: 'Navegación principal' }).getByRole('link', { name: 'Notas', exact: true }).click()
    await expect(page.getByRole('heading', { name: 'Desglose y notas', exact: true })).toBeVisible()
}

test('profesor navega a notas con layout, ruta activa, breadcrumbs y regreso', async ({ page }, testInfo) => {
    const estado = await escenario(page)
    await abrirNotas(page)
    await expect.poll(() => estado.auditorias.some(evento =>
        evento.tipo === 'navegacion' && evento.ruta === '/notas')).toBeTruthy()
    await expect(page.locator('.user-name')).toHaveText('Docente de prueba')
    await expect(page.getByRole('navigation', { name: 'Ruta de navegación' })).toContainText('Inicio / Notas')
    await expect(page.getByRole('navigation', { name: 'Navegación principal' }).getByRole('link', { name: 'Notas', exact: true })).toHaveAttribute('aria-current', 'page')
    await page.getByLabel('Grupo del periodo activo').selectOption('1')
    await expect(page.getByText('Sin notas registradas.')).toBeVisible()
    await page.screenshot({ path: testInfo.outputPath('notas-en-layout.png'), fullPage: true })
    await page.getByRole('link', { name: 'Volver al inicio', exact: true }).click()
    await expect(page.locator('.header-page-title')).toHaveText('Inicio')
    await page.getByRole('link', { name: 'Consultar y registrar notas', exact: true }).click()
    await expect(page).toHaveURL(/\/notas$/)
    await page.reload()
    await expect(page.getByLabel('Grupo del periodo activo')).toBeEnabled()
})

for (const rol of ['ADMINISTRADOR', 'ESTUDIANTE']) {
    test(`${rol} no ve acceso ni ejecuta consultas de notas mediante URL directa`, async ({ page }) => {
        const estado = await escenario(page, { rol })
        await expect(page.getByRole('link', { name: 'Notas', exact: true })).toHaveCount(0)
        await expect(page.getByRole('link', { name: 'Consultar y registrar notas' })).toHaveCount(0)
        await page.goto('/notas')
        if (rol === 'ESTUDIANTE') await expect(page).toHaveURL(/\/estudiante$/)
        else await expect(page.getByRole('heading', { name: 'Acceso restringido' })).toBeVisible()
        await expect(page.getByLabel('Grupo del periodo activo')).toHaveCount(0)
        expect(estado.consultas).toEqual([])
    })
}

test('asignar y modificar nota desde el layout conserva verbos y bloqueo del desglose', async ({ page }) => {
    const estado = await escenario(page)
    await abrirNotas(page)
    await page.getByLabel('Grupo del periodo activo').selectOption('1')
    await expect(page.getByText('Sin notas registradas.')).toBeVisible()
    page.on('dialog', dialog => dialog.accept())
    await page.getByLabel('Nota Proyecto', { exact: true }).fill('85')
    await page.getByRole('button', { name: 'Asignar nota', exact: true }).click()
    await expect(page.getByText('Ya existen notas: el desglose está bloqueado.')).toBeVisible()
    await expect(page.getByLabel('Rubro 1', { exact: true })).toBeDisabled()
    await page.getByLabel('Nota Proyecto', { exact: true }).fill('90')
    await page.getByRole('button', { name: 'Modificar nota', exact: true }).click()
    await expect(page.getByText('Nota guardada.', { exact: true })).toBeVisible()
    expect(estado.escrituras).toEqual([
        { method: 'POST', body: { matriculaId: 12, rubroId: 9, nota: 85 } },
        { method: 'PUT', body: { matriculaId: 12, rubroId: 9, nota: 90 } },
    ])
})

test('carga de desglose usa el cliente compartido y refresca el resultado en el layout', async ({ page }) => {
    const estado = await escenario(page, { sinDesglose: true })
    await abrirNotas(page)
    await page.getByLabel('Grupo del periodo activo').selectOption('1')
    await page.getByLabel('Rubro 1', { exact: true }).fill('Proyecto')
    await page.getByLabel('Porcentaje 1', { exact: true }).fill('100')
    page.on('dialog', dialog => dialog.accept())
    await page.getByRole('button', { name: 'Guardar desglose', exact: true }).click()
    await expect(page.getByText('Desglose guardado.', { exact: true })).toBeVisible()
    expect(estado.escrituras).toEqual([
        { method: 'POST', body: { grupoId: 1, rubros: [{ nombre: 'Proyecto', porcentaje: 100 }] } },
    ])
})

test('fallo de consulta no habilita escritura y permite reintentar sin perder sesión', async ({ page }) => {
    const estado = await escenario(page)
    await abrirNotas(page)
    await expect(page.getByLabel('Grupo del periodo activo')).toBeEnabled()
    estado.caida = true
    await page.getByLabel('Grupo del periodo activo').selectOption('1')
    await expect(page.getByRole('alert')).toContainText('no está disponible')
    await expect(page.getByRole('button', { name: 'Guardar desglose' })).toHaveCount(0)
    estado.caida = false
    await page.getByRole('button', { name: 'Reintentar consulta' }).click()
    await expect(page.getByText('Sin notas registradas.')).toBeVisible()
    await expect(page.locator('.user-name')).toHaveText('Docente de prueba')
    expect(estado.escrituras).toEqual([])
})

test('profesor sin grupos ve el estado vacío dentro del layout', async ({ page }) => {
    const estado = await escenario(page)
    estado.sinGrupos = true
    await abrirNotas(page)
    await expect(page.getByText('No tienes grupos en el periodo activo.')).toBeVisible()
    await expect(page.getByRole('button', { name: 'Guardar desglose' })).toHaveCount(0)
})

test('sesión rechazada al consultar notas abandona el layout y limpia credenciales', async ({ page }) => {
    const estado = await escenario(page)
    estado.expirar = true
    await page.getByRole('link', { name: 'Consultar y registrar notas', exact: true }).click()
    await page.getByLabel('Grupo del periodo activo').selectOption('1')
    await expect(page).toHaveURL(/\/login$/)
    await expect(page.getByRole('alert')).toContainText('La sesión venció')
    await expect(page.getByRole('heading', { name: 'Desglose y notas' })).toHaveCount(0)
    expect(await page.evaluate(() => sessionStorage.getItem('nuevo-avatar.sesion'))).toBeNull()
    expect(estado.renovaciones).toBe(1)
    expect(estado.consultas.some(path => path.endsWith('/obtenerdesglose') || path.endsWith('/obtenernotasgrupo'))).toBeTruthy()
    expect(estado.escrituras).toEqual([])
})

test('salir desde notas retira el módulo; volver atrás no recupera acceso', async ({ page }) => {
    await escenario(page)
    await abrirNotas(page)
    await page.getByRole('button', { name: 'Salir', exact: true }).click()
    await expect(page).toHaveURL(/\/login$/)
    await page.goBack()
    await expect(page.getByRole('heading', { name: 'Iniciar sesión' })).toBeVisible()
    await expect(page.getByLabel('Grupo del periodo activo')).toHaveCount(0)
})

test('profesor sin ADM17 por URL directa no consulta notas y no ve el acceso', async ({ page }) => {
    const estado = await escenario(page)
    estado.permiso = false
    estado.consultas.length = 0
    await page.goto('/notas')
    await expect(page.getByRole('heading', { name: 'Acceso restringido' })).toBeVisible()
    await expect(page.getByRole('link', { name: 'Notas', exact: true })).toHaveCount(0)
    expect(estado.consultas).toEqual([])
})
