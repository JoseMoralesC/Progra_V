-- Persona 5: diagnostico de solo lectura. Ejecutar sobre la base elegida.
SET NOCOUNT ON;

SELECT DB_NAME() AS BaseActual, SYSDATETIME() AS FechaServidor;

SELECT requerido.Nombre,
       CASE WHEN OBJECT_ID(requerido.Nombre, 'U') IS NULL
            THEN 'FALTA' ELSE 'EXISTE' END AS Estado
FROM (VALUES
    ('academico.Curso'), ('academico.Grupo'), ('academico.Periodo'),
    ('matricula.Matricula'), ('matricula.DesgloseRubro'),
    ('matricula.NotaRubro'), ('finanzas.Factura'),
    ('finanzas.FacturaDetalle'), ('finanzas.Pago')
) requerido(Nombre);

-- Ejecutar lo siguiente solo si las tablas correspondientes existen.
-- La vigencia del servicio Java depende de la fecha de su computadora.
SELECT c.CursoId, c.Nombre AS Curso, g.GrupoId, p.PeriodoId,
       p.FechaInicio, p.FechaFin,
       CASE WHEN CAST(GETDATE() AS date) BETWEEN p.FechaInicio AND p.FechaFin
            THEN 'VIGENTE' ELSE 'NO VIGENTE' END AS Vigencia,
       (SELECT COUNT(*) FROM matricula.DesgloseRubro r
        WHERE r.GrupoId = g.GrupoId) AS RubrosExistentes,
       (SELECT COUNT(*) FROM matricula.NotaRubro n
        JOIN matricula.DesgloseRubro r ON r.RubroId = n.RubroId
        WHERE r.GrupoId = g.GrupoId) AS NotasExistentes
FROM academico.Grupo g
JOIN academico.Curso c ON c.CursoId = g.CursoId
JOIN academico.Periodo p ON p.PeriodoId = g.PeriodoId
ORDER BY p.FechaInicio DESC, g.GrupoId;

-- ID propuesto para pruebas de "no existe": todas las cantidades deben ser 0.
SELECT 'Curso' AS Entidad, COUNT(*) AS Coincidencias
FROM academico.Curso WHERE CursoId = 2147483647
UNION ALL SELECT 'Grupo', COUNT(*) FROM academico.Grupo WHERE GrupoId = 2147483647
UNION ALL SELECT 'Matricula', COUNT(*) FROM matricula.Matricula WHERE MatriculaId = 2147483647
UNION ALL SELECT 'Rubro', COUNT(*) FROM matricula.DesgloseRubro WHERE RubroId = 2147483647
UNION ALL SELECT 'Factura', COUNT(*) FROM finanzas.Factura WHERE FacturaId = 2147483647
UNION ALL SELECT 'Pago', COUNT(*) FROM finanzas.Pago WHERE PagoId = 2147483647;

-- Datos para el caso opcional de rubro de otro grupo.
SELECT RubroId, GrupoId, Nombre, Porcentaje
FROM matricula.DesgloseRubro
ORDER BY GrupoId, RubroId;
