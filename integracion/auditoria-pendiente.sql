SET XACT_ABORT ON;
BEGIN TRANSACTION;
DECLARE @bloqueo int;
EXEC @bloqueo = sys.sp_getapplock @Resource=N'NuevoAvatarAuditoriaSchema', @LockMode='Exclusive', @LockOwner='Transaction', @LockTimeout=15000;
IF @bloqueo < 0 THROW 50001, 'No se pudo preparar la cola de auditoria', 1;
IF SCHEMA_ID(N'general') IS NULL EXEC(N'CREATE SCHEMA general');
IF OBJECT_ID(N'general.auditoria_pendiente', N'U') IS NULL
BEGIN
    CREATE TABLE general.auditoria_pendiente (
        id_evento uniqueidentifier NOT NULL PRIMARY KEY,
        usuario nvarchar(150) NOT NULL,
        descripcion nvarchar(max) NOT NULL,
        fecha_hora datetime2 NOT NULL,
        intentos int NOT NULL DEFAULT 0,
        proximo_intento datetime2 NOT NULL DEFAULT SYSUTCDATETIME()
    );
    CREATE INDEX IX_auditoria_pendiente_entrega ON general.auditoria_pendiente(proximo_intento,fecha_hora,id_evento);
END;
COMMIT;
