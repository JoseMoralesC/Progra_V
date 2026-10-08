-- Datos academicos exclusivos para el recorrido de Jose; no crea matriculas.
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;
    DECLARE @InstitucionId int, @ProfesorId int, @CarreraId int,
            @CursoId int, @PeriodoId int, @GrupoId int;
    DECLARE @Hoy date = CAST(GETDATE() AS date);

    SELECT @InstitucionId = InstitucionId FROM academico.Institucion
    WHERE Nombre = N'Institucion Pruebas Jose';
    IF @InstitucionId IS NULL
    BEGIN
        INSERT academico.Institucion (Nombre) VALUES (N'Institucion Pruebas Jose');
        SET @InstitucionId = SCOPE_IDENTITY();
    END;

    SELECT @ProfesorId = ProfesorId FROM academico.Profesor
    WHERE Email = 'profesor.persona5@example.com';
    IF @ProfesorId IS NULL
    BEGIN
        INSERT academico.Profesor
            (TipoIdentificacion, Identificacion, Email, NombreCompleto, FechaNacimiento)
        VALUES ('LOCAL', 'P5-JOSE-PROFESOR', 'profesor.persona5@example.com',
                N'Profesor Pruebas Jose', '1990-01-01');
        SET @ProfesorId = SCOPE_IDENTITY();
    END;

    SELECT @CarreraId = CarreraId FROM academico.Carrera
    WHERE InstitucionId = @InstitucionId AND Nombre = N'Carrera Pruebas Jose';
    IF @CarreraId IS NULL
    BEGIN
        INSERT academico.Carrera (Nombre, InstitucionId, DirectorProfesorId)
        VALUES (N'Carrera Pruebas Jose', @InstitucionId, @ProfesorId);
        SET @CarreraId = SCOPE_IDENTITY();
    END;

    SELECT @CursoId = CursoId FROM academico.Curso
    WHERE CarreraId = @CarreraId AND Nombre = 'Curso Pruebas Jose';
    IF @CursoId IS NULL
    BEGIN
        INSERT academico.Curso (CarreraId, Nivel, Nombre)
        VALUES (@CarreraId, 1, 'Curso Pruebas Jose');
        SET @CursoId = SCOPE_IDENTITY();
    END;

    SELECT @PeriodoId = PeriodoId FROM academico.Periodo
    WHERE Anio = YEAR(@Hoy) AND NumeroPeriodo = 1;
    IF @PeriodoId IS NULL
    BEGIN
        INSERT academico.Periodo (Anio, NumeroPeriodo, FechaInicio, FechaFin)
        VALUES (YEAR(@Hoy), 1, DATEFROMPARTS(YEAR(@Hoy), 1, 1),
                DATEFROMPARTS(YEAR(@Hoy), 12, 31));
        SET @PeriodoId = SCOPE_IDENTITY();
    END;
    IF NOT EXISTS (SELECT 1 FROM academico.Periodo WHERE PeriodoId = @PeriodoId
                   AND @Hoy BETWEEN FechaInicio AND FechaFin)
        THROW 51010, 'El periodo existente no esta vigente. No se modificaron sus fechas.', 1;

    SELECT @GrupoId = GrupoId FROM academico.Grupo
    WHERE CursoId = @CursoId AND PeriodoId = @PeriodoId AND NumeroGrupo = 1;
    IF @GrupoId IS NULL
    BEGIN
        INSERT academico.Grupo
            (NumeroGrupo, CursoId, ProfesorId, Horario, Cupo, PeriodoId)
        VALUES (1, @CursoId, @ProfesorId, 'Lunes 18:00 a 20:00', 30, @PeriodoId);
        SET @GrupoId = SCOPE_IDENTITY();
    END;
    IF EXISTS (SELECT 1 FROM matricula.NotaRubro n
               JOIN matricula.DesgloseRubro r ON r.RubroId = n.RubroId
               WHERE r.GrupoId = @GrupoId)
        THROW 51011, 'Este grupo de pruebas ya tiene notas; use otro grupo para repetir el recorrido.', 1;

    COMMIT TRANSACTION;
    SELECT @CursoId AS cursoId, @GrupoId AS grupoId, @PeriodoId AS periodoId;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
