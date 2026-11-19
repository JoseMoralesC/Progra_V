# Modulos web de Jose - A2

React para ADM2 (login/sesion) y ADM17 (desglose/notas). Se importa el CSS de
la plantilla de Alejandro desde `Usuario.ConsumoWeb`, sin modificar sus archivos.
`Login`, `Notas` y `ClienteSesion` se pueden integrar al layout compartido.

```powershell
npm ci
npm run dev
```

Abrir el origen `JOSE_FRONTEND_BASE_URL` del `.env` raiz. El Gateway usa `GATEWAY_BASE_URL`
y los servicios deben estar configurados/iniciados. La web no crea datos de
demostracion ni conoce credenciales de SQL o claves internas.

```powershell
npm run build
```

El envoltorio local permite ejecutar los componentes propios. El dashboard
por perfil se integra mediante `onAuthenticated`, sin implementarlo aqui.

Las pruebas automatizadas y la configuracion de Playwright se retiraron el 13 de noviembre de 2026. Los informes enlazados se conservan como evidencia historica.

La integración con la plantilla React se abre desde `NuevoAvatar.Estudiante.React`
en el origen definido por `FRONTEND_BASE_URL` del `.env` raiz. Este cliente de 5173 conserva la entrada aislada para
trabajar notas. El componente también está integrado en `/notas` de la plantilla
para el profesor; el dashboard completo sigue pendiente de ADM3.

Guía y resultados: [integración de sesión y layout](../../docs/segundo_avance/pruebas/jose-integracion-sesion-layout.md).
Ver también [integración de notas y layout](../../docs/segundo_avance/pruebas/jose-integracion-notas-layout.md).

El gateway actualizado exige asignaciones activas por módulo, también para este
cliente aislado. Preparación y consumo por el equipo:
[contrato rol-módulo](../../docs/segundo_avance/planificacion/permisos-rol-modulo.md).
La protección concurrente añadida al servicio Notas y su verificación se describen
en [concurrencia y permisos](../../docs/segundo_avance/pruebas/jose-concurrencia-permisos.md).
