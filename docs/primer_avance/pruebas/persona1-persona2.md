# Evidencia técnica - Persona 1 y Persona 2

Documento la ejecución del 29 de septiembre de 2026.

## Cobertura del roadmap

| Persona | Criterio | Endpoint | Estado verificado |
|---|---|---|---|
| 1 | USR1 | `/usuario` | CRUD, filtros, BCrypt, rol y dominio |
| 1 | USR2 | `/rol` | Implementado en Seguridad |
| 1 | USR3 | `/parametro` | Implementado en Seguridad |
| 1 | USR4 | `/modulo` | Implementado en Seguridad |
| 1 | USR5 | `/login`, `/refresh`, `/validate` | Login `201` y validación JWT `200` |
| 1 | GEN1 | `/bitacora` | Consultas de Persona 2 registradas |
| 2 | ACD1 | `/institucion` | CRUD protegido |
| 2 | ACD2 | `/carrera` | CRUD, filtro y relaciones |
| 2 | ACD3 | `/curso` | CRUD, filtro y nivel 1-12 |
| 2 | ACD4 | `/grupo` | CRUD y referencias requeridas |
| 2 | ACD5 | `/periodo` | CRUD y rango válido de fechas |
| 2 | ACD6 | `/profesor` | CRUD, mayoría de edad y teléfonos |

## Prueba integrada

En esta ejecución registro el inicio de Seguridad en el puerto `5158` y Persona 2 en `8082`, ambos conectados a `db70273`.

| Verificación | Resultado |
|---|---|
| `POST /login` con usuario de integración | `201 Created` |
| `GET /validate` con JWT | `200 OK`, cuerpo `true` |
| `GET /usuario` | `200 OK` |
| `GET /institucion` | `200 OK` |
| `GET /profesor` | `200 OK` |
| `GET /carrera` | `200 OK` |
| `GET /curso` | `200 OK` |
| `GET /periodo` | `200 OK` |
| `GET /grupo` | `200 OK` |
| `GET /institucion` sin JWT | `401 Unauthorized` |
| `GET /v3/api-docs` | `200 OK` |
| Registros agregados a `/bitacora` | 7 consultas, una por recurso |

## Pruebas automatizadas de Persona 2

Documento el comando ejecutado: `.\mvnw.cmd test` con `DB_PASSWORD` configurada solo como variable de entorno.

- 9 pruebas ejecutadas.
- 0 fallos.
- 0 errores.
- 0 omitidas.
- Hibernate validó las tablas de `academico` y `seguridad` contra `db70273`.

## Pruebas automatizadas de Persona 1

Con las pruebas de `NuevoAvatarSeguridad.Tests` documento los contratos críticos de autenticación:

- rechazo de datos de login vacíos;
- `401` para credenciales inválidas;
- `201` y respuesta de tokens para credenciales válidas;
- lectura de JWT desde `Authorization: Bearer`;
- `401` para un JWT inválido.

## Mis criterios REST y de diseño

- Utilizo JSON como representación de intercambio.
- Aplico la semántica HTTP: `GET` consulta, `POST` crea, `PUT` reemplaza los campos editables y `DELETE` elimina.
- Mantengo controladores delgados, reglas de negocio y transacciones en servicios, y persistencia en repositorios.
- Utilizo dependencias inyectadas por constructor, DTO separados de entidades y manejo global uniforme de errores.
- Organizo autorización y bitácora como componentes transversales para evitar duplicación.
