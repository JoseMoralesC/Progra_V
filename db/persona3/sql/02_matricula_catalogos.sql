/*
    Nuevo Avatar - Persona 3: catálogo territorial
    Historia:
        MAT4 - Administración de direcciones

    Este script documenta la estructura utilizada por:
        /provincias
        /cantones
        /distritos

   
*/

SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF SCHEMA_ID(N'matricula') IS NULL
        EXEC(N'CREATE SCHEMA matricula');

    

    IF OBJECT_ID(N'matricula.Provincia', N'U') IS NULL
    BEGIN
        CREATE TABLE matricula.Provincia (
            ProvinciaId int          IDENTITY(1,1) NOT NULL,
            Nombre      varchar(100) NOT NULL,

            CONSTRAINT PK_Provincia
                PRIMARY KEY CLUSTERED (ProvinciaId),

            CONSTRAINT UQ_Provincia_Nombre
                UNIQUE (Nombre),

            CONSTRAINT CK_Provincia_Nombre_NoVacio
                CHECK (LEN(LTRIM(RTRIM(Nombre))) > 0)
        );
    END;

 

    IF OBJECT_ID(N'matricula.Canton', N'U') IS NULL
    BEGIN
        IF OBJECT_ID(N'matricula.Provincia', N'U') IS NULL
            THROW 51011, 'Falta matricula.Provincia requerida por matricula.Canton.', 1;

        CREATE TABLE matricula.Canton (
            CantonId    int          IDENTITY(1,1) NOT NULL,
            ProvinciaId int          NOT NULL,
            Nombre      varchar(100) NOT NULL,

            CONSTRAINT PK_Canton
                PRIMARY KEY CLUSTERED (CantonId),

            CONSTRAINT UQ_Canton_Provincia_Nombre
                UNIQUE (ProvinciaId, Nombre),

            CONSTRAINT FK_Canton_Provincia
                FOREIGN KEY (ProvinciaId)
                REFERENCES matricula.Provincia (ProvinciaId),

            CONSTRAINT CK_Canton_Nombre_NoVacio
                CHECK (LEN(LTRIM(RTRIM(Nombre))) > 0)
        );
    END;

   

    IF OBJECT_ID(N'matricula.Distrito', N'U') IS NULL
    BEGIN
        IF OBJECT_ID(N'matricula.Canton', N'U') IS NULL
            THROW 51012, 'Falta matricula.Canton requerida por matricula.Distrito.', 1;

        CREATE TABLE matricula.Distrito (
            DistritoId int          IDENTITY(1,1) NOT NULL,
            CantonId   int          NOT NULL,
            Nombre     varchar(100) NOT NULL,

            CONSTRAINT PK_Distrito
                PRIMARY KEY CLUSTERED (DistritoId),

            CONSTRAINT UQ_Distrito_Canton_Nombre
                UNIQUE (CantonId, Nombre),

            CONSTRAINT FK_Distrito_Canton
                FOREIGN KEY (CantonId)
                REFERENCES matricula.Canton (CantonId),

            CONSTRAINT CK_Distrito_Nombre_NoVacio
                CHECK (LEN(LTRIM(RTRIM(Nombre))) > 0)
        );
    END;

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;

    THROW;
END CATCH;