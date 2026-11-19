-- Ejecutar en PrograV_Bitacora. Aditiva; conserva el historial.
SET XACT_ABORT ON;
BEGIN TRANSACTION;
DECLARE @bloqueo int;
EXEC @bloqueo = sys.sp_getapplock @Resource=N'NuevoAvatarAuditoriaSchema', @LockMode='Exclusive', @LockOwner='Transaction', @LockTimeout=15000;
IF @bloqueo < 0 THROW 50001, 'No se pudo preparar la recepcion de auditoria', 1;
IF COL_LENGTH(N'general.bitacora', N'id_evento') IS NULL
    ALTER TABLE general.bitacora ADD id_evento uniqueidentifier NULL;
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID(N'general.bitacora') AND name = N'UX_bitacora_evento')
    EXEC(N'CREATE UNIQUE INDEX UX_bitacora_evento ON general.bitacora(id_evento) WHERE id_evento IS NOT NULL');
COMMIT;
