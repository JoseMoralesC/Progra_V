-- Estructura del servicio Grupo. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Grupo]
GO
CREATE SCHEMA [academico]
GO
CREATE TABLE [academico].[Grupo] (
  [GrupoId] [int] IDENTITY(1,1) NOT NULL,
  [NumeroGrupo] [int] NOT NULL,
  [CursoId] [int] NOT NULL,
  [ProfesorId] [int] NOT NULL,
  [Horario] [varchar](100) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [Cupo] [int] NOT NULL,
  [PeriodoId] [int] NOT NULL
)
GO
ALTER TABLE [academico].[Grupo] ADD CONSTRAINT [PK_Grupo] PRIMARY KEY CLUSTERED ([GrupoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[Grupo] WITH CHECK ADD CONSTRAINT [CK_Grupo_Cupo] CHECK ([Cupo]>(0))
GO
ALTER TABLE [academico].[Grupo] WITH CHECK ADD CONSTRAINT [CK_Grupo_Horario_NoVacio] CHECK (len(ltrim(rtrim([Horario])))>(0))
GO
ALTER TABLE [academico].[Grupo] WITH CHECK ADD CONSTRAINT [CK_Grupo_Numero] CHECK ([NumeroGrupo]>(0))
GO
