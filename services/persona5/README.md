# Nuevo Avatar - Persona 5

Servicio REST para matricula, notas, facturacion, pagos y notificaciones.

Historias asignadas:

- `MAT2`: `/matricula`
- `MAT5`: `/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas`
- `IPN1`: `/factura`
- `IPN2`: `/pago`
- `IPN3`: `/notificar`

## Requisitos

- Java 21.
- Seguridad en `http://localhost:5158`.
- SQL Server con los scripts base y las migraciones de `db/persona5/migraciones`.
- Variables de ambiente para la conexion a la base.

## Variables

```powershell
$env:SPRING_DATASOURCE_URL = 'jdbc:sqlserver://servidor:1433;databaseName=NuevoAvatar_Integracion;encrypt=true;trustServerCertificate=true'
$env:SPRING_DATASOURCE_USERNAME = 'usuario'
$env:SPRING_DATASOURCE_PASSWORD = 'clave'
$env:SEGURIDAD_URL = 'http://localhost:5158'
```

## Ejecucion

Desde esta carpeta, usando el wrapper existente de Persona 2:

```powershell
..\persona2\mvnw.cmd -f pom.xml spring-boot:run
```

El servicio escucha en `http://localhost:8085`.
Swagger queda en `http://localhost:8085/swagger-ui.html`.

## Acuerdos

- Todos los endpoints de negocio validan `Authorization: Bearer <jwt>` contra `/validate`.
- Las acciones importantes se registran en `/bitacora`.
- No se versionan usuarios, claves, tokens ni datos SMTP.
- Hibernate solo valida el modelo: la estructura se administra con SQL versionado.
