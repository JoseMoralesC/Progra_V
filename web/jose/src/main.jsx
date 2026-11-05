import React, { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { ClienteSesion } from './sesion.js';
import { Login } from './Login.jsx';
import { Notas } from './Notas.jsx';
// Se reutiliza el CSS de la plantilla creada por Alejandro, sin modificar su fuente.
import '../../../Usuario.ConsumoWeb/NuevoAvatar.Web/wwwroot/css/site.css';
import './modulos.css';

function App() {
  const [sesion, setSesion] = useState(null), [loading, setLoading] = useState(true), [error, setError] = useState('');
  const [cliente] = useState(() => new ClienteSesion({ onExpired: () => {
    setSesion(null); history.replaceState(null, '', '/login'); setError('La sesión venció. Inicia sesión nuevamente.');
  } }));
  function ingresar(data) {
    setSesion(data); setError('');
    // El layout anfitrion consume el evento para navegar al dashboard de ADM3/WEB1.
    window.dispatchEvent(new CustomEvent('avatar:sesion-iniciada', { detail: { rol: data.rol } }));
    history.replaceState(null, '', data.rol === 'PROFESOR' ? '/notas' : '/sesion');
  }
  useEffect(() => {
    if (!cliente.tokens) { history.replaceState(null, '', '/login'); setLoading(false); return; }
    cliente.actual().then(ingresar).catch(e => setError(e.message)).finally(() => setLoading(false));
  }, [cliente]);
  async function salir() {
    setSesion(null); history.replaceState(null, '', '/login');
    try { await cliente.logout(); setError(''); }
    catch { setError('La sesión se cerró en este dispositivo. No se pudo confirmar el registro de salida.'); }
  }
  if (loading) return <main className="modulos"><p role="status">Comprobando sesión…</p></main>;
  return <main className="modulos">
    {error && <p role="alert" className="aviso error">{error}</p>}
    {!sesion ? <Login cliente={cliente} onAuthenticated={ingresar}/> : <>
      <header className="topbar"><div><strong>Nuevo Avatar</strong><p>{sesion.nombre} · {sesion.rol}</p></div><button className="btn btn-soft" onClick={salir}>Cerrar sesión</button></header>
      {sesion.rol === 'PROFESOR' ? <Notas cliente={cliente}/> : <section className="panel"><h1>Sesión iniciada</h1><p>Tu cuenta se autenticó correctamente.</p></section>}
    </>}
  </main>;
}
createRoot(document.getElementById('root')).render(<App/>);
