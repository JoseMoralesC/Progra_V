import { chromium } from '@playwright/test'
import { mkdir } from 'node:fs/promises'
import { fileURLToPath } from 'node:url'

if (!process.env.TEST_USER || !process.env.TEST_PASSWORD) {
    throw new Error('Faltan TEST_USER y TEST_PASSWORD en el proceso de prueba.')
}

const origen = 'http://127.0.0.1:5080'
const rutaRoles = `${origen}/gateway/admin/modulo/ADM20/roles`
let token
let rolesIniciales
let asignacionNueva = false
let aprobada = false

async function solicitar(ruta, metodo = 'GET', datos) {
    return fetch(origen + ruta, {
        method: metodo,
        headers: {
            Authorization: `Bearer ${token}`,
            ...(datos ? { 'Content-Type': 'application/json' } : {})
        },
        ...(datos ? { body: JSON.stringify(datos) } : {}),
        signal: AbortSignal.timeout(15000)
    })
}

async function lista(ruta) {
    const respuesta = await solicitar(ruta)
    if (respuesta.status !== 200) throw new Error(`${ruta}: HTTP ${respuesta.status}`)
    const datos = await respuesta.json()
    if (!Array.isArray(datos)) throw new Error(`${ruta}: respuesta no es una lista.`)
    return datos
}

try {
    const ingreso = await fetch(`${origen}/gateway/auth/login`, {
        method: 'POST',
        headers: { usuario: process.env.TEST_USER, contrasena: process.env.TEST_PASSWORD }
    })
    if (![200, 201].includes(ingreso.status)) throw new Error(`Inicio administrativo HTTP ${ingreso.status}`)
    token = (await ingreso.json()).access_token
    if (!token) throw new Error('No se recibió token de acceso.')

    const asignaciones = await solicitar('/gateway/admin/modulo/ADM20/roles')
    if (asignaciones.status !== 200) throw new Error(`Asignaciones ADM20 HTTP ${asignaciones.status}`)
    rolesIniciales = await asignaciones.json()
    if (!Array.isArray(rolesIniciales)) throw new Error('Asignaciones ADM20 inválidas.')
    if (!rolesIniciales.includes('ADMIN')) {
        const cambio = await solicitar('/gateway/admin/modulo/ADM20/roles', 'PUT', [...rolesIniciales, 'ADMIN'])
        if (cambio.status !== 204) throw new Error(`Asignación ADM20 HTTP ${cambio.status}`)
        asignacionNueva = true
        console.log('ADM20 asignado al rol administrador; asignaciones anteriores conservadas')
    }

    const menu = await lista('/gateway/menu')
    if (!menu.some(modulo => modulo.idModulo === 'ADM20')) {
        throw new Error('ADM20 no aparece en el menú autorizado después de asignarlo.')
    }

    const [usuarios, carreras, cursos, activos, periodos] = await Promise.all([
        lista('/gateway/admin/usuario'), lista('/gateway/admin/carrera'),
        lista('/gateway/admin/curso'), lista('/gateway/admin/periodo?estado=ACTIVO'),
        lista('/gateway/admin/periodo')
    ])
    const facturas = (await Promise.all(periodos.map(periodo =>
        lista(`/gateway/fin/factura?periodoId=${periodo.periodoId}`)
    ))).flat()
    const esperado = new Map([
        ['Usuarios', usuarios.length], ['Carreras', carreras.length],
        ['Cursos', cursos.length], ['Períodos activos', activos.length],
        ['Facturas pendientes', facturas.filter(factura => factura.estado === 'PENDIENTE').length]
    ])

    const navegador = await chromium.launch({
        headless: true,
        executablePath: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe'
    })
    try {
        const pagina = await navegador.newPage({ viewport: { width: 1440, height: 900 } })
        await pagina.goto('http://127.0.0.1:4175/login')
        await pagina.getByLabel('Correo institucional').fill(process.env.TEST_USER)
        await pagina.getByLabel('Contraseña').fill(process.env.TEST_PASSWORD)
        await pagina.getByRole('button', { name: 'Ingresar' }).click()
        await pagina.waitForURL('**/inicio', { timeout: 15000 })
        const indicadores = pagina.getByRole('region', { name: 'Indicadores administrativos' })
        for (const [nombre, cantidad] of esperado) {
            const tarjeta = indicadores.getByRole('heading', { name: nombre }).locator('..')
            await tarjeta.getByText(String(cantidad), { exact: true }).waitFor({ timeout: 20000 })
            console.log(`ADM3: ${nombre} = ${cantidad}, igual a la consulta real`)
        }
        const evidencia = fileURLToPath(new URL(
            '../../../docs/segundo_avance/pruebas/fabian/evidencias/adm3_indicadores.png',
            import.meta.url
        ))
        await mkdir(fileURLToPath(new URL(
            '../../../docs/segundo_avance/pruebas/fabian/evidencias/', import.meta.url
        )), { recursive: true })
        await indicadores.screenshot({ path: evidencia })
        const actividad = pagina.getByRole('region', { name: 'Bitácoras recientes' })
        await actividad.getByRole('list').waitFor({ timeout: 20000 })
        const eventos = await actividad.locator('li').count()
        if (eventos < 1 || eventos > 10) throw new Error(`ADM3: bitácoras visibles = ${eventos}`)
        await actividad.screenshot({ path: fileURLToPath(new URL(
            '../../../docs/segundo_avance/pruebas/fabian/evidencias/adm3_bitacoras_admin.png',
            import.meta.url
        )) })
        console.log(`ADM3: ${eventos} bitácoras recientes visibles para el administrador`)
        const notificaciones = pagina.getByRole('region', { name: 'Notificaciones recientes' })
        await notificaciones.getByText('No hay notificaciones recientes.').waitFor({ timeout: 20000 })
        await notificaciones.screenshot({ path: fileURLToPath(new URL(
            '../../../docs/segundo_avance/pruebas/fabian/evidencias/adm3_notificaciones_admin.png',
            import.meta.url
        )) })
        console.log('ADM3: resumen administrativo de notificaciones disponible y vacío')
    } finally {
        await navegador.close()
    }
    aprobada = true
} finally {
    if (asignacionNueva && !aprobada && token && rolesIniciales) {
        const restauracion = await solicitar('/gateway/admin/modulo/ADM20/roles', 'PUT', rolesIniciales)
        if (restauracion.status !== 204) {
            throw new Error('No se pudo restaurar ADM20 después del fallo de prueba.')
        }
    }
}
