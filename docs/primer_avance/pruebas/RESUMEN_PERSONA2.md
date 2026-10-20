# Persona 1 y Persona 2: pruebas en PrograV

Verificación del 5 de octubre de 2026 sobre SQL Server del equipo, conectado por Tailscale. Seguridad usa el puerto 5158 y Persona 2 el 8082. La configuración de conexión y las credenciales se mantienen localmente.

| Comprobación | Resultado |
|---|---|
| Peticiones HTTP de ambas colecciones | 99 ejecutadas, 128 aserciones aprobadas, cero fallos y cero peticiones omitidas. |
| Persona 1 | 43 peticiones: login, validación JWT, refresh y revocación del anterior; CRUD de roles, parámetros y módulos; creación/consulta de bitácora; rechazos sin token y datos inválidos. |
| Persona 2 | 56 peticiones: CRUD de usuarios, instituciones, profesores y carreras; filtros, duplicados, mayoría de edad, dominio, teléfonos, referencias inexistentes, restricciones al eliminar y autorización. |
| Limpieza | Eliminados y consultados con resultado 404 los registros ficticios de roles, parámetros de prueba, módulos, usuarios, instituciones, profesores y carreras. Las bitácoras y sesiones de autenticación permanecen. |
| Suite de Persona 2 | 12 pruebas aprobadas, cero fallos/errores/omisiones. Incluye validación Hibernate del esquema real de PrograV y tres pruebas de ocultación de secretos en bitácora. |
| Transacciones SQL | DBCC OPENTRAN sobre PrograV confirmó que no había transacciones abiertas después de las pruebas. |

## Evidencia

- [Runner Postman Persona 1: 56 aprobadas](evidencias/postman_persona1_runner.png).
- [Runner Postman Persona 2: 72 aprobadas](evidencias/postman_persona2_runner.png).
- [Consulta, actualización y validaciones de usuario](evidencias/postman_persona2_usuario.png).
- [Eliminaciones y confirmaciones 404](evidencias/postman_persona2_limpieza.png).
- [Resultados de las 99 peticiones](evidencias/2026-10-05/persona1-persona2-crud/resultados-http.json): método, ruta de plantilla, código HTTP y resultado de cada aserción.
- [Resultados de la suite Java](evidencias/2026-10-05/persona1-persona2-crud/suite-persona2.json).
- [Verificación de transacciones](evidencias/2026-10-05/persona1-persona2-crud/transacciones.json).
- [Comprobación inicial de conexión y consultas](evidencias/2026-10-05/persona2-prograv/verificacion-http.json).
- [Guía y colecciones importables en Postman](postman/README_PERSONA1_PERSONA2.md).

## Cambios y alcance

Persona 2 toma DB_URL, DB_USER y DB_PASSWORD del ambiente. `run-prograv.ps1` permite usar la misma conexión local de Seguridad y comprueba que el destino sea PrograV. Se conservó `ddl-auto=validate`.

DOMPROF faltaba en PrograV y se creó mediante la API de parámetros con valor cuc.ac.cr, conforme al script versionado de datos iniciales. Es configuración requerida por profesores y permanece después de la limpieza.

Se corrigió la bitácora de Persona 2 para sustituir los valores JSON de contraseña/hash por `[OCULTA]` antes de guardarlos. Las pruebas cubren campos de contraseña, comillas escapadas y conservación de datos no sensibles.

Las colecciones se ejecutaron inicialmente mediante `ejecutar-http.cjs`. Posteriormente, el 5 de octubre de 2026, se ejecutaron en la aplicación Postman 12.30.5, Runner local, una iteración y entorno `Persona1_Persona2.local`: Persona 1 aprobó 56 comprobaciones en 11,104 segundos y Persona 2 aprobó 72 en 16,476 segundos. Ambos resultados muestran cero fallos, errores y omisiones. Las capturas no incluyen contraseñas, tokens ni cuerpos con datos personales reales. Las credenciales se configuraron como valores locales, sin compartirlas.

Las capturas del Runner están incluidas. Queda pendiente la revisión completa de los textos de GEN1 contra cada criterio del PDF. Esta evidencia acredita los casos enumerados; no certifica todos los criterios del proyecto ni los servicios de otras personas.
