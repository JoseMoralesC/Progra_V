-- Estructura del servicio Direccion. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Direccion]
GO
CREATE SCHEMA [matricula]
GO
CREATE TABLE [matricula].[Provincia] (
  [ProvinciaId] [int] IDENTITY(1,1) NOT NULL,
  [Nombre] [varchar](100) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [matricula].[Provincia] ADD CONSTRAINT [PK_Provincia] PRIMARY KEY CLUSTERED ([ProvinciaId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[Provincia] ADD CONSTRAINT [UQ_Provincia_Nombre] UNIQUE NONCLUSTERED ([Nombre] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[Provincia] WITH CHECK ADD CONSTRAINT [CK_Provincia_Nombre_NoVacio] CHECK (len(ltrim(rtrim([Nombre])))>(0))
GO
CREATE TABLE [matricula].[Canton] (
  [CantonId] [int] IDENTITY(1,1) NOT NULL,
  [ProvinciaId] [int] NOT NULL,
  [Nombre] [varchar](100) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [matricula].[Canton] ADD CONSTRAINT [PK_Canton] PRIMARY KEY CLUSTERED ([CantonId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[Canton] ADD CONSTRAINT [UQ_Canton_Provincia_Nombre] UNIQUE NONCLUSTERED ([ProvinciaId] ASC,[Nombre] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[Canton] WITH CHECK ADD CONSTRAINT [CK_Canton_Nombre_NoVacio] CHECK (len(ltrim(rtrim([Nombre])))>(0))
GO
CREATE TABLE [matricula].[Distrito] (
  [DistritoId] [int] IDENTITY(1,1) NOT NULL,
  [CantonId] [int] NOT NULL,
  [Nombre] [varchar](100) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [matricula].[Distrito] ADD CONSTRAINT [PK_Distrito] PRIMARY KEY CLUSTERED ([DistritoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[Distrito] ADD CONSTRAINT [UQ_Distrito_Canton_Nombre] UNIQUE NONCLUSTERED ([CantonId] ASC,[Nombre] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[Distrito] WITH CHECK ADD CONSTRAINT [CK_Distrito_Nombre_NoVacio] CHECK (len(ltrim(rtrim([Nombre])))>(0))
GO
ALTER TABLE [matricula].[Canton] WITH CHECK ADD CONSTRAINT [FK_Canton_Provincia] FOREIGN KEY ([ProvinciaId]) REFERENCES [matricula].[Provincia] ([ProvinciaId]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [matricula].[Distrito] WITH CHECK ADD CONSTRAINT [FK_Distrito_Canton] FOREIGN KEY ([CantonId]) REFERENCES [matricula].[Canton] ([CantonId]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
