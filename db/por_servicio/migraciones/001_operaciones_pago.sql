USE [PrograV_Pago]
GO
-- Infraestructura de recuperacion autorizada. No cambia filas migradas.
IF OBJECT_ID(N'finanzas.OperacionPago',N'U') IS NULL
BEGIN
    CREATE TABLE finanzas.OperacionPago (
        OperacionId uniqueidentifier NOT NULL CONSTRAINT PK_OperacionPago PRIMARY KEY,
        PagoId int NOT NULL,
        FacturaId int NOT NULL,
        Tipo varchar(10) NOT NULL,
        EstadoEsperado varchar(20) NOT NULL,
        EstadoDestino varchar(20) NOT NULL,
        Estado varchar(12) NOT NULL CONSTRAINT DF_OperacionPago_Estado DEFAULT('PENDIENTE'),
        Intentos int NOT NULL CONSTRAINT DF_OperacionPago_Intentos DEFAULT(0),
        UltimoError varchar(200) NULL,
        Creada datetime2 NOT NULL CONSTRAINT DF_OperacionPago_Creada DEFAULT(sysutcdatetime()),
        Actualizada datetime2 NOT NULL CONSTRAINT DF_OperacionPago_Actualizada DEFAULT(sysutcdatetime()),
        CONSTRAINT FK_OperacionPago_Pago FOREIGN KEY(PagoId) REFERENCES finanzas.Pago(PagoId),
        CONSTRAINT CK_OperacionPago_Tipo CHECK(Tipo IN ('PAGO','REVERSO')),
        CONSTRAINT CK_OperacionPago_Estado CHECK(Estado IN ('PENDIENTE','COMPLETADA','FALLIDA'))
    );
    CREATE UNIQUE INDEX UX_OperacionPago_Pendiente ON finanzas.OperacionPago(FacturaId) WHERE Estado='PENDIENTE';
    CREATE INDEX IX_OperacionPago_Cola ON finanzas.OperacionPago(Estado,Creada);
END
GO
