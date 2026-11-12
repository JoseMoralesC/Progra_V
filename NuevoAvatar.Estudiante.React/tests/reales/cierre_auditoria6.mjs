import { chromium, expect } from '@playwright/test'
import { createServer } from 'vite'
import { fileURLToPath } from 'node:url'
import { writeFile } from 'node:fs/promises'

const root = fileURLToPath(new URL('../../', import.meta.url))
const salida = fileURLToPath(new URL('../../../docs/segundo_avance/pruebas/fabian/resultado_cierre6_real.json', import.meta.url))
const checks = []
const tokens = []
let browser, server, originales, rolesModificados = false, tokenAdmin, paginaAdmin
const origin = 'http://127.0.0.1:5080'
async function api(path, token, method = 'GET', data) {
    return fetch(origin + path, { method, headers: {
        ...(token ? { Authorization: 'Bearer ' + token } : {}),
        ...(data ? { 'Content-Type': 'application/json' } : {})
    }, ...(data ? { body: JSON.stringify(data) } : {}), signal: AbortSignal.timeout(15000) })
}
function registrar(nombre, ok, detalle) {
    checks.push({ prueba: nombre, resultado: ok ? 'aprobada' : 'fallida', detalle })
    if (!ok) throw new Error(nombre)
}
async function omitir(page) {
    const modal = page.getByRole('dialog', { name: 'Bienvenido a Nuevo Avatar' })
    if (await modal.waitFor({ state: 'visible', timeout: 4000 }).then(() => true).catch(() => false))
        await modal.getByRole('button', { name: /Omitir/ }).click()
}
try {
    server = await createServer({ root, server: { host: '127.0.0.1', port: 4175, strictPort: true }, clearScreen: false })
    await server.listen()
    browser = await chromium.launch({ headless: true })
    for (const [perfil, user, pass] of [
        ['ADMINISTRADOR', process.env.TEST_USER, process.env.TEST_PASSWORD],
        ['ESTUDIANTE', process.env.STUDENT_USER, process.env.STUDENT_PASSWORD]
    ]) {
        if (!user || !pass) throw new Error('Faltan credenciales en el proceso')
        const context = await browser.newContext()
        const page = await context.newPage()
        await page.goto('http://127.0.0.1:4175/login')
        await page.getByLabel('Correo institucional').fill(user)
        await page.getByLabel('Contraseña', { exact: true }).fill(pass)
        await page.getByRole('button', { name: 'Ingresar', exact: true }).click()
        await page.waitForURL(url => url.pathname === (perfil === 'ESTUDIANTE' ? '/estudiante' : '/inicio'), { timeout: 20000 })
        const session = await page.evaluate(() => JSON.parse(sessionStorage.getItem('nuevo-avatar.sesion') || '{}'))
        registrar('Ingreso desde navegador ' + perfil, !!session.access_token, { ruta: new URL(page.url()).pathname })
        tokens.push(session.access_token)
        const sesion = await api('/gateway/auth/sesion', session.access_token)
        const datos = await sesion.json()
        registrar('Perfil real ' + perfil, sesion.status === 200 && datos.rol === perfil, { status: sesion.status, rol: datos.rol })
        await omitir(page)
        if (perfil === 'ADMINISTRADOR') {
            tokenAdmin = session.access_token
            paginaAdmin = page
            for (const recurso of ['usuario','rol','parametro','institucion']) {
                const r = await api('/gateway/admin/' + recurso, tokenAdmin)
                registrar('Lectura administrativa ' + recurso, r.status === 200, { status: r.status })
            }
            const p = await api('/gateway/admin/parametro/TAMPAG', tokenAdmin)
            const valor = await p.json()
            registrar('TAMPAG publicado por API', p.status === 200 && Number(valor.valor) === 15, { status: p.status, valor: valor.valor })
            const secreto = await api('/gateway/admin/parametro/MAILPASS', tokenAdmin)
            const oculto = await secreto.json()
            registrar('MAILPASS protegido en API', secreto.status === 200 && oculto.valor === '********', { status: secreto.status, enmascarado: oculto.valor === '********' })
            await page.goto('http://127.0.0.1:4175/parametros')
            await omitir(page)
            await expect(page.getByRole('table')).toBeVisible()
            const fila = page.getByRole('row').filter({ has: page.getByRole('cell', { name: 'MAILPASS', exact: true }) })
            for (let i = 0; i < 20 && await fila.count() === 0; i++) {
                const next = page.getByRole('button', { name: 'Siguiente', exact: true })
                if (!await next.isVisible() || !await next.isEnabled()) break
                await next.click()
            }
            await expect(fila).toContainText('••••••••')
            registrar('MAILPASS protegido en tabla', true, { enmascarado: true })
            await page.goto('http://127.0.0.1:4175/usuarios')
            await expect(page.getByRole('button', { name: 'Email (ID)', exact: true })).toBeVisible()
            const header = page.getByRole('columnheader', { name: 'Email (ID)', exact: true })
            await page.getByRole('button', { name: 'Email (ID)', exact: true }).click()
            await expect(header).toHaveAttribute('aria-sort', 'descending')
            await page.getByRole('button', { name: 'Email (ID)', exact: true }).click()
            await expect(header).toHaveAttribute('aria-sort', 'ascending')
            registrar('Orden de usuarios con datos reales', true, { columnas_ordenables: true })
        } else {
            const propio = await api('/gateway/expediente/propio', session.access_token)
            registrar('Expediente propio de la cuenta preparada', propio.status === 200, { status: propio.status })
            for (const recurso of ['usuario','rol','parametro','institucion']) {
                const r = await api('/gateway/admin/' + recurso, session.access_token)
                registrar('Estudiante sin acceso administrativo ' + recurso, r.status === 403, { status: r.status })
            }
            await page.goto('http://127.0.0.1:4175/usuarios')
            await expect(page).toHaveURL(/\/estudiante$/)
            await expect(page.getByRole('button', { name: 'Nuevo usuario', exact: true })).toHaveCount(0)
            registrar('Ruta administrativa restringida para estudiante', true, {})
        }
    }
    if (process.env.PROBAR_REVOCACION === '1') {
        const r = await api('/gateway/admin/modulo/ADM8/roles', tokenAdmin)
        if (r.status !== 200) throw new Error('No se pudo consultar el permiso original')
        originales = await r.json()
        const roles = await (await api('/gateway/admin/rol', tokenAdmin)).json()
        const rol = roles.find(x => ['ADMINISTRADOR','ADMIN'].includes(x.nombreRol.trim().toUpperCase()))
        if (!rol || !originales.includes(rol.idRol)) throw new Error('Rol administrativo no identificado')
        const inexistente = '/gateway/admin/institucion/2147483647'
        const previa = await api(inexistente, tokenAdmin)
        if (previa.status !== 404) throw new Error('La institución reservada para comprobar el permiso debe ser inexistente')
        await paginaAdmin.goto('http://127.0.0.1:4175/roles')
        await omitir(paginaAdmin)
        await paginaAdmin.getByRole('button', { name: 'Permisos de menú', exact: true }).click()
        const modal = paginaAdmin.getByRole('dialog', { name: 'Permisos de menú', exact: true })
        const casilla = modal.getByRole('checkbox', { name: 'ADM8 ' + rol.nombreRol, exact: true })
        await expect(casilla).toBeChecked()
        rolesModificados = true
        await casilla.uncheck()
        await modal.getByRole('button', { name: 'Guardar ADM8', exact: true }).click()
        await expect(paginaAdmin.getByRole('link', { name: 'Instituciones', exact: true })).toHaveCount(0)
        // Carreras conserva su consulta auxiliar. La baja exige exclusivamente ADM8.
        const denegada = await api(inexistente, tokenAdmin, 'DELETE')
        registrar('Revocación real ADM8 y menú sin recargar', denegada.status === 403, { status: denegada.status })
        await casilla.check()
        await modal.getByRole('button', { name: 'Guardar ADM8', exact: true }).click()
        await expect(paginaAdmin.getByRole('link', { name: 'Instituciones', exact: true })).toBeVisible()
        const permitida = await api('/gateway/admin/institucion', tokenAdmin)
        registrar('Restauración real ADM8 y menú sin recargar', permitida.status === 200, { status: permitida.status })
    }
} catch (error) {
    checks.push({ prueba: 'Recorrido completo', resultado: 'fallida', detalle: { error: String(error.message).replace(/Bearer\s+\S+/g, 'Bearer [oculto]') } })
    process.exitCode = 1
} finally {
    if (rolesModificados && originales) {
        try {
            const actual = await (await api('/gateway/admin/modulo/ADM8/roles', tokenAdmin)).json()
            if (JSON.stringify([...actual].sort()) !== JSON.stringify([...originales].sort())) {
                const r = await api('/gateway/admin/modulo/ADM8/roles', tokenAdmin, 'PUT', originales)
                if (r.status !== 204) throw new Error('Restauración rechazada')
            }
            const final = await (await api('/gateway/admin/modulo/ADM8/roles', tokenAdmin)).json()
            registrar('Asignaciones originales conservadas', JSON.stringify([...final].sort()) === JSON.stringify([...originales].sort()), {})
        } catch { checks.push({ prueba: 'Restauración de permisos', resultado: 'fallida' }); process.exitCode = 1 }
    }
    for (const token of tokens) { try { await api('/gateway/auth/logout', token, 'POST') } catch {} }
    await browser?.close()
    await server?.close()
    await writeFile(salida, JSON.stringify({ fecha: new Date().toISOString(), base: '47c27ce', respuestas_controladas: false, comprobaciones: checks }, null, 2) + '\n')
    console.log(JSON.stringify(checks, null, 2))
}

