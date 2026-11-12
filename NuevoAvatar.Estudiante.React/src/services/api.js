export class ApiError extends Error {
    constructor(message, status = 0) {
        super(message)
        this.name = 'ApiError'
        this.status = status
    }
}

async function request(base, path, { headers = {}, ...options } = {}) {
    let response
    try {
        response = await fetch(`${base.replace(/\/+$/, '')}${path}`, {
            ...options,
            headers: { Accept: 'application/json', ...headers },
            cache: 'no-store',
            credentials: 'omit'
        })
    } catch {
        throw new ApiError('No fue posible conectar con el servicio.')
    }

    if (!response.ok) {
        let message = response.status === 401 ? 'La sesión venció. Inicia sesión nuevamente.'
            : response.status === 403 ? 'No tienes permiso para esta operación.'
                : response.status >= 500 ? 'El servicio no está disponible.' : `La solicitud falló (${response.status}).`
        try {
            const detail = await response.text()
            if (detail && ![401, 403].includes(response.status) && response.status < 500) {
                try {
                    const body = JSON.parse(detail)
                    message = body.detail || body.message || body.error || detail
                } catch { message = detail }
            }
        } catch {
            // Conserva el mensaje HTTP si el cuerpo no se puede leer.
        }
        throw new ApiError(message, response.status)
    }

    if (response.status === 204) return null
    try {
        return await response.json()
    } catch {
        throw new ApiError('El servicio devolvió una respuesta inválida.', response.status)
    }
}

export function iniciarSesion(usuario, contrasena) {
    return request('/gateway/auth', '/login', {
        method: 'POST',
        headers: { usuario, contrasena }
    })
}

export function renovarSesion(refreshToken) {
    return request('/gateway/auth', '/refresh', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refresh_token: refreshToken })
    })
}

export function cerrarSesion(accessToken) {
    return request('/gateway/auth', '/logout', {
        method: 'POST',
        headers: { Authorization: `Bearer ${accessToken}` }
    })
}

export function obtenerSesion(accessToken) {
    return request('/gateway/auth', '/sesion', {
        headers: { Authorization: `Bearer ${accessToken}` }
    })
}

// Adapta el cliente esperado por las páginas administrativas al flujo de
// renovación y autorización ya administrado por AuthContext.
export function crearClienteAutenticado(authorizedFetch) {
    return {
        request(path, options = {}) {
            if (!path.startsWith('/gateway/')) throw new ApiError('Ruta de servicio no permitida.')
            return authorizedFetch(accessToken => request('', path, {
                ...options,
                headers: { ...options.headers, Authorization: `Bearer ${accessToken}` }
            }))
        }
    }
}

export function obtenerPerfil(accessToken) {
    return request('/gateway', '/perfil', {
        headers: { Authorization: `Bearer ${accessToken}` },
        cache: 'no-store'
    })
}

export function obtenerMenu(accessToken) {
    return request('/gateway', '/menu', {
        headers: { Authorization: `Bearer ${accessToken}` },
        cache: 'no-store'
    })
}

export function actualizarPerfil(nombre, accessToken) {
    return request('/gateway', '/perfil', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${accessToken}` },
        body: JSON.stringify({ nombre })
    })
}

export function cambiarContrasenaPropia(contrasenaActual, contrasenaNueva, accessToken) {
    return request('/gateway', '/perfil/contrasena', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${accessToken}` },
        body: JSON.stringify({ contrasenaActual, contrasenaNueva })
    })
}

export function obtenerPoliticaContrasena(accessToken) {
    return request('/gateway', '/perfil/politica-contrasena', {
        headers: { Authorization: `Bearer ${accessToken}` },
        cache: 'no-store'
    })
}

export function registrarEventoInterfaz(tipo, ruta, accessToken) {
    return request('/gateway', '/auditoria/interfaz', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${accessToken}` },
        body: JSON.stringify({ tipo, ruta })
    })
}

export function obtenerExpedientePropio(accessToken) {
    return request('/gateway', '/expediente/propio', {
        headers: { Authorization: `Bearer ${accessToken}` },
        cache: 'no-store'
    })
}

export function actualizarExpedientePropio(cambios, accessToken) {
    const body = {
        telefonos: cambios.telefonos,
        provinciaId: cambios.provinciaId,
        cantonId: cambios.cantonId,
        distritoId: cambios.distritoId,
        otrasSenas: cambios.otrasSenas
    }
    return request('/gateway', '/expediente/propio', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${accessToken}` },
        body: JSON.stringify(body)
    })
}

export function obtenerProvincias(accessToken) {
    return request('/gateway/mat', '/provincias', {
        headers: { Authorization: `Bearer ${accessToken}` }, cache: 'no-store'
    })
}

export function obtenerCantones(provinciaId, accessToken) {
    return request('/gateway/mat', `/cantones/${encodeURIComponent(provinciaId)}`, {
        headers: { Authorization: `Bearer ${accessToken}` }, cache: 'no-store'
    })
}

export function obtenerDistritos(provinciaId, cantonId, accessToken) {
    return request('/gateway/mat', `/distritos/${encodeURIComponent(provinciaId)}/${encodeURIComponent(cantonId)}`, {
        headers: { Authorization: `Bearer ${accessToken}` }, cache: 'no-store'
    })
}

const PREMATRICULA_PROPIA = '/prematricula/propio'

export function obtenerPrematriculasPropias(accessToken) {
    return request('/gateway/mat', PREMATRICULA_PROPIA, {
        headers: { Authorization: `Bearer ${accessToken}` }, cache: 'no-store'
    })
}

export function obtenerOpcionesPrematricula(accessToken) {
    return request('/gateway/mat', `${PREMATRICULA_PROPIA}/opciones`, {
        headers: { Authorization: `Bearer ${accessToken}` }, cache: 'no-store'
    })
}

function cuerpoPrematriculaPropia(datos) {
    return {
        carreraId: datos.carreraId,
        cursos: datos.cursos,
        observaciones: datos.observaciones,
        periodoId: datos.periodoId
    }
}

export function crearPrematriculaPropia(datos, accessToken) {
    return request('/gateway/mat', PREMATRICULA_PROPIA, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${accessToken}` },
        body: JSON.stringify(cuerpoPrematriculaPropia(datos))
    })
}

export function actualizarPrematriculaPropia(id, datos, accessToken) {
    return request('/gateway/mat', `${PREMATRICULA_PROPIA}/${encodeURIComponent(id)}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${accessToken}` },
        body: JSON.stringify(cuerpoPrematriculaPropia(datos))
    })
}

export function cancelarPrematriculaPropia(id, accessToken) {
    return request('/gateway/mat', `${PREMATRICULA_PROPIA}/${encodeURIComponent(id)}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${accessToken}` }
    })
}
