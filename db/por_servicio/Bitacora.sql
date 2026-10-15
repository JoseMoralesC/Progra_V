-- Estructura del servicio Bitacora. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Bitacora]
GO
CREATE SCHEMA [general]
GO
CREATE TABLE [general].[bitacora] (
  [id_bitacora] [bigint] IDENTITY(1,1) NOT NULL,
  [usuario] [varchar](150) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [descripcion] [varchar](max) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [fecha_hora] [datetime] NOT NULL CONSTRAINT [DF__bitacora__fecha___0A9D95DB] DEFAULT (getdate())
)
GO
ALTER TABLE [general].[bitacora] ADD CONSTRAINT [PK_bitacora] PRIMARY KEY CLUSTERED ([id_bitacora] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_bitacora_fecha] ON [general].[bitacora] ([fecha_hora] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_bitacora_usuario] ON [general].[bitacora] ([usuario] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [general].[bitacora] WITH CHECK ADD CONSTRAINT [CK_bitacora_descripcion] CHECK (ltrim(rtrim([descripcion]))<>'')
GO
ALTER TABLE [general].[bitacora] WITH CHECK ADD CONSTRAINT [CK_bitacora_usuario] CHECK (ltrim(rtrim([usuario]))<>'')
GO
