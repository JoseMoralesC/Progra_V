# Analisis del alcance y propuesta de reparto

Reajuste del 28 de octubre de 2026, limitado al segundo y tercer avance.
Sustituye el reparto con subhistorias del 23 de octubre. El profesor no permite
historias divididas ni una historia entre dos personas. El equipo acepta que
los puntos queden desiguales. El primer avance conserva su reparto y documentos.
El [backlog](../planificacion/backlog.md) es la fuente unica de asignaciones.

## Alcance y criterio de asignacion

Se conservan 41 historias completas y 240 puntos: las 33 ADM/MOB/GTW del PDF
suman 197 y las 8 WEB propuestas por el equipo suman 43. No se altera el alcance
ni la estimacion para igualar cargas. Cada historia tiene una persona responsable
de todos sus criterios, integracion, pruebas y documentacion, en un solo avance.

**Alejandro creo la plantilla de UI/UX.** Se reconoce expresamente su autoria y
se le asigna ADM1 completa, incluyendo responsive, navegacion por rol y
componentes comunes para ambas webs. La plantilla creada es un aporte existente
comunicado por el equipo; acreditar el cierre requiere verificar toda la HU.
Su reutilizacion en otras historias no vuelve a sumar los puntos de ADM1.

Las cuatro historias antes divididas recuperan su ID y sus 8 puntos originales:

| Historia | Avance | Responsable unico | Razon de continuidad |
|---|---:|---|---|
| ADM3 | 2 | Fabian | Integra el dashboard administrativo y del profesor con usuarios, permisos y parametros. |
| ADM17 | 2 | Jose | Reune rubros y notas con los permisos y pruebas de backend ya documentados por Jose. |
| MOB1 | 3 | Jose | Integra autenticacion y ciclo seguro de sesion con la base movil. |
| MOB5 | 3 | Alejandro | Reune seleccion y confirmacion movil con la matricula administrativa y WEB5. |

Los [criterios completos](../planificacion/subhistorias.md) mantienen el alcance
anterior sin subhistorias evaluables. Las contribuciones tecnicas previas
conservan su autoria historica; no crean responsabilidad compartida por HU.

## Carga por persona

| Persona | HU A2 / puntos | HU nuevas A3 / puntos | HU totales / puntos | Enfoque |
|---|---|---|---|---|
| Jose | 4 / 32 | 6 / 33 | 10 / 65 | Sesion, routing, seguridad y notas; luego grupos, auditoria y base/sesion/resumen/expediente movil. |
| Ramses | 6 / 30 | 6 / 30 | 12 / 60 | Modulos, oferta y correo; luego consultas, reportes y cursos web/movil. |
| Alejandro | 5 / 28 | 4 / 32 | 9 / 60 | Plantilla de UI/UX, perfil, inicio, expediente y prematricula propios; luego expedientes y matricula web/movil. |
| Fabian | 5 / 30 | 5 / 25 | 10 / 55 | Dashboards, usuarios, permisos, parametros e instituciones; luego finanzas y prematricula administrativa/movil. |
| Total | 20 / 120 | 21 / 120 | 41 / 240 | Historias completas, con un responsable cada una. |

Para equilibrar el segundo avance, GTW1 (8 puntos) pasa completa de Alejandro
a Jose, que ya tiene GTW2: el gateway queda bajo un mismo responsable con dos
historias oficiales separadas. Alejandro recibe ADM24/WEB1/WEB2/WEB4 completas
(20 puntos) y Fabian recibe ADM4/ADM5/ADM6/ADM8 completas (22 puntos).
A2 queda en 32/30/28/30 puntos para Jose/Ramses/Alejandro/Fabian. Alejandro
conserva ADM1 y el reconocimiento por la plantilla de UI/UX. A3 mantiene su
reparto; los totales acumulados quedan en 65/60/60/55 puntos. Se acepta esa diferencia
para mantener historias completas y continuidad de dominio. Si surge un bloqueo, se puede reasignar una historia
completa actualizando el backlog y todos los resumenes; no se divide para
conservar un total individual. Los puntos expresan complejidad, no horas ni nota.

