-- Estructura del servicio Rol. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Rol]
GO
CREATE SCHEMA [seguridad]
GO
CREATE TABLE [seguridad].[rol] (
  [id_rol] [varchar](20) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [nombre_rol] [varchar](100) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [seguridad].[rol] ADD CONSTRAINT [PK_rol] PRIMARY KEY CLUSTERED ([id_rol] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [seguridad].[rol] WITH CHECK ADD CONSTRAINT [CK_rol_nombre] CHECK (NOT [nombre_rol] like '%[^a-zA-Z ]%' AND ltrim(rtrim([nombre_rol]))<>'')
GO
