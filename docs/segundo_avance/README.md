# Plan de los avances 2 y 3

Estado: propuesta documental reajustada el 28 de octubre de 2026. No acredita
historias implementadas ni una aprobacion del profesor sobre las estimaciones.

## Alcance acordado y fuentes

1. El [PDF V2](requerimientos/Proyecto%20Nuevo%20Avatar%20V2%20%281%29.pdf)
   contiene 24 ADM, 7 MOB y 2 GTW: 33 historias oficiales.
2. Segun la aclaracion del profesor comunicada por Jose, tambien se requiere
   una web de usuarios. Jose confirmo que esa web es **solo para estudiantes**.
   El equipo debe redactar sus historias a partir de los servicios existentes.
3. El equipo elige el alcance del avance 2, sin una condicion adicional de incluir
   movil. El PDF situa las entregas en las semanas 10 y 14, con valores academicos
   de 15% y 30% (p. 22). No se deducen fechas calendario ni una formula de notas.
4. Las referencias a profesores en ADM3, ADM13, ADM17 y ADM18 se conservan,
   porque pertenecen al PDF. No se agrega una tercera web exclusiva de profesores.

Se propone una web administrativa y un portal de estudiantes, con accesos,
navegacion y permisos diferenciados. Pueden compartir componentes y una base de
codigo; la separacion de usuarios no obliga a duplicar login ni infraestructura.
La app movil es otro cliente y conserva sus historias oficiales.

## Donde consultar cada decision

| Documento | Contenido que gobierna |
|---|---|
| [Contexto de desarrollo](contexto-desarrollo.md) | Sintesis documental para iniciar A2; no sustituye los criterios de origen. |
| [Desarrollo de Jose](desarrollo-jose.md) | Gateway YARP, sesion, componentes React propios y notas; reutiliza la plantilla de Alejandro y documenta pendientes de integracion. |
| [Backlog unico](planificacion/backlog.md) | 41 historias completas, con un responsable por HU; responsables y puntos. |
| [Historias indivisibles](planificacion/subhistorias.md) | Criterios completos de ADM3, ADM17, MOB1 y MOB5; reemplaza las divisiones anteriores. |
| [Historias web de estudiantes](requerimientos/web-estudiantes.md) | Criterios de las 8 HU propuestas por el equipo; no son IDs del PDF. |
| [Analisis y reparto](roadmaps/propuesta.md) | Problemas del reparto anterior, estimacion, carga desigual y riesgos individuales. |
| [Roadmap del avance 2](roadmaps/roadmap%202.md) | Corte de semana 10, dependencias, recorridos y cierre. |
| [Roadmap del avance 3](../tercer_avance/roadmaps/roadmap%203.md) | Incremento final, movil y regresion del avance 2. |
| [Contratos y dependencias](planificacion/contratos.md) | Adaptaciones de V1, seguridad por identidad y bloqueos que deben resolverse. |
| [Evidencia individual](pruebas/criterio-evidencia.md) | Registro por criterio, responsable y entrega, sin resultados ficticios. |

Los roadmaps remiten al backlog; no mantienen repartos alternativos. Un cambio
de propietario, puntos o entrega se realiza primero en el backlog y luego se
recalculan los resumenes. El PDF original no se modifica.

## Corte propuesto

| Entrega | Alcance nuevo | Unidades evaluables | Puntos |
|---|---|---:|---:|
| Avance 2 | Gateway, seguridad, oferta y usuarios administrativos, correo, notas; portal estudiantil con inicio, expediente y prematricula. | 20 | 120 |
| Avance 3 | Gestion administrativa de grupos/estudiantes/matricula, reportes y finanzas; resto del portal estudiantil; app movil completa. | 21 | 120 |
| Total incremental | 41 historias completas: 33 oficiales y 8 WEB. | 41 | 240 |

El profesor no permite dividir historias ni asignar una HU entre dos personas.
Cada historia tiene un responsable unico; se acepta carga desigual por puntos.

| Persona | Puntos A2 | Puntos A3 | Total |
|---|---:|---:|---:|
| Jose | 32 | 33 | 65 |
| Ramses | 30 | 30 | 60 |
| Alejandro | 28 | 32 | 60 |
| Fabian | 30 | 25 | 55 |
| Total | 120 | 120 | 240 |

**Alejandro creo la plantilla de UI/UX y es el responsable de ADM1.** Se reconoce
su autoria; el cierre completo de ADM1 requiere verificar los criterios de la HU.

El avance 3 entrega el sistema acumulado: las 41 historias y la regresion del
avance 2, aunque solo 21 se planifiquen como incremento nuevo. La igualdad de
puntos entre entregas no modifica sus porcentajes academicos; el cierre final
incluye integracion y regresion de todo el sistema.

## Control de versiones de la planificacion

La propuesta del 19 de octubre contabilizaba solo las 33 HU del PDF, con 197
puntos y sin corte entre entregas. La revision incorporo 41 HU y 240
puntos propuestos. Los 43 puntos adicionales corresponden a interfaces web de
estudiantes; no vuelven a cobrar los servicios V1 ni los componentes compartidos.
El reajuste del 28 de octubre elimina las divisiones y atribuye ADM1 a Alejandro.
Para equilibrar A2, GTW1 pasa completa de Alejandro a Jose, que ya tiene GTW2;
ADM1 y el reconocimiento por la plantilla de UI/UX permanecen con Alejandro.
Alejandro recibe ADM24/WEB1/WEB2/WEB4; Fabian recibe ADM4/ADM5/ADM6/ADM8,
siempre como historias completas. Asi quedan en 28 y 30 puntos en A2.
Se conservan el alcance, las estimaciones y las asignaciones del avance 3. El primer avance no se modifica.
Las versiones anteriores permanecen en Git.
