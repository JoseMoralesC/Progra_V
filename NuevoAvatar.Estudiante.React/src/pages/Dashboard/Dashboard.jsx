import { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import './dashboard.css'

const indicadores = [
    { id: 'ADM4', nombre: 'Usuarios', ruta: '/gateway/admin/usuario' },
    { id: 'ADM9', nombre: 'Carreras', ruta: '/gateway/admin/carrera' },
    { id: 'ADM10', nombre: 'Cursos', ruta: '/gateway/admin/curso' },
    { id: 'ADM12', nombre: 'Períodos activos', ruta: '/gateway/admin/periodo?estado=ACTIVO' }
]

function presentarBitacora(descripcion) {
    const texto = String(descripcion || '')
    const separador = texto.lastIndexOf(' · ')
    if (separador < 0) return { accion: texto || '—', modulo: '—' }
    return { accion: texto.slice(0, separador) || '—', modulo: texto.slice(separador + 3) || '—' }
}

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
                    const resultado = await cliente.request('/gateway/mat/profesor/grupos')
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
            if (permitido('ADM20') && permitido('ADM12')) {
                try {
                    const periodos = await cliente.request('/gateway/admin/periodo')
                    if (!Array.isArray(periodos)) throw new Error('No se pudieron leer los períodos.')
                    const facturas = await Promise.all(periodos.map(async periodo => {
                        const datos = await cliente.request(`/gateway/fin/factura?periodoId=${periodo.periodoId}`)
                        if (!Array.isArray(datos)) throw new Error('No se pudieron leer las facturas.')
                        return datos
                    }))
                    actuales['Facturas pendientes'] = facturas.flat().filter(factura => factura.estado === 'PENDIENTE').length
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

    if (usuario.rol === 'PROFESOR') return (
        <div className="dashboard">
            {errorAuditoria && <p role="alert">{errorAuditoria}</p>}
            <section className="panel">
                <h2>Bienvenido, {usuario.nombre}</h2>
                <p>Perfil: Profesor</p>
                <h3>Mis grupos y cursos</h3>
                {error && <p role="alert">{error}</p>}
                {!usuario.modulos.some(modulo => modulo.idModulo === 'ADM17') && <p>Sin permiso para consultar grupos.</p>}
                {grupos === null && !error && usuario.modulos.some(modulo => modulo.idModulo === 'ADM17') && <p>Cargando grupos…</p>}
                {grupos?.length === 0 && <p>No hay grupos activos asignados.</p>}
                {grupos?.length > 0 && <ul>{grupos.map(grupo => <li key={grupo.grupoId}>
                    Grupo {grupo.numeroGrupo}: {grupo.curso}
                </li>)}</ul>}
                {usuario.modulos.some(modulo => modulo.idModulo === 'ADM17') && <Link to="/notas">Consultar y registrar notas</Link>}
                <div className="dashboard-enlaces">
                    <a className="btn btn-primary dashboard-quick-link" href="mailto:soportenuevoavatar@gmail.com">Ayuda/soporte</a>
                    <Link className="btn btn-primary dashboard-quick-link" to="/perfil">Mi perfil</Link>
                </div>
            </section>
            <ActividadReciente key={claveActividad} cliente={cliente} usuario={usuario} />
        </div>
    )

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
                        ['ADM6', 'Parámetros', '/parametros'], ['ADM8', 'Instituciones', '/instituciones']
                    ].filter(([id]) => usuario.modulos.some(modulo => modulo.idModulo === id))
                        .map(([id, nombre, ruta]) => <Link className="btn btn-primary dashboard-quick-link" key={id} to={ruta}>{nombre}</Link>)}
                    <a className="btn btn-primary dashboard-quick-link" href="mailto:soportenuevoavatar@gmail.com">Ayuda/soporte</a>
                    <Link className="btn btn-primary dashboard-quick-link" to="/perfil">Mi perfil</Link>
                </nav>
            </section>
            {errorMetricas && <p role="alert">{errorMetricas}</p>}
            <section className="dashboard-metricas" aria-label="Indicadores administrativos">
                {[...indicadores.map(item => item.nombre), 'Facturas pendientes'].map(nombre => <article className="panel" key={nombre}>
                    <h3>{nombre}</h3><strong>{cargandoMetricas ? 'Cargando…' : metricas[nombre] ?? 'No disponible'}</strong>
                </article>)}
            </section>
            <ActividadReciente key={claveActividad} cliente={cliente} usuario={usuario} />
        </div>
    )
}


function ActividadReciente({ cliente, usuario }) {
    const [bitacoras, setBitacoras] = useState(null)
    const [notificaciones, setNotificaciones] = useState(null)
    const [errorBitacoras, setErrorBitacoras] = useState('')
    const [errorNotificaciones, setErrorNotificaciones] = useState('')
    const permiteNotificaciones = usuario.rol === 'PROFESOR' || usuario.modulos.some(m => m.idModulo === 'ADM22')

    useEffect(() => {
        const control = new AbortController()
        cliente.request('/gateway/admin/bitacora?vista=dashboard').then(datos => {
            if (!Array.isArray(datos)) throw new Error('No se pudieron leer las bitácoras.')
            if (!control.signal.aborted) setBitacoras(datos)
        }).catch(error => { if (!control.signal.aborted) setErrorBitacoras(error.message) })
        if (permiteNotificaciones) {
            cliente.request('/gateway/fin/notificar/historial?vista=dashboard').then(datos => {
                if (!Array.isArray(datos?.items)) throw new Error('No se pudieron leer las notificaciones.')
                if (!control.signal.aborted) setNotificaciones(datos.items)
            }).catch(error => { if (!control.signal.aborted) setErrorNotificaciones(error.message) })
        }
        return () => control.abort()
    }, [cliente, permiteNotificaciones])

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
        <section className="panel" aria-label="Bitácoras recientes">
            <h3>Bitácoras recientes</h3>
            {errorBitacoras && <p role="alert">{errorBitacoras}</p>}
            {bitacoras === null && !errorBitacoras && <p role="status">Cargando bitácoras…</p>}
            {bitacoras?.length === 0 && <p>No hay bitácoras recientes.</p>}
            {bitacoras?.length > 0 && <div className="dashboard-table-wrap">
                <table className="dashboard-activity-table">
                    <thead><tr><th>Módulo</th><th>Acción</th><th>Usuario</th><th>Fecha</th></tr></thead>
                    <tbody>{bitacoras.map(item => {
                        const { accion, modulo } = presentarBitacora(item.descripcion)
                        return <tr key={item.idBitacora}>
                            <td>{modulo}</td><td>{accion}</td><td>{item.usuario}</td>
                            <td>{item.fechaHora ? new Date(item.fechaHora).toLocaleString('es-CR') : '—'}</td>
                        </tr>
                    })}</tbody>
                </table>
            </div>}
        </section>
    </>
}
