# Pruebas y estado de persona4

Documento el estado al 4 de octubre de 2026: MAT3, MAT1, ACA1 y ACA2 implementadas localmente. Las migraciones de tablas y relaciones ya están aplicadas en PrograV. La integración completa con los servicios reales del equipo está pendiente.

| Comprobación | Resultado y alcance |
|---|---|
| Suite del backend | 251 pruebas aprobadas, cero fallos, errores u omisiones. H2 en memoria; dependencias externas simuladas. |
| MAT3 | Cinco operaciones, datos obligatorios, nombre/email/dominio, direcciones, identificación única, teléfonos, seguridad, bitácora, referencias y rollback. |
| MAT1 | Cinco operaciones, observaciones opcionales, referencias, cursos de primer nivel, período futuro, seguridad, bitácora y rollback. |
| ACA1 | Acumulado ponderado, pendientes sin aporte, intentos separados, ceros iniciales, parámetros, autorización y errores de integración. |
| ACA2 | Período requerido, campos del PDF, aislamiento de grupos/períodos, autorización, bitácora y errores de dependencia. |
| Backend con SQL Express local | 37 peticiones HTTP aprobadas para MAT3, MAT1 y ACA2, con persistencia JDBC real y dependencias HTTP simuladas. Ejecutadas antes de incorporar ACA1. |
| PrograV | Cuatro tablas nuevas, nueve relaciones habilitadas/verificadas, cuatro llaves primarias, tres índices adicionales y seis CHECK. Sin datos ficticios ni transacciones pendientes. |

En la inspección previa y posterior de PrograV registro que se conservaron las 16 relaciones y 37 columnas existentes incluidas en la revisión. Los catálogos revisados de direcciones y oferta académica estaban vacíos.

## Evidencia que documento

- [Resultados por clase de las 251 pruebas](evidencias/2026-10-04/aca1/resumen-pruebas.json), consolidados de los reportes JUnit originales.
- [Resumen de las 37 peticiones HTTP](evidencias/2026-10-04/http-sql-v2/resumen.json).
- [Peticiones y respuestas HTTP](evidencias/2026-10-04/http-sql-v2/peticiones.json), con datos ficticios y sin credenciales.
- [Confirmación de las migraciones en PrograV](evidencias/2026-10-04/prograv/02_aplicacion.rpt).
- [Resumen de la verificación de estructura](evidencias/2026-10-04/prograv/resumen.json).

Conservo los logs completos, reportes anteriores, capturas de trabajo y auxiliares de pruebas locales en el archivo local del proyecto. No los incluyo en esta selección para el commit.

## Mis pendientes de integración y entrega

- Me propongo configurar URLs y credenciales fuera del repositorio y probar USR5, USR3, GEN1, MAT4, MAT2 y MAT5 reales.
- Me propongo preparar los catálogos y datos acordados con el equipo, luego ejecutar las colecciones Postman.
- Me propongo guardar las capturas requeridas por el PDF para cada criterio; los resultados automatizados no sustituyen esas capturas.
- Me propongo completar con el equipo la documentación de análisis/diseño y los demás entregables del PDF. En este resumen documento los resultados de verificación del módulo.

Considero que MAT5 consulta notas de matrícula ACTIVA por estudiante/curso/grupo. ACA1 mantiene separados los intentos de grupos distintos; si ese contrato no permite distinguir dos matrículas, informa 503. La comprobación con servicios simulados no certifica todos los casos del ambiente compartido.
