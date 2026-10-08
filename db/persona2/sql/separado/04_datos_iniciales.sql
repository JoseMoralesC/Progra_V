/*
    Nuevo Avatar - datos mínimos de arranque (SQL Server)
    Ejecuto esta parte después de 01_seguridad_general.sql. Puedo repetirla:
    inserto solo las claves faltantes y conservo los valores ya configurados.

    No creo usuarios, contraseñas, tokens, bitácoras ni configuración SMTP.
    Defino DOMESTUD y DOMPROF con los dominios usados por el proyecto actual;
    uso minutos para EXPJWT y EXPREFRESH, según AuthService de Seguridad.
*/
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF OBJECT_ID(N'seguridad.rol', N'U') IS NULL
       OR OBJECT_ID(N'seguridad.modulo', N'U') IS NULL
       OR OBJECT_ID(N'seguridad.parametro', N'U') IS NULL
        THROW 50004, 'Primero ejecute 01_seguridad_general.sql.', 1;

    INSERT INTO seguridad.rol (id_rol, nombre_rol)
    SELECT v.id_rol, v.nombre_rol
    FROM (VALUES
        ('ADMIN', 'Administrador'),
        ('ESTUDIANTE', 'Estudiante'),
        ('PROFESOR', 'Profesor')
    ) AS v(id_rol, nombre_rol)
    WHERE NOT EXISTS (
        SELECT 1 FROM seguridad.rol AS r WHERE r.id_rol = v.id_rol
    );

    INSERT INTO seguridad.modulo (id_modulo, nombre_modulo)
    SELECT v.id_modulo, v.nombre_modulo
    FROM (VALUES
        ('ACADEMICO', 'Academico'),
        ('FINANZAS', 'Finanzas'),
        ('MATRICULA', 'Matricula'),
        ('SEGURIDAD', 'Seguridad')
    ) AS v(id_modulo, nombre_modulo)
    WHERE NOT EXISTS (
        SELECT 1 FROM seguridad.modulo AS m WHERE m.id_modulo = v.id_modulo
    );

    INSERT INTO seguridad.parametro (id_parametro, valor)
    SELECT v.id_parametro, v.valor
    FROM (VALUES
        ('DOMESTUD', 'cuc.cr'),
        ('DOMPROF', 'cuc.ac.cr'),
        ('EXPJWT', '5'),
        ('EXPREFRESH', '60')
    ) AS v(id_parametro, valor)
    WHERE NOT EXISTS (
        SELECT 1 FROM seguridad.parametro AS p
        WHERE p.id_parametro = v.id_parametro
    );

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
