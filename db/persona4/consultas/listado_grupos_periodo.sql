DECLARE @PeriodoId int = 20;

SELECT g.GrupoId, g.NumeroGrupo, g.CursoId, c.Nombre AS Curso, ca.Nombre AS Carrera
FROM academico.Grupo g
JOIN academico.Curso c ON c.CursoId = g.CursoId
JOIN academico.Carrera ca ON ca.CarreraId = c.CarreraId
WHERE g.PeriodoId = @PeriodoId
ORDER BY g.GrupoId;
