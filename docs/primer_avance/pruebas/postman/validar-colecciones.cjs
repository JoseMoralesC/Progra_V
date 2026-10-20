// Validacion local sin HTTP: no ejecuta CRUD, scripts de Postman ni correo.
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const assert = require('node:assert/strict');
const root = path.resolve(__dirname, '../../../..');
const services = JSON.parse(fs.readFileSync(path.join(root, 'tools/servicios.json'), 'utf8'));
const variables = Object.fromEntries(services.map(s => [s.name, s.name[0].toLowerCase()+s.name.slice(1)+'Url']));
const urls = Object.fromEntries(services.map(s => [variables[s.name], `http://localhost:${s.port}`]));
const owners = {
  login:'Autenticacion', validate:'Autenticacion', refresh:'Autenticacion',
  bitacora:'Bitacora', rol:'Rol', parametro:'Parametro', modulo:'Modulo',
  usuario:'Usuario', institucion:'Institucion', profesor:'Profesor', carrera:'Carrera',
  curso:'Curso', grupo:'Grupo', periodo:'Periodo',
  provincias:'Direccion', cantones:'Direccion', distritos:'Direccion',
  expediente:'Expediente', prematricula:'Prematricula', matricula:'Matricula',
  cargardesglose:'Notas', asignarnotarubro:'Notas', obtenerdesglose:'Notas', obtenernotas:'Notas',
  factura:'Factura', pago:'Pago', notificar:'Notificacion',
  historialacademico:'HistorialAcademico', listadoestudiantes:'ListadoEstudiantes'
};
const files = fs.readdirSync(__dirname).filter(f => f.endsWith('.json')).map(f => path.join(__dirname,f));
files.push(path.join(root,'docs/primer_avance/evidencia/fabian/Persona4_Fabian_Recorrido.postman_collection.json'));
const results = []; let requestCount=0; let scriptCount=0; let environments=0;
for (const file of files) {
  const data = JSON.parse(fs.readFileSync(file,'utf8'));
  if (data._postman_variable_scope === 'environment') {
    const values = new Map(data.values.filter(v=>v.enabled!==false).map(v=>[v.key,v.value]));
    for (const [key,value] of Object.entries(urls)) assert.equal(values.get(key),value,`${path.basename(file)}: ${key}`);
    environments++; continue;
  }
  assert.ok(data.info && Array.isArray(data.item),'Formato de coleccion invalido');
  const values = new Map((data.variable||[]).map(v=>[v.key,v.value]));
  let requests=0; let scripts=0;
  function visit(node) {
    for (const event of node.event||[]) {
      const source=event.script?.exec;
      assert.ok(Array.isArray(source),'Script sin exec');
      new vm.Script(source.join('\n'),{filename:path.basename(file)+':'+(node.name||'coleccion')+':'+event.listen});
      scripts++;
    }
    if(node.request) {
      const match=/^\{\{([A-Za-z]+Url)\}\}\/([^/?]+)/.exec(node.request.url);
      assert.ok(match,'URL sin variable por servicio: '+node.name);
      assert.equal(match[1],variables[owners[match[2]]],'Propietario incorrecto: '+node.name);
      assert.equal(values.get(match[1]),urls[match[1]],'Puerto incorrecto: '+node.name);
      requests++;
    }
    for(const item of node.item||[])visit(item);
  }
  visit(data);requestCount+=requests;scriptCount+=scripts;
  results.push({file:path.relative(root,file).replaceAll('\\','/'),requests,scripts,passed:true});
}
const report={validation:'static',http_requests_sent:0,passed:true,collections:results.length,requests:requestCount,scripts:scriptCount,environments,details:results};
fs.writeFileSync(path.join(root,'docs/primer_avance/pruebas/postman-validacion.json'),JSON.stringify(report,null,2)+'\n');
console.log(`APROBADO: ${results.length} colecciones, ${requestCount} peticiones, ${scriptCount} scripts y ${environments} entornos. Sin enviar HTTP.`);
