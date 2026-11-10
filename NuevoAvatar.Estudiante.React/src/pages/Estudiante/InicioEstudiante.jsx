import { useCallback, useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { obtenerExpedientePropio, obtenerPrematriculasPropias } from '../../services/api'
import { ADMINISTRATIVE_CONTACT_EMAIL } from '../../config/administrativeContact'

const estadoInicial = { status: 'loading', error: null }

function mensajeError(error) {
    if (error?.name === 'SessionExpiredError') {
        return 'La sesión expiró. Inicia sesión nuevamente para consultar tus gestiones.'
    }
    if (error?.status === 403) return 'Tu sesión no tiene permiso para consultar esta información.'
    if (error?.status === 0 || error?.status >= 500) {
        return 'El servicio no está disponible en este momento. Intenta nuevamente más tarde.'
    }
    return 'No fue posible consultar esta información. Intenta nuevamente.'
}

function EstadoCarga({ children }) {
    return <span className="status status-warning" role="status">{children}</span>
}

function EstadoError({ children }) {
    return <span className="status status-danger" role="alert">{children}</span>
}

export default function InicioEstudiante() {
    const { session, authorizedFetch } = useAuth()
    const user = session?.user || {}
    const role = user.nombreRol || user.idRol || 'Estudiante'
    const [expediente, setExpediente] = useState(estadoInicial)
    const [prematriculas, setPrematriculas] = useState(estadoInicial)
    const mounted = useRef(false)
    const requestIds = useRef({ expediente: 0, prematriculas: 0 })

    useEffect(() => {
        mounted.current = true
        return () => {
            mounted.current = false
            requestIds.current.expediente += 1
            requestIds.current.prematriculas += 1
        }
    }, [])

    const cargarExpediente = useCallback(async () => {
        const id = ++requestIds.current.expediente
        setExpediente(estadoInicial)
        try {
            await authorizedFetch(obtenerExpedientePropio)
            if (mounted.current && id === requestIds.current.expediente) {
                setExpediente({ status: 'available', error: null })
            }
        } catch (error) {
            if (!mounted.current || id !== requestIds.current.expediente) return
            if (error?.status === 404) {
                setExpediente({ status: 'missing', error: null })
            } else if (error?.status === 409) {
                setExpediente({ status: 'inconsistent', error: null })
            } else {
                setExpediente({ status: 'error', error: mensajeError(error) })
            }
        }
    }, [authorizedFetch])

    const cargarPrematriculas = useCallback(async () => {
        const id = ++requestIds.current.prematriculas
        setPrematriculas(estadoInicial)
        try {
            const datos = await authorizedFetch(obtenerPrematriculasPropias)
            if (!Array.isArray(datos)) throw new Error('Respuesta de prematrículas inválida.')
            if (mounted.current && id === requestIds.current.prematriculas) {
                setPrematriculas({ status: 'ready', count: datos.length, error: null })
            }
        } catch (error) {
            if (mounted.current && id === requestIds.current.prematriculas) {
                setPrematriculas({ status: 'error', error: mensajeError(error) })
            }
        }
    }, [authorizedFetch])

    const cargarResumen = useCallback(() => {
        cargarExpediente()
        cargarPrematriculas()
    }, [cargarExpediente, cargarPrematriculas])

    useEffect(() => {
        cargarResumen()
    }, [cargarResumen])

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
                    <p className="subtitle">Estado de tus trámites asociado a la sesión autenticada.</p>
                </div>
            </section>

            <section className="card-grid dashboard-grid" aria-label="Estado de mis gestiones">
                <article className="card dashboard-card" aria-label="Mi expediente" aria-busy={expediente.status === 'loading'}>
                    <div className="dashboard-card-top">
                        <span className="dashboard-icon" aria-hidden="true">▤</span>
                        <span className="card-title">Mi expediente</span>
                    </div>
                    {expediente.status === 'loading' && <><div className="card-value">Consultando</div><EstadoCarga>Cargando estado…</EstadoCarga></>}
                    {expediente.status === 'available' && <><div className="card-value">Disponible</div><span className="status status-success">Expediente vinculado</span></>}
                    {expediente.status === 'missing' && (
                        <>
                            <div className="card-value">Sin vínculo</div>
                            <p className="card-description">No encontramos un expediente asociado a tu sesión. Contacta al área administrativa para revisar el vínculo.</p>
                            {ADMINISTRATIVE_CONTACT_EMAIL && <a className="text-link" href={`mailto:${ADMINISTRATIVE_CONTACT_EMAIL}`}>Contactar al área administrativa</a>}
                        </>
                    )}
                    {expediente.status === 'inconsistent' && <><div className="card-value">Requiere revisión</div><p className="card-description">No fue posible confirmar un vínculo único con tu expediente. Contacta al área administrativa.</p>{ADMINISTRATIVE_CONTACT_EMAIL && <a className="text-link" href={`mailto:${ADMINISTRATIVE_CONTACT_EMAIL}`}>Contactar al área administrativa</a>}</>}
                    {expediente.status === 'error' && <><div className="card-value">No disponible</div><EstadoError>{expediente.error}</EstadoError><button className="btn btn-soft" type="button" onClick={cargarExpediente}>Reintentar</button></>}
                    <p className="card-description">Consulta tus datos y actualiza tus teléfonos y dirección.</p>
                    <Link className="text-link" to="/estudiante/expediente">
                        Abrir mi expediente <span aria-hidden="true">→</span>
                    </Link>
                </article>

                <article className="card dashboard-card" aria-label="Mis prematrículas" aria-busy={prematriculas.status === 'loading'}>
                    <div className="dashboard-card-top">
                        <span className="dashboard-icon" aria-hidden="true">▧</span>
                        <span className="card-title">Mis prematrículas</span>
                    </div>
                    {prematriculas.status === 'loading' && <><div className="card-value">Consultando</div><EstadoCarga>Cargando solicitudes…</EstadoCarga></>}
                    {prematriculas.status === 'ready' && <>
                        <div className="card-value">{prematriculas.count} {prematriculas.count === 1 ? 'solicitud' : 'solicitudes'}</div>
                        <p className="card-description">{prematriculas.count === 0 ? 'Aún no tienes solicitudes de prematrícula.' : 'Cantidad de solicitudes propias registradas.'}</p>
                    </>}
                    {prematriculas.status === 'error' && <><div className="card-value">No disponible</div><EstadoError>{prematriculas.error}</EstadoError><button className="btn btn-soft" type="button" onClick={cargarPrematriculas}>Reintentar</button></>}
                    <p className="card-description">Crea, revisa, modifica o cancela tus solicitudes.</p>
                    <Link className="text-link" to="/estudiante/prematricula">
                        Abrir mis prematrículas <span aria-hidden="true">→</span>
                    </Link>
                </article>
            </section>
        </>
    )
}
