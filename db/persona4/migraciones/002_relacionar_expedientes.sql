SET XACT_ABORT ON;
SET NOCOUNT ON;

IF OBJECT_ID(N'matricula.Estudiante', N'U') IS NULL
    OR OBJECT_ID(N'matricula.Matricula', N'U') IS NULL
    OR OBJECT_ID(N'finanzas.Factura', N'U') IS NULL
    THROW 51002, 'Faltan las tablas requeridas para las relaciones de estudiantes.', 1;

-- Los datos anteriores deben tener expediente real; no se crean estudiantes ficticios.
IF EXISTS (SELECT 1 FROM matricula.Matricula m
    WHERE NOT EXISTS (SELECT 1 FROM matricula.Estudiante e WHERE e.Identificacion = m.IdentificacionEstudiante))
    OR EXISTS (SELECT 1 FROM finanzas.Factura f
    WHERE NOT EXISTS (SELECT 1 FROM matricula.Estudiante e WHERE e.Identificacion = f.IdentificacionEstudiante))
    THROW 51003, 'Existen matriculas o facturas sin expediente. Se requiere resolver sus referencias antes de crear las relaciones.', 1;

IF OBJECT_ID(N'matricula.FK_Matricula_Estudiante', N'F') IS NOT NULL
    OR OBJECT_ID(N'finanzas.FK_Factura_Estudiante', N'F') IS NOT NULL
    THROW 51004, 'Las relaciones de estudiantes ya existen; no se reemplazan automaticamente.', 1;

BEGIN TRANSACTION;

-- Una correccion de identificacion conserva las referencias; la eliminacion no borra matriculas ni facturas.
ALTER TABLE matricula.Matricula WITH CHECK ADD CONSTRAINT FK_Matricula_Estudiante
    FOREIGN KEY (IdentificacionEstudiante) REFERENCES matricula.Estudiante(Identificacion)
    ON UPDATE CASCADE ON DELETE NO ACTION;
ALTER TABLE finanzas.Factura WITH CHECK ADD CONSTRAINT FK_Factura_Estudiante
    FOREIGN KEY (IdentificacionEstudiante) REFERENCES matricula.Estudiante(Identificacion)
    ON UPDATE CASCADE ON DELETE NO ACTION;

COMMIT TRANSACTION;
