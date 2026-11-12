
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
    onLogout
}) {
    const [collapsed, setCollapsed] = useState(false)
    const [mobile, setMobile] = useState(() =>
        typeof window !== 'undefined' && window.matchMedia('(max-width: 600px)').matches)

    useEffect(() => {
        const media = window.matchMedia('(max-width: 600px)')
        const actualizar = evento => setMobile(evento.matches)
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

    return (
        <div className={`stage ${mobile || collapsed ? 'sidebar-collapsed' : ''}`}>
            <Sidebar
                collapsed={mobile || collapsed}
                onToggle={() => { if (!mobile) setCollapsed(current => !current) }}
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

                <Footer
                    systemName={`${systemName} V2`}
                    text={footerText}
                />
            </main>
        </div>
    )
}

export default MainLayout
