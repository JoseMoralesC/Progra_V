/*
    Persona 5 - MAT5: notas por rubro.
    Una nota pertenece a una matricula y a un rubro de evaluacion.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF SCHEMA_ID(N'matricula') IS NULL EXEC(N'CREATE SCHEMA matricula');

    IF OBJECT_ID(N'matricula.NotaRubro', N'U') IS NOT NULL
        THROW 51003, 'La tabla matricula.NotaRubro ya existe.', 1;

    CREATE TABLE matricula.NotaRubro (
        NotaRubroId  int          IDENTITY(1,1) NOT NULL,
        MatriculaId  int          NOT NULL,
        RubroId      int          NOT NULL,
        Nota         decimal(5,2) NOT NULL,
        FechaRegistro datetime    NOT NULL
            CONSTRAINT DF_NotaRubro_Fecha DEFAULT (GETDATE()),
        CONSTRAINT PK_NotaRubro PRIMARY KEY CLUSTERED (NotaRubroId),
        CONSTRAINT FK_NotaRubro_Matricula FOREIGN KEY (MatriculaId)
            REFERENCES matricula.Matricula (MatriculaId),
        CONSTRAINT FK_NotaRubro_Rubro FOREIGN KEY (RubroId)
            REFERENCES matricula.DesgloseRubro (RubroId),
        CONSTRAINT UQ_NotaRubro_Matricula_Rubro UNIQUE (MatriculaId, RubroId),
        CONSTRAINT CK_NotaRubro_Nota CHECK (Nota BETWEEN 1 AND 100)
    );

    CREATE INDEX IX_NotaRubro_Matricula
        ON matricula.NotaRubro (MatriculaId);

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
