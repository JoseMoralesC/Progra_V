const { chromium } = require(require('node:path').resolve(__dirname, '../../../../NuevoAvatar.Estudiante.React/node_modules/@playwright/test'));
const fs = require('node:fs');
const path = require('node:path');
const out = __dirname;
const results = [];
const base = 'http://127.0.0.1:4177';
const expediente = {tipoIdentificacion:'Cedula', identificacion:'101', email:'ana@cuc.cr', nombreCompleto:'Ana Prueba', fechaNacimiento:'2000-01-01', telefonos:['8888-8888'], provinciaId:1, cantonId:2, distritoId:3, otrasSenas:'Casa de prueba'};
async function setup(browser, rol='ESTUDIANTE', count=1) {
  const context = await browser.newContext();
  const page = await context.newPage();
  page.setDefaultTimeout(2500);
  const calls=[];
  const email=rol==='ESTUDIANTE'?'ana@cuc.cr':'ana@cuc.ac.cr';
  const user={email,nombre:'Ana Prueba',idRol:'ROL_PRUEBA',tipoIdentificacion:'Cedula',identificacion:'101'};
  const modulos=['ADM4','ADM5','ADM6','ADM7','ADM8','ADM9','ADM10','ADM11','ADM12','ADM17','ADM22'].map((idModulo,orden)=>({idModulo,nombreModulo:idModulo,activo:true,orden}));
  await page.route('**/*',async route=>{
    const req=route.request(), url=new URL(req.url()), p=url.pathname;
    if(!p.startsWith('/api/')&&!p.startsWith('/gateway'))return route.continue();
    calls.push({path:p,method:req.method()});
    let body=[], status=200;
    if(p==='/api/auth/login'||p==='/api/auth/refresh')body={access_token:'audit-fake',refresh_token:'audit-fake-refresh',usuarioID:email,expires_in:new Date(Date.now()+3600000).toISOString()};
    else if(p.startsWith('/api/users/'))body=user;
    else if(p.startsWith('/api/roles/'))body={idRol:'ROL_PRUEBA',nombreRol:rol};
    else if(p==='/gateway/menu')body=rol==='PROFESOR'?modulos.filter(m=>m.idModulo==='ADM17'):modulos;
    else if(p==='/gateway/perfil')body=user;
    else if(p==='/gateway/perfil/contrasena')status=204;
    else if(p==='/gateway/auth/logout')status=204;
    else if(p==='/gateway/expediente/propio')body=expediente;
    else if(p==='/gateway/mat/provincias')body=[{provinciaId:1,nombre:'Cartago'}];
    else if(p==='/gateway/mat/cantones/1')body=[{provinciaId:1,cantonId:2,nombre:'Central'}];
    else if(p==='/gateway/mat/distritos/1/2')body=[{cantonId:2,distritoId:3,nombre:'Oriental'}];
    else if(p==='/gateway/mat/prematricula/propio/opciones')body={carreras:[{id:1,nombre:'Informatica'}],cursos:[{id:10,carreraId:1,nombre:'Programacion'}],periodos:[{id:20,fechaInicio:'2027-01-01'}]};
    else if(p==='/gateway/mat/prematricula/propio')body=Array.from({length:count},(_,i)=>({id:i+1,carreraId:1,cursos:[10],periodoId:20,observaciones:null}));
    else if(p.startsWith('/gateway/mat/prematricula/propio/')&&req.method()==='DELETE')status=204;
    else if(p==='/gateway/fin/notificar/historial')body={items:[]};
    else if(p==='/gateway/admin/parametro/TAMPAG')body={valor:'15'};
    else if(p==='/gateway/admin/rol')body=[{idRol:'ROL_PRUEBA',nombreRol:rol}];
    else if(p==='/gateway/mat/profesor/grupos')body=[];
    else if(p.startsWith('/gateway/admin/'))body=[];
    else {status=501;body={error:'Sin respuesta simulada para '+p};}
    return route.fulfill({status,contentType:'application/json',body:status===204?undefined:JSON.stringify(body)});
  });
  const login=async(value=email)=>{
    await page.goto(base+'/login');
    await page.getByLabel('Usuario o correo').fill(value);
    await page.getByLabel('Contraseña',{exact:true}).fill('Clave-ficticia1!');
    await page.getByRole('button',{name:'Ingresar',exact:true}).click();
    await page.waitForSelector('.user-name');
    const aviso=page.getByRole('button',{name:'Cerrar notificación',exact:true});
    if(await aviso.count())await aviso.first().click();
  };
  return {page,context,calls,login};
}
async function check(id,owner,title,run){try{const observed=await run();results.push({id,responsable:owner,titulo:title,estado:'aprobada',observado:observed});}catch(e){results.push({id,responsable:owner,titulo:title,estado:'fallida',observado:e.message.split('\n').slice(0,5).join(' ')});}}
function ensure(ok,message){if(!ok)throw new Error(message);}
(async()=>{
 const browser=await chromium.launch({headless:true});
 try{
  for(const rol of ['ADMINISTRADOR','PROFESOR','ESTUDIANTE'])await check('D-login-'+rol,'Jose','Ingreso con respuestas del contrato actual: '+rol,async()=>{const s=await setup(browser,rol);try{await s.login();await s.page.waitForSelector('.user-name');ensure((await s.page.locator('.user-name').innerText())==='Ana Prueba','Sin identidad');return {ruta:new URL(s.page.url()).pathname,llamadas:s.calls};}finally{await s.context.close();}});
  await check('D-recarga','Jose','Conservar la sesión al recargar una cuenta ingresada',async()=>{const s=await setup(browser);try{await s.login();await s.page.waitForSelector('.user-name');await s.page.reload();await s.page.waitForTimeout(150);ensure(!s.page.url().endsWith('/login'),'La recarga envía a /login: AuthContext mantiene la sesión solamente en memoria.');return s.page.url();}finally{await s.context.close();}});
  await check('D-formato-login','Jose','Rechazar un correo sin formato institucional antes de llamar a login',async()=>{const s=await setup(browser);try{await s.login('texto-sin-correo');await s.page.waitForTimeout(200);ensure(!s.calls.some(c=>c.path==='/api/auth/login'),'El formulario envía texto-sin-correo a /api/auth/login.');return 'Solicitud detenida';}finally{await s.context.close();}});
  await check('D-notas','Jose','Abrir la pantalla funcional de Notas desde el portal del profesor',async()=>{const s=await setup(browser,'PROFESOR');try{await s.login();await s.page.waitForSelector('.user-name');await s.page.getByRole('link',{name:'Consultar y registrar notas'}).click();await s.page.waitForURL('**/notas');await s.page.waitForTimeout(150);await s.page.screenshot({path:path.join(out,'notas.png')});ensure(!(await s.page.locator('body').innerText()).includes('requiere integrar su servicio'),'La ruta /notas muestra un mensaje provisional en lugar del componente Notas.');return 'Componente disponible';}finally{await s.context.close();}});
  await check('D-inicio-estudiante','Alejandro','Inicio muestra expediente y cantidad de solicitudes simuladas',async()=>{const s=await setup(browser,'ESTUDIANTE',2);try{await s.login();await s.page.getByText('2 solicitudes',{exact:true}).waitFor();await s.page.getByText('Expediente vinculado',{exact:true}).waitFor();return 'Estado disponible y dos solicitudes';}finally{await s.context.close();}});
  await check('D-confirmar-expediente','Alejandro','Guardar expediente exige confirmación antes de enviar PUT',async()=>{const s=await setup(browser);try{await s.login();await s.page.getByRole('link',{name:'Mi expediente',exact:true}).click();await s.page.getByRole('button',{name:'Editar contacto y dirección'}).click();await s.page.getByLabel('Cantón',{exact:true}).waitFor();await s.page.getByRole('button',{name:'Guardar cambios'}).click();await s.page.waitForTimeout(200);ensure(!s.calls.some(c=>c.path==='/gateway/expediente/propio'&&c.method==='PUT'),'Guardar cambios envió PUT de inmediato, sin confirmación.');return 'Confirmación solicitada';}finally{await s.context.close();}});
  await check('D-confirmar-cancelacion','Alejandro','Cancelar una prematrícula exige confirmación antes de DELETE',async()=>{const s=await setup(browser);try{await s.login();await s.page.getByRole('link',{name:'Mis prematrículas',exact:true}).click();await s.page.getByRole('button',{name:'Cancelar solicitud'}).click();await s.page.waitForTimeout(100);ensure(!s.calls.some(c=>c.method==='DELETE'),'Cancelar solicitud envió DELETE de inmediato, sin confirmación.');return 'Confirmación solicitada';}finally{await s.context.close();}});
  await check('D-paginacion-prematricula','Alejandro','Mostrar como máximo 15 solicitudes por página',async()=>{const s=await setup(browser,'ESTUDIANTE',16);try{await s.login();await s.page.getByRole('link',{name:'Mis prematrículas',exact:true}).click();await s.page.getByRole('heading',{name:'Solicitud #16',exact:true}).waitFor();const n=await s.page.getByRole('article',{name:/Prematrícula/}).count();ensure(n<=15,'Se muestran '+n+' solicitudes simultáneamente; no hay paginación.');return n;}finally{await s.context.close();}});
  await check('D-cambio-clave-salida','Alejandro','Cambio de contraseña retira la sesión y solicita login',async()=>{const s=await setup(browser);try{await s.login();await s.page.getByRole('link',{name:'Mi perfil',exact:true}).click();await s.page.getByLabel('Contraseña actual',{exact:true}).fill('Anterior1!');await s.page.getByLabel('Nueva contraseña',{exact:true}).fill('NuevaClave1!');await s.page.getByLabel('Confirmar nueva contraseña',{exact:true}).fill('NuevaClave1!');await s.page.getByRole('button',{name:'Actualizar contraseña'}).click();await s.page.getByText('Contraseña actualizada correctamente.',{exact:true}).waitFor();ensure(s.page.url().endsWith('/login'),'Tras cambiar contraseña permanece abierta /estudiante/perfil con la sesión activa.');return 'Login requerido';}finally{await s.context.close();}});
  await check('D-admin-sin-permiso-ui','Fabian','Una URL administrativa sin permiso de módulo bloquea sus controles',async()=>{const s=await setup(browser,'PROFESOR');try{await s.login();await s.page.waitForSelector('.user-name');await s.page.evaluate(()=>history.pushState({},'', '/usuarios'));await s.page.evaluate(()=>dispatchEvent(new PopStateEvent('popstate')));await s.page.getByRole('button',{name:'Nuevo usuario'}).waitFor();throw new Error('El profesor abre /usuarios y ve Nuevo usuario; AdminRoleGate solo distingue el perfil general y no el permiso del módulo. El gateway real conserva su rechazo 403.');}finally{await s.context.close();}});
  for(const [ruta,boton] of [['/usuarios','Nuevo usuario'],['/roles','Nuevo rol'],['/parametros','Nuevo parámetro'],['/instituciones','Nueva institución']])await check('D-mantenimiento-'+ruta.slice(1),'Fabian','Abrir formulario administrativo con sesión actual: '+ruta,async()=>{const s=await setup(browser,'ADMINISTRADOR');try{await s.login();await s.page.evaluate(r=>{history.pushState({},'',r);dispatchEvent(new PopStateEvent('popstate'));},ruta);await s.page.getByRole('button',{name:boton,exact:true}).click();await s.page.getByRole('dialog').waitFor();return 'Formulario disponible con catálogos simulados; no se guardaron datos.';}finally{await s.context.close();}});
  await check('D-layout-compartido','Alejandro','Las dos experiencias reutilizan el layout y no desbordan en 390 px',async()=>{const a=await setup(browser,'ADMINISTRADOR'),s=await setup(browser);try{await a.login();await s.login();const medidas={};for(const [name,p] of [['administracion',a.page],['estudiante',s.page]]){await p.setViewportSize({width:390,height:844});await p.screenshot({path:path.join(out,'movil-'+name+'.png'),fullPage:true});medidas[name]=await p.evaluate(()=>({ancho:innerWidth,contenido:document.documentElement.scrollWidth}));}ensure(Object.values(medidas).every(d=>d.contenido<=d.ancho),'Desbordamiento horizontal: '+JSON.stringify(medidas));return medidas;}finally{await a.context.close();await s.context.close();}});
 }finally{await browser.close();fs.writeFileSync(path.join(out,'diagnostico.json'),JSON.stringify({base,limite:'API interceptada, sin SQL ni SMTP',casos:results},null,2));console.log(JSON.stringify(results.map(r=>({id:r.id,estado:r.estado,observado:r.observado})),null,2));}
})().catch(e=>{console.error(e);process.exitCode=1;});
