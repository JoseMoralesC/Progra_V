import { useEffect, useMemo, useState } from 'react'
import { useAuth } from '../../context/AuthContext'
import { useNotifications } from '../../context/NotificationContext'
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
    const [cargando, setCargando] = useState(true)
    const [cantonesCargadosPara, setCantonesCargadosPara] = useState('')
    const [distritosCargadosPara, setDistritosCargadosPara] = useState('')
    const [editando, setEditando] = useState(false)
    const [guardando, setGuardando] = useState(false)
    const [errorCarga, setErrorCarga] = useState('')
    const [errores, setErrores] = useState({})
    const [intentoCarga, setIntentoCarga] = useState(0)

    useEffect(() => {
        let vigente = true
        Promise.all([
            authorizedFetch(token => obtenerExpedientePropio(token)),
            authorizedFetch(token => obtenerProvincias(token))
        ]).then(([datos, listaProvincias]) => {
            if (!vigente) return
            setExpediente(datos)
            setFormulario(crearFormulario(datos))
            setProvincias(Array.isArray(listaProvincias) ? listaProvincias : [])
            setErrorCarga('')
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
        if (!formulario.provinciaId) return undefined
        let vigente = true
        authorizedFetch(token => obtenerCantones(formulario.provinciaId, token))
            .then(datos => {
                if (!vigente) return
                setCantones(Array.isArray(datos) ? datos : [])
                setCantonesCargadosPara(formulario.provinciaId)
            })
            .catch(error => {
                if (!vigente) return
                setCantones([])
                setCantonesCargadosPara(formulario.provinciaId)
                notify(textoError(error, 'cargar los cantones'), 'error')
            })
        return () => { vigente = false }
    }, [authorizedFetch, formulario.provinciaId, notify])

    useEffect(() => {
        if (!formulario.provinciaId || !formulario.cantonId) return undefined
        let vigente = true
        const claveTerritorial = `${formulario.provinciaId}:${formulario.cantonId}`
        authorizedFetch(token => obtenerDistritos(formulario.provinciaId, formulario.cantonId, token))
            .then(datos => {
                if (!vigente) return
                setDistritos(Array.isArray(datos) ? datos : [])
                setDistritosCargadosPara(claveTerritorial)
            })
            .catch(error => {
                if (!vigente) return
                setDistritos([])
                setDistritosCargadosPara(claveTerritorial)
                notify(textoError(error, 'cargar los distritos'), 'error')
            })
        return () => { vigente = false }
    }, [authorizedFetch, formulario.cantonId, formulario.provinciaId, notify])

    const provinciaActual = useMemo(() => provincias.find(item =>
        String(item.provinciaId) === String(expediente?.provinciaId)), [expediente, provincias])
    const cantonActual = useMemo(() => cantones.find(item =>
        String(item.cantonId) === String(expediente?.cantonId)), [cantones, expediente])
    const distritoActual = useMemo(() => distritos.find(item =>
        String(item.distritoId) === String(expediente?.distritoId)), [distritos, expediente])

    function cambiarTelefono(indice, valor) {
        setFormulario(actual => ({
            ...actual,
            telefonos: actual.telefonos.map((telefono, posicion) => posicion === indice ? valor : telefono)
        }))
    }

    const cargandoCantones = Boolean(formulario.provinciaId) && cantonesCargadosPara !== formulario.provinciaId
    const claveTerritorial = `${formulario.provinciaId}:${formulario.cantonId}`
    const cargandoDistritos = Boolean(formulario.provinciaId && formulario.cantonId) &&
        distritosCargadosPara !== claveTerritorial

    function cambiarProvincia(evento) {
        setCantones([])
        setDistritos([])
        setFormulario(actual => ({ ...actual, provinciaId: evento.target.value, cantonId: '', distritoId: '' }))
        setErrores(actual => ({ ...actual, provinciaId: '', cantonId: '', distritoId: '' }))
    }

    function cambiarCanton(evento) {
        setDistritos([])
        setFormulario(actual => ({ ...actual, cantonId: evento.target.value, distritoId: '' }))
        setErrores(actual => ({ ...actual, cantonId: '', distritoId: '' }))
    }

    function iniciarEdicion() {
        setFormulario(crearFormulario(expediente))
        setErrores({})
        setEditando(true)
    }

    function cancelarEdicion() {
        setFormulario(crearFormulario(expediente))
        setErrores({})
        setEditando(false)
    }

    function reintentarCarga() {
        setErrorCarga('')
        setCargando(true)
        setIntentoCarga(valor => valor + 1)
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
        if (!cantones.some(item => String(item.cantonId) === formulario.cantonId &&
            String(item.provinciaId) === formulario.provinciaId)) {
            nuevosErrores.cantonId = 'El cantón no corresponde a la provincia seleccionada.'
        }
        if (!distritos.some(item => String(item.distritoId) === formulario.distritoId &&
            String(item.cantonId) === formulario.cantonId)) {
            nuevosErrores.distritoId = 'El distrito no corresponde al cantón seleccionado.'
        }
        setErrores(nuevosErrores)
        if (Object.keys(nuevosErrores).length) return

        setGuardando(true)
        try {
            const actualizado = await authorizedFetch(token => actualizarExpedientePropio({
                telefonos,
                provinciaId: Number(formulario.provinciaId),
                cantonId: Number(formulario.cantonId),
                distritoId: Number(formulario.distritoId),
                otrasSenas: formulario.otrasSenas.trim()
            }, token))
            setExpediente(actualizado)
            setFormulario(crearFormulario(actualizado))
            setEditando(false)
            setErrores({})
            notify('Tu expediente se guardó correctamente.', 'success')
        } catch (error) {
            notify(textoError(error, 'guardar los cambios del expediente'), 'error')
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
                {!editando && (
                    <button className="btn btn-primary" type="button" onClick={iniciarEdicion}>
                        Editar contacto y dirección
                    </button>
                )}
            </section>

            <section className="panel" aria-labelledby="datos-personales-title">
                <h2 id="datos-personales-title">Datos personales · solo lectura</h2>
                <div className="form-grid" style={{ marginTop: 18 }}>
                    <CampoLectura etiqueta="Nombre completo" valor={expediente.nombreCompleto} />
                    <CampoLectura etiqueta="Correo electrónico" valor={expediente.email} />
                    <CampoLectura etiqueta="Tipo de identificación" valor={expediente.tipoIdentificacion} />
                    <CampoLectura etiqueta="Identificación" valor={expediente.identificacion} />
                    <CampoLectura etiqueta="Fecha de nacimiento" valor={expediente.fechaNacimiento} />
                </div>
            </section>

            <form className="panel" onSubmit={guardar} aria-labelledby="contacto-direccion-title">
                <div className="section-heading">
                    <div>
                        <h2 id="contacto-direccion-title">Teléfonos y dirección</h2>
                        <p className="subtitle">Estos son los únicos datos que puedes editar.</p>
                    </div>
                    {!editando && <span className="status status-success">Solo lectura</span>}
                </div>

                <div className="form-grid" style={{ marginTop: 18 }}>
                    <div className="field">
                        {editando
                            ? <label htmlFor="expediente-telefonos">Teléfonos</label>
                            : <span className="helper">Teléfonos</span>}
                        {editando ? (
                            <>
                                {formulario.telefonos.map((telefono, indice) => (
                                    <div key={`telefono-${indice}`} style={{ display: 'flex', gap: 8, marginBottom: 8 }}>
                                        <input
                                            id={indice === 0 ? 'expediente-telefonos' : `expediente-telefono-${indice}`}
                                            aria-label={`Teléfono ${indice + 1}`}
                                            value={telefono}
                                            disabled={guardando}
                                            onChange={evento => cambiarTelefono(indice, evento.target.value)}
                                        />
                                        <button className="btn btn-soft" type="button" disabled={guardando || formulario.telefonos.length < 2}
                                            onClick={() => setFormulario(actual => ({ ...actual,
                                                telefonos: actual.telefonos.filter((_, posicion) => posicion !== indice) }))}>
                                            Quitar
                                        </button>
                                    </div>
                                ))}
                                <button className="btn btn-soft" type="button" disabled={guardando}
                                    onClick={() => setFormulario(actual => ({ ...actual, telefonos: [...actual.telefonos, ''] }))}>
                                    Agregar teléfono
                                </button>
                            </>
                        ) : (
                            (expediente.telefonos || []).length
                                ? <ul>{expediente.telefonos.map((telefono, indice) => <li key={`${telefono}-${indice}`}>{telefono}</li>)}</ul>
                                : <p>No hay teléfonos registrados.</p>
                        )}
                        {errores.telefonos && <p className="status status-danger" role="alert">{errores.telefonos}</p>}
                    </div>

                    {[
                        ['provinciaId', 'Provincia', provincias, 'provinciaId', cambiarProvincia, cargandoCantones],
                        ['cantonId', 'Cantón', cantones, 'cantonId', cambiarCanton, cargandoCantones],
                        ['distritoId', 'Distrito', distritos, 'distritoId', evento => setFormulario(actual => ({ ...actual, distritoId: evento.target.value })), cargandoDistritos]
                    ].map(([campo, etiqueta, opciones, clave, alCambiar, cargandoOpciones]) => {
                        const valor = editando ? formulario[campo] : String(expediente[campo] ?? '')
                        const actual = campo === 'provinciaId' ? provinciaActual
                            : campo === 'cantonId' ? cantonActual : distritoActual
                        return (
                            <div className="field" key={campo}>
                                <label htmlFor={`expediente-${campo}`}>{etiqueta}</label>
                                {editando ? (
                                    <select id={`expediente-${campo}`} value={valor} onChange={alCambiar}
                                        disabled={guardando || cargandoOpciones || (campo !== 'provinciaId' && !opciones.length)}>
                                        <option value="">Selecciona {etiqueta.toLocaleLowerCase('es')}</option>
                                        {opciones.map(opcion => (
                                            <option key={opcion[clave]} value={opcion[clave]}>{opcion.nombre}</option>
                                        ))}
                                    </select>
                                ) : <p>{actual?.nombre || 'No disponible'}</p>}
                                {errores[campo] && <p className="status status-danger" role="alert">{errores[campo]}</p>}
                            </div>
                        )
                    })}

                    <div className="field" style={{ gridColumn: '1 / -1' }}>
                        <label htmlFor="expediente-otras-senas">Otras señas</label>
                        {editando ? (
                            <textarea id="expediente-otras-senas" value={formulario.otrasSenas} disabled={guardando}
                                onChange={evento => setFormulario(actual => ({ ...actual, otrasSenas: evento.target.value }))} />
                        ) : <p>{expediente.otrasSenas}</p>}
                        {errores.otrasSenas && <p className="status status-danger" role="alert">{errores.otrasSenas}</p>}
                    </div>
                </div>

                {editando && (
                    <div className="profile-actions">
                        <button className="btn btn-primary" type="submit" disabled={guardando || cargandoCantones || cargandoDistritos}>
                            {guardando ? 'Guardando…' : 'Guardar cambios'}
                        </button>
                        <button className="btn btn-soft" type="button" disabled={guardando} onClick={cancelarEdicion}>
                            Cancelar
                        </button>
                    </div>
                )}
            </form>
        </>
    )
}
