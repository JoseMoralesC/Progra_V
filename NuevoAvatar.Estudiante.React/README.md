# Nuevo Avatar - Plantilla Administrativa

Plantilla frontend reutilizable desarrollada con React y Vite para el sistema Nuevo Avatar.

## Tecnologías
- React
- Vite
- React Router DOM
- JavaScript
- CSS

## Requisitos
- Node.js
- npm

## Instalación

```bash
npm install
```

## Ejecución

```bash
npm run dev
```

## Compilación

```bash
npm run build
```

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

## Componentes compartidos

- MainLayout: estructura principal.
- Sidebar: menú lateral.
- Header: encabezado.
- Breadcrumb: navegación jerárquica.
- Notifications: sistema de notificaciones.
- NotificationContext: gestión global de notificaciones.

## Integración de nuevos módulos

Las pantallas deben integrarse mediante las rutas de la aplicación y reutilizar el layout principal.

Los componentes compartidos deben mantenerse independientes de la lógica particular de cada módulo.

## Consideraciones

La visibilidad de opciones del menú no sustituye la autorización del backend.

La integración de permisos, autenticación y auditoría debe utilizar los servicios correspondientes del sistema.

## Autenticación ADM1

En desarrollo, Vite reenvía las rutas `/api/auth`, `/api/users` y `/api/roles`
a los microservicios locales configurados en `vite.config.js` (puertos 5158,
8082 y 5160). El prefijo se elimina antes de enviar la petición, por lo que se
mantienen las rutas reales `/login`, `/usuario/{email}` y `/rol/{idRol}`.

Para un despliegue distinto, configurar `VITE_AUTH_API_BASE_URL`,
`VITE_USERS_API_BASE_URL` y `VITE_ROLES_API_BASE_URL` con rutas accesibles desde
el navegador. El proxy del archivo Vite solo está activo en el servidor de
desarrollo: el host de producción debe proporcionar esas rutas y resolver CORS
si utiliza orígenes distintos. El repositorio no define un gateway ni una
política CORS para estos servicios.

La sesión vive únicamente en memoria. No se guardan access ni refresh tokens en
`localStorage` o `sessionStorage`; por ello, al recargar la página se solicita
login nuevamente. Esta decisión reduce persistencia en almacenamiento accesible
al JavaScript de la página, pero no protege los tokens durante su uso ni frente
a XSS. El despliegue debe usar HTTPS. El backend no ofrece logout/revocación
remota; el botón Salir limpia solo el estado local.

La ruta protegida verifica que exista una sesión cargada en la aplicación. No
representa autorización por rol o módulo; los permisos reales requieren soporte
backend que todavía no forma parte de esta fase.
