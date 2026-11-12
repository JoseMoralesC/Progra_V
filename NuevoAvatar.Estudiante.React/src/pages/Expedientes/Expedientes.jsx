import { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { ConfirmarEliminacion } from '../../components/ConfirmarEliminacion'
import { Modal } from '../../components/Modal'
import { RowActionsMenu } from '../../components/RowActionsMenu'
import { expedientesApi, validarExpediente } from './expedientes-api'
import '../Instituciones/instituciones.css'
import './expedientes.css'

const vacio = {
    email: '', tipoIdentificacion: '', identificacion: '', nombreCompleto: '', fechaNacimiento: '',
    provinciaId: '', cantonId: '', distritoId: '', otrasSenas: '', telefonos: ['']
}

const id = value => String(value ?? '')

export function Expedientes({ cliente }) {
    const api = useMemo(() => expedientesApi(cliente), [cliente])
    const [expedientes, setExpedientes] = useState([])
    const [estudiantes, setEstudiantes] = useState([])
    const [provincias, setProvincias] = useState([])
    const [cantones, setCantones] = useState([])
    const [distritos, setDistritos] = useState([])
    const [cargando, setCargando] = useState(true)
    const [cargandoDependencias, setCargandoDependencias] = useState(true)
    const [cargandoCantones, setCargandoCantones] = useState(false)
    const [cargandoDistritos, setCargandoDistritos] = useState(false)
    const [error, setError] = useState('')
    const [errorDependencias, setErrorDependencias] = useState('')
    const [errorCantones, setErrorCantones] = useState('')
    const [errorDistritos, setErrorDistritos] = useState('')
    const [aviso, setAviso] = useState('')
    const [formulario, setFormulario] = useState(null)
    const [estudianteSeleccionado, setEstudianteSeleccionado] = useState('')
    const [detalle, setDetalle] = useState(null)
    const [pendiente, setPendiente] = useState(null)
    const [guardando, setGuardando] = useState(false)
    const [busqueda, setBusqueda] = useState('')
    const [pagina, setPagina] = useState(0)
    const [intentoCantones, setIntentoCantones] = useState(0)
    const [intentoDistritos, setIntentoDistritos] = useState(0)
    const secuenciaCantones = useRef(0)
    const secuenciaDistritos = useRef(0)

    const cargarExpedientes = useCallback(async () => {
        setCargando(true)
        setError('')
        try {
            const datos = await api.listar()
            if (!Array.isArray(datos)) throw new Error('El servicio devolvió una lista de expedientes inválida.')
            setExpedientes(datos)
            setPagina(0)
        } catch (e) { setError(e.message) }
        finally { setCargando(false) }
    }, [api])

    const cargarDependencias = useCallback(async () => {
        setCargandoDependencias(true)
        setErrorDependencias('')
        try {
            const [usuarios, roles, listaProvincias] = await Promise.all([api.estudiantes(), api.roles(), api.provincias()])
            if (!Array.isArray(usuarios) || !Array.isArray(roles) || !Array.isArray(listaProvincias))
                throw new Error('Una dependencia devolvió datos inválidos.')
            const rolesEstudiante = new Set(roles.filter(rol => String(rol.nombreRol).trim().toUpperCase() === 'ESTUDIANTE')
                .map(rol => rol.idRol))
            setEstudiantes(usuarios.filter(usuario => rolesEstudiante.has(usuario.idRol)))
            setProvincias(listaProvincias)
        } catch (e) { setErrorDependencias(e.message) }
        finally { setCargandoDependencias(false) }
    }, [api])

    useEffect(() => {
        cargarExpedientes()
        cargarDependencias()
    }, [cargarExpedientes, cargarDependencias])

    useEffect(() => {
        const provinciaId = formulario?.provinciaId
        const consulta = ++secuenciaCantones.current
        setCantones([])
        setErrorCantones('')
        if (!provinciaId) { setCargandoCantones(false); return undefined }
        setCargandoCantones(true)
        api.cantones(provinciaId).then(datos => {
            if (consulta !== secuenciaCantones.current) return
            if (!Array.isArray(datos)) throw new Error('No se pudo leer el catálogo de cantones.')
            setCantones(datos)
        }).catch(e => { if (consulta === secuenciaCantones.current) setErrorCantones(e.message) })
            .finally(() => { if (consulta === secuenciaCantones.current) setCargandoCantones(false) })
        return () => { if (consulta === secuenciaCantones.current) secuenciaCantones.current++ }
    }, [api, formulario?.provinciaId, intentoCantones])

    useEffect(() => {
        const provinciaId = formulario?.provinciaId
        const cantonId = formulario?.cantonId
        const consulta = ++secuenciaDistritos.current
        setDistritos([])
        setErrorDistritos('')
        if (!provinciaId || !cantonId) { setCargandoDistritos(false); return undefined }
        setCargandoDistritos(true)
        api.distritos(provinciaId, cantonId).then(datos => {
            if (consulta !== secuenciaDistritos.current) return
            if (!Array.isArray(datos)) throw new Error('No se pudo leer el catálogo de distritos.')
            setDistritos(datos)
        }).catch(e => { if (consulta === secuenciaDistritos.current) setErrorDistritos(e.message) })
            .finally(() => { if (consulta === secuenciaDistritos.current) setCargandoDistritos(false) })
        return () => { if (consulta === secuenciaDistritos.current) secuenciaDistritos.current++ }
    }, [api, formulario?.provinciaId, formulario?.cantonId, intentoDistritos])

    const filtrados = expedientes.filter(item => {
        const termino = busqueda.trim().toLocaleLowerCase()
        return !termino || [item.nombreCompleto, item.email, item.identificacion]
            .some(valor => String(valor || '').toLocaleLowerCase().includes(termino))
    })
    const tamanoPagina = 10
    const paginas = Math.max(1, Math.ceil(filtrados.length / tamanoPagina))
    const paginaActual = Math.min(pagina, paginas - 1)
    const visibles = filtrados.slice(paginaActual * tamanoPagina, (paginaActual + 1) * tamanoPagina)

    function abrir(expediente = null) {
        setError('')
        setAviso('')
        setDetalle(null)
        setFormulario(expediente ? { ...expediente, telefonos: [...(expediente.telefonos || [])].length
            ? [...expediente.telefonos] : [''], provinciaId: id(expediente.provinciaId), cantonId: id(expediente.cantonId),
        distritoId: id(expediente.distritoId) } : { ...vacio })
        setEstudianteSeleccionado(expediente?.email || '')
    }

    function seleccionarEstudiante(email) {
        setEstudianteSeleccionado(email)
        const usuario = estudiantes.find(item => item.email === email)
        if (!usuario) return
        setFormulario(actual => ({ ...actual, email: usuario.email, tipoIdentificacion: usuario.tipoIdentificacion,
            identificacion: usuario.identificacion, nombreCompleto: usuario.nombre }))
    }

    function cambiarCampo(campo, valor) {
        setFormulario(actual => ({ ...actual, [campo]: valor }))
        if (campo === 'provinciaId') setFormulario(actual => ({ ...actual, provinciaId: valor, cantonId: '', distritoId: '' }))
        if (campo === 'cantonId') setFormulario(actual => ({ ...actual, cantonId: valor, distritoId: '' }))
    }

    async function guardar(evento) {
        evento.preventDefault()
        if (guardando || !formulario) return
        const { __identificacionOriginal, ...camposFormulario } = formulario
        const datos = { ...camposFormulario, email: formulario.email.trim(), tipoIdentificacion: formulario.tipoIdentificacion.trim(),
            identificacion: formulario.identificacion.trim(), nombreCompleto: formulario.nombreCompleto.trim(),
            provinciaId: Number(formulario.provinciaId), cantonId: Number(formulario.cantonId),
            distritoId: Number(formulario.distritoId), otrasSenas: formulario.otrasSenas.trim(),
            telefonos: formulario.telefonos.map(telefono => telefono.trim()) }
        const estudiante = estudiantes.find(item => item.email === estudianteSeleccionado)
        let mensaje = validarExpediente(datos, estudiantes, estudianteSeleccionado)
        if (!mensaje && !cantones.some(item => id(item.cantonId) === id(datos.cantonId) && id(item.provinciaId) === id(datos.provinciaId)))
            mensaje = 'El cantón seleccionado no corresponde a la provincia.'
        if (!mensaje && !distritos.some(item => id(item.distritoId) === id(datos.distritoId) && id(item.cantonId) === id(datos.cantonId)))
            mensaje = 'El distrito seleccionado no corresponde al cantón.'
        if (!mensaje && !provincias.some(item => id(item.provinciaId) === id(datos.provinciaId)))
            mensaje = 'Selecciona una provincia válida.'
        if (!mensaje && !estudiante) mensaje = 'Selecciona una cuenta de estudiante existente.'
        if (mensaje) { setError(mensaje); return }

        setGuardando(true)
        setError('')
        try {
            if (__identificacionOriginal) await api.modificar(__identificacionOriginal, datos)
            else await api.crear(datos)
            setFormulario(null)
            await cargarExpedientes()
            setAviso(__identificacionOriginal ? 'Expediente actualizado correctamente.' : 'Expediente creado correctamente.')
        } catch (e) { setError(e.message) }
        finally { setGuardando(false) }
    }

    async function verDetalle(expediente) {
        setError('')
        try { setDetalle(await api.obtener(expediente.identificacion)) }
        catch (e) { setError(e.message) }
    }

    async function eliminar(expediente) {
        setError('')
        setAviso('')
        try {
            await api.eliminar(expediente.identificacion)
            await cargarExpedientes()
            setAviso('Expediente eliminado correctamente.')
        } catch (e) { setError(e.message) }
    }

    const nombreProvincia = value => provincias.find(item => id(item.provinciaId) === id(value))?.nombre || value || '—'
    const nombreCanton = value => cantones.find(item => id(item.cantonId) === id(value))?.nombre || value || '—'
    const nombreDistrito = value => distritos.find(item => id(item.distritoId) === id(value))?.nombre || value || '—'

    return <div className="instituciones">
        {pendiente && <ConfirmarEliminacion titulo="Eliminar expediente" mensaje={`¿Confirma eliminar el expediente de ${pendiente.nombreCompleto}? Esta operación no se puede deshacer.`}
            confirmar="Eliminar" ocupado={guardando} onCancelar={() => setPendiente(null)}
            onConfirmar={async () => { setGuardando(true); await eliminar(pendiente); setGuardando(false); setPendiente(null) }} />}
        <div className="page-head"><div><h1>Expedientes</h1><p className="subtitle">Gestión administrativa de expedientes estudiantiles</p></div>
            <button type="button" className="btn btn-primary" disabled={cargandoDependencias || !!errorDependencias || estudiantes.length === 0}
                onClick={() => abrir()}>Nuevo expediente</button></div>
        <section className="panel" aria-label="Listado de expedientes">
            <div className="expedientes-filtros"><label className="field" htmlFor="expedientes-busqueda">Buscar por nombre, correo o identificación</label>
                <input id="expedientes-busqueda" value={busqueda} onChange={evento => { setBusqueda(evento.target.value); setPagina(0) }} />
                <button type="button" className="btn btn-soft" onClick={cargarExpedientes}>Actualizar</button></div>
            {error && !formulario && <p role="alert" className="aviso error">{error}</p>}
            {aviso && <p role="status" className="aviso">{aviso}</p>}
            {errorDependencias && <div className="aviso error" role="alert"><p>No se pudieron cargar las cuentas de estudiante o el catálogo de provincias. {errorDependencias}</p>
                <button type="button" className="btn btn-soft" onClick={cargarDependencias}>Reintentar</button></div>}
            {cargando ? <p role="status">Cargando expedientes…</p> : error ? null : filtrados.length === 0
                ? <p role="status">{expedientes.length ? 'No hay expedientes que coincidan con la búsqueda.' : 'No hay expedientes registrados.'}</p>
                : <div className="instituciones-tabla"><table><thead><tr><th>Nombre</th><th>Correo</th><th>Identificación</th><th>Fecha de nacimiento</th><th>Acciones</th></tr></thead>
                    <tbody>{visibles.map(item => <tr key={item.identificacion}><td>{item.nombreCompleto}</td><td>{item.email}</td>
                        <td>{item.tipoIdentificacion} · {item.identificacion}</td><td>{item.fechaNacimiento}</td>
                        <td><RowActionsMenu rowLabel={item.nombreCompleto} actions={[
                            { label: 'Detalle', onSelect: () => verDetalle(item) },
                            { label: 'Editar', onSelect: () => abrir({ ...item, __identificacionOriginal: item.identificacion }) },
                            { label: 'Eliminar', danger: true, onSelect: () => setPendiente(item) }
                        ]} /></td></tr>)}</tbody></table></div>}
            {!cargando && filtrados.length > tamanoPagina && <div className="instituciones-paginas">
                <button type="button" className="btn btn-soft" disabled={paginaActual === 0} onClick={() => setPagina(paginaActual - 1)}>Anterior</button>
                <span>Página {paginaActual + 1} de {paginas}</span>
                <button type="button" className="btn btn-soft" disabled={paginaActual + 1 >= paginas} onClick={() => setPagina(paginaActual + 1)}>Siguiente</button>
            </div>}
        </section>

        {formulario && <Modal titulo={formulario.__identificacionOriginal ? 'Editar expediente' : 'Nuevo expediente'} ocupado={guardando}
            onCerrar={() => { setFormulario(null); setError('') }}>
            {error && <p role="alert" className="aviso error">{error}</p>}
            <form className="expedientes-formulario" onSubmit={guardar}>
                <p className="subtitle">Selecciona la cuenta existente para vincular los datos del estudiante.</p>
                <div className="field"><label htmlFor="expediente-estudiante">Estudiante</label>
                    <select id="expediente-estudiante" required value={estudianteSeleccionado} disabled={guardando || cargandoDependencias || !!errorDependencias}
                        onChange={evento => seleccionarEstudiante(evento.target.value)}>
                        <option value="">Selecciona una cuenta</option>{estudiantes.map(usuario => <option key={usuario.email} value={usuario.email}>
                            {usuario.nombre} — {usuario.email} ({usuario.tipoIdentificacion}: {usuario.identificacion})</option>)}
                    </select>
                    {estudiantes.length === 0 && !cargandoDependencias && !errorDependencias && <small>No hay cuentas con rol Estudiante disponibles.</small>}
                </div>
                <div className="expedientes-form-grid">
                    <div className="field"><label htmlFor="expediente-email">Correo</label><input id="expediente-email" value={formulario.email} readOnly /></div>
                    <div className="field"><label htmlFor="expediente-tipo">Tipo de identificación</label><input id="expediente-tipo" value={formulario.tipoIdentificacion} readOnly /></div>
                    <div className="field"><label htmlFor="expediente-identificacion">Identificación</label><input id="expediente-identificacion" value={formulario.identificacion} readOnly /></div>
                    <div className="field"><label htmlFor="expediente-nombre">Nombre completo</label><input id="expediente-nombre" value={formulario.nombreCompleto} required maxLength={150} disabled={guardando}
                        onChange={evento => cambiarCampo('nombreCompleto', evento.target.value)} /></div>
                    <div className="field"><label htmlFor="expediente-nacimiento">Fecha de nacimiento</label><input id="expediente-nacimiento" type="date" value={formulario.fechaNacimiento || ''} required disabled={guardando}
                        onChange={evento => cambiarCampo('fechaNacimiento', evento.target.value)} /></div>
                    <div className="field"><label htmlFor="expediente-provincia">Provincia</label><select id="expediente-provincia" required value={formulario.provinciaId} disabled={guardando || cargandoDependencias || !!errorDependencias}
                        onChange={evento => cambiarCampo('provinciaId', evento.target.value)}><option value="">Selecciona una provincia</option>
                        {provincias.map(item => <option key={item.provinciaId} value={item.provinciaId}>{item.nombre}</option>)}</select></div>
                    <div className="field"><label htmlFor="expediente-canton">Cantón</label><select id="expediente-canton" required value={formulario.cantonId} disabled={guardando || cargandoCantones || !formulario.provinciaId}
                        onChange={evento => cambiarCampo('cantonId', evento.target.value)}><option value="">Selecciona un cantón</option>
                        {cantones.map(item => <option key={item.cantonId} value={item.cantonId}>{item.nombre}</option>)}</select>
                        {cargandoCantones && <small role="status">Cargando cantones…</small>}{errorCantones && <small role="alert">{errorCantones} <button type="button" onClick={() => setIntentoCantones(valor => valor + 1)}>Reintentar</button></small>}</div>
                    <div className="field"><label htmlFor="expediente-distrito">Distrito</label><select id="expediente-distrito" required value={formulario.distritoId} disabled={guardando || cargandoDistritos || !formulario.cantonId}
                        onChange={evento => cambiarCampo('distritoId', evento.target.value)}><option value="">Selecciona un distrito</option>
                        {distritos.map(item => <option key={item.distritoId} value={item.distritoId}>{item.nombre}</option>)}</select>
                        {cargandoDistritos && <small role="status">Cargando distritos…</small>}{errorDistritos && <small role="alert">{errorDistritos} <button type="button" onClick={() => setIntentoDistritos(valor => valor + 1)}>Reintentar</button></small>}</div>
                    <div className="field"><label htmlFor="expediente-senas">Otras señas</label><textarea id="expediente-senas" value={formulario.otrasSenas} required disabled={guardando}
                        onChange={evento => cambiarCampo('otrasSenas', evento.target.value)} /></div>
                    <div className="field"><label htmlFor="expediente-telefonos">Teléfonos</label>{formulario.telefonos.map((telefono, indice) => <div className="expedientes-telefono" key={indice}>
                        <input id={indice === 0 ? 'expediente-telefonos' : undefined} aria-label={`Teléfono ${indice + 1}`} value={telefono} required disabled={guardando}
                            onChange={evento => setFormulario(actual => ({ ...actual, telefonos: actual.telefonos.map((valor, posicion) => posicion === indice ? evento.target.value : valor) }))} />
                        <button type="button" className="btn btn-soft" disabled={guardando || formulario.telefonos.length < 2}
                            onClick={() => setFormulario(actual => ({ ...actual, telefonos: actual.telefonos.filter((_, posicion) => posicion !== indice) }))}>Quitar</button>
                    </div>)}<button type="button" className="btn btn-soft" disabled={guardando} onClick={() => setFormulario(actual => ({ ...actual, telefonos: [...actual.telefonos, ''] }))}>Agregar teléfono</button></div>
                </div>
                <div className="instituciones-acciones"><button type="submit" className="btn btn-primary" disabled={guardando || cargandoDependencias || !!errorDependencias}>{guardando ? 'Guardando…' : 'Guardar'}</button>
                    <button type="button" className="btn btn-soft" disabled={guardando} onClick={() => { setFormulario(null); setError('') }}>Cancelar</button></div>
            </form>
        </Modal>}

        {detalle && <Modal titulo="Detalle del expediente" onCerrar={() => setDetalle(null)}>
            <dl className="expedientes-detalle">
                <dt>Nombre completo</dt><dd>{detalle.nombreCompleto}</dd><dt>Correo</dt><dd>{detalle.email}</dd>
                <dt>Tipo de identificación</dt><dd>{detalle.tipoIdentificacion}</dd><dt>Identificación</dt><dd>{detalle.identificacion}</dd>
                <dt>Fecha de nacimiento</dt><dd>{detalle.fechaNacimiento}</dd><dt>Provincia</dt><dd>{nombreProvincia(detalle.provinciaId)}</dd>
                <dt>Cantón</dt><dd>{nombreCanton(detalle.cantonId)}</dd><dt>Distrito</dt><dd>{nombreDistrito(detalle.distritoId)}</dd>
                <dt>Otras señas</dt><dd>{detalle.otrasSenas}</dd><dt>Teléfonos</dt><dd>{(detalle.telefonos || []).join(', ') || '—'}</dd>
            </dl><button type="button" className="btn btn-soft" onClick={() => setDetalle(null)}>Cerrar</button>
        </Modal>}
    </div>
}
