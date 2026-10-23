# Roadmap 2 - Proyecto Nuevo Avatar V2

Estado: **planificación inicial**, 19 de octubre de 2026. Este documento organiza trabajo propuesto; ninguna etapa ni historia se declara ejecutada.

Organizo el trabajo de cuatro personas para continuar desde el primer avance cerrado. Sigo el formato del roadmap anterior: alcance, acuerdos comunes, dependencias, distribución por persona, etapas y criterios de cierre.

## Principio de alcance

La fuente funcional es el [PDF V2](../requerimientos/Proyecto%20Nuevo%20Avatar%20V2%20%281%29.pdf). La [propuesta](propuesta.md) contiene la asignación y estimación completa de **33 HU / 197 puntos**: `ADM1`–`ADM24`, `MOB0`–`MOB6` y `GTW1`–`GTW2`.

Las etapas siguientes son una secuencia técnica del backlog V2. El PDF agrupa segundo/tercer alcance y no determina qué HU corresponde a cada entrega. Por ello, la primera actividad es confirmar ese corte con el profesor; **las etapas internas no sustituyen los entregables académicos**. Hasta entonces mantengo todas las HU planificadas, sin prometer las 33 para el segundo avance ni relegar automáticamente móvil al tercero.

Uso únicamente información de `docs/`: el [roadmap V1](../../primer_avance/roadmaps/roadmap%201.md) como referencia de organización y la [guía de integración](../../primer_avance/integracion-servicios.md) para la base técnica documentada. No reabro el cierre del primer avance ni presento los hallazgos históricos como defectos actuales.

## Arquitectura y acuerdos comunes

Propongo React/TypeScript para web, React Native/TypeScript para móvil y YARP para el gateway. Las alternativas permitidas y las decisiones pendientes se explican en la propuesta. Continúo los servicios y bases independientes documentados en el primer avance.

- Web y móvil consumen una URL base del gateway, que enruta a los servicios propietarios de los datos. No acceden a SQL ni a endpoints internos con claves de servicio.
- GTW1 y GTW2 se implementan en un solo gateway: Persona 4 (Fabian) coordina rutas y Persona 1 (Jose), validación de token. Se acuerda una tabla con ruta externa, destino, verbos y requisito de autenticación; los ejemplos de rutas del PDF no sustituyen los contratos reales.
- Las rutas protegidas validan el token mediante `/validate`; se prueba rechazo 401 antes de llegar al recurso. Las excepciones de GTW2 deben quedar explícitas, incluida la referencia SRV7 pendiente de aclarar.
- El menú y los accesos respetan el rol. Ocultar una opción se acompaña de autorización para llamadas directas; móvil solo permite consultar/modificar los datos propios que autoriza su HU.
- Web conserva los tokens y campos exigidos por ADM2, renueva con `/refresh` y vuelve al login si falla. Móvil usa almacenamiento seguro de plataforma según MOB1. Cerrar sesión limpia las credenciales del cliente.
- Las plantillas comparten mensajes y confirmaciones. Los mantenimientos usan listado → nuevo/editar → guardar/regresar → listado, con eliminación confirmada. La paginación tiene máximo de 15 registros por página y tamaño parametrizable, conforme al flujo general del PDF.
- Todas las pantallas contemplan carga, vacío, éxito, error de validación, dependencia no disponible y sesión expirada. Filtros, orden y exportaciones se implementan donde los criterios los solicitan.
- Los servicios mantienen las bitácoras CRUD, consultas y errores técnicos. Los clientes registran los eventos UI exigidos por ADM1/ADM2/MOB1; se distingue el evento de navegación del cambio de negocio para no duplicar una misma acción.
- Se conserva JSON nuevo, anterior/actual o eliminado según la operación; se excluyen contraseñas y tokens del registro. Las consultas usan la descripción establecida por el PDF.
- Cada adaptación de API o datos se vincula a su HU V2 y propietario V1. Los cambios estructurales siguen la gestión de aprobación y script indicada en el PDF; no se modifica manualmente la BD para habilitar una pantalla.

## Dependencias principales

