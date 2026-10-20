-- Expedientes exclusivos del recorrido de persona 5. No crea matriculas.
-- El catalogo territorial ficticio esta etiquetado como prueba, no como dato oficial.
SET NOCOUNT ON;
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    DECLARE @ProvinciaId int, @CantonId int, @DistritoId int;
    SELECT @ProvinciaId = ProvinciaId FROM matricula.Provincia
    WHERE Nombre = 'Provincia Pruebas Jose';
    IF @ProvinciaId IS NULL
    BEGIN
        INSERT matricula.Provincia (Nombre) VALUES ('Provincia Pruebas Jose');
        SET @ProvinciaId = SCOPE_IDENTITY();
    END;
    SELECT @CantonId = CantonId FROM matricula.Canton
    WHERE ProvinciaId = @ProvinciaId AND Nombre = 'Canton Pruebas Jose';
    IF @CantonId IS NULL
    BEGIN
        INSERT matricula.Canton (ProvinciaId, Nombre)
        VALUES (@ProvinciaId, 'Canton Pruebas Jose');
        SET @CantonId = SCOPE_IDENTITY();
    END;
    SELECT @DistritoId = DistritoId FROM matricula.Distrito
    WHERE CantonId = @CantonId AND Nombre = 'Distrito Pruebas Jose';
    IF @DistritoId IS NULL
    BEGIN
        INSERT matricula.Distrito (CantonId, Nombre)
        VALUES (@CantonId, 'Distrito Pruebas Jose');
        SET @DistritoId = SCOPE_IDENTITY();
    END;
    INSERT matricula.Estudiante
        (Identificacion, TipoIdentificacion, Email, NombreCompleto,
         FechaNacimiento, DistritoId, OtrasSenas)
    SELECT d.Identificacion, 'LOCAL', d.Email, d.Nombre,
           CAST('2000-01-01' AS date), @DistritoId, 'Datos ficticios de prueba persona cinco'
    FROM (VALUES
        ('P5-JOSE-ESTUDIANTE', 'estudiante.persona5@example.com', N'Estudiante Pruebas Jose'),
        ('P5-JOSE-ESTUDIANTE-M', 'estudiante.modificado.persona5@example.com', N'Estudiante Modificacion Jose')
    ) d(Identificacion, Email, Nombre)
    WHERE NOT EXISTS (SELECT 1 FROM matricula.Estudiante e
                      WHERE e.Identificacion = d.Identificacion);
    COMMIT TRANSACTION;
    SELECT Identificacion FROM matricula.Estudiante
    WHERE Identificacion IN ('P5-JOSE-ESTUDIANTE', 'P5-JOSE-ESTUDIANTE-M');
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
