/*
    Nuevo Avatar - catálogo territorial de matrícula (SQL Server)
    Crea las tres tablas presentes en la base compartida. Los nombres de
    provincias, cantones y distritos se cargarán con un catálogo oficial
    aparte; este archivo no inventa datos geográficos.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF EXISTS (
        SELECT 1 FROM sys.tables AS t
        JOIN sys.schemas AS s ON s.schema_id = t.schema_id
        WHERE s.name = N'matricula'
    )
        THROW 50003, 'Matrícula ya contiene tablas; use una base vacía para esta parte.', 1;

    IF SCHEMA_ID(N'matricula') IS NULL EXEC(N'CREATE SCHEMA matricula');

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

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
