import { useAuth } from '../../context/AuthContext'
import { Link } from 'react-router-dom'

function EstadoNoDisponible({ children }) {
    return (
        <article className="card dashboard-card" aria-label={children}>
            <div className="dashboard-card-top">
                <span className="dashboard-icon" aria-hidden="true">◷</span>
                <span className="card-title">{children}</span>
            </div>
            <div className="card-value">No consultable</div>
            <p className="card-description">
                El Gateway aún no ofrece una operación estudiantil segura para consultar estos datos.
            </p>
            <button className="btn btn-soft" type="button" disabled>
                Funcionalidad no disponible
            </button>
        </article>
    )
}

export default function InicioEstudiante() {
    const { session } = useAuth()
    const user = session.user
    const role = user.nombreRol || user.idRol || 'Estudiante'

    return (
        <>
            <section className="welcome-panel" aria-labelledby="student-welcome-title">
                <div className="welcome-content">
                    <span className="eyebrow">PORTAL ESTUDIANTIL</span>
                    <h2 id="student-welcome-title">Hola, {user.nombre || user.email}</h2>
                    <p>Sesión autenticada como {role}.</p>
                </div>
                <div className="welcome-decoration" aria-hidden="true">AV</div>
            </section>

            <section className="section-heading" aria-labelledby="student-summary-title">
                <div>
                    <h2 id="student-summary-title">Mis gestiones</h2>
                    <p className="subtitle">
                        El estado se mostrará cuando estén disponibles las consultas propias autorizadas.
                    </p>
                </div>
            </section>

            <section className="card-grid dashboard-grid" aria-label="Estado de mis gestiones">
                <article className="card dashboard-card" aria-label="Mi expediente">
                    <div className="dashboard-card-top">
                        <span className="dashboard-icon" aria-hidden="true">▤</span>
                        <span className="card-title">Mi expediente</span>
                    </div>
                    <div className="card-value">Consulta y actualización</div>
                    <p className="card-description">Revisa tus datos y actualiza tus teléfonos y dirección.</p>
                    <Link className="text-link" to="/estudiante/expediente">
                        Abrir mi expediente <span aria-hidden="true">→</span>
                    </Link>
                </article>
                <EstadoNoDisponible>Mis prematrículas</EstadoNoDisponible>
            </section>

            <section className="panel" role="note">
                <h2>Otras consultas estudiantiles</h2>
                <p>
                    Las consultas de prematrícula se habilitarán cuando exista una operación segura vinculada a tu sesión.
                </p>
            </section>
        </>
    )
}
