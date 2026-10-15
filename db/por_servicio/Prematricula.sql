-- Estructura del servicio Prematricula. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Prematricula]
GO
CREATE SCHEMA [matricula]
GO
CREATE TABLE [matricula].[Prematricula] (
  [PrematriculaId] [int] IDENTITY(1,1) NOT NULL,
  [IdentificacionEstudiante] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [CarreraId] [int] NOT NULL,
  [PeriodoId] [int] NOT NULL,
  [Observaciones] [nvarchar](max) COLLATE Modern_Spanish_CI_AS NULL
)
GO
ALTER TABLE [matricula].[Prematricula] ADD CONSTRAINT [PK_Prematricula] PRIMARY KEY CLUSTERED ([PrematriculaId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_Prematricula_Estudiante] ON [matricula].[Prematricula] ([IdentificacionEstudiante] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE TABLE [matricula].[PrematriculaCurso] (
  [PrematriculaCursoId] [int] IDENTITY(1,1) NOT NULL,
  [PrematriculaId] [int] NOT NULL,
  [CursoId] [int] NOT NULL
)
GO
ALTER TABLE [matricula].[PrematriculaCurso] ADD CONSTRAINT [PK_PrematriculaCurso] PRIMARY KEY CLUSTERED ([PrematriculaCursoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_PrematriculaCurso_Prematricula] ON [matricula].[PrematriculaCurso] ([PrematriculaId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[PrematriculaCurso] WITH CHECK ADD CONSTRAINT [FK_PrematriculaCurso_Prematricula] FOREIGN KEY ([PrematriculaId]) REFERENCES [matricula].[Prematricula] ([PrematriculaId]) ON DELETE CASCADE ON UPDATE NO ACTION
GO
