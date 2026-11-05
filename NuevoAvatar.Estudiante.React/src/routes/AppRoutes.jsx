
import { Navigate, Route, Routes } from 'react-router-dom'
import MainLayout from '../layouts/MainLayout'

const demoUser = {
    nombre: 'Usuario de prueba',
    rol: 'Administrador'
}

const adminMenu = [
    { label: 'Inicio', path: '/inicio', icon: '⌂' },
    { label: 'Usuarios', path: '/usuarios', icon: '♙' },
    { label: 'Módulos', path: '/modulos', icon: '▦' },
    { label: 'Reportes', path: '/reportes', icon: '▤' }
]

function AdminPage({ title, description }) {
    return (
        <MainLayout
            pageTitle={title}
            breadcrumbItems={[
                { label: 'Administración' },
                { label: title }
            ]}
            user={demoUser}
            menuItems={adminMenu}
            systemName="Nuevo Avatar"
            sectionTitle="ADMINISTRACIÓN"
            footerText="Sistema administrativo"
        >
            <div className="page-head">
                <div>
                    <span className="section-label">ADMINISTRACIÓN</span>
                    <h2>{title}</h2>
                    <p className="subtitle">{description}</p>
                </div>
            </div>

            <div className="panel">
                <h3>{title}</h3>
                <p className="subtitle">
                    Esta pantalla es un espacio de demostración para integrar
                    el módulo correspondiente.
                </p>
            </div>
        </MainLayout>
    )
}

function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/inicio" replace />} />

            <Route
                path="/inicio"
                element={
                    <AdminPage
                        title="Inicio"
                        description="Panel principal del sistema administrativo."
                    />
                }
            />

            <Route
                path="/usuarios"
                element={
                    <AdminPage
                        title="Usuarios"
                        description="Espacio para integrar la gestión de usuarios."
                    />
                }
            />

            <Route
                path="/modulos"
                element={
                    <AdminPage
                        title="Módulos"
                        description="Espacio para integrar la gestión de módulos."
                    />
                }
            />

            <Route
                path="/reportes"
                element={
                    <AdminPage
                        title="Reportes"
                        description="Espacio para integrar los reportes del sistema."
                    />
                }
            />

            <Route path="*" element={<Navigate to="/inicio" replace />} />
        </Routes>
    )
}

export default AppRoutes
