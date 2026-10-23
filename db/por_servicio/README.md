# Bases de datos por servicio

## Resultado de esta ejecucion

Migracion ejecutada y confirmada el 14 de octubre de 2026: **22 bases, 26 tablas y 522 filas**. Se verificaron **52 indices, 43 CHECK y 7 FK locales**, ademas de las definiciones de columnas, valores binarios y contadores de identidad. Una segunda verificacion despues del commit tambien resulto correcta. Se probo el rechazo de una segunda copia sobre destinos ocupados. Los detalles estan en los dos archivos `resultado-*.json`.

Esta etapa implementa la distribucion aprobada de las **26 tablas en 22 bases** de la misma instancia SQL Server accesible por Tailscale. Las credenciales se leen del `.env` local y no se escriben en estos archivos.

`PrograV` se conserva como origen, con sus tablas, filas, relaciones y objetos tecnicos de diagramas. La etapa posterior adapto las conexiones y las consultas entre servicios; su configuracion y pruebas se describen en [integracion-servicios.md](../../docs/primer_avance/integracion-servicios.md). Las credenciales administrativas siguen reservadas a las herramientas y cada aplicacion selecciona su cuenta propia.
## Archivos

- `distribucion.json`: propietario unico de cada tabla. Es la entrada del migrador.
- `plan.json`: inventario obtenido de la base real, indices y relaciones locales/externas.
- `00_crear_bases.sql`: crea unicamente bases que no existen, con la intercalacion del origen.
- Un `.sql` por servicio: estructura sin registros, contrasenas ni tokens. Conserva esquemas, columnas, tipos, nulabilidad, intercalacion, identidad, defaults, PK, unicidad, indices, CHECK y FK internas.
- `resultado-migracion.json`: resultado de la copia confirmada, incluyendo recuentos y SHA-256 de valores binarios por tabla.
- `resultado-verificacion.json`: resultado de una comprobacion independiente posterior.
- `../../tools/migrar-bases.py`: inspeccion, generacion de estructura, copia y verificacion.

Los SQL por servicio son una referencia de estructura para una base vacia. **No ejecutarlos sobre las bases ya migradas ni antes de `--aplicar`**, porque el migrador rechaza destinos con objetos. Para la copia verificada debe usarse el migrador, no ejecutar manualmente los DDL.

## Distribucion

| Base | Tablas |
|---|---|
| PrograV_Autenticacion | seguridad.refresh_token |
| PrograV_Usuario | seguridad.usuario |
| PrograV_Rol | seguridad.rol |
| PrograV_Parametro | seguridad.parametro |
| PrograV_Modulo | seguridad.modulo |
| PrograV_Bitacora | general.bitacora |
| PrograV_Institucion | academico.Institucion |
| PrograV_Profesor | academico.Profesor, academico.ProfesorTelefono |
| PrograV_Carrera | academico.Carrera |
| PrograV_Curso | academico.Curso |
| PrograV_Periodo | academico.Periodo |
| PrograV_Grupo | academico.Grupo |
| PrograV_Direccion | matricula.Provincia, matricula.Canton, matricula.Distrito |
| PrograV_Expediente | matricula.Estudiante, matricula.EstudianteTelefono |
| PrograV_Prematricula | matricula.Prematricula, matricula.PrematriculaCurso |
| PrograV_Matricula | matricula.Matricula |
| PrograV_Notas | matricula.DesgloseRubro, matricula.NotaRubro |
| PrograV_Factura | finanzas.Factura, finanzas.FacturaDetalle |
| PrograV_Pago | finanzas.Pago |
| PrograV_HistorialAcademico | Sin tablas propias |
| PrograV_ListadoEstudiantes | Sin tablas propias |
| PrograV_Notificacion | Sin tablas propias |

Se conservan las tablas vacias del origen y todos los registros exportados/actuales, incluidos historicos, anulados y de prueba. No se corrigen nombres, dominios, fechas, hashes, tokens ni montos durante la migracion.

## Ejecucion

