# Modulos web de Jose - A2

React para ADM2 (login/sesion) y ADM17 (desglose/notas). Se importa el CSS de
la plantilla de Alejandro desde `Usuario.ConsumoWeb`, sin modificar sus archivos.
`Login`, `Notas` y `ClienteSesion` se pueden integrar al layout compartido.

```powershell
npm ci
npm run dev
```

Abrir `http://127.0.0.1:5173`. El gateway debe estar disponible en el puerto 5080
y los servicios deben estar configurados/iniciados. La web no crea datos de
demostracion ni conoce credenciales de SQL o claves internas.

```powershell
npm test
npm run build
npx playwright install chromium
npm run test:ui
```

Playwright usa respuestas HTTP simuladas; las pruebas no escriben SQL Server.
El envoltorio local permite ejecutar los componentes propios. El dashboard
por perfil se integra mediante `onAuthenticated`, sin implementarlo aqui.

Guia completa: [desarrollo de Jose](../../docs/segundo_avance/desarrollo-jose.md).
