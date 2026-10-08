# Persona 5 - Jose: demostracion del 8 de octubre de 2026

## Estado de los datos

Prepare y verifique estos datos en la base **PrograV**, el 8 de octubre de
2026. El grupo principal esta limpio: cero matriculas, rubros, notas,
facturas y pagos. Las creaciones del recorrido quedan pendientes para
realizarlas frente al profesor. No envie correos durante la preparacion.

Las matriculas 3 y 4 de ayer estan ANULADAS. El grupo anterior 1 conserva
los rubros 3 y 4, con dos notas. Por eso repetir el recorrido completo
alli puede devolver 409 al reemplazar desglose, aunque la matricula este
anulada. Compare las filas anteriores antes y despues de preparar los
datos nuevos: no cambiaron matriculas, rubros, notas, facturas ni pagos.

## Coleccion lista para importar

Importar [Persona5_Jose_Demo_20261008.postman_collection.json](postman/Persona5_Jose_Demo_20261008.postman_collection.json).

Su nombre es **Jose - Persona 5 - Demostracion 2026-10-08**. Tiene un ID de
coleccion distinto al recorrido anterior, 79 solicitudes y las variables
academicas ya configuradas. Los contratos, rutas y pruebas son los del
recorrido de Jose. La coleccion original no fue modificada.

1. Seleccionar la coleccion nueva y mantener **No Environment**.
2. En Variables, completar `contrasena` con la misma clave del login de ayer.
   El usuario sigue siendo `jose`. No se exporta la clave en este archivo.
3. Confirmar que Seguridad este disponible en `http://localhost:5158`
   y Persona 5 en `http://localhost:8085`.
4. Ejecutar `00.1 Login - guarda token` y `00.2 Validar token`.
   El token anterior no sirve como preparacion para hoy: obtener uno nuevo.
5. Realizar el recorrido manualmente. No ejecutar toda la coleccion con
   Runner: hay envio de correo y casos cuyo resultado depende del orden.

Los scripts escriben variables de coleccion. Un entorno o variables de
otra prioridad con los mismos nombres pueden ocultar esos valores.

## Variables iniciales de hoy

| Variable | Valor |
|---|---|
| baseUrl | http://localhost:8085 |
| seguridadUrl | http://localhost:5158 |
| usuario | jose |
| contrasena | La misma del login; completar localmente |
| token | Vacio antes del nuevo login |
| estudiante | P5-JOSE-DEMO-20261008 |
| cursoId | 4 |
| grupoId | 3 |
| periodoId | 1 |
| grupoPruebasConfirmado | true |
| matriculaId | Vacio; lo obtiene 02.1 |
| rubroId, rubro2Id | Vacios; los obtiene el desglose |
| notaId | Vacio; lo obtiene la asignacion |
| facturaId | Vacio; lo obtiene 04.1 |
| pagoId | Vacio; lo obtiene 05.1 |
| idInexistente | 2147483647; verificado ausente |
| emailDestino | El mismo buzon de prueba de la captura de ayer |
| otroCursoId | 2 |
| periodoInactivoId | 3 |
| grupoInactivoId | 4 |
| rubroOtroGrupoId | 3 |

El periodo 1 conserva su vigencia del **2026-01-01 al 2026-12-31**.
Se reutiliza porque sigue siendo valido; no tiene que ser nuevo.
El periodo 3 termina el **2026-10-07** y solo se utiliza para demostrar
rechazo de periodo no vigente.

El expediente `P5-JOSE-DEMO-20261008-M` tambien existe. La solicitud 02.4
agrega `-M` a la identificacion y actualiza automaticamente `estudiante`.
Despues de esa solicitud, el valor debe quedar
`P5-JOSE-DEMO-20261008-M`. **No ejecutar 02.4 dos veces seguidas**, porque
intentaria usar una identificacion con `-M-M` que no fue preparada.

`rubroOtroGrupoId=3` es un rubro del grupo anterior, utilizado solo como
referencia de lectura para el rechazo 09.4. No pertenece al grupo nuevo 3:
la coincidencia entre esos numeros no significa que sean la misma entidad.

## Orden para demostrar

