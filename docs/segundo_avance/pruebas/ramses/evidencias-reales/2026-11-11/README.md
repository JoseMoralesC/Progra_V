# Evidencia real de autenticación administrativa

La imagen `LOGIN-administrador-sesion-real.png` fue generada después de ejecutar el flujo contra el Gateway y los servicios locales conectados a las bases remotas.

Resultados comprobados:

- `POST /gateway/auth/login`: HTTP `201`.
- `GET /gateway/auth/sesion`: HTTP `200`.
- Perfil devuelto: `ADMINISTRADOR`.
- La contraseña, el access token y el refresh token no aparecen en la evidencia.

La primera captura demuestra autenticación e identidad administrativa. En la base compartida, el menú respondía `503` porque no existe `seguridad.rol_modulo`; la alternativa local siguiente permitió completar también esa evidencia.

## Alternativa local para ADM7

Para no modificar la base administrada por José, preparé `PrograV_Modulo` en la instancia local `.\SQLEXPRESS`, donde el usuario de Windows tiene permisos administrativos. Cargué 22 módulos y 12 asignaciones para el rol `ADMIN`.

El flujo comprobado quedó así:

- Login por Gateway: HTTP `201`.
- Sesión administrativa: HTTP `200`.
- Menú autorizado: HTTP `200`, con 12 módulos.
- Pantalla ADM7: listado real de la base local y modal de creación con el fondo bloqueado.

Evidencias:

- `MENU-administrador-real.png`.
- `ADM7-modulos-listado-real.png`.
- `ADM7-modulos-modal-real.png`.

Esta evidencia usa Autenticación, Usuario, Gateway y Bitácora del flujo integrado. Únicamente la base de Módulo se ejecuta localmente como alternativa temporal hasta que la migración se aplique en el servidor compartido.
