# ADM2 — Integración de sesión con la plantilla React

Fecha: 4 de noviembre de 2026 (America/Mexico_City).
Responsable de ADM2: Jose. Plantilla UI/UX y componentes base: Alejandro.
Base de trabajo: `034ca8b13928ffa1709d69e0b04bd70ceb99d1f0`, con cambios locales
posteriores sin commit al registrar esta ficha.
Estado: incremento de desarrollo implementado y verificado localmente; cierre
integral de ADM2 pendiente.

## Desarrollo incorporado

La aplicación `NuevoAvatar.Estudiante.React` utiliza el formulario `Login` y
`ClienteSesion` existentes de Jose. `SesionProvider` comparte una instancia entre
los componentes, obtiene identidad desde `/gateway/auth/sesion`, restaura sesión
al recargar y protege las rutas antes de mostrar el layout.

El nombre/rol de demostración se reemplazó por identidad del servidor y el botón
Salir quedó conectado. Se programó la renovación antes del vencimiento, incluso
sin solicitudes de negocio, reutilizando el mecanismo de refresh compartido.
Al fallar la renovación se vuelve al login con aviso. Si falla una consulta de
restauración por indisponibilidad, se ofrece reintentar o cerrar sesión y se oculta
el contenido autenticado mientras no se pueda comprobar la identidad.

Salir retira tokens e identidad sin esperar al servidor. Las respuestas pendientes
se descartan si cambió la sesión. El cliente compartido también descarta tokens
de un login que no obtiene una identidad autorizada o no logra completar esa
consulta. El cierre conserva el contrato existente: invalida credenciales del
cliente y solicita el registro de logout; no se afirma revocación de JWT emitidos.

Los tres perfiles llegan a `/inicio` con su identidad. Ese destino identifica
explícitamente el trabajo todavía pendiente: dashboard administrativo/profesor y
contenido académico del estudiante. No se implementaron ADM3 o WEB1 completos,
ni se atribuye al equipo una integración de menú/permisos todavía ausente.
Las opciones demostrativas no aparecen como módulos disponibles; una URL sin
pantalla integrada informa la situación y permite volver al inicio.

El desarrollo principal está en:

- `NuevoAvatar.Estudiante.React/src/sesion/SesionProvider.jsx` y `SesionContext.js`.
- `NuevoAvatar.Estudiante.React/src/routes/AppRoutes.jsx` y conexión desde `App.jsx`.
- `NuevoAvatar.Estudiante.React/vite.config.js`: proxy al gateway 5080, puerto
  propio 4175 y una sola copia de React al importar el formulario compartido.
- `web/jose/src/sesion.js`: limpieza de login incompleto y rechazo de identidad
  recibida después de cerrar o cambiar la sesión.

## Verificación del incremento

Las pruebas de navegador usan respuestas HTTP controladas y se ejecutan contra
la aplicación React con sus componentes reales. No acreditan SQL, disponibilidad
de propietarios, envío de bitácoras real ni aprobación final por criterio.

| Área | Comprobación local | Límite |
| --- | --- | --- |
| Ingreso por perfil | Administrador/profesor/estudiante llegan al layout con nombre/rol de `/sesion`; recarga conserva identidad comprobada | API interceptada, sin credenciales reales |
| Credenciales inválidas | Se muestra el mensaje requerido y no se abre el layout | Respuesta 401 simulada |
| Identidad rechazada | 403 o perfil no reconocido no conservan una cuenta autenticada | Verificación de UI/cliente, sin alterar roles reales |
| Rutas protegidas | URL directa sin sesión retorna a login | No reemplaza autorización de backend |
| Renovación | Timer renueva credenciales y mantiene layout; restauración con token vencido comparte un solo refresh en StrictMode | Reloj y transporte controlados |
| Expiración | Refresh rechazado limpia tokens y retira identidad | Rechazo simulado |
| Dependencia caída | Restauración 503 oculta layout, conserva tokens para reintento y permite recuperarse | Servicio simulado |
| Salida | Limpieza incluso si Auth falla; atrás/recarga no recuperan cuenta; refresh tardío no la restaura | No acredita persistencia de bitácora de logout |
| Pantallas pendientes | URL de expediente no muestra un trámite implementado | WEB2/WEB4 siguen pendientes |
| Login móvil | Formulario visible y sin desbordamiento a 390 × 844 | No es una batería responsive de ADM1 |

Comandos de comprobación:

```powershell
npm.cmd test --prefix web/jose
npm.cmd run test:ui --prefix web/jose -- --reporter=line
npm.cmd run lint --prefix NuevoAvatar.Estudiante.React
npm.cmd run build --prefix NuevoAvatar.Estudiante.React
npm.cmd run test:ui --prefix NuevoAvatar.Estudiante.React -- --reporter=line
```

Resultados finales de esta ejecución:

- Cliente de sesión compartido: **10/10 pruebas Node aprobadas**.
- Regresión de login/notas en `web/jose`: **6/6 pruebas de navegador aprobadas**.
- Integración en la plantilla React: **15/15 pruebas de navegador aprobadas**.
- Build de la plantilla: aprobado.
- Lint: sin errores; permanece una advertencia previa de Fast Refresh en
  `src/context/NotificationContext.jsx`, ajena a este incremento.

Son **31 pruebas distintas aprobadas**, con los límites de simulación descritos.
Las capturas de fallos, si los hay, se generan en los directorios ignorados de
Playwright. Esta ficha no modifica los porcentajes de la tercera auditoría,
que conserva el estado anterior a este desarrollo.

## Cómo abrir la integración

Desde la raíz, iniciar gateway y servicios configurados con los lanzadores de
`tools/`. Para la sesión son necesarios Autenticación, Usuario, Rol, Parámetro,
Bitácora y el propietario de identidad Profesor/Expediente cuando corresponda.

```powershell
powershell -NoProfile -File tools/ejecutar-gateway.ps1
```

En otra terminal:

```powershell
cd NuevoAvatar.Estudiante.React
npm.cmd ci
npm.cmd run dev
```

Abrir `http://127.0.0.1:4175`. La aplicación requiere el repositorio completo
porque importa el login/cliente compartidos desde `web/jose`.

## Pendientes de cierre de ADM2

Integrar los destinos finales de ADM3 y WEB1 y el menú dinámico acordado con
ADM1/ADM5/ADM7; comprobar los recorridos con cuentas y servicios reales, incluyendo
expiración/salida y bitácoras persistidas. Registrar captura por criterio,
versión comprometida y revisión de apoyo. La integración de notas en el layout
se conserva como siguiente incremento de ADM17, sin darla por realizada aquí.

Actualización posterior del mismo día: la integración de ADM17 se documenta por
separado en [notas dentro del layout](jose-integracion-notas-layout.md). Los
resultados anteriores describen el corte inicial de ADM2.
