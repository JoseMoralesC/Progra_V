-- Estructura del servicio Institucion. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Institucion]
GO
CREATE SCHEMA [academico]
GO
CREATE TABLE [academico].[Institucion] (
  [InstitucionId] [int] IDENTITY(1,1) NOT NULL,
  [Nombre] [nvarchar](150) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [academico].[Institucion] ADD CONSTRAINT [PK_Institucion] PRIMARY KEY CLUSTERED ([InstitucionId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[Institucion] WITH CHECK ADD CONSTRAINT [CK_Institucion_Nombre] CHECK (len(ltrim(rtrim([Nombre])))>(0) AND NOT ([Nombre]) collate Latin1_General_100_BIN2 like N'%[^A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]%')
GO
