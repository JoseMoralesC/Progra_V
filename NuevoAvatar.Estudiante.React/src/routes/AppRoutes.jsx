import { Navigate, Route, Routes, useNavigate } from 'react-router-dom'
import MainLayout from '../layouts/MainLayout'
import Login from '../pages/Login/Login'
import ProtectedRoute from '../components/ProtectedRoute'
import { useAuth } from '../context/AuthContext'
import { useNotifications } from '../context/NotificationContext'

function AdminPage({ title, description }) {
    const { session, logout } = useAuth()
    const { notify } = useNotifications()
    const navigate = useNavigate()

    function handleLogout() {
        logout()
        notify('Sesión cerrada en este navegador.', 'info')
        navigate('/login', { replace: true })
    }

    return (
        <MainLayout
            pageTitle={title}
            breadcrumbItems={[
                { label: 'Administración' },
                { label: title }
            ]}
            user={{ ...session.user, rol: session.user.nombreRol }}
            systemName="Nuevo Avatar"
            sectionTitle="ADMINISTRACIÓN"
            footerText="Sistema administrativo"
            onLogout={handleLogout}
        >
            <section className="panel">
                <h2>{title}</h2>
                <p>{description}</p>
            </section>
        </MainLayout>
    )
}

export default function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/inicio" replace />} />
            <Route path="/login" element={<Login />} />

            <Route element={<ProtectedRoute />}>
                <Route
                    path="/inicio"
                    element={(
                        <AdminPage
                            title="Inicio"
                            description="Panel principal del sistema administrativo."
                        />
                    )}
                />
                <Route
                    path="/usuarios"
                    element={(
                        <AdminPage
                            title="Usuarios"
                            description="Espacio para integrar la gestión de usuarios."
                        />
                    )}
                />
                <Route
                    path="/modulos"
                    element={(
                        <AdminPage
                            title="Módulos"
                            description="Espacio para integrar la gestión de módulos."
                        />
                    )}
                />
                <Route
                    path="/reportes"
                    element={(
                        <AdminPage
                            title="Reportes"
                            description="Espacio para integrar los reportes del sistema."
                        />
                    )}
                />
            </Route>

            <Route path="*" element={<Navigate to="/inicio" replace />} />
        </Routes>
    )
}
