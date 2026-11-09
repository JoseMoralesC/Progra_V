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
    obtenerRol,
    obtenerPerfil,
    obtenerUsuario,
    renovarSesion
} from '../services/api'
import { useNotifications } from './NotificationContext'

const AuthContext = createContext(null)
const REFRESH_EARLY_MS = 30_000

class SessionExpiredError extends Error {
    constructor() {
        super('La sesión expiró. Inicia sesión nuevamente.')
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
    const { notify } = useNotifications()
    const [session, setSession] = useState(null)
    const sessionRef = useRef(null)
    const sessionVersion = useRef(0)
    const refreshPromise = useRef(null)
    const expiryTimer = useRef(null)

    const updateSession = useCallback(value => {
        sessionRef.current = value
        setSession(value)
    }, [])

    const clearSession = useCallback(() => {
        sessionVersion.current += 1
        if (expiryTimer.current) window.clearTimeout(expiryTimer.current)
        expiryTimer.current = null
        refreshPromise.current = null
        updateSession(null)
    }, [updateSession])

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
                    ? 'La sesión expiró. Inicia sesión nuevamente.'
                    : 'No fue posible renovar la sesión. Inicia sesión nuevamente.'
                notify(message, 'warning', 0)
                throw new SessionExpiredError()
            })
            .finally(() => {
                if (refreshPromise.current === pending) refreshPromise.current = null
            })
        refreshPromise.current = pending

        return refreshPromise.current
    }, [clearSession, notify, updateSession])

    const authorizedFetch = useCallback(async fetcher => {
        const current = sessionRef.current
        if (!current) throw new Error('La sesión ya no está activa.')
        let accessToken = current.accessToken
        if (Date.now() >= current.expiresAt - REFRESH_EARLY_MS) accessToken = await refresh()
        try {
            return await fetcher(accessToken)
        } catch (error) {
            if (error.status !== 401) throw error
            accessToken = await refresh()
            return fetcher(accessToken)
        }
    }, [refresh])

    const login = useCallback(async (usuario, contrasena) => {
        clearSession()
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
            user: null
        }
        updateSession(next)

        try {
            let user = await authorizedFetch(accessToken => obtenerUsuario(next.email, accessToken))
            if (version !== sessionVersion.current) throw new SessionChangedError()
            if (!user?.idRol) throw new Error('El perfil no contiene el rol esperado.')
            const role = await authorizedFetch(accessToken => obtenerRol(user.idRol, accessToken))
            if (version !== sessionVersion.current) throw new SessionChangedError()
            next = {
                ...sessionRef.current,
                user: { ...user, nombreRol: role?.nombreRol || user.idRol }
            }
            updateSession(next)
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
        clearSession()
        if (!accessToken) return Promise.resolve()
        return cerrarSesionApi(accessToken).catch(() => {
            notify('La sesión se cerró en este navegador, pero no se pudo confirmar el registro de salida.', 'warning')
        })
    }, [clearSession, notify])
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
        isAuthenticated: Boolean(session?.user),
        login,
        logout,
        authorizedFetch,
        actualizarNombrePerfil,
        cambiarContrasena,
        cargarPerfilPropio
    }), [session, login, logout, authorizedFetch, actualizarNombrePerfil, cambiarContrasena, cargarPerfilPropio])

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

// oxlint-disable-next-line react/only-export-components
export function useAuth() {
    const context = useContext(AuthContext)
    if (!context) throw new Error('useAuth debe utilizarse dentro de AuthProvider.')
    return context
}