| Bloque | Dependencias | Resultado que desbloquea |
|---|---|---|
| GTW1 + GTW2 | Rutas V1 documentadas, USR5 y lista de excepciones acordada | Acceso web/móvil a servicios mediante entrada única. |
| ADM1 + ADM2 + ADM5 + ADM7 | Contratos de sesión, usuario/rol, módulos y permisos | Shell web, menú y navegación autorizada. |
| MOB0 + MOB1 | Gateway, sesión y vínculo usuario–estudiante | Base móvil autenticada y datos propios. |
| ADM4, ADM6, ADM8–ADM13 | Plantilla, seguridad y servicios de mantenimiento | Usuarios, parámetros y oferta académica disponibles. |
| ADM16 + MOB3 | MAT3, MAT4 y correspondencia territorial | Expedientes completos y actualización móvil permitida. |
| ADM14 + MOB4 | Expediente, carrera/curso, periodo futuro | Prematrícula con validaciones y confirmación. |
| ADM15 + MOB5 | Expediente, curso/grupo y periodo activo | Matrícula y consulta coherentes desde ambos clientes. |
| ADM17 | Matrícula, grupos y MAT5 | Desglose y notas de estudiantes matriculados. |
| ADM18 + ADM19 + MOB6 | Oferta, matrículas y notas según la consulta | Historial, listados y cursos propios. |
| ADM20 + ADM21 | Matrícula, IPN1 e IPN2 | Facturación, pago y reversos con estados verificables. |
| ADM22 + ADM3 + MOB2 | Parámetros, correo e información de dominios | Historial de envíos, paneles y resúmenes con datos reales. |
| ADM23 + ADM24 | GEN1, usuario y política parametrizable | Auditoría consultable y perfil/cambio de contraseña. |

Los catálogos, sesión y gateway se acuerdan primero. Las pantallas pueden avanzar con ejemplos de contrato, pero su cierre exige integración real. No hace falta esperar a terminar cada CRUD visual para probar un flujo si los servicios y datos requeridos ya están disponibles.

## Distribución por persona

Cada persona responde por sus pantallas, integración con gateway, validaciones, coordinación de API, bitácoras y evidencia por criterio. Se usan únicamente **Persona 1–4**, con nombres asignados por el equipo. El responsable V2 coordina cualquier adaptación necesaria de servicios V1; la asignación histórica no constituye una quinta persona en este roadmap.

| Persona | Enfoque | Cantidad de HU | Puntos |
|---|---|---:|---:|
| Persona 1 (Jose) | Plantilla web, sesión, permisos y auditoría | 8 | 50 |
| Persona 2 (Ramses) | Usuarios, oferta académica y dashboard | 9 | 49 |
| Persona 3 (Alejandro) | Expedientes, prematrícula, matrícula y consultas | 8 | 49 |
| Persona 4 (Fabian) | Evaluación, finanzas, correo y base móvil | 8 | 49 |
| **Total** | | **33** | **197** |

Uso la escala de Planning Poker documentada en la [propuesta](propuesta.md): **0, ½, 1, 2, 3, 5, 8, 13 y 21**. Las cartas **∞, ? y café** sirven para discutir la estimación y no suman puntos. Las puntuaciones actuales de 3, 5 y 8 siguen siendo válidas; el total se mantiene en **197 puntos**.

La diferencia máxima es de **1 punto** respecto a un promedio de **49,25 puntos**. Es el reparto más parejo posible con las estimaciones actuales de las HU completas. Las tareas compartidas no se contabilizan como nuevas HU. Revisaré el esfuerzo real tras el primer flujo vertical, especialmente en las bases compartidas.

### Persona 1 (Jose) - Plantilla web, sesión, permisos y auditoría

**HU:** ADM1, ADM2, ADM5, ADM6, ADM7, ADM23, ADM24, GTW2. **Estimación:** 50 puntos.

1. Entregar la plantilla web responsive, mensajes, confirmaciones, ruta activa, breadcrumbs y título.
2. Integrar login/refresh, menú por rol y matriz persistida rol–módulo.
3. Acordar con Persona 4 (Fabian) autenticación y excepciones del gateway, junto con el contrato de sesión móvil.
4. Completar roles, parámetros, módulos, consulta de bitácoras, perfil y cambio de contraseña.

Evidencia clave: navegación responsive, permisos por rol, token inválido/expirado, refresh fallido, acceso directo denegado, cambios auditados y contraseña conforme a parámetros.

### Persona 2 (Ramses) - Usuarios, oferta académica y dashboard

**HU:** ADM3, ADM4, ADM8, ADM9, ADM10, ADM11, ADM12, ADM13, MOB6. **Estimación:** 49 puntos.

1. Construir usuarios, instituciones, profesores y carreras; validar dominios, selectores y restricciones de eliminación.
2. Implementar cursos, periodos y grupos, incluyendo estados derivados, unicidad y relaciones.
3. Integrar dashboard por rol con datos de los servicios propietarios, notificaciones y bitácoras.
4. Implementar mis cursos móvil sobre la plantilla de Persona 4 (Fabian) y coordinar los datos de matrícula con Persona 3 (Alejandro).

Evidencia clave: filtros, dominios por rol, fechas/estados de periodo, grupos únicos, métricas verificables, búsqueda local y pull-to-refresh.

### Persona 3 (Alejandro) - Expedientes, prematrícula, matrícula y consultas académicas

**HU:** ADM14, ADM15, ADM16, ADM18, ADM19, MOB3, MOB4, MOB5. **Estimación:** 49 puntos.

