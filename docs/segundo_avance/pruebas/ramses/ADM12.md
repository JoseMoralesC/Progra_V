# ADM12 — Mantenimiento de periodos

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


- Estado derivado `FUTURO`, `ACTIVO` o `CERRADO`.
- Filtros por año y estado.
- Rechazo de fechas invertidas, duplicados y rangos solapados con `409`.
- Eliminación protegida por las referencias configuradas.
- Pantalla React con modales accesibles y pruebas .NET/Node/Playwright.

Usa la estructura existente de `PrograV_Periodo`; no requiere migración SQL.
