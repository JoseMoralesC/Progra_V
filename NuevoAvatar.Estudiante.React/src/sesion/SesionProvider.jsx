import { useEffect, useRef, useState } from 'react'
import { ClienteSesion, ErrorApi } from '../../../web/jose/src/sesion.js'
import { SesionContext } from './SesionContext'
import { useLocation } from 'react-router-dom'

const roles = ['ADMINISTRADOR', 'PROFESOR', 'ESTUDIANTE']

function validarUsuario(usuario) {
    if (typeof usuario?.nombre !== 'string' || !usuario.nombre.trim() || !roles.includes(usuario.rol)) {
        throw new ErrorApi('La cuenta no tiene un perfil autorizado para esta aplicación.', 403)
    }
    return usuario
}

async function conPermisos(cliente, datos) {
    const usuario = validarUsuario(datos)
    const modulos = await cliente.request('/gateway/menu')
    if (!Array.isArray(modulos) || modulos.some(m => !m || typeof m.idModulo !== 'string' ||
        typeof m.nombreModulo !== 'string' || typeof m.activo !== 'boolean' || !Number.isInteger(m.orden))) {
        throw new ErrorApi('No se pudieron comprobar los permisos.', 503)
    }
    return { ...usuario, modulos: modulos.filter(m => m.activo) }
}

// ADM2: una sola instancia compartida. La identidad procede de /sesion;
// no se deduce del correo ni de datos de demostración de la plantilla.
export function SesionProvider({ children }) {
    const [estado, setEstado] = useState('cargando')
    const [usuario, setUsuario] = useState(null)
    const [mensaje, setMensaje] = useState('')
    const [intento, setIntento] = useState(0)
    const restaurando = useRef(null)
    const location = useLocation()
    const [rutaVerificada, setRutaVerificada] = useState(null)
    const [cliente] = useState(() => new ClienteSesion({
        onExpired: () => {
            setUsuario(null)
            setEstado('sin-sesion')
            setMensaje('La sesión venció. Inicia sesión nuevamente.')
        }
    }))

    useEffect(() => {
        if (!cliente.tokens) return

        const control = new AbortController()
        const version = cliente.generation
        // StrictMode puede repetir el efecto; ambos comparten la misma consulta
        // y renovación, sin restaurar una cuenta después de salir.
        if (restaurando.current?.version !== version) {
            restaurando.current = { version, consulta: cliente.actual().then(datos => conPermisos(cliente, datos)) }
        }
        const consulta = restaurando.current.consulta
        consulta.then(datos => {
            if (control.signal.aborted || version !== cliente.generation || !cliente.tokens) return
            setUsuario(validarUsuario(datos))
            setMensaje('')
            setEstado('autenticado')
            setRutaVerificada(location.key)
        }).catch(error => {
            if (control.signal.aborted || version !== cliente.generation) return
            setUsuario(null)
            if (error.status === 403) cliente.limpiar()
            setMensaje(error.message)
            setEstado(cliente.tokens ? 'error' : 'sin-sesion')
        }).finally(() => {
            if (restaurando.current?.consulta === consulta) restaurando.current = null
        })

        return () => control.abort()
    }, [cliente, intento, location.key])

    useEffect(() => {
        if (estado !== 'autenticado' || !cliente.tokens) return
        const control = new AbortController()
        const version = cliente.generation
        // También se renueva mientras el usuario permanece en el inicio sin
        // hacer consultas de negocio. ClienteSesion comparte refresh simultáneos.
        const espera = Math.max(0, Date.parse(cliente.tokens.expires_in) - Date.now() - 15000)
        const timer = window.setTimeout(async () => {
            try {
                const datos = await conPermisos(cliente, await cliente.actual())
                if (control.signal.aborted || version !== cliente.generation || !cliente.tokens) return
                setUsuario(validarUsuario(datos))
            } catch (error) {
                if (control.signal.aborted || version !== cliente.generation) return
                setUsuario(null)
                if (error.status === 403) cliente.limpiar()
                setMensaje(error.message)
                setEstado(cliente.tokens ? 'error' : 'sin-sesion')
            }
        }, Math.min(espera, 2147483647))
        return () => {
            control.abort()
            window.clearTimeout(timer)
        }
    }, [cliente, estado, usuario])

    async function autenticar(datos) {
        if (!cliente.tokens) return
        const version = cliente.generation
        try {
            const actual = await conPermisos(cliente, datos)
            if (version !== cliente.generation || !cliente.tokens) return
            setUsuario(actual)
            setMensaje('')
            setEstado('autenticado')
            setRutaVerificada(location.key)
        } catch (error) {
            if (version === cliente.generation) cliente.limpiar()
            throw error
        }
    }

    async function salir() {
        // logout limpia tokens antes de esperar al endpoint. También se retira
        // la identidad del layout aunque Auth no esté disponible.
        const pendiente = cliente.logout()
        const version = cliente.generation
        setUsuario(null)
        setMensaje('')
        setEstado('sin-sesion')
        try {
            await pendiente
        } catch {
            if (cliente.generation === version) {
                setMensaje('La sesión se cerró en este dispositivo. No se pudo confirmar el registro de salida.')
            }
        }
    }

    function reintentar() {
        setMensaje('')
        setEstado('cargando')
        setIntento(actual => actual + 1)
    }

    return (
        <SesionContext.Provider value={{ cliente, usuario,
            estado: estado === 'cargando' && !cliente.tokens ? 'sin-sesion' :
                estado === 'autenticado' && rutaVerificada !== location.key ? 'cargando' : estado,
            mensaje, autenticar, salir, reintentar }}>
            {children}
        </SesionContext.Provider>
    )
}
