# Roadmap del avance 2

Propuesta revisada el 23 de octubre de 2026. Entrega de semana 10 segun el PDF,
sin inventar fecha calendario. Este corte sustituye la planificacion sin frontera
de entrega del 19 de octubre. Ninguna HU se declara implementada.

## Compromiso de esta entrega

Entregar 22 unidades evaluables de 20 HU de origen completas, 120 puntos:  gateway, seguridad y dos
experiencias web utilizables. La web de estudiantes incluye inicio, expediente
y prematricula; la administrativa incluye usuarios, oferta, notas y correo,
ademas de las vistas por rol que pide el PDF. Movil se entrega en el avance 3.

La fuente unica de asignacion es el [backlog](../planificacion/backlog.md).
El [analisis de reparto](propuesta.md) explica el equilibrio y la carga de cada uno.

| Persona | Historias comprometidas A2 | Puntos |
|---|---|---:|
| Jose | ADM1, ADM2, ADM3-P, ADM17-N, GTW2 | 30 |
| Ramses | ADM7, ADM9, ADM10, ADM11, ADM12, ADM22 | 30 |
| Alejandro | ADM4, ADM5, ADM6, ADM8, GTW1 | 30 |
| Fabian | ADM3-A, ADM17-R, ADM24, WEB1, WEB2, WEB4 | 30 |
| Total | 22 unidades; 20 historias de origen | 120 |

## Que se demuestra al finalizar

1. Login -> gateway -> menu autorizado -> inicio del perfil, con refresh y salida.
2. Administrador: usuarios/roles/parametros/modulos e instituciones/oferta
   academica; filtros, estados y restricciones exigidos por sus historias.
3. Estudiante: inicio -> expediente propio -> cambio permitido de contacto ->
   prematricula propia de primer nivel en periodo futuro.
4. Perfil de profesor del PDF: dashboard -> desglose/notas de un grupo autorizado.
   No se construye un portal independiente de profesores.
5. Administrador: enviar correo sanitizado -> historial real -> informacion
   reciente del dashboard. Ninguna tarjeta se llena con valores ficticios.
6. Cuenta propia: perfil y cambio de contrasena con politica y auditoria.

Las matriculas/grupos/facturas que alimentan notas o metricas se preparan con
servicios V1 y fixtures identificables. El mantenimiento visual de esos datos
no se acredita hasta las HU A3 correspondientes. ADM3 tiene dependencias UI
resueltas en A2: ADM17 (notas), ADM22 (notificaciones) y ADM24 (perfil).

## Secuencia interna del avance 2

| Etapa | Trabajo y responsables | Condicion de salida |
|---|---|---|
| 1. Contratos e identidad | Alejandro define routing, roles y cuenta; Jose token/propiedad; Fabian integra el expediente propio; Ramses acuerda modulos. | Primer login y consulta propia por gateway; prueba con dos estudiantes y rol incorrecto. |
| 2. Bases web y catalogos | Jose ADM1/ADM2; Ramses ADM7 y oferta; Alejandro ADM4/ADM5/ADM6/ADM8; Fabian ADM24. | Menu y permisos persistidos, contratos de oferta disponibles y perfil funcional. |
| 3. Flujos completos | Fabian WEB1/WEB2/WEB4/ADM3-A/ADM17-R; Jose ADM3-P/ADM17-N; Ramses ADM22. | Tramite propio, notas y correo con servicios reales, sin mocks para acreditar cierre. |
| 4. Verificacion individual e integrada | Cada autor completa criterios y documentacion; revisor de apoyo contrasta evidencia. | 22 unidades cerradas y cobertura completa de las 20 HU de origen. |

Las etapas son orden de dependencia, no entregas parciales ni reparto de notas.
Se puede trabajar en paralelo tras acordar contratos; no hace falta esperar a
que termine toda la oferta visual para desarrollar cada formulario.

## Reglas comunes

- Una base de componentes puede servir a ambas webs. ADM1/ADM2 se cuentan una vez.
- Gateway unico para todos los clientes; contrato de rutas externas separado de
  `/interno/**`, que permanece fuera del navegador.
- Validar JWT no sustituye permisos por rol ni propiedad de los datos.
- Las pantallas de mantenimiento siguen listado paginado (maximo 15,
  parametrizable), nuevo/editar, guardar/regresar y confirmacion de eliminacion.
- Bitacoras desde cada servicio con JSON exigido. ADM23 es la pantalla de
  consulta A3; no aplaza el registro de auditoria de las acciones A2.
- Los cambios de API/BD necesarios para cada criterio se incluyen en su HU y
  siguen la aprobacion de cambios de datos del PDF.

## Fuera de este corte

El avance 2 no promete ADM13-ADM16, ADM18-ADM21, ADM23, WEB3, WEB5-WEB8 ni
MOB0-MOB6. Permanecen asignadas en el [avance 3](../../tercer_avance/roadmaps/roadmap%203.md).
No se presentan pantallas vacias como cumplimiento. Los accesos de la primera
entrega solo habilitan funcionalidades disponibles; se amplian con el avance 3.

## Documentacion y criterio de cierre

Cada persona entrega sus fichas por HU y una fila por criterio, con fuente,
precondiciones, esperado, observado, captura y version. Usar la
[guia de evidencia](../pruebas/criterio-evidencia.md). No reutilizar capturas V1
para certificar estas interfaces nuevas.

El paquete A2 contiene analisis/diseno actualizado, diagramas de datos/casos de
uso/clases aplicables, contratos y scripts aprobados, codigo comprometido para
este corte, guia de ejecucion, tablero actualizado, pruebas y PR a la rama final.
El documento incluye conclusiones, recomendaciones y bibliografia. Las partes
pendientes del sistema quedan identificadas en A3, sin declararlas entregadas.

Una HU cierra solo con todos sus criterios satisfechos, integracion real por
gateway, validaciones, permisos, bitacora y evidencia revisada. Demostrar tambien
sesion vencida, dependencia caida e intentos de acceso a datos ajenos.

## Control del compromiso

Todos tienen exactamente 30 puntos. ADM3 y ADM17 se dividen en bloques
funcionales de 5 + 3, con criterios y responsables separados y cierre conjunto
del padre. Ver [subhistorias](../planificacion/subhistorias.md). Si un contrato
revela trabajo adicional, se reestima y se rebalancea antes del corte.
No se reduce un criterio del PDF para conservar el numero de historias.
Las dudas concretas y responsables estan en [contratos](../planificacion/contratos.md).
