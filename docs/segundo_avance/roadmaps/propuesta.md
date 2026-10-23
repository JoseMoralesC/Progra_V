# Analisis del alcance y propuesta de reparto

Revision del 23 de octubre de 2026. Sustituye el reparto del 19 de octubre.
Es una propuesta de compromiso del equipo, no una nueva rubrica del profesor.
El [indice del plan](../README.md) distingue PDF, aclaraciones y decisiones.

## Diagnostico del roadmap anterior

El reparto anterior era correcto para las 33 HU expresas del PDF: 197 puntos,
50/49/49/49 por persona. Sin embargo, ese equilibrio dejaba cuatro problemas:

1. No incluia historias explicitas para la web estudiantil que aclaro el profesor.
2. No asignaba cada HU a una entrega; una persona podia concentrar su carga en
   el final y otra asumir todas las dependencias tempranas.
3. Jose acumulaba plantilla, sesion, permisos, parametros, perfil y auditoria;
   Fabian acumulaba gateway, base movil y finanzas. Los puntos iguales no
   eliminaban la concentracion de infraestructura ni sus riesgos de evaluacion.
4. No especificaba contratos propios del estudiante. Tener CRUD con JWT en V1
   no garantiza consultas propias ni protege contra cambiar IDs en el navegador.

El plan revisado incorpora las [8 HU WEB](../requerimientos/web-estudiantes.md),
con 43 puntos adicionales propuestos. Tras confirmar el usuario que se permiten
subhistorias evaluables, se conservan las 41 HU de origen y se planifican
45 unidades, sin alterar los 240 puntos. Cada persona tiene 30 por avance.
El [backlog](../planificacion/backlog.md) es la fuente unica de asignaciones.

## Corte elegido y alternativas consideradas

| Alternativa | Ventaja | Problema |
|---|---|---|
| Toda la web en A2 y todo movil en A3 | Frontera visual sencilla. | Concentra 24 ADM, portal estudiantil y gateway antes del primer corte; una parte movil pequena no compensa la carga del cierre web. |
| UI en A2; permisos, pruebas e integracion en A3 | Permite mostrar mas pantallas pronto. | Deja HU incompletas en A2 y expone la calificacion individual a criterios no demostrables. Se descarta. |
| Flujos completos en A2; ampliacion web y movil en A3 | Cada persona presenta HU completas en ambos avances y se prueba identidad desde el inicio. | Requiere fixtures V1 y acuerdos tempranos de API; es el corte propuesto. |

A2 permite administrar usuarios/oferta, autenticar por gateway, operar notas y
correo, y que el estudiante consulte/actualice su expediente y se prematricule.
A3 agrega matricula web/movil, administracion de expedientes/grupos/prematricula,
reportes, finanzas, resto del portal y la app movil completa.

No es necesario crear las pantallas administrativas de todas las dependencias
para probar el primer flujo: V1 ya dispone de servicios. Para notas y dashboards
A2 se preparan matriculas, grupos y facturas verificables mediante fixtures/API.
Esos datos no acreditan las pantallas que se entregaran en A3.

## Reparto evaluable

| Persona | A2: unidades / puntos | A3 nuevas: unidades / puntos | Total: unidades / puntos | Enfoque |
|---|---|---|---|---|
| Jose | 5 / 30 | 6 / 30 | 11 / 60 | Plantilla/sesion/seguridad y vista del profesor/notas; luego base movil, grupos y auditoria. |
| Ramses | 6 / 30 | 6 / 30 | 12 / 60 | Modulos, oferta y correo; luego consultas, reportes y cursos web/movil. |
| Alejandro | 5 / 30 | 5 / 30 | 10 / 60 | Usuarios/roles/parametros/instituciones y routing; luego expedientes/matricula y persistencia de sesion movil. |
| Fabian | 6 / 30 | 6 / 30 | 12 / 60 | Inicio administrativo/estudiantil, rubros, perfil/expediente/prematricula propios; luego finanzas y prematricula/seleccion movil. |
| Total | 22 / 120 | 23 / 120 | 45 / 240 | 41 historias de origen, cuatro divididas en dos. |

Se logra igualdad exacta por persona y entrega conservando los puntos originales.
La cantidad de unidades sigue variando segun su complejidad. Los puntos iguales
no garantizan igual duracion ni eliminan los riesgos de integracion; las tareas
compartidas y sus responsables se mantienen visibles.

## Descomposicion necesaria para 30 puntos exactos

El usuario confirmo que el profesor permite subhistorias evaluables. La solucion
no cambia las estimaciones: ADM3 y ADM17 (A2), MOB1 y MOB5 (A3) se dividen en
bloques funcionales de 5 + 3. Se mantienen los criterios y el ID padre del PDF.
Los [criterios de cada unidad](../planificacion/subhistorias.md) separan actores
o etapas de un flujo, fijan evidencia propia y exigen integracion conjunta.

Con los pesos 3/5/8 actuales no existe un reparto de ocho grupos de 30 usando
solo historias indivisibles. Dos divisiones por avance bastan y son el minimo
para el corte elegido usando 8 -> 5 + 3. La solucion no convierte tareas de
apoyo en historias nuevas ni contabiliza 8 del padre ademas de 5 + 3 de los hijos.

