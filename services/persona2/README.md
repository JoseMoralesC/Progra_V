# Nuevo Avatar - Persona 2

Servicio REST de oferta académica y administración de usuarios. Implementa los criterios de Persona 2 (`ACD1` a `ACD6`) y el endpoint de usuarios de Persona 1 (`USR1`).

## Requisitos

- Java 21.
- Persona 1/Seguridad en `http://localhost:5158`.
- Variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD` para SQL Server.
- Base de integración `PrograV`, accesible por Tailscale, con los esquemas `academico` y `seguridad`.

## Ejecución

En Windows PowerShell:

```powershell
.\run-prograv.ps1
```

El script toma la conexión `SeguridadDb` del archivo local de Seguridad `appsettings.Development.json`, verifica que use `PrograV` y configura las variables solo durante la ejecución. Ese archivo contiene credenciales locales y debe permanecer ignorado por Git.

También puede ejecutarse directamente configurando `DB_URL` (URL JDBC), `DB_USER` y `DB_PASSWORD` en la terminal y luego `mvnw.cmd spring-boot:run`.

El servicio escucha en `http://localhost:8082`. Swagger queda disponible en `http://localhost:8082/swagger-ui.html` y el documento OpenAPI en `/v3/api-docs`.

## Autenticación y bitácora

Todos los endpoints de negocio exigen `Authorization: Bearer <jwt>`. El JWT se valida llamando a `GET http://localhost:5158/validate`. Cada consulta y operación se registra mediante `POST http://localhost:5158/bitacora`.

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

Cada recurso ofrece `GET`, `GET /{id}`, `POST`, `PUT /{id}` y `DELETE /{id}`. Las altas responden `201`, las eliminaciones exitosas `204`, las validaciones incorrectas `400`, los recursos inexistentes `404`, conflictos referenciales `409` y peticiones sin token válido `401`.

## Pruebas

```powershell
# Configuro DB_URL, DB_USER y DB_PASSWORD localmente antes de ejecutar.
.\mvnw.cmd test
```

Las pruebas cubren validaciones de DTO, fechas de periodo, relaciones de grupo y autorización. El contexto de Spring también ejecuta la validación Hibernate del esquema real porque `spring.jpa.hibernate.ddl-auto=validate`.

## Transacciones

Las lecturas usan `@Transactional(readOnly = true)` y las escrituras transacciones acotadas. `spring.jpa.open-in-view=false`, el timeout es de 30 segundos y HikariCP mantiene un pool pequeño con detección de conexiones retenidas, para evitar transacciones abiertas después de responder.
