/*
    Nuevo Avatar - Persona 2: oferta académica (SQL Server)
    Ejecutar en la base elegida. Crea las tablas en orden de dependencias.
    No crea la base, no carga datos de prueba y no modifica tablas existentes.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF EXISTS (
        SELECT 1 FROM sys.tables AS t
        JOIN sys.schemas AS s ON s.schema_id = t.schema_id
        WHERE s.name = N'academico'
    )
        THROW 50002, 'Académico ya contiene tablas; use una base vacía para esta parte.', 1;

    IF SCHEMA_ID(N'academico') IS NULL EXEC(N'CREATE SCHEMA academico');

    CREATE TABLE academico.Institucion (
        InstitucionId int           IDENTITY(1,1) NOT NULL,
        Nombre        nvarchar(150) NOT NULL,
        CONSTRAINT PK_Institucion PRIMARY KEY CLUSTERED (InstitucionId),
        CONSTRAINT CK_Institucion_Nombre CHECK (
            LEN(LTRIM(RTRIM(Nombre))) > 0
            AND Nombre COLLATE Latin1_General_100_BIN2
                NOT LIKE N'%[^A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]%'
        )
    );

    CREATE TABLE academico.Profesor (
        ProfesorId          int           IDENTITY(1,1) NOT NULL,
        TipoIdentificacion  varchar(30)   NOT NULL,
        Identificacion      varchar(30)   NOT NULL,
        Email               varchar(150)  NOT NULL,
        NombreCompleto      nvarchar(150) NOT NULL,
        FechaNacimiento     date          NOT NULL,
        CONSTRAINT PK_Profesor PRIMARY KEY CLUSTERED (ProfesorId),
        CONSTRAINT UQ_Profesor_Email UNIQUE (Email),
        CONSTRAINT UQ_Profesor_Identificacion
            UNIQUE (TipoIdentificacion, Identificacion),
        CONSTRAINT CK_Profesor_Tipo CHECK (
            LEN(LTRIM(RTRIM(TipoIdentificacion))) > 0
        ),
        CONSTRAINT CK_Profesor_Identificacion CHECK (
            LEN(LTRIM(RTRIM(Identificacion))) > 0
        ),
        CONSTRAINT CK_Profesor_Email CHECK (
            Email LIKE '%_@__%.__%' AND Email NOT LIKE '% %'
        ),
        CONSTRAINT CK_Profesor_Nombre CHECK (
            LEN(LTRIM(RTRIM(NombreCompleto))) > 0
            AND NombreCompleto COLLATE Latin1_General_100_BIN2
                NOT LIKE N'%[^A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]%'
        )
    );

    /* Solo los teléfonos se eliminan en cascada con su profesor. */
    CREATE TABLE academico.ProfesorTelefono (
        ProfesorTelefonoId int         IDENTITY(1,1) NOT NULL,
        ProfesorId         int         NOT NULL,
        Telefono           varchar(30) NOT NULL,
        CONSTRAINT PK_ProfesorTelefono
            PRIMARY KEY CLUSTERED (ProfesorTelefonoId),
        CONSTRAINT UQ_ProfesorTelefono UNIQUE (ProfesorId, Telefono),
        CONSTRAINT FK_ProfesorTelefono_Profesor FOREIGN KEY (ProfesorId)
            REFERENCES academico.Profesor (ProfesorId) ON DELETE CASCADE,
        CONSTRAINT CK_ProfesorTelefono_NoVacio CHECK (
            LEN(LTRIM(RTRIM(Telefono))) > 0
        )
    );

    CREATE TABLE academico.Carrera (
        CarreraId           int           IDENTITY(1,1) NOT NULL,
        Nombre              nvarchar(150) NOT NULL,
        InstitucionId       int           NOT NULL,
        DirectorProfesorId  int           NOT NULL,
        CONSTRAINT PK_Carrera PRIMARY KEY CLUSTERED (CarreraId),
        CONSTRAINT FK_Carrera_Institucion FOREIGN KEY (InstitucionId)
            REFERENCES academico.Institucion (InstitucionId),
        CONSTRAINT FK_Carrera_Director FOREIGN KEY (DirectorProfesorId)
            REFERENCES academico.Profesor (ProfesorId),
        CONSTRAINT CK_Carrera_Nombre CHECK (
            LEN(LTRIM(RTRIM(Nombre))) > 0
            AND Nombre COLLATE Latin1_General_100_BIN2
                NOT LIKE N'%[^A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]%'
        )
    );
    CREATE INDEX IX_Carrera_Institucion
        ON academico.Carrera (InstitucionId);
    CREATE INDEX IX_Carrera_Director
        ON academico.Carrera (DirectorProfesorId);

    CREATE TABLE academico.Curso (
        CursoId   int          IDENTITY(1,1) NOT NULL,
        CarreraId int          NOT NULL,
        Nivel     tinyint      NOT NULL,
        Nombre    varchar(150) NOT NULL,
        CONSTRAINT PK_Curso PRIMARY KEY CLUSTERED (CursoId),
        CONSTRAINT FK_Curso_Carrera FOREIGN KEY (CarreraId)
            REFERENCES academico.Carrera (CarreraId),
        CONSTRAINT CK_Curso_Nivel CHECK (Nivel BETWEEN 1 AND 12),
        CONSTRAINT CK_Curso_Nombre_NoVacio CHECK (
            LEN(LTRIM(RTRIM(Nombre))) > 0
        ),
        CONSTRAINT CK_Curso_Nombre_SoloLetras CHECK (
            Nombre NOT LIKE '%[^A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]%'
        )
    );

    CREATE TABLE academico.Periodo (
        PeriodoId     int      IDENTITY(1,1) NOT NULL,
        Anio          smallint NOT NULL,
        NumeroPeriodo tinyint  NOT NULL,
        FechaInicio   date     NOT NULL,
        FechaFin      date     NOT NULL,
        CONSTRAINT PK_Periodo PRIMARY KEY CLUSTERED (PeriodoId),
        CONSTRAINT UQ_Periodo_Anio_Numero UNIQUE (Anio, NumeroPeriodo),
        CONSTRAINT CK_Periodo_Fecha CHECK (FechaFin >= FechaInicio)
    );

    CREATE TABLE academico.Grupo (
        GrupoId     int          IDENTITY(1,1) NOT NULL,
        NumeroGrupo int          NOT NULL,
        CursoId     int          NOT NULL,
        ProfesorId  int          NOT NULL,
        Horario     varchar(100) NOT NULL,
        Cupo        int          NOT NULL,
        PeriodoId   int          NOT NULL,
        CONSTRAINT PK_Grupo PRIMARY KEY CLUSTERED (GrupoId),
        CONSTRAINT FK_Grupo_Curso FOREIGN KEY (CursoId)
            REFERENCES academico.Curso (CursoId),
        CONSTRAINT FK_Grupo_Profesor FOREIGN KEY (ProfesorId)
            REFERENCES academico.Profesor (ProfesorId),
        CONSTRAINT FK_Grupo_Periodo FOREIGN KEY (PeriodoId)
            REFERENCES academico.Periodo (PeriodoId),
        CONSTRAINT CK_Grupo_Numero CHECK (NumeroGrupo > 0),
        CONSTRAINT CK_Grupo_Cupo CHECK (Cupo > 0),
        CONSTRAINT CK_Grupo_Horario_NoVacio CHECK (
            LEN(LTRIM(RTRIM(Horario))) > 0
        )
    );

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
