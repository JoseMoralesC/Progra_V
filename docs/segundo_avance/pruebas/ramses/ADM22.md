# ADM22 — Notificaciones por correo e historial

- **ID y título:** ADM22 — Envío de notificaciones por correo e historial
- **Origen:** PDF Proyecto Nuevo Avatar V2, p. 12 y reglas comunes pp. 16–19
- **Responsable único:** Ramses (Persona 2)
- **Revisor de apoyo:** Pendiente de revisión cruzada en recorrido integrado
- **Puntos acordados:** 5 puntos (Avance 2)
- **Dependencias y contratos:** `NuevoAvatar.Notificacion`, `NuevoAvatar.Parametro` (`MAILHOST`, `MAILPORT`, `MAILUSER`, `MAILPASS`, `MAILFROM`, `MAILTLS`, `TAMPAG`), `NuevoAvatar.Bitacora`, `db/por_servicio/migraciones/003_notificacion_historial.sql`, consumidor en `ADM3`
- **Estado actual:** En verificación local (sanitización Jsoup, lectura de parámetros, historial ante fallos de configuración/SMTP/SQL, paginación navegable y pruebas locales completas; pendiente prueba SMTP/SQL real en Tailscale)

## Matriz de criterios de aceptación

| HU | Criterio y fuente | Responsable | Precondiciones y pasos | Esperado | Observado | Captura/registro | Versión | Estado |
|---|---|---|---|---|---|---|---|---|
| ADM22 | C1 (PDF p. 12): Formulario con email(s) validados, asunto y cuerpo HTML permitido y sanitizado | Ramses | Ingresar destinatarios, asunto y HTML con `<script>` o atributos `onclick`; intentar destinatario inválido | Rechaza correos inválidos (`422`) y limpia etiquetas/atributos peligrosos con `Jsoup.clean(..., Safelist.relaxed())` antes de enviar y guardar | Verificado en `NotificacionServiceTest` y `notificaciones.test.js` | `NotificacionServiceTest.java`, `notificaciones.test.js` | Rama `ramses` | Parcial con verificación local |
| ADM22 | C2 (PDF p. 12): Confirmación previa y visualización explícita del resultado de envío (éxito, advertencia sin historial o error) | Ramses | Abrir modal en `Notificaciones.jsx`, confirmar envío y verificar mensaje en pantalla | Muestra mensaje de éxito (`ENVIADA`), advertencia sin reenvío si falló el guardado posterior al SMTP (`ENVIADA_SIN_HISTORIAL`) o alerta de error recargando el historial | Verificado en `NotificacionServiceTest.informaEnvioRealizadoAunqueFalleGuardadoDeHistorial` y `Notificaciones.jsx` | `NotificacionService.java`, `Notificaciones.jsx` | Rama `ramses` | Parcial con verificación local |
| ADM22 | C3 (PDF p. 12): Historial de envíos (fecha, destinatarios, asunto, estado) filtrable y paginado (máx. 15) | Ramses | Filtrar por destinatario/estado y recorrer páginas con botones `Anterior` y `Siguiente` | Consulta `/gateway/fin/notificar/historial` con tamaño leído de `TAMPAG` (máx. 15), muestra estados de carga/vacío y navega entre páginas | Verificado en `Notificaciones.jsx` y `notificaciones.test.js` | `Notificaciones.jsx`, `notificaciones-api.js` | Rama `ramses` | Parcial con verificación local |
| ADM22 | C4 (PDF p. 12): Parámetros de servidor/cuenta obtenidos de Parámetros (`MAILHOST`, `MAILPORT`, `MAILUSER`, `MAILPASS`, `MAILFROM`, `MAILTLS`) y registro de fallos | Ramses | Enviar notificación con parámetros completos vs con parámetro faltante o SMTP caído | Si falta configuración devuelve `503` y registra intento en historial con estado `ERROR`; si falla SMTP devuelve `502` y registra `ERROR` en historial | Verificado en `NotificacionServiceTest.registraErrorEnHistorialCuandoFaltanParametrosSmtp` y `guardaHistorialEnErrorCuandoFallaTransporteSmtp` | `NotificacionServiceTest.java` | Rama `ramses` | Parcial con verificación local |

## Verificación automatizada

- `.\NuevoAvatar.Notas\mvnw.cmd -B -pl integracion,NuevoAvatar.Notificacion -am test` (5 pruebas en `NotificacionServiceTest` aprobadas)
- `npm test`, `npm run build` y `npm run test:ui -- --reporter=line` desde `web/ramses`
- Migración idempotente en `db/por_servicio/migraciones/003_notificacion_historial.sql` (pendiente aplicar y capturar en instancia SQL compartida por Tailscale).
