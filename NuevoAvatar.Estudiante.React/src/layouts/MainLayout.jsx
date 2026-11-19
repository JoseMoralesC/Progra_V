
import { useEffect, useState } from 'react'

import Sidebar from '../components/Sidebar/Sidebar'
import Header from '../components/Header/Header'
import Footer from '../components/Footer/Footer'

function MainLayout({
    children,
    pageTitle,
    breadcrumbItems = [],
    user = null,
    permissions = null,
    menuItems,
    systemName = 'Nuevo Avatar',
    sectionTitle = 'ADMINISTRACIÓN',
    footerText = 'Sistema administrativo',
    mostrarFooter = true,
    onLogout
}) {
    const [collapsed, setCollapsed] = useState(false)
    const [mobileExpanded, setMobileExpanded] = useState(false)
    const [mobile, setMobile] = useState(() =>
        typeof window !== 'undefined' && window.matchMedia('(max-width: 800px)').matches)

    useEffect(() => {
        const media = window.matchMedia('(max-width: 800px)')
        const actualizar = evento => {
            setMobile(evento.matches)
            setMobileExpanded(false)
        }
        setMobile(media.matches)
        media.addEventListener('change', actualizar)
        return () => media.removeEventListener('change', actualizar)
    }, [])

    const [dark, setDark] = useState(() => {
        try {
            return localStorage.getItem('avatar-theme') === 'dark'
        } catch {
            return false
        }
    })

    useEffect(() => {
        document.body.classList.toggle('dark', dark)

        try {
            localStorage.setItem('avatar-theme', dark ? 'dark' : 'light')
        } catch {
            // El tema funciona aunque el almacenamiento no esté disponible.
        }
    }, [dark])

    const menuCollapsed = mobile ? !mobileExpanded : collapsed

    return (
        <div className={`stage ${menuCollapsed ? 'sidebar-collapsed' : ''}`}>
            <Sidebar
                collapsed={menuCollapsed}
                onToggle={() => {
                    if (mobile) setMobileExpanded(current => !current)
                    else setCollapsed(current => !current)
                }}
                onNavigate={() => { if (mobile) setMobileExpanded(false) }}
                permissions={permissions}
                menuItems={menuItems}
                systemName={systemName}
                sectionTitle={sectionTitle}
                footerText={footerText}
            />

            <main className="content">
                <Header
                    pageTitle={pageTitle}
                    breadcrumbItems={breadcrumbItems}
                    user={user}
                    dark={dark}
                    onThemeToggle={() => setDark(current => !current)}
                    onLogout={onLogout}
                />

                <section className="page-content">
                    {children}
                </section>

                {mostrarFooter && <Footer systemName={`${systemName} V2`} text={footerText} />}
            </main>
        </div>
    )
}

export default MainLayout
