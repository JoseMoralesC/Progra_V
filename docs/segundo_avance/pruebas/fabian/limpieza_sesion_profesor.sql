SET XACT_ABORT ON;
BEGIN TRANSACTION;

DELETE FROM seguridad.refresh_token
WHERE email_usuario = @Correo;

SELECT COUNT(*) AS TokensRestantes
FROM seguridad.refresh_token
WHERE email_usuario = @Correo;

COMMIT TRANSACTION;
