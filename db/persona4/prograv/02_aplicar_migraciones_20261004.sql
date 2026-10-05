-- Aplicacion conjunta de las migraciones de Fabian autorizada el 4 de octubre de 2026.
USE [PrograV];
SET NOCOUNT ON;
SET XACT_ABORT ON;
SET LOCK_TIMEOUT 5000;
IF DB_NAME() <> N'PrograV' OR CONVERT(nvarchar(128), SERVERPROPERTY('ServerName')) <> N'Jose-Morales-C'
    THROW 51510, 'El servidor o la base no corresponden al ambiente revisado.', 1;
IF @@TRANCOUNT <> 0
    THROW 51511, 'La sesion tiene una transaccion pendiente.', 1;
BEGIN TRY
    BEGIN TRANSACTION;
    IF OBJECT_ID(N'matricula.Estudiante', N'U') IS NOT NULL
       OR OBJECT_ID(N'matricula.EstudianteTelefono', N'U') IS NOT NULL
       OR OBJECT_ID(N'matricula.Prematricula', N'U') IS NOT NULL
       OR OBJECT_ID(N'matricula.PrematriculaCurso', N'U') IS NOT NULL
        THROW 51512, 'Las tablas ya existen; no se reemplazan.', 1;
    -- Las tablas referenciadas permanecen estables durante la incorporacion de las relaciones.
    IF EXISTS (SELECT 1 FROM matricula.Matricula WITH (TABLOCKX, HOLDLOCK))
       OR EXISTS (SELECT 1 FROM finanzas.Factura WITH (TABLOCKX, HOLDLOCK))
        THROW 51513, 'Hay matriculas o facturas nuevas sin expediente; se cancela la migracion.', 1;
    -- Fuente conservada: 001_crear_expedientes.sql
    EXEC sys.sp_executesql N'SET XACT_ABORT ON;
SET NOCOUNT ON;

IF OBJECT_ID(N''matricula.Distrito'', N''U'') IS NULL
    THROW 51000, ''Falta el catalogo matricula.Distrito.'', 1;
IF OBJECT_ID(N''matricula.Estudiante'', N''U'') IS NOT NULL
    OR OBJECT_ID(N''matricula.EstudianteTelefono'', N''U'') IS NOT NULL
    THROW 51001, ''Las tablas de expediente ya existen; el script no modifica estructuras existentes.'', 1;

BEGIN TRANSACTION;

-- La identificacion unica fue confirmada por Jose y coincide con su contrato de matricula.
CREATE TABLE matricula.Estudiante (
    Identificacion varchar(30) NOT NULL CONSTRAINT PK_Estudiante PRIMARY KEY,
    TipoIdentificacion nvarchar(max) NOT NULL,
    Email nvarchar(max) NOT NULL,
    NombreCompleto nvarchar(max) NOT NULL,
    FechaNacimiento date NOT NULL,
    DistritoId int NOT NULL,
    OtrasSenas nvarchar(max) NOT NULL,
    CONSTRAINT FK_Estudiante_Distrito FOREIGN KEY (DistritoId)
        REFERENCES matricula.Distrito(DistritoId),
    CONSTRAINT CK_Estudiante_Identificacion CHECK (LEN(LTRIM(RTRIM(Identificacion))) > 0),
    CONSTRAINT CK_Estudiante_Tipo CHECK (LEN(LTRIM(RTRIM(TipoIdentificacion))) > 0),
    CONSTRAINT CK_Estudiante_Email CHECK (LEN(LTRIM(RTRIM(Email))) > 0),
    CONSTRAINT CK_Estudiante_Nombre CHECK (LEN(LTRIM(RTRIM(NombreCompleto))) > 0),
    CONSTRAINT CK_Estudiante_Senas CHECK (LEN(LTRIM(RTRIM(OtrasSenas))) > 0)
);

CREATE TABLE matricula.EstudianteTelefono (
    EstudianteTelefonoId int IDENTITY(1,1) NOT NULL CONSTRAINT PK_EstudianteTelefono PRIMARY KEY,
    IdentificacionEstudiante varchar(30) NOT NULL,
    Telefono nvarchar(max) NOT NULL,
    CONSTRAINT FK_EstudianteTelefono_Estudiante FOREIGN KEY (IdentificacionEstudiante)
        REFERENCES matricula.Estudiante(Identificacion) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT CK_EstudianteTelefono_NoVacio CHECK (LEN(LTRIM(RTRIM(Telefono))) > 0)
);
CREATE INDEX IX_EstudianteTelefono_Estudiante ON matricula.EstudianteTelefono(IdentificacionEstudiante);

COMMIT TRANSACTION;
';
    -- Fuente conservada: 002_relacionar_expedientes.sql
    EXEC sys.sp_executesql N'SET XACT_ABORT ON;
SET NOCOUNT ON;

