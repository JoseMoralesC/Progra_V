# Capturas preparadas antes de las migraciones

## Alcance

Antes de aplicar las migraciones de Módulo y Notificación documenté parcialmente cuatro historias. Las imágenes fueron generadas con los datos controlados de las pruebas de interfaz para demostrar el diseño, los listados y el bloqueo del fondo al abrir un modal. No las presento como evidencia del recorrido integrado ni como datos obtenidos de SQL mientras `/gateway/menu` continúe respondiendo `503`.

## ADM9 — Carreras

Preparé una captura del listado de carreras con sus datos principales y filtros visibles. También se muestra el modal con los selectores de institución y profesor director. El fondo permanece bloqueado mientras el modal está abierto.

Archivos: `ADM9-carreras-listado.png` y `ADM9-carreras-modal.png`.

## ADM10 — Cursos

Preparé una captura del listado de cursos con los filtros de carrera y nivel. El modal muestra los datos del curso y la selección de carrera sin dejar interactuar con el contenido del fondo.

Archivos: `ADM10-cursos-listado.png` y `ADM10-cursos-modal.png`.

## ADM11 — Profesores

Preparé una captura del listado de profesores donde se ven la búsqueda, el orden y la paginación. También se muestra el modal de mantenimiento con sus campos principales y el fondo bloqueado.

Archivos: `ADM11-profesores-listado.png` y `ADM11-profesores-modal.png`.

## ADM12 — Periodos

Preparé una captura del listado de periodos mostrando el año, número, fechas y los estados derivados. El modal documenta el formulario y su bloqueo de fondo.

Archivos: `ADM12-periodos-listado.png` y `ADM12-periodos-modal.png`.

## Evidencia que dejaré pendiente

- No tomaré todavía la captura del menú administrativo ni de ADM7 porque falta `seguridad.rol_modulo` y el catálogo de módulos.
- No tomaré todavía ADM22 porque falta `general.Notificacion` y el servicio no puede iniciar.
- No usaré respuestas simuladas como si fueran datos reales.
- No mostraré contraseñas, JWT, cadenas de conexión, llaves internas ni configuración SMTP.

## Criterio para aceptar cada captura

En cada imagen mostraré el título de la pantalla, datos cargados, controles propios de la historia y un resultado comprensible. Una captura de Swagger, consola o código no sustituirá la evidencia visual del mantenimiento integrado.
