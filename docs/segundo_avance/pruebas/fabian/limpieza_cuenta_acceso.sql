USE PrograV_Autenticacion;
GO

SET XACT_ABORT ON;
BEGIN TRANSACTION;

DECLARE @Correo nvarchar(255) = N'fabian.acceso.auditoria@cuc.cr';

DELETE FROM seguridad.refresh_token
WHERE email_usuario = @Correo;

SELECT COUNT(*) AS TokensRestantes
FROM seguridad.refresh_token
WHERE email_usuario = @Correo;

COMMIT TRANSACTION;
