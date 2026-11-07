import { useCallback, useEffect, useMemo, useState } from 'react'
import { ConfirmarEliminacion } from '../../components/ConfirmarEliminacion'
import { parametrosApi, validarParametro } from './parametros-api'
import '../Instituciones/instituciones.css'

export function Parametros({ cliente }) {
    const api = useMemo(() => parametrosApi(cliente), [cliente])
    const [registros, setRegistros] = useState([])
    const [tamano, setTamano] = useState(15)
    const [pagina, setPagina] = useState(0)
    const [estado, setEstado] = useState('Cargando parámetros…')
    const [error, setError] = useState('')
    const [aviso, setAviso] = useState('')
    const [formulario, setFormulario] = useState(null)
    const [guardando, setGuardando] = useState(false)
    const [pendiente, setPendiente] = useState(null)

    const cargar = useCallback(async idVisible => {
        setEstado('Cargando parámetros…')
        setError('')
        try {
            const datos = await api.listar()
            if (!Array.isArray(datos)) throw new Error('No se pudo leer la lista de parámetros.')
            setRegistros(datos)
            setPagina(Math.floor(Math.max(0, datos.findIndex(item => item.idParametro === idVisible)) / tamano))
            setEstado(datos.length ? '' : 'No hay parámetros registrados.')
            return true
        } catch (e) {
            setEstado('')
            setError(e.message)
            return false
        }
    }, [api, tamano])

    useEffect(() => {
        let vigente = true
        api.listar().then(datos => {
            if (!vigente) return
            if (!Array.isArray(datos)) throw new Error('No se pudo leer la lista de parámetros.')
            setRegistros(datos)
            setEstado(datos.length ? '' : 'No hay parámetros registrados.')
        }).catch(e => {
            if (!vigente) return
            setEstado('')
            setError(e.message)
        })
        api.tamanoPagina().then(valor => { if (vigente) setTamano(valor) })
        return () => { vigente = false }
    }, [api])

    const totalPaginas = Math.max(1, Math.ceil(registros.length / tamano))
    const paginaActual = Math.min(pagina, totalPaginas - 1)
    const visibles = registros.slice(paginaActual * tamano, (paginaActual + 1) * tamano)

    function abrir(parametro = null) {
        setFormulario(parametro ? { ...parametro, existente: true } : { idParametro: '', valor: '', existente: false })
        setError('')
        setAviso('')
    }

    async function guardar(evento) {
        evento.preventDefault()
        const parametro = { idParametro: formulario.idParametro.trim(), valor: formulario.valor }
        const mensaje = validarParametro(parametro)
        if (mensaje) return setError(mensaje)
        setGuardando(true)
        setError('')
        try {
            if (formulario.existente) await api.modificar(parametro)
            else await api.crear(parametro)
            setFormulario(null)
            if (await cargar(parametro.idParametro)) setAviso('Parámetro guardado correctamente.')
        } catch (e) {
            setError(e.message)
        } finally {
            setGuardando(false)
        }
    }

    async function eliminar(parametro) {
        setError('')
        setAviso('')
        try {
            await api.eliminar(parametro.idParametro)
            if (await cargar()) setAviso('Parámetro eliminado correctamente.')
        } catch (e) {
            setError(e.message)
        }
    }

    return <div className="instituciones">
        {pendiente && <ConfirmarEliminacion onCancelar={() => setPendiente(null)}
            onConfirmar={() => { setPendiente(null); eliminar(pendiente) }} />}
        <div className="page-head"><div><h1>Parámetros</h1><p className="subtitle">Mantenimiento de parámetros del sistema</p></div>
            <button type="button" className="btn btn-primary" onClick={() => abrir()}>Nuevo parámetro</button></div>
        <section className="panel" aria-label="Listado de parámetros" hidden={Boolean(formulario)}>
            {error && <p role="alert" className="aviso error">{error}</p>}
            {aviso && <p role="status" className="aviso">{aviso}</p>}
            {estado && <p role="status">{estado}</p>}
            {!estado && <div className="instituciones-tabla"><table>
                <thead><tr><th>ID</th><th>Valor</th><th>Acciones</th></tr></thead>
                <tbody>{visibles.map(item => <tr key={item.idParametro}><td>{item.idParametro}</td><td>{item.valor}</td>
                    <td><div className="instituciones-acciones">
                        <button type="button" className="btn btn-soft" onClick={() => abrir(item)}>Editar</button>
                        <button type="button" className="btn btn-danger" onClick={() => setPendiente(item)}>Eliminar</button>
                    </div></td></tr>)}</tbody>
            </table></div>}
            {registros.length > tamano && <div className="instituciones-paginas">
                <button type="button" className="btn btn-soft" disabled={paginaActual === 0} onClick={() => setPagina(paginaActual - 1)}>Anterior</button>
                <span>Página {paginaActual + 1} de {totalPaginas}</span>
                <button type="button" className="btn btn-soft" disabled={paginaActual + 1 === totalPaginas} onClick={() => setPagina(paginaActual + 1)}>Siguiente</button>
            </div>}
        </section>
        {formulario && <section className="panel" aria-label={formulario.existente ? 'Editar parámetro' : 'Nuevo parámetro'}>
            {error && <p role="alert" className="aviso error">{error}</p>}
            <h2>{formulario.existente ? 'Editar parámetro' : 'Nuevo parámetro'}</h2>
            <form className="instituciones-formulario" onSubmit={guardar}>
                <div className="field"><label htmlFor="parametro-id">ID</label>
                    <input id="parametro-id" required maxLength={10} disabled={formulario.existente} value={formulario.idParametro}
                        onChange={e => setFormulario({ ...formulario, idParametro: e.target.value.toUpperCase() })} /></div>
                <div className="field"><label htmlFor="parametro-valor">Valor</label>
                    <textarea id="parametro-valor" required maxLength={500} value={formulario.valor}
                        onChange={e => setFormulario({ ...formulario, valor: e.target.value })} /></div>
                <button type="submit" className="btn btn-primary" disabled={guardando}>Guardar</button>
                <button type="button" className="btn btn-soft" onClick={() => setFormulario(null)}>Cancelar</button>
            </form>
        </section>}
    </div>
}
