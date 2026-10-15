"""Prueba servicios reales con bases temporales y sus cuentas de ejecucion.

Requiere las 22 aplicaciones compiladas y tools/preparar-cuentas.py ejecutado.
No envia correos. No escribe en PrograV ni en las 22 bases de negocio.
Las bases temporales creadas por esta ejecucion se eliminan al terminar.
"""
import concurrent.futures
import argparse
from datetime import datetime,timezone
import http.server
import importlib.util
import json
import os
from pathlib import Path
import re
import socket
import subprocess
import threading
import time
import urllib.error
import urllib.request
import uuid

ROOT=Path(__file__).resolve().parent.parent
spec=importlib.util.spec_from_file_location("migration",ROOT/"tools/migrar-bases.py")
m=importlib.util.module_from_spec(spec);spec.loader.exec_module(m)


def cleanup_report(report_path):
    """Reintenta solamente la limpieza de bases identificadas en un reporte local."""
    path=Path(report_path).resolve()
    if path.parent != (ROOT/"docs/pruebas").resolve():
        raise ValueError("El reporte debe estar en docs/pruebas")
    report=json.loads(path.read_text(encoding="utf-8"))
    databases=report["temporary_databases"]
    services=json.loads((ROOT/"db/por_servicio/distribucion.json").read_text())
    prefixes=set()
    for database in databases:
        match=re.fullmatch(r"(PrograV_IT_[0-9a-f]{8}_)([A-Za-z]+)",database)
        if not match or match[2] not in services:
            raise ValueError("El reporte contiene un destino de limpieza no autorizado")
        prefixes.add(match[1])
    if len(prefixes)!=1 or len(databases)!=len(set(databases)):
        raise ValueError("El reporte debe identificar una sola ejecucion de prueba")
    with m.connect(m.configuration(),"master",autocommit=True) as connection:
        cursor=connection.cursor()
        for database in databases:
            if cursor.execute("SELECT DB_ID(?)",database).fetchval() is None:
                continue
            cursor.execute("ALTER DATABASE "+m.quote(database)+" SET SINGLE_USER WITH ROLLBACK IMMEDIATE")
            cursor.execute("DROP DATABASE "+m.quote(database))
    report["temporary_databases_cleaned"]=True
    report["cleanup_completed_at_utc"]=datetime.now(timezone.utc).isoformat()
    path.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding="utf-8")
    print("Limpieza completada: solamente bases temporales de la ejecucion indicada.")


