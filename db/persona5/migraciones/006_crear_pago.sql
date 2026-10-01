/*
    Persona 5 - IPN2: pagos.
    Un pago cancela una factura; al reversar, el servicio marca el pago como
    anulado y devuelve la factura a pendiente.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF SCHEMA_ID(N'finanzas') IS NULL EXEC(N'CREATE SCHEMA finanzas');

    IF OBJECT_ID(N'finanzas.Pago', N'U') IS NOT NULL
        THROW 51006, 'La tabla finanzas.Pago ya existe.', 1;

    CREATE TABLE finanzas.Pago (
        PagoId      int           IDENTITY(1,1) NOT NULL,
        FacturaId   int           NOT NULL,
        PeriodoId   int           NOT NULL,
        Monto       decimal(12,2) NOT NULL,
        Estado      varchar(20)   NOT NULL
            CONSTRAINT DF_Pago_Estado DEFAULT ('APLICADO'),
        FechaPago   datetime      NOT NULL
            CONSTRAINT DF_Pago_Fecha DEFAULT (GETDATE()),
        CONSTRAINT PK_Pago PRIMARY KEY CLUSTERED (PagoId),
        CONSTRAINT FK_Pago_Factura FOREIGN KEY (FacturaId)
            REFERENCES finanzas.Factura (FacturaId),
        CONSTRAINT FK_Pago_Periodo FOREIGN KEY (PeriodoId)
            REFERENCES academico.Periodo (PeriodoId),
        CONSTRAINT CK_Pago_Monto CHECK (Monto > 0),
        CONSTRAINT CK_Pago_Estado CHECK (
            Estado IN ('APLICADO', 'ANULADO')
        )
    );

    CREATE INDEX IX_Pago_Factura
        ON finanzas.Pago (FacturaId);

    CREATE INDEX IX_Pago_Periodo
        ON finanzas.Pago (PeriodoId);

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
