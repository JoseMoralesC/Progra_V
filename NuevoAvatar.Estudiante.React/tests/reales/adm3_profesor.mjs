import { chromium } from '@playwright/test'
import { spawnSync } from 'node:child_process'
import { randomUUID } from 'node:crypto'
import { mkdir } from 'node:fs/promises'
import { fileURLToPath } from 'node:url'

if (!process.env.TEST_USER || !process.env.TEST_PASSWORD) {
    throw new Error('Faltan TEST_USER y TEST_PASSWORD en el proceso de prueba.')
}

const origen = 'http://127.0.0.1:5080'
const correo = 'fabian.profesor.auditoria@cuc.ac.cr'
const identificacion = 'FABIAN-PROFESOR-2026'
const clave = `${randomUUID()}Aa1!`
const asignaciones = new Map()
const creado = { rol: false, profesor: null, usuario: false, grupo: null }
let tokenAdmin

async function solicitar(ruta, metodo = 'GET', datos, token = tokenAdmin, base = origen) {
    return fetch(base + ruta, {
        method: metodo,
        headers: {
            Authorization: `Bearer ${token}`,
            ...(datos ? { 'Content-Type': 'application/json' } : {})
        },
        ...(datos ? { body: JSON.stringify(datos) } : {}),
        signal: AbortSignal.timeout(15000)
    })
}

async function exigir(respuesta, esperado, paso) {
    if (respuesta.status !== esperado) {
        throw new Error(`${paso}: HTTP ${respuesta.status}; ${await respuesta.text()}`)
    }
    return respuesta
}

async function lista(ruta) {
    const respuesta = await exigir(await solicitar(ruta), 200, ruta)
    const datos = await respuesta.json()
    if (!Array.isArray(datos)) throw new Error(`${ruta}: lista inválida.`)
    return datos
}

