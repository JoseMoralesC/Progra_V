-- Estructura del servicio Carrera. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Carrera]
GO
CREATE SCHEMA [academico]
GO
CREATE TABLE [academico].[Carrera] (
  [CarreraId] [int] IDENTITY(1,1) NOT NULL,
  [Nombre] [nvarchar](150) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [InstitucionId] [int] NOT NULL,
  [DirectorProfesorId] [int] NOT NULL
)
GO
ALTER TABLE [academico].[Carrera] ADD CONSTRAINT [PK_Carrera] PRIMARY KEY CLUSTERED ([CarreraId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_Carrera_Director] ON [academico].[Carrera] ([DirectorProfesorId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_Carrera_Institucion] ON [academico].[Carrera] ([InstitucionId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[Carrera] WITH CHECK ADD CONSTRAINT [CK_Carrera_Nombre] CHECK (len(ltrim(rtrim([Nombre])))>(0) AND NOT ([Nombre]) collate Latin1_General_100_BIN2 like N'%[^A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]%')
GO
