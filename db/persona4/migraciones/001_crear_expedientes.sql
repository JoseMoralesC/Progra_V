SET XACT_ABORT ON;
SET NOCOUNT ON;

IF OBJECT_ID(N'matricula.Distrito', N'U') IS NULL
    THROW 51000, 'Falta el catalogo matricula.Distrito.', 1;
IF OBJECT_ID(N'matricula.Estudiante', N'U') IS NOT NULL
    OR OBJECT_ID(N'matricula.EstudianteTelefono', N'U') IS NOT NULL
    THROW 51001, 'Las tablas de expediente ya existen; el script no modifica estructuras existentes.', 1;

BEGIN TRANSACTION;

-- La identificacion unica fue confirmada por Jose y coincide con su contrato de matricula.
CREATE TABLE matricula.Estudiante (
    Identificacion varchar(30) NOT NULL CONSTRAINT PK_Estudiante PRIMARY KEY,
    TipoIdentificacion nvarchar(max) NOT NULL,
    Email nvarchar(max) NOT NULL,
    NombreCompleto nvarchar(max) NOT NULL,
    FechaNacimiento date NOT NULL,
    DistritoId int NOT NULL,
    OtrasSenas nvarchar(max) NOT NULL,
    CONSTRAINT FK_Estudiante_Distrito FOREIGN KEY (DistritoId)
        REFERENCES matricula.Distrito(DistritoId),
    CONSTRAINT CK_Estudiante_Identificacion CHECK (LEN(LTRIM(RTRIM(Identificacion))) > 0),
    CONSTRAINT CK_Estudiante_Tipo CHECK (LEN(LTRIM(RTRIM(TipoIdentificacion))) > 0),
    CONSTRAINT CK_Estudiante_Email CHECK (LEN(LTRIM(RTRIM(Email))) > 0),
    CONSTRAINT CK_Estudiante_Nombre CHECK (LEN(LTRIM(RTRIM(NombreCompleto))) > 0),
    CONSTRAINT CK_Estudiante_Senas CHECK (LEN(LTRIM(RTRIM(OtrasSenas))) > 0)
);

CREATE TABLE matricula.EstudianteTelefono (
    EstudianteTelefonoId int IDENTITY(1,1) NOT NULL CONSTRAINT PK_EstudianteTelefono PRIMARY KEY,
    IdentificacionEstudiante varchar(30) NOT NULL,
    Telefono nvarchar(max) NOT NULL,
    CONSTRAINT FK_EstudianteTelefono_Estudiante FOREIGN KEY (IdentificacionEstudiante)
        REFERENCES matricula.Estudiante(Identificacion) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT CK_EstudianteTelefono_NoVacio CHECK (LEN(LTRIM(RTRIM(Telefono))) > 0)
);
CREATE INDEX IX_EstudianteTelefono_Estudiante ON matricula.EstudianteTelefono(IdentificacionEstudiante);

COMMIT TRANSACTION;
