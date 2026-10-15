-- Solo crea las bases que no existen. No elimina ni copia datos.
USE [master]
GO
IF DB_ID(N'PrograV_Autenticacion') IS NULL
    CREATE DATABASE [PrograV_Autenticacion] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Usuario') IS NULL
    CREATE DATABASE [PrograV_Usuario] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Rol') IS NULL
    CREATE DATABASE [PrograV_Rol] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Parametro') IS NULL
    CREATE DATABASE [PrograV_Parametro] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Modulo') IS NULL
    CREATE DATABASE [PrograV_Modulo] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Bitacora') IS NULL
    CREATE DATABASE [PrograV_Bitacora] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Institucion') IS NULL
    CREATE DATABASE [PrograV_Institucion] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Profesor') IS NULL
    CREATE DATABASE [PrograV_Profesor] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Carrera') IS NULL
    CREATE DATABASE [PrograV_Carrera] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Curso') IS NULL
    CREATE DATABASE [PrograV_Curso] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Periodo') IS NULL
    CREATE DATABASE [PrograV_Periodo] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Grupo') IS NULL
    CREATE DATABASE [PrograV_Grupo] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Direccion') IS NULL
    CREATE DATABASE [PrograV_Direccion] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Expediente') IS NULL
    CREATE DATABASE [PrograV_Expediente] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Prematricula') IS NULL
    CREATE DATABASE [PrograV_Prematricula] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Matricula') IS NULL
    CREATE DATABASE [PrograV_Matricula] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Notas') IS NULL
    CREATE DATABASE [PrograV_Notas] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Factura') IS NULL
    CREATE DATABASE [PrograV_Factura] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Pago') IS NULL
    CREATE DATABASE [PrograV_Pago] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_HistorialAcademico') IS NULL
    CREATE DATABASE [PrograV_HistorialAcademico] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_ListadoEstudiantes') IS NULL
    CREATE DATABASE [PrograV_ListadoEstudiantes] COLLATE Modern_Spanish_CI_AS;
GO
IF DB_ID(N'PrograV_Notificacion') IS NULL
    CREATE DATABASE [PrograV_Notificacion] COLLATE Modern_Spanish_CI_AS;
GO
