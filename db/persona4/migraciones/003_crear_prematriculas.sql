SET XACT_ABORT ON;
SET NOCOUNT ON;

IF OBJECT_ID(N'matricula.Estudiante', N'U') IS NULL
    OR OBJECT_ID(N'academico.Carrera', N'U') IS NULL
    OR OBJECT_ID(N'academico.Curso', N'U') IS NULL
    OR OBJECT_ID(N'academico.Periodo', N'U') IS NULL
    THROW 51005, 'Faltan estudiante, carrera, curso o periodo para crear prematriculas.', 1;
IF OBJECT_ID(N'matricula.Prematricula', N'U') IS NOT NULL
    OR OBJECT_ID(N'matricula.PrematriculaCurso', N'U') IS NOT NULL
    THROW 51006, 'Las tablas de prematricula ya existen; no se reemplazan automaticamente.', 1;

BEGIN TRANSACTION;

CREATE TABLE matricula.Prematricula (
    PrematriculaId int IDENTITY(1,1) NOT NULL CONSTRAINT PK_Prematricula PRIMARY KEY,
    IdentificacionEstudiante varchar(30) NOT NULL,
    CarreraId int NOT NULL,
    PeriodoId int NOT NULL,
    Observaciones nvarchar(max) NULL,
    CONSTRAINT FK_Prematricula_Estudiante FOREIGN KEY (IdentificacionEstudiante)
        REFERENCES matricula.Estudiante(Identificacion) ON UPDATE CASCADE,
    CONSTRAINT FK_Prematricula_Carrera FOREIGN KEY (CarreraId) REFERENCES academico.Carrera(CarreraId),
    CONSTRAINT FK_Prematricula_Periodo FOREIGN KEY (PeriodoId) REFERENCES academico.Periodo(PeriodoId)
);

-- El detalle conserva los cursos solicitados sin alterar el catalogo academico.
CREATE TABLE matricula.PrematriculaCurso (
    PrematriculaCursoId int IDENTITY(1,1) NOT NULL CONSTRAINT PK_PrematriculaCurso PRIMARY KEY,
    PrematriculaId int NOT NULL,
    CursoId int NOT NULL,
    CONSTRAINT FK_PrematriculaCurso_Prematricula FOREIGN KEY (PrematriculaId)
        REFERENCES matricula.Prematricula(PrematriculaId) ON DELETE CASCADE,
    CONSTRAINT FK_PrematriculaCurso_Curso FOREIGN KEY (CursoId) REFERENCES academico.Curso(CursoId)
);
CREATE INDEX IX_PrematriculaCurso_Prematricula ON matricula.PrematriculaCurso(PrematriculaId);
CREATE INDEX IX_Prematricula_Estudiante ON matricula.Prematricula(IdentificacionEstudiante);

COMMIT TRANSACTION;
