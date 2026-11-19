const fs = require('fs');
const p3Path = 'docs/pruebas/postman/PrograV_Persona3_Pruebas_POSTMAN_FINAL.json';
const p3 = JSON.parse(fs.readFileSync(p3Path, 'utf8'));

// Update collection variables
const cVar = p3.variable.find(v => v.key === 'carreraId');
if (cVar) cVar.value = '6';

function find(item, name) {
  if (item.name && item.name.includes(name)) return item;
  if (item.item) {
    for (const c of item.item) {
      const f = find(c, name);
      if (f) return f;
    }
  }
}

// 1. P3-CUR-03
const cur03 = find(p3, 'P3-CUR-03');
if (cur03) {
  cur03.request.body.raw = JSON.stringify({
    carreraId: 6,
    nivel: 1,
    nombre: "Curso Prueba Persona Tres"
  }, null, 2);
  cur03.event.forEach(e => {
    if (e.listen === 'test') {
      e.script.exec = [
        "pm.test('HTTP 201', function () {",
        "    pm.response.to.have.status(201);",
        "});",
        "",
        "const respData = pm.response.json();",
        "if (respData.cursoId != null) {",
        "    pm.collectionVariables.set('cursoPruebaId', String(respData.cursoId));",
        "    if (pm.environment) pm.environment.set('cursoPruebaId', String(respData.cursoId));",
        "}"
      ];
    }
  });
}

// 2. P3-CUR-04
const cur04 = find(p3, 'P3-CUR-04');
if (cur04) {
  cur04.request.body.raw = JSON.stringify({
    carreraId: 6,
    nivel: 3,
    nombre: "Curso Prueba QA Modificado"
  }, null, 2);
}

// 3. P3-PER-03
const per03 = find(p3, 'P3-PER-03');
if (per03) {
  per03.event.forEach(e => {
    if (e.listen === 'test') {
      e.script.exec = [
        "pm.test('HTTP 201', function () {",
        "    pm.response.to.have.status(201);",
        "});",
        "",
        "const respData = pm.response.json();",
        "if (respData.periodoId != null) {",
        "    pm.collectionVariables.set('periodoPruebaId', String(respData.periodoId));",
        "    if (pm.environment) pm.environment.set('periodoPruebaId', String(respData.periodoId));",
        "}"
      ];
    }
  });
}

// 4. P3-GRU-03
const gru03 = find(p3, 'P3-GRU-03');
if (gru03) {
  gru03.event.forEach(e => {
    if (e.listen === 'test') {
      e.script.exec = [
        "pm.test('HTTP 201', function () {",
        "    pm.response.to.have.status(201);",
        "});",
        "",
        "const respData = pm.response.json();",
        "if (respData.grupoId != null) {",
        "    pm.collectionVariables.set('grupoPruebaId', String(respData.grupoId));",
        "    if (pm.environment) pm.environment.set('grupoPruebaId', String(respData.grupoId));",
        "}"
      ];
    }
  });
}

fs.writeFileSync(p3Path, JSON.stringify(p3, null, 2) + '\n');
console.log('P3 patched successfully!');
