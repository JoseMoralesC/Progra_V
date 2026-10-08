/*
    Nuevo Avatar - usuario local para pruebas de login (SQL Server)
    Ejecutar despues de 04_datos_iniciales.sql en la base usada por Seguridad.

    El endpoint /login acepta:
      usuario: jose
      contrasena: 3500192Mc#

    Internamente se guarda como correo porque seguridad.usuario.email es la
    llave primaria y tiene validacion de formato.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF OBJECT_ID(N'seguridad.usuario', N'U') IS NULL
       OR OBJECT_ID(N'seguridad.rol', N'U') IS NULL
        THROW 50005, 'Primero ejecute los scripts de seguridad y datos iniciales.', 1;

    IF NOT EXISTS (
        SELECT 1 FROM seguridad.rol WHERE id_rol = 'ADMIN'
    )
    BEGIN
        INSERT INTO seguridad.rol (id_rol, nombre_rol)
        VALUES ('ADMIN', 'Administrador');
    END;

    IF EXISTS (
        SELECT 1 FROM seguridad.usuario WHERE email = 'jose@cuc.cr'
    )
    BEGIN
        UPDATE seguridad.usuario
           SET tipo_identificacion = 'LOCAL',
               identificacion = 'jose',
               nombre = 'Jose',
               id_rol = 'ADMIN',
               password_hash = '$2a$10$yuxAVQKZ337l.OZZMTaCpunVs8aDgWihN5eieeW7rCxLNkHE81UZG'
         WHERE email = 'jose@cuc.cr';
    END
    ELSE
    BEGIN
        INSERT INTO seguridad.usuario (
            email,
            tipo_identificacion,
            identificacion,
            nombre,
            id_rol,
            password_hash
        )
        VALUES (
            'jose@cuc.cr',
            'LOCAL',
            'jose',
            'Jose',
            'ADMIN',
            '$2a$10$yuxAVQKZ337l.OZZMTaCpunVs8aDgWihN5eieeW7rCxLNkHE81UZG'
        );
    END;

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
