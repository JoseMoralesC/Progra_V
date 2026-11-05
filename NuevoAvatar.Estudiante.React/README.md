# Nuevo Avatar - Plantilla Administrativa

Plantilla frontend reutilizable desarrollada con React y Vite para el sistema Nuevo Avatar.

## Tecnologías

- React
- Vite
- React Router DOM
- JavaScript
- CSS

## Requisitos e instalación

Se requiere Node.js y npm.

```bash
npm ci
```

## Ejecución y verificación

```bash
npm run dev
npm run lint
npm run build
```

Vite sirve la aplicación en `http://127.0.0.1:4175`. El puerto queda fijo para
que coincida con la configuración del proyecto.

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

## Autenticación ADM1

El login de `src/pages/Login` consume los microservicios existentes mediante
`src/services/api.js` y el estado centralizado de `src/context/AuthContext.jsx`:

- `POST /login`, enviando `usuario` y `contrasena` en headers.
- `GET /usuario/{email}` y `GET /rol/{idRol}`, con bearer access token.
- `POST /refresh`, enviando `refresh_token` en el body JSON.

En desarrollo, `vite.config.js` reenvía `/api/auth` al puerto 5158, `/api/users`
al 8082 y `/api/roles` al 5160, eliminando el prefijo antes de reenviar cada
ruta. El proxy solo aplica al servidor de desarrollo. Para otros despliegues,
`VITE_AUTH_API_BASE_URL`, `VITE_USERS_API_BASE_URL` y
`VITE_ROLES_API_BASE_URL` pueden apuntar a URLs accesibles desde el navegador;
el servidor de producción debe resolver CORS si los servicios tienen otro
origen.

Los access y refresh tokens se mantienen en memoria: no se guardan en
`localStorage` ni `sessionStorage`, por lo que al recargar se solicita iniciar
sesión de nuevo. Esto no protege los tokens mientras la aplicación está en uso
ni frente a XSS; el despliegue debe usar HTTPS. El backend no ofrece en esta
integración un endpoint de logout/revocación remota, así que Salir limpia el
estado local. `ProtectedRoute` comprueba que exista una sesión cargada; no es
autorización por rol o módulo. La protección efectiva de recursos corresponde
al backend.

## Proxy compartido de gateway

Se conserva el proxy `/gateway` hacia `http://localhost:5080` y el permiso de
Vite para importar módulos dentro del repositorio. `resolve.dedupe` mantiene una
sola instancia de React al consumir código compartido. Estos ajustes permiten
que otras integraciones del repositorio usen el gateway desde este servidor;
las rutas ADM1 descritas arriba continúan usando sus servicios directos.

`web/jose/src/sesion.js` y `src/sesion/SesionProvider.jsx` conservan el flujo de
sesión basado en rutas `/gateway/auth/*`, `sessionStorage` y consulta del menú
del gateway. Ese contrato y almacenamiento son independientes de
`AuthContext` de ADM1. El proveedor de gateway no se monta globalmente: en el
código actual no hay componentes con `useSesion` ni una ruta de Notas activa en
`AppRoutes`.

Cuando el equipo incorpore una ruta real que consuma gateway, debe envolver
solo esa rama de rutas con `SesionProvider` y usar allí su propio flujo de login,
estado y logout. ADM1 debe permanecer bajo `AuthProvider` y `ProtectedRoute`.
No transferir tokens entre clientes ni usar un proveedor para renovar o cerrar
la sesión del otro. La pantalla ADM17 Notas documentada por `origin/dev` queda
pendiente de reincorporar a una ruta; no se considera integrada por conservar
el cliente y el proveedor.

## Componentes compartidos

- `MainLayout`: estructura administrativa.
- `Sidebar`: navegación lateral.
- `Header`: título, breadcrumbs, usuario y salida.
- `Breadcrumb`: navegación jerárquica.
- `Notifications` y `NotificationContext`: mensajes globales.

La visibilidad del menú no sustituye la autorización del backend. La
integración de permisos reales, CRUD, auditoría y módulos de negocio queda
sujeta a los contratos y servicios correspondientes.
