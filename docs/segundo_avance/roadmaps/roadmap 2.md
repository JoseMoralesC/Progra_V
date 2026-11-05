0

# Roadmap del avance 2

Propuesta reajustada el 28 de octubre de 2026. Entrega de semana 10 segun el PDF,
sin inventar fecha calendario. Este corte sustituye la planificacion sin frontera
de entrega del 19 de octubre. Ninguna HU se declara implementada.

## Compromiso de esta entrega

Entregar 20 historias completas e indivisibles, 120 puntos:  gateway, seguridad y dos
experiencias web utilizables. La web de estudiantes incluye inicio, expediente
y prematricula; la administrativa incluye usuarios, oferta, notas y correo,
ademas de las vistas por rol que pide el PDF. Movil se entrega en el avance 3.

La fuente unica de asignacion es el [backlog](../planificacion/backlog.md).
El [analisis de reparto](propuesta.md) explica la carga desigual y la carga de cada uno.

| Persona   | Historias comprometidas A2             | Puntos |
| --------- | -------------------------------------- | -----: |
| Jose      | ADM2, ADM17, GTW1, GTW2                |     32 |
| Ramses    | ADM7, ADM9, ADM10, ADM11, ADM12, ADM22 |     30 |
| Alejandro | ADM1, ADM24, WEB1, WEB2, WEB4          |     28 |
| Fabian    | ADM3, ADM4, ADM5, ADM6, ADM8           |     30 |
| Total     | 20 historias completas                 |    120 |

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

| Etapa                                  | Trabajo y responsables                                                                                                       | Condicion de salida                                                                      |
| -------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- |
| 1. Contratos e identidad               | Fabian define roles y cuenta; Jose routing, token/propiedad; Alejandro integra el expediente propio; Ramses acuerda modulos. | Primer login y consulta propia por gateway; prueba con dos estudiantes y rol incorrecto. |
| 2. Bases web y catalogos               | Jose ADM2; Ramses ADM7 y oferta; Alejandro ADM1 (plantilla de UI/UX creada por el) y ADM24; Fabian ADM4/ADM5/ADM6/ADM8.      | Menu y permisos persistidos, contratos de oferta disponibles y perfil funcional.         |
| 3. Flujos completos                    | Alejandro WEB1/WEB2/WEB4; Fabian ADM3; Jose ADM17; Ramses ADM22.                                                             | Tramite propio, notas y correo con servicios reales, sin mocks para acreditar cierre.    |
| 4. Verificacion individual e integrada | Cada autor completa criterios y documentacion; revisor de apoyo contrasta evidencia.                                         | 20 HU cerradas, cada una con un responsable unico.                                       |

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
MOB0-MOB6. Permanecen asignadas en el [avance 3](<../../tercer_avance/roadmaps/roadmap%203.md>).
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

Cada HU se asigna completa a una persona: ADM3 a Fabian y ADM17 a Jose.
Alejandro creo la plantilla de UI/UX y asume ADM1 completa. GTW1 pasa completa
a Jose, junto con GTW2, para equilibrar A2 sin dividir historias. Alejandro
recibe ADM24/WEB1/WEB2/WEB4 y Fabian recibe ADM4/ADM5/ADM6/ADM8 completas:
quedan en 28 y 30 puntos respectivamente. La carga puede ser
desigual; se conservan las estimaciones. Ver los
[criterios indivisibles](../planificacion/subhistorias.md). Si un contrato revela
trabajo adicional, se reestima y se reasignan historias completas antes del corte.
No se reduce un criterio del PDF para conservar el numero de historias.
Las dudas concretas y responsables estan en [contratos](../planificacion/contratos.md).
