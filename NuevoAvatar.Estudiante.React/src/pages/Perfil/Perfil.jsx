import { useEffect, useState } from 'react'
import { useAuth } from '../../context/AuthContext'
import { useNotifications } from '../../context/NotificationContext'

const camposPerfil = [
    { key: 'nombre', label: 'Nombre' },
    { key: 'email', label: 'Correo electrónico' },
    { key: 'tipoIdentificacion', label: 'Tipo de identificación' },
    { key: 'identificacion', label: 'Identificación' }
]

function Perfil() {
    const { cargarPerfilPropio, actualizarNombrePerfil, cambiarContrasena, cargarPoliticaContrasena } = useAuth()
    const { notify } = useNotifications()
    const [usuario, setUsuario] = useState(null)
    const [cargando, setCargando] = useState(true)
    const [errorCarga, setErrorCarga] = useState('')
    const nombreActual = usuario?.nombre ?? ''
    const [nombreEditado, setNombreEditado] = useState('')
    const [editando, setEditando] = useState(false)
    const [guardando, setGuardando] = useState(false)
    const [errorNombre, setErrorNombre] = useState('')
    const [contrasenaActual, setContrasenaActual] = useState('')
    const [contrasenaNueva, setContrasenaNueva] = useState('')
    const [confirmacionContrasena, setConfirmacionContrasena] = useState('')
    const [guardandoContrasena, setGuardandoContrasena] = useState(false)
    const [errorContrasena, setErrorContrasena] = useState('')
    const [politicaContrasena, setPoliticaContrasena] = useState(null)
    const [cargandoPolitica, setCargandoPolitica] = useState(true)
    const [errorPolitica, setErrorPolitica] = useState('')
    const [intentoPolitica, setIntentoPolitica] = useState(0)

    function validarContrasena(valor, politica) {
        const longitud = [...valor].length
        if (longitud < politica.minLength || longitud > politica.maxLength) {
            return `La contraseña debe tener entre ${politica.minLength} y ${politica.maxLength} caracteres.`
        }
        if (politica.requireUppercase && !/\p{Lu}/u.test(valor)) {
            return 'La contraseña debe incluir al menos una letra mayúscula.'
        }
        if (politica.requireNumber && !/\p{Nd}/u.test(valor)) {
            return 'La contraseña debe incluir al menos un número.'
        }
        return ''
    }

    function superaLimiteBcrypt(valor, maxBcryptUtf8Bytes) {
        return new TextEncoder().encode(valor).length > maxBcryptUtf8Bytes
    }

    async function cargar() {
        setCargando(true)
        setErrorCarga('')
        try {
            setUsuario(await cargarPerfilPropio())
        } catch (error) {
            setErrorCarga(error.message || 'No fue posible cargar tu perfil.')
        } finally {
            setCargando(false)
        }
    }

    useEffect(() => {
        let activo = true
        cargarPerfilPropio()
            .then(perfil => { if (activo) setUsuario(perfil) })
            .catch(error => {
                if (activo) setErrorCarga(error.message || 'No fue posible cargar tu perfil.')
            })
            .finally(() => { if (activo) setCargando(false) })
        return () => { activo = false }
    }, [cargarPerfilPropio])

    useEffect(() => {
        let activo = true
        setCargandoPolitica(true)
        setPoliticaContrasena(null)
        setErrorPolitica('')
        cargarPoliticaContrasena()
            .then(politica => {
                if (!Number.isInteger(politica?.minLength) || !Number.isInteger(politica?.maxLength) ||
                    politica.minLength < 1 || politica.maxLength < politica.minLength ||
                    typeof politica.requireUppercase !== 'boolean' || typeof politica.requireNumber !== 'boolean' ||
                    politica.maxBcryptUtf8Bytes !== 72) {
                    throw new Error('El servicio devolvió una política de contraseña inválida.')
                }
                if (activo) setPoliticaContrasena(politica)
            })
            .catch(error => {
                if (activo) {
                    setPoliticaContrasena(null)
                    setErrorPolitica(error.message || 'No fue posible cargar la política institucional de contraseña.')
                }
            })
            .finally(() => { if (activo) setCargandoPolitica(false) })
        return () => { activo = false }
    }, [cargarPoliticaContrasena, intentoPolitica])

    if (cargando) return <section className="panel" role="status">Cargando tu perfil…</section>
    if (errorCarga || !usuario) return (
        <section className="panel" role="alert">
            <h2>No fue posible cargar tu perfil</h2>
            <p>{errorCarga || 'La sesión no devolvió datos de perfil.'}</p>
            <button className="btn btn-primary" type="button" onClick={cargar}>Reintentar</button>
        </section>
    )

    function iniciarEdicion() {
        setNombreEditado(nombreActual)
        setErrorNombre('')
        setEditando(true)
    }

    function cancelarEdicion() {
        setNombreEditado('')
        setErrorNombre('')
        setEditando(false)
    }

    async function guardarNombre(event) {
        event.preventDefault()
        if (guardando) return

        const nombreNormalizado = nombreEditado.trim()
        if (!nombreNormalizado) {
            setErrorNombre('El nombre es obligatorio.')
            return
        }
        if (nombreNormalizado.length > 150) {
            setErrorNombre('El nombre no puede superar los 150 caracteres.')
            return
        }

        setErrorNombre('')
        setGuardando(true)
        try {
            const nombre = await actualizarNombrePerfil(nombreNormalizado)
            setUsuario(actual => ({ ...actual, nombre }))
            setNombreEditado('')
            setEditando(false)
            notify('Nombre actualizado correctamente.', 'success')
        } catch (error) {
            notify(error.message || 'No fue posible actualizar el nombre.', 'error')
        } finally {
            setGuardando(false)
        }
    }

    async function guardarContrasena(event) {
        event.preventDefault()
        if (guardandoContrasena) return
        setErrorContrasena('')
        if (!contrasenaActual) {
            setErrorContrasena('Ingresa tu contraseña actual.')
            return
        }
        if (!politicaContrasena) {
            setErrorContrasena('No se puede cambiar la contraseña sin cargar la política institucional vigente.')
            return
        }
        const errorPoliticaContrasena = validarContrasena(contrasenaNueva, politicaContrasena)
        if (errorPoliticaContrasena) {
            setErrorContrasena(errorPoliticaContrasena)
            return
        }
        if (superaLimiteBcrypt(contrasenaNueva, politicaContrasena.maxBcryptUtf8Bytes)) {
            setErrorContrasena(`La contraseña no puede superar ${politicaContrasena.maxBcryptUtf8Bytes} bytes UTF-8 por el límite de BCrypt.`)
            return
        }
        if (contrasenaNueva !== confirmacionContrasena) {
            setErrorContrasena('La confirmación no coincide con la nueva contraseña.')
            return
        }

        setGuardandoContrasena(true)
        try {
            await cambiarContrasena(contrasenaActual, contrasenaNueva)
            setContrasenaActual('')
            setContrasenaNueva('')
            setConfirmacionContrasena('')
            notify('Contraseña actualizada correctamente.', 'success')
        } catch (error) {
            setContrasenaActual('')
            setContrasenaNueva('')
            setConfirmacionContrasena('')
            let mensaje = error.message || 'No fue posible actualizar la contraseña.'
            try {
                const detalle = JSON.parse(mensaje)
                mensaje = detalle.detail || detalle.message || detalle.error || mensaje
            } catch {
                // Mantiene el mensaje HTTP devuelto por la API.
            }
            if (error.status === 401) mensaje = 'La sesión expiró. Inicia sesión nuevamente.'
            if (error.status === 503) mensaje = 'No fue posible obtener o aplicar la política institucional desde Parámetro. El cambio no se realizó; inténtalo de nuevo más tarde.'
            setErrorContrasena(mensaje)
            notify(mensaje, 'error')
        } finally {
            setGuardandoContrasena(false)
        }
    }

    const datos = [
        ...camposPerfil.map(campo => ({
            ...campo,
            value: campo.key === 'nombre' && editando ? nombreEditado : usuario[campo.key]
        })),
        { key: 'rol', label: 'Rol', value: usuario.nombreRol || usuario.idRol }
    ]

    return (
        <>
        <section className="panel" aria-labelledby="perfil-heading">
            <h2 id="perfil-heading">Datos del usuario</h2>
            <form onSubmit={guardarNombre} noValidate>
                <div className="form-grid">
                    {datos.map(({ key, label, value }) => (
                        <div className="field" key={key}>
                            <label htmlFor={`perfil-${key}`}>{label}</label>
                            <input
                                id={`perfil-${key}`}
                                type="text"
                                value={value ?? ''}
                                readOnly={key !== 'nombre' || !editando}
                                aria-invalid={key === 'nombre' && Boolean(errorNombre)}
                                aria-describedby={key === 'nombre' && errorNombre ? 'perfil-nombre-error' : undefined}
                                onChange={key === 'nombre' && editando ? event => setNombreEditado(event.target.value) : undefined}
                                disabled={key === 'nombre' && guardando}
                            />
                            {key === 'nombre' && errorNombre && (
                                <span id="perfil-nombre-error" className="status-danger" role="alert">
                                    {errorNombre}
                                </span>
                            )}
                        </div>
                    ))}
                </div>
                <div className="profile-actions">
                    {editando ? (
                        <>
                            <button className="btn btn-primary" type="button" onClick={guardarNombre} disabled={guardando}>
                                {guardando ? 'Guardando…' : 'Guardar'}
                            </button>
                            <button className="btn btn-soft" type="button" onClick={cancelarEdicion} disabled={guardando}>
                                Cancelar
                            </button>
                        </>
                    ) : (
                        <button className="btn btn-primary" type="button" onClick={iniciarEdicion}>
                            Editar
                        </button>
                    )}
                </div>
            </form>
            <p className="subtitle profile-session-note">
                Salir cierra la sesión en este navegador. No cerrará sesiones iniciadas en otros dispositivos.
            </p>
        </section>
        <section className="panel" aria-labelledby="contrasena-heading">
            <h2 id="contrasena-heading">Cambiar contraseña</h2>
            {cargandoPolitica && <p role="status">Cargando la política institucional de contraseña…</p>}
            {!cargandoPolitica && errorPolitica && <div className="status status-danger" role="alert">
                <p>No fue posible cargar la política institucional desde Parámetro. El cambio de contraseña está bloqueado.</p>
                <p>{errorPolitica}</p>
                <button className="btn btn-primary" type="button" onClick={() => setIntentoPolitica(intento => intento + 1)}>
                    Reintentar
                </button>
            </div>}
            {politicaContrasena && <p>La contraseña debe tener entre {politicaContrasena.minLength} y {politicaContrasena.maxLength} caracteres
                {politicaContrasena.requireUppercase ? ', incluir una mayúscula' : ''}
                {politicaContrasena.requireNumber ? ', incluir un número' : ''}.
                {' '}Máximo técnico de BCrypt: {politicaContrasena.maxBcryptUtf8Bytes} bytes UTF-8.</p>}
            <form onSubmit={guardarContrasena} noValidate>
                <div className="form-grid">
                    <div className="field">
                        <label htmlFor="contrasena-actual">Contraseña actual</label>
                        <input id="contrasena-actual" type="password" autoComplete="current-password"
                            value={contrasenaActual} disabled={guardandoContrasena}
                            onChange={event => setContrasenaActual(event.target.value)} required />
                    </div>
                    <div className="field">
                        <label htmlFor="contrasena-nueva">Nueva contraseña</label>
                        <input id="contrasena-nueva" type="password" autoComplete="new-password"
                            value={contrasenaNueva} disabled={guardandoContrasena}
                            onChange={event => setContrasenaNueva(event.target.value)} required />
                    </div>
                    <div className="field">
                        <label htmlFor="contrasena-confirmacion">Confirmar nueva contraseña</label>
                        <input id="contrasena-confirmacion" type="password" autoComplete="new-password"
                            value={confirmacionContrasena} disabled={guardandoContrasena}
                            onChange={event => setConfirmacionContrasena(event.target.value)} required />
                    </div>
                </div>
                {errorContrasena && <p className="status status-danger" role="alert">{errorContrasena}</p>}
                <div className="profile-actions">
                    <button className="btn btn-primary" type="submit" disabled={guardandoContrasena || cargandoPolitica || !politicaContrasena}>
                        {guardandoContrasena ? 'Actualizando…' : 'Actualizar contraseña'}
                    </button>
                </div>
            </form>
        </section>
        </>
    )
}

export default Perfil
