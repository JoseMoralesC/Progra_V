import {
    createContext,
    useCallback,
    useContext,
    useEffect,
    useMemo,
    useRef,
    useState
} from 'react'
import {
    actualizarPerfil,
    cambiarContrasenaPropia as cambiarContrasenaApi,
    cerrarSesion as cerrarSesionApi,
    iniciarSesion,
    obtenerMenu,
    obtenerPerfil,
    obtenerPoliticaContrasena as obtenerPoliticaContrasenaApi,
    obtenerSesion,
    renovarSesion
} from '../services/api'

const AuthContext = createContext(null)
const REFRESH_EARLY_MS = 30_000
const STORAGE_KEY = 'nuevo-avatar.sesion'
const LOGOUT_EPOCH_KEY = 'nuevo-avatar.auth.logout-epoch'
const PERFILES = ['ADMINISTRADOR', 'PROFESOR', 'ESTUDIANTE']

function logoutEpochActual() {
    try { return localStorage.getItem(LOGOUT_EPOCH_KEY) || '' } catch { return '' }
}

function nuevoLogoutEpoch() {
    return globalThis.crypto?.randomUUID?.() || `${Date.now()}-${Math.random()}`
}

function nuevoIdSesionBienvenida() {
    return globalThis.crypto?.randomUUID?.() || `${Date.now()}-${Math.random()}`
}

function guardarTokens(session) {
    try {
        if (!session) sessionStorage.removeItem(STORAGE_KEY)
        else sessionStorage.setItem(STORAGE_KEY, JSON.stringify({
            access_token: session.accessToken, refresh_token: session.refreshToken,
            usuarioID: session.email, expires_in: new Date(session.expiresAt).toISOString(),
            logoutEpoch: session.logoutEpoch || '',
            welcomeSessionId: session.welcomeSessionId || ''
        }))
    } catch { /* La sesión sigue disponible en memoria si el almacenamiento está bloqueado. */ }
}

async function identidad(accessToken) {
    const user = await obtenerSesion(accessToken)
    if (!user?.nombre?.trim() || !PERFILES.includes(user.rol)) {
        throw Object.assign(new Error('La cuenta no tiene un perfil autorizado.'), { status: 403 })
    }
    const modulos = await obtenerMenu(accessToken)
    if (!Array.isArray(modulos) || modulos.some(m => !m || typeof m.idModulo !== 'string' ||
        typeof m.nombreModulo !== 'string' || typeof m.activo !== 'boolean' || !Number.isInteger(m.orden))) {
        throw Object.assign(new Error('No se pudieron comprobar los permisos.'), { status: 503 })
    }
    return { ...user, email: user.usuarioID, idRol: user.rol, nombreRol: user.rol,
        modulos: modulos.filter(m => m.activo) }
}

class SessionExpiredError extends Error {
    constructor() {
        super('La sesión venció. Inicia sesión nuevamente.')
        this.name = 'SessionExpiredError'
    }
}

class SessionChangedError extends Error {
    constructor() {
        super('La sesión cambió antes de completar la solicitud.')
        this.name = 'SessionChangedError'
    }
}

function expirationTime(value) {
    const parsed = Date.parse(value)
    if (!Number.isFinite(parsed)) throw new Error('La respuesta no contiene una fecha de expiración válida.')
    return parsed
}

