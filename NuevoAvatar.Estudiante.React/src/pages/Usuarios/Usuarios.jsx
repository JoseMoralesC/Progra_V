import { useCallback, useEffect, useMemo, useState } from 'react'
import { Modal } from '../../components/Modal'
import { ConfirmarEliminacion } from '../../components/ConfirmarEliminacion'
import { RowActionsMenu } from '../../components/RowActionsMenu'
import { usuariosApi, validarUsuario } from './usuarios-api'
import '../Instituciones/instituciones.css'

const filtrosVacios = { identificacion: '', nombre: '', tipo: '', dominio: '', rol: '' }
const usuarioVacio = { email: '', tipoIdentificacion: '', identificacion: '', nombre: '', idRol: '', contrasena: '' }

export function Usuarios({ cliente }) {
    const api = useMemo(() => usuariosApi(cliente), [cliente])
    const [usuarios, setUsuarios] = useState([])
    const [tiposIdentificacion, setTiposIdentificacion] = useState([])
    const [roles, setRoles] = useState([])
    const [filtros, setFiltros] = useState(filtrosVacios)
    const [aplicados, setAplicados] = useState(filtrosVacios)
    const [tamano, setTamano] = useState(15)
    const [pagina, setPagina] = useState(0)
    const [orden, setOrden] = useState('email')
    const [ascendente, setAscendente] = useState(true)
    const [estado, setEstado] = useState('Cargando usuarios…')
    const [error, setError] = useState('')
    const [aviso, setAviso] = useState('')
    const [formulario, setFormulario] = useState(null)
    const [detalle, setDetalle] = useState(null)
    const [guardando, setGuardando] = useState(false)
    const [pendiente, setPendiente] = useState(null)

    const cargar = useCallback(async filtro => {
        setEstado('Cargando usuarios…')
        setError('')
        try {
            const datos = await api.listar(filtro)
            if (!Array.isArray(datos)) throw new Error('No se pudo leer la lista de usuarios.')
            setUsuarios(datos)
            setPagina(0)
            setEstado(datos.length ? '' : 'No hay usuarios para mostrar.')
            return datos
        } catch (e) { setEstado(''); setError(e.message); return false }
    }, [api])

    useEffect(() => {
        let vigente = true
        Promise.all([api.listar(filtrosVacios), api.roles(), api.tamanoPagina()]).then(([datos, listaRoles, tam]) => {
            if (!vigente) return
            if (!Array.isArray(datos) || !Array.isArray(listaRoles)) throw new Error('No se pudieron leer usuarios y roles.')
            setUsuarios(datos)
            setTiposIdentificacion([...new Set(datos
                .map(item => item.tipoIdentificacion?.trim())
                .filter(Boolean))].sort((a, b) => a.localeCompare(b, 'es')))
            setRoles(listaRoles)
            setTamano(tam)
            setEstado(datos.length ? '' : 'No hay usuarios para mostrar.')
        }).catch(e => { if (vigente) { setEstado(''); setError(e.message) } })
        return () => { vigente = false }
    }, [api])

    const filtrados = usuarios.filter(item =>
        (!aplicados.dominio || item.email.toLowerCase().endsWith(`@${aplicados.dominio}`)) &&
        (!aplicados.rol || item.idRol === aplicados.rol))
    function comparar(a, b) {
        const valor = item => String(orden === 'idRol' ? roles.find(rol => rol.idRol === item.idRol)?.nombreRol || item.idRol : item[orden] || '')
        return (ascendente ? 1 : -1) * valor(a).localeCompare(valor(b), 'es', { sensitivity: 'base' })
    }
    const ordenados = [...filtrados].sort(comparar)
    const totalPaginas = Math.max(1, Math.ceil(ordenados.length / tamano))
    const paginaActual = Math.min(pagina, totalPaginas - 1)
    const visibles = ordenados.slice(paginaActual * tamano, (paginaActual + 1) * tamano)
    const nombreRol = id => roles.find(item => item.idRol === id)?.nombreRol || id
    const opcionesTipoIdentificacion = useMemo(() => {
        const opciones = new Set(tiposIdentificacion)
        const valorActual = formulario?.tipoIdentificacion?.trim()
        if (valorActual) opciones.add(valorActual)
        return [...opciones].sort((a, b) => a.localeCompare(b, 'es'))
    }, [formulario?.tipoIdentificacion, tiposIdentificacion])

    async function filtrar(evento) {
        evento.preventDefault()
        setAplicados(filtros)
        setAviso('')
        await cargar(filtros)
    }

    function ordenar(campo) {
        if (orden === campo) setAscendente(actual => !actual)
        else { setOrden(campo); setAscendente(true) }
        setPagina(0)
    }

    function abrir(usuario = null) {
        setDetalle(null)
        setFormulario(usuario ? { ...usuario, contrasena: '', existente: true } : { ...usuarioVacio, existente: false })
        setError('')
        setAviso('')
    }

    async function verDetalle(usuario) {
        setDetalle(null)
        setError('')
        try {
            setDetalle(await api.obtener(usuario.email))
        } catch (e) { setError(e.message) }
    }

    async function guardar(evento) {
        evento.preventDefault()
        const datos = { ...formulario, email: formulario.email.trim().toLowerCase(),
            nombre: formulario.nombre.trim(), identificacion: formulario.identificacion.trim(),
            tipoIdentificacion: formulario.tipoIdentificacion.trim() }
        if (!opcionesTipoIdentificacion.includes(datos.tipoIdentificacion)) {
            setError('Selecciona un tipo de identificación existente.')
            return
        }
        const mensaje = validarUsuario(datos, roles, !formulario.existente)
        if (mensaje) return setError(mensaje)
        setGuardando(true)
        setError('')
        try {
            if (formulario.existente) {
                const { email, tipoIdentificacion, identificacion, nombre, idRol, contrasena } = datos
                await api.modificar(email, { tipoIdentificacion, identificacion, nombre, idRol,
                    ...(contrasena ? { contrasena } : {}) })
            } else {
                const { email, tipoIdentificacion, identificacion, nombre, idRol, contrasena } = datos
                await api.crear({ email, tipoIdentificacion, identificacion, nombre, idRol, contrasena })
            }
            setFormulario(null)
            setDetalle(null)
            setFiltros(filtrosVacios)
            setAplicados(filtrosVacios)
            const lista = await cargar(filtrosVacios)
            if (lista) {
                const posicion = [...lista].sort(comparar).findIndex(item => item.email === datos.email)
                setPagina(Math.floor(Math.max(0, posicion) / tamano))
                setAviso('Usuario guardado correctamente.')
            }
        } catch (e) { setError(e.message) }
        finally { setGuardando(false) }
    }

    async function eliminar(usuario) {
        setError('')
        setAviso('')
        try {
            await api.eliminar(usuario.email)
            if (await cargar(aplicados)) setAviso('Usuario eliminado correctamente.')
        } catch (e) { setError(e.message) }
    }

    return <div className="instituciones">
        {pendiente && <ConfirmarEliminacion onCancelar={() => setPendiente(null)}
            onConfirmar={() => { setPendiente(null); eliminar(pendiente) }} />}
        <div className="page-head"><div><h1>Usuarios</h1><p className="subtitle">Mantenimiento de usuarios</p></div>
            <button type="button" className="btn btn-primary" onClick={() => abrir()}>Nuevo usuario</button></div>
        <section className="panel" aria-label="Listado de usuarios">
            <form className="usuarios-filtros" onSubmit={filtrar}>
                {['identificacion', 'nombre', 'tipo'].map(campo => <div className="field" key={campo}>
                    <label htmlFor={`filtro-${campo}`}>{campo === 'tipo' ? 'Tipo de identificación' : campo[0].toUpperCase() + campo.slice(1)}</label>
                    <input id={`filtro-${campo}`} value={filtros[campo]} onChange={e => setFiltros({ ...filtros, [campo]: e.target.value })} />
                </div>)}
                <div className="field"><label htmlFor="filtro-dominio">Dominio</label><select id="filtro-dominio" value={filtros.dominio} onChange={e => setFiltros({ ...filtros, dominio: e.target.value })}>
                    <option value="">Todos</option><option value="cuc.cr">cuc.cr</option><option value="cuc.ac.cr">cuc.ac.cr</option></select></div>
                <div className="field"><label htmlFor="filtro-rol">Rol</label><select id="filtro-rol" value={filtros.rol} onChange={e => setFiltros({ ...filtros, rol: e.target.value })}>
                    <option value="">Todos</option>{roles.map(item => <option key={item.idRol} value={item.idRol}>{item.nombreRol}</option>)}</select></div>
                <button type="submit" className="btn btn-soft">Filtrar</button>
            </form>
            {error && !formulario && <p role="alert" className="aviso error">{error}</p>}
            {aviso && <p role="status" className="aviso">{aviso}</p>}
            {estado && <p role="status">{estado}</p>}
            {!estado && <div className="instituciones-tabla"><table><thead><tr>
                {[['email', 'Email (ID)'], ['tipoIdentificacion', 'Tipo de identificación'], ['identificacion', 'Identificación'], ['nombre', 'Nombre'], ['idRol', 'Rol']]
                    .map(([campo, titulo]) => <th key={campo} scope="col" aria-sort={orden === campo ? (ascendente ? 'ascending' : 'descending') : 'none'}>
                        <button type="button" className="btn btn-soft" onClick={() => ordenar(campo)}>{titulo}</button>
                    </th>)}
                <th>Acciones</th></tr></thead><tbody>{visibles.map(item => <tr key={item.email}>
                    <td>{item.email}</td><td>{item.tipoIdentificacion}</td><td>{item.identificacion}</td><td>{item.nombre}</td><td>{nombreRol(item.idRol)}</td>
                    <td><RowActionsMenu rowLabel={item.email} actions={[
                        { label: 'Detalle', onSelect: () => verDetalle(item) },
                        { label: 'Editar', onSelect: () => abrir(item) },
                        { label: 'Eliminar', danger: true, onSelect: () => setPendiente(item) }
                    ]} /></td>
                </tr>)}</tbody></table></div>}
            {filtrados.length > tamano && <div className="instituciones-paginas">
                <button type="button" className="btn btn-soft" disabled={paginaActual === 0} onClick={() => setPagina(paginaActual - 1)}>Anterior</button>
                <span>Página {paginaActual + 1} de {totalPaginas}</span>
                <button type="button" className="btn btn-soft" disabled={paginaActual + 1 === totalPaginas} onClick={() => setPagina(paginaActual + 1)}>Siguiente</button>
            </div>}
        </section>
        {detalle && <Modal titulo="Detalle de usuario" onCerrar={() => setDetalle(null)}><p>{detalle.email}</p>
            <p>{detalle.nombre} · {nombreRol(detalle.idRol)}</p>
            <p>{detalle.tipoIdentificacion}: {detalle.identificacion}</p>
            <p>Última modificación: {detalle.fechaModificacion ? new Date(detalle.fechaModificacion).toLocaleString('es-CR') : 'Sin fecha registrada'}</p>
            <button type="button" className="btn btn-soft" onClick={() => setDetalle(null)}>Cerrar</button></Modal>}
        {formulario && <Modal titulo={formulario.existente ? 'Editar usuario' : 'Nuevo usuario'} ocupado={guardando}
            onCerrar={() => { setFormulario(null); setError('') }}>
            {error && <p role="alert" className="aviso error">{error}</p>}
            <form className="usuarios-filtros" onSubmit={guardar}>
                {[
                    ['email', 'Correo', 'email']
                ].map(([campo, titulo, tipo]) => <div className="field" key={campo}><label htmlFor={`usuario-${campo}`}>{titulo}</label>
                    <input id={`usuario-${campo}`} type={tipo} required value={formulario[campo]}
                        disabled={campo === 'email' && formulario.existente}
                        onChange={e => setFormulario({ ...formulario, [campo]: e.target.value })} /></div>)}
                <div className="field"><label htmlFor="usuario-tipo-identificacion">Tipo de identificación</label>
                    <select id="usuario-tipo-identificacion" required value={formulario.tipoIdentificacion}
                        onChange={e => setFormulario({ ...formulario, tipoIdentificacion: e.target.value })}>
                        <option value="">Seleccione</option>
                        {opcionesTipoIdentificacion.map(tipo => <option key={tipo} value={tipo}>{tipo}</option>)}
                    </select>
                    {!opcionesTipoIdentificacion.length && <p className="helper" role="status">No hay tipos registrados para seleccionar.</p>}
                </div>
                {[
                    ['identificacion', 'Identificación', 'text'], ['nombre', 'Nombre', 'text']
                ].map(([campo, titulo, tipo]) => <div className="field" key={campo}><label htmlFor={`usuario-${campo}`}>{titulo}</label>
                    <input id={`usuario-${campo}`} type={tipo} required value={formulario[campo]}
                        disabled={campo === 'email' && formulario.existente}
                        onChange={e => setFormulario({ ...formulario, [campo]: e.target.value })} /></div>)}
                <div className="field"><label htmlFor="usuario-rol">Rol</label><select id="usuario-rol" required value={formulario.idRol}
                    onChange={e => setFormulario({ ...formulario, idRol: e.target.value })}>
                    <option value="">Seleccione</option>{roles.map(item => <option key={item.idRol} value={item.idRol}>{item.nombreRol}</option>)}</select></div>
                <div className="field"><label htmlFor="usuario-contrasena">Contraseña</label><input id="usuario-contrasena" type="password"
                    required={!formulario.existente} value={formulario.contrasena}
                    onChange={e => setFormulario({ ...formulario, contrasena: e.target.value })} /></div>
                <button type="submit" className="btn btn-primary" disabled={guardando}>Guardar</button>
                <button type="button" className="btn btn-soft" disabled={guardando} onClick={() => { setFormulario(null); setError('') }}>Cancelar</button>
            </form>
        </Modal>}
    </div>
}
