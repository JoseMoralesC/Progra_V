import React, { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { ClienteSesion } from '../../jose/src/sesion.js';
import { Login } from './Login.jsx';
import { Modulos } from './Modulos.jsx';
import '../../../Usuario.ConsumoWeb/NuevoAvatar.Web/wwwroot/css/site.css';
import './ramses.css';
function App() {
  const [sesion, setSesion] = useState(null), [cargando, setCargando] = useState(true), [error, setError] = useState('');
  const [cliente] = useState(() => new ClienteSesion({ onExpired: () => { setSesion(null); setError('La sesión venció. Iniciá sesión nuevamente.'); } }));
  useEffect(() => {
    if (!cliente.tokens) { setCargando(false); return; }
    cliente.actual().then(setSesion).catch(e => setError(e.message)).finally(() => setCargando(false));
  }, [cliente]);
  async function salir() { setSesion(null); try { await cliente.logout(); } catch { setError('La sesión se cerró localmente.'); } }
  if (cargando) return <main className="ramses"><p role="status">Comprobando sesión…</p></main>;
  if (!sesion) return <main className="ramses">{error && <p className="aviso error" role="alert">{error}</p>}<Login cliente={cliente} onAuthenticated={setSesion}/></main>;
  if (sesion.rol !== 'ADMINISTRADOR') return <main className="ramses"><section className="panel"><h1>Acceso restringido</h1><p>ADM7 requiere el rol ADMINISTRADOR.</p><button className="btn btn-soft" onClick={salir}>Cerrar sesión</button></section></main>;
  return <main className="ramses"><header className="topbar"><strong>Nuevo Avatar · {sesion.nombre}</strong><button className="btn btn-soft" onClick={salir}>Cerrar sesión</button></header><Modulos cliente={cliente}/></main>;
}
createRoot(document.getElementById('root')).render(<App/>);
