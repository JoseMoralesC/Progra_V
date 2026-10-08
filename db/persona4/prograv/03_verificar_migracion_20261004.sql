-- Verificacion posterior de solo lectura; no inserta datos de prueba.
USE [PrograV];
SET NOCOUNT ON;
IF CONVERT(nvarchar(128), SERVERPROPERTY('ServerName')) <> N'Jose-Morales-C'
    THROW 51520, 'Servidor distinto del revisado.', 1;
IF @@TRANCOUNT <> 0
    THROW 51521, 'Existe una transaccion pendiente en la sesion.', 1;

SELECT N'matricula.Estudiante' AS Tabla, COUNT_BIG(*) AS Filas FROM matricula.Estudiante
UNION ALL SELECT N'matricula.EstudianteTelefono', COUNT_BIG(*) FROM matricula.EstudianteTelefono
UNION ALL SELECT N'matricula.Prematricula', COUNT_BIG(*) FROM matricula.Prematricula
UNION ALL SELECT N'matricula.PrematriculaCurso', COUNT_BIG(*) FROM matricula.PrematriculaCurso;

SELECT OBJECT_SCHEMA_NAME(i.object_id) + N'.' + OBJECT_NAME(i.object_id) AS Tabla,
       i.name AS Indice, i.is_primary_key AS LlavePrimaria, i.is_disabled AS Deshabilitado
FROM sys.indexes i WHERE i.index_id > 0 AND i.object_id IN (
    OBJECT_ID(N'matricula.Estudiante'), OBJECT_ID(N'matricula.EstudianteTelefono'),
    OBJECT_ID(N'matricula.Prematricula'), OBJECT_ID(N'matricula.PrematriculaCurso'))
ORDER BY Tabla, i.index_id;

SELECT OBJECT_SCHEMA_NAME(c.parent_object_id) + N'.' + OBJECT_NAME(c.parent_object_id) AS Tabla,
       c.name AS Restriccion, c.is_disabled AS Deshabilitada, c.is_not_trusted AS SinValidar
FROM sys.check_constraints c WHERE c.parent_object_id IN (
    OBJECT_ID(N'matricula.Estudiante'), OBJECT_ID(N'matricula.EstudianteTelefono'));

SELECT N'VERIFICACION_POSTERIOR_COMPLETA' AS Resultado;
