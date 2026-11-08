-- ADM4: registra la última modificación del usuario.
USE [PrograV_Usuario];
GO

IF COL_LENGTH('seguridad.usuario', 'fecha_modificacion') IS NULL
    ALTER TABLE seguridad.usuario
    ADD fecha_modificacion datetimeoffset(0) NULL;
GO
