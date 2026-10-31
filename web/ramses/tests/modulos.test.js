import { test } from 'node:test';
import assert from 'node:assert/strict';
import { moduloApi, normalizarModulo, validarModulo } from '../src/modulos-api.js';
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
