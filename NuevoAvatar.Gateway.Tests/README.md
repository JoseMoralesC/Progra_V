Pruebas de bitácora sin conexión a la base ni a servicios externos:

```powershell
dotnet run --project NuevoAvatar.Gateway.Tests/NuevoAvatar.Gateway.Tests.csproj
```

Validan mensajes legibles, resultados explícitos, compatibilidad con registros históricos,
orden y límite del resumen del profesor, historial completo paginado del administrador
(incluidos eventos de otros usuarios y registros sin clasificar), parámetros de paginación,
historial vacío y navegación estable mientras se agregan movimientos nuevos,
permisos de administrador y profesor, alcance de eventos de
autenticación, login fallido/exitoso, excepciones y registro de accesos denegados.
El proceso termina con error si una verificación falla.

También valida que el detalle conserva la descripción y fecha del registro original y oculta contraseñas.

Las pruebas SQL de caída del receptor, reinicio, confirmación perdida, idempotencia y rollback se ejecutan con `--entrega-sql`. Crean y eliminan exclusivamente una base temporal de nombre único; requieren las variables `DB_HOST`, `DB_PORT`, `DB_USERNAME` y `DB_PASSWORD`. Ver `docs/segundo_avance/auditoria/bitacora-escritura-y-lectura.md`.
