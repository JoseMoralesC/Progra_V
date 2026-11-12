import { useCallback, useEffect, useMemo, useState } from 'react'
import { useAuth } from '../../context/AuthContext'
import { useNotifications } from '../../context/NotificationContext'
import { ConfirmarEliminacion } from '../../components/ConfirmarEliminacion'
import {
    actualizarPrematriculaPropia,
    cancelarPrematriculaPropia,
    crearPrematriculaPropia,
    obtenerOpcionesPrematricula,
    obtenerPrematriculasPropias
} from '../../services/api'

const formularioVacio = { carreraId: '', cursos: [], observaciones: '', periodoId: '' }
const SOLICITUDES_POR_PAGINA = 10

function mensajeError(error, accion) {
    if (error?.status === 401) return 'Tu sesión expiró. Inicia sesión nuevamente.'
    if (error?.status === 403) return 'Tu sesión no tiene permiso para esta operación.'
    if (error?.status === 404) return 'No encontramos esa solicitud dentro de tus prematrículas.'
    if (error?.status === 400) return error.message || 'Revisa que los cursos sean de primer nivel y el período inicie en una fecha futura.'
    if (error?.status === 503) return 'Un servicio requerido no está disponible. Inténtalo más tarde.'
    return `No fue posible ${accion}. Inténtalo nuevamente.`
}

function formularioDesde(solicitud) {
    return {
        carreraId: String(solicitud.carreraId),
        cursos: (solicitud.cursos || []).map(String),
        observaciones: solicitud.observaciones || '',
        periodoId: String(solicitud.periodoId)
    }
}

