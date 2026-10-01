# Migraciones - Persona 5

Scripts incrementales para las HU de Jose:

- `MAT2`: matricula.
- `MAT5`: desglose de rubros y notas.
- `IPN1`: facturacion.
- `IPN2`: pagos.
- `IPN3`: notificaciones por correo.

Ejecutar sobre la base de integracion ya creada. No incluyen `CREATE DATABASE`,
credenciales, datos de prueba ni configuracion SMTP.

## Orden

1. `001_crear_matricula.sql`
2. `002_crear_desglose_rubro.sql`
3. `003_crear_nota_rubro.sql`
4. `004_crear_factura.sql`
5. `005_crear_factura_detalle.sql`
6. `006_crear_pago.sql`

`IPN3` no crea tabla por ahora. El envio de correo debe usar parametros o
variables de ambiente, y cada envio debe quedar auditado mediante `GEN1`.

## Notas de integracion

- `IdentificacionEstudiante` queda como texto hasta que Persona 4 integre la
  tabla de expedientes/estudiantes.
- Las referencias a curso, grupo y periodo apuntan a `academico`.
- La sumatoria de rubros igual a 100 se valida en el servicio, porque depende
  de varias filas.
