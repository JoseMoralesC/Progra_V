# Plan de los avances 2 y 3

Estado: propuesta documental revisada el 23 de octubre de 2026. No acredita
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
| [Backend de Jose](desarrollo-jose.md) | Implementacion tecnica sin UI/UX, contratos, pruebas locales y pendientes de integracion. |
| [Backlog unico](planificacion/backlog.md) | 45 unidades evaluables de 41 HU de origen; responsables y puntos. |
| [Subhistorias](planificacion/subhistorias.md) | Criterios y trazabilidad de las cuatro divisiones de 8 en 5 + 3. |
| [Historias web de estudiantes](requerimientos/web-estudiantes.md) | Criterios de las 8 HU propuestas por el equipo; no son IDs del PDF. |
| [Analisis y reparto](roadmaps/propuesta.md) | Problemas del reparto anterior, estimacion, equilibrio y riesgos individuales. |
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
| Avance 2 | Gateway, seguridad, oferta y usuarios administrativos, correo, notas; portal estudiantil con inicio, expediente y prematricula. | 22 | 120 |
| Avance 3 | Gestion administrativa de grupos/estudiantes/matricula, reportes y finanzas; resto del portal estudiantil; app movil completa. | 23 | 120 |
| Total incremental | 41 HU de origen: 33 oficiales y 8 WEB; cuatro se dividen en dos. | 45 | 240 |

Cada integrante tiene **30 puntos en A2 y 30 en A3**, por 60 en total.
El usuario confirmo que se permiten subhistorias evaluables. Se conservan
los puntos y alcance originales; no se suman padres e hijos a la vez.

El avance 3 entrega el sistema acumulado: las 41 historias y la regresion del
avance 2, aunque solo 21 se planifiquen como incremento nuevo. La igualdad de
puntos entre entregas no modifica sus porcentajes academicos; el cierre final
incluye integracion y regresion de todo el sistema.

## Control de versiones de la planificacion

La propuesta del 19 de octubre contabilizaba solo las 33 HU del PDF, con 197
puntos y sin corte entre entregas. Esta revision la sustituye por 41 HU y 240
puntos propuestos. Los 43 puntos adicionales corresponden a interfaces web de
estudiantes; no vuelven a cobrar los servicios V1 ni los componentes compartidos.
Las versiones anteriores permanecen en Git.
