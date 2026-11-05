import React, { useEffect, useRef, useState } from 'react';

const raiz = '/gateway/mat/';
export function Notas({ cliente }) {
  const [grupos, setGrupos] = useState([]), [grupo, setGrupo] = useState('');
  const [rubros, setRubros] = useState([]), [alumnos, setAlumnos] = useState([]), [filtro, setFiltro] = useState('');
  const [edicion, setEdicion] = useState([{ nombre: '', porcentaje: '' }]);
  const [loading, setLoading] = useState(true), [saving, setSaving] = useState(false), [mensaje, setMensaje] = useState('');
  const [error, setError] = useState(''), [dirty, setDirty] = useState(false), [ready, setReady] = useState(false);
  const revision = useRef(0);
  async function cargar(id) {
    const version = ++revision.current;
    setLoading(true); setReady(false); setError(''); setRubros([]); setAlumnos([]); setDirty(false);
    setEdicion([{ nombre: '', porcentaje: '' }]);
    if (!id) { setLoading(false); return; }
    try {
      const [desglose, estudiantes] = await Promise.all([
        cliente.request(raiz + 'obtenerdesglose?grupoId=' + id), cliente.request(raiz + 'obtenernotasgrupo?grupoId=' + id)]);
      if (version !== revision.current) return;
      setRubros(desglose); setAlumnos(estudiantes);
      setEdicion(desglose.length ? desglose.map(r => ({ nombre: r.nombre, porcentaje: r.porcentaje })) : [{ nombre: '', porcentaje: '' }]);
      setReady(true);
    } catch (e) { if (version === revision.current) setError(e.message); }
    finally { if (version === revision.current) setLoading(false); }
  }
  useEffect(() => {
    let activo = true;
    cliente.request(raiz + 'profesor/grupos').then(data => { if (activo) setGrupos(data); })
      .catch(e => { if (activo) setError(e.message); }).finally(() => { if (activo) setLoading(false); });
    return () => { activo = false; revision.current++; };
  }, [cliente]);
  const bloqueado = alumnos.some(a => a.notas.length > 0);
  const total = edicion.reduce((n, r) => n + Math.round(Number(r.porcentaje || 0) * 100), 0);
  function cambiarRubro(index, campo, valor) {
    setDirty(true); setEdicion(prev => prev.map((r, i) => i === index ? { ...r, [campo]: valor } : r));
  }
  async function guardarDesglose(event) {
    event.preventDefault(); setError(''); setMensaje('');
    if (total !== 10000 || edicion.some(r => !r.nombre.trim() || Number(r.porcentaje) <= 0 || Number(r.porcentaje) > 100)) {
      setError('Los rubros deben tener nombre y sumar exactamente 100 %.'); return;
    }
    if (!window.confirm(rubros.length ? '¿Reemplazar el desglose de este grupo?' : '¿Guardar el desglose de este grupo?')) return;
    setSaving(true);
    try {
      await cliente.request(raiz + 'cargardesglose', { method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ grupoId: Number(grupo), rubros: edicion.map(r => ({ nombre: r.nombre.trim(), porcentaje: Number(r.porcentaje) })) }) });
      await cargar(grupo); setMensaje('Desglose guardado.');
    } catch (e) { setError(e.message); }
    finally { setSaving(false); }
  }
  async function guardarNota(alumno, rubro, nota, existe) {
    setError(''); setMensaje('');
    if (!Number.isFinite(nota) || nota < 1 || nota > 100) { setError('La nota debe estar entre 1 y 100.'); return; }
    if (!window.confirm('¿Guardar esta nota?')) return;
    setSaving(true);
    try {
      await cliente.request(raiz + 'asignarnotarubro', { method: existe ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ matriculaId: alumno.matriculaId, rubroId: rubro.id, nota }) });
      await cargar(grupo); setMensaje('Nota guardada.');
    } catch (e) { setError(e.message); }
    finally { setSaving(false); }
  }
  return <>
    <section className="page-head"><div><h1>Desglose y notas</h1><p className="subtitle">Administra la evaluación de tus grupos.</p></div></section>
    <section className="panel">
      <div className="field"><label htmlFor="grupo">Grupo del periodo activo</label><select id="grupo" value={grupo} disabled={saving || loading} onChange={e => {
        if (dirty && !window.confirm('¿Descartar los cambios del desglose?')) return;
        setGrupo(e.target.value); setMensaje(''); setFiltro(''); cargar(e.target.value);
      }}><option value="">Selecciona un grupo</option>{grupos.map(g => <option key={g.grupoId} value={g.grupoId}>{g.curso} · Grupo {g.numeroGrupo} · {g.horario}</option>)}</select></div>
      {loading && <p role="status">Cargando…</p>}
      {!loading && !error && !grupos.length && <p>No tienes grupos en el periodo activo.</p>}
      {error && <p role="alert" className="aviso error">{error}</p>}
      {mensaje && <p role="status" className="aviso">{mensaje}</p>}
      {!loading && grupo && !ready && <button className="btn btn-soft" onClick={() => cargar(grupo)}>Reintentar consulta</button>}
    </section>
    {ready && <>
      <section className="panel"><h2>Rubros de evaluación</h2>
        {!rubros.length && <p>Este grupo no tiene desglose. Agrega los rubros para comenzar.</p>}
        {bloqueado && <p className="aviso">Ya existen notas: el desglose está bloqueado.</p>}
        <form onSubmit={guardarDesglose}><fieldset disabled={saving || loading || bloqueado}>
          {edicion.map((r, i) => <div className="rubro" key={i}>
            <div className="field"><label htmlFor={'nombre-' + i}>Rubro {i + 1}</label><input id={'nombre-' + i} maxLength={100} required value={r.nombre} onChange={e => cambiarRubro(i, 'nombre', e.target.value)}/></div>
            <div className="field"><label htmlFor={'porcentaje-' + i}>Porcentaje {i + 1}</label><input id={'porcentaje-' + i} type="number" min="0.01" max="100" step="0.01" required value={r.porcentaje} onChange={e => cambiarRubro(i, 'porcentaje', e.target.value)}/></div>
            <button className="btn btn-soft" type="button" aria-label={'Eliminar rubro ' + (i + 1)} disabled={edicion.length === 1} onClick={() => { setEdicion(edicion.filter((_, j) => j !== i)); setDirty(true); }}>Quitar</button>
          </div>)}
          <p>Total: {(total / 100).toFixed(2)} %</p><div className="acciones">
            <button className="btn btn-soft" type="button" onClick={() => { setEdicion([...edicion, { nombre: '', porcentaje: '' }]); setDirty(true); }}>Agregar rubro</button>
            <button className="btn btn-primary">{saving ? 'Guardando…' : 'Guardar desglose'}</button>
            <button className="btn btn-soft" type="button" onClick={() => cargar(grupo)}>Regresar sin cambios</button>
          </div></fieldset></form>
      </section>
      <section className="panel"><h2>Notas del grupo</h2>
        {!rubros.length ? <p>Primero carga el desglose.</p> : <>
          <div className="field"><label htmlFor="estudiante">Vista por estudiante</label><select id="estudiante" value={filtro} onChange={e => setFiltro(e.target.value)}><option value="">Todo el grupo</option>{alumnos.map(a => <option key={a.matriculaId} value={a.matriculaId}>{a.identificacionEstudiante}</option>)}</select></div>
          {!alumnos.length && <p>No hay estudiantes con matrícula activa en este grupo.</p>}
          {alumnos.filter(a => !filtro || String(a.matriculaId) === filtro).map(a => <article key={a.matriculaId}>
            <h3>Estudiante {a.identificacionEstudiante}</h3>{!a.notas.length && <p>Sin notas registradas.</p>}
            {rubros.map(r => <Nota key={`${grupo}-${a.matriculaId}-${r.id}-${a.notas.find(n => n.rubroId === r.id)?.nota ?? 'nueva'}`}
              alumno={a} rubro={r} actual={a.notas.find(n => n.rubroId === r.id)} disabled={saving || loading || dirty}
              guardar={guardarNota}/>)}
          </article>)}
          {dirty && <p className="aviso">Guarda o descarta los cambios del desglose antes de asignar notas.</p>}
        </>}
      </section>
    </>}
  </>;
}

function Nota({ alumno, rubro, actual, disabled, guardar }) {
  const [valor, setValor] = useState(actual?.nota ?? '');
  return <form className="rubro" onSubmit={e => { e.preventDefault(); guardar(alumno, rubro, Number(valor), Boolean(actual)); }}>
    <div><strong>{rubro.nombre}</strong><p>{rubro.porcentaje} %</p></div>
    <div className="field"><label htmlFor={`nota-${alumno.matriculaId}-${rubro.id}`}>Nota {rubro.nombre}</label><input id={`nota-${alumno.matriculaId}-${rubro.id}`} type="number" min="1" max="100" step="0.01" value={valor} onChange={e => setValor(e.target.value)} required disabled={disabled}/></div>
    <button className="btn btn-primary" disabled={disabled}>{actual ? 'Modificar nota' : 'Asignar nota'}</button>
  </form>;
}
