// Compruebo scripts con respuestas simuladas, sin enviar HTTP ni modificar SQL.
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const assert = require('node:assert/strict');
const {test} = require('node:test');
const load = name => JSON.parse(fs.readFileSync(path.join(__dirname, name), 'utf8'));
const p2 = load('Persona2_Ramses.postman_collection.json');
const p5 = load('Persona5_Jose.postman_collection.json');
function find(node, name) {
  if (node.name === name) return node;
  for (const item of node.item || []) { const found = find(item, name); if (found) return found; }
}
function session(collection, env = {}) {
  const local = new Map();
  const values = new Map((collection.variable || []).map(v => [v.key, v.value]));
  const environment = new Map(Object.entries(env));
  const facade = map => ({get: key => map.get(key), set: (key, value) => map.set(key, value), unset: key => map.delete(key)});
  const expect = value => {
    const api = {eql: other => assert.deepEqual(value, other),
      a: type => { assert.equal(typeof value, type); return api; },
      lengthOf: n => assert.equal(value.length, n),
      property: key => assert.ok(!Object.hasOwn(value, key))};
    for (const key of ['to', 'be', 'have', 'and', 'not']) api[key] = api;
    return api;
  };
  const pm = {collectionVariables: facade(values), environment: facade(environment),
    variables: {...facade(local), get: key => local.has(key) ? local.get(key) : environment.has(key) ? environment.get(key) : values.get(key)},
    execution: {skipRequest: () => { pm.skipped = true; }},
    test: (_name, fn) => fn(), expect};
  return {pm, values, environment, run(name, listen, status = 200, body = {}) {
    local.clear(); pm.skipped = false;
    pm.response = {code: status, json: () => body, to: {have: {status: code => assert.equal(status, code)}}};
    const item = find(collection, name);
    assert.ok(item, name);
    for (const event of item.event || []) if (event.listen === listen) vm.runInNewContext(event.script.exec.join('\n'), {pm});
    return pm;
  }};
}
for (const [key, create, remove, confirm, field] of [
  ['institucionId', 'Crear institución', 'Eliminar institucion creado en esta ejecución', 'Confirmar eliminación de institucion', 'id'],
  ['profesorId', 'Crear profesor con dos teléfonos', 'Eliminar profesor creado en esta ejecución', 'Confirmar eliminación de profesor', 'id'],
  ['carreraId', 'Crear carrera', 'Eliminar carrera creado en esta ejecución', 'Confirmar eliminación de carrera', 'id'],
  ['emailPrueba', 'Crear usuario ficticio', 'Eliminar usuario creado en esta ejecución', 'Confirmar eliminación de usuario', 'email']
]) {
  test(`Limpieza de ${key}: ignora registros existentes y flags antiguos`, () => {
    const s = session(p2, {[key]: 999});
    s.values.set(key + 'Creado', true);
    assert.equal(s.run(remove, 'prerequest').skipped, true);
    assert.equal(s.run(confirm, 'prerequest').skipped, true);
  });
  test(`Limpieza de ${key}: usa exclusivamente el alta y confirma su eliminación`, () => {
    const id = field === 'email' ? 'ficticio@cuc.cr' : 123;
    const s = session(p2, {institucionId: 7, profesorId: 8});
    s.run(create, 'prerequest');
    s.run(create, 'test', 201, {[field]: id, telefonos: ['1', '2']});
    s.environment.set(key, 999);
    assert.equal(s.run(remove, 'prerequest').variables.get(key), id);
    // Un fallo HTTP no borra la procedencia, para poder reintentar.
    assert.throws(() => s.run(remove, 'test', 503));
    assert.equal(s.values.get(key + 'Limpieza'), id);
    s.run(remove, 'test', 204);
    assert.equal(s.values.has(key + 'Limpieza'), false);
    assert.equal(s.run(confirm, 'prerequest').variables.get(key), id);
    assert.equal(s.run(remove, 'prerequest').skipped, true);
  });
}
test('Crear carrera con referencias existentes no las convierte en registros de prueba', () => {
  const s = session(p2, {institucionId: 91, profesorId: 92});
  s.run('Crear carrera', 'prerequest');
  assert.equal(s.pm.variables.get('institucionId'), 91);
  assert.equal(s.pm.variables.get('profesorId'), 92);
  assert.equal(s.run('Eliminar institucion creado en esta ejecución', 'prerequest').skipped, true);
  assert.equal(s.run('Eliminar profesor creado en esta ejecución', 'prerequest').skipped, true);
});
test('Crear carrera rechaza referencias ausentes antes de HTTP', () => {
  const s = session(p2, {institucionId: '', profesorId: ''});
  assert.throws(() => s.run('Crear carrera', 'prerequest'), /configurar sus IDs/);
  assert.equal(s.pm.skipped, true);
});
test('Persona 5 conserva curso, grupo y periodo configurados al crear matrícula', () => {
  const s = session(p5, {cursoId: 51, grupoId: 52, periodoId: 53, estudiante: 'FICTICIO'});
  s.run('02.1 Crear matricula', 'prerequest');
  s.run('02.1 Crear matricula', 'test', 201, {id: 100, estado: 'ACTIVA', identificacionEstudiante: 'FICTICIO'});
  assert.equal(s.environment.get('cursoId'), 51);
  assert.equal(s.environment.get('grupoId'), 52);
  assert.equal(s.environment.get('periodoId'), 53);
  assert.equal(s.environment.get('matriculaId'), 100);
});
test('Persona 5 exige confirmación explícita antes de cambiar rubros', () => {
  const s = session(p5);
  assert.throws(() => s.run('03.1 Cargar desglose 40 y 60', 'prerequest'), /Confirmar grupo/);
  assert.equal(s.pm.skipped, true);
  assert.equal(s.values.get('grupoPruebasConfirmado'), 'false');
  s.environment.set('grupoPruebasConfirmado', 'true');
  assert.equal(s.run('03.1 Cargar desglose 40 y 60', 'prerequest').skipped, false);
});
test('Persona 5 no usa factura fija si falla el alta', () => {
  const s = session(p5, {matriculaId: 20, facturaId: 999});
  s.run('04.1 Crear factura', 'prerequest');
  assert.equal(s.environment.has('facturaId'), false);
  assert.throws(() => s.run('04.2 Consultar factura', 'prerequest'), /configurar facturaId/);
  assert.equal(s.pm.skipped, true);
});
