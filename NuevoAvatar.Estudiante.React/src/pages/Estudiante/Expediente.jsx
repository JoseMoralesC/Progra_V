import { useEffect, useState } from 'react'
import { useAuth } from '../../context/AuthContext'
import { useNotifications } from '../../context/NotificationContext'
import { Modal } from '../../components/Modal'
import {
    actualizarExpedientePropio,
    obtenerCantones,
    obtenerDistritos,
    obtenerExpedientePropio,
    obtenerProvincias
} from '../../services/api'

const formularioVacio = {
    telefonos: [],
    provinciaId: '',
    cantonId: '',
    distritoId: '',
    otrasSenas: ''
}

const catalogoInactivo = { clave: '', estado: 'idle', error: '' }

function textoError(error, accion) {
    if (error?.status === 401) return 'Tu sesión expiró. Inicia sesión nuevamente.'
    if (error?.status === 403) return 'Tu sesión no tiene permiso para consultar este expediente.'
    if (error?.status === 404) return 'No encontramos un expediente asociado a tu sesión.'
    if (error?.status === 400) return 'Revisa los teléfonos y que provincia, cantón y distrito correspondan entre sí.'
    if (error?.status === 409) return 'No se pudo confirmar un vínculo único entre tu sesión y el expediente.'
    if (error?.status === 503) return 'El servicio no está disponible en este momento. Inténtalo más tarde.'
    return `No fue posible ${accion}. Inténtalo nuevamente.`
}

function crearFormulario(expediente) {
    return {
        telefonos: Array.isArray(expediente?.telefonos) ? [...expediente.telefonos] : [],
        provinciaId: String(expediente?.provinciaId ?? ''),
        cantonId: String(expediente?.cantonId ?? ''),
        distritoId: String(expediente?.distritoId ?? ''),
        otrasSenas: expediente?.otrasSenas ?? ''
    }
}

function CampoLectura({ etiqueta, valor }) {
    return (
        <div className="field">
            <span className="helper">{etiqueta}</span>
            <p>{valor || 'No disponible'}</p>
        </div>
    )
}

