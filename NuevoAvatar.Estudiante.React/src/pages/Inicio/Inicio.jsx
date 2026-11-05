
import { Link } from 'react-router-dom'
import { useNotifications } from '../../context/NotificationContext'

function Inicio() {
    const { notify } = useNotifications()

    return (
        <>
            <section className="welcome-panel">
                <div className="welcome-content">
                    <span className="eyebrow">PORTAL ESTUDIANTIL</span>
                    <h1>¡Bienvenido!</h1>
                    <p>
                        Consulta tu información académica y gestiona tus procesos
                        estudiantiles desde un solo lugar.
                    </p>
                </div>

                <div className="welcome-decoration" aria-hidden="true">
                    AV
                </div>
            </section>

            <section className="section-heading">
                <div>
                    <h2>Resumen académico</h2>
                    <p className="subtitle">
                        Una vista general de tu información estudiantil.
                    </p>
                </div>
            </section>

            <section className="card-grid dashboard-grid">
                <article className="card dashboard-card">
                    <div className="dashboard-card-top">
                        <span className="dashboard-icon">▤</span>
                        <span className="card-title">Expediente</span>
                    </div>
                    <div className="card-value">Mi información</div>
                    <p className="card-description">
                        Consulta tus datos personales y académicos.
                    </p>
                    <Link to="/expediente" className="text-link">
                        Consultar expediente <span>→</span>
                    </Link>
                </article>

                <article className="card dashboard-card">
                    <div className="dashboard-card-top">
                        <span className="dashboard-icon">▦</span>
                        <span className="card-title">Prematrícula</span>
                    </div>
                    <div className="card-value">Proceso académico</div>
                    <p className="card-description">
                        Consulta los detalles de tu prematrícula.
                    </p>
                    <Link to="/prematricula" className="text-link">
                        Ver prematrícula <span>→</span>
                    </Link>
                </article>

                <article className="card dashboard-card">
                    <div className="dashboard-card-top">
                        <span className="dashboard-icon">◷</span>
                        <span className="card-title">Estado</span>
                    </div>
                    <div className="card-value">Portal disponible</div>
                    <p className="card-description">
                        Accede a las funcionalidades del portal estudiantil.
                    </p>
                    <span className="status status-success">● Disponible</span>
                </article>
            </section>

            <section className="section-heading">
                <div>
                    <h2>Accesos rápidos</h2>
                    <p className="subtitle">
                        Continúa con tus gestiones académicas.
                    </p>
                </div>
            </section>

            <section className="quick-access-grid">
                <Link to="/expediente" className="quick-access-card">
                    <div className="quick-icon">♙</div>
                    <div>
                        <h3>Mi expediente</h3>
                        <p>Revisa tus datos personales y académicos.</p>
                    </div>
                    <span className="quick-arrow">→</span>
                </Link>

                <Link to="/prematricula" className="quick-access-card">
                    <div className="quick-icon">▦</div>
                    <div>
                        <h3>Mi prematrícula</h3>
                        <p>Consulta el detalle de tu proceso.</p>
                    </div>
                    <span className="quick-arrow">→</span>
                </Link>
            </section>

            <section className="panel activity-panel">
                <div className="section-heading">
                    <div>
                        <h2>Actividad reciente</h2>
                        <p className="subtitle">
                            Información de tus gestiones.
                        </p>
                    </div>
                </div>

                <div className="empty">
                    <div className="empty-icon">◷</div>
                    <strong>Sin actividad reciente</strong>
                    <p>
                        Cuando existan movimientos académicos, aparecerán aquí.
                    </p>
                </div>
            </section>

            {/* SECCION TEMPORAL PARA PRUEBAS DE ADM1 */}
            <section className="panel notification-test-panel">
                <h2>Pruebas de notificaciones</h2>
                <p className="subtitle">
                    Herramientas temporales para validar el sistema de notificaciones.
                </p>

                <div className="notification-test-actions">
                    <button
                        type="button"
                        onClick={() => notify(
                            'La operación se realizó correctamente.',
                            'success'
                        )}
                    >
                        Probar éxito
                    </button>

                    <button
                        type="button"
                        onClick={() => notify(
                            'No fue posible completar la operación.',
                            'error'
                        )}
                    >
                        Probar error
                    </button>

                    <button
                        type="button"
                        onClick={() => notify(
                            'Hay información que requiere tu atención.',
                            'warning'
                        )}
                    >
                        Probar advertencia
                    </button>

                    <button
                        type="button"
                        onClick={() => notify(
                            'Tienes una nueva notificación informativa.',
                            'info'
                        )}
                    >
                        Probar información
                    </button>
                </div>
            </section>
        </>
    )
}

export default Inicio
