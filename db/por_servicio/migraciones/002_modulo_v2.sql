USE [PrograV_Modulo];
GO
IF COL_LENGTH('seguridad.modulo', 'activo') IS NULL
    ALTER TABLE seguridad.modulo ADD activo bit NOT NULL CONSTRAINT DF_modulo_activo DEFAULT (1);
GO
IF COL_LENGTH('seguridad.modulo', 'orden') IS NULL
    ALTER TABLE seguridad.modulo ADD orden int NOT NULL CONSTRAINT DF_modulo_orden DEFAULT (0);
GO
IF NOT EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = 'CK_modulo_orden')
    ALTER TABLE seguridad.modulo WITH CHECK ADD CONSTRAINT CK_modulo_orden CHECK (orden >= 0);
GO
IF OBJECT_ID('seguridad.rol_modulo', 'U') IS NULL
BEGIN
    CREATE TABLE seguridad.rol_modulo (
        id_modulo varchar(20) NOT NULL,
        id_rol varchar(20) NOT NULL,
        CONSTRAINT PK_rol_modulo PRIMARY KEY (id_modulo, id_rol),
        CONSTRAINT FK_rol_modulo_modulo FOREIGN KEY (id_modulo)
            REFERENCES seguridad.modulo(id_modulo) ON DELETE CASCADE
    );
END;
GO
