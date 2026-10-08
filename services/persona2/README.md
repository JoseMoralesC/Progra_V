# Nuevo Avatar - Persona 2

En este README documento el servicio REST de oferta académica y administración de usuarios, que implementa los criterios de Persona 2 (`ACD1` a `ACD6`) y el endpoint de usuarios de Persona 1 (`USR1`).

## Requisitos que utilizo

- Java 21.
- Persona 1/Seguridad en `http://localhost:5158`.
- Variable de entorno `DB_PASSWORD` con la contraseña de SQL Server.
- Base `db70273` con los esquemas `academico` y `seguridad`.

## Ejecución

Inicio el servicio desde Windows PowerShell:

```powershell
$env:DB_PASSWORD = '<contraseña>'
.\mvnw.cmd spring-boot:run
```

Accedo al servicio en `http://localhost:8082`. Consulto Swagger en `http://localhost:8082/swagger-ui.html` y reviso el documento OpenAPI en `/v3/api-docs`.

## Autenticación y bitácora

Para los endpoints de negocio utilizo `Authorization: Bearer <jwt>`. Valido el JWT mediante `GET http://localhost:5158/validate`. Registro cada consulta y operación mediante `POST http://localhost:5158/bitacora`.

## Recursos

| Recurso | Rutas principales | Criterio |
|---|---|---|
| Usuario | `/usuario` | USR1 |
| Institución | `/institucion` | ACD1 |
| Carrera | `/carrera?institucionId=` | ACD2 |
| Curso | `/curso?carreraId=` | ACD3 |
| Grupo | `/grupo` | ACD4 |
| Periodo | `/periodo` | ACD5 |
| Profesor | `/profesor` | ACD6 |

En cada recurso dispongo de `GET`, `GET /{id}`, `POST`, `PUT /{id}` y `DELETE /{id}`. Las altas responden `201`, las eliminaciones exitosas `204`, las validaciones incorrectas `400`, los recursos inexistentes `404`, conflictos referenciales `409` y peticiones sin token válido `401`.

## Pruebas

```powershell
$env:DB_PASSWORD = '<contraseña>'
.\mvnw.cmd test
```

Con estas pruebas verifico validaciones de DTO, fechas de periodo, relaciones de grupo y autorización. También valido el esquema real mediante el contexto de Spring y Hibernate, porque `spring.jpa.hibernate.ddl-auto=validate`.

## Transacciones

Utilizo `@Transactional(readOnly = true)` para las lecturas y transacciones acotadas para las escrituras. Configuro `spring.jpa.open-in-view=false`, un timeout de 30 segundos y un pool pequeño de HikariCP con detección de conexiones retenidas, para evitar transacciones abiertas después de responder.
