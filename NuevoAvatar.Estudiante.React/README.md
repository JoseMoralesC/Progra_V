# Nuevo Avatar - Plantilla Administrativa

Plantilla frontend reutilizable desarrollada con React y Vite para el sistema Nuevo Avatar.

## Tecnologías

- React
- Vite
- React Router DOM
- JavaScript
- CSS

## Requisitos e instalación

Se requiere Node.js 22.15 o posterior y npm.
Configura `FRONTEND_BASE_URL` en el `.env` de la raiz del repositorio con el origen
HTTP(S) del frontend, sin rutas ni credenciales. El archivo esta excluido de Git.
Tambien puedes inyectar la variable desde el entorno de ejecucion.
No uses el prefijo `VITE_`: esta configuracion se consume solo desde Node.
Configura también `GATEWAY_BASE_URL` en ese mismo `.env` con el origen del Gateway.
Los proxies de Vite y el arranque del Gateway comparten ese valor.
En JavaScript se obtiene desde `tools/gateway-env.cjs` como `gatewayBaseURL`;
en PowerShell, desde `tools/gateway-env.ps1`.

```bash
npm ci
```

## Ejecución y verificación

```bash
npm run dev
npm run lint
npm run build
```

Vite usa `FRONTEND_BASE_URL` del `.env` raíz. El servidor
obtiene su host y puerto de esa variable y exige que el puerto esté disponible.
La dirección de acceso sigue siendo visible para quien navega por la aplicación.

## Estructura principal

```text
src/
├── components/
│   ├── Breadcrumb/
│   ├── Footer/
│   ├── Header/
│   ├── Notifications/
│   └── Sidebar/
├── context/
├── layouts/
├── pages/
├── routes/
└── styles/
```

`MainLayout` compone Sidebar, Header, Breadcrumb y Footer. Las notificaciones
se comparten mediante `NotificationContext`. Las pantallas administrativas
se integran mediante las rutas de `src/routes/AppRoutes.jsx`.

## Autenticación ADM2

El login de `src/pages/Login` consume el gateway mediante
`src/services/api.js` y el estado centralizado de `src/context/AuthContext.jsx`:

- `POST /gateway/auth/login`, enviando `usuario` y `contrasena` en headers.
- `GET /gateway/auth/sesion` y `GET /gateway/menu`, con bearer access token.
- `POST /gateway/auth/refresh`, enviando `refresh_token` en el body JSON.
- `POST /gateway/auth/logout`, conservando la salida local aunque falle el registro remoto.

En desarrollo, `vite.config.js` reenvía `/gateway` al origen `GATEWAY_BASE_URL`. El despliegue
debe resolver ese mismo prefijo hacia el gateway; el navegador no necesita
direcciones de los microservicios ni claves internas.

La sesión conserva los tokens en memoria y en `sessionStorage`, sin contraseña.
Al recargar se comprueban identidad y permisos antes de mostrar el contenido.
Un fallo de dependencia muestra Reintentar; un rechazo de identidad retira la
sesión. El refresh se comparte entre peticiones y las respuestas de una cuenta
cerrada no restauran su sesión. Salir limpia primero el cliente. El gateway y
cada servicio conservan la autorización efectiva de los datos.

## Proxy compartido de gateway

Se conserva el proxy `/gateway` hacia `GATEWAY_BASE_URL` del `.env` raiz y el permiso de
Vite para importar módulos dentro del repositorio. `resolve.dedupe` mantiene una
sola instancia de React al consumir código compartido. Estos ajustes permiten
que otras integraciones del repositorio usen el gateway desde este servidor;
las operaciones del navegador utilizan las rutas públicas del gateway.

`AuthProvider` es el proveedor activo de la aplicación principal. Sus páginas
conservan `useAuth`, incluyendo perfil y portal estudiantil. Notas y los seis
mantenimientos de Ramses reciben el adaptador `crearClienteAutenticado`, por lo
que comparten renovación y salida con el resto de la aplicación.

Las rutas integradas son `/notas`, `/modulos`, `/carreras`, `/cursos`,
`/profesores`, `/periodos` y `/notificaciones`; requieren su perfil y módulo.
Las aplicaciones aisladas en `web/jose` y `web/ramses` conservan
`ClienteSesion` para comprobaciones independientes. No se monta un segundo
proveedor de sesión en las rutas de la aplicación principal.

## Componentes compartidos

- `MainLayout`: estructura administrativa.
- `Sidebar`: navegación lateral.
- `Header`: título, breadcrumbs, usuario y salida.
- `Breadcrumb`: navegación jerárquica.
- `Notifications` y `NotificationContext`: mensajes globales.

La visibilidad del menú no sustituye la autorización del backend. La
integración de permisos reales, CRUD, auditoría y módulos de negocio queda
sujeta a los contratos y servicios correspondientes.
