import { chromium } from '@playwright/test'

if (!process.env.TEST_USER || !process.env.TEST_PASSWORD) {
    throw new Error('Faltan TEST_USER y TEST_PASSWORD en el proceso de prueba.')
}

const origen = 'http://127.0.0.1:5080'
const rutaRoles = `${origen}/gateway/admin/modulo/ADM8/roles`
const rutaInstituciones = `${origen}/gateway/admin/institucion`
const rutaInexistente = `${rutaInstituciones}/2147483647`
const navegador = await chromium.launch({
    headless: true,
    executablePath: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe'
})

let token
let originales
let errorPrincipal
let errorLimpieza

async function consultar(ruta, metodo = 'GET', datos) {
    return fetch(ruta, {
        method: metodo,
        headers: {
            Authorization: `Bearer ${token}`,
            ...(datos ? { 'Content-Type': 'application/json' } : {})
        },
        ...(datos ? { body: JSON.stringify(datos) } : {}),
        signal: AbortSignal.timeout(15000)
    })
}

try {
    const pagina = await navegador.newPage()
    await pagina.goto('http://127.0.0.1:4175/login')
    await pagina.getByLabel('Correo institucional').fill(process.env.TEST_USER)
    await pagina.getByLabel('Contraseña').fill(process.env.TEST_PASSWORD)
    await pagina.getByRole('button', { name: 'Ingresar' }).click()
    await pagina.waitForURL('**/inicio', { timeout: 15000 })
    await pagina.goto('http://127.0.0.1:4175/roles')
    await pagina.getByRole('button', { name: 'Permisos de menú' }).click()
    await pagina.getByRole('dialog').waitFor({ timeout: 15000 })

    token = await pagina.evaluate(() => JSON.parse(
        sessionStorage.getItem('nuevo-avatar.sesion') || '{}'
    ).access_token)
    if (!token) throw new Error('La sesión de prueba no contiene un token activo.')
    const respuestaInicial = await consultar(rutaRoles)
    if (respuestaInicial.status !== 200) throw new Error(`Roles iniciales HTTP ${respuestaInicial.status}`)
    originales = await respuestaInicial.json()
    if (!originales.includes('ADMIN')) throw new Error('ADM8 no está asignado al rol administrador.')
    const inexistente = await consultar(rutaInexistente)
    if (inexistente.status !== 404) throw new Error('El ID reservado para la prueba ya existe.')

    const casilla = pagina.getByRole('checkbox', { name: 'ADM8 Administrador' })
    await casilla.uncheck()
    await pagina.getByRole('button', { name: 'Guardar ADM8' }).click()
    await pagina.getByRole('link', { name: 'Instituciones', exact: true })
        .waitFor({ state: 'hidden', timeout: 15000 })
    const bloqueada = await consultar(rutaInexistente, 'DELETE')
    if (bloqueada.status !== 403) throw new Error(`ADM8 tras revocación HTTP ${bloqueada.status}`)
    console.log('ADM5: revocación real ocultó Instituciones del menú y bloqueó su baja con HTTP 403')

    await casilla.check()
    await pagina.getByRole('button', { name: 'Guardar ADM8' }).click()
    await pagina.getByRole('link', { name: 'Instituciones', exact: true })
        .waitFor({ state: 'visible', timeout: 15000 })
    const restaurada = await consultar(rutaInstituciones)
    if (restaurada.status !== 200) throw new Error(`ADM8 después de restaurar HTTP ${restaurada.status}`)
    console.log('ADM5: restauración mostró Instituciones sin recargar la página y devolvió HTTP 200')
} catch (error) {
    errorPrincipal = error
}
try {
    if (token && originales) {
        const respuesta = await consultar(rutaRoles)
        if (respuesta.status !== 200) throw new Error('No se pudo comprobar la asignación final de ADM8.')
        const actuales = await respuesta.json()
        if (JSON.stringify([...actuales].sort()) !== JSON.stringify([...originales].sort())) {
            const restauracion = await consultar(rutaRoles, 'PUT', originales)
            if (restauracion.status !== 204) throw new Error('No se pudo restaurar la asignación original de ADM8.')
            console.log('Asignación original de ADM8 restaurada tras la prueba')
        }
    }
} catch (error) {
    errorLimpieza = error
}
try {
    await navegador.close()
} catch (error) {
    errorLimpieza = errorLimpieza
        ? new AggregateError([errorLimpieza, error], 'Falló la restauración y el cierre del navegador.')
        : error
}

if (errorPrincipal && errorLimpieza) {
    throw new AggregateError([errorPrincipal, errorLimpieza], 'Falló la prueba y la restauración de permisos.')
}
if (errorPrincipal) throw errorPrincipal
if (errorLimpieza) throw errorLimpieza