export function AuthProvider({ children }) {
    const [session, setSession] = useState(null)
    const [estado, setEstado] = useState('cargando')
    const [mensaje, setMensaje] = useState('')
    const [intento, setIntento] = useState(0)
    const restauracion = useRef(null)
    const sessionRef = useRef(null)
    const sessionVersion = useRef(0)
    const refreshPromise = useRef(null)
    const expiryTimer = useRef(null)
    const pendingLogoutEpoch = useRef(null)

    const updateSession = useCallback(value => {
        sessionRef.current = value
        setSession(value)
        guardarTokens(value)
    }, [])

    const clearSession = useCallback(() => {
        sessionVersion.current += 1
        if (expiryTimer.current) window.clearTimeout(expiryTimer.current)
        expiryTimer.current = null
        refreshPromise.current = null
        pendingLogoutEpoch.current = null
        updateSession(null)
        setEstado('sin-sesion')
    }, [updateSession])

    useEffect(() => {
        function onStorage(event) {
            if (event.key !== LOGOUT_EPOCH_KEY || !event.newValue ||
                event.newValue !== logoutEpochActual()) return

            const currentEpoch = sessionRef.current?.logoutEpoch ?? pendingLogoutEpoch.current
            if (currentEpoch == null || currentEpoch === event.newValue) return

            clearSession()
            setMensaje('La sesión se cerró en otra pestaña. Inicia sesión nuevamente.')
        }

        window.addEventListener('storage', onStorage)
        return () => window.removeEventListener('storage', onStorage)
    }, [clearSession])

    const refresh = useCallback(() => {
        if (refreshPromise.current) return refreshPromise.current
        const current = sessionRef.current
        if (!current?.refreshToken) return Promise.reject(new Error('No hay sesión que renovar.'))
        const version = sessionVersion.current
        const currentRefreshToken = current.refreshToken
        let pending
        pending = renovarSesion(currentRefreshToken)
            .then(tokens => {
                if (!tokens?.access_token || !tokens?.refresh_token) {
                    throw new Error('La respuesta de renovación no contiene los tokens esperados.')
                }
                const expiresAt = expirationTime(tokens.expires_in)
                if (version !== sessionVersion.current || sessionRef.current?.refreshToken !== currentRefreshToken) {
                    throw new SessionChangedError()
                }
                const next = {
                    ...sessionRef.current,
                    accessToken: tokens.access_token,
                    refreshToken: tokens.refresh_token,
                    expiresAt
                }
                updateSession(next)
                return next.accessToken
            })
            .catch(error => {
                if (version !== sessionVersion.current || sessionRef.current?.refreshToken !== currentRefreshToken) {
                    throw new SessionChangedError()
                }
                clearSession()
                const message = error.status === 401
                    ? 'La sesión venció. Inicia sesión nuevamente.'
                    : 'No fue posible renovar la sesión. Inicia sesión nuevamente.'
                setMensaje(message)
                throw new SessionExpiredError()
            })
            .finally(() => {
                if (refreshPromise.current === pending) refreshPromise.current = null
            })
        refreshPromise.current = pending

        return refreshPromise.current
    }, [clearSession, updateSession])

    const authorizedFetch = useCallback(async fetcher => {
        const current = sessionRef.current
        if (!current) throw new Error('La sesión ya no está activa.')
        const version = sessionVersion.current
        let accessToken = current.accessToken
        if (Date.now() >= current.expiresAt - REFRESH_EARLY_MS) accessToken = await refresh()
        try {
            const result = await fetcher(accessToken)
            if (version !== sessionVersion.current) throw new SessionChangedError()
            return result
        } catch (error) {
            if (error.status !== 401) throw error
            accessToken = await refresh()
            try {
                const result = await fetcher(accessToken)
                if (version !== sessionVersion.current) throw new SessionChangedError()
                return result
            } catch (retryError) {
                if (version === sessionVersion.current && retryError.status === 401) {
                    clearSession()
                    setMensaje('La sesión venció. Inicia sesión nuevamente.')
                }
                throw retryError
            }
        }
    }, [refresh, clearSession])

    const login = useCallback(async (usuario, contrasena) => {
        usuario = usuario.trim().toLowerCase()
        if (!/^[^\s@]+@(?:cuc\.cr|cuc\.ac\.cr)$/.test(usuario) || !contrasena.trim()) {
            throw new Error('Usuario y/o contraseña incorrectos')
        }
        clearSession()
        setMensaje('')
        const loginLogoutEpoch = logoutEpochActual()
        pendingLogoutEpoch.current = loginLogoutEpoch
        const version = sessionVersion.current
        let tokens
        try {
            tokens = await iniciarSesion(usuario, contrasena)
        } catch (error) {
            error.phase = 'authentication'
            throw error
        }
        if (version !== sessionVersion.current) throw new SessionChangedError()
        if (!tokens?.access_token || !tokens?.refresh_token || !tokens?.usuarioID) {
            throw new Error('La respuesta de autenticación no contiene los datos esperados.')
        }

        let next = {
            email: tokens.usuarioID,
            accessToken: tokens.access_token,
            refreshToken: tokens.refresh_token,
            expiresAt: expirationTime(tokens.expires_in),
            logoutEpoch: loginLogoutEpoch,
            welcomeSessionId: nuevoIdSesionBienvenida(),
            user: null
        }
        updateSession(next)

        try {
            const user = await authorizedFetch(identidad)
            if (version !== sessionVersion.current) throw new SessionChangedError()
            next = {
                ...sessionRef.current,
                user: { ...user, email: user.email || next.email, usuarioID: user.usuarioID || next.email }
            }
            updateSession(next)
            pendingLogoutEpoch.current = null
            setEstado('autenticado')
            return next
        } catch (error) {
            if (version === sessionVersion.current) clearSession()
            if (error.name !== 'SessionExpiredError' && error.name !== 'SessionChangedError') {
                error.phase = 'profile'
            }
            throw error
        }
    }, [authorizedFetch, clearSession, updateSession])

    useEffect(() => {
        const control = new AbortController()
        if (!restauracion.current) {
            restauracion.current = (async () => {
                let saved
                try { saved = JSON.parse(sessionStorage.getItem(STORAGE_KEY) || 'null') } catch { /* Sin sesión guardada. */ }
                if (!saved) { setEstado('sin-sesion'); return }
                if (!saved.access_token || !saved.refresh_token || !saved.usuarioID || !Number.isFinite(Date.parse(saved.expires_in))) {
                    clearSession(); return
                }
                const version = sessionVersion.current
                const savedLogoutEpoch = saved.logoutEpoch ?? ''
                const welcomeSessionId = typeof saved.welcomeSessionId === 'string' && saved.welcomeSessionId
                    ? saved.welcomeSessionId : nuevoIdSesionBienvenida()
                pendingLogoutEpoch.current = savedLogoutEpoch
                updateSession({ email: saved.usuarioID, accessToken: saved.access_token,
                    refreshToken: saved.refresh_token, expiresAt: Date.parse(saved.expires_in),
                    logoutEpoch: savedLogoutEpoch, welcomeSessionId, user: null })
                try {
                    const user = await authorizedFetch(identidad)
                    if (version !== sessionVersion.current) return
                    updateSession({ ...sessionRef.current, user: { ...user, email: user.email || saved.usuarioID } })
                    pendingLogoutEpoch.current = null
                    setEstado('autenticado'); setMensaje('')
                } catch (error) {
                    if (version !== sessionVersion.current) return
                    if ([401, 403].includes(error.status)) clearSession()
                    else setEstado('error')
                    setMensaje(error.message)
                }
            })()
        }
        restauracion.current.finally(() => { if (!control.signal.aborted) restauracion.current = null })
        return () => control.abort()
    }, [intento, authorizedFetch, clearSession, updateSession])

    useEffect(() => {
        if (expiryTimer.current) window.clearTimeout(expiryTimer.current)
        if (!session) return undefined
        const delay = Math.max(0, session.expiresAt - Date.now() - REFRESH_EARLY_MS)
        expiryTimer.current = window.setTimeout(() => { refresh().catch(() => {}) }, delay)
        return () => {
            if (expiryTimer.current) window.clearTimeout(expiryTimer.current)
        }
    }, [session, refresh])

    const logout = useCallback(() => {
        const accessToken = sessionRef.current?.accessToken
        if (accessToken) {
            try { localStorage.setItem(LOGOUT_EPOCH_KEY, nuevoLogoutEpoch()) } catch {
                // La limpieza local sigue funcionando si el almacenamiento compartido está bloqueado.
            }
        }
        clearSession()
        if (!accessToken) return Promise.resolve()
        return cerrarSesionApi(accessToken).catch(() => {
            setMensaje('La sesión se cerró en este dispositivo. No se pudo confirmar el registro de salida.')
        })
    }, [clearSession])
    const actualizarNombrePerfil = useCallback(async nombre => {
        const respuesta = await authorizedFetch(accessToken => actualizarPerfil(nombre, accessToken))
        if (typeof respuesta?.nombre !== 'string' || !respuesta.nombre.trim()) {
            throw new Error('El servicio devolvió un perfil actualizado inválido.')
        }
        const current = sessionRef.current
        if (!current?.user) throw new Error('La sesión ya no está activa.')
        updateSession({ ...current, user: { ...current.user, nombre: respuesta.nombre.trim() } })
        return respuesta.nombre.trim()
    }, [authorizedFetch, updateSession])
    const cambiarContrasena = useCallback((contrasenaActual, contrasenaNueva) =>
        authorizedFetch(accessToken => cambiarContrasenaApi(contrasenaActual, contrasenaNueva, accessToken)),
    [authorizedFetch])
    const cargarPoliticaContrasena = useCallback(() =>
        authorizedFetch(obtenerPoliticaContrasenaApi), [authorizedFetch])
    const cargarPerfilPropio = useCallback(async () => {
        const perfil = await authorizedFetch(obtenerPerfil)
        if (!perfil?.email || !perfil?.nombre || !perfil?.idRol) {
            throw new Error('El servicio no devolvió los datos del perfil autenticado.')
        }
        const current = sessionRef.current
        if (!current?.user) throw new Error('La sesión ya no está activa.')
        const rol = current.user.nombreRol || current.user.idRol
        const user = {
            ...current.user,
            ...perfil,
            usuarioID: perfil.email,
            nombreRol: rol,
            rol
        }
        updateSession({ ...current, email: perfil.email, user })
        return user
    }, [authorizedFetch, updateSession])
    const value = useMemo(() => ({
        session,
        estado,
        mensaje,
        reintentar: () => { setEstado('cargando'); setIntento(n => n + 1) },
        isAuthenticated: estado === 'autenticado' && Boolean(session?.user),
        login,
        logout,
        authorizedFetch,
        actualizarNombrePerfil,
        cambiarContrasena,
        cargarPoliticaContrasena,
        cargarPerfilPropio
    }), [session, estado, mensaje, login, logout, authorizedFetch, actualizarNombrePerfil,
        cambiarContrasena, cargarPoliticaContrasena, cargarPerfilPropio])

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

// oxlint-disable-next-line react/only-export-components
export function useAuth() {
    const context = useContext(AuthContext)
    if (!context) throw new Error('useAuth debe utilizarse dentro de AuthProvider.')
    return context
}
