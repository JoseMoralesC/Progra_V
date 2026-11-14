# ADM10 — Mantenimiento de cursos

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


- CRUD compatible con los endpoints existentes.
- Filtros combinables por carrera y nivel.
- Nivel válido entre 1 y 12 y carrera remota obligatoria.
- Eliminación protegida por referencias en Grupo, Matrícula y Prematrícula.
- Pantalla React mediante gateway y modales accesibles.
- Pruebas .NET, Node, build y Playwright.

ADM10 usa la estructura existente de `PrograV_Curso`; no requiere migración SQL.