1. **00 - Login y validacion:** obtener token y comprobar 200 con `true`.
2. **01 - Seguridad:** solicitudes sin token y token invalido deben dar 401.
3. **02 - MAT2:** crear matricula (201), consultar (200), probar duplicado
   (409), modificar identificacion una sola vez y verificar el cambio.
   Los demas rechazos de la carpeta se pueden mostrar con esos mismos IDs.
4. **03 - MAT5:** cargar y consultar rubros; reemplazarlos antes de asignar
   notas; comprobar suma invalida; asignar/modificar notas y probar limites.
   Al existir notas, reemplazar rubros debe dar 409.
5. **09 - Casos adicionales:** ejecutar mientras la matricula siga ACTIVA,
   despues de MAT5 y antes de 07. Otro curso, periodo incompatible, periodo
   pasado y rubro de otro grupo deben dar 422. Los cuatro datos necesarios
   ya estan configurados.
6. **04 - IPN1:** crear factura con `matriculaId`, comprobar subtotal 30000,
   impuesto 600, total 30600, PENDIENTE y detalle Servicios estudiantiles.
7. **05 - IPN2:** registrar pago (201) y comprobar factura PAGADA. El reverso
   de factura pagada debe dar 409. Reversar pago, comprobar PENDIENTE,
   reversar factura y comprobar ANULADA.
8. **06 - IPN3:** probar requeridos y email invalido. Ejecutar envio HTML
   solo si SMTP esta configurado y se desea mostrar la recepcion.
9. **07 - MAT2:** anular matricula al final y verificar ausencia de la lista
   de activas. Comprobar los rechazos de nota/factura con matricula anulada.
10. **08 - Bitacora:** consultar las entradas recientes del recorrido.
    La existencia de una entrada no demuestra el formato completo exigido:
    el filtro actual no captura todos los JSON anteriores y eliminados.

Si vence el token, repetir solo 00.1 y 00.2. No repetir creaciones ni
restablecer estudiante al valor inicial a mitad de la demostracion.

La modificacion de matricula puede reactivarla en el codigo actual, pero
no hay una solicitud independiente de reactivacion en esta coleccion.
No agregar ese paso antes de cerrar los casos de matricula anulada:
cambiaria sus resultados esperados.

## Que se puede reutilizar

- Usuario y contrasena de login, URLs, servidor SMTP y buzon de pruebas.
- Periodo vigente, carrera, profesor y distrito del expediente.
- Curso anterior y un rubro anterior, solo para casos negativos de referencias.

Para repetir todo el ciclo se necesitan un grupo sin notas y variables
generadas vacias. Cambiar solamente estudiante o token no limpia el
desglose del grupo anterior.

## Archivos y verificacion

- [Fixture SQL](persona5-demo-20261008.sql): prepara curso/grupos, dos
  expedientes y periodo pasado en una transaccion. No cambia datos anteriores
  ni crea operaciones de negocio. Fue ejecutado correctamente.
- [Verificacion SQL](persona5-demo-20261008-verificacion.json): IDs obtenidos,
  grupo limpio, expedientes, casos negativos y preservacion del recorrido anterior.
- [Coleccion de hoy](postman/Persona5_Jose_Demo_20261008.postman_collection.json):
  valores iniciales y solicitudes preparadas; no es un reporte de ejecucion.

El script puede volver a consultar/preparar el mismo fixture mientras el
grupo siga sin matriculas ni rubros. Despues de utilizarlo, rechaza una
repreparacion en lugar de borrar datos: para otra demostracion completa
hay que preparar otro conjunto exclusivo.

## Antes de empezar

En la preparacion no encontre servicios escuchando en los puertos 5158 y
8085. Mantenerlos iniciados durante la demostracion. Persona 5 carga el
`.env` de la raiz; revisar su conexion y SMTP sin mostrar secretos.

Desde `services/persona5`, iniciar el servicio Java con:

```powershell
..\persona2\mvnw.cmd -f pom.xml spring-boot:run
```

La puesta en marcha de Seguridad se documenta en su carpeta de servicio.
Esta preparacion verifica datos SQL; no certifica una ejecucion HTTP nueva
ni envio de correo de hoy.
