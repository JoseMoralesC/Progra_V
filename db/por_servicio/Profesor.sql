-- Estructura del servicio Profesor. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Profesor]
GO
CREATE SCHEMA [academico]
GO
CREATE TABLE [academico].[Profesor] (
  [ProfesorId] [int] IDENTITY(1,1) NOT NULL,
  [TipoIdentificacion] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [Identificacion] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [Email] [varchar](150) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [NombreCompleto] [nvarchar](150) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [FechaNacimiento] [date] NOT NULL
)
GO
ALTER TABLE [academico].[Profesor] ADD CONSTRAINT [PK_Profesor] PRIMARY KEY CLUSTERED ([ProfesorId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[Profesor] ADD CONSTRAINT [UQ_Profesor_Identificacion] UNIQUE NONCLUSTERED ([TipoIdentificacion] ASC,[Identificacion] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[Profesor] ADD CONSTRAINT [UQ_Profesor_Email] UNIQUE NONCLUSTERED ([Email] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[Profesor] WITH CHECK ADD CONSTRAINT [CK_Profesor_Email] CHECK ([Email] like '%_@__%.__%' AND NOT [Email] like '% %')
GO
ALTER TABLE [academico].[Profesor] WITH CHECK ADD CONSTRAINT [CK_Profesor_Identificacion] CHECK (len(ltrim(rtrim([Identificacion])))>(0))
GO
ALTER TABLE [academico].[Profesor] WITH CHECK ADD CONSTRAINT [CK_Profesor_Nombre] CHECK (len(ltrim(rtrim([NombreCompleto])))>(0) AND NOT ([NombreCompleto]) collate Latin1_General_100_BIN2 like N'%[^A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]%')
GO
ALTER TABLE [academico].[Profesor] WITH CHECK ADD CONSTRAINT [CK_Profesor_Tipo] CHECK (len(ltrim(rtrim([TipoIdentificacion])))>(0))
GO
CREATE TABLE [academico].[ProfesorTelefono] (
  [ProfesorTelefonoId] [int] IDENTITY(1,1) NOT NULL,
  [ProfesorId] [int] NOT NULL,
  [Telefono] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [academico].[ProfesorTelefono] ADD CONSTRAINT [PK_ProfesorTelefono] PRIMARY KEY CLUSTERED ([ProfesorTelefonoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[ProfesorTelefono] ADD CONSTRAINT [UQ_ProfesorTelefono] UNIQUE NONCLUSTERED ([ProfesorId] ASC,[Telefono] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[ProfesorTelefono] WITH CHECK ADD CONSTRAINT [CK_ProfesorTelefono_NoVacio] CHECK (len(ltrim(rtrim([Telefono])))>(0))
GO
ALTER TABLE [academico].[ProfesorTelefono] WITH CHECK ADD CONSTRAINT [FK_ProfesorTelefono_Profesor] FOREIGN KEY ([ProfesorId]) REFERENCES [academico].[Profesor] ([ProfesorId]) ON DELETE CASCADE ON UPDATE NO ACTION
GO
