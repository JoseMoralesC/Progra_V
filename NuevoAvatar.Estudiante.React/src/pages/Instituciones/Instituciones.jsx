import { useCallback, useEffect, useMemo, useState } from 'react'
import { Modal } from '../../components/Modal'
import { ConfirmarEliminacion } from '../../components/ConfirmarEliminacion'
import { institucionesApi, validarNombre } from './instituciones-api'
import './instituciones.css'

export function Instituciones({ cliente }) {
    const api = useMemo(() => institucionesApi(cliente), [cliente])
    const [instituciones, setInstituciones] = useState([])
    const [busqueda, setBusqueda] = useState('')
    const [filtro, setFiltro] = useState('')
    const [tamano, setTamano] = useState(15)
    const [pagina, setPagina] = useState(0)
    const [estado, setEstado] = useState('Cargando instituciones…')
    const [error, setError] = useState('')
    const [aviso, setAviso] = useState('')
    const [edicion, setEdicion] = useState(null)
    const [nombre, setNombre] = useState('')
    const [guardando, setGuardando] = useState(false)
    const [pendiente, setPendiente] = useState(null)

    const cargar = useCallback(async (nombreBuscado, idVisible) => {
        setEstado('Cargando instituciones…')
        setError('')
        try {
            const datos = await api.listar(nombreBuscado)
            if (!Array.isArray(datos)) throw new Error('No se pudo leer la lista de instituciones.')
            setInstituciones(datos)
            setPagina(Math.floor(Math.max(0, datos.findIndex(item => item.id === idVisible)) / tamano))
            setEstado(datos.length ? '' : 'No hay instituciones para mostrar.')
            return true
        } catch (e) {
            setEstado('')
            setError(e.message)
            return false
        }
    }, [api, tamano])

    useEffect(() => {
        let vigente = true
        api.listar('').then(datos => {
            if (!vigente) return
            if (!Array.isArray(datos)) throw new Error('No se pudo leer la lista de instituciones.')
            setInstituciones(datos)
            setEstado(datos.length ? '' : 'No hay instituciones para mostrar.')
        }).catch(e => {
            if (!vigente) return
            setEstado('')
            setError(e.message)
        })
        api.tamanoPagina().then(setTamano)
        return () => { vigente = false }
    }, [api])

    const totalPaginas = Math.max(1, Math.ceil(instituciones.length / tamano))
    const paginaActual = Math.min(pagina, totalPaginas - 1)
    const visibles = instituciones.slice(paginaActual * tamano, (paginaActual + 1) * tamano)

    function buscar(evento) {
        evento.preventDefault()
        const texto = busqueda.trim()
        setFiltro(texto)
        setAviso('')
        cargar(texto)
    }

    function abrir(institucion = null) {
        setEdicion(institucion || { id: null })
        setNombre(institucion?.nombre || '')
        setError('')
        setAviso('')
    }

    async function guardar(evento) {
        evento.preventDefault()
        const mensaje = validarNombre(nombre)
        if (mensaje) return setError(mensaje)
        setGuardando(true)
        setError('')
        try {
            const actual = edicion.id === null ? await api.crear(nombre.trim()) : await api.modificar(edicion.id, nombre.trim())
            setEdicion(null)
            setBusqueda('')
            setFiltro('')
            if (await cargar('', actual.id)) setAviso('Institución guardada correctamente.')
        } catch (e) {
            setError(e.message)
        } finally {
            setGuardando(false)
        }
    }

    async function eliminar(institucion) {
        setError('')
        setAviso('')
        try {
            await api.eliminar(institucion.id)
            if (await cargar(filtro)) setAviso('Institución eliminada correctamente.')
        } catch (e) {
            setError(e.message)
        }
    }

    return <div className="instituciones">
        {pendiente && <ConfirmarEliminacion onCancelar={() => setPendiente(null)}
            onConfirmar={() => { setPendiente(null); eliminar(pendiente) }} />}
        <div className="page-head">
            <div><h1>Instituciones</h1><p className="subtitle">Mantenimiento de instituciones</p></div>
            <button type="button" className="btn btn-primary" onClick={() => abrir()}>Nueva institución</button>
        </div>

        <section className="panel" aria-label="Listado de instituciones">
            <form className="instituciones-busqueda" onSubmit={buscar}>
                <label className="field" htmlFor="instituciones-nombre">Buscar por nombre</label>
                <input id="instituciones-nombre" value={busqueda} onChange={e => setBusqueda(e.target.value)} />
                <button type="submit" className="btn btn-soft">Buscar</button>
            </form>
            {error && !edicion && <p role="alert" className="aviso error">{error}</p>}
            {aviso && <p role="status" className="aviso">{aviso}</p>}
            {estado && <p role="status">{estado}</p>}
            {!estado && <div className="instituciones-tabla">
                <table>
                    <thead><tr><th>Nombre</th><th>Acciones</th></tr></thead>
                    <tbody>{visibles.map(item => <tr key={item.id}>
                        <td>{item.nombre}</td>
                        <td><div className="instituciones-acciones">
                            <button type="button" className="btn btn-soft" onClick={() => abrir(item)}>Editar</button>
                            <button type="button" className="btn btn-danger" onClick={() => setPendiente(item)}>Eliminar</button>
                        </div></td>
                    </tr>)}</tbody>
                </table>
            </div>}
            {instituciones.length > tamano && <div className="instituciones-paginas">
                <button type="button" className="btn btn-soft" disabled={paginaActual === 0} onClick={() => setPagina(paginaActual - 1)}>Anterior</button>
                <span>Página {paginaActual + 1} de {totalPaginas}</span>
                <button type="button" className="btn btn-soft" disabled={paginaActual + 1 === totalPaginas} onClick={() => setPagina(paginaActual + 1)}>Siguiente</button>
            </div>}
        </section>

        {edicion && <Modal titulo={edicion.id === null ? 'Nueva institución' : 'Editar institución'} ocupado={guardando}
            onCerrar={() => { setEdicion(null); setError('') }}>
            {error && <p role="alert" className="aviso error">{error}</p>}
            <form className="instituciones-formulario" onSubmit={guardar}>
                <div className="field"><label htmlFor="institucion-nombre">Nombre</label>
                    <input id="institucion-nombre" value={nombre} maxLength={150} required onChange={e => setNombre(e.target.value)} /></div>
                <button type="submit" className="btn btn-primary" disabled={guardando}>Guardar</button>
                <button type="button" className="btn btn-soft" disabled={guardando} onClick={() => { setEdicion(null); setError('') }}>Cancelar</button>
            </form>
        </Modal>}
    </div>
}