Requisitos: Tailscale conectado, Python con `pyodbc`, Microsoft ODBC Driver 18 y permisos SQL suficientes para leer el origen y crear bases/objetos destino. `DB_NAME` sigue identificando el origen; no cambiarlo por uno de los destinos para ejecutar el migrador.

Desde la raiz del repositorio:

```powershell
# Solo inspecciona SQL Server y genera estructura local sin datos.
python tools/migrar-bases.py

# Crea destinos vacios, copia y verifica antes de confirmar.
python tools/migrar-bases.py --aplicar

# Compara estructura y datos actuales sin modificar SQL Server.
python tools/migrar-bases.py --verificar
```

`--aplicar` rechaza destinos con tablas u otros objetos de aplicacion, en lugar de vaciarlos o sobrescribirlos. Una segunda ejecucion despues de migrar debe rechazar el destino ocupado. No es una herramienta de sincronizacion ni una migracion incremental.

Las bases se crean antes de la transaccion de copia, porque SQL Server exige `CREATE DATABASE` fuera de ella. Si falla la copia, se revierten sus tablas/filas; las bases previamente creadas quedan vacias para un reintento. Nunca se eliminan bases automaticamente.

## Verificaciones

La copia usa una unica conexion y una transaccion que abarca los destinos. Durante ella toma bloqueos compartidos de tabla con `HOLDLOCK` sobre el origen para impedir que sus filas cambien mientras se copian y verifican. Esto puede bloquear temporalmente escrituras de las aplicaciones; no altera filas del origen. Hay limite de espera al adquirir bloqueos.

Antes de copiar compara de nuevo los metadatos y comprueba que las 31 relaciones originales no tengan referencias huerfanas. Las filas se copian directamente dentro del servidor con `INSERT ... SELECT`, manteniendo los IDs originales mediante `IDENTITY_INSERT`.

Para cada tabla compara el numero de registros y SHA-256 de todas las columnas convertidas a valores binarios, ordenadas por PK. El hash incorpora marcadores de NULL y longitudes para distinguir valores, espacios finales y limites entre campos. Los reportes no incluyen contenido de registros.

Tambien compara definiciones de columnas, restricciones, indices y FK locales, asi como los contadores de identidad. Conserva contadores que hayan avanzado mas alla del ID maximo almacenado para no reutilizar identificadores eliminados. Las caracteristicas de tabla no contempladas por el generador producen un rechazo, no una conversion silenciosa.

La verificacion posterior compara contra el origen en ese momento. Si las aplicaciones escriben en el origen o en las bases separadas, los destinos pueden dejar de coincidir; no se mantienen sincronizados automaticamente. Despues de empezar a usar las bases separadas, una diferencia puede corresponder a nuevas operaciones legitimas: no se debe volver a copiar el origen sobre ellas. El cambio inicial requiere un corte coordinado y una copia actualizada.

## Relaciones e integracion posterior

Se mantienen **7 FK internas** y se documentan **24 referencias entre servicios** en `plan.json`. Sus columnas y valores se preservan, pero en los destinos no existen las FK externas ni sus cascadas. SQL Server no permite FK entre bases distintas.

La adaptacion posterior incorpora:

1. Clientes HTTP para consultar datos del propietario y entidades JPA limitadas a tablas propias.
2. Verificacion interna de credenciales entre Autenticacion y Usuario.
3. Una tabla tecnica `finanzas.OperacionPago` para recuperacion de Pago/Factura, sin modificar los registros migrados.
4. Validacion de referencias por API y rechazo de borrados/cambios de identificacion con referencias externas.
5. Cuentas de ejecucion independientes, sin `db_owner` ni acceso a bases ajenas.
6. Una suite reproducible de integracion con bases temporales y reporte separado de esta verificacion de copia.

La comparacion de copia cubre las 26 tablas originales. Los resultados funcionales y los limites de las comprobaciones HTTP de referencias se documentan en [integracion-servicios.md](../../docs/primer_avance/integracion-servicios.md).
