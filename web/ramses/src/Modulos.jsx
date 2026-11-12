import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { Modal } from './Modal.jsx';
import { RowActionsMenu } from '../../../NuevoAvatar.Estudiante.React/src/components/RowActionsMenu.jsx';
import { MAX_PAGINA, moduloApi, paginar, validarModulo } from './modulos-api.js';
const vacio = { idModulo: '', nombreModulo: '', activo: true, orden: 0 };
export function Modulos({ cliente }) {
  const api = useMemo(() => moduloApi(cliente), [cliente]);
  const [items, setItems] = useState([]), [roles, setRoles] = useState([]), [modal, setModal] = useState(null);
  const [page, setPage] = useState(0), [size, setSize] = useState(MAX_PAGINA);
  const [form, setForm] = useState(vacio), [seleccion, setSeleccion] = useState([]), [estado, setEstado] = useState('Cargando módulos…'), [error, setError] = useState(''), [mensaje, setMensaje] = useState('');
  const cargar = useCallback(async () => { setEstado('Cargando módulos…'); try { const data = await api.listar(); setItems(data); setEstado(data.length ? '' : 'No hay módulos registrados.'); } catch (e) { setEstado(''); setError(e.message); } }, [api]);
  useEffect(() => { cargar(); api.listarRoles().then(setRoles).catch(e => setError(e.message)); api.tamanoPagina().then(setSize); }, [cargar, api]);
  const pagina = useMemo(() => paginar(items, page, size), [items, page, size]);
  const abrirForm = item => { setError(''); setMensaje(''); setForm(item ? { ...item } : { ...vacio }); setModal(item ? 'editar' : 'crear'); };
  async function guardar() {
    const err = validarModulo(form); if (err) return setError(err);
    try {
      modal === 'crear' ? await api.crear(form) : await api.modificar(form);
      setMensaje(modal === 'crear' ? 'Módulo creado correctamente.' : 'Módulo actualizado correctamente.');
      setModal(null);
      const lista = await api.listar(); setItems(lista); setEstado('');
      setPage(Math.floor(Math.max(0, lista.findIndex(x => x.idModulo === form.idModulo.trim().toUpperCase())) / size));
    } catch (e) { setError(e.message); }
  }
  async function abrirRoles(item) { try { setError(''); setMensaje(''); setForm(item); setSeleccion(await api.roles(item.idModulo)); setModal('roles'); } catch (e) { setError(e.message); } }
  async function guardarRoles() { try { await api.guardarRoles(form.idModulo, seleccion); setMensaje('Roles del módulo actualizados correctamente.'); setModal(null); } catch (e) { setError(e.message); } }
  async function eliminar() { try { await api.eliminar(form.idModulo); setMensaje('Módulo eliminado correctamente.'); setModal(null); await cargar(); } catch (e) { setError(e.message); } }
  return <section aria-labelledby="titulo-modulos">
    <div className="page-head"><div><p className="crumb">Administración / Seguridad</p><h1 id="titulo-modulos">Mantenimiento de módulos</h1><p>Define el orden, estado y acceso por rol del menú.</p></div><button className="btn btn-primary" onClick={() => abrirForm()}>Nuevo módulo</button></div>
    {error && <p className="aviso error" role="alert">{error}</p>}
    {mensaje && <p className="aviso" role="status">{mensaje}</p>}
    <div className="panel"><table><thead><tr><th>Identificador</th><th>Nombre</th><th>Orden</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>{pagina.items.map(item => <tr key={item.idModulo}><td>{item.idModulo}</td><td>{item.nombreModulo}</td><td>{item.orden}</td><td><span className="status">{item.activo ? 'Activo' : 'Inactivo'}</span></td><td><RowActionsMenu rowLabel={item.nombreModulo} actions={[{label:'Editar',onSelect:()=>abrirForm(item)},{label:'Roles',onSelect:()=>abrirRoles(item)},{label:'Eliminar',danger:true,onSelect:()=>{setError('');setMensaje('');setForm(item);setModal('eliminar')}}]} /></td></tr>)}</tbody></table>{estado && <p className="empty" role="status">{estado}</p>}<div className="acciones"><button className="btn btn-soft" disabled={pagina.page === 0} onClick={() => setPage(pagina.page - 1)}>Anterior</button><span>Página {pagina.page + 1} · {pagina.total} registros</span><button className="btn btn-soft" disabled={(pagina.page + 1) * pagina.size >= pagina.total} onClick={() => setPage(pagina.page + 1)}>Siguiente</button></div></div>
    {(modal === 'crear' || modal === 'editar') && <Modal error={error} titulo={modal === 'crear' ? 'Crear módulo' : 'Editar módulo'} onClose={() => setModal(null)} actions={<><button className="btn btn-soft" onClick={() => setModal(null)}>Cancelar</button><button className="btn btn-primary" onClick={guardar}>Guardar</button></>}><div className="form-grid"><label className="field">Identificador<input autoFocus value={form.idModulo} disabled={modal === 'editar'} maxLength="20" onChange={e => setForm({ ...form, idModulo: e.target.value })}/></label><label className="field">Nombre<input value={form.nombreModulo} maxLength="100" onChange={e => setForm({ ...form, nombreModulo: e.target.value })}/></label><label className="field">Orden<input type="number" min="0" value={form.orden} onChange={e => setForm({ ...form, orden: e.target.value })}/></label><label className="check"><input type="checkbox" checked={form.activo} onChange={e => setForm({ ...form, activo: e.target.checked })}/> Módulo activo</label></div></Modal>}
    {modal === 'roles' && <Modal error={error} titulo={`Roles de ${form.nombreModulo}`} onClose={() => setModal(null)} actions={<><button className="btn btn-soft" onClick={() => setModal(null)}>Cancelar</button><button className="btn btn-primary" onClick={guardarRoles}>Guardar asignaciones</button></>}><fieldset><legend>Roles con acceso</legend>{roles.map(rol => <label className="check" key={rol.idRol}><input type="checkbox" checked={seleccion.includes(rol.idRol)} onChange={e => setSeleccion(e.target.checked ? [...seleccion, rol.idRol] : seleccion.filter(x => x !== rol.idRol))}/>{rol.nombreRol} ({rol.idRol})</label>)}</fieldset></Modal>}
    {modal === 'eliminar' && <Modal error={error} titulo="Confirmar eliminación" onClose={() => setModal(null)} actions={<><button className="btn btn-soft" onClick={() => setModal(null)}>No, cancelar</button><button className="btn btn-danger" onClick={eliminar}>Sí, eliminar</button></>}><p>¿Realmente desea eliminar el elemento seleccionado? Se eliminará el módulo <strong>{form.nombreModulo}</strong> y sus asignaciones de roles.</p></Modal>}
  </section>;
}
