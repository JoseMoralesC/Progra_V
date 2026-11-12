# ADM3: criterios e integración

Responsable: Fabián Espinoza. Actualización: 12 de noviembre de 2026.
Requisitos: `Proyecto Nuevo Avatar V2 (1).pdf`, páginas 5 y 6, y `subhistorias.md`.

## Alcance

El dashboard muestra el saludo, el perfil y los accesos correspondientes a los permisos vigentes. El administrador consulta los conteos de usuarios, carreras, cursos, períodos activos y facturas pendientes de todos los períodos. El profesor consulta sus grupos y cursos del período vigente y el acceso a notas y desglose.

El acceso de ayuda abre `mailto:soportenuevoavatar@gmail.com`. El enlace **Mi perfil** apunta a `/perfil` para ambos perfiles. La pantalla de destino corresponde a ADM24, a cargo de Alejandro; no forma parte de esta entrega de Fabián.

## Decisiones acordadas

Estas decisiones concretan los aspectos que el PDF no define; no son citas literales del documento.

- Cada resumen muestra como máximo 10 registros recientes, ordenados por fecha e identificador descendentes.
- No hay filtros, buscadores ni selección de usuario, módulo o grupo en el dashboard.
- El administrador recibe bitácoras de los módulos autorizados por su menú vigente. El profesor recibe sus acciones de notas y los eventos de notas/desglose que se puedan relacionar con sus grupos autorizados. Ambos pueden consultar su propia visita al dashboard.
- Los registros con un módulo desconocido se omiten. Los eventos de otras personas se omiten cuando no es posible comprobar su relación con un grupo autorizado.
- Las notificaciones del profesor son envíos con estado `ENVIADA` dirigidos a su correo registrado. Se comprueba la dirección completa, sin coincidencias parciales. El administrador requiere el permiso ADM22 para consultar el historial de notificaciones.
- La bitácora se presenta como resumen de acción, módulo, usuario y fecha. No se entrega al dashboard el JSON de los cambios. Las notificaciones muestran asunto, estado y fecha, sin contenido HTML ni otros destinatarios.

## Contratos existentes

| Petición | Comportamiento |
|---|---|
| `GET /gateway/menu?vista=dashboard` | Registra la visita mediante el servicio de bitácora con el usuario de sesión y una descripción fija. Devuelve el menú si se confirma el registro. |
| `GET /gateway/admin/bitacora?vista=dashboard` | Devuelve un arreglo con `idBitacora`, `usuario`, `descripcion` y `fechaHora`, limitado a los eventos autorizados. |
| `GET /gateway/fin/notificar/historial?vista=dashboard` | Devuelve `{ items: [...] }` con `id`, `asunto`, `estado` y `fecha`, limitado a las notificaciones pertinentes. |
| `GET /gateway/fin/factura?periodoId=N&vista=dashboard` | Con ADM3 devuelve `{ pendientes: N }` del período solicitado, sin datos de facturas. La consulta completa de Factura conserva el permiso ADM20. |

No se crean ni eliminan endpoints. Sin `vista=dashboard`, estas rutas conservan su comportamiento anterior. La excepción para el profesor solo permite GET en las rutas exactas de los resúmenes; no permite escritura, consulta global ni detalle por identificador. Los permisos y la identidad se obtienen en el servidor. Los parámetros enviados por el navegador no amplían el alcance de las respuestas.

## Dependencias de los mantenimientos

Los mantenimientos ADM4, ADM5, ADM6 y ADM8 utilizan modales para crear y editar. La consulta de detalle de usuario y la edición de la matriz de permisos también se presentan en modales.

El gateway permite las siguientes operaciones auxiliares a cuentas con perfil de administrador, sin conceder acceso al mantenimiento completo del servicio consultado:

| Permiso del usuario | Operación auxiliar |
|---|---|
| ADM4 | `GET /gateway/admin/rol`, para las opciones y nombres de roles. |
| ADM5 | `GET /gateway/admin/modulo` y `GET`/`PUT /gateway/admin/modulo/{id}/roles`, para la matriz de permisos. |
| ADM4, ADM5 o ADM8 | `GET /gateway/admin/parametro/TAMPAG`, para el tamaño de página. |

Las autorizaciones originales de cada módulo se conservan. Estas excepciones no habilitan otros métodos o subrutas ni la consulta de otros parámetros.

## Relaciones de bitácora

El esquema de bitácora permanece igual. El módulo se identifica mediante formatos emitidos por los servicios, sin buscar nombres de módulos dentro del JSON o de texto libre. Para eventos de notas/desglose de otros usuarios, se utilizan los formatos de alta y modificación de `AuditoriaNotas`.

La consulta `/profesor/grupos` identifica los grupos autorizados del período vigente. Las consultas existentes de notas y desglose relacionan las matrículas y los rubros con esos grupos. En una nota, ambos deben pertenecer al mismo grupo autorizado; en una modificación se comprueban los estados anterior y actual. Las relaciones eliminadas o incompletas se omiten. Un fallo de un servicio necesario produce un error del resumen, sin devolver la lista global como alternativa.

## Archivos principales

- `NuevoAvatar.Estudiante.React/src/pages/Dashboard/Dashboard.jsx`: presentación y consumo de servicios.
- `NuevoAvatar.Gateway/AuditoriaDashboard.cs`: registro de la visita.
- `NuevoAvatar.Gateway/ResumenDashboard.cs`: selección de registros según sesión, permisos y grupos.
- `NuevoAvatar.Gateway/FacturaDashboard.cs`: conteo acotado de facturas pendientes.
- `NuevoAvatar.Gateway/ClasificacionBitacora.cs`: reconocimiento de formatos de los servicios.
- `NuevoAvatar.Gateway/GatewayApplication.cs` y `NuevoAvatar.Gateway.Seguridad/SeguridadGateway.cs`: integración y autorización de las rutas existentes.

## Verificación

Las pruebas automatizadas con servicios simulados cubren métricas, grupos,
resúmenes, errores, permisos retirados, grupos ajenos, destinatarios
parcialmente coincidentes y límites posteriores a la selección. Las pruebas
del gateway comprueban que ADM3 solo permite el conteo de facturas y que
la consulta completa y las escrituras siguen protegidas por ADM20.

El recorrido con servicios y base reales se registra en
`docs/segundo_avance/pruebas/fabian/resultado_pruebas_reales.md`. Ese recorrido
no modifica permisos de facturación. Este cambio tampoco modifica los servicios
propietarios de notas, notificación o bitácora ni ejecuta scripts en la base
compartida.
