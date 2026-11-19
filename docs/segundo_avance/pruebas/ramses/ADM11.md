# ADM11 — Mantenimiento de profesores

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


## Alcance implementado

- CRUD con mayoría de edad, dominio `DOMPROF`, identificación, correo y teléfonos únicos.
- Búsqueda por nombre, identificación o correo; paginación y orden con campos permitidos.
- Eliminación bloqueada únicamente cuando el profesor tiene un grupo cuyo periodo está activo en la fecha actual.
- Pantalla React mediante gateway y modales accesibles que bloquean el fondo.
- Bitácora existente para consultas, cambios y errores controlados.

## Verificación

- Reactor Maven para integración y Profesor.
- Pruebas Node y build Vite en `web/ramses`.
- Regresión de gateway y Playwright del modal.