export default function Prematricula() {
    const { authorizedFetch } = useAuth()
    const { notify } = useNotifications()
    const [solicitudes, setSolicitudes] = useState([])
    const [opciones, setOpciones] = useState({ carreras: [], cursos: [], periodos: [] })
    const [formulario, setFormulario] = useState(formularioVacio)
    const [editando, setEditando] = useState(null)
    const [cargando, setCargando] = useState(true)
    const [guardando, setGuardando] = useState(false)
    const [errorCarga, setErrorCarga] = useState('')
    const [errores, setErrores] = useState({})
    const [pagina, setPagina] = useState(0)
    const [pendienteCancelar, setPendienteCancelar] = useState(null)

    const cargar = useCallback(async () => {
        setCargando(true)
        setErrorCarga('')
        try {
            const [lista, catalogos] = await Promise.all([
                authorizedFetch(token => obtenerPrematriculasPropias(token)),
                authorizedFetch(token => obtenerOpcionesPrematricula(token))
            ])
            setSolicitudes(Array.isArray(lista) ? lista : [])
            setOpciones({
                carreras: Array.isArray(catalogos?.carreras) ? catalogos.carreras : [],
                cursos: Array.isArray(catalogos?.cursos) ? catalogos.cursos : [],
                periodos: Array.isArray(catalogos?.periodos) ? catalogos.periodos : []
            })
        } catch (error) {
            const mensaje = mensajeError(error, 'cargar tus prematrículas')
            setErrorCarga(mensaje)
            notify(mensaje, 'error')
        } finally {
            setCargando(false)
        }
    }, [authorizedFetch, notify])

    useEffect(() => { cargar() }, [cargar])

    const cursosDisponibles = useMemo(() => opciones.cursos.filter(curso =>
        String(curso.carreraId) === formulario.carreraId), [formulario.carreraId, opciones.cursos])
    const totalPaginas = Math.max(1, Math.ceil(solicitudes.length / SOLICITUDES_POR_PAGINA))
    const paginaActual = Math.min(pagina, totalPaginas - 1)
    const solicitudesVisibles = solicitudes.slice(
        paginaActual * SOLICITUDES_POR_PAGINA,
        (paginaActual + 1) * SOLICITUDES_POR_PAGINA
    )

    useEffect(() => {
        setPagina(0)
    }, [solicitudes])

    function iniciarEdicion(solicitud = null) {
        setEditando(solicitud?.id ?? 'nueva')
        setFormulario(solicitud ? formularioDesde(solicitud) : formularioVacio)
        setErrores({})
    }

    function cancelarEdicion() {
        setEditando(null)
        setFormulario(formularioVacio)
        setErrores({})
    }

    function cambiarCursos(id, seleccionado) {
        setFormulario(actual => ({
            ...actual,
            cursos: seleccionado
                ? [...new Set([...actual.cursos, id])]
                : actual.cursos.filter(curso => curso !== id)
        }))
    }

    async function guardar(evento) {
        evento.preventDefault()
        if (guardando) return
        const nuevosErrores = {}
        if (!formulario.carreraId || !opciones.carreras.some(item => String(item.id) === formulario.carreraId)) {
            nuevosErrores.carreraId = 'Selecciona una carrera disponible.'
        }
        if (!formulario.cursos.length || formulario.cursos.some(id =>
            !cursosDisponibles.some(item => String(item.id) === id))) {
            nuevosErrores.cursos = 'Selecciona al menos un curso de primer nivel de la carrera.'
        }
        if (!formulario.periodoId || !opciones.periodos.some(item => String(item.id) === formulario.periodoId)) {
            nuevosErrores.periodoId = 'Selecciona un período con fecha de inicio futura.'
        }
        setErrores(nuevosErrores)
        if (Object.keys(nuevosErrores).length) return

        const cambios = {
            carreraId: Number(formulario.carreraId),
            cursos: formulario.cursos.map(Number),
            observaciones: formulario.observaciones.trim() || null,
            periodoId: Number(formulario.periodoId)
        }
        setGuardando(true)
        try {
            const actualizado = editando === 'nueva'
                ? await authorizedFetch(token => crearPrematriculaPropia(cambios, token))
                : await authorizedFetch(token => actualizarPrematriculaPropia(editando, cambios, token))
            setSolicitudes(actuales => editando === 'nueva'
                ? [...actuales, actualizado]
                : actuales.map(item => item.id === actualizado.id ? actualizado : item))
            notify(editando === 'nueva' ? 'Prematrícula creada correctamente.' : 'Prematrícula actualizada correctamente.', 'success')
            cancelarEdicion()
        } catch (error) {
            notify(mensajeError(error, 'guardar la prematrícula'), 'error')
        } finally {
            setGuardando(false)
        }
    }

    async function cancelarSolicitud(id) {
        if (guardando) return
        setGuardando(true)
        try {
            await authorizedFetch(token => cancelarPrematriculaPropia(id, token))
            setSolicitudes(actuales => actuales.filter(item => item.id !== id))
            if (editando === id) cancelarEdicion()
            notify('Prematrícula cancelada.', 'success')
        } catch (error) {
            notify(mensajeError(error, 'cancelar la prematrícula'), 'error')
        } finally {
            setGuardando(false)
        }
    }

    if (cargando) return <section className="panel" role="status">Cargando tus prematrículas…</section>
    if (errorCarga) return (
        <section className="panel" role="alert">
            <h2>No se pudieron cargar tus prematrículas</h2>
            <p>{errorCarga}</p>
            <button className="btn btn-soft" type="button" onClick={cargar}>Reintentar</button>
        </section>
    )

    return (
        <>
            <section className="page-head">
                <div>
                    <h2>Mis prematrículas</h2>
                    <p className="subtitle">Consulta y administra únicamente tus propias solicitudes.</p>
                </div>
                {editando === null && <button className="btn btn-primary" type="button" onClick={() => iniciarEdicion()}>
                    Nueva solicitud
                </button>}
            </section>

            {editando !== null && (
                <form className="panel" onSubmit={guardar} noValidate>
                    <h3>{editando === 'nueva' ? 'Crear prematrícula' : 'Editar prematrícula'}</h3>
                    <div className="form-grid">
                        <div className="field">
                            <label htmlFor="prematricula-carrera">Carrera</label>
                            <select id="prematricula-carrera" value={formulario.carreraId} disabled={guardando}
                                onChange={event => setFormulario(actual => ({ ...actual, carreraId: event.target.value, cursos: [] }))}>
                                <option value="">Selecciona una carrera</option>
                                {opciones.carreras.map(item => <option key={item.id} value={item.id}>{item.nombre}</option>)}
                            </select>
                            {errores.carreraId && <p className="status status-danger" role="alert">{errores.carreraId}</p>}
                        </div>
                        <fieldset className="field course-fieldset">
                            <legend>Cursos de primer nivel</legend>
                            {!formulario.carreraId && <p>Selecciona primero una carrera.</p>}
                            {formulario.carreraId && !cursosDisponibles.length && <p>No hay cursos de primer nivel disponibles.</p>}
                            {Boolean(cursosDisponibles.length) && <div className="course-options">
                                {cursosDisponibles.map(curso => (
                                    <label key={curso.id} className="course-option">
                                        <input className="course-option__checkbox" type="checkbox" value={curso.id}
                                            checked={formulario.cursos.includes(String(curso.id))}
                                            disabled={guardando} onChange={event => cambiarCursos(String(curso.id), event.target.checked)} />
                                        <span>{curso.nombre}</span>
                                    </label>
                                ))}
                            </div>}
                            {errores.cursos && <p className="status status-danger" role="alert">{errores.cursos}</p>}
                        </fieldset>
                        <div className="field">
                            <label htmlFor="prematricula-periodo">Período</label>
                            <select id="prematricula-periodo" value={formulario.periodoId} disabled={guardando}
                                onChange={event => setFormulario(actual => ({ ...actual, periodoId: event.target.value }))}>
                                <option value="">Selecciona un período</option>
                                {opciones.periodos.map(item => <option key={item.id} value={item.id}>
                                    Período {item.id} · inicia {item.fechaInicio}
                                </option>)}
                            </select>
                            {errores.periodoId && <p className="status status-danger" role="alert">{errores.periodoId}</p>}
                        </div>
                        <div className="field" style={{ gridColumn: '1 / -1' }}>
                            <label htmlFor="prematricula-observaciones">Observaciones (opcional)</label>
                            <textarea id="prematricula-observaciones" value={formulario.observaciones} disabled={guardando}
                                onChange={event => setFormulario(actual => ({ ...actual, observaciones: event.target.value }))} />
                        </div>
                    </div>
                    <div className="profile-actions">
                        <button className="btn btn-primary" type="submit" disabled={guardando}>
                            {guardando ? 'Guardando…' : 'Guardar solicitud'}
                        </button>
                        <button className="btn btn-soft" type="button" disabled={guardando} onClick={cancelarEdicion}>
                            Cancelar edición
                        </button>
                    </div>
                </form>
            )}

            <section className="panel" aria-labelledby="prematriculas-lista-title">
                <h3 id="prematriculas-lista-title">Solicitudes registradas</h3>
                {!solicitudes.length && <p>Aún no tienes solicitudes de prematrícula.</p>}
                {solicitudesVisibles.map(solicitud => {
                    const carrera = opciones.carreras.find(item => item.id === solicitud.carreraId)?.nombre || `Carrera ${solicitud.carreraId}`
                    const cursos = (solicitud.cursos || []).map(id =>
                        opciones.cursos.find(item => item.id === id)?.nombre || `Curso ${id}`).join(', ')
                    return (
                        <article className="panel" key={solicitud.id} aria-label={`Prematrícula ${solicitud.id}`}>
                            <h4>Solicitud #{solicitud.id}</h4>
                            <p><strong>Carrera:</strong> {carrera}</p>
                            <p><strong>Cursos:</strong> {cursos || 'Sin cursos'}</p>
                            <p><strong>Período:</strong> {solicitud.periodoId}</p>
                            {solicitud.observaciones && <p><strong>Observaciones:</strong> {solicitud.observaciones}</p>}
                            <div className="profile-actions">
                                <button className="btn btn-soft" type="button" disabled={guardando || editando !== null}
                                    onClick={() => iniciarEdicion(solicitud)}>Editar</button>
                                <button className="btn btn-soft" type="button" disabled={guardando || editando !== null}
                                    onClick={() => setPendienteCancelar(solicitud)}>Cancelar solicitud</button>
                            </div>
                        </article>
                    )
                })}
                {solicitudes.length > SOLICITUDES_POR_PAGINA && (
                    <div className="profile-actions" aria-label="Paginación de prematrículas">
                        <button className="btn btn-soft" type="button" disabled={paginaActual === 0}
                            onClick={() => setPagina(actual => Math.max(0, actual - 1))}>
                            Anterior
                        </button>
                        <span aria-live="polite">Página {paginaActual + 1} de {totalPaginas}</span>
                        <button className="btn btn-soft" type="button" disabled={paginaActual + 1 >= totalPaginas}
                            onClick={() => setPagina(actual => Math.min(totalPaginas - 1, actual + 1))}>
                            Siguiente
                        </button>
                    </div>
                )}
            </section>
            {pendienteCancelar && (
                <ConfirmarEliminacion
                    titulo="Cancelar prematrícula"
                    mensaje="¿Deseas cancelar esta solicitud de prematrícula? Esta acción no se puede deshacer desde el portal."
                    confirmar="Sí, cancelar"
                    cancelar="Mantener solicitud"
                    ocupado={guardando}
                    onCancelar={() => setPendienteCancelar(null)}
                    onConfirmar={() => {
                        const id = pendienteCancelar.id
                        setPendienteCancelar(null)
                        cancelarSolicitud(id)
                    }}
                />
            )}
        </>
    )
}
