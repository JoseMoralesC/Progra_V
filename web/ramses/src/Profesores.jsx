import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { Modal } from './Modal.jsx';
import { RowActionsMenu } from '../../../NuevoAvatar.Estudiante.React/src/components/RowActionsMenu.jsx';
import { MAX_PAGINA, profesoresApi, validarProfesor } from './profesores-api.js';
const vacio = { tipoIdentificacion: 'Cédula', identificacion: '', email: '', nombreCompleto: '', fechaNacimiento: '', telefonos: [''] };
export function Profesores({ cliente }) {
  const api = useMemo(() => profesoresApi(cliente), [cliente]);
  const filtroUsado = useRef(false);
  const [size, setSize] = useState(MAX_PAGINA);
  const [dominioProfesor, setDominioProfesor] = useState('');
  const [data, setData] = useState({ items: [], page: 0, size: MAX_PAGINA, total: 0 }), [texto, setTexto] = useState(''), [sort, setSort] = useState('nombreCompleto'), [direction, setDirection] = useState('asc');
  const [modal, setModal] = useState(null), [form, setForm] = useState(vacio), [error, setError] = useState(''), [mensaje, setMensaje] = useState(''), [estado, setEstado] = useState('Cargando profesores…');
  useEffect(() => {
    api.tamanoPagina().then(setSize);
    api.dominio().then(setDominioProfesor).catch(() => setDominioProfesor(''));
  }, [api]);
  const cargar = useCallback(async (page = 0, carga = false) => { setEstado('Cargando profesores…'); try { const result = await api.buscar({ texto, page, size, sort, direction, carga }); setData(result); setEstado(result.items.length ? '' : 'No hay profesores para mostrar.'); } catch (e) { setError(e.message); setEstado(''); } }, [api, texto, size, sort, direction]);
  useEffect(() => { cargar(0, !filtroUsado.current); }, [cargar]);
  const abrir = item => { setError(''); setMensaje(''); setForm(item ? { ...item, telefonos: [...item.telefonos] } : { ...vacio, telefonos: [''] }); setModal(item ? 'editar' : 'crear'); };
  async function guardar() {
    const err = validarProfesor(form, dominioProfesor); if (err) return setError(err);
    try {
      modal === 'crear' ? await api.crear(form) : await api.modificar(form);
      setMensaje(modal === 'crear' ? 'Profesor creado correctamente.' : 'Profesor actualizado correctamente.');
      setModal(null); filtroUsado.current = false; setTexto(form.identificacion.trim());
      setData(await api.buscar({ texto: form.identificacion.trim(), page: 0, size, sort, direction, carga: true })); setEstado('');
    } catch (e) { setError(e.message); }
  }
  async function eliminar() { try { await api.eliminar(form.id); setMensaje('Profesor eliminado correctamente.'); setModal(null); await cargar(0, true); } catch (e) { setError(e.message); } }
  return <section><div className="page-head"><div><p className="crumb">Administración / Académico</p><h1>Mantenimiento de profesores</h1><p>Consulta, ordena y administra docentes y teléfonos.</p></div><button className="btn btn-primary" onClick={() => abrir()}>Nuevo profesor</button></div>
    <div className="filters"><label className="field">Buscar<input value={texto} onChange={e => { filtroUsado.current = true; setTexto(e.target.value); }} placeholder="Nombre, identificación o correo"/></label><label className="field">Orden<select value={sort} onChange={e => { filtroUsado.current = true; setSort(e.target.value); }}><option value="nombreCompleto">Nombre</option><option value="identificacion">Identificación</option><option value="email">Correo</option><option value="fechaNacimiento">Nacimiento</option></select></label><label className="field">Dirección<select value={direction} onChange={e => { filtroUsado.current = true; setDirection(e.target.value); }}><option value="asc">Ascendente</option><option value="desc">Descendente</option></select></label></div>
    {error && <p className="aviso error" role="alert">{error}</p>}{mensaje && <p className="aviso" role="status">{mensaje}</p>}<div className="panel"><table><thead><tr><th>Nombre</th><th>Identificación</th><th>Correo</th><th>Teléfonos</th><th>Acciones</th></tr></thead><tbody>{data.items.map(item => <tr key={item.id}><td>{item.nombreCompleto}</td><td>{item.tipoIdentificacion}: {item.identificacion}</td><td>{item.email}</td><td>{item.telefonos.join(', ')}</td><td><RowActionsMenu rowLabel={item.nombreCompleto} actions={[{label:'Editar',onSelect:()=>abrir(item)},{label:'Eliminar',danger:true,onSelect:()=>{setError('');setMensaje('');setForm(item);setModal('eliminar')}}]} /></td></tr>)}</tbody></table>{estado && <p className="empty" role="status">{estado}</p>}<div className="acciones"><button className="btn btn-soft" disabled={data.page === 0} onClick={() => cargar(data.page - 1)}>Anterior</button><span>Página {data.page + 1} · {data.total} registros</span><button className="btn btn-soft" disabled={(data.page + 1) * data.size >= data.total} onClick={() => cargar(data.page + 1)}>Siguiente</button></div></div>
    {(modal === 'crear' || modal === 'editar') && <Modal error={error} titulo={modal === 'crear' ? 'Crear profesor' : 'Editar profesor'} onClose={() => setModal(null)} actions={<><button className="btn btn-soft" onClick={() => setModal(null)}>Cancelar</button><button className="btn btn-primary" onClick={guardar}>Guardar</button></>}><div className="form-grid"><label className="field">Tipo<input value={form.tipoIdentificacion} onChange={e => setForm({ ...form, tipoIdentificacion: e.target.value })}/></label><label className="field">Identificación<input value={form.identificacion} onChange={e => setForm({ ...form, identificacion: e.target.value })}/></label><label className="field">Nombre<input value={form.nombreCompleto} onChange={e => setForm({ ...form, nombreCompleto: e.target.value })}/></label><label className="field">Correo<input type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })}/></label><label className="field">Nacimiento<input type="date" value={form.fechaNacimiento} onChange={e => setForm({ ...form, fechaNacimiento: e.target.value })}/></label><label className="field">Teléfono (solo 8 dígitos)<input inputMode="numeric" maxLength="8" value={form.telefonos[0] || ''} onChange={e => setForm({ ...form, telefonos: [e.target.value.replace(/\D/g, '')] })}/></label></div></Modal>}
    {modal === 'eliminar' && <Modal error={error} titulo="Confirmar eliminación" onClose={() => setModal(null)} actions={<><button className="btn btn-soft" onClick={() => setModal(null)}>No, cancelar</button><button className="btn btn-danger" onClick={eliminar}>Sí, eliminar</button></>}><p>¿Realmente desea eliminar el elemento seleccionado? Se eliminará a <strong>{form.nombreCompleto}</strong>. Los grupos activos impedirán la operación.</p></Modal>}
  </section>;
}
