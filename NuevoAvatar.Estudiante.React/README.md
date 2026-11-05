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
npm ci
```

## Ejecución

```bash
npm run dev
```

Abrir `http://127.0.0.1:4175`. La aplicación comparte el formulario `Login` y
`ClienteSesion` de `web/jose`, por lo que se ejecuta desde el repositorio completo.
El proxy `/gateway` apunta al gateway local del puerto 5080.

## Integración de sesión (ADM2)

La plantilla UI/UX es aporte de Alejandro. La integración de login/sesión es parte
de ADM2 de Jose y utiliza una única instancia de `ClienteSesion`, disponible en
`useSesion()` para los componentes que se incorporen después.

- Sin sesión, las rutas redirigen a `/login`.
- Después del login se consulta `/gateway/auth/sesion` y se abre `/inicio` con
  nombre y rol reales. No se utilizan usuarios de demostración.
- Al recargar se comprueba la sesión almacenada. Si una dependencia falla,
  se ofrece reintentar o salir sin mostrar el layout como autenticado.
- Se renueva el token antes de vencer, también mientras se permanece en el inicio.
  Si falla la renovación se limpian los tokens y se regresa al login.
- “Salir” retira identidad y tokens antes de esperar la confirmación del servidor.
  Una renovación o consulta tardía no restaura la cuenta cerrada.

El inicio distingue administrador, profesor y estudiante e identifica las funciones
pendientes. Los dashboards de ADM3, el contenido académico WEB1 y la integración
de las pantallas administrativas se mantienen como trabajo pendiente. Se habilitan Inicio y,
para el profesor, Notas;
una URL de un módulo todavía no integrado muestra un mensaje y permite regresar.
Las restricciones de API siguen siendo responsabilidad del gateway y del servicio.

Los permisos ya se consultan con `GET /gateway/menu` al ingresar, restaurar,
navegar y renovar. Notas requiere perfil de profesor y módulo ADM17 activo
asignado a su rol. Inicio permite actualizar permisos explícitamente. Antes de
usar cuentas reales, preparar/verificar las asignaciones según el
[contrato rol-módulo](../docs/segundo_avance/planificacion/permisos-rol-modulo.md).
El gateway no concede acceso por el solo perfil administrador ni usa permisos
de ejemplo si el servicio de módulos no responde.

Para usar cuentas reales deben estar configurados y activos gateway, Autenticación,
Usuario, Rol, Parámetro y Bitácora, además de Profesor o Expediente cuando la
identidad consultada lo requiera. Los lanzadores del repositorio están en `tools/`.
La aplicación no inicia esos servicios ni crea cuentas/datos.

## Notas dentro de la plantilla (ADM17)

El profesor dispone de `/notas`, accesible desde el menú y desde el inicio.
Se monta el componente `Notas` de Jose con la misma instancia de `ClienteSesion`:
consulta de grupos propios, carga de desglose y asignación/modificación de notas.
La pantalla conserva identidad, salida, ruta activa y breadcrumbs; permite volver
al inicio. Administrador y estudiante no ven el acceso y una URL directa muestra
acceso restringido sin montar el componente ni consultar sus API.

Esta integración no sustituye las restricciones del gateway/servicio ni entrega
el dashboard completo de ADM3. Además de las dependencias de sesión, deben estar
activos Notas y sus propietarios de datos, incluidos Grupo, Curso, Periodo y
Matrícula. Ver [alcance y verificación de ADM17](../docs/segundo_avance/pruebas/jose-integracion-notas-layout.md).

## Verificación local de ADM2

```bash
npm run lint
npm run build
npx playwright install chromium
npm run test:ui
```

Las pruebas de navegador interceptan las API y no escriben SQL ni envían correos.
La regresión del cliente compartido se ejecuta con `npm test` desde `web/jose`.
Ver [alcance y resultados de integración](../docs/segundo_avance/pruebas/jose-integracion-sesion-layout.md).

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
