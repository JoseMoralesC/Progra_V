const fs = require('fs');

const p4Paths = [
  'docs/pruebas/postman/Persona4_Fabian_Recorrido.postman_collection.json',
  'docs/evidencia/fabian/Persona4_Fabian_Recorrido.postman_collection.json'
];

const envPaths = [
  'docs/pruebas/postman/Persona1_Persona2.postman_environment.json',
  'docs/pruebas/postman/NuevoAvatar_Microservicios.postman_environment.json'
];

for (const p of p4Paths) {
  const p4 = JSON.parse(fs.readFileSync(p, 'utf8'));

  function setVar(key, val) {
    if (!p4.variable) p4.variable = [];
    const v = p4.variable.find(x => x.key === key);
    if (v) v.value = val;
    else p4.variable.push({ key, value: val });
  }

  setVar('cantonId', '1');
  setVar('distritoId', '1');
  setVar('provinciaId', '1');
  setVar('carreraId', '6');
  setVar('cursoId', '5');
  setVar('otroCursoId', '7');
  setVar('periodoFuturoId', '6');
  setVar('periodoHoyId', '1');
  setVar('periodoPasadoId', '3');
  setVar('periodoId', '1');

  function walk(item) {
    if (item.name === 'Crear prematricula de prueba' && item.event) {
      item.event.forEach(e => {
        if (e.listen === 'test') {
          e.script.exec = [
            'pm.test("HTTP 201", function () { pm.response.to.have.status(201); });',
            'if (pm.response.code === 201) {',
            '    const id = pm.response.json().id;',
            '    pm.collectionVariables.set("prematriculaId", id);',
            '    if (pm.environment) pm.environment.set("prematriculaId", id);',
            '}'
          ];
        }
      });
    }
    if (item.item) item.item.forEach(walk);
  }
  walk(p4);

  fs.writeFileSync(p, JSON.stringify(p4, null, 2) + '\n');
  console.log(`P4 file patched: ${p}`);
}

for (const ep of envPaths) {
  const env = JSON.parse(fs.readFileSync(ep, 'utf8'));
  function setEnvVar(k, v) {
    const found = env.values.find(x => x.key === k);
    if (found) found.value = v;
    else env.values.push({ key: k, value: v, type: 'default', enabled: true });
  }
  setEnvVar('cantonId', '1');
  setEnvVar('distritoId', '1');
  setEnvVar('provinciaId', '1');
  setEnvVar('carreraId', '6');
  setEnvVar('cursoId', '5');
  setEnvVar('otroCursoId', '7');
  setEnvVar('periodoFuturoId', '6');
  setEnvVar('periodoHoyId', '1');
  setEnvVar('periodoPasadoId', '3');
  setEnvVar('periodoId', '1');
  setEnvVar('identificacionPrueba', 'PRUEBA-MAT3-FABIAN');
  setEnvVar('identificacionEstudiante', 'PRUEBA-MAT3-FABIAN');

  fs.writeFileSync(ep, JSON.stringify(env, null, 2) + '\n');
  console.log(`Env file updated: ${ep}`);
}
