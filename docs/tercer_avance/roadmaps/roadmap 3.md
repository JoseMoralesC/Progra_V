# Roadmap del avance 3

Propuesta reajustada el 28 de octubre de 2026. Entrega final de semana 14, valor academico
30% segun el PDF (p. 22); sin inferir fecha calendario ni formula de nota.
La asignacion detallada y los puntos gobiernan desde el
[backlog unico](../../segundo_avance/planificacion/backlog.md).

## Compromiso nuevo y acumulado

El incremento nuevo comprende 21 historias completas e indivisibles y 120 puntos. Completa la web
administrativa, el portal estudiantil y las siete historias moviles del PDF.
La entrega final demuestra las **41 HU acumuladas**, incluidas las 20 de A2,
sin contabilizar sus puntos otra vez como trabajo nuevo.

| Persona | Historias nuevas A3 | Puntos nuevos | Total V2 |
|---|---|---:|---:|
| Jose | ADM13, ADM23, MOB0, MOB1, MOB2, MOB3 | 33 | 65 |
| Ramses | ADM18, ADM19, WEB3, WEB6, WEB7, MOB6 | 30 | 60 |
| Alejandro | ADM15, ADM16, WEB5, MOB5 | 32 | 60 |
| Fabian | ADM14, ADM20, ADM21, WEB8, MOB4 | 25 | 55 |
| Total | 21 historias completas | 120 | 240 |

## Flujos que se agregan

1. Administracion: grupos -> expediente -> prematricula/matricula guiada;
   listados, filtros y exportaciones exigidos.
2. Estudiante web: matricularse -> mis cursos -> mis notas -> mi historial.
3. Matricula -> factura -> pago -> reverso administrativo; estudiante consulta
   documentos propios y estados, sin pasarela de pago ni reversos propios.
4. Movil: login seguro -> resumen -> expediente -> prematricula/matricula ->
   cursos/detalle; destinos de navegacion definidos sin enlaces vacios.
5. Administrador: consulta de bitacoras con filtros y detalle antes/despues.
   El registro de auditoria ya debe funcionar desde A2.

El profesor conserva las vistas y permisos que el PDF le asigna en ADM3/13/17/18;
esto no agrega una tercera web ni extiende al estudiante permisos de profesor.

## Secuencia interna

| Etapa | Trabajo y responsables | Condicion de salida |
|---|---|---|
| 1. Contratos y base movil | Jose MOB0/MOB1 completo; Alejandro escritura y flujo completo de matricula movil; Fabian finanzas/prematricula movil; Ramses consultas propias. | Sesion movil real y contratos propios para todos los clientes. Resolver destinos implicitos antes de cerrar la navegacion. |
| 2. Gestion y autoservicio | Jose ADM13/MOB2/MOB3; Alejandro ADM15/ADM16/WEB5/MOB5; Fabian ADM14/ADM20/ADM21/WEB8/MOB4. | Inscripcion y operaciones financieras coherentes, con datos propios y casos negativos. |
| 3. Consultas y auditoria | Ramses ADM18/ADM19/WEB3/WEB6/WEB7/MOB6; Jose ADM23. | Promedios comprobables, filtros/exportaciones y trazabilidad financiera/academica. |
| 4. Regresion y entrega | Cada responsable y su revisor de apoyo. | Matriz de las 41 HU acumuladas, demostracion integrada y paquete documental final. |

Las etapas pueden solaparse mediante contratos acordados. Las pruebas de cada
plataforma permanecen separadas aunque se reutilicen calculos o componentes.
MOB1 pertenece completa a Jose y MOB5 completa a Alejandro, con 8 puntos cada
una. Ver [criterios indivisibles](../../segundo_avance/planificacion/subhistorias.md).
La plantilla de UI/UX creada por Alejandro se reconoce en ADM1 del avance 2;
su reutilizacion no genera puntos adicionales en el avance 3.

## Restricciones para cerrar el alcance

- MOB0 exige destinos para Pagos/Perfil; MOB6 enlaza Notas/Historial. El PDF no
  desglosa todos esos destinos como HU moviles propias. Jose/Ramses deben
  registrar el comportamiento y el esfuerzo con el equipo/profesor al inicio
  de A3. No se acreditan tabs vacios ni se inventa un modulo financiero movil.
- La consulta financiera del estudiante es de lectura. El registro y reverso
  de pagos corresponde a ADM21. Un mismo registro se observa desde ambos perfiles.
- Un token valido no permite consultar otro estudiante. Se repiten pruebas
  por identidad desde web y movil con dos cuentas distintas.
- El servicio conserva las reglas de notas, cupos/duplicados, periodos,
  historial y recuperacion de Pago/Factura. Toda ampliacion se vincula a su HU.
- No se presupone que dos bases diferentes compartan una transaccion SQL.
  En escenarios de fallo se demuestra el estado pendiente y su recuperacion.

## Regresion del avance 2

Se vuelve a demostrar login/refresh/salida, roles/menu, usuarios y oferta,
perfil, expediente estudiantil, prematricula, notas, correo y dashboards.
Cada responsable original acredita que las ampliaciones no rompieron esos
criterios. Esto es trabajo de cierre final, no otras 20 historias con puntos nuevos.

## Paquete final

Analisis y diseno consolidado, diagramas completos, scripts aprobados y orden de
aplicacion, codigo de servicios/webs/movil/gateway, guia de arranque sin secretos,
roles y datos de demostracion, tablero actualizado, matriz de criterios y capturas,
conclusiones/recomendaciones/bibliografia y PR a la rama final.

Los resultados nuevos se registran bajo `docs/tercer_avance/pruebas/` y las
capturas bajo `docs/tercer_avance/evidencia/personaN/`. La evidencia A2 permanece
en su entrega y la regresion final enlaza sus nuevas ejecuciones con cada HU.
Usar la [guia compartida](../../segundo_avance/pruebas/criterio-evidencia.md).

No se presenta una HU parcialmente implementada como cerrada. La nota y su
formula corresponden al profesor; el plan documenta autoria, carga y cumplimiento.
