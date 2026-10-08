-- Intentos ficticios; las notas se reciben del servicio MAT5 simulado.
INSERT INTO academico.Grupo VALUES (200, 1, 10, 20), (201, 2, 10, 21), (202, 1, 11, 21);
INSERT INTO matricula.Matricula (MatriculaId, IdentificacionEstudiante, CursoId, GrupoId, PeriodoId, Estado)
VALUES (1, '123', 10, 200, 20, 'ACTIVA'), (2, '123', 10, 201, 21, 'ACTIVA'),
       (3, '123', 11, 202, 21, 'ACTIVA'), (4, '456', 10, 200, 20, 'ACTIVA'),
       (5, '123', 12, 200, 20, 'ANULADA');
