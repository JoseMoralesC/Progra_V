-- Revision de solo lectura anterior a las migraciones autorizadas por Fabian.
USE [PrograV];
SET NOCOUNT ON;
SET LOCK_TIMEOUT 5000;
IF DB_NAME() <> N'PrograV'
    THROW 51500, 'La revision corresponde exclusivamente a PrograV.', 1;

SELECT CONVERT(nvarchar(128), SERVERPROPERTY('ServerName')) AS Servidor,
       DB_NAME() AS BaseDatos, ORIGINAL_LOGIN() AS LoginConexion,
       CONVERT(nvarchar(128), DATABASEPROPERTYEX(DB_NAME(), 'Collation')) AS Intercalacion,
       HAS_PERMS_BY_NAME(DB_NAME(), 'DATABASE', 'CREATE TABLE') AS PuedeCrearTablas,
       HAS_PERMS_BY_NAME('matricula', 'SCHEMA', 'ALTER') AS PuedeAlterarEsquemaMatricula,
       HAS_PERMS_BY_NAME('matricula.Matricula', 'OBJECT', 'ALTER') AS PuedeAlterarMatricula,
       HAS_PERMS_BY_NAME('finanzas.Factura', 'OBJECT', 'ALTER') AS PuedeAlterarFactura,
       HAS_PERMS_BY_NAME('academico.Carrera', 'OBJECT', 'REFERENCES') AS PuedeReferenciarCarrera,
       HAS_PERMS_BY_NAME('academico.Curso', 'OBJECT', 'REFERENCES') AS PuedeReferenciarCurso,
       HAS_PERMS_BY_NAME('academico.Periodo', 'OBJECT', 'REFERENCES') AS PuedeReferenciarPeriodo;

DECLARE @Objetos TABLE (Nombre nvarchar(256) PRIMARY KEY);
INSERT @Objetos VALUES
    (N'matricula.Provincia'), (N'matricula.Canton'), (N'matricula.Distrito'),
    (N'matricula.Estudiante'), (N'matricula.EstudianteTelefono'),
    (N'matricula.Prematricula'), (N'matricula.PrematriculaCurso'),
    (N'matricula.Matricula'), (N'finanzas.Factura'),
    (N'academico.Carrera'), (N'academico.Curso'), (N'academico.Periodo');

SELECT o.Nombre AS Objeto, OBJECT_ID(o.Nombre, N'U') AS IdTabla,
       (SELECT SUM(p.rows) FROM sys.partitions p
        WHERE p.object_id = OBJECT_ID(o.Nombre, N'U') AND p.index_id IN (0, 1)) AS FilasMetadatos
FROM @Objetos o ORDER BY o.Nombre;

SELECT o.Nombre AS Objeto, c.name AS Columna, ty.name AS Tipo, c.max_length AS LongitudBytes,
       c.is_nullable AS AdmiteNull, c.is_identity AS Identidad, c.collation_name AS Intercalacion
FROM @Objetos o JOIN sys.columns c ON c.object_id = OBJECT_ID(o.Nombre, N'U')
JOIN sys.types ty ON ty.user_type_id = c.user_type_id
ORDER BY o.Nombre, c.column_id;

SELECT OBJECT_SCHEMA_NAME(k.parent_object_id) + N'.' + OBJECT_NAME(k.parent_object_id) AS Tabla,
       k.name AS Llave, k.type_desc AS Tipo, c.name AS Columna, ic.key_ordinal AS Orden
FROM sys.key_constraints k JOIN sys.index_columns ic
  ON ic.object_id = k.parent_object_id AND ic.index_id = k.unique_index_id
JOIN sys.columns c ON c.object_id = ic.object_id AND c.column_id = ic.column_id
WHERE k.parent_object_id IN (SELECT OBJECT_ID(Nombre, N'U') FROM @Objetos)
ORDER BY Tabla, Llave, Orden;

SELECT fk.name AS Relacion,
       OBJECT_SCHEMA_NAME(fk.parent_object_id) + N'.' + OBJECT_NAME(fk.parent_object_id) AS Tabla,
       pc.name AS Columna,
       OBJECT_SCHEMA_NAME(fk.referenced_object_id) + N'.' + OBJECT_NAME(fk.referenced_object_id) AS Referencia,
       rc.name AS ColumnaReferenciada, fk.update_referential_action_desc AS AlActualizar,
       fk.delete_referential_action_desc AS AlEliminar, fk.is_disabled AS Deshabilitada,
       fk.is_not_trusted AS SinValidar
FROM sys.foreign_keys fk JOIN sys.foreign_key_columns fc ON fc.constraint_object_id = fk.object_id
JOIN sys.columns pc ON pc.object_id = fc.parent_object_id AND pc.column_id = fc.parent_column_id
JOIN sys.columns rc ON rc.object_id = fc.referenced_object_id AND rc.column_id = fc.referenced_column_id
WHERE fk.parent_object_id IN (SELECT OBJECT_ID(Nombre, N'U') FROM @Objetos)
   OR fk.referenced_object_id IN (SELECT OBJECT_ID(Nombre, N'U') FROM @Objetos)
ORDER BY Tabla, Relacion, fc.constraint_column_id;

SELECT OBJECT_SCHEMA_NAME(tr.parent_id) + N'.' + OBJECT_NAME(tr.parent_id) AS Tabla,
       tr.name AS Disparador, tr.is_disabled AS Deshabilitado
FROM sys.triggers tr WHERE tr.parent_id IN (SELECT OBJECT_ID(Nombre, N'U') FROM @Objetos);

-- Solo cantidades: no se extraen identificaciones ni otros datos personales.
IF OBJECT_ID(N'matricula.Matricula', N'U') IS NOT NULL
BEGIN
    IF OBJECT_ID(N'matricula.Estudiante', N'U') IS NULL
        EXEC(N'SELECT N''Matricula'' AS Origen, COUNT_BIG(*) AS FilasSinExpediente,
                     COUNT(DISTINCT IdentificacionEstudiante) AS IdentificacionesSinExpediente
               FROM matricula.Matricula;');
    ELSE
        EXEC(N'SELECT N''Matricula'' AS Origen, COUNT_BIG(*) AS FilasSinExpediente,
                     COUNT(DISTINCT m.IdentificacionEstudiante) AS IdentificacionesSinExpediente
               FROM matricula.Matricula m WHERE NOT EXISTS
                    (SELECT 1 FROM matricula.Estudiante e WHERE e.Identificacion = m.IdentificacionEstudiante);');
END;
IF OBJECT_ID(N'finanzas.Factura', N'U') IS NOT NULL
BEGIN
    IF OBJECT_ID(N'matricula.Estudiante', N'U') IS NULL
        EXEC(N'SELECT N''Factura'' AS Origen, COUNT_BIG(*) AS FilasSinExpediente,
                     COUNT(DISTINCT IdentificacionEstudiante) AS IdentificacionesSinExpediente
               FROM finanzas.Factura;');
    ELSE
        EXEC(N'SELECT N''Factura'' AS Origen, COUNT_BIG(*) AS FilasSinExpediente,
                     COUNT(DISTINCT f.IdentificacionEstudiante) AS IdentificacionesSinExpediente
               FROM finanzas.Factura f WHERE NOT EXISTS
                    (SELECT 1 FROM matricula.Estudiante e WHERE e.Identificacion = f.IdentificacionEstudiante);');
END;

SELECT N'REVISION_PREVIA_COMPLETA_SIN_CAMBIOS' AS Resultado;
