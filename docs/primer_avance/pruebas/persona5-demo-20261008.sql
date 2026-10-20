-- Persona 5: fixture exclusivo para la demostracion del 8 de octubre de 2026.
-- Solo prepara referencias y expedientes; no crea matriculas, notas, facturas o pagos.
-- Reutiliza carrera/profesor/periodo vigentes. No modifica el recorrido anterior.
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF DB_NAME() <> N'PrograV'
        THROW 51100, 'Este fixture debe ejecutarse en PrograV.', 1;

    DECLARE @Hoy date = CONVERT(date, GETDATE());
    IF @Hoy <> CONVERT(date, '20261008', 112)
        THROW 51101, 'Este fixture corresponde a la demostracion del 2026-10-08.', 1;

    DECLARE @CarreraId int, @ProfesorId int, @PeriodoId int, @DistritoId int,
            @CursoId int, @GrupoId int, @PeriodoInactivoId int, @GrupoInactivoId int,
            @OtroCursoId int, @RubroOtroGrupoId int, @NumeroPeriodo int;
    DECLARE @Estudiante varchar(30) = 'P5-JOSE-DEMO-20261008';
    DECLARE @EstudianteModificado varchar(30) = 'P5-JOSE-DEMO-20261008-M';
    DECLARE @NombreCurso varchar(150) = 'Curso Demostracion Jose Octubre Ocho';

    SELECT TOP (1) @CarreraId = c.CarreraId, @ProfesorId = g.ProfesorId,
                   @PeriodoId = g.PeriodoId, @OtroCursoId = c.CursoId
    FROM academico.Grupo g
    JOIN academico.Curso c ON c.CursoId = g.CursoId
    JOIN academico.Periodo p ON p.PeriodoId = g.PeriodoId
    WHERE @Hoy BETWEEN p.FechaInicio AND p.FechaFin
      AND c.Nombre <> @NombreCurso
    ORDER BY CASE WHEN g.GrupoId = 1 THEN 0 ELSE 1 END, g.GrupoId;

    SELECT TOP (1) @DistritoId = e.DistritoId
    FROM matricula.Estudiante e
    WHERE e.Identificacion = 'P5-JOSE-ESTUDIANTE';

    IF @CarreraId IS NULL OR @ProfesorId IS NULL OR @PeriodoId IS NULL OR @DistritoId IS NULL
        THROW 51102, 'Faltan las referencias academicas o el distrito del fixture anterior.', 1;

    SELECT @CursoId = CursoId FROM academico.Curso WITH (UPDLOCK, HOLDLOCK)
    WHERE CarreraId = @CarreraId AND Nombre = @NombreCurso;
    IF @CursoId IS NULL
    BEGIN
        INSERT academico.Curso (CarreraId, Nivel, Nombre)
        VALUES (@CarreraId, 1, @NombreCurso);
        SET @CursoId = CONVERT(int, SCOPE_IDENTITY());
    END;

    SELECT @GrupoId = GrupoId FROM academico.Grupo WITH (UPDLOCK, HOLDLOCK)
    WHERE CursoId = @CursoId AND PeriodoId = @PeriodoId AND NumeroGrupo = 1;
    IF @GrupoId IS NULL
    BEGIN
        INSERT academico.Grupo (NumeroGrupo, CursoId, ProfesorId, Horario, Cupo, PeriodoId)
        VALUES (1, @CursoId, @ProfesorId, 'Demostracion Jose ocho de octubre', 30, @PeriodoId);
        SET @GrupoId = CONVERT(int, SCOPE_IDENTITY());
    END;

    IF EXISTS (SELECT 1 FROM matricula.Matricula WHERE GrupoId = @GrupoId)
       OR EXISTS (SELECT 1 FROM matricula.DesgloseRubro WHERE GrupoId = @GrupoId)
        THROW 51103, 'El grupo de hoy ya fue utilizado. No se borra ni se reinicia su historial.', 1;

    INSERT matricula.Estudiante
        (Identificacion, TipoIdentificacion, Email, NombreCompleto,
         FechaNacimiento, DistritoId, OtrasSenas)
    SELECT datos.Identificacion, 'LOCAL', datos.Email, datos.Nombre,
           CONVERT(date, '20000101', 112), @DistritoId,
           N'Expediente ficticio de demostracion persona cinco ocho de octubre'
    FROM (VALUES
        (@Estudiante, N'p5.demo.20261008@cuc.cr', N'Estudiante Demostracion Jose'),
        (@EstudianteModificado, N'p5.demo.20261008.m@cuc.cr', N'Estudiante Modificacion Jose')
    ) datos(Identificacion, Email, Nombre)
    WHERE NOT EXISTS (
        SELECT 1 FROM matricula.Estudiante e WITH (UPDLOCK, HOLDLOCK)
        WHERE e.Identificacion = datos.Identificacion
    );

    -- Periodo pasado, exclusivo para los rechazos 09.2 y 09.3.
    SELECT @PeriodoInactivoId = PeriodoId
    FROM academico.Periodo WITH (UPDLOCK, HOLDLOCK)
    WHERE Anio = YEAR(@Hoy) AND FechaInicio = CONVERT(date, '20260901', 112)
          AND FechaFin = CONVERT(date, '20261007', 112);
    IF @PeriodoInactivoId IS NULL
    BEGIN
        SELECT @NumeroPeriodo = ISNULL(MAX(CONVERT(int, NumeroPeriodo)), 0) + 1
        FROM academico.Periodo WITH (UPDLOCK, HOLDLOCK) WHERE Anio = YEAR(@Hoy);
        IF @NumeroPeriodo > 255
            THROW 51104, 'No queda un numero de periodo disponible para este fixture.', 1;
        INSERT academico.Periodo (Anio, NumeroPeriodo, FechaInicio, FechaFin)
        VALUES (YEAR(@Hoy), @NumeroPeriodo, CONVERT(date, '20260901', 112), CONVERT(date, '20261007', 112));
        SET @PeriodoInactivoId = CONVERT(int, SCOPE_IDENTITY());
    END;

    SELECT @GrupoInactivoId = GrupoId FROM academico.Grupo WITH (UPDLOCK, HOLDLOCK)
    WHERE CursoId = @CursoId AND PeriodoId = @PeriodoInactivoId AND NumeroGrupo = 2;
    IF @GrupoInactivoId IS NULL
    BEGIN
        INSERT academico.Grupo (NumeroGrupo, CursoId, ProfesorId, Horario, Cupo, PeriodoId)
        VALUES (2, @CursoId, @ProfesorId, 'Caso negativo periodo pasado Jose', 30, @PeriodoInactivoId);
        SET @GrupoInactivoId = CONVERT(int, SCOPE_IDENTITY());
    END;

    -- Leer un rubro anterior es suficiente para probar pertenencia a otro grupo.
    SELECT TOP (1) @RubroOtroGrupoId = RubroId
    FROM matricula.DesgloseRubro WHERE GrupoId <> @GrupoId ORDER BY RubroId;
    IF @RubroOtroGrupoId IS NULL
        THROW 51105, 'Falta un rubro de otro grupo para el caso opcional 09.4.', 1;

    IF EXISTS (SELECT 1 FROM academico.Curso WHERE CursoId = 2147483647)
       OR EXISTS (SELECT 1 FROM academico.Grupo WHERE GrupoId = 2147483647)
       OR EXISTS (SELECT 1 FROM matricula.Matricula WHERE MatriculaId = 2147483647)
       OR EXISTS (SELECT 1 FROM matricula.DesgloseRubro WHERE RubroId = 2147483647)
       OR EXISTS (SELECT 1 FROM finanzas.Factura WHERE FacturaId = 2147483647)
       OR EXISTS (SELECT 1 FROM finanzas.Pago WHERE PagoId = 2147483647)
        THROW 51106, 'El identificador elegido para inexistentes ya existe.', 1;

    SELECT @Estudiante AS estudiante, @EstudianteModificado AS estudianteModificado,
           @CursoId AS cursoId, @GrupoId AS grupoId, @PeriodoId AS periodoId,
           @OtroCursoId AS otroCursoId, @PeriodoInactivoId AS periodoInactivoId,
           @GrupoInactivoId AS grupoInactivoId, @RubroOtroGrupoId AS rubroOtroGrupoId,
           2147483647 AS idInexistente;

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
