# Scripts de SQL Server

Documento dos formas alternativas de preparar una **base vacía**.
Selecciono la base destino en SQL Server antes de ejecutar los archivos;
ninguno incluye `CREATE DATABASE` ni borra tablas.

## Mi instalación completa

Para esta alternativa ejecuto únicamente
[`01_base_nuevo_avatar.sql`](01_base_nuevo_avatar.sql).
Con este archivo creo la estructura de los cuatro esquemas y cargo
los catálogos mínimos.

## Mi instalación por partes

Para esta alternativa ejecuto los archivos de [`separado/`](separado/)
en este orden:

1. [`01_seguridad_general.sql`](separado/01_seguridad_general.sql): Persona 1 y bitácora.
2. [`02_academico.sql`](separado/02_academico.sql): Persona 2.
3. [`03_matricula_catalogos.sql`](separado/03_matricula_catalogos.sql): tablas territoriales presentes en la base compartida.
4. [`04_datos_iniciales.sql`](separado/04_datos_iniciales.sql): roles, módulos y parámetros mínimos.

Considero que los tres archivos de estructura rechazan tablas existentes
en su propio esquema para evitar instalaciones parciales. Puedo repetir
el archivo de datos iniciales sin sobrescribir valores existentes.
**Elijo una sola ruta para cada base.** Para una base ya instalada,
documento los cambios posteriores en scripts de migración.

En los scripts base no incluyo usuarios, contraseñas, tokens, bitácoras,
datos geográficos ni credenciales SMTP. Preparo esos datos según el entorno
o la fuente oficial correspondiente. El script adicional
`05_usuario_prueba_jose.sql` queda separado de las dos rutas base.
