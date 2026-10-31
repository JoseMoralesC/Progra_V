# ADM7 — Mantenimiento de módulos

## Alcance implementado

- CRUD de módulos con estado y orden.
- Asignación reemplazable de roles y menú activo ordenado por rol.
- Ruta de lectura `/gateway/menu/{rolId}` disponible para los roles autenticados.
- Registro en bitácora de creación, modificación, eliminación y asignaciones.
- Pantalla React que consume exclusivamente el gateway.
- Inicio y cierre de sesión desde la pantalla, reutilizando el cliente de sesión de José.
- Formularios y confirmaciones en un modal que bloquea el fondo, atrapa el foco y permite cerrar con `Escape`.

## Verificación automatizada

- `dotnet build NuevoAvatar.Modulo/NuevoAvatar.Modulo.csproj`
- `npm test` y `npm run build` desde `web/ramses`.
- `npm run test:ui` desde `web/ramses`.

La migración `002_modulo_v2.sql` es idempotente y se entrega sin ejecutarla en Tailscale.
