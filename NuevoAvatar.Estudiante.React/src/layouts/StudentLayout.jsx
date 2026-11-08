import { Navigate, Outlet, useLocation, useNavigate } from 'react-router-dom'
import MainLayout from './MainLayout'
import { useAuth } from '../context/AuthContext'
import { useNotifications } from '../context/NotificationContext'
import { esEstudiante } from '../navigation/studentNavigation'

export default function StudentLayout() {
    const { session, logout } = useAuth()
    const { notify } = useNotifications()
    const location = useLocation()
    const navigate = useNavigate()

    if (!esEstudiante(session?.user)) return <Navigate to="/inicio" replace />

    function handleLogout() {
        logout()
        notify('Sesión cerrada en este navegador.', 'info')
        navigate('/login', { replace: true })
    }

    const pageTitle = location.pathname === '/estudiante/expediente' ? 'Mi expediente'
        : location.pathname === '/estudiante/perfil' ? 'Mi perfil' : 'Inicio del estudiante'
    const breadcrumbItems = pageTitle === 'Inicio del estudiante'
        ? [{ label: 'Inicio' }]
        : [{ label: 'Inicio', path: '/estudiante' }, { label: pageTitle }]

    return (
        <MainLayout
            pageTitle={pageTitle}
            breadcrumbItems={breadcrumbItems}
            user={{ ...session.user, rol: session.user.nombreRol || session.user.idRol }}
            menuItems={[
                { id: 'portal-inicio', label: 'Inicio', path: '/estudiante', icon: '⌂' },
                { id: 'portal-expediente', label: 'Mi expediente', path: '/estudiante/expediente', icon: '▤' },
                { id: 'portal-perfil', label: 'Mi perfil', path: '/estudiante/perfil', icon: '◉' }
            ]}
            menuStatus="ready"
            systemName="Nuevo Avatar"
            sectionTitle="PORTAL ESTUDIANTIL"
            footerText="Portal estudiantil"
            onLogout={handleLogout}
        >
            <Outlet />
        </MainLayout>
    )
}