## Escala y alcance de los puntos

Se conserva la escala acordada de Planning Poker: 0, 1/2, 1, 2, 3, 5, 8, 13 y 21.
Infinito, interrogacion y cafe son cartas de discusion; no suman puntos.

- 3: mantenimiento pequeno con reglas conocidas.
- 5: interfaz de dominio, relaciones, filtros o consultas e integracion acotada.
- 8: seguridad/infraestructura transversal o flujo con varias reglas y dependencias.
- 13/21: aclarar o dividir una historia demasiado incierta antes de comprometerla.

Se mantienen los 197 puntos anteriores de ADM/MOB/GTW como estimacion inicial,
no como certificacion de que las ampliaciones de API ya estan cubiertas en codigo.
Las nuevas WEB1/2/3/4/6/7/8 se proponen en 5 y WEB5 en 8: total 43.

La estimacion incluye adaptacion de backend, pruebas y documentacion por HU.
Las tareas comunes se asignan a una HU propietaria y no se cobran por cada
consumidor. Si la inspeccion de contratos cambia una estimacion de forma material,
se reestima con el equipo y se rebalancea el mismo avance; no se oculta el esfuerzo
bajo otra persona ni se conserva artificialmente el total de 240.

## Calificacion y responsabilidad individual

El PDF define 15% para A2 y 30% para A3, pero no establece en el texto revisado
la formula que combina puntos y calificacion de cada HU. No se asume que la
nota sea un promedio simple ni se convierten story points en porcentaje academico.
Se registra por separado para cada HU: puntos, responsable, criterios completos,
evidencia y calificacion cuando el profesor la comunique.

Cada unidad evaluable tiene un responsable y un revisor de apoyo; el revisor no
recibe otros puntos por revisar. Dependencias compartidas no se registran como
historias duplicadas ni como responsables colectivos sin una persona concreta.
Los padres descompuestos tienen coordinador de integracion, sin otros puntos,
y se cierran solo cuando ambos hijos y la prueba conjunta cumplen el alcance.
Una HU no se marca terminada si solo existen algunas pantallas o parte de sus
criterios. Los defectos y su impacto en consumidores quedan visibles en el tablero.

| Autor responsable | Revisor de apoyo | Objetivo de la revision |
|---|---|---|
| Jose | Fabian | Seguridad de sesion y gateway, luego navegacion/identidad movil. |
| Fabian | Jose | Autorizacion, notas y consistencia de operaciones financieras. |
| Alejandro | Ramses | Integridad de referencias, campos propios y tramites estudiantiles. |
| Ramses | Alejandro | Filtros, contratos de consulta, exportaciones y datos de oferta. |

## Cambios principales de responsabilidad

- Jose cede ADM5 a Alejandro, mantiene ADM1/ADM2/GTW2 y toma ADM3-P/ADM17-N:
  plantilla/sesion, seguridad y experiencia del profesor. En A3 conserva auditoria
  y base movil, toma ADM13 y comparte MOB1 con Alejandro.
- Ramses mantiene sus unidades de ambas entregas: su reparto ya era 30/30.
- Alejandro recibe ADM5 y GTW1, conservando usuarios/parametros/instituciones.
  WEB2/WEB4/ADM24 pasan a Fabian. En A3 concentra ADM15/ADM16/WEB5 y las partes
  de persistencia de sesion y confirmacion movil; cede ADM13 a Jose y ADM14/MOB4 a Fabian.
- Fabian conserva ADM3-A/ADM17-R/WEB1 y recibe perfil/expediente/prematricula
  estudiantiles. En A3 mantiene finanzas, recibe la prematricula administrativa
  y movil, y comparte la matricula movil con Alejandro.

GTW1 (routing) y GTW2 (seguridad) siguen integrados en un unico gateway. Los
servicios de apoyo no se reimplementan por cambiar de responsable.

El reparto no queda atado al autor historico de cada servicio V1. Los cambios
necesarios se coordinan desde la HU V2 responsable y se integran por contrato.

## Acuerdos y riesgos antes de comprometer el plan

- El corte A2/A3 lo elige el equipo por la libertad comunicada por el profesor;
  no se deja pendiente una autorizacion adicional del profesor para empezar.
- El equipo debe revisar la estimacion WEB y los permisos por identidad; no
  se presenta esta propuesta como puntaje ya aceptado por el profesor.
- Resolver SRV7, el alcance de profesor en ADM13 y los destinos moviles implicitos
  con el profesor cuando corresponda. Estas dudas no justifican inventar accesos.
- Mantener React/TypeScript, React Native/TypeScript y YARP como propuestas
  tecnicas previas; el PDF permite alternativas. Este cambio es documental,
  no selecciona versiones ni verifica compatibilidad de frameworks.
- Los cambios de BD siguen el procedimiento del PDF. No se modifica SQL ni codigo
  como parte de esta planificacion.

Los bloqueos y sus responsables estan en [contratos](../planificacion/contratos.md).
La [matriz de evidencia](../pruebas/criterio-evidencia.md) permite demostrar la
aportacion individual sin confundir preparacion con una prueba aprobada.
