# ADM9 — Mantenimiento de carreras

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


## Alcance implementado

- CRUD y filtro existente por institución.
- Institución y profesor director validados mediante sus servicios propietarios.
- Eliminación bloqueada con `409` cuando la carrera tiene cursos.
- Selectores reales y buscables de institución y profesor.
- Pantalla React mediante gateway y modales accesibles que bloquean el fondo.
- Bitácora existente para consultas, cambios y errores.

## Verificación

- Reactor Maven para integración y Carrera.
- Pruebas de referencias y bloqueo por cursos.
- Pruebas Node, build Vite y Playwright.

ADM9 usa la estructura existente de `PrograV_Carrera`; no requiere migración SQL.
