
import Breadcrumb from '../Breadcrumb/Breadcrumb'

function Header({
    pageTitle,
    breadcrumbItems = [],
    user = null,
    dark,
    onThemeToggle,
    onLogout
}) {
    const displayName = user?.nombre || 'Usuario'
    const displayRole = user?.rol || 'Sin rol asignado'

    const initials = displayName
        .split(' ')
        .filter(Boolean)
        .slice(0, 2)
        .map(word => word.charAt(0).toUpperCase())
        .join('')

    return (
        <header className="topbar">
            <div className="header-heading">
                <Breadcrumb items={breadcrumbItems} />
                <h1 className="header-page-title">{pageTitle}</h1>
            </div>

            <div className="top-right">
                <button
                    className="round-btn"
                    type="button"
                    onClick={onThemeToggle}
                    title={dark ? 'Activar tema claro' : 'Activar tema oscuro'}
                    aria-label={dark ? 'Activar tema claro' : 'Activar tema oscuro'}
                    aria-pressed={dark}
                >
                    {dark ? '☀' : '☾'}
                </button>

                <div className="user">
                    <div className="avatar" aria-hidden="true">
                        {initials || 'US'}
                    </div>

                    <div className="user-info">
                        <div className="user-name">{displayName}</div>
                        <div className="user-role">{displayRole}</div>
                    </div>
                </div>

                <button
                    className="logout-btn"
                    type="button"
                    onClick={onLogout}
                    disabled={!onLogout}
                    title={onLogout ? 'Cerrar sesión' : 'Pendiente de integración'}
                >
                    Salir
                </button>
            </div>
        </header>
    )
}

export default Header
