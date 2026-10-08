-- Reinicio de datos ficticios entre casos; este archivo solo se usa con H2 en memoria.
DELETE FROM matricula.PrematriculaCurso;
DELETE FROM matricula.Prematricula;
DELETE FROM academico.Grupo;
DELETE FROM academico.Curso;
DELETE FROM academico.Carrera;
DELETE FROM academico.Periodo;
DELETE FROM finanzas.Factura;
DELETE FROM matricula.Matricula;
DELETE FROM matricula.EstudianteTelefono;
DELETE FROM matricula.Estudiante;
DELETE FROM matricula.Distrito;
DELETE FROM matricula.Canton;
DELETE FROM matricula.Provincia;
INSERT INTO matricula.Provincia VALUES (1, 'Provincia de prueba');
INSERT INTO matricula.Canton VALUES (2, 1, 'Canton de prueba');
INSERT INTO matricula.Distrito VALUES (3, 2, 'Distrito de prueba');
