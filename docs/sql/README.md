# Scripts de SQL Server

Hay dos formas alternativas de preparar una **base vacía**. Seleccioná la base destino en SQL Server antes de ejecutar los archivos; ninguno incluye `CREATE DATABASE` ni borra tablas.

## Instalación completa

Ejecutá únicamente [`01_base_nuevo_avatar.sql`](01_base_nuevo_avatar.sql). Crea la estructura de los cuatro esquemas y carga los catálogos mínimos.

## Instalación por partes

Ejecutá estos cuatro archivos de [`separado/`](separado/) en orden:

1. [`01_seguridad_general.sql`](separado/01_seguridad_general.sql): Persona 1 y bitácora.
2. [`02_academico.sql`](separado/02_academico.sql): Persona 2.
3. [`03_matricula_catalogos.sql`](separado/03_matricula_catalogos.sql): tablas territoriales presentes en la base compartida.
4. [`04_datos_iniciales.sql`](separado/04_datos_iniciales.sql): roles, módulos y parámetros mínimos.

Los tres archivos de estructura rechazan tablas existentes en su propio esquema para evitar instalaciones parciales. El archivo de datos iniciales sí puede repetirse sin sobrescribir valores existentes. **No ejecutes ambas rutas sobre la misma base.** Para una base ya instalada, los cambios posteriores deben ir en scripts de migración.

No se incluyen usuarios, contraseñas, tokens, bitácoras, datos geográficos ni credenciales SMTP. Esos datos dependen del entorno o de una fuente oficial.
