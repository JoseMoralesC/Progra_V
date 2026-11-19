const fs = require('fs');
const p2Path = 'docs/pruebas/postman/Persona2_Ramses.postman_collection.json';
const p2 = JSON.parse(fs.readFileSync(p2Path, 'utf8'));

// 1. Ensure collection variables exist
function setColVar(key, val) {
  if (!p2.variable) p2.variable = [];
  const found = p2.variable.find(v => v.key === key);
  if (found) {
    if (!found.value && val) found.value = val;
  } else {
    p2.variable.push({ key, value: val, type: 'string' });
  }
}

setColVar('token', '');
setColVar('refresh_token', '');
setColVar('dominioProfesor', 'cuc.ac.cr');
setColVar('institucionId', '3');
setColVar('profesorId', '5');
setColVar('carreraId', '6');

// 2. Walk items and patch scripts
function walk(item) {
  if (item.name === 'Login válido y guardar tokens' && item.event) {
    item.event.forEach(e => {
      if (e.listen === 'test') {
        e.script.exec = [
          'pm.test("HTTP 201", () => pm.response.to.have.status(201));',
          'if (pm.response.code === 201) {',
          '  const j = pm.response.json();',
          '  pm.collectionVariables.set("token", j.access_token);',
          '  pm.collectionVariables.set("refresh_token", j.refresh_token);',
          '  if (pm.environment) {',
          '    pm.environment.set("token", j.access_token);',
          '    pm.environment.set("refresh_token", j.refresh_token);',
          '  }',
          '}',
          'pm.test("JWT y refresh emitidos", () => {',
          '  const j = pm.response.json();',
          '  pm.expect(j.access_token).to.be.a("string").and.not.empty;',
          '  pm.expect(j.refresh_token).to.be.a("string").and.not.empty;',
          '});'
        ];
      }
    });
  }

  if (item.name === 'Crear institución' && item.event) {
    item.event.forEach(e => {
      if (e.listen === 'test') {
        e.script.exec = [
          'pm.test("HTTP 201", () => pm.response.to.have.status(201));',
          'if (pm.response.code === 201) {',
          '  const id = pm.response.json().id;',
          '  pm.collectionVariables.set("institucionId", id);',
          '  pm.collectionVariables.set("institucionIdCreado", true);',
          '  if (pm.environment) pm.environment.set("institucionId", id);',
          '}'
        ];
      }
    });
  }

  if (item.name === 'Crear profesor con dos teléfonos' && item.event) {
    item.event.forEach(e => {
      if (e.listen === 'test') {
        e.script.exec = [
          'pm.test("HTTP 201", () => pm.response.to.have.status(201));',
          'if (pm.response.code === 201) {',
          '  const id = pm.response.json().id;',
          '  pm.collectionVariables.set("profesorId", id);',
          '  pm.collectionVariables.set("profesorIdCreado", true);',
          '  if (pm.environment) pm.environment.set("profesorId", id);',
          '}',
          'pm.test("Dos teléfonos", () => pm.expect(pm.response.json().telefonos).to.have.lengthOf(2));'
        ];
      }
    });
  }

  if (item.name === 'Crear usuario ficticio' && item.event) {
    item.event.forEach(e => {
      if (e.listen === 'test') {
        e.script.exec = [
          'pm.test("HTTP 201", () => pm.response.to.have.status(201));',
          'if (pm.response.code === 201) {',
          '  const email = pm.response.json().email;',
          '  pm.collectionVariables.set("emailPrueba", email);',
          '  pm.collectionVariables.set("emailPruebaCreado", true);',
          '  if (pm.environment) pm.environment.set("emailPrueba", email);',
          '}',
          'pm.test("Respuesta sin contraseña", () => {',
          '  const j = pm.response.json();',
          '  pm.expect(j).not.to.have.property("passwordHash");',
          '  pm.expect(j).not.to.have.property("contrasena");',
          '});'
        ];
      }
    });
  }

  if (item.name === 'Crear carrera' && item.event) {
    item.event.forEach(e => {
      if (e.listen === 'prerequest') {
        e.script.exec = [
          'pm.collectionVariables.unset("carreraId");',
          'pm.collectionVariables.unset("carreraIdCreado");',
          'let instId = pm.collectionVariables.get("institucionId") || (pm.environment && pm.environment.get("institucionId"));',
          'let profId = pm.collectionVariables.get("profesorId") || (pm.environment && pm.environment.get("profesorId"));',
          'if (!instId || isNaN(Number(instId))) { instId = 3; pm.collectionVariables.set("institucionId", 3); }',
          'if (!profId || isNaN(Number(profId))) { profId = 5; pm.collectionVariables.set("profesorId", 5); }',
          'if (pm.environment) { pm.environment.set("institucionId", instId); pm.environment.set("profesorId", profId); }',
          'pm.collectionVariables.set("institucionIdCreado", true);',
          'pm.collectionVariables.set("profesorIdCreado", true);'
        ];
      }
      if (e.listen === 'test') {
        e.script.exec = [
          'pm.test("HTTP 201", () => pm.response.to.have.status(201));',
          'if (pm.response.code === 201) {',
          '  const id = pm.response.json().id;',
          '  pm.collectionVariables.set("carreraId", id);',
          '  pm.collectionVariables.set("carreraIdCreado", true);',
          '  if (pm.environment) pm.environment.set("carreraId", id);',
          '}'
        ];
      }
    });
  }

  if (item.item) item.item.forEach(walk);
}

walk(p2);

fs.writeFileSync(p2Path, JSON.stringify(p2, null, 2) + '\n');
console.log('P2 synchronized successfully!');