IF OBJECT_ID(N''matricula.Estudiante'', N''U'') IS NULL
    OR OBJECT_ID(N''matricula.Matricula'', N''U'') IS NULL
    OR OBJECT_ID(N''finanzas.Factura'', N''U'') IS NULL
    THROW 51002, ''Faltan las tablas requeridas para las relaciones de estudiantes.'', 1;

-- Los datos anteriores deben tener expediente real; no se crean estudiantes ficticios.
IF EXISTS (SELECT 1 FROM matricula.Matricula m
    WHERE NOT EXISTS (SELECT 1 FROM matricula.Estudiante e WHERE e.Identificacion = m.IdentificacionEstudiante))
    OR EXISTS (SELECT 1 FROM finanzas.Factura f
    WHERE NOT EXISTS (SELECT 1 FROM matricula.Estudiante e WHERE e.Identificacion = f.IdentificacionEstudiante))
    THROW 51003, ''Existen matriculas o facturas sin expediente. Se requiere resolver sus referencias antes de crear las relaciones.'', 1;

IF OBJECT_ID(N''matricula.FK_Matricula_Estudiante'', N''F'') IS NOT NULL
    OR OBJECT_ID(N''finanzas.FK_Factura_Estudiante'', N''F'') IS NOT NULL
    THROW 51004, ''Las relaciones de estudiantes ya existen; no se reemplazan automaticamente.'', 1;

BEGIN TRANSACTION;

-- Una correccion de identificacion conserva las referencias; la eliminacion no borra matriculas ni facturas.
ALTER TABLE matricula.Matricula WITH CHECK ADD CONSTRAINT FK_Matricula_Estudiante
    FOREIGN KEY (IdentificacionEstudiante) REFERENCES matricula.Estudiante(Identificacion)
    ON UPDATE CASCADE ON DELETE NO ACTION;
ALTER TABLE finanzas.Factura WITH CHECK ADD CONSTRAINT FK_Factura_Estudiante
    FOREIGN KEY (IdentificacionEstudiante) REFERENCES matricula.Estudiante(Identificacion)
    ON UPDATE CASCADE ON DELETE NO ACTION;

COMMIT TRANSACTION;
';
    -- Fuente conservada: 003_crear_prematriculas.sql
    EXEC sys.sp_executesql N'SET XACT_ABORT ON;
SET NOCOUNT ON;

IF OBJECT_ID(N''matricula.Estudiante'', N''U'') IS NULL
    OR OBJECT_ID(N''academico.Carrera'', N''U'') IS NULL
    OR OBJECT_ID(N''academico.Curso'', N''U'') IS NULL
    OR OBJECT_ID(N''academico.Periodo'', N''U'') IS NULL
    THROW 51005, ''Faltan estudiante, carrera, curso o periodo para crear prematriculas.'', 1;
IF OBJECT_ID(N''matricula.Prematricula'', N''U'') IS NOT NULL
    OR OBJECT_ID(N''matricula.PrematriculaCurso'', N''U'') IS NOT NULL
    THROW 51006, ''Las tablas de prematricula ya existen; no se reemplazan automaticamente.'', 1;

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
';
    IF @@TRANCOUNT <> 1
        THROW 51514, 'Nivel de transaccion inesperado.', 1;
    IF (SELECT COUNT(*) FROM sys.tables WHERE object_id IN (
            OBJECT_ID(N'matricula.Estudiante'), OBJECT_ID(N'matricula.EstudianteTelefono'),
            OBJECT_ID(N'matricula.Prematricula'), OBJECT_ID(N'matricula.PrematriculaCurso'))) <> 4
        THROW 51515, 'No se crearon las cuatro tablas esperadas.', 1;
    IF (SELECT COUNT(*) FROM sys.foreign_keys WHERE name IN (
            N'FK_Estudiante_Distrito', N'FK_EstudianteTelefono_Estudiante',
            N'FK_Matricula_Estudiante', N'FK_Factura_Estudiante',
            N'FK_Prematricula_Estudiante', N'FK_Prematricula_Carrera', N'FK_Prematricula_Periodo',
            N'FK_PrematriculaCurso_Prematricula', N'FK_PrematriculaCurso_Curso')
            AND is_disabled = 0 AND is_not_trusted = 0) <> 9
        THROW 51516, 'Las relaciones no quedaron completas y verificadas.', 1;
    COMMIT TRANSACTION;
    SELECT N'MIGRACIONES_001_002_003_APLICADAS' AS Resultado,
           CONVERT(nvarchar(128), SERVERPROPERTY('ServerName')) AS Servidor,
           DB_NAME() AS BaseDatos, ORIGINAL_LOGIN() AS Usuario,
           SYSDATETIMEOFFSET() AS FechaServidor, @@TRANCOUNT AS TransaccionesPendientes;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;