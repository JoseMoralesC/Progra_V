import { useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

function Login() {
    const [usuario, setUsuario] = useState('')
    const [contrasena, setContrasena] = useState('')
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
            else if (error.status === 401 && error.phase === 'authentication') setErrorIngreso('Usuario y/o contraseña incorrectos')
            else setErrorIngreso(error.message || 'Error de conexión con los servicios.')
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
                {(errorIngreso || mensaje) && <p role="alert" className="status status-danger">{errorIngreso || mensaje}</p>}
                <form className="login-form" onSubmit={handleSubmit}>
                    <label htmlFor="login-user">Correo institucional</label>
                    <input id="login-user" name="usuario" type="email" autoComplete="username" value={usuario} onChange={event => setUsuario(event.target.value)} required disabled={submitting} />
                    <label htmlFor="login-password">Contraseña</label>
                    <input id="login-password" name="contrasena" type="password" autoComplete="current-password" value={contrasena} onChange={event => setContrasena(event.target.value)} required disabled={submitting} />
                    <button className="logout-btn login-submit" type="submit" disabled={submitting}>
                        {submitting ? 'Ingresando…' : 'Ingresar'}
                    </button>
                </form>
            </section>
        </main>
    )
}

export default Login
