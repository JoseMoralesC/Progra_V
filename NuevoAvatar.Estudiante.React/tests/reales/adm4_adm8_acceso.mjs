import { chromium } from '@playwright/test'
import { spawnSync } from 'node:child_process'
import { randomUUID } from 'node:crypto'
import { fileURLToPath } from 'node:url'

if (!process.env.TEST_USER || !process.env.TEST_PASSWORD) {
    throw new Error('Faltan TEST_USER y TEST_PASSWORD en el proceso de prueba.')
}

const origen = 'http://127.0.0.1:5080'
const correo = 'fabian.acceso.auditoria@cuc.cr'
const rutaUsuario = `/gateway/admin/usuario/${encodeURIComponent(correo)}`
const claveEstudiante = `${randomUUID()}Aa1!`
let tokenAdmin
let creado = false
let errorPrincipal
let errorLimpieza

async function solicitar(ruta, { metodo = 'GET', token = tokenAdmin, datos } = {}) {
    const respuesta = await fetch(origen + ruta, {
        method: metodo,
        headers: {
            Authorization: `Bearer ${token}`,
            ...(datos ? { 'Content-Type': 'application/json' } : {})
        },
        ...(datos ? { body: JSON.stringify(datos) } : {})
    })
    return respuesta
}

try {
    const ingreso = await fetch(`${origen}/gateway/auth/login`, {
        method: 'POST',
        headers: { usuario: process.env.TEST_USER, contrasena: process.env.TEST_PASSWORD }
    })
    if (![200, 201].includes(ingreso.status)) throw new Error(`Inicio administrativo HTTP ${ingreso.status}`)
    tokenAdmin = (await ingreso.json()).access_token
    if (!tokenAdmin) throw new Error('No se recibió token administrativo.')

    const inicial = await solicitar(rutaUsuario)
    if (inicial.status !== 404) throw new Error(`Cuenta temporal existente o inaccesible: HTTP ${inicial.status}`)

    const alta = await solicitar('/gateway/admin/usuario', {
        metodo: 'POST',
        datos: {
            email: correo,
            tipoIdentificacion: 'Cedula',
            identificacion: 'FABIAN-ACCESO-2026',
            nombre: 'Fabian Acceso Auditoria',
            idRol: 'ESTUDIANTE',
            contrasena: claveEstudiante
        }
    })
    if (alta.status !== 201) throw new Error(`Alta temporal HTTP ${alta.status}`)
    creado = true

    const ingresoEstudiante = await fetch(`${origen}/gateway/auth/login`, {
        method: 'POST',
        headers: { usuario: correo, contrasena: claveEstudiante }
    })
    if (![200, 201].includes(ingresoEstudiante.status)) {
        throw new Error(`Inicio estudiantil HTTP ${ingresoEstudiante.status}`)
    }
    const tokenEstudiante = (await ingresoEstudiante.json()).access_token
    if (!tokenEstudiante) throw new Error('No se recibió token estudiantil.')

    for (const [historia, ruta] of [
        ['ADM4', '/gateway/admin/usuario'],
        ['ADM8', '/gateway/admin/institucion']
    ]) {
        const respuesta = await solicitar(ruta, { token: tokenEstudiante })
        if (respuesta.status !== 403) throw new Error(`${historia}: acceso API HTTP ${respuesta.status}`)
        console.log(`${historia}: API administrativa rechazó la cuenta estudiantil con HTTP 403`)
    }

    const navegador = await chromium.launch({
        headless: true,
        executablePath: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe'
    })
    try {
        const pagina = await navegador.newPage()
        await pagina.goto('http://127.0.0.1:4175/login')
        await pagina.getByLabel('Correo institucional').fill(correo)
        await pagina.getByLabel('Contraseña').fill(claveEstudiante)
        await pagina.getByRole('button', { name: 'Ingresar' }).click()
        await pagina.waitForURL('**/estudiante', { timeout: 15000 })
        for (const [historia, ruta] of [['ADM4', '/usuarios'], ['ADM8', '/instituciones']]) {
            await pagina.goto(`http://127.0.0.1:4175${ruta}`)
            await pagina.waitForURL('**/estudiante', { timeout: 15000 })
            console.log(`${historia}: URL administrativa redirigida al portal estudiantil`)
        }
    } finally {
        await navegador.close()
    }
} catch (error) {
    errorPrincipal = error
}
if (creado && tokenAdmin) {
    try {
        const limpieza = spawnSync('pwsh', [
            '-NoProfile', '-File', fileURLToPath(new URL(
                '../../../docs/segundo_avance/pruebas/fabian/limpieza_cuenta_acceso.ps1',
                import.meta.url
            ))
        ], { encoding: 'utf8' })
        if (limpieza.status !== 0) {
            throw new Error(`No se pudieron limpiar las referencias de sesión: ${limpieza.stderr.trim()}`)
        }
        const baja = await solicitar(rutaUsuario, { metodo: 'DELETE' })
        console.log(`Limpieza de cuenta temporal: HTTP ${baja.status}`)
        if (baja.status !== 204) throw new Error('La cuenta temporal requiere limpieza manual.')
    } catch (error) {
        errorLimpieza = error
    }
}

if (errorPrincipal && errorLimpieza) {
    throw new AggregateError([errorPrincipal, errorLimpieza], 'Falló la prueba y la limpieza de la cuenta temporal.')
}
if (errorPrincipal) throw errorPrincipal
if (errorLimpieza) throw errorLimpieza
