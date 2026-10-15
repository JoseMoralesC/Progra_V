-- Estructura del servicio Notas. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Notas]
GO
CREATE SCHEMA [matricula]
GO
CREATE TABLE [matricula].[DesgloseRubro] (
  [RubroId] [int] IDENTITY(1,1) NOT NULL,
  [GrupoId] [int] NOT NULL,
  [Nombre] [varchar](100) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [Porcentaje] [decimal](5,2) NOT NULL,
  [FechaCreacion] [datetime] NOT NULL CONSTRAINT [DF_DesgloseRubro_Fecha] DEFAULT (getdate())
)
GO
ALTER TABLE [matricula].[DesgloseRubro] ADD CONSTRAINT [PK_DesgloseRubro] PRIMARY KEY CLUSTERED ([RubroId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[DesgloseRubro] ADD CONSTRAINT [UQ_DesgloseRubro_Grupo_Nombre] UNIQUE NONCLUSTERED ([GrupoId] ASC,[Nombre] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_DesgloseRubro_Grupo] ON [matricula].[DesgloseRubro] ([GrupoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[DesgloseRubro] WITH CHECK ADD CONSTRAINT [CK_DesgloseRubro_Nombre_NoVacio] CHECK (len(ltrim(rtrim([Nombre])))>(0))
GO
ALTER TABLE [matricula].[DesgloseRubro] WITH CHECK ADD CONSTRAINT [CK_DesgloseRubro_Porcentaje] CHECK ([Porcentaje]>(0) AND [Porcentaje]<=(100))
GO
CREATE TABLE [matricula].[NotaRubro] (
  [NotaRubroId] [int] IDENTITY(1,1) NOT NULL,
  [MatriculaId] [int] NOT NULL,
  [RubroId] [int] NOT NULL,
  [Nota] [decimal](5,2) NOT NULL,
  [FechaRegistro] [datetime] NOT NULL CONSTRAINT [DF_NotaRubro_Fecha] DEFAULT (getdate())
)
GO
ALTER TABLE [matricula].[NotaRubro] ADD CONSTRAINT [PK_NotaRubro] PRIMARY KEY CLUSTERED ([NotaRubroId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[NotaRubro] ADD CONSTRAINT [UQ_NotaRubro_Matricula_Rubro] UNIQUE NONCLUSTERED ([MatriculaId] ASC,[RubroId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_NotaRubro_Matricula] ON [matricula].[NotaRubro] ([MatriculaId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[NotaRubro] WITH CHECK ADD CONSTRAINT [CK_NotaRubro_Nota] CHECK ([Nota]>=(1) AND [Nota]<=(100))
GO
ALTER TABLE [matricula].[NotaRubro] WITH CHECK ADD CONSTRAINT [FK_NotaRubro_Rubro] FOREIGN KEY ([RubroId]) REFERENCES [matricula].[DesgloseRubro] ([RubroId]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
