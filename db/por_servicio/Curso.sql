-- Estructura del servicio Curso. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Curso]
GO
CREATE SCHEMA [academico]
GO
CREATE TABLE [academico].[Curso] (
  [CursoId] [int] IDENTITY(1,1) NOT NULL,
  [CarreraId] [int] NOT NULL,
  [Nivel] [tinyint] NOT NULL,
  [Nombre] [varchar](150) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [academico].[Curso] ADD CONSTRAINT [PK_Curso] PRIMARY KEY CLUSTERED ([CursoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[Curso] WITH CHECK ADD CONSTRAINT [CK_Curso_Nivel] CHECK ([Nivel]>=(1) AND [Nivel]<=(12))
GO
ALTER TABLE [academico].[Curso] WITH CHECK ADD CONSTRAINT [CK_Curso_Nombre_NoVacio] CHECK (len(ltrim(rtrim([Nombre])))>(0))
GO
ALTER TABLE [academico].[Curso] WITH CHECK ADD CONSTRAINT [CK_Curso_Nombre_SoloLetras] CHECK (NOT [Nombre] like '%[^A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]%')
GO