## Corte entre entregas

A2 entrega gateway, seguridad, usuarios/oferta, notas, correo y vistas por rol;
el estudiante dispone de inicio, expediente y prematricula. A3 agrega grupos,
matricula, expedientes administrativos, reportes, finanzas, resto del portal y
las siete HU moviles. El final demuestra las 41 historias acumuladas y la
regresion de las 20 de A2, sin sumar otra vez sus puntos.

Se mantienen flujos completos por entrega. Aplazar permisos, pruebas o
integracion a A3 dejaria incompletas las historias de A2. Para notas y metricas
se preparan grupos, matriculas y facturas mediante fixtures/API V1 verificables;
esos datos no acreditan las pantallas administrativas previstas para A3.

## Estimacion y calificacion

Se conserva la escala de Planning Poker: 0, 1/2, 1, 2, 3, 5, 8, 13 y 21.
Infinito, interrogacion y cafe son cartas de discusion, sin puntos. Un 3
representa mantenimiento pequeno; 5, interfaz de dominio e integracion acotada;
8, seguridad transversal o flujos con varias reglas y dependencias. Las
incertidumbres de 13/21 se aclaran antes de comprometer la historia, manteniendo
una HU completa con un responsable para la evaluacion.

WEB1/2/3/4/6/7/8 se estiman en 5 y WEB5 en 8. Las estimaciones incluyen interfaz,
adaptaciones de API, permisos, bitacora, integracion, pruebas y documentacion.
El trabajo compartido se vincula a una HU propietaria, sin cobrarlo por consumidor.
Si cambia el alcance se reestima con el equipo; no se oculta el esfuerzo ni se
conservan artificialmente los puntos.

El PDF indica 15% para A2 y 30% para A3; no se infiere una formula de nota ni
fechas calendario. Los puntos y criterios se registran separados de la nota
que comunique el profesor. Las estimaciones WEB siguen siendo propuestas del
equipo, sin presentarlas como puntajes aprobados por el profesor.

## Revision y responsabilidad individual

| Autor responsable | Revisor de apoyo | Objetivo de la revision |
|---|---|---|
| Jose | Fabian | Sesion, gateway y notas; luego identidad y navegacion movil. |
| Fabian | Jose | Dashboards, autorizacion y consistencia financiera. |
| Alejandro | Ramses | Plantilla, integridad de referencias y tramites estudiantiles. |
| Ramses | Alejandro | Filtros, exportaciones, consultas y oferta. |

El revisor contrasta evidencia; no comparte responsabilidad evaluable ni recibe
puntos por la HU revisada. Cada responsable cierra su historia completa. Se
registran apoyos previos, bloqueos y defectos sin atribuir a una persona trabajo
hecho por otra. La [guia de evidencia](../pruebas/criterio-evidencia.md) permite
identificar la aportacion individual y el cumplimiento real.

GTW1 y GTW2 de Jose son historias distintas del PDF integradas en
un unico gateway. Las dependencias entre HU no implican compartir una historia.
El autor historico de un servicio V1 no determina el responsable de su HU V2.

## Acuerdos y riesgos

- El corte A2/A3 lo elige el equipo segun la libertad comunicada por el profesor.
- Resolver SRV7, permisos de profesor en ADM13 y destinos moviles implicitos con
  el profesor cuando corresponda; no inventar accesos ni acreditar tabs vacios.
- Mantener React/TypeScript, React Native/TypeScript y YARP como propuestas
  tecnicas previas. Este reajuste documental no selecciona versiones.
- Los cambios de BD siguen el procedimiento del PDF. Esta planificacion no
  modifica codigo ni SQL.
- Los contratos, responsables y consumidores se detallan en
  [contratos](../planificacion/contratos.md). Cada HU demuestra permisos por rol,
  identidad propia, errores de dependencia y bitacoras con evidencia real.
