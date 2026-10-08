# Propuesta - Proyecto Nuevo Avatar V1

En esta propuesta presento la estimación y asignación de las historias de usuario del proyecto Nuevo Avatar V1. Documento el responsable y los puntos de historia definidos para cada una según su complejidad.

En mi propuesta identifico:

- Qué historia de usuario se va a desarrollar.
- Quién será responsable.
- Cuántos puntos de historia tiene.
- Por qué se considera de complejidad baja, media o alta.

## Criterio usado para los history points

Utilizo una escala tipo Fibonacci, como la vista en clase:

| Puntos | Complejidad | Criterio usado |
|---:|---|---|
| 1 | Muy baja | Consulta o cambio mínimo, casi sin reglas. |
| 3 | Baja | CRUD simple o consulta con pocas validaciones. |
| 5 | Media | CRUD con relaciones, validaciones importantes o dependencia de otra HU. |
| 8 | Alta | Seguridad, tokens, procesos transaccionales o varias reglas de negocio. |

Para la estimación tomo en cuenta:

- Cantidad de operaciones solicitadas.
- Validaciones indicadas en los criterios de aceptación.
- Relaciones con otras historias.


## Propuesta de asignación y puntos

| Responsable | HU | Historia resumida | Puntos | Justificación breve |
|---|---|---|---:|---|
| Héctor | USR5 | Servicio de login, refresh y validación de token | 8 | Maneja autenticación, tokens, expiración y respuestas de autorización. |
| Héctor | GEN1 | Registro y consulta de bitácoras | 3 | Servicio general sencillo, pero usado por las demás HU. |
| Héctor | USR2 | Administrar roles | 3 | CRUD simple con validaciones de campos y nombre. |
| Héctor | USR3 | Administrar parámetros | 3 | CRUD simple con reglas de longitud y formato. |
| Héctor | USR4 | Administrar módulos | 3 | CRUD simple con validaciones básicas. |
| Ramsés | USR1 | Administrar usuarios | 8 | Tiene CRUD, filtros, contraseña encriptada, rol, dominio de correo y token. |
| Ramsés | ACD1 | Administrar instituciones | 3 | CRUD simple con validación de nombre. |
| Ramsés | ACD2 | Administrar carreras | 5 | Tiene relación con institución y profesor director. |
| Ramsés | ACA2 | Consultar listado de estudiantes por periodo | 3 | Consulta de datos ya existentes de matrícula y oferta académica. |
| Alejandro | ACD3 | Administrar cursos | 5 | CRUD con relación a carrera y regla de nivel entre 1 y 12. |
| Alejandro | ACD4 | Administrar grupos | 5 | Relaciona curso, profesor, horario, cupo y periodo. |
| Alejandro | ACD5 | Administrar periodos | 3 | CRUD simple con fechas y número de periodo. |
| Alejandro | MAT4 | Consultar provincias, cantones y distritos | 3 | Consultas con validación de la relación provincia-cantón-distrito. |
| Alejandro | IPN3 | Gestión de notificaciones | 3 | Envío de correo con parámetros configurables. |
| Fabián | MAT3 | Administrar expedientes de estudiantes | 5 | CRUD con datos personales, dirección, teléfonos y validación de correo. |
| Fabián | MAT1 | Administrar prematrícula | 5 | Depende de estudiante, carrera, cursos y periodos futuros. |
| Fabián | ACD6 | Administrar profesores | 5 | CRUD con mayoría de edad, teléfonos y dominio de correo parametrizable. |
| Fabián | ACA1 | Consultar historial académico | 3 | Consulta de promedios por estudiante a partir de notas registradas. |
| José | MAT2 | Administrar matrícula | 5 | Relaciona estudiante, curso, grupo y periodo activo. |
| José | MAT5 | Administrar notas | 8 | Maneja desglose, sumatoria 100, bloqueo de rubros y notas por estudiante. |
| José | IPN1 | Administrar facturación | 5 | Incluye factura, detalle, impuesto, estado y relación con matrícula. |
| José | IPN2 | Administrar pagos | 5 | Actualiza estado de factura y permite reversar pagos. |

## Resumen por integrante

| Responsable | Historias asignadas | Total de puntos |
|---|---|---:|
| Héctor | USR5, GEN1, USR2, USR3, USR4 | 20 |
| Ramsés | USR1, ACD1, ACD2, ACA2 | 19 |
| Alejandro | ACD3, ACD4, ACD5, MAT4, IPN3 | 19 |
| Fabián | MAT3, MAT1, ACD6, ACA1 | 18 |
| José | MAT2, MAT5, IPN1, IPN2 | 23 |

Estimo el total del proyecto en **99 puntos de historia**.

## Nota final

El pasado viernes por la noche nos reunimos como Grupo 7 para estimar las historias de usuario mediante la escala Fibonacci. Durante la sesión analizamos los criterios de aceptación, las operaciones, las validaciones y las relaciones entre las historias para asignar una puntuación según su complejidad.

Una vez finalizada la estimación, organizamos las historias según su puntuación y sus dependencias. Con ese análisis realizamos la asignación por integrante que presento en la tabla anterior, procurando distribuir el trabajo de acuerdo con la complejidad estimada.
