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
    const { cargarPerfilPropio, actualizarNombrePerfil, cambiarContrasena } = useAuth()
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

    function validarContrasena(valor) {
        const caracteres = [...valor]
        return caracteres.length >= 8
            && /\p{Lu}/u.test(valor)
            && /\p{Ll}/u.test(valor)
            && /\p{Nd}/u.test(valor)
            && /[\p{P}\p{S}]/u.test(valor)
    }

    function superaLimiteBcrypt(valor) {
        return new TextEncoder().encode(valor).length > 72
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
        if (!validarContrasena(contrasenaNueva)) {
            setErrorContrasena('La nueva contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.')
            return
        }
        if (superaLimiteBcrypt(contrasenaNueva)) {
            setErrorContrasena('La nueva contraseña no puede superar 72 bytes en UTF-8 por el límite de BCrypt.')
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
            <p>Usa al menos 8 caracteres e incluye mayúsculas, minúsculas, números y un carácter especial. Máximo 72 bytes en UTF-8.</p>
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
                    <button className="btn btn-primary" type="submit" disabled={guardandoContrasena}>
                        {guardandoContrasena ? 'Actualizando…' : 'Actualizar contraseña'}
                    </button>
                </div>
            </form>
        </section>
        </>
    )
}

export default Perfil
