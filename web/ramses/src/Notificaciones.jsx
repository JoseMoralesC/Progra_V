import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { Modal } from './Modal.jsx';
import { MAX_PAGINA, notificacionesApi, validarNotificacion } from './notificaciones-api.js';
const vacio = { destinatarios: [''], asunto: '', cuerpoHtml: '' };
export function Notificaciones({ cliente }) {
  const api = useMemo(() => notificacionesApi(cliente), [cliente]);
  const [size, setSize] = useState(MAX_PAGINA);
  const [historial, setHistorial] = useState({ items: [], page: 0, size: MAX_PAGINA, total: 0 });
  const [filtros, setFiltros] = useState({ destinatario: '', estado: '' });
  const [modal, setModal] = useState(false), [form, setForm] = useState(vacio), [error, setError] = useState(''), [mensaje, setMensaje] = useState(''), [estadoCarga, setEstadoCarga] = useState('Cargando historial…');
  useEffect(() => { api.tamanoPagina().then(setSize); }, [api]);
  const cargar = useCallback(async (page = 0) => {
    setEstadoCarga('Cargando historial…');
    try {
      const res = await api.historial({ ...filtros, page, size });
      const items = res?.items || [];
      setHistorial({ items, page: res?.page ?? page, size: res?.size ?? size, total: res?.total ?? items.length });
      setEstadoCarga(items.length ? '' : 'No hay notificaciones registradas.');
    } catch (e) { setError(e.message); setEstadoCarga(''); }
  }, [api, filtros, size]);
  useEffect(() => { cargar(0); }, [cargar]);
  async function enviar() {
    const m = validarNotificacion(form); if (m) return setError(m);
    setError(''); setMensaje('');
    try {
      const res = await api.enviar(form);
      setModal(false); setForm(vacio);
      setMensaje(res?.estado === 'ENVIADA_SIN_HISTORIAL'
        ? 'La notificación fue enviada por SMTP, pero no se pudo guardar en el historial. No reenvíes el mensaje.'
        : 'Notificación enviada y registrada en el historial correctamente.');
      await cargar(0);
    } catch (e) {
      setError(e.message);
      await cargar(historial.page ?? 0);
    }
  }
  return <section><div className="page-head"><div><h1>Notificaciones</h1><p>Envía HTML sanitizado y consulta el historial.</p></div><button className="btn btn-primary" onClick={() => { setError(''); setMensaje(''); setModal(true); }}>Nueva notificación</button></div>
    <div className="filters"><label className="field">Destinatario<input value={filtros.destinatario} onChange={e => setFiltros({ ...filtros, destinatario: e.target.value })}/></label><label className="field">Estado<select value={filtros.estado} onChange={e => setFiltros({ ...filtros, estado: e.target.value })}><option value="">Todos</option><option>ENVIADA</option><option>ERROR</option></select></label></div>
    {error && <p role="alert" className="aviso error">{error}</p>}{mensaje && <p role="status" className="aviso">{mensaje}</p>}
    <div className="panel"><table><thead><tr><th>Fecha</th><th>Destinatarios</th><th>Asunto</th><th>Estado</th></tr></thead><tbody>{historial.items.map(x => <tr key={x.id}><td>{x.fecha}</td><td>{x.destinatarios}</td><td>{x.asunto}</td><td>{x.estado}</td></tr>)}</tbody></table>{estadoCarga && <p className="empty" role="status">{estadoCarga}</p>}<div className="acciones"><button className="btn btn-soft" disabled={historial.page === 0} onClick={() => cargar(historial.page - 1)}>Anterior</button><span>Página {historial.page + 1} · {historial.total} registros</span><button className="btn btn-soft" disabled={(historial.page + 1) * historial.size >= historial.total} onClick={() => cargar(historial.page + 1)}>Siguiente</button></div></div>
    {modal && <Modal error={error} titulo="Enviar notificación" onClose={() => setModal(false)} actions={<><button className="btn btn-soft" onClick={() => setModal(false)}>Cancelar</button><button className="btn btn-primary" onClick={enviar}>Confirmar y enviar</button></>}><div className="form-grid"><label className="field">Destinatarios separados por coma<input value={form.destinatarios.join(', ')} onChange={e => setForm({ ...form, destinatarios: e.target.value.split(',') })}/></label><label className="field">Asunto<input value={form.asunto} onChange={e => setForm({ ...form, asunto: e.target.value })}/></label><label className="field">Contenido HTML<textarea value={form.cuerpoHtml} onChange={e => setForm({ ...form, cuerpoHtml: e.target.value })}/></label></div></Modal>}
  </section>;
}
