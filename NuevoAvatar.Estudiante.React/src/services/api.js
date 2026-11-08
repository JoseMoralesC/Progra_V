const API_BASES = {
    auth: import.meta.env.VITE_AUTH_API_BASE_URL || '/api/auth',
    users: import.meta.env.VITE_USERS_API_BASE_URL || '/api/users',
    roles: import.meta.env.VITE_ROLES_API_BASE_URL || '/api/roles'
}

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
            headers: { Accept: 'application/json', ...headers }
        })
    } catch {
        throw new ApiError('No fue posible conectar con el servicio.')
    }

    if (!response.ok) {
        let message = `La solicitud falló (${response.status}).`
        try {
            const detail = await response.text()
            if (detail) message = detail
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
    return request(API_BASES.auth, '/login', {
        method: 'POST',
        headers: { usuario, contrasena }
    })
}

export function renovarSesion(refreshToken) {
    return request(API_BASES.auth, '/refresh', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refresh_token: refreshToken })
    })
}

export function obtenerUsuario(email, accessToken) {
    return request(API_BASES.users, `/usuario/${encodeURIComponent(email)}`, {
        headers: { Authorization: `Bearer ${accessToken}` }
    })
}

export function obtenerRol(idRol, accessToken) {
    return request(API_BASES.roles, `/rol/${encodeURIComponent(idRol)}`, {
        headers: { Authorization: `Bearer ${accessToken}` }
    })
}

// Adapta el cliente esperado por las páginas administrativas al flujo de
// renovación y autorización ya administrado por AuthContext.
export function crearClienteAutenticado(authorizedFetch) {
    return {
        request(path, options = {}) {
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
