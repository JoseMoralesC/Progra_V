-- Estructura del servicio Usuario. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Usuario]
GO
CREATE SCHEMA [seguridad]
GO
CREATE TABLE [seguridad].[usuario] (
  [email] [varchar](150) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [tipo_identificacion] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [identificacion] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [nombre] [varchar](150) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [id_rol] [varchar](20) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [password_hash] [varchar](255) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [seguridad].[usuario] ADD CONSTRAINT [PK_usuario] PRIMARY KEY CLUSTERED ([email] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [seguridad].[usuario] WITH CHECK ADD CONSTRAINT [CK_usuario_email_formato] CHECK ([email] like '%_@__%.__%')
GO
ALTER TABLE [seguridad].[usuario] WITH CHECK ADD CONSTRAINT [CK_usuario_nombre] CHECK (ltrim(rtrim([nombre]))<>'')
GO
