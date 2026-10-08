DECLARE @Identificacion varchar(30) = '00123';

SELECT TipoIdentificacion, Identificacion, NombreCompleto
FROM matricula.Estudiante
WHERE Identificacion = @Identificacion;
