
import { useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

const MENSAJE_CREDENCIALES = 'Usuario y/o contraseña incorrectos'

function Login() {
    const [usuario, setUsuario] = useState('')
    const [contrasena, setContrasena] = useState('')
    const [mostrarContrasena, setMostrarContrasena] = useState(false)
    const [submitting, setSubmitting] = useState(false)
    const [errorIngreso, setErrorIngreso] = useState('')
    const { login, isAuthenticated, mensaje } = useAuth()
    const navigate = useNavigate()
    const location = useLocation()

    if (isAuthenticated) return <Navigate to="/inicio" replace />

    async function handleSubmit(event) {
        event.preventDefault()
        if (submitting) return

        setErrorIngreso('')

        if (!usuario.trim() || !contrasena.trim()) {
            setErrorIngreso(MENSAJE_CREDENCIALES)
            return
        }

        setSubmitting(true)

        try {
            await login(usuario.trim(), contrasena)
            setContrasena('')
            navigate(location.state?.from?.pathname || '/inicio', { replace: true })
        } catch (error) {
            if (error.name === 'SessionExpiredError' || error.name === 'SessionChangedError') return

            if (error.phase === 'profile') {
                setErrorIngreso(`No fue posible cargar el perfil: ${error.message}`)
            }
            else if (
                error.phase === 'authentication' &&
                [400, 401, 403].includes(error.status)
            ) {
                setErrorIngreso(MENSAJE_CREDENCIALES)
            }
            else if (error.message === 'Ingresa un correo institucional válido y tu contraseña.') {
                setErrorIngreso(MENSAJE_CREDENCIALES)
            }
            else {
                setErrorIngreso(error.message || 'Error de conexión con los servicios.')
            }
        } finally {
            setSubmitting(false)
        }
    }

    return (
        <main className="login-page">
            <section className="panel login-panel">
                <span className="section-label">NUEVO AVATAR</span>
                <h1>Iniciar sesión</h1>
                <p className="subtitle">Ingresa con tus credenciales institucionales.</p>

                {(errorIngreso || mensaje) && (
                    <p role="alert" className="status status-danger">
                        {errorIngreso || mensaje}
                    </p>
                )}

                <form className="login-form" onSubmit={handleSubmit} noValidate>
                    <label htmlFor="login-user">Correo institucional</label>
                    <input
                        id="login-user"
                        name="usuario"
                        type="email"
                        autoComplete="username"
                        value={usuario}
                        onChange={event => setUsuario(event.target.value)}
                        required
                        disabled={submitting}
                    />

                    <label htmlFor="login-password">Contraseña</label>
                    <div className="login-password-field">
                        <input
                            id="login-password"
                            name="contrasena"
                            type={mostrarContrasena ? 'text' : 'password'}
                            autoComplete="current-password"
                            value={contrasena}
                            onChange={event => setContrasena(event.target.value)}
                            required
                            disabled={submitting}
                        />
                        <button
                            className="login-password-toggle"
                            type="button"
                            aria-label={mostrarContrasena ? 'Ocultar contraseña' : 'Mostrar contraseña'}
                            aria-pressed={mostrarContrasena}
                            onClick={() => setMostrarContrasena(visible => !visible)}
                            disabled={submitting}
                        >
                            {mostrarContrasena ? (
                                <svg aria-hidden="true" viewBox="0 0 24 24" focusable="false">
                                    <path d="M3 3l18 18M10.6 10.6a2 2 0 002.8 2.8M9.9 5.2A10.8 10.8 0 0112 5c5 0 8.3 4.5 9 7-.3 1.1-1.1 2.5-2.3 3.7M6.2 6.2C4.4 7.4 3.3 9.4 3 12c.7 2.5 4 7 9 7 1.1 0 2.1-.2 3-.6" />
                                </svg>
                            ) : (
                                <svg aria-hidden="true" viewBox="0 0 24 24" focusable="false">
                                    <path d="M3 12s3.3-7 9-7 9 7 9 7-3.3 7-9 7-9-7-9-7z" />
                                    <circle cx="12" cy="12" r="3" />
                                </svg>
                            )}
                        </button>
                    </div>

                    <button
                        className="logout-btn login-submit"
                        type="submit"
                        disabled={submitting}
                    >
                        {submitting ? 'Ingresando…' : 'Ingresar'}
                    </button>
                </form>
            </section>
        </main>
    )
}

export default Login
