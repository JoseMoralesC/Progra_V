-- ADM5: garantiza que los nombres de rol sean únicos en el servicio propietario.
USE [PrograV_Rol];
GO

IF EXISTS (
    SELECT nombre_rol
    FROM seguridad.rol
    GROUP BY nombre_rol
    HAVING COUNT(*) > 1
)
    THROW 51000, 'Existen nombres de rol duplicados. Resolverlos antes de aplicar el índice.', 1;
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE object_id = OBJECT_ID(N'seguridad.rol')
      AND name = N'UX_rol_nombre_rol'
)
    CREATE UNIQUE INDEX UX_rol_nombre_rol ON seguridad.rol (nombre_rol);
GO
