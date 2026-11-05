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