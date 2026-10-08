-- Esquema exclusivo de H2 para verificar consultas y transacciones del backend.
CREATE SCHEMA IF NOT EXISTS matricula;
CREATE SCHEMA IF NOT EXISTS finanzas;
CREATE TABLE IF NOT EXISTS matricula.Provincia (ProvinciaId int PRIMARY KEY, Nombre varchar(100));
CREATE TABLE IF NOT EXISTS matricula.Canton (CantonId int PRIMARY KEY, ProvinciaId int REFERENCES matricula.Provincia(ProvinciaId), Nombre varchar(100));
CREATE TABLE IF NOT EXISTS matricula.Distrito (DistritoId int PRIMARY KEY, CantonId int REFERENCES matricula.Canton(CantonId), Nombre varchar(100));
CREATE TABLE IF NOT EXISTS matricula.Estudiante (
    Identificacion varchar(30) PRIMARY KEY,
    TipoIdentificacion nvarchar(max) NOT NULL,
    Email nvarchar(max) NOT NULL,
    NombreCompleto nvarchar(max) NOT NULL,
    FechaNacimiento date NOT NULL,
    DistritoId int NOT NULL REFERENCES matricula.Distrito(DistritoId),
    OtrasSenas nvarchar(max) NOT NULL
);
CREATE TABLE IF NOT EXISTS matricula.EstudianteTelefono (
    EstudianteTelefonoId int IDENTITY PRIMARY KEY,
    IdentificacionEstudiante varchar(30) NOT NULL REFERENCES matricula.Estudiante(Identificacion) ON UPDATE CASCADE ON DELETE CASCADE,
    Telefono nvarchar(max) NOT NULL CHECK (LENGTH(TRIM(Telefono)) > 0)
);
CREATE TABLE IF NOT EXISTS matricula.Matricula (
    MatriculaId int PRIMARY KEY,
    CursoId int, GrupoId int, PeriodoId int, Estado varchar(20),
    IdentificacionEstudiante varchar(30) NOT NULL REFERENCES matricula.Estudiante(Identificacion) ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS finanzas.Factura (
    FacturaId int PRIMARY KEY,
    MatriculaId int NOT NULL REFERENCES matricula.Matricula(MatriculaId),
    IdentificacionEstudiante varchar(30) NOT NULL REFERENCES matricula.Estudiante(Identificacion) ON UPDATE CASCADE
);
CREATE SCHEMA IF NOT EXISTS academico;
CREATE TABLE IF NOT EXISTS academico.Carrera (CarreraId int PRIMARY KEY, Nombre nvarchar(150) NOT NULL);
CREATE TABLE IF NOT EXISTS academico.Curso (
    CursoId int PRIMARY KEY, CarreraId int NOT NULL REFERENCES academico.Carrera(CarreraId),
    Nivel tinyint NOT NULL, Nombre nvarchar(150) NOT NULL
);
CREATE TABLE IF NOT EXISTS academico.Periodo (
    PeriodoId int PRIMARY KEY, FechaInicio date NOT NULL, FechaFin date NOT NULL
);
CREATE TABLE IF NOT EXISTS matricula.Prematricula (
    PrematriculaId int IDENTITY PRIMARY KEY,
    IdentificacionEstudiante varchar(30) NOT NULL REFERENCES matricula.Estudiante(Identificacion) ON UPDATE CASCADE,
    CarreraId int NOT NULL REFERENCES academico.Carrera(CarreraId),
    PeriodoId int NOT NULL REFERENCES academico.Periodo(PeriodoId),
    Observaciones nvarchar(max)
);
CREATE TABLE IF NOT EXISTS matricula.PrematriculaCurso (
    PrematriculaCursoId int IDENTITY PRIMARY KEY,
    PrematriculaId int NOT NULL REFERENCES matricula.Prematricula(PrematriculaId) ON DELETE CASCADE,
    CursoId int NOT NULL REFERENCES academico.Curso(CursoId)
);
CREATE TABLE IF NOT EXISTS academico.Grupo (
    GrupoId int PRIMARY KEY, NumeroGrupo int NOT NULL,
    CursoId int NOT NULL REFERENCES academico.Curso(CursoId),
    PeriodoId int NOT NULL REFERENCES academico.Periodo(PeriodoId)
);
