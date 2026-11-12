## Resultado del recorrido

Conté **156 comprobaciones reales: 144 aprobadas y 12 fallidas**, con [detalle por responsable y caso](resultados-conservados.md#resultados-reales).

| Integrante | Aprobadas / incluidas | Fallidas | Resultado |
| ---------- | --------------------: | -------: | --------: |
| Alejandro  |                 21/31 |       10 |    67,74% |
| Fabian     |                 20/21 |        1 |    95,24% |
| Jose       |                 79/80 |        1 |    98,75% |
| Ramses     |                 24/24 |        0 |   100,00% |
| Total      |               144/156 |       12 |    92,31% |

Estos porcentajes expresan el resultado del recorrido, no aportes ni cobertura total. Varios fallos corresponden al mismo problema observado en API y navegador. No sumé estas pruebas a las barras locales de 576 casos; la auditoría muestra ambos grupos por separado y la suma de 732 comprobaciones.

| Registro                                                                  | Qué contiene                                                                                                         |       Casos incluidos |
| ------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------- | --------------------: |
| [servicios-y-datos](resultados-conservados.md#observaciones)               | Ingreso, menú, perfiles, consultas, altas temporales, relaciones, restricciones, duplicado, persistencia y limpieza. |                    60 |
| [navegador](resultados-conservados.md#observaciones)                       | Administrador, estudiante suministrado y estudiante temporal, con respuestas reales.                                  |                    34 |
| [Notas: servicios](resultados-conservados.md#observaciones)                | Desglose, notas, permisos, concurrencia, rollback y limpieza; incluye un contenedor de navegador.                     | 41 de sus 42 entradas |
| [Notas: navegador](resultados-conservados.md#observaciones)                | Ingreso, restauración, salida y recorrido docente con modificación persistida.                                      |                    14 |
| [Perfil: servicios](resultados-conservados.md#observaciones)               | Cuenta temporal, rechazo de contraseña anterior, hash y bitácora.                                                   |                     4 |
| [Perfil y parámetros: navegador](resultados-conservados.md#observaciones) | Cambio real de contraseña, nuevo ingreso y confidencialidad de MAILPASS.                                             |                     3 |

Excluí la entrada «Recorrido real de navegador» del registro de servicios de Notas porque sus catorce casos detallados ya se incluyen. Las verificaciones administrativas de limpieza y conservación de cuentas quedan como evidencia adicional, sin inflar las 156 comprobaciones.

## Qué pude comprobar

Inicié veinte servicios/procesos de negocio y gateway con los artefactos disponibles y los destinos compartidos del manifiesto. Después inicié un servicio heredado de apoyo necesario para consultar referencias de período; no lo evalué como historia adicional. Los registros de [arranque](resultados-conservados.md#observaciones) y [apoyo](resultados-conservados.md#observaciones) identifican los procesos. No apliqué migraciones ni cambié configuración versionada.

Recorrí los formularios de usuarios, roles, parámetros, instituciones, módulos, carreras, cursos, profesores, períodos y notificaciones. Al cerrar la bienvenida abrieron correctamente; utilicé el menú actual de acciones, edité un parámetro temporal y confirmé su persistencia. La institución con carrera y la carrera con curso bloquearon su eliminación.

La cuenta estudiantil suministrada pudo ingresar. Su correo encuentra un expediente, pero difieren identificación y tipo entre ambos servicios: las consultas propias devolvieron 404. El [diagnóstico de vínculo](resultados-conservados.md#observaciones) conserva solo indicadores, sin sus datos personales. No modifiqué esa cuenta ni su expediente.

Preparé una cuenta estudiantil temporal con un expediente coherente y territorio válido de referencia. Desde la web guardé contacto con confirmación, recargué el resultado y comprobé persistencia. También creé, modifiqué y cancelé una prematrícula propia; el servidor rechazó el intento de alterar un campo protegido del expediente.

Con otra cuenta temporal cambié la contraseña desde perfil, ingresé con la nueva, confirmé el rechazo de la anterior y comprobé hash BCrypt y bitácora. Las contraseñas de las cuentas suministradas no cambiaron.

Preparé un profesor, rol y cuatro grupos temporales para Notas. Comprobé pertenencia, revocación de su permiso temporal, asignación y modificación, reemplazo con notas existentes, dos altas simultáneas, espera agotada, rollback y persistencia desde web. La competencia entre reemplazo y primera nota produjo 200 para el reemplazo y 422 para la nota sobre el rubro anterior. Completa la condición del orden HTTP inverso que seguía pendiente.

Consulté el historial compartido de Notificación y abrí/cancelé el formulario. No envié un correo nuevo. Mantengo la evidencia de envío y recepción anterior del equipo con su procedencia, sin presentarla como repetida aquí.

## Fallos que agrego o confirmo

| Hallazgo de la auditoría | Qué observé                                                                                                                                                                                                                       |
| ------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| H01                       | Dos prematrículas iguales del estudiante temporal fueron aceptadas con 201; ambas se retiraron.                                                                                                                                    |
| H11                       | Dos rubros de igual nombre devolvieron 500. El rollback conservó el desglose, pero la respuesta no es una validación funcional clara.                                                                                             |
| H12                       | El inicio no comunica disponibilidad ni falta de vínculo, tampoco con datos reales.                                                                                                                                                |
| H13                       | El menú administrativo devuelve activo el acceso a expedientes ajeno a las veinte historias; no recorrí ese mantenimiento.                                                                                                        |
| H15                       | La identificación y el tipo de la cuenta suministrada no coinciden con el expediente de su correo. El rechazo de vinculación es coherente; falta preparar los datos correctos de presentación.                                   |
| H16                       | El perfil estudiantil permite intentar editar correo, pero la acción informa que no existe una operación de backend compatible. No se modificó el correo.                                                                        |
| H17                       | La contraseña SMTP coincide con el texto representado en la tabla de Parámetros. Solo guardé [indicadores de presencia y exposición](resultados-conservados.md#observaciones); el valor no está en el informe ni en su captura. |

El detalle de responsables, causas y condiciones de cierre queda en [pendientes de la auditoría](../../pendientes_auditoria6.md). El caso de rollback aprobado y el caso de respuesta 500 fallido miden condiciones distintas; no los mezclo.

## Preparación, limpieza y conservación

Conservé tres intentos previos de preparación, fuera del conteo final:

- [Preparación inicial](resultados-conservados.md#observaciones): el generador pidió un número de período fuera del rango admitido; el 400 fue correcto y ajusté el ejecutor de auditoría.
- [Preparación de parámetro](resultados-conservados.md#observaciones): el generador incluyó un dígito en un ID que admite solo letras. El 400 fue correcto. La baja del período preparado devolvió 503 al faltar el servicio de referencia; tras iniciarlo se eliminó con 204 y consulta posterior 404, según [su limpieza](resultados-conservados.md#observaciones).
- [Preparación de vínculo](resultados-conservados.md#observaciones) y [su navegador](resultados-conservados.md#observaciones): inicialmente el expediente temporal copió otro tipo de documento y no quedó vinculado. Corregí exclusivamente la preparación de los datos temporales y repetí el recorrido. La baja de su usuario requirió retirar primero sus sesiones; [la limpieza posterior](resultados-conservados.md#observaciones) confirmó 404.

Estos intentos no se presentan como defectos nuevos del producto ni se suman a las ejecuciones finales. Ninguno implicó corregir las cuentas suministradas.

Los registros finales confirman la limpieza de sus cuentas, sesiones y datos temporales. La [comprobación por marcas](resultados-conservados.md#observaciones) verifica que no quedaron los usuarios, profesores, carreras y cursos creados en esos intentos. Notas confirma su propia limpieza. Conservé las bitácoras como registro de las operaciones. La [comprobación final de accesos](resultados-conservados.md#observaciones) confirma que las credenciales suministradas siguieron permitiendo ingresar.

Detuve únicamente los procesos propios: [cierre principal](resultados-conservados.md#observaciones) y [cierre del apoyo](resultados-conservados.md#observaciones). No dejé los servidores de prueba de navegador abiertos.

## Ejecutores y límites

Retiré los ejecutores temporales al organizar la entrega. Conservo a continuación la descripción de cómo preparé y retiré los datos; los enlaces llevan al registro documental:

- [Arranque de servicios](resultados-conservados.md#observaciones): configuración local en memoria, artefactos existentes y bitácoras de proceso bajo `tmp`, fuera de las evidencias publicables. Al finalizar utiliza la marca de detención `tmp/auditoria6-real/detener` o su cierre de proceso para retirar los servicios propios.
- [Servicios y datos](resultados-conservados.md#observaciones) con su [navegador](resultados-conservados.md#observaciones).
- [Servicios de Notas](resultados-conservados.md#observaciones) con su [navegador](resultados-conservados.md#observaciones).
- [Servicios de perfil](resultados-conservados.md#observaciones) con su [navegador de perfil/parámetros](resultados-conservados.md#observaciones).

Los tres ejecutores de servicios recibían `--credenciales` con la ruta del archivo externo. Las cuentas llegan a Node por entrada estándar y no se guardan. Los servidores de navegador usan los puertos 4175, 4176 y 4177 y se cierran al finalizar. No ejecuté pruebas con respuestas simuladas en este recorrido.

Las operaciones con registros temporales son reversibles y su finalidad es verificar el compromiso actual. El recorrido no acredita todos los criterios del PDF: quedan casos específicos de módulo inactivo, territorialidad, restricciones y fallos de correo, además de los defectos documentados. No declaro la entrega lista al 100% por haber podido completar sus recorridos positivos.
