export class ErrorApi extends Error {
  constructor(message, status = 0) { super(message); this.status = status; }
}

// ADM2: cliente reutilizable por el layout de ADM1. Nunca recibe claves internas.
export class ClienteSesion {
  constructor({ fetcher = globalThis.fetch.bind(globalThis), storage = globalThis.sessionStorage,
    now = Date.now, onExpired = () => {} } = {}) {
    this.fetcher = fetcher; this.storage = storage; this.now = now; this.onExpired = onExpired;
    this.key = 'nuevo-avatar.sesion'; this.generation = 0; this.refreshing = null; this.tokens = null;
    try {
      const guardada = JSON.parse(storage.getItem(this.key) || 'null');
      if (guardada) this.tokens = this.validar(guardada);
    } catch { storage.removeItem(this.key); }
  }

  validar(value) {
    if (!value || typeof value.access_token !== 'string' || !value.access_token ||
        typeof value.refresh_token !== 'string' || !value.refresh_token ||
        typeof value.usuarioID !== 'string' || !value.usuarioID ||
        typeof value.expires_in !== 'string' || !Number.isFinite(Date.parse(value.expires_in)))
      throw new ErrorApi('Respuesta de sesión incompleta.');
    return { access_token: value.access_token, refresh_token: value.refresh_token,
      usuarioID: value.usuarioID, expires_in: value.expires_in };
  }

  guardar(value) {
    const tokens = this.validar(value);
    this.storage.setItem(this.key, JSON.stringify(tokens)); this.tokens = tokens;
    return tokens;
  }

  limpiar() {
    this.generation++; this.tokens = null; this.refreshing = null; this.storage.removeItem(this.key);
  }

  async leer(response) {
    if (response.status === 204) return null;
    if (!response.ok) {
      let mensaje = response.status >= 500 ? 'El servicio no está disponible. Inténtalo nuevamente.' : 'No se pudo completar la operación.';
      if (response.status === 403) mensaje = 'No tienes permiso para esta operación.';
      if (response.status === 401) mensaje = 'La sesión venció. Inicia sesión nuevamente.';
      if ([400, 404, 409, 422].includes(response.status)) {
        try { const body = await response.json(); mensaje = body.mensaje || body.message || body.error || mensaje; } catch { /* Mensaje controlado. */ }
      }
      throw new ErrorApi(mensaje, response.status);
    }
    return response.json();
  }

  async enviar(path, options) {
    try { return await this.fetcher(path, { ...options, cache: 'no-store', credentials: 'omit' }); }
    catch { throw new ErrorApi('No se pudo conectar con el servicio. Inténtalo nuevamente.'); }
  }

  async login(email, password) {
    const usuario = email.trim().toLowerCase();
    if (!/^[^\s@]+@(?:cuc\.cr|cuc\.ac\.cr)$/.test(usuario) || !password.trim())
      throw new ErrorApi('Ingresa un correo institucional válido y tu contraseña.');
    this.limpiar(); const version = this.generation;
    const response = await this.enviar('/gateway/auth/login', { method: 'POST', headers: { usuario, contrasena: password } });
    if (response.status === 401) throw new ErrorApi('Usuario y/o contraseña incorrectos', 401);
    const tokens = await this.leer(response);
    if (version !== this.generation) throw new ErrorApi('La sesión cambió. Inténtalo nuevamente.');
    this.guardar(tokens);
    return this.actual();
  }

  async renovar() {
    if (this.refreshing) return this.refreshing;
    if (!this.tokens) throw new ErrorApi('Inicia sesión para continuar.', 401);
    const version = this.generation, refresh_token = this.tokens.refresh_token;
    const promise = (async () => {
      try {
        const response = await this.enviar('/gateway/auth/refresh', { method: 'POST',
          headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ refresh_token }) });
        const tokens = await this.leer(response);
        if (version !== this.generation) throw new ErrorApi('La sesión cambió.', 401);
        return this.guardar(tokens);
      } catch (error) {
        if (version === this.generation) { this.limpiar(); this.onExpired(); }
        throw error;
      }
    })();
    this.refreshing = promise;
    try { return await promise; }
    finally { if (this.refreshing === promise) this.refreshing = null; }
  }

  async request(path, options = {}) {
    if (!path.startsWith('/gateway/')) throw new ErrorApi('Ruta de servicio no permitida.');
    if (!this.tokens) throw new ErrorApi('Inicia sesión para continuar.', 401);
    if (Date.parse(this.tokens.expires_in) <= this.now() + 15000) await this.renovar();
    const enviar = () => this.enviar(path, { ...options, headers: { ...options.headers,
      Authorization: 'Bearer ' + this.tokens.access_token } });
    let response = await enviar();
    if (response.status === 401) {
      await this.renovar(); response = await enviar();
      if (response.status === 401) { this.limpiar(); this.onExpired(); }
    }
    return this.leer(response);
  }

  actual() { return this.request('/gateway/auth/sesion'); }

  async logout() {
    const token = this.tokens?.access_token;
    // Se limpia antes de la llamada: ni una caida ni un refresh en vuelo restauran la cuenta.
    this.limpiar();
    if (token) await this.leer(await this.enviar('/gateway/auth/logout', {
      method: 'POST', headers: { Authorization: 'Bearer ' + token } }));
  }
}
