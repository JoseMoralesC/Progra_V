-- Estructura del servicio Parametro. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Parametro]
GO
CREATE SCHEMA [seguridad]
GO
CREATE TABLE [seguridad].[parametro] (
  [id_parametro] [varchar](10) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [valor] [varchar](500) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [seguridad].[parametro] ADD CONSTRAINT [PK_parametro] PRIMARY KEY CLUSTERED ([id_parametro] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [seguridad].[parametro] WITH CHECK ADD CONSTRAINT [CK_parametro_id_mayus] CHECK (NOT [id_parametro] like '%[^A-Z]%')
GO
ALTER TABLE [seguridad].[parametro] WITH CHECK ADD CONSTRAINT [CK_parametro_valor] CHECK (ltrim(rtrim([valor]))<>'')
GO
