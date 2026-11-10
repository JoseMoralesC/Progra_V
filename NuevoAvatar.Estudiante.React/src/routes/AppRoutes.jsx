import { useEffect, useMemo, useState } from 'react'
import { Navigate, Outlet, Route, Routes, useNavigate } from 'react-router-dom'
import MainLayout from '../layouts/MainLayout'
import StudentLayout from '../layouts/StudentLayout'
import Login from '../pages/Login/Login'
import ProtectedRoute from '../components/ProtectedRoute'
import { Dashboard } from '../pages/Dashboard/Dashboard.jsx'
import { Instituciones } from '../pages/Instituciones/Instituciones.jsx'
import { Parametros } from '../pages/Parametros/Parametros.jsx'
import { Roles } from '../pages/Roles/Roles.jsx'
import { Usuarios } from '../pages/Usuarios/Usuarios.jsx'
import InicioEstudiante from '../pages/Estudiante/InicioEstudiante'
import Expediente from '../pages/Estudiante/Expediente'
import Prematricula from '../pages/Estudiante/Prematricula'
import Perfil from '../pages/Perfil/Perfil'
import { useAuth } from '../context/AuthContext'
import { useNotifications } from '../context/NotificationContext'
import { crearClienteAutenticado, obtenerMenu } from '../services/api'
import { esEstudiante } from '../navigation/studentNavigation'

const adminMenu = [
    { idModulo: 'ADM8', label: 'Instituciones', path: '/instituciones', icon: '▦' },
    { idModulo: 'ADM6', label: 'Parámetros', path: '/parametros', icon: '▤' },
    { idModulo: 'ADM5', label: 'Roles', path: '/roles', icon: '▧' },
    { idModulo: 'ADM4', label: 'Usuarios', path: '/usuarios', icon: '♙' },
    { idModulo: 'ADM7', label: 'Módulos', path: '/modulos', icon: '▦' },
    { idModulo: 'ADM23', label: 'Reportes', path: '/reportes', icon: '▤' }
]

function roleOf(user) {
    return String(user?.nombreRol || user?.idRol || '').trim().toLocaleUpperCase('es')
}

function AdminPage({ title, children }) {
    const { session, logout, authorizedFetch } = useAuth()
    const { notify } = useNotifications()
    const navigate = useNavigate()
    const [modulos, setModulos] = useState([])
    const cliente = useMemo(() => crearClienteAutenticado(authorizedFetch), [authorizedFetch])
    const user = session?.user || {}
    const role = roleOf(user)
    useEffect(() => {
        let vigente = true
        authorizedFetch(token => obtenerMenu(token))
            .then(menu => { if (vigente && Array.isArray(menu)) setModulos(menu.filter(item => item?.activo !== false)) })
            .catch(() => { if (vigente) setModulos([]) })
        return () => { vigente = false }
    }, [authorizedFetch])
    const dashboardUser = {
        ...user,
        usuarioID: user.usuarioID || user.email || session?.email || '',
        rol: role,
        modulos: Array.isArray(user.modulos) && user.modulos.length ? user.modulos : modulos
    }

    function handleLogout() {
        logout()
        notify('Sesión cerrada en este navegador.', 'info')
        navigate('/login', { replace: true })
    }

    return (
        <MainLayout
            pageTitle={title}
            breadcrumbItems={[{ label: 'Administración' }, { label: title }]}
            user={{ ...user, rol: user.nombreRol || user.idRol }}
            menuItems={[{ label: 'Inicio', path: '/inicio', icon: '⌂' },
                ...adminMenu.filter(item => modulos.some(modulo => modulo.idModulo === item.idModulo))]}
            systemName="Nuevo Avatar"
            sectionTitle="ADMINISTRACIÓN"
            footerText="Sistema administrativo"
            onLogout={handleLogout}
        >
            {children({ cliente, usuario: dashboardUser })}
        </MainLayout>
    )
}

function AdminRoleGate() {
    const { session } = useAuth()
    if (esEstudiante(session?.user)) return <Navigate to="/estudiante" replace />
    const role = roleOf(session?.user)
    if (!['ADMINISTRADOR', 'PROFESOR'].includes(role)) return <Navigate to="/login" replace />
    return <Outlet />
}

function AdminPlaceholder({ title, description }) {
    return <section className="panel"><h2>{title}</h2><p>{description}</p></section>
}

function DashboardRoute() {
    const { session } = useAuth()
    if (esEstudiante(session?.user)) return <Navigate to="/estudiante" replace />
    return <AdminPage title="Inicio">{({ cliente, usuario }) => <Dashboard cliente={cliente} usuario={usuario} />}</AdminPage>
}

export default function AppRoutes() {
    const { session } = useAuth()
    const home = esEstudiante(session?.user) ? '/estudiante' : '/inicio'

    return (
        <Routes>
            <Route path="/" element={<Navigate to={home} replace />} />
            <Route path="/login" element={<Login />} />

            <Route element={<ProtectedRoute />}>
                <Route path="/estudiante" element={<StudentLayout />}>
                    <Route index element={<InicioEstudiante />} />
                    <Route path="expediente" element={<Expediente />} />
                    <Route path="prematricula" element={<Prematricula />} />
                    <Route path="perfil" element={<Perfil />} />
                </Route>

                <Route element={<AdminRoleGate />}>
                    <Route path="/inicio" element={<DashboardRoute />} />
                    <Route path="/instituciones" element={<AdminPage title="Instituciones">{({ cliente }) => <Instituciones cliente={cliente} />}</AdminPage>} />
                    <Route path="/parametros" element={<AdminPage title="Parámetros">{({ cliente }) => <Parametros cliente={cliente} />}</AdminPage>} />
                    <Route path="/roles" element={<AdminPage title="Roles y permisos">{({ cliente }) => <Roles cliente={cliente} />}</AdminPage>} />
                    <Route path="/usuarios" element={<AdminPage title="Usuarios">{({ cliente }) => <Usuarios cliente={cliente} />}</AdminPage>} />
                    <Route path="/notas" element={<AdminPage title="Notas">{() => <AdminPlaceholder title="Notas" description="La pantalla de notas requiere integrar su servicio con la sesión autenticada actual." />}</AdminPage>} />
                    <Route path="/perfil" element={<AdminPage title="Perfil">{() => <Perfil />}</AdminPage>} />
                    <Route path="/modulos" element={<AdminPage title="Módulos">{() => <AdminPlaceholder title="Módulos" description="Espacio para integrar la gestión de módulos." />}</AdminPage>} />
                    <Route path="/reportes" element={<AdminPage title="Reportes">{() => <AdminPlaceholder title="Reportes" description="Espacio para integrar los reportes del sistema." />}</AdminPage>} />
                </Route>
            </Route>

            <Route path="*" element={<Navigate to={home} replace />} />
        </Routes>
    )
}
