import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { Modal } from './Modal.jsx';
import { RowActionsMenu } from '../../../NuevoAvatar.Estudiante.React/src/components/RowActionsMenu.jsx';
import { MAX_PAGINA, paginar, periodosApi, validarPeriodo } from './periodos-api.js';
const vacio = { anio: new Date().getFullYear(), numeroPeriodo: 1, fechaInicio: '', fechaFin: '' };
export function Periodos({ cliente }) {
  const api = useMemo(() => periodosApi(cliente), [cliente]);
  const [items, setItems] = useState([]), [filtros, setFiltros] = useState({ anio: '', estado: '' });
  const [page, setPage] = useState(0), [size, setSize] = useState(MAX_PAGINA);
  const [modal, setModal] = useState(null), [form, setForm] = useState(vacio), [error, setError] = useState(''), [mensaje, setMensaje] = useState(''), [estado, setEstado] = useState('Cargando periodos…');
  useEffect(() => { api.tamanoPagina().then(setSize); }, [api]);
  const cargar = useCallback(async () => {
    setEstado('Cargando periodos…');
    try { const data = await api.listar(filtros); setItems(data); setEstado(data.length ? '' : 'No hay periodos para mostrar.'); }
    catch (e) { setError(e.message); setEstado(''); }
  }, [api, filtros]);
  useEffect(() => { cargar(); }, [cargar]);
  const pagina = useMemo(() => paginar(items, page, size), [items, page, size]);
  const abrir = x => { setError(''); setMensaje(''); setForm(x ? { ...x, fechaInicio: x.fechaInicio.slice(0, 10), fechaFin: x.fechaFin.slice(0, 10) } : { ...vacio }); setModal(x ? 'editar' : 'crear'); };
  async function guardar() {
    const m = validarPeriodo(form); if (m) return setError(m);
    const v = { ...form, anio: Number(form.anio), numeroPeriodo: Number(form.numeroPeriodo) };
    try {
      const guardado = modal === 'crear' ? await api.crear(v) : await api.modificar(v);
      setMensaje(modal === 'crear' ? 'Periodo creado correctamente.' : 'Periodo actualizado correctamente.');
      setModal(null); setFiltros({ anio: '', estado: '' });
      const lista = await api.listar(); setItems(lista); setEstado('');
      setPage(Math.floor(Math.max(0, lista.findIndex(x => x.periodoId === (guardado?.periodoId ?? form.periodoId))) / size));
    }
    catch (e) { setError(e.message); }
  }
  async function eliminar() { try { await api.eliminar(form.periodoId); setMensaje('Periodo eliminado correctamente.'); setModal(null); await cargar(); } catch (e) { setError(e.message); } }
  return <section><div className="page-head"><div><h1>Mantenimiento de periodos</h1><p>Fechas, solapamientos y estado actual.</p></div><button className="btn btn-primary" onClick={() => abrir()}>Nuevo periodo</button></div>
    <div className="filters"><label className="field">Año<input type="number" value={filtros.anio} onChange={e => { setPage(0); setFiltros({ ...filtros, anio: e.target.value }); }}/></label><label className="field">Estado<select value={filtros.estado} onChange={e => { setPage(0); setFiltros({ ...filtros, estado: e.target.value }); }}><option value="">Todos</option><option>FUTURO</option><option>ACTIVO</option><option>CERRADO</option></select></label></div>
    {error && <p role="alert" className="aviso error">{error}</p>}{mensaje && <p role="status" className="aviso">{mensaje}</p>}
    <div className="panel"><table><thead><tr><th>Año</th><th>Número</th><th>Inicio</th><th>Fin</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>{pagina.items.map(x => <tr key={x.periodoId}><td>{x.anio}</td><td>{x.numeroPeriodo}</td><td>{x.fechaInicio.slice(0, 10)}</td><td>{x.fechaFin.slice(0, 10)}</td><td><span className="status">{x.estado}</span></td><td><RowActionsMenu rowLabel={`Período ${x.numeroPeriodo} de ${x.anio}`} actions={[{label:'Editar',onSelect:()=>abrir(x)},{label:'Eliminar',danger:true,onSelect:()=>{setError('');setMensaje('');setForm(x);setModal('eliminar')}}]} /></td></tr>)}</tbody></table>{estado && <p className="empty" role="status">{estado}</p>}<div className="acciones"><button className="btn btn-soft" disabled={pagina.page === 0} onClick={() => setPage(pagina.page - 1)}>Anterior</button><span>Página {pagina.page + 1} · {pagina.total} registros</span><button className="btn btn-soft" disabled={(pagina.page + 1) * pagina.size >= pagina.total} onClick={() => setPage(pagina.page + 1)}>Siguiente</button></div></div>
    {(modal === 'crear' || modal === 'editar') && <Modal error={error} titulo={modal === 'crear' ? 'Crear periodo' : 'Editar periodo'} onClose={() => setModal(null)} actions={<><button className="btn btn-soft" onClick={() => setModal(null)}>Cancelar</button><button className="btn btn-primary" onClick={guardar}>Guardar</button></>}><div className="form-grid"><label className="field">Año<input type="number" value={form.anio} onChange={e => setForm({ ...form, anio: e.target.value })}/></label><label className="field">Número<input type="number" min="1" value={form.numeroPeriodo} onChange={e => setForm({ ...form, numeroPeriodo: e.target.value })}/></label><label className="field">Inicio<input type="date" value={form.fechaInicio} onChange={e => setForm({ ...form, fechaInicio: e.target.value })}/></label><label className="field">Fin<input type="date" value={form.fechaFin} onChange={e => setForm({ ...form, fechaFin: e.target.value })}/></label></div></Modal>}
    {modal === 'eliminar' && <Modal error={error} titulo="Confirmar eliminación" onClose={() => setModal(null)} actions={<><button className="btn btn-soft" onClick={() => setModal(null)}>No, cancelar</button><button className="btn btn-danger" onClick={eliminar}>Sí, eliminar</button></>}><p>¿Realmente desea eliminar el elemento seleccionado? Se eliminará el periodo <strong>{form.anio}-{form.numeroPeriodo}</strong>.</p></Modal>}
  </section>;
}
