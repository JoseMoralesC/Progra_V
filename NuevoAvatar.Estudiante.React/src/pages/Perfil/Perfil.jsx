import { useState } from 'react'
import { useAuth } from '../../context/AuthContext'
import { useNotifications } from '../../context/NotificationContext'

const camposPerfil = [
    { key: 'nombre', label: 'Nombre' },
    { key: 'email', label: 'Correo electrónico' },
    { key: 'tipoIdentificacion', label: 'Tipo de identificación' },
    { key: 'identificacion', label: 'Identificación' }
]

function Perfil() {
    const { session, actualizarNombrePerfil } = useAuth()
    const { notify } = useNotifications()
    const usuario = session?.user ?? {}
    const nombreActual = usuario.nombre ?? ''
    const [nombreEditado, setNombreEditado] = useState('')
    const [editando, setEditando] = useState(false)
    const [guardando, setGuardando] = useState(false)
    const [errorNombre, setErrorNombre] = useState('')

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
            await actualizarNombrePerfil(nombreNormalizado)
            setNombreEditado('')
            setEditando(false)
            notify('Nombre actualizado correctamente.', 'success')
        } catch (error) {
            setNombreEditado('')
            setEditando(false)
            notify(error.message || 'No fue posible actualizar el nombre.', 'error')
        } finally {
            setGuardando(false)
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
    )
}

export default Perfil
