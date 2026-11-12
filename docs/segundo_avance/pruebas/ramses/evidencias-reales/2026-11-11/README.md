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

## Alternativa local para ADM22

También preparé `PrograV_Notificacion` en `.\SQLEXPRESS` usando el script oficial `db/por_servicio/Notificacion.sql`. El servicio Notificación se ejecutó contra esa base local, mientras Autenticación, Parámetro, Bitácora y Gateway conservaron el flujo integrado.

Resultados comprobados:

- Menú con permiso ADM22: HTTP `200`.
- `GET /gateway/fin/notificar/historial`: HTTP `200`.
- Historial persistente disponible, inicialmente con cero registros.
- Pantalla ADM22 y modal de composición accesibles mediante el Gateway.

Evidencias:

- `ADM22-notificaciones-historial-real.png`.
- `ADM22-notificaciones-modal-real.png`.

No se realizó un envío SMTP real. Esa prueba requiere revisar los parámetros `MAILHOST`, `MAILPORT`, `MAILUSER`, `MAILPASS`, `MAILFROM` y `MAILTLS`, además de confirmar un destinatario autorizado. Ningún secreto aparece en las capturas.
