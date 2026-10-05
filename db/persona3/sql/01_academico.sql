/*
    Nuevo Avatar - Persona 3: oferta académica
    Historias:
        ACD3 - Administración de cursos
        ACD4 - Administración de grupos
        ACD5 - Administración de periodos

  
*/

SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF SCHEMA_ID(N'academico') IS NULL
        THROW 51001, 'El esquema academico no existe. Ejecute primero la estructura académica base.', 1;

   


    IF OBJECT_ID(N'academico.Carrera', N'U') IS NULL
        THROW 51002, 'Falta la dependencia academico.Carrera requerida por ACD3.', 1;

    IF OBJECT_ID(N'academico.Curso', N'U') IS NULL
    BEGIN
        CREATE TABLE academico.Curso (
            CursoId   int          IDENTITY(1,1) NOT NULL,
            CarreraId int          NOT NULL,
            Nivel     tinyint      NOT NULL,
            Nombre    varchar(150) NOT NULL,

            CONSTRAINT PK_Curso
                PRIMARY KEY CLUSTERED (CursoId),

            CONSTRAINT FK_Curso_Carrera
                FOREIGN KEY (CarreraId)
                REFERENCES academico.Carrera (CarreraId),

            CONSTRAINT CK_Curso_Nivel
                CHECK (Nivel BETWEEN 1 AND 12),

            CONSTRAINT CK_Curso_Nombre_NoVacio
                CHECK (LEN(LTRIM(RTRIM(Nombre))) > 0),

            CONSTRAINT CK_Curso_Nombre_SoloLetras
                CHECK (
                    Nombre NOT LIKE '%[^A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]%'
                )
        );
    END;

  

    IF OBJECT_ID(N'academico.Periodo', N'U') IS NULL
    BEGIN
        CREATE TABLE academico.Periodo (
            PeriodoId     int      IDENTITY(1,1) NOT NULL,
            Anio          smallint NOT NULL,
            NumeroPeriodo tinyint  NOT NULL,
            FechaInicio   date     NOT NULL,
            FechaFin      date     NOT NULL,

            CONSTRAINT PK_Periodo
                PRIMARY KEY CLUSTERED (PeriodoId),

            CONSTRAINT UQ_Periodo_Anio_Numero
                UNIQUE (Anio, NumeroPeriodo),

            CONSTRAINT CK_Periodo_Fecha
                CHECK (FechaFin >= FechaInicio)
        );
    END;

    
    IF OBJECT_ID(N'academico.Profesor', N'U') IS NULL
        THROW 51003, 'Falta la dependencia academico.Profesor requerida por ACD4.', 1;

    IF OBJECT_ID(N'academico.Curso', N'U') IS NULL
        THROW 51004, 'Falta academico.Curso requerida por ACD4.', 1;

    IF OBJECT_ID(N'academico.Periodo', N'U') IS NULL
        THROW 51005, 'Falta academico.Periodo requerida por ACD4.', 1;

    IF OBJECT_ID(N'academico.Grupo', N'U') IS NULL
    BEGIN
        CREATE TABLE academico.Grupo (
            GrupoId     int          IDENTITY(1,1) NOT NULL,
            NumeroGrupo int          NOT NULL,
            CursoId     int          NOT NULL,
            ProfesorId  int          NOT NULL,
            Horario     varchar(100) NOT NULL,
            Cupo        int          NOT NULL,
            PeriodoId   int          NOT NULL,

            CONSTRAINT PK_Grupo
                PRIMARY KEY CLUSTERED (GrupoId),

            CONSTRAINT FK_Grupo_Curso
                FOREIGN KEY (CursoId)
                REFERENCES academico.Curso (CursoId),

            CONSTRAINT FK_Grupo_Profesor
                FOREIGN KEY (ProfesorId)
                REFERENCES academico.Profesor (ProfesorId),

            CONSTRAINT FK_Grupo_Periodo
                FOREIGN KEY (PeriodoId)
                REFERENCES academico.Periodo (PeriodoId),

            CONSTRAINT CK_Grupo_Numero
                CHECK (NumeroGrupo > 0),

            CONSTRAINT CK_Grupo_Cupo
                CHECK (Cupo > 0),

            CONSTRAINT CK_Grupo_Horario_NoVacio
                CHECK (LEN(LTRIM(RTRIM(Horario))) > 0)
        );
    END;

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;

    THROW;
END CATCH;