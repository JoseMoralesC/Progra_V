USE [PrograV_Notificacion];
GO
IF SCHEMA_ID('general') IS NULL EXEC('CREATE SCHEMA general');
GO
IF OBJECT_ID('general.Notificacion','U') IS NULL CREATE TABLE [general].[Notificacion] ([NotificacionId] bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_Notificacion PRIMARY KEY,[Destinatarios] varchar(1000) NOT NULL,[Asunto] nvarchar(150) NOT NULL,[HtmlSanitizado] nvarchar(max) NOT NULL,[Fecha] datetimeoffset NOT NULL,[Usuario] varchar(150) NOT NULL,[Estado] varchar(20) NOT NULL,[Error] nvarchar(500) NULL);
GO
