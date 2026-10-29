import { test } from 'node:test';
import assert from 'node:assert/strict';
import { ClienteSesion } from '../src/sesion.js';

const now = Date.parse('2026-10-28T12:00:00Z');
const tokens = (vence = now + 600000, extra = {}) => ({ access_token: 'original', refresh_token: 'refresh-1',
  expires_in: new Date(vence).toISOString(), usuarioID: 'p@cuc.ac.cr', ...extra });
const json = (data, status = 200) => new Response(JSON.stringify(data), { status, headers: { 'Content-Type': 'application/json' } });
function storage() { const map = new Map(); return { getItem: key => map.get(key) ?? null, setItem: (key, value) => map.set(key, value), removeItem: key => map.delete(key) }; }

test('login conserva contrato real y obtiene perfil del servidor, sin persistir password', async () => {
  const calls = [], store = storage();
  const client = new ClienteSesion({ storage: store, now: () => now, fetcher: async (path, options) => {
    calls.push([path, options]);
    return json(path.endsWith('/login') ? tokens() : { rol: 'PROFESOR', nombre: 'Docente' });
  } });
  assert.equal((await client.login(' P@CUC.AC.CR ', 'secreto')).rol, 'PROFESOR');
  assert.equal(calls[0][1].headers.usuario, 'p@cuc.ac.cr');
  assert.equal(calls[0][1].headers.contrasena, 'secreto');
  assert.equal(calls[1][1].headers.Authorization, 'Bearer original');
  assert.ok(!store.getItem(client.key).includes('secreto'));
});

test('solicitudes simultaneas con token vencido comparten una sola renovacion rotativa', async () => {
  let refreshes = 0;
  const client = new ClienteSesion({ storage: storage(), now: () => now, fetcher: async (path, options) => {
    if (path.endsWith('/refresh')) { refreshes++; await new Promise(resolve => setTimeout(resolve, 10));
      return json(tokens(now + 600000, { access_token: 'nuevo', refresh_token: 'refresh-2' })); }
    assert.equal(options.headers.Authorization, 'Bearer nuevo'); return json([]);
  } });
  client.guardar(tokens(now - 1000));
  await Promise.all([client.request('/gateway/mat/obtenerdesglose'), client.actual(), client.actual()]);
  assert.equal(refreshes, 1); assert.equal(client.tokens.refresh_token, 'refresh-2');
});

test('refresh fallido limpia credenciales y notifica retorno a login', async () => {
  let expired = 0;
  const client = new ClienteSesion({ storage: storage(), now: () => now, onExpired: () => expired++, fetcher: async () => json({}, 401) });
  client.guardar(tokens(now - 1000));
  await assert.rejects(client.actual());
  assert.equal(client.tokens, null); assert.equal(client.storage.getItem(client.key), null); assert.equal(expired, 1);
});

test('logout durante refresh no permite restaurar la sesion al finalizar la respuesta', async () => {
  let completar;
  const client = new ClienteSesion({ storage: storage(), now: () => now, fetcher: async path => {
    if (path.endsWith('/refresh')) return new Promise(resolve => { completar = () => resolve(json(tokens())); });
    return new Response(null, { status: 204 });
  } });
  client.guardar(tokens(now - 1000));
  const pendiente = client.renovar();
  await client.logout(); completar(); await assert.rejects(pendiente);
  assert.equal(client.tokens, null); assert.equal(client.storage.getItem(client.key), null);
});

test('401 de negocio renueva una vez; segundo 401 limpia sesion sin bucles', async () => {
  let refreshes = 0, requests = 0;
  const client = new ClienteSesion({ storage: storage(), now: () => now, fetcher: async path => {
    if (path.endsWith('/refresh')) { refreshes++; return json(tokens()); }
    requests++; return json({}, 401);
  } });
  client.guardar(tokens()); await assert.rejects(client.actual());
  assert.equal(refreshes, 1); assert.equal(requests, 2); assert.equal(client.tokens, null);
});

test('caida de negocio no elimina sesion ni devuelve datos inventados', async () => {
  const client = new ClienteSesion({ storage: storage(), now: () => now, fetcher: async () => json({}, 503) });
  client.guardar(tokens()); await assert.rejects(client.actual(), e => e.status === 503);
  assert.ok(client.tokens);
});

test('no envia tokens fuera del gateway', async () => {
  const client = new ClienteSesion({ storage: storage(), now: () => now, fetcher: () => assert.fail('No debe salir') });
  client.guardar(tokens()); await assert.rejects(client.request('https://ajeno.invalid'));
});

test('credenciales incorrectas y respuesta incompleta no dejan una sesion nueva', async () => {
  for (const response of [json({}, 401), json({ access_token: 'sin-refresh' }, 201)]) {
    const client = new ClienteSesion({ storage: storage(), now: () => now, fetcher: async () => response });
    await assert.rejects(client.login('p@cuc.ac.cr', 'secreto')); assert.equal(client.tokens, null);
  }
});
