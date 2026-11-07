import { Link, Navigate, Outlet, Route, Routes } from 'react-router-dom'
import { Login } from '../../../web/jose/src/Login.jsx'
import { Notas } from '../../../web/jose/src/Notas.jsx'
import { Instituciones } from '../pages/Instituciones/Instituciones.jsx'
import { Parametros } from '../pages/Parametros/Parametros.jsx'
import { Roles } from '../pages/Roles/Roles.jsx'
import { Usuarios } from '../pages/Usuarios/Usuarios.jsx'
import MainLayout from '../layouts/MainLayout'
import { useSesion } from '../sesion/SesionContext'

const perfiles = {
    ADMINISTRADOR: { titulo: 'Inicio de administración', seccion: 'ADMINISTRACIÓN', pendiente: 'El dashboard administrativo y sus mantenimientos están pendientes de integración.' },
    PROFESOR: { titulo: 'Inicio del profesor', seccion: 'PROFESOR', pendiente: 'El dashboard del profesor está pendiente de integración.' },
    ESTUDIANTE: { titulo: 'Inicio del estudiante', seccion: 'ESTUDIANTE', pendiente: 'El inicio académico, expediente y prematrícula propios están pendientes de integración.' }
}

function Acceso() {
    const { cliente, usuario, mensaje, autenticar } = useSesion()
    if (usuario) return <Navigate to="/inicio" replace />
    return (
        <main className="acceso-sesion">
            {mensaje && <p className="aviso" role="alert">{mensaje}</p>}
            <Login cliente={cliente} onAuthenticated={autenticar} />
        </main>
    )
}

function RequiereSesion() {
    const { usuario } = useSesion()
    return usuario ? <Outlet /> : <Navigate to="/login" replace />
}

function PaginaSesion({ children, title, breadcrumbItems = [{ label: 'Inicio' }] }) {
    const { usuario, salir } = useSesion()
    const perfil = perfiles[usuario.rol]
    const menu = [{ label: 'Inicio', path: '/inicio', icon: '⌂' }]
    const notas = usuario.modulos.find(m => m.idModulo === 'ADM17')
    if (usuario.rol === 'PROFESOR' && notas) menu.push({ label: notas.nombreModulo, path: '/notas', icon: '▤' })
    const instituciones = usuario.modulos.find(m => m.idModulo === 'ADM8')
    if (usuario.rol === 'ADMINISTRADOR' && instituciones) menu.push({ label: instituciones.nombreModulo, path: '/instituciones', icon: '▦' })
    const parametros = usuario.modulos.find(m => m.idModulo === 'ADM6')
    if (usuario.rol === 'ADMINISTRADOR' && parametros) menu.push({ label: parametros.nombreModulo, path: '/parametros', icon: '▤' })
    const roles = usuario.modulos.find(m => m.idModulo === 'ADM5')
    if (usuario.rol === 'ADMINISTRADOR' && roles) menu.push({ label: roles.nombreModulo, path: '/roles', icon: '▧' })
    const usuarios = usuario.modulos.find(m => m.idModulo === 'ADM4')
    if (usuario.rol === 'ADMINISTRADOR' && usuarios) menu.push({ label: usuarios.nombreModulo, path: '/usuarios', icon: '♙' })
    return (
        <MainLayout
            pageTitle={title || perfil.titulo}
            breadcrumbItems={breadcrumbItems}
            user={usuario}
            menuItems={menu}
            sectionTitle={perfil.seccion}
            footerText="Nuevo Avatar"
            onLogout={salir}
        >
            {children}
        </MainLayout>
    )
}

function InicioSesion() {
    const { usuario, reintentar } = useSesion()
    return (
        <PaginaSesion>
            <section className="panel">
                <h2>Bienvenido, {usuario.nombre}</h2>
                <p>Tu sesión está activa como {usuario.rol}.</p>
                <p>{perfiles[usuario.rol].pendiente}</p>
                {usuario.rol === 'PROFESOR' && usuario.modulos.some(m => m.idModulo === 'ADM17') &&
                    <Link to="/notas">Abrir desglose y notas</Link>}
                <p><button type="button" onClick={reintentar}>Actualizar permisos</button></p>
            </section>
        </PaginaSesion>
    )
}

