import { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import './dashboard.css'

const indicadores = [
    { id: 'ADM4', nombre: 'Usuarios', ruta: '/gateway/admin/usuario?vista=dashboard' },
    { id: 'ADM9', nombre: 'Carreras', ruta: '/gateway/admin/carrera?vista=dashboard' },
    { id: 'ADM10', nombre: 'Cursos', ruta: '/gateway/admin/curso?vista=dashboard' },
    { id: 'ADM12', nombre: 'Períodos activos', ruta: '/gateway/admin/periodo?estado=ACTIVO&vista=dashboard' }
]

export function Dashboard({ cliente, usuario }) {
    const [metricas, setMetricas] = useState({})
    const [grupos, setGrupos] = useState(null)
    const [error, setError] = useState('')
    const [errorMetricas, setErrorMetricas] = useState('')
    const [cargandoMetricas, setCargandoMetricas] = useState(true)
    const [errorAuditoria, setErrorAuditoria] = useState('')
    const visita = useRef(null)
    const claveActividad = `${usuario.usuarioID}:${usuario.rol}:${usuario.modulos.map(m => m.idModulo).join(',')}`

    useEffect(() => {
        const control = new AbortController()
        // Los efectos repetidos del mismo montaje comparten el registro de visita.
        if (!visita.current) visita.current = cliente.request('/gateway/menu?vista=dashboard')
        visita.current.catch(() => {
            if (!control.signal.aborted) setErrorAuditoria('No se pudo registrar la visualización del dashboard.')
        })
        return () => control.abort()
    }, [cliente])

    useEffect(() => {
        let vigente = true
        const permitido = id => usuario.modulos.some(modulo => modulo.idModulo === id)
        async function cargar() {
            setError('')
            setErrorMetricas('')
            setCargandoMetricas(true)
            setGrupos(null)
            if (usuario.rol === 'PROFESOR') {
                if (!permitido('ADM17')) return
                try {
                    const resultado = await cliente.request('/gateway/mat/profesor/grupos?vista=dashboard')
                    if (!Array.isArray(resultado)) throw new Error('No se pudieron leer los grupos.')
                    if (vigente) setGrupos(resultado)
                } catch (fallo) {
                    if (vigente) setError(fallo.message)
                }
                return
            }
            if (usuario.rol !== 'ADMINISTRADOR') return
            const resultados = await Promise.allSettled(indicadores.map(async indicador => {
                if (!permitido(indicador.id)) return null
                const datos = await cliente.request(indicador.ruta)
                if (!Array.isArray(datos)) throw new Error('Respuesta inválida')
                return datos.length
            }))
            if (!vigente) return
            const fallos = indicadores.filter((_, indice) => resultados[indice].status === 'rejected').map(item => item.nombre)
            const actuales = Object.fromEntries(indicadores.map((indicador, indice) => [
                indicador.nombre,
                resultados[indice].status === 'fulfilled' ? resultados[indice].value : null
            ]))
            if (permitido('ADM3') && permitido('ADM12')) {
                try {
                    const periodos = await cliente.request('/gateway/admin/periodo?vista=dashboard')
                    if (!Array.isArray(periodos)) throw new Error('No se pudieron leer los períodos.')
                    const facturas = await Promise.all(periodos.map(async periodo => {
                        const datos = await cliente.request(`/gateway/fin/factura?periodoId=${periodo.periodoId}&vista=dashboard`)
                        if (!Number.isSafeInteger(datos?.pendientes) || datos.pendientes < 0)
                            throw new Error('No se pudo leer el indicador de facturas.')
                        return datos.pendientes
                    }))
                    actuales['Facturas pendientes'] = facturas.reduce((suma, cantidad) => suma + cantidad, 0)
                } catch {
                    fallos.push('Facturas pendientes')
                    actuales['Facturas pendientes'] = null
                }
            } else {
                actuales['Facturas pendientes'] = null
            }
            if (vigente) {
                setMetricas(actuales)
                setCargandoMetricas(false)
                setErrorMetricas(fallos.length ? `No se pudieron consultar: ${fallos.join(', ')}.` : '')
            }

        }
        cargar()
        return () => { vigente = false }
    }, [cliente, usuario])

    if (usuario.rol === 'PROFESOR') {
        const cursosImpartidos = grupos ? Array.from(
            grupos.reduce((mapa, g) => {
                const clave = g.cursoId ?? g.curso
                if (!mapa.has(clave)) {
                    mapa.set(clave, {
                        id: clave,
                        nombre: g.curso,
                        grupos: []
                    })
                }
                if (g.numeroGrupo && !mapa.get(clave).grupos.includes(g.numeroGrupo)) {
                    mapa.get(clave).grupos.push(g.numeroGrupo)
                }
                return mapa
            }, new Map()).values()
        ) : null

        return (
            <div className="dashboard">
                {errorAuditoria && <p role="alert">{errorAuditoria}</p>}
                <section className="panel">
                    <h2>Bienvenido, {usuario.nombre}</h2>
                    <p>Perfil: Profesor</p>
                    {error && <p role="alert">{error}</p>}
                    {!usuario.modulos.some(modulo => modulo.idModulo === 'ADM17') && <p>Sin permiso para consultar grupos.</p>}
                    {grupos === null && !error && usuario.modulos.some(modulo => modulo.idModulo === 'ADM17') && <p>Cargando grupos…</p>}
                    {grupos?.length === 0 && <p>No hay grupos activos asignados.</p>}
                    {grupos?.length > 0 && (
                        <div className="dashboard-widgets-grid">
                            <article className="dashboard-widget-card" aria-label="Mis grupos">
                                <h3>Mis grupos</h3>
                                <ul className="dashboard-widget-list">
                                    {grupos.map(grupo => (
                                        <li key={grupo.grupoId} className="dashboard-widget-item">
                                            <span className="dashboard-item-titulo">Grupo {grupo.numeroGrupo}: {grupo.curso}</span>
                                            {grupo.horario && <span className="dashboard-item-detalle">Horario: {grupo.horario}</span>}
                                        </li>
                                    ))}
                                </ul>
                            </article>
                            <article className="dashboard-widget-card" aria-label="Cursos que imparto">
                                <h3>Cursos que imparto</h3>
                                <ul className="dashboard-widget-list">
                                    {cursosImpartidos?.map(curso => (
                                        <li key={curso.id} className="dashboard-widget-item">
                                            <span className="dashboard-item-titulo">{curso.nombre}</span>
                                            <span className="dashboard-item-detalle">
                                                {curso.grupos.length === 1
                                                    ? `Grupo: ${curso.grupos[0]}`
                                                    : `Grupos: ${curso.grupos.sort((a, b) => a - b).join(', ')}`}
                                            </span>
                                        </li>
                                    ))}
                                </ul>
                            </article>
                        </div>
                    )}
                    <div className="dashboard-enlaces">
                        {usuario.modulos.some(modulo => modulo.idModulo === 'ADM17') && (
                            <Link className="btn btn-primary dashboard-quick-link" to="/notas">Consultar y registrar notas</Link>
                        )}
                        <a className="btn btn-primary dashboard-quick-link" href="mailto:soportenuevoavatar@gmail.com">Ayuda/soporte</a>
                        <Link className="btn btn-primary dashboard-quick-link" to="/perfil">Mi perfil</Link>
                    </div>
                </section>
                <ActividadReciente key={claveActividad} cliente={cliente} usuario={usuario} />
            </div>
        )
    }

    if (usuario.rol !== 'ADMINISTRADOR') return null
    return (
        <div className="dashboard">
            {errorAuditoria && <p role="alert">{errorAuditoria}</p>}
            <section className="panel">
                <h2>Bienvenido, {usuario.nombre}</h2>
                <p>Perfil: Administrador</p>
                <nav className="dashboard-enlaces" aria-label="Accesos rápidos">
                    {[
                        ['ADM4', 'Usuarios', '/usuarios'], ['ADM5', 'Roles y permisos', '/roles'],
                        ['ADM6', 'Parámetros', '/parametros'], ['ADM8', 'Instituciones', '/instituciones'],
                        ['ADM9', 'Carreras', '/carreras'], ['ADM10', 'Cursos', '/cursos'],
                        ['ADM12', 'Períodos', '/periodos']
                    ].filter(([id]) => usuario.modulos.some(modulo => modulo.idModulo === id))
                        .map(([id, nombre, ruta]) => <Link className="btn btn-primary dashboard-quick-link" key={id} to={ruta}>{nombre}</Link>)}
                    {usuario.modulos.some(modulo => modulo.idModulo === 'ADM3') &&
                        <a className="btn btn-primary dashboard-quick-link" href="#facturas-pendientes">Facturas pendientes</a>}
                    <a className="btn btn-primary dashboard-quick-link" href="mailto:soportenuevoavatar@gmail.com">Ayuda/soporte</a>
                    <Link className="btn btn-primary dashboard-quick-link" to="/perfil">Mi perfil</Link>
                </nav>
            </section>
            {errorMetricas && <p role="alert">{errorMetricas}</p>}
            <section className="dashboard-metricas" aria-label="Indicadores administrativos">
                {[...indicadores.map(item => item.nombre), 'Facturas pendientes'].map(nombre => <article className="panel"
                    id={nombre === 'Facturas pendientes' ? 'facturas-pendientes' : undefined} key={nombre}>
                    <h3>{nombre}</h3><strong>{cargandoMetricas ? 'Cargando…' : metricas[nombre] ?? 'No disponible'}</strong>
                </article>)}
            </section>
            <ActividadReciente key={claveActividad} cliente={cliente} usuario={usuario} />
        </div>
    )
}


function ActividadReciente({ cliente, usuario }) {
    const [bitacoras, setBitacoras] = useState(null)
    const administrador = usuario.rol === 'ADMINISTRADOR'
    const [pagina, setPagina] = useState(0)
    const [tamano, setTamano] = useState(25)
    const [total, setTotal] = useState(0)
    const [revision, setRevision] = useState(0)
    const hastaId = useRef(null)
    const [hayNuevos, setHayNuevos] = useState(false)
    const [errorActualizacion, setErrorActualizacion] = useState('')
    const cargandoBitacoras = useRef(false)
    const [actualizando, setActualizando] = useState(true)
    const paginas = Math.max(1, Math.ceil(total / tamano))
    const [notificaciones, setNotificaciones] = useState(null)
    const [errorBitacoras, setErrorBitacoras] = useState('')
    const [errorNotificaciones, setErrorNotificaciones] = useState('')
    const permiteNotificaciones = usuario.rol === 'PROFESOR' || usuario.modulos.some(m => m.idModulo === 'ADM22')

    useEffect(() => {
        const control = new AbortController()
        if (permiteNotificaciones) {
            cliente.request('/gateway/fin/notificar/historial?vista=dashboard').then(datos => {
                if (!Array.isArray(datos?.items)) throw new Error('No se pudieron leer las notificaciones.')
                if (!control.signal.aborted) setNotificaciones(datos.items)
            }).catch(error => { if (!control.signal.aborted) setErrorNotificaciones(error.message) })
        }
        return () => control.abort()
    }, [cliente, permiteNotificaciones])

    useEffect(() => {
        const control = new AbortController()
        async function cargarBitacoras() {
            cargandoBitacoras.current = true
            setActualizando(true)
            setErrorBitacoras('')
            const ruta = '/gateway/admin/bitacora?vista=dashboard' +
                (administrador ? `&page=${pagina}&size=${tamano}` + (hastaId.current === null ? '' : `&hastaId=${hastaId.current}`) : '')
            try {
                const datos = await cliente.request(ruta, { signal: control.signal })
                if (control.signal.aborted) return
                if (administrador) {
                    if (!Array.isArray(datos?.items) || !Number.isSafeInteger(datos.total) || datos.total < 0 ||
                        !Number.isSafeInteger(datos.hastaId) || datos.hastaId < 0)
                        throw new Error('No se pudo leer el historial de bitácora.')
                    const ultima = Math.max(0, Math.ceil(datos.total / tamano) - 1)
                    setTotal(datos.total)
                    hastaId.current = datos.hastaId
                    if (pagina === 0) setHayNuevos(false)
                    if (pagina > ultima) { setPagina(ultima); return }
                    setBitacoras(datos.items)
                } else {
                    if (!Array.isArray(datos)) throw new Error('No se pudieron leer las bitácoras.')
                    setBitacoras(datos)
                }
                setErrorActualizacion('')
            } catch (error) { if (!control.signal.aborted) setErrorBitacoras(error.message) }
            finally { if (!control.signal.aborted) { cargandoBitacoras.current = false; setActualizando(false) } }
        }
        cargarBitacoras()
        return () => control.abort()
    }, [cliente, administrador, pagina, tamano, revision])

    useEffect(() => {
        const control = new AbortController()
        const consultando = { actual: false }
        const reloj = setInterval(async () => {
            if (document.hidden || cargandoBitacoras.current || consultando.actual) return
            if (!administrador) {
                hastaId.current = null
                setRevision(valor => valor + 1)
                return
            }
            consultando.actual = true
            try {
                const datos = await cliente.request('/gateway/admin/bitacora?vista=dashboard&page=0&size=1', { signal: control.signal })
                if (!control.signal.aborted) {
                    if (!Number.isSafeInteger(datos?.hastaId)) throw new Error('No se pudo comprobar si hay movimientos nuevos.')
                    setHayNuevos(datos.hastaId > (hastaId.current ?? 0))
                    setErrorActualizacion('')
                }
            } catch (error) { if (!control.signal.aborted) setErrorActualizacion(error.message) }
            finally { consultando.actual = false }
        }, 10000)
        return () => { clearInterval(reloj); control.abort() }
    }, [cliente, administrador, pagina])

    return <>
        <section className="panel" aria-label="Notificaciones recientes">
            <h3>Notificaciones recientes</h3>
            {!permiteNotificaciones && <p>Sin permiso para consultar notificaciones.</p>}
            {errorNotificaciones && <p role="alert">{errorNotificaciones}</p>}
            {permiteNotificaciones && notificaciones === null && !errorNotificaciones && <p role="status">Cargando notificaciones…</p>}
            {notificaciones?.length === 0 && <p>No hay notificaciones recientes.</p>}
            {notificaciones?.length > 0 && <ul>{notificaciones.map(item => <li key={item.id}>
                {item.asunto} · {item.estado} · {new Date(item.fecha).toLocaleString('es-CR')}
            </li>)}</ul>}
        </section>
        <section className="panel" aria-label={administrador ? 'Historial de bitácora' : 'Bitácoras recientes'}>
            <h3>{administrador ? 'Historial de bitácora' : 'Bitácoras recientes'}</h3>
            {administrador && <div className="dashboard-bitacora-controles">
                <label>Registros por página <select value={tamano} onChange={evento => {
                    setTamano(Number(evento.target.value)); setPagina(0)
                }}>
                    {[10, 25, 50, 100].map(cantidad => <option key={cantidad} value={cantidad}>{cantidad}</option>)}
                </select></label>
                <button type="button" disabled={actualizando} onClick={() => {
                    hastaId.current = null; setPagina(0); setRevision(valor => valor + 1)
                }}>Actualizar historial</button>
                <span>Se comprueban movimientos nuevos cada 10 segundos.</span>
            </div>}
            {administrador && <p role="status">{hayNuevos ? 'Hay movimientos nuevos. Pulsa “Actualizar historial” para verlos.' : 'El historial permanece estable mientras lo lees.'}</p>}
            {errorActualizacion && <p role="alert">{errorActualizacion}</p>}
            {errorBitacoras && <p role="alert">{errorBitacoras}</p>}
            {bitacoras === null && !errorBitacoras && <p role="status">Cargando bitácoras…</p>}
            {bitacoras?.length === 0 && <p>No hay registros de bitácora.</p>}
            {bitacoras?.length > 0 && <div className="dashboard-table-wrap">
                <table className="dashboard-activity-table">
                    <thead><tr><th>ID</th><th>Módulo</th><th>Resumen de la acción</th><th>Resultado</th><th>Realizado por</th><th>Fecha</th></tr></thead>
                    <tbody>{bitacoras.map(item => {
                        return <tr key={item.idBitacora}>
                            <td>{item.idBitacora}</td><td>{item.modulo}</td><td>{item.accion}</td><td>{item.resultado}</td><td>{item.usuario}</td>
                            <td>{item.fechaHora ? new Date(item.fechaHora).toLocaleString('es-CR') : '—'}</td>
                        </tr>
                    })}</tbody>
                </table>
            </div>}
            {administrador && <nav className="dashboard-bitacora-controles" aria-label="Paginación de bitácora">
                <span role="status">{total.toLocaleString('es-CR')} registros · Página {pagina + 1} de {paginas}</span>
                <button type="button" disabled={pagina === 0 || actualizando} onClick={() => setPagina(0)}>Primera</button>
                <button type="button" disabled={pagina === 0 || actualizando} onClick={() => setPagina(valor => valor - 1)}>Anterior</button>
                <button type="button" disabled={pagina + 1 >= paginas || actualizando} onClick={() => setPagina(valor => valor + 1)}>Siguiente</button>
                <button type="button" disabled={pagina + 1 >= paginas || actualizando} onClick={() => setPagina(paginas - 1)}>Última</button>
                <form onSubmit={evento => {
                    evento.preventDefault()
                    const destino = Number(new FormData(evento.currentTarget).get('pagina'))
                    if (Number.isInteger(destino) && destino >= 1 && destino <= paginas) setPagina(destino - 1)
                }}>
                    <label>Ir a página <input key={pagina} name="pagina" type="number" min="1" max={paginas} defaultValue={pagina + 1} required /></label>
                    <button type="submit" disabled={actualizando}>Ir</button>
                </form>
            </nav>}
        </section>
    </>
}
