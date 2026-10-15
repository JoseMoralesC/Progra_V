-- Estructura del servicio Matricula. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Matricula]
GO
CREATE SCHEMA [matricula]
GO
CREATE TABLE [matricula].[Matricula] (
  [MatriculaId] [int] IDENTITY(1,1) NOT NULL,
  [IdentificacionEstudiante] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [CursoId] [int] NOT NULL,
  [GrupoId] [int] NOT NULL,
  [PeriodoId] [int] NOT NULL,
  [Estado] [varchar](20) COLLATE Modern_Spanish_CI_AS NOT NULL CONSTRAINT [DF_Matricula_Estado] DEFAULT ('ACTIVA'),
  [FechaMatricula] [datetime] NOT NULL CONSTRAINT [DF_Matricula_Fecha] DEFAULT (getdate())
)
GO
ALTER TABLE [matricula].[Matricula] ADD CONSTRAINT [PK_Matricula] PRIMARY KEY CLUSTERED ([MatriculaId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE UNIQUE NONCLUSTERED INDEX [UX_Matricula_Activa] ON [matricula].[Matricula] ([IdentificacionEstudiante] ASC,[CursoId] ASC,[GrupoId] ASC,[PeriodoId] ASC) WHERE ([Estado]='ACTIVA') WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_Matricula_Grupo] ON [matricula].[Matricula] ([CursoId] ASC,[GrupoId] ASC,[PeriodoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[Matricula] WITH CHECK ADD CONSTRAINT [CK_Matricula_Estado] CHECK ([Estado]='ANULADA' OR [Estado]='ACTIVA')
GO
ALTER TABLE [matricula].[Matricula] WITH CHECK ADD CONSTRAINT [CK_Matricula_Estudiante_NoVacio] CHECK (len(ltrim(rtrim([IdentificacionEstudiante])))>(0))
GO
