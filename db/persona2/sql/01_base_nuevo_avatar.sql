/*
    Nuevo Avatar - esquema inicial de SQL Server
    Fuente: metadatos de db70273 consultados el 30-09-2026.

    Ejecución: seleccionar una base de datos VACÍA antes de correr este archivo.
    No crea la base ni elimina objetos. Si detecta tablas existentes en los
    esquemas del proyecto, se detiene para proteger los datos actuales.

    Incluye las 16 tablas existentes de seguridad, general, academico y
    matricula (catálogo territorial), con claves, índices y validaciones.
    Solo carga catálogos y parámetros mínimos; no copia usuarios, hashes,
    refresh tokens, bitácoras ni valores SMTP de la base de desarrollo.
*/

SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF EXISTS (
        SELECT 1
        FROM sys.tables AS t
        JOIN sys.schemas AS s ON s.schema_id = t.schema_id
        WHERE s.name IN (N'seguridad', N'general', N'academico', N'matricula')
    )
        THROW 50001, 'La base ya contiene tablas del proyecto. Use una base vacía; este script no migra datos.', 1;

    IF SCHEMA_ID(N'seguridad') IS NULL EXEC(N'CREATE SCHEMA seguridad');
    IF SCHEMA_ID(N'general') IS NULL EXEC(N'CREATE SCHEMA general');
    IF SCHEMA_ID(N'academico') IS NULL EXEC(N'CREATE SCHEMA academico');
    IF SCHEMA_ID(N'matricula') IS NULL EXEC(N'CREATE SCHEMA matricula');

    /* Persona 1: roles y cuentas. La clave se guarda como hash BCrypt. */
    CREATE TABLE seguridad.rol (
        id_rol       varchar(20)  NOT NULL,
        nombre_rol   varchar(100) NOT NULL,
        CONSTRAINT PK_rol PRIMARY KEY CLUSTERED (id_rol),
        CONSTRAINT CK_rol_nombre CHECK (
            nombre_rol NOT LIKE '%[^a-zA-Z ]%'
            AND LTRIM(RTRIM(nombre_rol)) <> ''
        )
    );

    CREATE TABLE seguridad.usuario (
        email                varchar(150) NOT NULL,
        tipo_identificacion  varchar(30)  NOT NULL,
        identificacion       varchar(30)  NOT NULL,
        nombre               varchar(150) NOT NULL,
        id_rol               varchar(20)  NOT NULL,
        password_hash        varchar(255) NOT NULL,
        CONSTRAINT PK_usuario PRIMARY KEY CLUSTERED (email),
        CONSTRAINT FK_usuario_rol FOREIGN KEY (id_rol)
            REFERENCES seguridad.rol (id_rol),
        CONSTRAINT CK_usuario_email_formato CHECK (email LIKE '%_@__%.__%'),
        CONSTRAINT CK_usuario_nombre CHECK (LTRIM(RTRIM(nombre)) <> '')
    );

    /* El refresh token es persistente y puede revocarse. Nunca usar tokens
       reales como datos semilla. */
    CREATE TABLE seguridad.refresh_token (
        id_token          int          IDENTITY(1,1) NOT NULL,
        email_usuario     varchar(150) NOT NULL,
        refresh_token     varchar(500) NOT NULL,
        fecha_creacion    datetime     NOT NULL
            CONSTRAINT DF_refresh_token_fecha_creacion DEFAULT (GETDATE()),
        fecha_expiracion  datetime     NOT NULL,
        revocado          bit          NOT NULL
            CONSTRAINT DF_refresh_token_revocado DEFAULT (0),
        CONSTRAINT PK_refresh_token PRIMARY KEY CLUSTERED (id_token),
        CONSTRAINT FK_refresh_token_usuario FOREIGN KEY (email_usuario)
            REFERENCES seguridad.usuario (email)
    );
    CREATE INDEX IX_refresh_token_valor
        ON seguridad.refresh_token (refresh_token);

    CREATE TABLE seguridad.parametro (
        id_parametro varchar(10)  NOT NULL,
        valor        varchar(500) NOT NULL,
        CONSTRAINT PK_parametro PRIMARY KEY CLUSTERED (id_parametro),
        CONSTRAINT CK_parametro_id_mayus CHECK (
            id_parametro NOT LIKE '%[^A-Z]%'
        ),
        CONSTRAINT CK_parametro_valor CHECK (LTRIM(RTRIM(valor)) <> '')
    );

    CREATE TABLE seguridad.modulo (
        id_modulo      varchar(20)  NOT NULL,
        nombre_modulo  varchar(100) NOT NULL,
        CONSTRAINT PK_modulo PRIMARY KEY CLUSTERED (id_modulo),
        CONSTRAINT CK_modulo_nombre CHECK (
            nombre_modulo NOT LIKE '%[^a-zA-Z ]%'
            AND LTRIM(RTRIM(nombre_modulo)) <> ''
        )
    );

    /* Bitácora transversal: GET, altas, cambios, bajas y errores técnicos. */
    CREATE TABLE general.bitacora (
        id_bitacora bigint       IDENTITY(1,1) NOT NULL,
        usuario     varchar(150) NOT NULL,
        descripcion varchar(max) NOT NULL,
        fecha_hora  datetime     NOT NULL
            CONSTRAINT DF_bitacora_fecha_hora DEFAULT (GETDATE()),
        CONSTRAINT PK_bitacora PRIMARY KEY CLUSTERED (id_bitacora),
        CONSTRAINT CK_bitacora_usuario CHECK (LTRIM(RTRIM(usuario)) <> ''),
        CONSTRAINT CK_bitacora_descripcion CHECK (LTRIM(RTRIM(descripcion)) <> '')
    );
    CREATE INDEX IX_bitacora_usuario ON general.bitacora (usuario);
    CREATE INDEX IX_bitacora_fecha ON general.bitacora (fecha_hora);

    /* Persona 2: crear primero catálogos independientes y luego sus relaciones. */
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

    /* El borrado del profesor elimina sus teléfonos; sus carreras y grupos
       siguen protegidos por claves foráneas sin cascada. */
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

    /* Catálogo territorial presente en la base compartida. Los datos de
       provincias, cantones y distritos se cargan por separado. */
    CREATE TABLE matricula.Provincia (
        ProvinciaId int          IDENTITY(1,1) NOT NULL,
        Nombre      varchar(100) NOT NULL,
        CONSTRAINT PK_Provincia PRIMARY KEY CLUSTERED (ProvinciaId),
        CONSTRAINT UQ_Provincia_Nombre UNIQUE (Nombre),
        CONSTRAINT CK_Provincia_Nombre_NoVacio CHECK (
            LEN(LTRIM(RTRIM(Nombre))) > 0
        )
    );

    CREATE TABLE matricula.Canton (
        CantonId    int          IDENTITY(1,1) NOT NULL,
        ProvinciaId int          NOT NULL,
        Nombre      varchar(100) NOT NULL,
        CONSTRAINT PK_Canton PRIMARY KEY CLUSTERED (CantonId),
        CONSTRAINT UQ_Canton_Provincia_Nombre UNIQUE (ProvinciaId, Nombre),
        CONSTRAINT FK_Canton_Provincia FOREIGN KEY (ProvinciaId)
            REFERENCES matricula.Provincia (ProvinciaId),
        CONSTRAINT CK_Canton_Nombre_NoVacio CHECK (
            LEN(LTRIM(RTRIM(Nombre))) > 0
        )
    );

    CREATE TABLE matricula.Distrito (
        DistritoId int          IDENTITY(1,1) NOT NULL,
        CantonId   int          NOT NULL,
        Nombre     varchar(100) NOT NULL,
        CONSTRAINT PK_Distrito PRIMARY KEY CLUSTERED (DistritoId),
        CONSTRAINT UQ_Distrito_Canton_Nombre UNIQUE (CantonId, Nombre),
        CONSTRAINT FK_Distrito_Canton FOREIGN KEY (CantonId)
            REFERENCES matricula.Canton (CantonId),
        CONSTRAINT CK_Distrito_Nombre_NoVacio CHECK (
            LEN(LTRIM(RTRIM(Nombre))) > 0
        )
    );

    /* Catálogos mínimos que usan los servicios. No se cargan los registros
       de prueba encontrados en la base de desarrollo. */
    INSERT INTO seguridad.rol (id_rol, nombre_rol) VALUES
        ('ADMIN', 'Administrador'),
        ('ESTUDIANTE', 'Estudiante'),
        ('PROFESOR', 'Profesor');

    INSERT INTO seguridad.modulo (id_modulo, nombre_modulo) VALUES
        ('ACADEMICO', 'Academico'),
        ('FINANZAS', 'Finanzas'),
        ('MATRICULA', 'Matricula'),
        ('SEGURIDAD', 'Seguridad');

    /* Las expiraciones se interpretan en minutos por AuthService. Los
       valores SMTP deben configurarse aparte para cada entorno. */
    INSERT INTO seguridad.parametro (id_parametro, valor) VALUES
        ('DOMESTUD', 'cuc.cr'),
        ('DOMPROF', 'cuc.ac.cr'),
        ('EXPJWT', '5'),
        ('EXPREFRESH', '60');

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
