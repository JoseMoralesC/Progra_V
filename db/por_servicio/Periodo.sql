-- Estructura del servicio Periodo. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Periodo]
GO
CREATE SCHEMA [academico]
GO
CREATE TABLE [academico].[Periodo] (
  [PeriodoId] [int] IDENTITY(1,1) NOT NULL,
  [Anio] [smallint] NOT NULL,
  [NumeroPeriodo] [tinyint] NOT NULL,
  [FechaInicio] [date] NOT NULL,
  [FechaFin] [date] NOT NULL
)
GO
ALTER TABLE [academico].[Periodo] ADD CONSTRAINT [PK_Periodo] PRIMARY KEY CLUSTERED ([PeriodoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[Periodo] ADD CONSTRAINT [UQ_Periodo_Anio_Numero] UNIQUE NONCLUSTERED ([Anio] ASC,[NumeroPeriodo] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [academico].[Periodo] WITH CHECK ADD CONSTRAINT [CK_Periodo_Fecha] CHECK ([FechaFin]>=[FechaInicio])
GO
