// Ejecuta las peticiones y aserciones de estas colecciones con Node y sus
// bibliotecas integradas. No es Postman ni Newman: el reporte indica ese alcance.
const fs = require('node:fs');
const path = require('node:path');
const assert = require('node:assert/strict');
const vm = require('node:vm');
const crypto = require('node:crypto');

function expect(value, negate = false) {
  const check = (fn) => { if (negate) assert.throws(fn); else fn(); };
  const api = {
    eql: other => check(() => {
      const normalize = v => v !== null && typeof v === 'object' ? JSON.parse(JSON.stringify(v)) : v;
      assert.deepStrictEqual(normalize(value), normalize(other));
    }),
    a: type => { check(() => assert.equal(Array.isArray(value) ? 'array' : typeof value, type)); return api; },
    an: type => api.a(type),
    include: part => check(() => assert.ok(value.includes(part))),
    lengthOf: n => check(() => assert.equal(value.length, n)),
    property: key => check(() => assert.ok(Object.hasOwn(value, key)))
  };
  for (const key of ['to', 'be', 'have', 'and']) Object.defineProperty(api, key, {get: () => api});
  Object.defineProperty(api, 'not', {get: () => expect(value, !negate)});
  Object.defineProperty(api, 'empty', {get: () => {check(() => assert.equal(value.length, 0)); return api;}});
  return api;
}

async function main() {
  const env = new Map(JSON.parse(fs.readFileSync(path.join(__dirname, 'Persona1_Persona2.postman_environment.json'))).values.map(x => [x.key, x.value]));
  env.set('usuario', process.env.POSTMAN_USER);
  env.set('password', process.env.POSTMAN_PASSWORD);
  if (!env.get('usuario') || !env.get('password')) throw new Error('Configure POSTMAN_USER y POSTMAN_PASSWORD localmente.');
  const report = {fecha: new Date().toISOString(), base: 'PrograV', herramienta: 'Ejecutor HTTP local Node; no Postman/Newman', preparacion: [], colecciones: []};
  const call = (url, options) => fetch(url, {...options, signal: AbortSignal.timeout(30000)});
  const login = await call(env.get('seguridadUrl')+'/login', {method:'POST', headers:{usuario:env.get('usuario'), contrasena:env.get('password')}});
  assert.equal(login.status, 201, 'Login de preparación');
  const loginBody = await login.json();
  const auth = {Authorization:'Bearer '+loginBody.access_token};
  let domain = await call(env.get('seguridadUrl')+'/parametro/DOMPROF', {headers:auth});
  if (domain.status === 404) {
    domain = await call(env.get('seguridadUrl')+'/parametro', {method:'POST', headers:{...auth,'Content-Type':'application/json'}, body:JSON.stringify({idParametro:'DOMPROF',valor:env.get('dominioProfesor')})});
    assert.equal(domain.status,201,'Creación de DOMPROF');
    report.preparacion.push({parametro:'DOMPROF',accion:'Creado porque faltaba; dato inicial definido en 04_datos_iniciales.sql',estado:201});
  } else {
    assert.equal(domain.status,200,'Lectura de DOMPROF');
    report.preparacion.push({parametro:'DOMPROF',accion:'Conservado',estado:200});
  }
  env.set('dominioProfesor',(await domain.json()).valor);
  const facade = map => ({get:key=>map.get(key),set:(key,value)=>map.set(key,value),unset:key=>map.delete(key)});
  for (const file of ['Persona1_Seguridad.postman_collection.json','Persona2_Ramses.postman_collection.json']) {
    const collection = JSON.parse(fs.readFileSync(path.join(__dirname,file),'utf8'));
    const local = new Map((collection.variable||[]).map(x=>[x.key,x.value]));
    const output = {archivo:file,peticiones:[]}; report.colecciones.push(output);
    const walk = async (items, group='') => {
      for (const item of items) {
        if (item.item) { await walk(item.item,item.name); continue; }
        const record = {grupo:group,nombre:item.name,metodo:item.request.method,estado:null,aserciones:[]}; output.peticiones.push(record);
        const scope = new Map(); let skipped = false;
        const replace = text => text.replace(/\{\{([^}]+)\}\}/g, (_,key) => key==='$guid' ? crypto.randomUUID() : String(scope.get(key)??local.get(key)??env.get(key)??''));
        const pm = {
          environment:facade(env), collectionVariables:facade(local),
          variables:{...facade(scope),replaceIn:replace},
          execution:{skipRequest:()=>{skipped=true;}}, expect,
          test:(name,fn)=>{try{fn();record.aserciones.push({nombre:name,aprobada:true});}catch(e){record.aserciones.push({nombre:name,aprobada:false,error:e.message});}}
        };
        const context = vm.createContext({pm,Date,Math,Array,String,Number,Error});
        try {
          for (const event of item.event||[]) if(event.listen==='prerequest') vm.runInContext(event.script.exec.join('\n'),context,{timeout:1000});
          if(skipped){record.omitida=true;continue;}
          const headers = Object.fromEntries((item.request.header||[]).map(x=>[x.key,replace(x.value)]));
          if (item.request.auth?.type!=='noauth') headers.Authorization='Bearer '+env.get('token');
          const url = replace(item.request.url);
          // El reporte conserva rutas de plantilla; nunca credenciales ni JWT.
          record.ruta=item.request.url;
          const response=await call(url,{method:item.request.method,headers,body:item.request.body?replace(item.request.body.raw):undefined});
          const text=await response.text(); record.estado=response.status;
          pm.response={code:response.status,json:()=>JSON.parse(text),to:{have:{status:n=>assert.equal(response.status,n)}}};
          for(const event of item.event||[])if(event.listen==='test')vm.runInContext(event.script.exec.join('\n'),context,{timeout:1000});
          // No se exportan respuestas completas: contienen datos personales o
          // bitácoras del equipo. Las aserciones verifican los campos en memoria.
        } catch(e) {record.error=e.message;}
        console.log(`${collection.info.name.split(' - ')[0]} | ${record.estado||'ERROR'} | ${item.name} | ${record.aserciones.some(x=>!x.aprobada)||record.error?'FALLO':'OK'}`);
      }
    };
    await walk(collection.item);
  }
  const all=report.colecciones.flatMap(x=>x.peticiones);
  report.resumen={peticiones:all.length,omitidas:all.filter(x=>x.omitida).length,aserciones:all.flatMap(x=>x.aserciones).length,fallos:all.filter(x=>x.error||x.aserciones.some(a=>!a.aprobada)).length};
  const outputPath=process.env.HTTP_REPORT_PATH;
  if(!outputPath)throw new Error('Configure HTTP_REPORT_PATH fuera de los archivos versionados.');
  fs.mkdirSync(path.dirname(outputPath),{recursive:true});
  fs.writeFileSync(outputPath,JSON.stringify(report,null,2)+'\n');
  console.log(JSON.stringify(report.resumen));
  if(report.resumen.fallos||report.resumen.omitidas)process.exitCode=1;
}
main().catch(e=>{console.error(e.message);process.exitCode=1;});
