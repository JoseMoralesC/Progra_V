# Cierre de observaciones de la auditoría 6 — Ramsés

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


> Nota de mantenimiento (13 de noviembre de 2026): las pruebas automatizadas de `NuevoAvatar.Estudiante.React`, su configuración de Playwright y los ejecutores Python asociados se retiraron del proyecto. Las referencias a esos archivos y comandos en este informe corresponden a ejecuciones históricas y ya no son instrucciones vigentes. Se conservan los resultados y evidencias.


Versión base revisada: `9d29ce9`. Fecha de ejecución: 12 de noviembre de 2026.

## Confidencialidad de MAILPASS

- La API administrativa devuelve `********` y longitud 8, nunca el valor real.
- Crear, modificar y eliminar parámetros registra únicamente el identificador en bitácora.
- Enviar nuevamente la máscara conserva el secreto almacenado.
- La interfaz muestra puntos y utiliza un campo de contraseña.
- Dos pruebas nuevas de confidencialidad pasan dentro de la suite de seguridad.

Resultado de `NuevoAvatar.Autenticacion.Tests`: **31/31 aprobadas**.

## Interfaz integrada de Ramsés

La preparación de Playwright espera y cierra la bienvenida antes de interactuar. El recorrido también utiliza el nuevo menú de acciones para editar y eliminar módulos. Vite se inicia desde el propio proyecto y se cierra de forma determinista.

Resultado de `tests/ui/ramses.spec.js`: **10/10 aprobadas**, sin errores generales.

## Historial compartido de Notificación

El ejecutor dejó de redirigir Notificación a SQL Express. La comprobación real del endpoint interno informó:

- Servicio: `Notificacion`.
- Base: `PrograV_Notificacion`.
- Historial por Gateway: HTTP `200`.

Se ejecutó un envío identificado como `Nuevo Avatar - Cierre Auditoria 6 ADM22`:

- Respuesta: `ENVIADA`.
- Registro encontrado en el historial compartido: sí.
- Estado del historial: `ENVIADA`.
- HTML peligroso eliminado: sí.
- Error: ninguno.

## Reglas negativas y regresión

- Profesor: edad mínima, dominio configurable, teléfonos y grupos activos cubiertos; **6/6 pruebas aprobadas**.
- Notificación: configuración ausente, transporte SMTP fallido y guardado de historial fallido cubiertos; **5/5 pruebas aprobadas**.
- Curso y Periodo: filtros, niveles, estados, conflictos y concurrencia; **21/21 pruebas académicas aprobadas**.
- React: build de producción correcto y lint sin errores. Las advertencias restantes corresponden a componentes de otras áreas incorporados en `dev`.

No se publicaron contraseñas, tokens ni valores SMTP en esta evidencia.
