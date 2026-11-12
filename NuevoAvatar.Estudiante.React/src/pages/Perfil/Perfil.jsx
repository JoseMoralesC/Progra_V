import { useEffect, useState } from 'react'
import { useLocation } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { useNotifications } from '../../context/NotificationContext'
import { Modal } from '../../components/Modal'

const camposPerfil = [
    { key: 'nombre', label: 'Nombre' },
    { key: 'email', label: 'Correo electrónico' },
    { key: 'tipoIdentificacion', label: 'Tipo de identificación' },
    { key: 'identificacion', label: 'Identificación' }
]

function Perfil() {
    const location = useLocation()
    const esPerfilEstudiantil = location.pathname === '/estudiante/perfil'
    const { cargarPerfilPropio, actualizarNombrePerfil, cambiarContrasena, cargarPoliticaContrasena } = useAuth()
    const { notify } = useNotifications()
    const [usuario, setUsuario] = useState(null)
    const [cargando, setCargando] = useState(true)
    const [errorCarga, setErrorCarga] = useState('')
    const nombreActual = usuario?.nombre ?? ''
    const [nombreEditado, setNombreEditado] = useState('')
    const [correoEditado, setCorreoEditado] = useState('')
    const [editando, setEditando] = useState(false)
    const [cambiandoContrasena, setCambiandoContrasena] = useState(false)
    const [guardando, setGuardando] = useState(false)
    const [errorNombre, setErrorNombre] = useState('')
    const [errorCorreo, setErrorCorreo] = useState('')
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
        if (esPerfilEstudiantil) {
            setCorreoEditado(usuario?.email || '')
            setErrorCorreo('')
        } else {
            setNombreEditado(nombreActual)
            setErrorNombre('')
        }
        setEditando(true)
    }

    function cancelarEdicion() {
        setNombreEditado('')
        setCorreoEditado('')
        setErrorNombre('')
        setErrorCorreo('')
        setEditando(false)
    }

    function guardarCorreo(event) {
        event.preventDefault()
        if (guardando) return
        const correo = correoEditado.trim()
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(correo)) {
            setErrorCorreo('Ingresa un correo electrónico válido.')
            return
        }
        if (correo.toLocaleLowerCase() === String(usuario?.email || '').toLocaleLowerCase()) {
            cancelarEdicion()
            return
        }

        // El endpoint actual de perfil solo acepta "nombre"; no enviar un payload incompatible.
        setErrorCorreo('El backend actual no permite cambiar el correo electrónico. No se guardaron cambios; se requiere habilitar esta operación en Usuario y Gateway.')
    }

    function abrirCambioContrasena() {
        setContrasenaActual('')
        setContrasenaNueva('')
        setConfirmacionContrasena('')
        setErrorContrasena('')
        setCambiandoContrasena(true)
    }

    function cerrarCambioContrasena() {
        if (guardandoContrasena) return
        setContrasenaActual('')
        setContrasenaNueva('')
        setConfirmacionContrasena('')
        setErrorContrasena('')
        setCambiandoContrasena(false)
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
            cancelarEdicion()
            notify('Nombre actualizado correctamente.', 'success')
        } catch (error) {
            setErrorNombre(error.message || 'No fue posible actualizar el nombre.')
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
            setErrorContrasena('La contraseña supera el tamaño permitido. Elige una contraseña más corta.')
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
            setCambiandoContrasena(false)
            setErrorContrasena('')
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
            if (/bcrypt|72\s*bytes|utf-8/i.test(mensaje)) {
                mensaje = 'La contraseña supera el tamaño permitido. Elige una contraseña más corta.'
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
        ...camposPerfil.map(campo => ({ ...campo, value: usuario[campo.key] })),
        { key: 'rol', label: 'Rol', value: usuario.nombreRol || usuario.idRol }
    ]

    const requisitosContrasena = politicaContrasena ? [
        `Mínimo ${politicaContrasena.minLength} caracteres`,
        `Máximo ${politicaContrasena.maxLength} caracteres`,
        ...(politicaContrasena.requireUppercase ? ['Al menos una letra mayúscula'] : []),
        ...(politicaContrasena.requireNumber ? ['Al menos un número'] : [])
    ] : []

    return (
        <>
        <section className="panel" aria-labelledby="perfil-heading">
            <h2 id="perfil-heading">Datos del usuario</h2>
            <div className="form-grid">
                {datos.map(({ key, label, value }) => (
                    <div className="field" key={key}>
                        <span className="helper">{label}</span>
                        <p>{value || 'No disponible'}</p>
                    </div>
                ))}
            </div>
            <div className="profile-actions">
                <button className="btn btn-primary" type="button" onClick={iniciarEdicion}>Editar perfil</button>
                <button className="btn btn-soft" type="button" onClick={abrirCambioContrasena}>Cambiar contraseña</button>
            </div>
        </section>

        {editando && <Modal titulo="Editar perfil" onCerrar={cancelarEdicion} ocupado={guardando}>
            {esPerfilEstudiantil ? <form className="profile-modal-form" onSubmit={guardarCorreo} noValidate>
                <div className="field">
                    <label htmlFor="perfil-correo">Correo electrónico</label>
                    <input id="perfil-correo" type="email" value={correoEditado} maxLength={254}
                        aria-invalid={Boolean(errorCorreo)} aria-describedby={errorCorreo ? 'perfil-correo-error' : undefined}
                        disabled={guardando} onChange={event => {
                            setCorreoEditado(event.target.value)
                            if (errorCorreo) setErrorCorreo('')
                        }} autoFocus />
                    {errorCorreo && <span id="perfil-correo-error" className="status-danger" role="alert">{errorCorreo}</span>}
                </div>
                <div className="profile-actions">
                    <button className="btn btn-primary" type="submit" disabled={guardando}>Guardar cambios</button>
                    <button className="btn btn-soft" type="button" onClick={cancelarEdicion} disabled={guardando}>Cancelar</button>
                </div>
            </form> : <form className="profile-modal-form" onSubmit={guardarNombre} noValidate>
                <div className="field">
                    <label htmlFor="perfil-nombre">Nombre</label>
                    <input id="perfil-nombre" type="text" value={nombreEditado} maxLength={150}
                        aria-invalid={Boolean(errorNombre)} aria-describedby={errorNombre ? 'perfil-nombre-error' : undefined}
                        disabled={guardando} onChange={event => setNombreEditado(event.target.value)} autoFocus />
                    {errorNombre && <span id="perfil-nombre-error" className="status-danger" role="alert">{errorNombre}</span>}
                </div>
                <div className="profile-actions">
                    <button className="btn btn-primary" type="submit" disabled={guardando}>{guardando ? 'Guardando…' : 'Guardar cambios'}</button>
                    <button className="btn btn-soft" type="button" onClick={cancelarEdicion} disabled={guardando}>Cancelar</button>
                </div>
            </form>}
        </Modal>}

        {cambiandoContrasena && <Modal titulo="Cambiar contraseña" onCerrar={cerrarCambioContrasena} ocupado={guardandoContrasena}>
            {cargandoPolitica && <p role="status">Cargando los requisitos de contraseña…</p>}
            {!cargandoPolitica && errorPolitica && <div className="status status-danger" role="alert">
                <p>No se pudo cargar la política institucional. El cambio de contraseña está bloqueado.</p>
                <p>{errorPolitica}</p>
                <button className="btn btn-primary" type="button" onClick={() => setIntentoPolitica(intento => intento + 1)}>Reintentar</button>
            </div>}
            {politicaContrasena && <ul className="password-policy-list" aria-label="Requisitos de contraseña">
                {requisitosContrasena.map(requisito => <li key={requisito}>{requisito}</li>)}
            </ul>}
            <form className="profile-modal-form" onSubmit={guardarContrasena} noValidate>
                <div className="form-grid">
                    <div className="field"><label htmlFor="contrasena-actual">Contraseña actual</label>
                        <input id="contrasena-actual" type="password" autoComplete="current-password" value={contrasenaActual}
                            disabled={guardandoContrasena} onChange={event => setContrasenaActual(event.target.value)} required /></div>
                    <div className="field"><label htmlFor="contrasena-nueva">Nueva contraseña</label>
                        <input id="contrasena-nueva" type="password" autoComplete="new-password" value={contrasenaNueva}
                            disabled={guardandoContrasena} onChange={event => setContrasenaNueva(event.target.value)} required /></div>
                    <div className="field"><label htmlFor="contrasena-confirmacion">Confirmar nueva contraseña</label>
                        <input id="contrasena-confirmacion" type="password" autoComplete="new-password" value={confirmacionContrasena}
                            disabled={guardandoContrasena} onChange={event => setConfirmacionContrasena(event.target.value)} required /></div>
                </div>
                {errorContrasena && <p className="status status-danger" role="alert">{errorContrasena}</p>}
                <div className="profile-actions">
                    <button className="btn btn-primary" type="submit" disabled={guardandoContrasena || cargandoPolitica || !politicaContrasena}>
                        {guardandoContrasena ? 'Actualizando…' : 'Actualizar contraseña'}
                    </button>
                    <button className="btn btn-soft" type="button" onClick={cerrarCambioContrasena} disabled={guardandoContrasena}>Cancelar</button>
                </div>
            </form>
        </Modal>}
        </>
    )
}

export default Perfil
