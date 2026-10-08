-- Oferta y estudiantes ficticios para las pruebas de MAT1.
INSERT INTO matricula.Estudiante VALUES ('123', 'Cedula', 'ana@cuc.cr', 'Ana Maria', '2012-05-10', 3, 'Casa azul');
INSERT INTO matricula.Estudiante VALUES ('456', 'Cedula', 'maria@cuc.cr', 'Maria Mora', '2000-01-01', 3, 'Casa verde');
INSERT INTO matricula.EstudianteTelefono (IdentificacionEstudiante, Telefono) VALUES ('123', '111'), ('456', '222');
INSERT INTO academico.Carrera VALUES (1, 'Carrera de prueba'), (2, 'Segunda carrera');
INSERT INTO academico.Curso VALUES (10, 1, 1, 'Curso primero'), (11, 1, 1, 'Curso segundo'),
    (12, 1, 2, 'Curso avanzado'), (13, 2, 1, 'Otro curso');
INSERT INTO academico.Periodo VALUES (20, '2026-10-04', '2027-01-31'),
    (21, '2026-10-03', '2027-01-31'), (22, '2026-09-01', '2026-12-31');