class Suite:
    def __init__(self):
        self.cfg=m.configuration()
        self.services={s["name"]:s for s in json.loads((ROOT/"tools/servicios.json").read_text())}
        self.manifest=json.loads((ROOT/"db/por_servicio/distribucion.json").read_text())
        self.prefix="PrograV_IT_"+uuid.uuid4().hex[:8]+"_"
        self.created=[];self.processes={};self.logs={};self.results=[];self.token=None
        self.folder=ROOT/"tmp"/self.prefix;self.folder.mkdir(parents=True,exist_ok=False)
        self.created_at=datetime.now(timezone.utc).isoformat()
        self.proxy=None;self.cleaned=False

    def check(self,label,condition,**details):
        self.results.append({"test":label,"passed":bool(condition),**details})
        if not condition:raise AssertionError(label)

    def request(self,service,path,method="GET",body=None,internal=False,authorized=True,headers=None):
        port=self.services[service]["port"]
        values={"Content-Type":"application/json"}
        if internal:values["X-Servicio-Key"]=self.cfg["SERVICES_INTERNAL_KEY"]
        elif authorized and self.token:values["Authorization"]="Bearer "+self.token
        values.update(headers or {})
        data=None if body is None else json.dumps(body).encode()
        request=urllib.request.Request(f"http://localhost:{port}"+path,data=data,headers=values,method=method)
        try:
            with urllib.request.urlopen(request,timeout=25) as response:
                raw=response.read();return response.status,json.loads(raw) if raw else None
        except urllib.error.HTTPError as error:
            raw=error.read()
            try:value=json.loads(raw)
            except ValueError:value=None
            return error.code,value

    def prepare(self):
        owners={table:service for service,tables in self.manifest.items() for table in tables}
        with m.connect(self.cfg,self.cfg["DB_NAME"],autocommit=True) as connection:
            self.model=m.inspect_source(connection.cursor(),owners)
        with m.connect(self.cfg,"master",autocommit=True) as admin:
            cursor=admin.cursor()
            for service,tables in self.manifest.items():
                database=self.prefix+service
                if cursor.execute("SELECT DB_ID(?)",database).fetchval() is not None:raise ValueError("Destino temporal existente")
                cursor.execute("CREATE DATABASE "+m.quote(database)+" COLLATE "+self.model["collation"])
                self.created.append(database)
                cursor.execute("USE "+m.quote(database))
                for statement in m.schema_statements(self.model,tables):cursor.execute(statement)
                for table in tables:
                    columns=",".join(m.quote(c["name"]) for c in self.model["tables"][table]["columns"])
                    identity=any(c["is_identity"] for c in self.model["tables"][table]["columns"])
                    if identity:cursor.execute("SET IDENTITY_INSERT "+m.table_name(table)+" ON")
                    cursor.execute("INSERT "+m.table_name(table)+" ("+columns+") SELECT "+columns+" FROM "+m.quote(self.cfg["DB_NAME"])+"."+m.table_name(table))
                    if identity:cursor.execute("SET IDENTITY_INSERT "+m.table_name(table)+" OFF")
                for fk in self.model["foreign_keys"]:
                    if fk["local"] and fk["parent_table"] in tables:cursor.execute(m.foreign_key_ddl(fk))
                login=self.cfg["SERVICE_DB_USER_"+service.upper()]
                cursor.execute("CREATE USER "+m.quote(login)+" FOR LOGIN "+m.quote(login))
                cursor.execute("ALTER ROLE db_datareader ADD MEMBER "+m.quote(login))
                cursor.execute("ALTER ROLE db_datawriter ADD MEMBER "+m.quote(login))
                cursor.execute("USE [master]")
        with m.connect(self.cfg,self.prefix+"Pago",autocommit=True) as connection:
            script=(ROOT/"db/por_servicio/migraciones/001_operaciones_pago.sql").read_text()
            for batch in re.split(r"(?m)^GO\s*$",script):
                if batch.strip() and not batch.strip().startswith("USE "):connection.cursor().execute(batch)
        import bcrypt
        self.password=uuid.uuid4().hex+"!Aa9"
        password_hash=bcrypt.hashpw(self.password.encode(),bcrypt.gensalt()).decode()
        with m.connect(self.cfg,self.prefix+"Usuario",autocommit=True) as connection:
            connection.cursor().execute("INSERT seguridad.usuario(email,tipo_identificacion,identificacion,nombre,id_rol,password_hash) VALUES(?,?,?,?,?,?)",
                "integracion@cuc.ac.cr","LOCAL","IT-ADMIN","Administrador Integracion","ADMIN",password_hash)
        with m.connect(self.cfg,self.prefix+"Periodo",autocommit=True) as connection:
            connection.cursor().execute("UPDATE academico.Periodo SET FechaInicio=DATEADD(day,-30,CONVERT(date,GETDATE())),FechaFin=DATEADD(day,30,CONVERT(date,GETDATE())) WHERE PeriodoId=1")
        with m.connect(self.cfg,self.prefix+"Parametro",autocommit=True) as connection:
            for name,value in [("EXPJWT","60"),("EXPREFRESH","120")]:
                connection.cursor().execute("IF EXISTS(SELECT 1 FROM seguridad.parametro WHERE id_parametro=?) UPDATE seguridad.parametro SET valor=? WHERE id_parametro=? ELSE INSERT seguridad.parametro(id_parametro,valor) VALUES(?,?)",name,value,name,name,value)
        print("Bases temporales preparadas; origen y destinos de negocio conservados.",flush=True)

    def isolation(self):
        for service in self.manifest:
            scoped=dict(self.cfg,DB_USERNAME=self.cfg["SERVICE_DB_USER_"+service.upper()],DB_PASSWORD=self.cfg["SERVICE_DB_PASSWORD_"+service.upper()])
            with m.connect(scoped,"PrograV_"+service,autocommit=True) as connection:
                cursor=connection.cursor()
                accessible=cursor.execute("SELECT DB_NAME(),IS_MEMBER('db_owner'),HAS_DBACCESS(?)",self.cfg["DB_NAME"]).fetchone()
                forbidden=[other for other in self.manifest if other!=service and cursor.execute("SELECT HAS_DBACCESS(?)","PrograV_"+other).fetchval()]
                self.check("Aislamiento SQL "+service,accessible[0]=="PrograV_"+service and accessible[1]==0 and not accessible[2] and not forbidden)
        print("22 cuentas comprobadas: sin db_owner ni acceso a bases ajenas.",flush=True)

    def start(self,service,extra=None):
        if service in self.processes:return
        info=self.services[service];folder=ROOT/("NuevoAvatar."+service)
        with socket.socket() as sock:
            if sock.connect_ex(("localhost",info["port"]))==0:raise RuntimeError("Puerto ocupado: "+service)
        env=dict(os.environ,**self.cfg)
        env.update(SERVICE_DB_NAME=self.prefix+service,DB_USERNAME=self.cfg["SERVICE_DB_USER_"+service.upper()],DB_PASSWORD=self.cfg["SERVICE_DB_PASSWORD_"+service.upper()],DB_USER=self.cfg["SERVICE_DB_USER_"+service.upper()])
        env["DB_URL"]="jdbc:sqlserver://"+self.cfg["DB_HOST"]+":"+self.cfg.get("DB_PORT","1433")+";databaseName="+env["SERVICE_DB_NAME"]+";encrypt=true;trustServerCertificate=true"
        env.update(SPRING_DATASOURCE_URL=env["DB_URL"],SPRING_DATASOURCE_USERNAME=env["DB_USERNAME"],SPRING_DATASOURCE_PASSWORD=env["DB_PASSWORD"],ASPNETCORE_ENVIRONMENT="Production",DOTNET_ENVIRONMENT="Production",DEBUG="false",TRACE="false",ASPNETCORE_URLS="http://localhost:"+str(info["port"]))
        env.update(extra or {})
        if info["kind"]=="dotnet":
            project=next(folder.glob("*.csproj"));dll=folder/"bin/Debug/net8.0"/(project.stem+".dll")
            command=["dotnet",str(dll)]
        else:
            jar=next(p for p in (folder/"target").glob("*.jar") if not p.name.endswith(".original"))
            command=["java","-Xms32m","-Xmx128m","-XX:MaxMetaspaceSize=128m","-XX:ReservedCodeCacheSize=32m","-Xss512k","-jar",str(jar),"--logging.level.root=WARN","--spring.main.banner-mode=off"]
        log=(self.folder/(service+".log")).open("ab");self.logs[service]=log
        process=subprocess.Popen(command,cwd=folder,env=env,stdout=log,stderr=subprocess.STDOUT,creationflags=subprocess.CREATE_NO_WINDOW)
        self.processes[service]=process
        deadline=time.monotonic()+120
        while time.monotonic()<deadline:
            if process.poll() is not None:raise RuntimeError("Fallo de arranque "+service+"; revisar log local en tmp")
            try:
                status,result=self.request(service,"/interno/estado",internal=True)
                if status==200:
                    self.check("Base propia "+service,result["base"]==self.prefix+service)
                    return
            except (OSError,TimeoutError):pass
            time.sleep(.5)
        raise RuntimeError("Tiempo de arranque agotado: "+service)

    def stop(self,*services):
        for service in services:
            process=self.processes.pop(service,None)
            if process:
                process.terminate()
                try:process.wait(timeout=15)
                except subprocess.TimeoutExpired:process.kill();process.wait(timeout=10)
                self.logs.pop(service).close()

    def token_refresh(self):
        status,data=self.request("Autenticacion","/login","POST",authorized=False,headers={"usuario":"integracion@cuc.ac.cr","contrasena":self.password})
        self.check("Login mediante Usuario y Parametro",status==201)
        self.token=data["access_token"]
        status,data=self.request("Autenticacion","/refresh","POST",{"refresh_token":data["refresh_token"]})
        self.check("Rotacion de refresh token",status==201)
        self.token=data["access_token"]

    def smoke(self):
        resources={"Rol":"/rol","Modulo":"/modulo","Institucion":"/institucion","Profesor":"/profesor","Carrera":"/carrera","Curso":"/curso","Periodo":"/periodo","Grupo":"/grupo","Direccion":"/provincias","Matricula":"/matricula?cursoId=4&grupoId=3","Notas":"/obtenerdesglose?grupoId=3","Factura":"/factura/1","Pago":"/pago?periodoId=1","Notificacion":None}
        for service,path in resources.items():
            self.start(service)
            status,_=self.request(service,"/interno/estado",authorized=False)
            self.check("API interna rechaza falta de clave "+service,status==401)
            if path:
                status,_=self.request(service,path,authorized=False);self.check("Sin JWT "+service,status==401)
                status,_=self.request(service,path);self.check("Consulta autenticada "+service,status==200,status=status)
            else:
                status,_=self.request(service,"/notificar","POST",{"email":"invalido","asunto":"prueba","cuerpoHtml":"<p>Prueba</p>"});self.check("Correo invalido sin enviar SMTP",status==400)
            self.stop(service)
        print("Consultas de catalogos y seguridad comprobadas.",flush=True)

    def academic(self):
        self.token_refresh()
        for service in ["Direccion","Expediente"]:self.start(service)
        student={"tipoIdentificacion":"LOCAL","identificacion":"IT-ESTUDIANTE","email":"integracion@cuc.cr","nombreCompleto":"Estudiante Integracion","fechaNacimiento":"1990-01-01","provinciaId":1,"cantonId":1,"distritoId":1,"otrasSenas":"Prueba de integracion","telefonos":["88881111"]}
        status,_=self.request("Expediente","/expediente","POST",student);self.check("Crear expediente mediante Direccion y Parametro",status==201,status=status)
        self.stop("Direccion")
        for service in ["Curso","Grupo","Periodo","Profesor"]:self.start(service)
        status,group=self.request("Grupo","/grupo","POST",{"numeroGrupo":9,"cursoId":4,"profesorId":5,"horario":"Lunes","cupo":20,"periodoId":1})
        self.check("Crear grupo con referencias por API",status==201,status=status)
        self.group=group.get("grupoId",group.get("GrupoId"))
        status,_=self.request("Grupo","/grupo","POST",{"numeroGrupo":10,"cursoId":4,"profesorId":999999,"horario":"Lunes","cupo":20,"periodoId":1})
        self.check("Grupo rechaza profesor inexistente",status==400,status=status)
        self.stop("Profesor")
        self.start("Matricula")
        payload={"identificacionEstudiante":"NO-EXISTE","cursoId":4,"grupoId":self.group,"periodoId":1}
        status,_=self.request("Matricula","/matricula","POST",payload);self.check("Matricula rechaza estudiante inexistente",status==422,status=status)
        payload["identificacionEstudiante"]="IT-ESTUDIANTE"
        status,enrollment=self.request("Matricula","/matricula","POST",payload);self.check("Crear matricula mediante cuatro propietarios",status==201,status=status)
        self.enrollment=enrollment["id"]
        self.stop("Periodo")
        self.start("Notas")
        status,rubrics=self.request("Notas","/cargardesglose","POST",{"grupoId":self.group,"rubros":[{"nombre":"Proyecto","porcentaje":100}]})
        self.check("Crear desglose con grupo remoto",status==200,status=status)
        status,_=self.request("Notas","/asignarnotarubro","POST",{"matriculaId":self.enrollment,"rubroId":rubrics[0]["id"],"nota":85})
        self.check("Asignar nota con matricula remota",status==200,status=status)
        self.start("HistorialAcademico")
        status,history=self.request("HistorialAcademico","/historialacademico?tipoIdentificacion=LOCAL&identificacion=IT-ESTUDIANTE")
        self.check("Historial ponderado mediante APIs",status==200 and len(history)==1 and float(history[0].get("promedio",-1))==85,status=status)
        self.stop("HistorialAcademico","Notas")
        self.start("Carrera");self.start("ListadoEstudiantes")
        status,listing=self.request("ListadoEstudiantes","/listadoestudiantes?periodoId=1")
        self.check("Listado compuesto mediante APIs",status==200 and any(row.get("identificacion")=="IT-ESTUDIANTE" for row in listing),status=status)
        self.stop("ListadoEstudiantes","Carrera","Grupo","Curso")
        self.start("Prematricula");self.start("Carrera");self.start("Curso");self.start("Periodo")
        status,_=self.request("Prematricula","/prematricula");self.check("Prematricula consulta su base propia",status==200)
        status,_=self.request("Prematricula","/prematricula","POST",{"identificacionEstudiante":"IT-ESTUDIANTE","carreraId":3,"cursos":[4],"periodoId":1})
        self.check("Prematricula valida periodo futuro via API",status==400,status=status)
        self.stop("Carrera","Curso","Periodo")
        self.start("Factura")
        status,_=self.request("Expediente","/expediente/IT-ESTUDIANTE","DELETE")
        # Todas las dependencias de borrado deben responder antes de eliminar.
        self.check("Eliminar estudiante referenciado se rechaza",status==409,status=status)
        self.stop("Prematricula","Carrera","Curso","Periodo","Expediente")
        print("Matricula, notas y consultas academicas comprobadas.",flush=True)

    def financial(self):
        self.token_refresh();self.start("Factura")
        with concurrent.futures.ThreadPoolExecutor(max_workers=2) as pool:
            creations=list(pool.map(lambda _:self.request("Factura","/factura","POST",{"matriculaId":self.enrollment}),range(2)))
        self.check("Creacion concurrente no duplica facturas",sorted(code for code,_ in creations)==[201,409],statuses=[code for code,_ in creations],reasons=[body.get("message") if isinstance(body,dict) else None for _,body in creations])
        status,invoice=next((item for item in creations if item[0]==201),(0,None))
        self.check("Factura obtiene matricula mediante API",status==201 and float(invoice["total"])==30600,status=status)
        self.invoice=invoice["id"]
        suite=self
        class Proxy(http.server.BaseHTTPRequestHandler):
            fail=True
            def log_message(self,*args):pass
            def do_GET(self):self.forward()
            def do_POST(self):
                if Proxy.fail and self.path.startswith("/interno/operaciones-pago/"):
                    self.send_response(503);self.end_headers();return
                self.forward()
            def forward(self):
                length=int(self.headers.get("Content-Length","0"));body=self.rfile.read(length) if length else None
                request=urllib.request.Request("http://localhost:8093"+self.path,data=body,headers={"Content-Type":"application/json","X-Servicio-Key":suite.cfg["SERVICES_INTERNAL_KEY"]},method=self.command)
                try:response=urllib.request.urlopen(request,timeout=20)
                except urllib.error.HTTPError as error:response=error
                with response:
                    self.send_response(response.code);self.send_header("Content-Type","application/json");self.end_headers();self.wfile.write(response.read())
        self.proxy=http.server.ThreadingHTTPServer(("localhost",18093),Proxy)
        threading.Thread(target=self.proxy.serve_forever,daemon=True).start()
        self.start("Pago",{"FACTURA_BASE_URL":"http://localhost:18093"})
        status,_=self.request("Pago","/pago","POST",{"facturaId":self.invoice})
        self.check("Fallo de Factura conserva operacion pendiente",status==503,status=status)
        with m.connect(self.cfg,self.prefix+"Pago",autocommit=True) as connection:
            cursor=connection.cursor();count=cursor.execute("SELECT COUNT(*) FROM finanzas.OperacionPago WHERE FacturaId=? AND Estado='PENDIENTE'",self.invoice).fetchval()
            self.check("Outbox persistida antes de comunicar factura",count==1)
        self.stop("Pago");Proxy.fail=False;self.start("Pago",{"FACTURA_BASE_URL":"http://localhost:18093"})
        deadline=time.monotonic()+40;recovered=False
        while time.monotonic()<deadline:
            with m.connect(self.cfg,self.prefix+"Pago",autocommit=True) as connection:
                recovered=connection.cursor().execute("SELECT COUNT(*) FROM finanzas.OperacionPago WHERE FacturaId=? AND Estado='COMPLETADA'",self.invoice).fetchval()==1
            if recovered:break
            time.sleep(1)
        self.check("Recuperacion automatica despues de reiniciar Pago",recovered)
        status,invoice=self.request("Factura","/factura/"+str(self.invoice));self.check("Factura pagada tras recuperacion",status==200 and invoice["estado"]=="PAGADA")
        with m.connect(self.cfg,self.prefix+"Pago",autocommit=True) as connection:
            cursor=connection.cursor();payment=cursor.execute("SELECT PagoId FROM finanzas.Pago WHERE FacturaId=? AND Estado='APLICADO'",self.invoice).fetchval()
            self.check("Un solo pago pese a reintentos",cursor.execute("SELECT COUNT(*) FROM finanzas.Pago WHERE FacturaId=?",self.invoice).fetchval()==1)
        status,_=self.request("Pago","/pago/"+str(payment)+"/reversar","PUT")
        self.check("Reverso sincroniza factura",status in [200,503],status=status)
        deadline=time.monotonic()+30
        while time.monotonic()<deadline:
            status,invoice=self.request("Factura","/factura/"+str(self.invoice))
            if invoice["estado"]=="PENDIENTE":break
            time.sleep(1)
        self.check("Factura pendiente despues del reverso",invoice["estado"]=="PENDIENTE")
        # Repetir la entrega de un evento antiguo nunca debe volver a pagarla.
        with m.connect(self.cfg,self.prefix+"Pago",autocommit=True) as connection:
            operation=connection.cursor().execute("SELECT CONVERT(varchar(36),OperacionId) FROM finanzas.OperacionPago WHERE FacturaId=? AND Tipo='PAGO'",self.invoice).fetchval()
        self.request("Factura","/interno/operaciones-pago/"+operation,"POST",internal=True)
        _,invoice=self.request("Factura","/factura/"+str(self.invoice));self.check("Entrega antigua no revierte estado posterior",invoice["estado"]=="PENDIENTE")
        with concurrent.futures.ThreadPoolExecutor(max_workers=2) as pool:
            responses=list(pool.map(lambda _:self.request("Pago","/pago","POST",{"facturaId":self.invoice}),range(2)))
        self.check("Peticiones concurrentes responden de forma controlada",all(status in [201,409,503] for status,_ in responses))
        time.sleep(4)
        with m.connect(self.cfg,self.prefix+"Pago",autocommit=True) as connection:
            count=connection.cursor().execute("SELECT COUNT(*) FROM finanzas.Pago WHERE FacturaId=? AND Estado='APLICADO'",self.invoice).fetchval()
        self.check("Concurrencia no duplica pagos aplicados",count==1)
        print("Pagos, reversos, concurrencia y recuperacion tras reinicio comprobados.",flush=True)

    def cleanup(self):
        self.stop(*list(self.processes))
        if self.proxy:self.proxy.shutdown();self.proxy.server_close()
        with m.connect(self.cfg,"master",autocommit=True) as connection:
            cursor=connection.cursor()
            for database in self.created:
                if not database.startswith(self.prefix) or not self.prefix.startswith("PrograV_IT_"):raise RuntimeError("Destino de limpieza no autorizado")
                cursor.execute("ALTER DATABASE "+m.quote(database)+" SET SINGLE_USER WITH ROLLBACK IMMEDIATE")
                cursor.execute("DROP DATABASE "+m.quote(database))
        self.cleaned=True
        print("Procesos de prueba detenidos y bases temporales retiradas.",flush=True)

    def run(self,phase="completa"):
        failure=None
        try:
            self.isolation();self.prepare()
            for service in ["Autenticacion","Parametro","Bitacora","Usuario"]:self.start(service)
            self.token_refresh()
            if phase=="completa":self.smoke()
            if phase in ["completa","academia"]:self.academic()
            if phase=="pagos":
                with m.connect(self.cfg,self.prefix+"Expediente",autocommit=True) as connection:
                    connection.cursor().execute("INSERT matricula.Estudiante(Identificacion,TipoIdentificacion,Email,NombreCompleto,FechaNacimiento,DistritoId,OtrasSenas) VALUES('IT-FINANZAS','LOCAL','finanzas@cuc.cr','Estudiante Finanzas','1990-01-01',1,'Prueba de integracion')")
                with m.connect(self.cfg,self.prefix+"Matricula",autocommit=True) as connection:
                    self.enrollment=connection.cursor().execute("INSERT matricula.Matricula(IdentificacionEstudiante,CursoId,GrupoId,PeriodoId,Estado,FechaMatricula) OUTPUT inserted.MatriculaId VALUES('IT-FINANZAS',4,3,1,'ACTIVA',GETDATE())").fetchval()
                self.start("Matricula")
            if phase in ["completa","pagos"]:self.financial()
        except BaseException as error:
            # No incluir respuestas, claves, cadenas de conexion ni parametros SQL.
            failure=type(error).__name__+": "+str(error) if not isinstance(error,m.pyodbc.Error) else "SQLSTATE: "+str(error.args[0])
            print("FALLO: "+failure,flush=True)
        finally:
            try:self.cleanup()
            except Exception as error:failure=(failure or "")+" Limpieza pendiente: "+type(error).__name__
            report={"phase":phase,"created_at_utc":self.created_at,"finished_at_utc":datetime.now(timezone.utc).isoformat(),"passed":failure is None,"failure":failure,"tests":self.results,"temporary_databases":self.created,"temporary_databases_cleaned":self.cleaned,"business_databases_modified":False,"smtp_sent":False}
            destination=ROOT/("docs/pruebas/integracion-"+("separada" if phase=="completa" else phase)+".json");destination.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding="utf-8")
        if failure:raise SystemExit(1)
        print("APROBADO: "+str(len(self.results))+" comprobaciones.",flush=True)

if __name__=="__main__":
    parser=argparse.ArgumentParser(description=__doc__)
    mode=parser.add_mutually_exclusive_group()
    mode.add_argument("--fase",choices=["completa","academia","pagos"],default="completa")
    mode.add_argument("--limpiar-pendientes",metavar="REPORTE")
    args=parser.parse_args()
    if args.limpiar_pendientes:
        try:cleanup_report(args.limpiar_pendientes)
        except m.pyodbc.Error as error:
            print("Limpieza pendiente. SQLSTATE: "+str(error.args[0]));raise SystemExit(1)
    else:Suite().run(args.fase)