try {
    const ingreso = await fetch(`${origen}/gateway/auth/login`, {
        method: 'POST',
        headers: { usuario: process.env.TEST_USER, contrasena: process.env.TEST_PASSWORD }
    })
    if (![200, 201].includes(ingreso.status)) throw new Error(`Ingreso administrativo HTTP ${ingreso.status}`)
    tokenAdmin = (await ingreso.json()).access_token
    if (!tokenAdmin) throw new Error('Falta el token administrativo.')

    await exigir(await solicitar('/gateway/admin/rol/PROFESOR'), 404, 'Rol temporal previo')
    await exigir(await solicitar(`/gateway/admin/usuario/${encodeURIComponent(correo)}`), 404, 'Usuario temporal previo')
    const profesoresPrevios = await lista('/gateway/admin/profesor')
    if (profesoresPrevios.some(item => item.email === correo || item.identificacion === identificacion)) {
        throw new Error('Ya existe el profesor de prueba.')
    }
    const periodos = await lista('/gateway/admin/periodo?estado=ACTIVO')
    const cursos = await lista('/gateway/admin/curso')
    if (!periodos.length || !cursos.length) throw new Error('Se necesita un período activo y un curso existente.')

    await exigir(await solicitar('/gateway/admin/rol', 'POST', {
        idRol: 'PROFESOR', nombreRol: 'Profesor'
    }), 201, 'Alta de rol profesor')
    creado.rol = true

    for (const modulo of ['ADM3', 'ADM17']) {
        const previos = await lista(`/gateway/admin/modulo/${modulo}/roles`)
        asignaciones.set(modulo, previos)
        if (!previos.includes('PROFESOR')) {
            await exigir(await solicitar(`/gateway/admin/modulo/${modulo}/roles`, 'PUT',
                [...previos, 'PROFESOR']), 204, `Permiso ${modulo}`)
        }
    }

    const altaProfesor = await exigir(await solicitar('/gateway/admin/profesor', 'POST', {
        tipoIdentificacion: 'Cedula', identificacion, email: correo,
        nombreCompleto: 'Fabian Profesor Auditoria', fechaNacimiento: '1990-01-01',
        telefonos: ['88887777']
    }), 201, 'Alta de profesor')
    creado.profesor = (await altaProfesor.json()).id
    if (!creado.profesor) throw new Error('No se recibió el ID del profesor temporal.')

    await exigir(await solicitar('/gateway/admin/usuario', 'POST', {
        email: correo, tipoIdentificacion: 'Cedula', identificacion,
        nombre: 'Fabian Profesor Auditoria', idRol: 'PROFESOR', contrasena: clave
    }), 201, 'Alta de cuenta docente')
    creado.usuario = true

    const altaGrupo = await exigir(await solicitar('/grupo', 'POST', {
        numeroGrupo: 900000 + Math.floor(Math.random() * 99999),
        cursoId: cursos[0].cursoId, profesorId: creado.profesor,
        horario: 'Lunes 08:00-10:00', cupo: 20, periodoId: periodos[0].periodoId
    }, tokenAdmin, 'http://127.0.0.1:5256'), 201, 'Alta de grupo temporal')
    creado.grupo = (await altaGrupo.json()).grupoId
    if (!creado.grupo) throw new Error('No se recibió el ID del grupo temporal.')

    const ingresoProfesor = await fetch(`${origen}/gateway/auth/login`, {
        method: 'POST', headers: { usuario: correo, contrasena: clave }
    })
    if (![200, 201].includes(ingresoProfesor.status)) {
        throw new Error(`Ingreso docente HTTP ${ingresoProfesor.status}`)
    }
    const tokenProfesor = (await ingresoProfesor.json()).access_token
    const grupos = await exigir(await solicitar('/gateway/mat/profesor/grupos', 'GET',
        null, tokenProfesor), 200, 'Consulta de grupos docentes')
    const asignados = await grupos.json()
    if (!asignados.some(grupo => grupo.grupoId === creado.grupo)) {
        throw new Error('El grupo temporal no aparece en el perfil docente.')
    }
    console.log('ADM3: sesión docente y grupo activo comprobados con datos reales.')
    const bitacoras = await exigir(await solicitar('/gateway/admin/bitacora?vista=dashboard',
        'GET', null, tokenProfesor), 200, 'Bitácoras del perfil docente')
    const eventos = await bitacoras.json()
    if (!Array.isArray(eventos) || eventos.length > 10 ||
        eventos.some(evento => evento.usuario.toLowerCase() !== correo)) {
        throw new Error('El resumen docente contiene eventos ajenos o supera diez registros.')
    }
    console.log(`ADM3: resumen docente acotado a ${eventos.length} eventos propios.`)

    const navegador = await chromium.launch({
        headless: true,
        executablePath: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe'
    })
    try {
        const pagina = await navegador.newPage({ viewport: { width: 1440, height: 900 } })
        await pagina.goto('http://127.0.0.1:4175/login')
        await pagina.getByLabel('Correo institucional').fill(correo)
        await pagina.getByLabel('Contraseña').fill(clave)
        await pagina.getByRole('button', { name: 'Ingresar' }).click()
        await pagina.waitForURL('**/inicio', { timeout: 15000 })
        const panel = pagina.getByRole('heading', { name: 'Mis grupos y cursos' }).locator('..')
        await panel.getByText(new RegExp(`Grupo \\d+: ${cursos[0].nombre}`)).waitFor({ timeout: 20000 })
        const carpeta = fileURLToPath(new URL(
            '../../../docs/segundo_avance/pruebas/fabian/evidencias/', import.meta.url
        ))
        await mkdir(carpeta, { recursive: true })
        await panel.screenshot({ path: `${carpeta}/adm3_profesor.png` })
    } finally {
        await navegador.close()
    }
} finally {
    const fallos = []
    if (creado.grupo) {
        const respuesta = await solicitar(`/grupo/${creado.grupo}`, 'DELETE', null,
            tokenAdmin, 'http://127.0.0.1:5256').catch(() => null)
        if (respuesta?.status !== 204) fallos.push('grupo temporal')
    }
    if (creado.usuario) {
        const limpieza = spawnSync('pwsh', ['-NoProfile', '-File', fileURLToPath(new URL(
            '../../../docs/segundo_avance/pruebas/fabian/limpieza_sesion_profesor.ps1', import.meta.url
        )), '-Correo', correo], { encoding: 'utf8' })
        if (limpieza.status !== 0) fallos.push('tokens de sesión')
        else {
            const respuesta = await solicitar(`/gateway/admin/usuario/${encodeURIComponent(correo)}`, 'DELETE')
                .catch(() => null)
            if (respuesta?.status !== 204) fallos.push('cuenta docente temporal')
        }
    }
    if (creado.profesor) {
        const respuesta = await solicitar(`/gateway/admin/profesor/${creado.profesor}`, 'DELETE').catch(() => null)
        if (respuesta?.status !== 204) fallos.push('profesor temporal')
    }
    for (const [modulo, previos] of asignaciones) {
        const respuesta = await solicitar(`/gateway/admin/modulo/${modulo}/roles`, 'PUT', previos).catch(() => null)
        if (respuesta?.status !== 204) fallos.push(`roles de ${modulo}`)
    }
    if (creado.rol) {
        const respuesta = await solicitar('/gateway/admin/rol/PROFESOR', 'DELETE').catch(() => null)
        if (respuesta?.status !== 200) fallos.push('rol profesor temporal')
    }
    if (fallos.length) throw new Error(`Limpieza pendiente: ${fallos.join(', ')}`)
    console.log('Datos temporales del perfil docente eliminados.')
}
