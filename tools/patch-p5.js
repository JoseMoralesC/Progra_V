const fs = require('fs');

const p5Path = 'docs/pruebas/postman/Persona5_Jose.postman_collection.json';
const p5 = JSON.parse(fs.readFileSync(p5Path, 'utf8'));

// 1. Update collection variables
function setVar(key, val) {
  if (!p5.variable) p5.variable = [];
  const v = p5.variable.find(x => x.key === key);
  if (v) v.value = val;
  else p5.variable.push({ key, value: val });
}

setVar('cursoId', '4');
setVar('grupoId', '3');
setVar('periodoId', '1');
setVar('estudiante', 'P5-JOSE-ESTUDIANTE');
setVar('grupoPruebasConfirmado', 'true');

// 2. Walk items and update scripts
function walk(item) {
  if (item.event) {
    item.event.forEach(e => {
      if (!e.script || !e.script.exec) return;
      let code = e.script.exec.join('\n');

      // Update pre-request fallbacks to cursoId=4 and grupoId=3
      if (e.listen === 'prerequest') {
        code = code.replace(/set\("cursoId",\s*["']2["']\)/g, 'set("cursoId", "4")');
        code = code.replace(/set\("grupoId",\s*["']1["']\)/g, 'set("grupoId", "3")');
        code = code.replace(/set\("cursoId",\s*2\)/g, 'set("cursoId", "4")');
        code = code.replace(/set\("grupoId",\s*1\)/g, 'set("grupoId", "3")');
      }

      // 02.1 Crear matricula test script: sync to environment
      if (item.name === '02.1 Crear matricula' && e.listen === 'test') {
        if (!code.includes('pm.environment.set("matriculaId"')) {
          code = code.replace(
            'pm.collectionVariables.set("matriculaId", respData.id);',
            'pm.collectionVariables.set("matriculaId", respData.id); if (pm.environment) { pm.environment.set("matriculaId", respData.id); pm.environment.set("cursoId", "4"); pm.environment.set("grupoId", "3"); }'
          );
        }
      }

      // 02.4 Modificar identificacion test script: sync estudiante to environment
      if (item.name === '02.4 Modificar identificacion' && e.listen === 'test') {
        if (!code.includes('pm.environment.set("estudiante"')) {
          code = code.replace(
            'pm.collectionVariables.set("estudiante", respData.identificacionEstudiante);',
            'pm.collectionVariables.set("estudiante", respData.identificacionEstudiante); if (pm.environment) pm.environment.set("estudiante", respData.identificacionEstudiante);'
          );
        }
      }

      // 03.1 Cargar desglose 40 y 60: sync rubroId and rubro2Id to environment
      if (item.name === '03.1 Cargar desglose 40 y 60' && e.listen === 'test') {
        if (!code.includes('pm.environment.set("rubroId"')) {
          code = code.replace(
            'pm.collectionVariables.set("rubro2Id", respData.find(x => x.nombre === "Proyecto").id);',
            'pm.collectionVariables.set("rubro2Id", respData.find(x => x.nombre === "Proyecto").id);\nif (pm.environment) {\n  pm.environment.set("rubroId", pm.collectionVariables.get("rubroId"));\n  pm.environment.set("rubro2Id", pm.collectionVariables.get("rubro2Id"));\n}'
          );
        }
      }

      // 03.3 Reemplazar desglose antes de notas: sync rubroId and rubro2Id
      if (item.name === '03.3 Reemplazar desglose antes de notas' && e.listen === 'test') {
        if (!code.includes('pm.environment.set("rubroId"')) {
          code = code.replace(
            'pm.collectionVariables.set("rubro2Id", respData.find(x => x.nombre === "Proyecto").id);',
            'pm.collectionVariables.set("rubro2Id", respData.find(x => x.nombre === "Proyecto").id);\nif (pm.environment) {\n  pm.environment.set("rubroId", pm.collectionVariables.get("rubroId"));\n  pm.environment.set("rubro2Id", pm.collectionVariables.get("rubro2Id"));\n}'
          );
        }
      }

      // 04.1 Crear factura: sync facturaId to environment
      if (item.name === '04.1 Crear factura' && e.listen === 'test') {
        if (!code.includes('pm.environment.set("facturaId"')) {
          code = code.replace(
            'pm.collectionVariables.set("facturaId", respData.id);',
            'pm.collectionVariables.set("facturaId", respData.id); if (pm.environment) pm.environment.set("facturaId", respData.id);'
          );
        }
      }

      // 05.1 Crear pago: sync pagoId to environment
      if (item.name === '05.1 Crear pago' && e.listen === 'test') {
        if (!code.includes('pm.environment.set("pagoId"')) {
          code = code.replace(
            'pm.collectionVariables.set("pagoId", respData.id);',
            'pm.collectionVariables.set("pagoId", respData.id); if (pm.environment) pm.environment.set("pagoId", respData.id);'
          );
        }
      }

      e.script.exec = code.split('\n');
    });
  }
  if (item.item) item.item.forEach(walk);
}

walk(p5);
fs.writeFileSync(p5Path, JSON.stringify(p5, null, 2) + '\n');
console.log('P5 patched successfully!');

// Also update environments
const envPaths = [
  'docs/pruebas/postman/Persona1_Persona2.postman_environment.json',
  'docs/pruebas/postman/NuevoAvatar_Microservicios.postman_environment.json'
];

for (const ep of envPaths) {
  const env = JSON.parse(fs.readFileSync(ep, 'utf8'));
  function setEnvVar(k, v) {
    const found = env.values.find(x => x.key === k);
    if (found) found.value = v;
    else env.values.push({ key: k, value: v, type: 'default', enabled: true });
  }
  setEnvVar('cursoId', '4');
  setEnvVar('grupoId', '3');
  setEnvVar('periodoId', '1');
  setEnvVar('estudiante', 'P5-JOSE-ESTUDIANTE');
  setEnvVar('grupoPruebasConfirmado', 'true');

  fs.writeFileSync(ep, JSON.stringify(env, null, 2) + '\n');
  console.log(`Env file updated: ${ep}`);
}
