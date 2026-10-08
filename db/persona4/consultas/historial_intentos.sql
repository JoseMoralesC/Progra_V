-- Consulta utilizada por ACA1; las notas se consumen mediante MAT5.
DECLARE @Identificacion varchar(30) = 'IDENTIFICACION_A_CONSULTAR';
SELECT m.MatriculaId, m.CursoId, m.GrupoId, c.Nombre
FROM matricula.Matricula m
JOIN academico.Curso c ON c.CursoId = m.CursoId
WHERE m.IdentificacionEstudiante = @Identificacion AND m.Estado = 'ACTIVA'
ORDER BY m.PeriodoId, m.MatriculaId;
