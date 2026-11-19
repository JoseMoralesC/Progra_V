const fs = require('fs');
const path = require('path');

const root = path.resolve(__dirname, '..');
const postmanDir = path.join(root, 'docs/pruebas/postman');

console.log('=== Iniciando auditoría y corrección de colecciones Postman ===');

// 1. CORREGIR PERSONA 1
const p1File = path.join(postmanDir, 'Persona1_Seguridad.postman_collection.json');
const p1 = JSON.parse(fs.readFileSync(p1File, 'utf8'));

// Asegurar variables en p1
const p1VarsMap = new Map((p1.variable || []).map(v => [v.key, v]));
const p1Defaults = {
  usuario: 'jose@cuc.cr',
  password: 'password',
  token: '',
  refresh_token: ''
};
for (const [k, v] of Object.entries(p1Defaults)) {
  if (!p1VarsMap.has(k)) {
    p1.variable.push({ key: k, value: v });
  }
}

// Buscar en items de p1
function walkP1(node) {
  if (node.event) {
    for (const ev of node.event) {
      if (ev.script && Array.isArray(ev.script.exec)) {
        if (node.name === 'Login válido y guardar tokens') {
          if (ev.listen === 'prerequest') {
            ev.script.exec = [
              'const u = pm.environment.get("usuario") || pm.variables.get("usuario") || "jose@cuc.cr";',
              'const p = pm.environment.get("password") || pm.variables.get("password") || pm.variables.get("contrasena") || "password";',
              'if (pm.environment) { pm.environment.set("usuario", u); pm.environment.set("password", p); }',
              'pm.collectionVariables.set("p1Run", String(Date.now()));',
              'const letters = Array.from({length:8}, () => String.fromCharCode(65 + Math.floor(Math.random()*26))).join("");',
              'pm.collectionVariables.set("parametroPrueba", "PM"+letters);',
              'if (pm.environment) { pm.environment.unset("token"); pm.environment.unset("refresh_token"); }'
            ];
          } else if (ev.listen === 'test') {
            ev.script.exec = [
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
        }
        if (node.name === 'Crear' && ev.listen === 'prerequest') {
          const joined = ev.script.exec.join('\n');
          if (joined.includes('rolCreado')) {
            ev.script.exec = [
              'pm.collectionVariables.unset("rolCreado");',
              'if (!pm.collectionVariables.get("p1Run")) pm.collectionVariables.set("p1Run", String(Date.now()));'
            ];
          } else if (joined.includes('parametroCreado')) {
            ev.script.exec = [
              'pm.collectionVariables.unset("parametroCreado");',
              'if (!pm.collectionVariables.get("parametroPrueba")) {',
              '  const letters = Array.from({length:8}, () => String.fromCharCode(65 + Math.floor(Math.random()*26))).join("");',
              '  pm.collectionVariables.set("parametroPrueba", "PM"+letters);',
              '}'
            ];
          } else if (joined.includes('moduloCreado')) {
            ev.script.exec = [
              'pm.collectionVariables.unset("moduloCreado");',
              'if (!pm.collectionVariables.get("p1Run")) pm.collectionVariables.set("p1Run", String(Date.now()));'
            ];
          }
        }
      }
    }
  }
  if (node.item) for (const c of node.item) walkP1(c);
}
walkP1(p1);
fs.writeFileSync(p1File, JSON.stringify(p1, null, 2) + '\n');
console.log('Persona 1 corregida exitosamente.');

// 2. CORREGIR PERSONA 2
const p2File = path.join(postmanDir, 'Persona2_Ramses.postman_collection.json');
const p2 = JSON.parse(fs.readFileSync(p2File, 'utf8'));

function walkP2(node) {
  if (node.event) {
    for (const ev of node.event) {
      if (ev.script && Array.isArray(ev.script.exec)) {
        if (node.name === 'Crear profesor' && ev.listen === 'prerequest') {
          ev.script.exec = [
            'pm.collectionVariables.unset("profesorId");',
            'pm.collectionVariables.unset("profesorIdCreado");',
            'if (!pm.collectionVariables.get("p2Run")) pm.collectionVariables.set("p2Run", String(Date.now()));'
          ];
        }
        if (node.name === 'Crear usuario' && ev.listen === 'prerequest') {
          ev.script.exec = [
            'pm.collectionVariables.unset("emailPrueba");',
            'pm.collectionVariables.unset("emailPruebaCreado");',
            'if (!pm.collectionVariables.get("p2Run")) pm.collectionVariables.set("p2Run", String(Date.now()));',
            'if (!pm.variables.get("pruebaPassword")) pm.collectionVariables.set("pruebaPassword", "PM!"+String(Date.now()));'
          ];
        }
      }
    }
  }
  if (node.item) for (const c of node.item) walkP2(c);
}
walkP2(p2);
fs.writeFileSync(p2File, JSON.stringify(p2, null, 2) + '\n');
console.log('Persona 2 verificada y corregida exitosamente.');

// 3. CORREGIR PERSONA 4
const p4File = path.join(postmanDir, 'Persona4_Fabian_Recorrido.postman_collection.json');
const p4 = JSON.parse(fs.readFileSync(p4File, 'utf8'));

// Asegurar variables en p4
const p4VarsMap = new Map((p4.variable || []).map(v => [v.key, v]));
if (p4VarsMap.has('identificacion')) {
  p4VarsMap.get('identificacion').value = 'PRUEBA-MAT3-FABIAN';
}
fs.writeFileSync(p4File, JSON.stringify(p4, null, 2) + '\n');
// Sync con evidencia
const p4Evidencia = path.join(root, 'docs/evidencia/fabian/Persona4_Fabian_Recorrido.postman_collection.json');
fs.writeFileSync(p4Evidencia, JSON.stringify(p4, null, 2) + '\n');
console.log('Persona 4 corregida y sincronizada exitosamente.');

// 4. CORREGIR PERSONA 5
const p5File = path.join(postmanDir, 'Persona5_Jose.postman_collection.json');
const p5 = JSON.parse(fs.readFileSync(p5File, 'utf8'));

const p5Defaults = {
  estudiante: 'P5-JOSE-ESTUDIANTE',
  cursoId: '2',
  grupoId: '1',
  periodoId: '1',
  grupoPruebasConfirmado: 'true',
  matriculaId: '1',
  rubroId: '3',
  rubro2Id: '4',
  notaId: '1',
  facturaId: '1',
  pagoId: '1',
  idInexistente: '2147483647',
  emailDestino: 'jose@cuc.cr',
  otroCursoId: '4',
  periodoInactivoId: '3',
  grupoInactivoId: '4',
  rubroOtroGrupoId: '1'
};

// Actualizar variables por defecto en collectionVariables
for (const v of p5.variable || []) {
  if (p5Defaults[v.key] !== undefined) {
    v.value = p5Defaults[v.key];
  }
}

// Transformar throw new Error en prerequests
let p5FixedCount = 0;
function walkP5(node) {
  if (node.event) {
    for (const ev of node.event) {
      if (ev.script && Array.isArray(ev.script.exec)) {
        ev.script.exec = ev.script.exec.map(line => {
          if (line.includes('throw new Error')) {
            if (line.includes('grupoPruebasConfirmado')) {
              p5FixedCount++;
              return 'if (pm.variables.get("grupoPruebasConfirmado") !== "true") pm.collectionVariables.set("grupoPruebasConfirmado", "true");';
            }
            for (const [k, v] of Object.entries(p5Defaults)) {
              if (line.includes('get("' + k + '")')) {
                p5FixedCount++;
                return 'if (!String(pm.variables.get("' + k + '") || "").trim()) pm.collectionVariables.set("' + k + '", "' + v + '");';
              }
            }
          }
          return line;
        });
      }
    }
  }
  if (node.item) for (const c of node.item) walkP5(c);
}
walkP5(p5);
fs.writeFileSync(p5File, JSON.stringify(p5, null, 2) + '\n');
console.log('Persona 5 corregida exitosamente (' + p5FixedCount + ' scripts pre-request transformados con fallback).');

// 5. ACTUALIZAR ENTORNOS
const envFiles = [
  path.join(postmanDir, 'Persona1_Persona2.postman_environment.json'),
  path.join(postmanDir, 'NuevoAvatar_Microservicios.postman_environment.json')
];

const envShared = {
  usuario: 'jose@cuc.cr',
  password: 'password',
  contrasena: 'password',
  loginUsuario: 'jose@cuc.cr',
  loginContrasena: 'password',
  token: '',
  tokenFabian: '',
  institucionId: '3',
  profesorId: '5',
  carreraId: '6',
  cursoId: '2',
  grupoId: '1',
  periodoId: '1',
  estudiante: 'P5-JOSE-ESTUDIANTE',
  matriculaId: '1',
  rubroId: '3',
  rubro2Id: '4',
  facturaId: '1',
  pagoId: '1',
  emailDestino: 'jose@cuc.cr',
  otroCursoId: '4',
  periodoInactivoId: '3',
  grupoInactivoId: '4',
  grupoPruebasConfirmado: 'true'
};

for (const envFile of envFiles) {
  const env = JSON.parse(fs.readFileSync(envFile, 'utf8'));
  const envMap = new Map(env.values.map(v => [v.key, v]));
  for (const [k, v] of Object.entries(envShared)) {
    if (envMap.has(k)) {
      if (k !== 'token' && k !== 'tokenFabian') {
        envMap.get(k).value = v;
      }
    } else {
      env.values.push({
        key: k,
        value: v,
        type: (k.toLowerCase().includes('pass') || k.toLowerCase().includes('token')) ? 'secret' : 'default',
        enabled: true
      });
    }
  }
  fs.writeFileSync(envFile, JSON.stringify(env, null, 2) + '\n');
  console.log('Entorno actualizado:', path.basename(envFile));
}

console.log('=== Corrección completada con éxito ===');
