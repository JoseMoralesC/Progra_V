/*
    Nuevo Avatar - Persona 1 y bitácora (SQL Server)
    Ejecutar en la base elegida antes de 04_datos_iniciales.sql.
    No crea la base ni modifica tablas existentes. Una instalación parcial
    requiere una migración específica, no volver a ejecutar este archivo.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF EXISTS (
        SELECT 1 FROM sys.tables AS t
        JOIN sys.schemas AS s ON s.schema_id = t.schema_id
        WHERE s.name IN (N'seguridad', N'general')
    )
        THROW 50001, 'Seguridad o general ya contiene tablas; use una base vacía para esta parte.', 1;

    IF SCHEMA_ID(N'seguridad') IS NULL EXEC(N'CREATE SCHEMA seguridad');
    IF SCHEMA_ID(N'general') IS NULL EXEC(N'CREATE SCHEMA general');

    /* El rol debe existir antes de crear usuarios. */
    CREATE TABLE seguridad.rol (
        id_rol       varchar(20)  NOT NULL,
        nombre_rol   varchar(100) NOT NULL,
        CONSTRAINT PK_rol PRIMARY KEY CLUSTERED (id_rol),
        CONSTRAINT CK_rol_nombre CHECK (
            nombre_rol NOT LIKE '%[^a-zA-Z ]%'
            AND LTRIM(RTRIM(nombre_rol)) <> ''
        )
    );

    /* La aplicación guarda un hash BCrypt, nunca la contraseña en claro. */
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

    /* Los refresh tokens pueden revocarse sin borrar la cuenta. */
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

    /* Bitácora compartida por todos los servicios. */
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

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
