/*
    Persona 5 - MAT2: matricula.
    Crea la tabla base para matricular estudiantes en curso, grupo y periodo.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF SCHEMA_ID(N'matricula') IS NULL EXEC(N'CREATE SCHEMA matricula');

    IF OBJECT_ID(N'matricula.Matricula', N'U') IS NOT NULL
        THROW 51001, 'La tabla matricula.Matricula ya existe.', 1;

    CREATE TABLE matricula.Matricula (
        MatriculaId              int          IDENTITY(1,1) NOT NULL,
        IdentificacionEstudiante varchar(30)  NOT NULL,
        CursoId                  int          NOT NULL,
        GrupoId                  int          NOT NULL,
        PeriodoId                int          NOT NULL,
        Estado                   varchar(20)  NOT NULL
            CONSTRAINT DF_Matricula_Estado DEFAULT ('ACTIVA'),
        FechaMatricula           datetime     NOT NULL
            CONSTRAINT DF_Matricula_Fecha DEFAULT (GETDATE()),
        CONSTRAINT PK_Matricula PRIMARY KEY CLUSTERED (MatriculaId),
        CONSTRAINT FK_Matricula_Curso FOREIGN KEY (CursoId)
            REFERENCES academico.Curso (CursoId),
        CONSTRAINT FK_Matricula_Grupo FOREIGN KEY (GrupoId)
            REFERENCES academico.Grupo (GrupoId),
        CONSTRAINT FK_Matricula_Periodo FOREIGN KEY (PeriodoId)
            REFERENCES academico.Periodo (PeriodoId),
        CONSTRAINT CK_Matricula_Estudiante_NoVacio CHECK (
            LEN(LTRIM(RTRIM(IdentificacionEstudiante))) > 0
        ),
        CONSTRAINT CK_Matricula_Estado CHECK (
            Estado IN ('ACTIVA', 'ANULADA')
        )
    );

    CREATE UNIQUE INDEX UX_Matricula_Activa
        ON matricula.Matricula (IdentificacionEstudiante, CursoId, GrupoId, PeriodoId)
        WHERE Estado = 'ACTIVA';

    CREATE INDEX IX_Matricula_Grupo
        ON matricula.Matricula (CursoId, GrupoId, PeriodoId);

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