1. Construir expediente web y móvil con selectores territoriales dependientes y campos editables limitados para el estudiante.
2. Implementar prematrícula en ambos clientes con periodos futuros y prevención de duplicados.
3. Construir matrícula guiada web y móvil con curso/grupo, periodo activo, confirmación y exportación CSV donde se solicita.
4. Integrar historial/listados con las notas de Persona 4 (Fabian) y la oferta de Persona 2 (Ramses); completar filtros, orden, paginación y exportación CSV/PDF.

Evidencia clave: correspondencia territorial, cambios propios permitidos, periodos futuro/activo, duplicados, promedio comprobable y exportaciones con datos.

### Persona 4 (Fabian) - Evaluación, finanzas, notificaciones y base móvil

**HU:** ADM17, ADM20, ADM21, ADM22, MOB0, MOB1, MOB2, GTW1. **Estimación:** 49 puntos.

1. Publicar el mapa de rutas y montar el gateway con la protección de Persona 1 (Jose).
2. Entregar plantilla móvil, tabs, mensajes, confirmaciones y login con almacenamiento seguro; acordar sesión y parámetros con Persona 1 (Jose).
3. Integrar resumen del estudiante con datos de matrícula de Persona 3 (Alejandro) y los servicios financieros.
4. Implementar desglose/notas y sus estados de edición; coordinar datos de historial con Persona 3 (Alejandro).
5. Integrar facturas/pagos/reversos y envío de correo con HTML sanitizado, historial y configuración desde parámetros.

Evidencia clave: routing GET/POST/PUT/DELETE, login móvil, resumen propio, rubros que suman 100, bloqueo con notas, factura de 30 600 (30 000 + 2%), pago/reverso y notificación real.

## Etapas de trabajo

### Etapa 1 - Alcance, contratos y primer flujo vertical

Mi objetivo es disponer de una base común consumible por ambos clientes antes de desarrollar las pantallas en paralelo.

| Trabajo | Responsable principal | Resultado esperado |
|---|---|---|
| Confirmar HU por entrega y decisiones pendientes de la propuesta | Equipo | Corte segundo/tercer avance y prioridades registrados. |
| Contrastar contratos V1 con necesidades V2 | Cada persona por dominio | Matriz HU → servicio → contrato reutilizado/ampliado → responsable. |
| Crear mapa externo de rutas y protección | Personas 4 (Fabian) y 1 (Jose) | Tabla de routing y excepciones; rutas listas para probar desde cliente. |
| Preparar plantillas y componentes comunes | Personas 1 (Jose) (web) y 4 (móvil) | Navegación web/móvil, mensajes y formularios base integrables. |
| Integrar login, rol y un recurso real por gateway | Personas 1 (Jose) y 4 (Fabian) | Web: login → menú → consulta protegida; móvil: login → consulta propia. |
| Definir datos de demostración | Personas 3 (Alejandro) y 4 (Fabian), con Persona 2 (Ramses) para oferta y usuarios | Estudiante, profesor, roles, oferta y periodos futuro/activo/cerrado identificables. |

Salida de etapa: primer flujo reproducible por gateway y contratos compartidos. Los datos simulados ayudan a desarrollar, pero no acreditan integración terminada.

### Etapa 2 - Pantallas y flujos de dominio

Mi objetivo es completar las HU incluidas en el corte acordado, siguiendo sus dependencias.

- Persona 1 (Jose) desarrolla plantilla web, sesión, permisos, parámetros, módulos, perfil, auditoría y protección del gateway.
- Persona 2 (Ramses) desarrolla usuarios y oferta académica completa, dashboard y mis cursos móvil.
- Persona 3 (Alejandro) desarrolla expedientes, prematrícula y matrícula web/móvil, historial y listados.
- Persona 4 (Fabian) desarrolla routing, base/sesión/resumen móvil, notas, facturas, pagos y correo.

Realizo integraciones parciales por flujo: oferta → expediente → prematrícula; matrícula → notas → historial/listado; matrícula → factura → pago/reverso. Los resúmenes y dashboards se completan con esos datos. Cada integración incluye casos negativos, autorización y bitácora; no dejo esas verificaciones para después de construir todas las pantallas.

Salida de etapa: historias funcionales del corte, integradas con servicios reales y evidencia vinculada a sus criterios. Las HU que se acuerden para el tercer avance conservan responsable y dependencia explícita en el backlog.

### Etapa 3 - Verificación integrada, documentación y cierre

Mi objetivo es entregar una versión demostrable del alcance acordado, con trazabilidad individual.

