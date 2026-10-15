-- Estructura del servicio Pago. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Pago]
GO
CREATE SCHEMA [finanzas]
GO
CREATE TABLE [finanzas].[Pago] (
  [PagoId] [int] IDENTITY(1,1) NOT NULL,
  [FacturaId] [int] NOT NULL,
  [PeriodoId] [int] NOT NULL,
  [Monto] [decimal](12,2) NOT NULL,
  [Estado] [varchar](20) COLLATE Modern_Spanish_CI_AS NOT NULL CONSTRAINT [DF_Pago_Estado] DEFAULT ('APLICADO'),
  [FechaPago] [datetime] NOT NULL CONSTRAINT [DF_Pago_Fecha] DEFAULT (getdate())
)
GO
ALTER TABLE [finanzas].[Pago] ADD CONSTRAINT [PK_Pago] PRIMARY KEY CLUSTERED ([PagoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_Pago_Factura] ON [finanzas].[Pago] ([FacturaId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
CREATE NONCLUSTERED INDEX [IX_Pago_Periodo] ON [finanzas].[Pago] ([PeriodoId] ASC) WITH (PAD_INDEX=OFF, IGNORE_DUP_KEY=OFF, ALLOW_ROW_LOCKS=ON, ALLOW_PAGE_LOCKS=ON)
GO
ALTER TABLE [finanzas].[Pago] WITH CHECK ADD CONSTRAINT [CK_Pago_Estado] CHECK ([Estado]='ANULADO' OR [Estado]='APLICADO')
GO
ALTER TABLE [finanzas].[Pago] WITH CHECK ADD CONSTRAINT [CK_Pago_Monto] CHECK ([Monto]>(0))
GO
