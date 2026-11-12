-- Tamaño de página administrativo. Conserva cualquier valor existente.
USE [PrograV_Parametro];
GO
SET XACT_ABORT ON;
BEGIN TRANSACTION;
IF NOT EXISTS (
    SELECT 1 FROM [seguridad].[parametro] WITH (UPDLOCK, HOLDLOCK)
    WHERE [id_parametro] = 'TAMPAG'
)
    INSERT INTO [seguridad].[parametro] ([id_parametro], [valor])
    VALUES ('TAMPAG', '15');
COMMIT TRANSACTION;
SELECT [id_parametro], [valor] FROM [seguridad].[parametro]
WHERE [id_parametro] = 'TAMPAG';
GO
