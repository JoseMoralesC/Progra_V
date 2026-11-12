import { useCallback, useEffect, useMemo, useState } from 'react'
import { Modal } from '../../components/Modal'
import { ConfirmarEliminacion } from '../../components/ConfirmarEliminacion'
import { rolesApi, validarRol } from './roles-api'
import '../Instituciones/instituciones.css'

export function Roles({ cliente, onPermisosActualizados }) {
    const api = useMemo(() => rolesApi(cliente), [cliente])
    const [roles, setRoles] = useState([])
    const [modulos, setModulos] = useState([])
    const [seleccion, setSeleccion] = useState({})
    const [permisosGuardados, setPermisosGuardados] = useState({})
    const [permisosAbiertos, setPermisosAbiertos] = useState(false)
    const [estado, setEstado] = useState('Cargando roles y permisos…')
    const [error, setError] = useState('')
    const [aviso, setAviso] = useState('')
    const [formulario, setFormulario] = useState(null)
    const [guardando, setGuardando] = useState(false)
    const [pendiente, setPendiente] = useState(null)
    const [tamano, setTamano] = useState(15)
    const [pagina, setPagina] = useState(0)
    const totalPaginas = Math.max(1, Math.ceil(roles.length / tamano))
    const paginaActual = Math.min(pagina, totalPaginas - 1)
    const visibles = roles.slice(paginaActual * tamano, (paginaActual + 1) * tamano)

    const cargar = useCallback(async idVisible => {
        setError('')
        try {
            const [listaRoles, listaModulos] = await Promise.all([api.listar(), api.modulos()])
            if (!Array.isArray(listaRoles) || !Array.isArray(listaModulos)) throw new Error('No se pudieron leer roles y módulos.')
            const asignaciones = await Promise.all(listaModulos.map(item => api.asignados(item.idModulo)))
            if (asignaciones.some(item => !Array.isArray(item))) throw new Error('No se pudieron leer los permisos.')
            setRoles(listaRoles)
            setPagina(Math.floor(Math.max(0, listaRoles.findIndex(item => item.idRol === idVisible)) / tamano))
            setModulos(listaModulos)
            const permisos = Object.fromEntries(listaModulos.map((item, indice) => [item.idModulo, asignaciones[indice]]))
            setSeleccion(permisos)
            setPermisosGuardados(permisos)
            setEstado('')
            return true
        } catch (e) {
            setEstado('')
            setError(e.message)
            return false
        }
    }, [api, tamano])

    useEffect(() => {
        let vigente = true
        api.tamanoPagina().then(valor => { if (vigente) setTamano(valor) })
        Promise.all([api.listar(), api.modulos()]).then(async ([listaRoles, listaModulos]) => {
            if (!Array.isArray(listaRoles) || !Array.isArray(listaModulos)) throw new Error('No se pudieron leer roles y módulos.')
            const asignaciones = await Promise.all(listaModulos.map(item => api.asignados(item.idModulo)))
            if (!vigente) return
            if (asignaciones.some(item => !Array.isArray(item))) throw new Error('No se pudieron leer los permisos.')
            setRoles(listaRoles)
            setModulos(listaModulos)
            const permisos = Object.fromEntries(listaModulos.map((item, indice) => [item.idModulo, asignaciones[indice]]))
            setSeleccion(permisos)
            setPermisosGuardados(permisos)
            setEstado('')
        }).catch(e => { if (vigente) { setEstado(''); setError(e.message) } })
        return () => { vigente = false }
    }, [api])

    function abrir(rol = null) {
        setFormulario(rol ? { ...rol, existente: true } : { idRol: '', nombreRol: '', existente: false })
        setError('')
        setAviso('')
    }

    async function guardarRol(evento) {
        evento.preventDefault()
        const datos = { idRol: formulario.idRol.trim().toUpperCase(), nombreRol: formulario.nombreRol.trim() }
        const mensaje = validarRol(datos, roles)
        if (mensaje) return setError(mensaje)
        setGuardando(true)
        setError('')
        try {
            if (formulario.existente) await api.modificar(datos)
            else await api.crear(datos)
            setFormulario(null)
            if (await cargar(datos.idRol)) setAviso('Rol guardado correctamente.')
        } catch (e) { setError(e.message) }
        finally { setGuardando(false) }
    }

    async function eliminarRol(rol) {
        setError('')
        setAviso('')
        try {
            await api.eliminar(rol.idRol)
            if (await cargar()) setAviso('Rol eliminado correctamente.')
        } catch (e) { setError(e.message) }
    }

    function abrirPermisos() {
        setSeleccion(permisosGuardados)
        setError('')
        setAviso('')
        setPermisosAbiertos(true)
    }

    function cerrarPermisos() {
        setSeleccion(permisosGuardados)
        setError('')
        setAviso('')
        setPermisosAbiertos(false)
    }

    function cambiar(idModulo, idRol, marcado) {
        setSeleccion(actual => ({ ...actual, [idModulo]: marcado
            ? [...actual[idModulo], idRol]
            : actual[idModulo].filter(id => id !== idRol) }))
        setAviso('')
    }

    async function guardarPermisos(idModulo) {
        setError('')
        setAviso('')
        setGuardando(true)
        try {
            await api.guardarAsignados(idModulo, seleccion[idModulo])
            setPermisosGuardados(actual => ({ ...actual, [idModulo]: seleccion[idModulo] }))
            const [actualizados] = await Promise.all([api.asignados(idModulo),
                onPermisosActualizados ? onPermisosActualizados() : api.menuPropio()])
            setSeleccion(actual => ({ ...actual, [idModulo]: actualizados }))
            setPermisosGuardados(actual => ({ ...actual, [idModulo]: actualizados }))
            setAviso(`Permisos de ${idModulo} guardados.`)
        } catch (e) { setError(e.message) }
        finally { setGuardando(false) }
    }

    return <div className="instituciones">
        {pendiente && <ConfirmarEliminacion onCancelar={() => setPendiente(null)}
            onConfirmar={() => { setPendiente(null); eliminarRol(pendiente) }} />}
        <div className="page-head"><div><h1>Roles y permisos</h1><p className="subtitle">Mantenimiento de roles y acceso a módulos</p></div>
            <div className="instituciones-acciones">
                <button type="button" className="btn btn-soft" disabled={Boolean(estado)} onClick={abrirPermisos}>Permisos de menú</button>
                <button type="button" className="btn btn-primary" onClick={() => abrir()}>Nuevo rol</button>
            </div></div>
        {error && !formulario && !permisosAbiertos && <p role="alert" className="aviso error">{error}</p>}
        {aviso && !permisosAbiertos && <p role="status" className="aviso">{aviso}</p>}
        {estado && <p role="status">{estado}</p>}
        {!estado && <section className="panel" aria-label="Listado de roles">
            <div className="instituciones-tabla"><table><thead><tr><th>ID</th><th>Nombre</th><th>Acciones</th></tr></thead>
                <tbody>{visibles.map(item => <tr key={item.idRol}><td>{item.idRol}</td><td>{item.nombreRol}</td><td>
                    <div className="instituciones-acciones"><button type="button" className="btn btn-soft" onClick={() => abrir(item)}>Editar</button>
                    <button type="button" className="btn btn-danger" onClick={() => setPendiente(item)}>Eliminar</button></div>
                </td></tr>)}</tbody></table></div>
            {!roles.length && <p>No hay roles registrados.</p>}
            {roles.length > tamano && <div className="instituciones-paginas">
                <button type="button" disabled={paginaActual === 0} onClick={() => setPagina(paginaActual - 1)}>Anterior</button>
                <span>Página {paginaActual + 1} de {totalPaginas}</span>
                <button type="button" disabled={paginaActual + 1 === totalPaginas} onClick={() => setPagina(paginaActual + 1)}>Siguiente</button>
            </div>}
        </section>}
        {formulario && <Modal titulo={formulario.existente ? 'Editar rol' : 'Nuevo rol'} ocupado={guardando}
            onCerrar={() => { setFormulario(null); setError('') }}>
            {error && <p role="alert" className="aviso error">{error}</p>}
            <form className="instituciones-formulario" onSubmit={guardarRol}>
                <div className="field"><label htmlFor="rol-id">ID</label><input id="rol-id" value={formulario.idRol}
                    disabled={formulario.existente} required onChange={e => setFormulario({ ...formulario, idRol: e.target.value.toUpperCase() })} /></div>
                <div className="field"><label htmlFor="rol-nombre">Nombre</label><input id="rol-nombre" value={formulario.nombreRol}
                    required onChange={e => setFormulario({ ...formulario, nombreRol: e.target.value })} /></div>
                <button type="submit" className="btn btn-primary" disabled={guardando}>Guardar</button>
                <button type="button" className="btn btn-soft" disabled={guardando} onClick={() => { setFormulario(null); setError('') }}>Cancelar</button>
            </form>
        </Modal>}
        {permisosAbiertos && <Modal titulo="Permisos de menú" ocupado={guardando} onCerrar={cerrarPermisos}>
            {error && <p role="alert" className="aviso error">{error}</p>}
            {aviso && <p role="status" className="aviso">{aviso}</p>}
            <div className="instituciones-tabla"><table><thead><tr><th>Módulo</th>{roles.map(item => <th key={item.idRol}>{item.nombreRol}</th>)}<th>Guardar</th></tr></thead>
                <tbody>{modulos.map(modulo => <tr key={modulo.idModulo}>
                    <td>{modulo.nombreModulo} ({modulo.idModulo})</td>
                    {roles.map(rol => <td key={rol.idRol}><label>
                        <input type="checkbox" aria-label={`${modulo.idModulo} ${rol.nombreRol}`}
                            disabled={guardando}
                            checked={(seleccion[modulo.idModulo] || []).includes(rol.idRol)}
                            onChange={e => cambiar(modulo.idModulo, rol.idRol, e.target.checked)} />
                    </label></td>)}
                    <td><button type="button" className="btn btn-soft" disabled={guardando}
                        onClick={() => guardarPermisos(modulo.idModulo)}>Guardar {modulo.idModulo}</button></td>
                </tr>)}</tbody></table></div>
            <button type="button" className="btn btn-soft" disabled={guardando} onClick={cerrarPermisos}>Cerrar</button>
        </Modal>}
    </div>
}
