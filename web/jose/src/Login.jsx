import React, { useState } from 'react';

export function Login({ cliente, onAuthenticated }) {
  const [email, setEmail] = useState(''), [password, setPassword] = useState('');
  const [busy, setBusy] = useState(false), [error, setError] = useState('');
  async function ingresar(event) {
    event.preventDefault(); setBusy(true); setError('');
    try { const sesion = await cliente.login(email, password); setPassword(''); onAuthenticated(sesion); }
    catch (e) { setError(e.message); }
    finally { setBusy(false); }
  }
  return <section className="panel acceso" aria-labelledby="login-title">
    <p className="subtitle">Nuevo Avatar</p><h1 id="login-title">Iniciar sesión</h1>
    <p>Ingresa con tu correo institucional.</p>
    <form onSubmit={ingresar}>
      <div className="field"><label htmlFor="email">Correo institucional</label><input id="email" type="email" autoComplete="username" required value={email} onChange={e => setEmail(e.target.value)} disabled={busy}/></div>
      <div className="field"><label htmlFor="password">Contraseña</label><input id="password" type="password" autoComplete="current-password" required value={password} onChange={e => setPassword(e.target.value)} disabled={busy}/></div>
      {error && <p role="alert" className="aviso error">{error}</p>}
      <button className="btn btn-primary" disabled={busy}>{busy ? 'Ingresando…' : 'Ingresar'}</button>
    </form>
  </section>;
}