| Recorrido | Personas que coordinan | Comprobación |
|---|---|---|
| Login → menú por rol → pantalla → refresh/logout | 1 (Jose) y 4 (Fabian) | Sesión, navegación y autorización; fallo de renovación vuelve a login. |
| Institución → profesor → carrera → curso → periodo → grupo | 2 (Ramses) | Relaciones, validaciones y bloqueos de eliminación. |
| Dirección → expediente → prematrícula web/móvil | 3 (Alejandro), con 2 (Ramses) para oferta | Correspondencia territorial, identidad y periodo futuro. |
| Matrícula web/móvil → notas → historial/listado | 2 (Ramses), 3 (Alejandro) y 4 (Fabian) | Periodo activo, duplicados, notas y resultados/exportaciones coherentes. |
| Matrícula → factura → pago → reverso | 3 (Alejandro) y 4 (Fabian) | Monto, motivo y estado de factura/pago verificables. |
| Correo → historial de envíos → dashboard/bitácora | 1 (Jose), 2 (Ramses) y 4 (Fabian) | Configuración parametrizada y resultado real del envío. |

La prueba de cada criterio utiliza interfaz web/móvil cuando corresponde y comprobación de API/bitácora cuando sea necesaria. Postman complementa estas pruebas; la presencia de una colección no acredita una pantalla ni un criterio ejecutado.

## Evidencia y definición de terminado

Propongo guardar las pruebas nuevas en `docs/segundo_avance/pruebas/` y las capturas en `docs/segundo_avance/evidencia/personaN/` cuando se ejecuten. No se crean resultados ficticios ni se reutilizan capturas V1 para acreditar pantallas V2.

La matriz de pruebas tendrá esta estructura; la fila siguiente es una plantilla, no una ejecución:

| HU | Criterio y página del PDF | Responsable | Datos/precondiciones | Pasos | Esperado | Observado | Evidencia | Versión | Estado |
|---|---|---|---|---|---|---|---|---|---|
| ID | Un criterio concreto | Persona N | Rol y datos de prueba | Interacciones/API | Resultado verificable | Por ejecutar | Ruta de captura al ejecutar | Commit/configuración | Pendiente |

Considero una HU terminada cuando:

- Todos sus criterios están trazados a una implementación y prueba ejecutada.
- La interfaz funciona con servicios reales a través del gateway y respeta rol/identidad.
- Las reglas se validan en cliente y servicio según corresponda, y los errores se muestran con claridad.
- Se comprueban las bitácoras requeridas, incluyendo antes/después donde aplica.
- Las capturas identifican HU, criterio, datos y versión; no presentan datos de ejemplo como resultados reales.
- Los cambios de contrato/BD quedan documentados y gestionados según el PDF.
- La HU está actualizada en el tablero y su cambio integrado mediante el PR correspondiente.

## Entregables de cierre

Para el alcance que se confirme, preparo los elementos solicitados en pp. 21–22 del PDF:

- Documento de análisis/diseño con portada, introducción, modelo de datos completo, casos de uso, clases, pruebas, conclusiones/recomendaciones y bibliografía.
- Scripts de BD y cambios aprobados que apliquen a V2.
- Código fuente de los servicios adaptados, web, móvil y gateway incluidos en la entrega acordada.
- Guía de ejecución con URLs, variables, orden de arranque y roles de demostración, sin secretos.
- HU actualizadas, matriz criterio–evidencia y capturas de ejecución.
- Pull request hacia Main o la rama final acordada.

No fijo fechas nuevas: el PDF refiere semanas académicas, y falta confirmar el calendario y el corte de HU. El seguimiento inicial se realiza por etapa y dependencias.

## Riesgos y controles

| Riesgo | Control propuesto |
|---|---|
| Confundir todo V2 con el segundo entregable | Confirmar y registrar el corte antes de comprometer fechas o alcance de entrega. |
| Gateway, sesión y plantillas bloquean al equipo | Priorizarlos en etapa 1 con un flujo vertical web/móvil. |
| V1 no cubre un dato nuevo de V2 | Contrastar criterio y contrato; asignar ampliación al propietario del servicio. |
| Menú visible sin autorización efectiva | Probar ruta directa, rol incorrecto e identidad ajena además del menú. |
| Trabajo compartido concentrado | Separar coordinación de routing, seguridad y plantillas; revisar carga tras etapa 1. |
| Falta de datos para notas, reportes o pagos | Preparar fixtures por flujo y evitar que reversos/borrados invaliden las siguientes demostraciones. |
| HU móvil implícita o referencia SRV sin definición | Registrar la decisión con el profesor y mantener visible el criterio pendiente de aclaración. |
| Capturas sin correspondencia con criterios | Mantener la matriz desde cada integración; distinguir pendiente, ejecutado y aprobado. |

## Nota final

Este roadmap inicia la planificación del segundo avance con una distribución equilibrada entre Persona 1–4 y conserva las etapas internas del primer avance. Las prioridades son confirmar la frontera de entrega, resolver los contratos compartidos y completar flujos verificables desde web y móvil mediante el gateway.
