-- Estructura del servicio Notificacion. Sin datos ni credenciales.
-- Crear previamente esta base con la intercalacion del origen: Modern_Spanish_CI_AS
USE [PrograV_Notificacion]
GO
CREATE SCHEMA [general]
GO
CREATE TABLE [general].[Notificacion] ([NotificacionId] bigint IDENTITY(1,1) NOT NULL PRIMARY KEY,[Destinatarios] varchar(1000) NOT NULL,[Asunto] nvarchar(150) NOT NULL,[HtmlSanitizado] nvarchar(max) NOT NULL,[Fecha] datetimeoffset NOT NULL,[Usuario] varchar(150) NOT NULL,[Estado] varchar(20) NOT NULL,[Error] nvarchar(500) NULL)
GO
