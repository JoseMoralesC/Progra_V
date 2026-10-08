# Nuevo Avatar - Persona 5

En este servicio documento mi trabajo de matricula, notas, facturacion,
pagos y notificaciones.

## Mis historias asignadas

- `MAT2`: `/matricula`
- `MAT5`: `/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas`
- `IPN1`: `/factura`
- `IPN2`: `/pago`
- `IPN3`: `/notificar`

## Requisitos que utilizo

- Java 21.
- Seguridad en `http://localhost:5158`.
- SQL Server con los scripts base y las migraciones de `db/persona5/migraciones`.
- Configuracion local de la conexion a la base y del correo.

## Mi configuracion

Utilizo el primer `.env` que el servicio encuentra en el directorio de
ejecucion o sus padres. Al iniciar desde `services/persona5`, cargo el
`.env` de la raiz del proyecto. Doy prioridad a las variables de ambiente
y a los argumentos de Spring cuando existen.

Para Gmail, completo `MAIL_PASSWORD` con la contrasena de aplicacion de
16 caracteres sin espacios y reinicio el servicio. Indico el destinatario
en cada solicitud de Postman.

Como alternativa al `.env`, configuro la conexion en PowerShell:

```powershell
$env:SPRING_DATASOURCE_URL = 'jdbc:sqlserver://servidor:1433;databaseName=NuevoAvatar_Integracion;encrypt=true;trustServerCertificate=true'
$env:SPRING_DATASOURCE_USERNAME = 'usuario'
$env:SPRING_DATASOURCE_PASSWORD = 'clave'
$env:SEGURIDAD_URL = 'http://localhost:5158'
```

## Como inicio mi servicio

Desde esta carpeta utilizo el wrapper existente de Persona 2:

```powershell
..\persona2\mvnw.cmd -f pom.xml spring-boot:run
```

Accedo al servicio en `http://localhost:8085` y a Swagger en
`http://localhost:8085/swagger-ui.html`.

## Mis acuerdos de integracion

Configuro el saludo SMTP (EHLO/HELO) con `MAIL_SMTP_LOCALHOST` y utilizo
`nuevoavatar.local` por defecto para evitar nombres de equipo con formato
de dominio invalido.

- Valido `Authorization: Bearer <jwt>` contra `/validate` en los endpoints de negocio.
- Registro las acciones importantes en `/bitacora`.
- Mantengo usuarios, claves, tokens y datos SMTP fuera del control de versiones.
- Administro la estructura con SQL versionado y utilizo Hibernate para validar el modelo.
