-- Estructura del servicio Expediente. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Expediente]
GO
CREATE SCHEMA [matricula]
GO
CREATE TABLE [matricula].[Estudiante] (
  [Identificacion] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [TipoIdentificacion] [nvarchar](max) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [Email] [nvarchar](max) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [NombreCompleto] [nvarchar](max) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [FechaNacimiento] [date] NOT NULL,
  [DistritoId] [int] NOT NULL,
  [OtrasSenas] [nvarchar](max) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [matricula].[Estudiante] ADD CONSTRAINT [PK_Estudiante] PRIMARY KEY CLUSTERED ([Identificacion] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[Estudiante] WITH CHECK ADD CONSTRAINT [CK_Estudiante_Email] CHECK (len(ltrim(rtrim([Email])))>(0))
GO
ALTER TABLE [matricula].[Estudiante] WITH CHECK ADD CONSTRAINT [CK_Estudiante_Identificacion] CHECK (len(ltrim(rtrim([Identificacion])))>(0))
GO
ALTER TABLE [matricula].[Estudiante] WITH CHECK ADD CONSTRAINT [CK_Estudiante_Nombre] CHECK (len(ltrim(rtrim([NombreCompleto])))>(0))
GO
ALTER TABLE [matricula].[Estudiante] WITH CHECK ADD CONSTRAINT [CK_Estudiante_Senas] CHECK (len(ltrim(rtrim([OtrasSenas])))>(0))
GO
ALTER TABLE [matricula].[Estudiante] WITH CHECK ADD CONSTRAINT [CK_Estudiante_Tipo] CHECK (len(ltrim(rtrim([TipoIdentificacion])))>(0))
GO
CREATE TABLE [matricula].[EstudianteTelefono] (
  [EstudianteTelefonoId] [int] IDENTITY(1,1) NOT NULL,
  [IdentificacionEstudiante] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [Telefono] [nvarchar](max) COLLATE Modern_Spanish_CI_AS NOT NULL
)
GO
ALTER TABLE [matricula].[EstudianteTelefono] ADD CONSTRAINT [PK_EstudianteTelefono] PRIMARY KEY CLUSTERED ([EstudianteTelefonoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_EstudianteTelefono_Estudiante] ON [matricula].[EstudianteTelefono] ([IdentificacionEstudiante] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [matricula].[EstudianteTelefono] WITH CHECK ADD CONSTRAINT [CK_EstudianteTelefono_NoVacio] CHECK (len(ltrim(rtrim([Telefono])))>(0))
GO
ALTER TABLE [matricula].[EstudianteTelefono] WITH CHECK ADD CONSTRAINT [FK_EstudianteTelefono_Estudiante] FOREIGN KEY ([IdentificacionEstudiante]) REFERENCES [matricula].[Estudiante] ([Identificacion]) ON DELETE CASCADE ON UPDATE CASCADE
GO
