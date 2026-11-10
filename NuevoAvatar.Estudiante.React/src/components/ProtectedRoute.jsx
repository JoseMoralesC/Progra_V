import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

function ProtectedRoute() {
    const { isAuthenticated, estado, mensaje, reintentar } = useAuth()
    const location = useLocation()
    if (estado === 'cargando') return <main className="login-page"><p role="status">Comprobando sesión…</p></main>
    if (estado === 'error') return <main className="login-page"><section className="panel">
        <h1>No se pudo comprobar tu sesión</h1><p role="alert">{mensaje}</p>
        <button type="button" onClick={reintentar}>Reintentar</button>
    </section></main>
    if (!isAuthenticated) return <Navigate to="/login" replace state={{ from: location }} />
    return <Outlet />
}

export default ProtectedRoute
