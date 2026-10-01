/*
    Persona 5 - IPN1: detalle de factura.
    El PDF indica el detalle "Servicios estudiantiles".
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF SCHEMA_ID(N'finanzas') IS NULL EXEC(N'CREATE SCHEMA finanzas');

    IF OBJECT_ID(N'finanzas.FacturaDetalle', N'U') IS NOT NULL
        THROW 51005, 'La tabla finanzas.FacturaDetalle ya existe.', 1;

    CREATE TABLE finanzas.FacturaDetalle (
        FacturaDetalleId int           IDENTITY(1,1) NOT NULL,
        FacturaId        int           NOT NULL,
        Descripcion      varchar(200)  NOT NULL,
        Monto            decimal(12,2) NOT NULL,
        CONSTRAINT PK_FacturaDetalle PRIMARY KEY CLUSTERED (FacturaDetalleId),
        CONSTRAINT FK_FacturaDetalle_Factura FOREIGN KEY (FacturaId)
            REFERENCES finanzas.Factura (FacturaId),
        CONSTRAINT CK_FacturaDetalle_Descripcion CHECK (
            LEN(LTRIM(RTRIM(Descripcion))) > 0
        ),
        CONSTRAINT CK_FacturaDetalle_Monto CHECK (Monto > 0)
    );

    CREATE INDEX IX_FacturaDetalle_Factura
        ON finanzas.FacturaDetalle (FacturaId);

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