function PaginaNotas() {
    const { usuario, cliente } = useSesion()
    return (
        <PaginaSesion title="Notas" breadcrumbItems={[{ label: 'Inicio' }, { label: 'Notas' }]}>
            <Link to="/inicio">Volver al inicio</Link>
            {usuario.rol === 'PROFESOR' && usuario.modulos.some(m => m.idModulo === 'ADM17') ? (
                <div className="notas-integradas"><Notas cliente={cliente} /></div>
            ) : (
                <section className="panel" role="alert">
                    <h2>Acceso restringido</h2>
                    <p>Se requiere perfil de profesor y permiso activo para el módulo de notas.</p>
                </section>
            )}
        </PaginaSesion>
    )
}

function PaginaInstituciones() {
    const { usuario, cliente } = useSesion()
    return (
        <PaginaSesion title="Instituciones" breadcrumbItems={[{ label: 'Inicio' }, { label: 'Instituciones' }]}>
            {usuario.rol === 'ADMINISTRADOR' && usuario.modulos.some(m => m.idModulo === 'ADM8') ? (
                <Instituciones cliente={cliente} />
            ) : (
                <section className="panel" role="alert">
                    <h2>Acceso restringido</h2>
                    <p>Se requiere perfil de administrador y permiso activo para instituciones.</p>
                </section>
            )}
        </PaginaSesion>
    )
}

function PaginaParametros() {
    const { usuario, cliente } = useSesion()
    return (
        <PaginaSesion title="Parámetros" breadcrumbItems={[{ label: 'Inicio' }, { label: 'Parámetros' }]}>
            {usuario.rol === 'ADMINISTRADOR' && usuario.modulos.some(m => m.idModulo === 'ADM6') ? (
                <Parametros cliente={cliente} />
            ) : (
                <section className="panel" role="alert">
                    <h2>Acceso restringido</h2>
                    <p>Se requiere perfil de administrador y permiso activo para parámetros.</p>
                </section>
            )}
        </PaginaSesion>
    )
}

function PaginaRoles() {
    const { usuario, cliente } = useSesion()
    return (
        <PaginaSesion title="Roles y permisos" breadcrumbItems={[{ label: 'Inicio' }, { label: 'Roles y permisos' }]}>
            {usuario.rol === 'ADMINISTRADOR' && usuario.modulos.some(m => m.idModulo === 'ADM5') ? (
                <Roles cliente={cliente} />
            ) : (
                <section className="panel" role="alert">
                    <h2>Acceso restringido</h2>
                    <p>Se requiere perfil de administrador y permiso activo para roles.</p>
                </section>
            )}
        </PaginaSesion>
    )
}

function PaginaUsuarios() {
    const { usuario, cliente } = useSesion()
    return (
        <PaginaSesion title="Usuarios" breadcrumbItems={[{ label: 'Inicio' }, { label: 'Usuarios' }]}>
            {usuario.rol === 'ADMINISTRADOR' && usuario.modulos.some(m => m.idModulo === 'ADM4') ? (
                <Usuarios cliente={cliente} />
            ) : (
                <section className="panel" role="alert">
                    <h2>Acceso restringido</h2>
                    <p>Se requiere perfil de administrador y permiso activo para usuarios.</p>
                </section>
            )}
        </PaginaSesion>
    )
}

function RutaNoDisponible() {
    return (
        <PaginaSesion title="Página no disponible">
            <section className="panel">
                <h2>Esta funcionalidad aún no está integrada</h2>
                <Link to="/inicio">Volver al inicio</Link>
            </section>
        </PaginaSesion>
    )
}

export default function AppRoutes() {
    const { estado, mensaje, reintentar, salir } = useSesion()
    if (estado === 'cargando') {
        return <main className="acceso-sesion"><p role="status">Comprobando sesión…</p></main>
    }
    if (estado === 'error') {
        return (
            <main className="acceso-sesion panel">
                <h1>No se pudo comprobar tu sesión</h1>
                <p role="alert">{mensaje}</p>
                <button type="button" onClick={reintentar}>Reintentar</button>
                <button type="button" onClick={salir}>Cerrar sesión</button>
            </main>
        )
    }
    return (
        <Routes>
            <Route path="/login" element={<Acceso />} />
            <Route element={<RequiereSesion />}>
                <Route path="/" element={<Navigate to="/inicio" replace />} />
                <Route path="/inicio" element={<InicioSesion />} />
                <Route path="/notas" element={<PaginaNotas />} />
                <Route path="/instituciones" element={<PaginaInstituciones />} />
                <Route path="/parametros" element={<PaginaParametros />} />
                <Route path="/roles" element={<PaginaRoles />} />
                <Route path="/usuarios" element={<PaginaUsuarios />} />
                <Route path="*" element={<RutaNoDisponible />} />
            </Route>
        </Routes>
    )
}
