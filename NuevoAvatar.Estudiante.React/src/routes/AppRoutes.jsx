import { useEffect, useMemo, useState } from 'react'
import { Link, Navigate, Outlet, Route, Routes, useNavigate } from 'react-router-dom'
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
import { crearClienteAutenticado, obtenerMenu } from '../services/api'
import { esEstudiante } from '../navigation/studentNavigation'
import { Notas } from '../../../web/jose/src/Notas.jsx'
import { Modulos } from '../../../web/ramses/src/Modulos.jsx'
import { Carreras } from '../../../web/ramses/src/Carreras.jsx'
import { Cursos } from '../../../web/ramses/src/Cursos.jsx'
import { Profesores } from '../../../web/ramses/src/Profesores.jsx'
import { Periodos } from '../../../web/ramses/src/Periodos.jsx'
import { Notificaciones } from '../../../web/ramses/src/Notificaciones.jsx'
import '../styles/mantenimientos.css'

const adminMenu = [
    { idModulo: 'ADM8', label: 'Instituciones', path: '/instituciones', icon: '▦' },
    { idModulo: 'ADM6', label: 'Parámetros', path: '/parametros', icon: '▤' },
    { idModulo: 'ADM5', label: 'Roles', path: '/roles', icon: '▧' },
    { idModulo: 'ADM4', label: 'Usuarios', path: '/usuarios', icon: '♙' },
    { idModulo: 'ADM7', label: 'Módulos', path: '/modulos', icon: '▦' },
    { idModulo: 'ADM9', label: 'Carreras', path: '/carreras', icon: '▤' },
    { idModulo: 'ADM10', label: 'Cursos', path: '/cursos', icon: '▤' },
    { idModulo: 'ADM11', label: 'Profesores', path: '/profesores', icon: '♙' },
    { idModulo: 'ADM12', label: 'Periodos', path: '/periodos', icon: '▤' },
    { idModulo: 'ADM22', label: 'Notificaciones', path: '/notificaciones', icon: '▤' },
    { idModulo: 'ADM17', label: 'Notas', path: '/notas', icon: '▤' }
]

function roleOf(user) {
    return String(user?.nombreRol || user?.idRol || '').trim().toLocaleUpperCase('es')
}

function AdminPage({ title, children, modulo, perfil }) {
    const { session, logout, authorizedFetch } = useAuth()
    const navigate = useNavigate()
    const [modulos, setModulos] = useState(session?.user?.modulos || [])
    const [errorMenu, setErrorMenu] = useState('')
    const cliente = useMemo(() => crearClienteAutenticado(authorizedFetch), [authorizedFetch])
    const user = session?.user || {}
    const role = roleOf(user)
    useEffect(() => {
        let vigente = true
        authorizedFetch(token => obtenerMenu(token))
            .then(menu => { if (vigente && Array.isArray(menu)) {
                const activos = menu.filter(item => item?.activo !== false)
                setModulos(actuales => JSON.stringify(actuales) === JSON.stringify(activos) ? actuales : activos)
                setErrorMenu('')
            } })
            .catch(error => { if (vigente) { setModulos([]); setErrorMenu(error.message) } })
        return () => { vigente = false }
    }, [authorizedFetch])
    const dashboardUser = useMemo(() => ({
        ...user,
        usuarioID: user.usuarioID || user.email || session?.email || '',
        rol: role,
        modulos
    }), [user, session?.email, role, modulos])

    function handleLogout() {
        logout()
        navigate('/login', { replace: true })
    }

    return (
        <MainLayout
            pageTitle={title}
            breadcrumbItems={[{ label: 'Inicio', path: '/inicio' }, { label: title }]}
            user={{ ...user, rol: user.nombreRol || user.idRol }}
            menuItems={[{ label: 'Inicio', path: '/inicio', icon: '⌂' },
                ...adminMenu.filter(item => (item.idModulo === 'ADM17' ? role === 'PROFESOR' : role === 'ADMINISTRADOR') &&
                    modulos.some(modulo => modulo.idModulo === item.idModulo))
                    .map(item => ({ ...item, label: modulos.find(m => m.idModulo === item.idModulo)?.nombreModulo || item.label }))
                    .sort((a, b) => (modulos.find(m => m.idModulo === a.idModulo)?.orden ?? 0) - (modulos.find(m => m.idModulo === b.idModulo)?.orden ?? 0))]}
            systemName="Nuevo Avatar"
            sectionTitle="ADMINISTRACIÓN"
            footerText="Sistema administrativo"
            onLogout={handleLogout}
        >
            {errorMenu ? <section className="panel" role="alert"><h2>No se pudieron comprobar los permisos</h2><p>{errorMenu}</p><Link to="/inicio">Volver al inicio</Link></section>
                : modulo && (role !== perfil || !modulos.some(m => m.idModulo === modulo))
                    ? <section className="panel"><h2>Acceso restringido</h2><p>No tienes permiso para esta pantalla.</p><Link to="/inicio">Volver al inicio</Link></section>
                    : <>{modulo && <Link to="/inicio">Volver al inicio</Link>}
                        <div className={modulo === 'ADM17' ? 'notas-integradas' : modulo ? 'ramses-integradas' : undefined}>
                            {children({ cliente, usuario: dashboardUser })}
                        </div></>}
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
                    <Route path="/notas" element={<AdminPage title="Notas" modulo="ADM17" perfil="PROFESOR">{({ cliente }) => <Notas cliente={cliente} />}</AdminPage>} />
                    <Route path="/perfil" element={<AdminPage title="Perfil">{() => <Perfil />}</AdminPage>} />
                    <Route path="/modulos" element={<AdminPage title="Módulos" modulo="ADM7" perfil="ADMINISTRADOR">{({ cliente }) => <Modulos cliente={cliente} />}</AdminPage>} />
                    <Route path="/carreras" element={<AdminPage title="Carreras" modulo="ADM9" perfil="ADMINISTRADOR">{({ cliente }) => <Carreras cliente={cliente} />}</AdminPage>} />
                    <Route path="/cursos" element={<AdminPage title="Cursos" modulo="ADM10" perfil="ADMINISTRADOR">{({ cliente }) => <Cursos cliente={cliente} />}</AdminPage>} />
                    <Route path="/profesores" element={<AdminPage title="Profesores" modulo="ADM11" perfil="ADMINISTRADOR">{({ cliente }) => <Profesores cliente={cliente} />}</AdminPage>} />
                    <Route path="/periodos" element={<AdminPage title="Periodos" modulo="ADM12" perfil="ADMINISTRADOR">{({ cliente }) => <Periodos cliente={cliente} />}</AdminPage>} />
                    <Route path="/notificaciones" element={<AdminPage title="Notificaciones" modulo="ADM22" perfil="ADMINISTRADOR">{({ cliente }) => <Notificaciones cliente={cliente} />}</AdminPage>} />
                </Route>
            </Route>

            <Route path="*" element={<Navigate to={home} replace />} />
        </Routes>
    )
}
