-- Preparación MANUAL de datos; NO forma parte de un arranque ni se ejecuta automáticamente.
-- Requiere migración 002 aplicada. Usar los ID reales consultados en Rol/Usuario.
-- Revisar con el equipo antes de la primera activación del gateway con permisos.
-- No volver a usar después de administrar revocaciones: repondría las asignaciones iniciales.
USE [PrograV_Modulo];
GO
SET XACT_ABORT ON;
DECLARE @ConfirmarInicializacion bit = 0;
DECLARE @IdRolAdministrador varchar(20) = NULL;
DECLARE @IdRolProfesor varchar(20) = NULL;
DECLARE @IdRolEstudiante varchar(20) = NULL;
IF @ConfirmarInicializacion <> 1 OR NULLIF(LTRIM(RTRIM(@IdRolAdministrador)), '') IS NULL
    THROW 50000, 'Indicar los ID reales de rol y confirmar explícitamente esta inicialización.', 1;
IF OBJECT_ID('seguridad.rol_modulo', 'U') IS NULL
    THROW 50000, 'Aplicar y verificar primero la migración 002.', 1;

DECLARE @Catalogo TABLE (id varchar(20) PRIMARY KEY, nombre varchar(100), orden int);
INSERT @Catalogo VALUES
 ('ADM3','Inicio',3), ('ADM4','Usuarios',4), ('ADM5','Roles',5), ('ADM6','Parametros',6),
 ('ADM7','Modulos',7), ('ADM8','Instituciones',8), ('ADM9','Carreras',9), ('ADM10','Cursos',10),
 ('ADM11','Profesores',11), ('ADM12','Periodos',12), ('ADM13','Grupos',13),
 ('ADM14','Prematriculas',14), ('ADM15','Matriculas',15), ('ADM16','Expedientes',16),
 ('ADM17','Notas',17), ('ADM18','Historial academico',18), ('ADM19','Listados',19),
 ('ADM20','Facturas',20), ('ADM21','Pagos',21), ('ADM22','Notificaciones',22),
 ('ADM23','Bitacoras',23), ('ADM24','Perfil',24);
BEGIN TRANSACTION;
-- No modificar nombre/estado/orden de módulos ya existentes.
INSERT seguridad.modulo (id_modulo, nombre_modulo, activo, orden)
SELECT c.id, c.nombre, 1, c.orden FROM @Catalogo c
WHERE NOT EXISTS (SELECT 1 FROM seguridad.modulo m WITH (UPDLOCK,HOLDLOCK) WHERE m.id_modulo=c.id);

DECLARE @Asignaciones TABLE (id_modulo varchar(20), id_rol varchar(20));
-- Solo alcance A2. Los módulos A3 se crean como catálogo, sin otorgarlos.
INSERT @Asignaciones SELECT id, UPPER(LTRIM(RTRIM(@IdRolAdministrador))) FROM @Catalogo
WHERE id IN ('ADM3','ADM4','ADM5','ADM6','ADM7','ADM8','ADM9','ADM10','ADM11','ADM12','ADM22','ADM24');
IF NULLIF(LTRIM(RTRIM(@IdRolProfesor)), '') IS NOT NULL
    INSERT @Asignaciones SELECT id, UPPER(LTRIM(RTRIM(@IdRolProfesor))) FROM @Catalogo WHERE id IN ('ADM3','ADM17','ADM24');
IF NULLIF(LTRIM(RTRIM(@IdRolEstudiante)), '') IS NOT NULL
    INSERT @Asignaciones VALUES ('ADM24', UPPER(LTRIM(RTRIM(@IdRolEstudiante))));
INSERT seguridad.rol_modulo (id_modulo,id_rol)
SELECT DISTINCT a.id_modulo,a.id_rol FROM @Asignaciones a
WHERE NOT EXISTS (SELECT 1 FROM seguridad.rol_modulo r WITH (UPDLOCK,HOLDLOCK)
                  WHERE r.id_modulo=a.id_modulo AND r.id_rol=a.id_rol);
COMMIT;
SELECT m.id_modulo,m.nombre_modulo,m.activo,m.orden,r.id_rol
FROM seguridad.modulo m LEFT JOIN seguridad.rol_modulo r ON r.id_modulo=m.id_modulo
ORDER BY m.orden,r.id_rol;
