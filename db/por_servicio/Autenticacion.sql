-- Estructura del servicio Autenticacion. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Autenticacion]
GO
CREATE SCHEMA [seguridad]
GO
CREATE TABLE [seguridad].[refresh_token] (
  [id_token] [int] IDENTITY(1,1) NOT NULL,
  [email_usuario] [varchar](150) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [refresh_token] [varchar](500) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [fecha_creacion] [datetime] NOT NULL CONSTRAINT [DF__refresh_t__fecha__0B91BA14] DEFAULT (getdate()),
  [fecha_expiracion] [datetime] NOT NULL,
  [revocado] [bit] NOT NULL CONSTRAINT [DF__refresh_t__revoc__0C85DE4D] DEFAULT ((0))
)
GO
ALTER TABLE [seguridad].[refresh_token] ADD CONSTRAINT [PK_refresh_token] PRIMARY KEY CLUSTERED ([id_token] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_refresh_token_valor] ON [seguridad].[refresh_token] ([refresh_token] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
