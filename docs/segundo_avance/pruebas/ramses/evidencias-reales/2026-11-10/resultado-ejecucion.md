# Ejecución real de las historias de Ramsés — 10 de noviembre de 2026

## Resultado

La compilación de los servicios requeridos terminó correctamente y la cuenta administrativa separada de QA pudo autenticarse por el gateway con respuesta `201`. La cuenta estudiantil de Ramsés no fue modificada.

El recorrido visual real no puede completarse todavía porque la base remota no tiene aplicadas dos migraciones entregadas en el repositorio. No se generaron capturas simuladas ni se expusieron credenciales.

## Bloqueos comprobados

### ADM7 y acceso a las pantallas administrativas

- `GET /gateway/auth/sesion` responde `200` para la cuenta administrativa de QA.
- `GET /gateway/menu` responde `503 Permisos no disponibles`.
- `PrograV_Modulo` no contiene `seguridad.rol_modulo`.
- El catálogo remoto contiene únicamente el módulo de prueba `David02`.
- El usuario SQL configurado no tiene permiso `ALTER` sobre la tabla ni el esquema.

El propietario de la base debe ejecutar, en este orden:

1. `db/por_servicio/migraciones/002_modulo_v2.sql`.
2. Completar los identificadores y la confirmación explícita de `db/por_servicio/configuracion/permisos_a2_inicial.sql`, revisarlo y ejecutarlo.

### ADM22

- Notificación no inicia porque Hibernate valida que falta `general.Notificacion`.
- El usuario SQL configurado no tiene permisos para crear la tabla ni modificar el esquema.

El propietario de la base debe ejecutar:

1. `db/por_servicio/migraciones/003_notificacion_historial.sql`.

## Evidencia pendiente después de las migraciones

Una vez aplicadas las migraciones se repetirá el ingreso administrativo y se guardará una captura sencilla por historia: título de la pantalla, listado o resultado real y mensaje funcional. Las imágenes no mostrarán contraseñas, JWT, cadenas de conexión ni valores SMTP.