export default function Expediente() {
    const { authorizedFetch } = useAuth()
    const { notify } = useNotifications()
    const [expediente, setExpediente] = useState(null)
    const [formulario, setFormulario] = useState(formularioVacio)
    const [provincias, setProvincias] = useState([])
    const [cantones, setCantones] = useState([])
    const [distritos, setDistritos] = useState([])
    const [estadoProvincias, setEstadoProvincias] = useState('loading')
    const [errorProvincias, setErrorProvincias] = useState('')
    const [estadoCantones, setEstadoCantones] = useState(catalogoInactivo)
    const [estadoDistritos, setEstadoDistritos] = useState(catalogoInactivo)
    const [cargando, setCargando] = useState(true)
    const [editando, setEditando] = useState(false)
    const [guardando, setGuardando] = useState(false)
    const [cambiosPendientes, setCambiosPendientes] = useState(null)
    const [errorGuardado, setErrorGuardado] = useState('')
    const [errorCarga, setErrorCarga] = useState('')
    const [errores, setErrores] = useState({})
    const [intentoCarga, setIntentoCarga] = useState(0)
    const [intentoProvincias, setIntentoProvincias] = useState(0)
    const [intentoCantones, setIntentoCantones] = useState(0)
    const [intentoDistritos, setIntentoDistritos] = useState(0)

    useEffect(() => {
        let vigente = true
        authorizedFetch(token => obtenerExpedientePropio(token)).then(datos => {
            if (!vigente) return
            setExpediente(datos)
            setFormulario(crearFormulario(datos))
        }).catch(error => {
            if (!vigente) return
            const mensaje = textoError(error, 'cargar tu expediente')
            setErrorCarga(mensaje)
            notify(mensaje, 'error')
        }).finally(() => {
            if (vigente) setCargando(false)
        })
        return () => { vigente = false }
    }, [authorizedFetch, intentoCarga, notify])

    useEffect(() => {
        let vigente = true
        authorizedFetch(token => obtenerProvincias(token)).then(datos => {
            if (!vigente) return
            if (!Array.isArray(datos)) throw new Error('El servicio devolvió una lista de provincias inválida.')
            setProvincias(datos)
            setEstadoProvincias('success')
        }).catch(error => {
            if (!vigente) return
            const mensaje = textoError(error, 'cargar las provincias')
            setErrorProvincias(mensaje)
            setEstadoProvincias('error')
            notify(mensaje, 'error')
        })
        return () => { vigente = false }
    }, [authorizedFetch, intentoProvincias, notify])

    useEffect(() => {
        const provinciaId = formulario.provinciaId
        if (!provinciaId) {
            return undefined
        }
        let vigente = true
        authorizedFetch(token => obtenerCantones(provinciaId, token))
            .then(datos => {
                if (!vigente) return
                if (!Array.isArray(datos)) throw new Error('El servicio devolvió una lista de cantones inválida.')
                setCantones(datos)
                setEstadoCantones({ clave: provinciaId, estado: 'success', error: '' })
            })
            .catch(error => {
                if (!vigente) return
                const mensaje = textoError(error, 'cargar los cantones')
                setEstadoCantones({ clave: provinciaId, estado: 'error', error: mensaje })
                notify(mensaje, 'error')
            })
        return () => { vigente = false }
    }, [authorizedFetch, formulario.provinciaId, intentoCantones, notify])

    useEffect(() => {
        const provinciaId = formulario.provinciaId
        const cantonId = formulario.cantonId
        const claveTerritorial = provinciaId && cantonId ? `${provinciaId}:${cantonId}` : ''
        if (!claveTerritorial) {
            return undefined
        }
        let vigente = true
        authorizedFetch(token => obtenerDistritos(provinciaId, cantonId, token))
            .then(datos => {
                if (!vigente) return
                if (!Array.isArray(datos)) throw new Error('El servicio devolvió una lista de distritos inválida.')
                setDistritos(datos)
                setEstadoDistritos({ clave: claveTerritorial, estado: 'success', error: '' })
            })
            .catch(error => {
                if (!vigente) return
                const mensaje = textoError(error, 'cargar los distritos')
                setEstadoDistritos({ clave: claveTerritorial, estado: 'error', error: mensaje })
                notify(mensaje, 'error')
            })
        return () => { vigente = false }
    }, [authorizedFetch, formulario.cantonId, formulario.provinciaId, intentoDistritos, notify])

    const claveProvinciaActual = formulario.provinciaId
    const claveTerritorial = `${formulario.provinciaId}:${formulario.cantonId}`
    const estadoCantonesActual = estadoCantones.clave === claveProvinciaActual
        ? estadoCantones.estado : formulario.provinciaId ? 'loading' : 'idle'
    const estadoDistritosActual = estadoDistritos.clave === claveTerritorial
        ? estadoDistritos.estado : formulario.provinciaId && formulario.cantonId ? 'loading' : 'idle'
    const cargandoCantones = estadoCantonesActual === 'loading'
    const cargandoDistritos = estadoDistritosActual === 'loading'
    const cantonesActuales = estadoCantones.clave === formulario.provinciaId ? cantones : []
    const distritosActuales = estadoDistritos.clave === claveTerritorial ? distritos : []

    const provinciaActual = provincias.find(item =>
        String(item.provinciaId) === String(expediente?.provinciaId))
    const cantonActual = cantonesActuales.find(item =>
        String(item.cantonId) === String(expediente?.cantonId))
    const distritoActual = distritosActuales.find(item =>
        String(item.distritoId) === String(expediente?.distritoId))

    function cambiarTelefono(indice, valor) {
        setFormulario(actual => ({
            ...actual,
            telefonos: actual.telefonos.map((telefono, posicion) => posicion === indice ? valor : telefono)
        }))
    }

    function cambiarProvincia(evento) {
        setCantones([])
        setDistritos([])
        setEstadoCantones(catalogoInactivo)
        setEstadoDistritos(catalogoInactivo)
        setFormulario(actual => ({ ...actual, provinciaId: evento.target.value, cantonId: '', distritoId: '' }))
        setErrores(actual => ({ ...actual, provinciaId: '', cantonId: '', distritoId: '' }))
    }

    function cambiarCanton(evento) {
        setDistritos([])
        setEstadoDistritos(catalogoInactivo)
        setFormulario(actual => ({ ...actual, cantonId: evento.target.value, distritoId: '' }))
        setErrores(actual => ({ ...actual, cantonId: '', distritoId: '' }))
    }

    function iniciarEdicion() {
        setFormulario(crearFormulario(expediente))
        setErrores({})
        setErrorGuardado('')
        setEditando(true)
    }

    function cancelarEdicion() {
        setFormulario(crearFormulario(expediente))
        setCantones([])
        setDistritos([])
        setEstadoCantones(catalogoInactivo)
        setEstadoDistritos(catalogoInactivo)
        setErrores({})
        setCambiosPendientes(null)
        setErrorGuardado('')
        setEditando(false)
    }

    function reintentarCarga() {
        setErrorCarga('')
        setCargando(true)
        setIntentoCarga(valor => valor + 1)
    }

    function reintentarProvincias() {
        setEstadoProvincias('loading')
        setErrorProvincias('')
        setIntentoProvincias(valor => valor + 1)
    }

    function reintentarCantones() {
        setEstadoCantones({ clave: formulario.provinciaId, estado: 'loading', error: '' })
        setCantones([])
        setIntentoCantones(valor => valor + 1)
    }

    function reintentarDistritos() {
        setEstadoDistritos({ clave: claveTerritorial, estado: 'loading', error: '' })
        setDistritos([])
        setIntentoDistritos(valor => valor + 1)
    }

    async function guardar(evento) {
        evento.preventDefault()
        if (guardando) return
        const telefonos = formulario.telefonos.map(telefono => telefono.trim())
        const nuevosErrores = {}
        if (telefonos.length === 0 || telefonos.some(telefono => !telefono)) {
            nuevosErrores.telefonos = 'Ingresa al menos un teléfono y completa todos los campos.'
        }
        if (!formulario.provinciaId) nuevosErrores.provinciaId = 'Selecciona una provincia.'
        if (!formulario.cantonId) nuevosErrores.cantonId = 'Selecciona un cantón.'
        if (!formulario.distritoId) nuevosErrores.distritoId = 'Selecciona un distrito.'
        if (!formulario.otrasSenas.trim()) nuevosErrores.otrasSenas = 'Ingresa otras señas.'
        if (!cantonesActuales.some(item => String(item.cantonId) === formulario.cantonId &&
            String(item.provinciaId) === formulario.provinciaId)) {
            nuevosErrores.cantonId = 'El cantón no corresponde a la provincia seleccionada.'
        }
        if (!distritosActuales.some(item => String(item.distritoId) === formulario.distritoId &&
            String(item.cantonId) === formulario.cantonId)) {
            nuevosErrores.distritoId = 'El distrito no corresponde al cantón seleccionado.'
        }
        setErrores(nuevosErrores)
        if (Object.keys(nuevosErrores).length) return

        setCambiosPendientes({
            telefonos,
            provinciaId: Number(formulario.provinciaId),
            cantonId: Number(formulario.cantonId),
            distritoId: Number(formulario.distritoId),
            otrasSenas: formulario.otrasSenas.trim()
        })
    }

    async function confirmarGuardado() {
        if (guardando || !cambiosPendientes) return
        setGuardando(true)
        try {
            const actualizado = await authorizedFetch(token => actualizarExpedientePropio(cambiosPendientes, token))
            setExpediente(actualizado)
            setFormulario(crearFormulario(actualizado))
            setEditando(false)
            setCambiosPendientes(null)
            setErrores({})
            notify('Tu expediente se guardó correctamente.', 'success')
        } catch (error) {
            const mensaje = textoError(error, 'guardar los cambios del expediente')
            setErrorGuardado(mensaje)
            notify(mensaje, 'error')
        } finally {
            setGuardando(false)
        }
    }

    if (cargando) return <section className="panel" role="status">Cargando tu expediente…</section>
    if (errorCarga) {
        return (
            <section className="panel" role="alert">
                <h2>No se pudo cargar Mi expediente</h2>
                <p>{errorCarga}</p>
                <button className="btn btn-soft" type="button" onClick={reintentarCarga}>
                    Reintentar
                </button>
            </section>
        )
    }
    if (!expediente) return null

    return (
        <>
            <section className="page-head">
                <div>
                    <h2>Mi expediente</h2>
                    <p className="subtitle">Consulta tus datos y actualiza tus teléfonos y dirección.</p>
                </div>
                <button className="btn btn-primary" type="button" onClick={iniciarEdicion}>Editar contacto y dirección</button>
            </section>

            <section className="panel" aria-labelledby="datos-personales-title">
                <h2 id="datos-personales-title">Datos personales</h2>
                <div className="form-grid" style={{ marginTop: 18 }}>
                    <CampoLectura etiqueta="Nombre completo" valor={expediente.nombreCompleto} />
                    <CampoLectura etiqueta="Correo electrónico" valor={expediente.email} />
                    <CampoLectura etiqueta="Tipo de identificación" valor={expediente.tipoIdentificacion} />
                    <CampoLectura etiqueta="Identificación" valor={expediente.identificacion} />
                    <CampoLectura etiqueta="Fecha de nacimiento" valor={expediente.fechaNacimiento} />
                </div>
            </section>

            <section className="panel" aria-labelledby="contacto-direccion-title">
                <div className="section-heading">
                    <div>
                        <h2 id="contacto-direccion-title">Teléfonos y dirección</h2>
                    </div>
                </div>
                <div className="form-grid" style={{ marginTop: 18 }}>
                    <div className="field"><span className="helper">Teléfonos</span>
                        {(expediente.telefonos || []).length
                            ? <ul>{expediente.telefonos.map((telefono, indice) => <li key={`${telefono}-${indice}`}>{telefono}</li>)}</ul>
                            : <p>No hay teléfonos registrados.</p>}</div>
                    <CampoLectura etiqueta="Provincia" valor={provinciaActual?.nombre} />
                    <CampoLectura etiqueta="Cantón" valor={cantonActual?.nombre} />
                    <CampoLectura etiqueta="Distrito" valor={distritoActual?.nombre} />
                    <CampoLectura etiqueta="Otras señas" valor={expediente.otrasSenas} />
                </div>
                {estadoProvincias === 'error' && <div className="status status-danger" role="alert"><p>{errorProvincias}</p>
                    <button className="btn btn-soft" type="button" onClick={reintentarProvincias}>Reintentar provincias</button></div>}
                {estadoCantonesActual === 'error' && <div className="status status-danger" role="alert"><p>{estadoCantones.error}</p>
                    <button className="btn btn-soft" type="button" onClick={reintentarCantones}>Reintentar cantones</button></div>}
                {estadoDistritosActual === 'error' && <div className="status status-danger" role="alert"><p>{estadoDistritos.error}</p>
                    <button className="btn btn-soft" type="button" onClick={reintentarDistritos}>Reintentar distritos</button></div>}
            </section>

            {editando && <Modal titulo="Editar contacto y dirección" ocupado={guardando} onCerrar={cancelarEdicion}>
                <form className="expediente-modal-form" onSubmit={guardar}>
                    <p className="subtitle">Solo puedes actualizar teléfonos y dirección.</p>
                    <div className="form-grid">
                        <div className="field expediente-modal-telefonos"><label htmlFor="expediente-telefonos">Teléfonos</label>
                            {formulario.telefonos.map((telefono, indice) => <div className="expediente-telefono" key={`telefono-${indice}`}>
                                <input id={indice === 0 ? 'expediente-telefonos' : `expediente-telefono-${indice}`}
                                    aria-label={`Teléfono ${indice + 1}`} value={telefono} disabled={guardando}
                                    onChange={evento => cambiarTelefono(indice, evento.target.value)} />
                                <button className="btn btn-soft" type="button" disabled={guardando || formulario.telefonos.length < 2}
                                    onClick={() => setFormulario(actual => ({ ...actual, telefonos: actual.telefonos.filter((_, posicion) => posicion !== indice) }))}>Quitar</button>
                            </div>)}
                            <button className="btn btn-soft" type="button" disabled={guardando}
                                onClick={() => setFormulario(actual => ({ ...actual, telefonos: [...actual.telefonos, ''] }))}>Agregar teléfono</button>
                            {errores.telefonos && <p className="status status-danger" role="alert">{errores.telefonos}</p>}
                        </div>
                        {[
                            ['provinciaId', 'Provincia', provincias, 'provinciaId', cambiarProvincia, estadoProvincias === 'loading'],
                            ['cantonId', 'Cantón', cantonesActuales, 'cantonId', cambiarCanton, cargandoCantones],
                            ['distritoId', 'Distrito', distritosActuales, 'distritoId', evento => setFormulario(actual => ({ ...actual, distritoId: evento.target.value })), cargandoDistritos]
                        ].map(([campo, etiqueta, opciones, clave, alCambiar, cargandoOpciones]) => (
                            <div className="field" key={campo}>
                                <label htmlFor={`expediente-${campo}`}>{etiqueta}</label>
                                <select id={`expediente-${campo}`} value={formulario[campo]} onChange={alCambiar}
                                    disabled={guardando || cargandoOpciones || (campo !== 'provinciaId' && !opciones.length)}>
                                    <option value="">Selecciona {etiqueta.toLocaleLowerCase('es')}</option>
                                    {opciones.map(opcion => <option key={opcion[clave]} value={opcion[clave]}>{opcion.nombre}</option>)}
                                </select>
                                {campo === 'provinciaId' && estadoProvincias === 'error' && <div className="status status-danger" role="alert">
                                    <p>{errorProvincias}</p><button className="btn btn-soft" type="button" onClick={reintentarProvincias}>Reintentar provincias</button></div>}
                                {campo === 'provinciaId' && estadoProvincias === 'loading' && <p className="helper" role="status">Cargando provincias…</p>}
                                {campo === 'cantonId' && estadoCantonesActual === 'error' && <div className="status status-danger" role="alert">
                                    <p>{estadoCantones.error}</p><button className="btn btn-soft" type="button" onClick={reintentarCantones}>Reintentar cantones</button></div>}
                                {campo === 'cantonId' && cargandoCantones && <p className="helper" role="status">Cargando cantones…</p>}
                                {campo === 'distritoId' && estadoDistritosActual === 'error' && <div className="status status-danger" role="alert">
                                    <p>{estadoDistritos.error}</p><button className="btn btn-soft" type="button" onClick={reintentarDistritos}>Reintentar distritos</button></div>}
                                {campo === 'distritoId' && cargandoDistritos && <p className="helper" role="status">Cargando distritos…</p>}
                                {campo === 'cantonId' && estadoCantonesActual === 'success' && !opciones.length && <p className="helper">No hay cantones disponibles para esta provincia.</p>}
                                {campo === 'distritoId' && estadoDistritosActual === 'success' && !opciones.length && <p className="helper">No hay distritos disponibles para este cantón.</p>}
                                {errores[campo] && <p className="status status-danger" role="alert">{errores[campo]}</p>}
                            </div>
                        ))}
                        <div className="field" style={{ gridColumn: '1 / -1' }}><label htmlFor="expediente-otras-senas">Otras señas</label>
                            <textarea id="expediente-otras-senas" value={formulario.otrasSenas} disabled={guardando}
                                onChange={evento => setFormulario(actual => ({ ...actual, otrasSenas: evento.target.value }))} />
                            {errores.otrasSenas && <p className="status status-danger" role="alert">{errores.otrasSenas}</p>}
                        </div>
                    </div>
                    <div className="profile-actions"><button className="btn btn-primary" type="submit"
                        disabled={guardando || cargandoCantones || cargandoDistritos || estadoProvincias !== 'success'}>Guardar cambios</button>
                        <button className="btn btn-soft" type="button" disabled={guardando} onClick={cancelarEdicion}>Cancelar</button></div>
                </form>
            </Modal>}
            {cambiosPendientes && (
                <Modal titulo="Confirmar actualización del expediente" ocupado={guardando}
                    onCerrar={() => { if (!guardando) { setCambiosPendientes(null); setErrorGuardado('') } }}>
                    <p>Se guardarán tus teléfonos y dirección. Los datos personales permanecerán sin cambios.</p>
                    {errorGuardado && <p className="status status-danger" role="alert">{errorGuardado}</p>}
                    <div className="profile-actions">
                        <button className="btn btn-primary" type="button" onClick={confirmarGuardado} disabled={guardando}>
                            {guardando ? 'Guardando…' : 'Confirmar y guardar'}
                        </button>
                        <button className="btn btn-soft" type="button" onClick={() => { setCambiosPendientes(null); setErrorGuardado('') }} disabled={guardando}>
                            Volver al formulario
                        </button>
                    </div>
                </Modal>
            )}
        </>
    )
}
