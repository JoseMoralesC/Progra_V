import { test } from 'node:test';
import assert from 'node:assert/strict';
import { leerTamanoPagina, moduloApi, normalizarModulo, paginar, validarModulo } from '../src/modulos-api.js';
test('normaliza el identificador y conserva el contrato del API', () => assert.deepEqual(normalizarModulo({ idModulo: ' adm_7 ', nombreModulo: ' Módulos ', activo: 1, orden: '2' }), { idModulo: 'ADM_7', nombreModulo: 'Módulos', activo: true, orden: 2 }));
test('rechaza identificador, nombre y orden inválidos', () => {
  assert.match(validarModulo({ idModulo: 'x', nombreModulo: 'Módulos', activo: true, orden: 1 }), /identificador/);
  assert.match(validarModulo({ idModulo: 'ADM7', nombreModulo: 'Módulos 7', activo: true, orden: 1 }), /nombre/);
  assert.match(validarModulo({ idModulo: 'ADM7', nombreModulo: 'Módulos', activo: true, orden: -1 }), /orden/);
});
test('solo utiliza rutas del gateway y serializa roles', async () => {
  const calls = [], cliente = { request: async (...args) => { calls.push(args); return []; } }, api = moduloApi(cliente);
  await api.listar(); await api.guardarRoles('ADM7', ['ADMINISTRADOR']); await api.listarRoles();
  assert.equal(calls[0][0], '/gateway/admin/modulo');
  assert.equal(calls[1][0], '/gateway/admin/modulo/ADM7/roles');
  assert.equal(calls[1][1].body, '["ADMINISTRADOR"]');
  assert.equal(calls[2][0], '/gateway/admin/rol');
});
test('lee tamaño de página desde parámetros con tope máximo de 15', async () => {
  assert.equal(await leerTamanoPagina({ request: async () => ({ valor: '8' }) }), 8);
  assert.equal(await leerTamanoPagina({ request: async () => ({ valor: '25' }) }), 15);
  assert.equal(await leerTamanoPagina({ request: async () => { throw new Error('404'); } }), 15);
  const p = paginar(Array.from({ length: 20 }, (_, i) => i + 1), 1, 15);
  assert.equal(p.items.length, 5);
  assert.equal(p.page, 1);
  assert.equal(p.size, 15);
});
