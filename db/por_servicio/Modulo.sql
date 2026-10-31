-- Estructura del servicio Modulo. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Modulo]
GO
CREATE SCHEMA [seguridad]
GO
CREATE TABLE [seguridad].[modulo] (
  [id_modulo] [varchar](20) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [nombre_modulo] [varchar](100) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [activo] [bit] NOT NULL CONSTRAINT [DF_modulo_activo] DEFAULT (1),
  [orden] [int] NOT NULL CONSTRAINT [DF_modulo_orden] DEFAULT (0)
)
GO
ALTER TABLE [seguridad].[modulo] ADD CONSTRAINT [PK_modulo] PRIMARY KEY CLUSTERED ([id_modulo] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [seguridad].[modulo] WITH CHECK ADD CONSTRAINT [CK_modulo_nombre] CHECK (NOT [nombre_modulo] like '%[^a-zA-Z ]%' AND ltrim(rtrim([nombre_modulo]))<>'')
GO
ALTER TABLE [seguridad].[modulo] WITH CHECK ADD CONSTRAINT [CK_modulo_orden] CHECK ([orden] >= 0)
GO
CREATE TABLE [seguridad].[rol_modulo] (
  [id_modulo] [varchar](20) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [id_rol] [varchar](20) COLLATE Modern_Spanish_CI_AS NOT NULL,
  CONSTRAINT [PK_rol_modulo] PRIMARY KEY ([id_modulo], [id_rol]),
  CONSTRAINT [FK_rol_modulo_modulo] FOREIGN KEY ([id_modulo]) REFERENCES [seguridad].[modulo]([id_modulo]) ON DELETE CASCADE
)
GO
