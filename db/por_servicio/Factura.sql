-- Estructura del servicio Factura. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Factura]
GO
CREATE SCHEMA [finanzas]
GO
CREATE TABLE [finanzas].[Factura] (
  [FacturaId] [int] IDENTITY(1,1) NOT NULL,
  [MatriculaId] [int] NOT NULL,
  [IdentificacionEstudiante] [varchar](30) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [PeriodoId] [int] NOT NULL,
  [Subtotal] [decimal](12,2) NOT NULL,
  [Impuesto] [decimal](12,2) NOT NULL,
  [Total] [decimal](12,2) NOT NULL,
  [Estado] [varchar](20) COLLATE Modern_Spanish_CI_AS NOT NULL CONSTRAINT [DF_Factura_Estado] DEFAULT ('PENDIENTE'),
  [FechaFactura] [datetime] NOT NULL CONSTRAINT [DF_Factura_Fecha] DEFAULT (getdate())
)
GO
ALTER TABLE [finanzas].[Factura] ADD CONSTRAINT [PK_Factura] PRIMARY KEY CLUSTERED ([FacturaId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_Factura_Periodo] ON [finanzas].[Factura] ([PeriodoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_Factura_Estudiante] ON [finanzas].[Factura] ([IdentificacionEstudiante] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [finanzas].[Factura] WITH CHECK ADD CONSTRAINT [CK_Factura_Estado] CHECK ([Estado]='ANULADA' OR [Estado]='PAGADA' OR [Estado]='PENDIENTE')
GO
ALTER TABLE [finanzas].[Factura] WITH CHECK ADD CONSTRAINT [CK_Factura_Estudiante_NoVacio] CHECK (len(ltrim(rtrim([IdentificacionEstudiante])))>(0))
GO
ALTER TABLE [finanzas].[Factura] WITH CHECK ADD CONSTRAINT [CK_Factura_Montos] CHECK ([Subtotal]>(0) AND [Impuesto]>=(0) AND [Total]=([Subtotal]+[Impuesto]))
GO
CREATE TABLE [finanzas].[FacturaDetalle] (
  [FacturaDetalleId] [int] IDENTITY(1,1) NOT NULL,
  [FacturaId] [int] NOT NULL,
  [Descripcion] [varchar](200) COLLATE Modern_Spanish_CI_AS NOT NULL,
  [Monto] [decimal](12,2) NOT NULL
)
GO
ALTER TABLE [finanzas].[FacturaDetalle] ADD CONSTRAINT [PK_FacturaDetalle] PRIMARY KEY CLUSTERED ([FacturaDetalleId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_FacturaDetalle_Factura] ON [finanzas].[FacturaDetalle] ([FacturaId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [finanzas].[FacturaDetalle] WITH CHECK ADD CONSTRAINT [CK_FacturaDetalle_Descripcion] CHECK (len(ltrim(rtrim([Descripcion])))>(0))
GO
ALTER TABLE [finanzas].[FacturaDetalle] WITH CHECK ADD CONSTRAINT [CK_FacturaDetalle_Monto] CHECK ([Monto]>(0))
GO
ALTER TABLE [finanzas].[FacturaDetalle] WITH CHECK ADD CONSTRAINT [FK_FacturaDetalle_Factura] FOREIGN KEY ([FacturaId]) REFERENCES [finanzas].[Factura] ([FacturaId]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
