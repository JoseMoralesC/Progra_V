
import { NavLink } from 'react-router-dom'

const defaultMenuItems = [
    { label: 'Inicio', path: '/inicio', icon: '⌂' },
    { label: 'Usuarios', path: '/usuarios', icon: '♙' },
    { label: 'Módulos', path: '/modulos', icon: '▦' },
    { label: 'Reportes', path: '/reportes', icon: '▤' }
]

function Sidebar({
    collapsed,
    onToggle,
    permissions = null,
    menuItems = defaultMenuItems,
    systemName = 'Nuevo Avatar',
    sectionTitle = 'ADMINISTRACIÓN',
    footerText = 'Sistema administrativo'
}) {
    const visibleItems = menuItems.filter(item => {
        if (!item.permission || permissions === null) return true
        return permissions.includes(item.permission)
    })

    return (
        <aside className="menu glass" aria-label="Menú principal">
            <div className="menu-head">
                <span className="head-title">{systemName}</span>

                <button
                    className="head-toggle"
                    type="button"
                    onClick={onToggle}
                    title={collapsed ? 'Expandir menú' : 'Contraer menú'}
                    aria-label={collapsed ? 'Expandir menú' : 'Contraer menú'}
                    aria-expanded={!collapsed}
                >
                    {collapsed ? '›' : '‹'}
                </button>
            </div>

            <div className="section-label">{sectionTitle}</div>

            <nav aria-label="Navegación principal">
                {visibleItems.map(item => (
                    <NavLink
                        key={item.path}
                        to={item.path}
                        end={item.path === '/inicio'}
                        title={collapsed ? item.label : undefined}
                        aria-label={item.label}
                        className={({ isActive }) =>
                            `menu-item ${isActive ? 'active' : ''}`
                        }
                    >
                        <span className="mi" aria-hidden="true">
                            {item.icon}
                        </span>

                        <span className="label">{item.label}</span>
                    </NavLink>
                ))}
            </nav>

            <div className="menu-divider" />

            <div className="menu-footer">
                <span className="footer-brand">{systemName}</span>
                <span>{footerText}</span>
            </div>
        </aside>
    )
}

export default Sidebar
