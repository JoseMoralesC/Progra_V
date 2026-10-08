/*
    Persona 5 - MAT5: desglose de rubros.
    Los porcentajes de un grupo deben sumar 100; esa regla se valida en el
    servicio antes de guardar.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF SCHEMA_ID(N'matricula') IS NULL EXEC(N'CREATE SCHEMA matricula');

    IF OBJECT_ID(N'matricula.DesgloseRubro', N'U') IS NOT NULL
        THROW 51002, 'La tabla matricula.DesgloseRubro ya existe.', 1;

    CREATE TABLE matricula.DesgloseRubro (
        RubroId       int          IDENTITY(1,1) NOT NULL,
        GrupoId       int          NOT NULL,
        Nombre        varchar(100) NOT NULL,
        Porcentaje    decimal(5,2) NOT NULL,
        FechaCreacion datetime     NOT NULL
            CONSTRAINT DF_DesgloseRubro_Fecha DEFAULT (GETDATE()),
        CONSTRAINT PK_DesgloseRubro PRIMARY KEY CLUSTERED (RubroId),
        CONSTRAINT FK_DesgloseRubro_Grupo FOREIGN KEY (GrupoId)
            REFERENCES academico.Grupo (GrupoId),
        CONSTRAINT UQ_DesgloseRubro_Grupo_Nombre UNIQUE (GrupoId, Nombre),
        CONSTRAINT CK_DesgloseRubro_Nombre_NoVacio CHECK (
            LEN(LTRIM(RTRIM(Nombre))) > 0
        ),
        CONSTRAINT CK_DesgloseRubro_Porcentaje CHECK (
            Porcentaje > 0 AND Porcentaje <= 100
        )
    );

    CREATE INDEX IX_DesgloseRubro_Grupo
        ON matricula.DesgloseRubro (GrupoId);

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
