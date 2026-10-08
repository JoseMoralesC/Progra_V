/*
    Persona 5 - IPN1: encabezado de factura.
    La factura nace a partir de una matricula y queda pendiente de cobro.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF SCHEMA_ID(N'finanzas') IS NULL EXEC(N'CREATE SCHEMA finanzas');

    IF OBJECT_ID(N'finanzas.Factura', N'U') IS NOT NULL
        THROW 51004, 'La tabla finanzas.Factura ya existe.', 1;

    CREATE TABLE finanzas.Factura (
        FacturaId                int          IDENTITY(1,1) NOT NULL,
        MatriculaId              int          NOT NULL,
        IdentificacionEstudiante varchar(30)  NOT NULL,
        PeriodoId                int          NOT NULL,
        Subtotal                 decimal(12,2) NOT NULL,
        Impuesto                 decimal(12,2) NOT NULL,
        Total                    decimal(12,2) NOT NULL,
        Estado                   varchar(20)  NOT NULL
            CONSTRAINT DF_Factura_Estado DEFAULT ('PENDIENTE'),
        FechaFactura             datetime     NOT NULL
            CONSTRAINT DF_Factura_Fecha DEFAULT (GETDATE()),
        CONSTRAINT PK_Factura PRIMARY KEY CLUSTERED (FacturaId),
        CONSTRAINT FK_Factura_Matricula FOREIGN KEY (MatriculaId)
            REFERENCES matricula.Matricula (MatriculaId),
        CONSTRAINT FK_Factura_Periodo FOREIGN KEY (PeriodoId)
            REFERENCES academico.Periodo (PeriodoId),
        CONSTRAINT CK_Factura_Estudiante_NoVacio CHECK (
            LEN(LTRIM(RTRIM(IdentificacionEstudiante))) > 0
        ),
        CONSTRAINT CK_Factura_Montos CHECK (
            Subtotal > 0 AND Impuesto >= 0 AND Total = Subtotal + Impuesto
        ),
        CONSTRAINT CK_Factura_Estado CHECK (
            Estado IN ('PENDIENTE', 'PAGADA', 'ANULADA')
        )
    );

    CREATE INDEX IX_Factura_Periodo
        ON finanzas.Factura (PeriodoId);

    CREATE INDEX IX_Factura_Estudiante
        ON finanzas.Factura (IdentificacionEstudiante);

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
