import React, { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { ClienteSesion } from '../../jose/src/sesion.js';
import { Login } from './Login.jsx';
import { Modulos } from './Modulos.jsx';
import { Profesores } from './Profesores.jsx';
import { Carreras } from './Carreras.jsx';
import { Cursos } from './Cursos.jsx';
import { Periodos } from './Periodos.jsx';
import { Notificaciones } from './Notificaciones.jsx';
import '../../../Usuario.ConsumoWeb/NuevoAvatar.Web/wwwroot/css/site.css';
import './ramses.css';
function App() {
  const [sesion, setSesion] = useState(null), [cargando, setCargando] = useState(true), [error, setError] = useState('');
  const [vista, setVista] = useState('modulos');
  const [cliente] = useState(() => new ClienteSesion({ onExpired: () => { setSesion(null); setError('La sesión venció. Iniciá sesión nuevamente.'); } }));
  useEffect(() => {
    if (!cliente.tokens) { setCargando(false); return; }
    cliente.actual().then(setSesion).catch(e => setError(e.message)).finally(() => setCargando(false));
  }, [cliente]);
  async function salir() { setSesion(null); try { await cliente.logout(); } catch { setError('La sesión se cerró localmente.'); } }
  if (cargando) return <main className="ramses"><p role="status">Comprobando sesión…</p></main>;
  if (!sesion) return <main className="ramses">{error && <p className="aviso error" role="alert">{error}</p>}<Login cliente={cliente} onAuthenticated={setSesion}/></main>;
  if (sesion.rol !== 'ADMINISTRADOR') return <main className="ramses"><section className="panel"><h1>Acceso restringido</h1><p>ADM7 requiere el rol ADMINISTRADOR.</p><button className="btn btn-soft" onClick={salir}>Cerrar sesión</button></section></main>;
  const contenido = vista === 'profesores' ? <Profesores cliente={cliente}/> : vista === 'carreras' ? <Carreras cliente={cliente}/> : vista === 'cursos' ? <Cursos cliente={cliente}/> : vista === 'periodos' ? <Periodos cliente={cliente}/> : vista === 'notificaciones' ? <Notificaciones cliente={cliente}/> : <Modulos cliente={cliente}/>;
  return <main className="ramses"><header className="topbar"><strong>Nuevo Avatar · {sesion.nombre}</strong><nav className="acciones"><button className="btn btn-soft" onClick={() => setVista('modulos')}>Módulos</button><button className="btn btn-soft" onClick={() => setVista('profesores')}>Profesores</button><button className="btn btn-soft" onClick={() => setVista('carreras')}>Carreras</button><button className="btn btn-soft" onClick={() => setVista('cursos')}>Cursos</button><button className="btn btn-soft" onClick={() => setVista('periodos')}>Periodos</button><button className="btn btn-soft" onClick={() => setVista('notificaciones')}>Notificaciones</button><button className="btn btn-soft" onClick={salir}>Cerrar sesión</button></nav></header>{contenido}</main>;
}
createRoot(document.getElementById('root')).render(<App/>);
