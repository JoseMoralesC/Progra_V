# Migraciones - Persona 5

Organizo mis scripts incrementales para estas historias:

- `MAT2`: matricula.
- `MAT5`: desglose de rubros y notas.
- `IPN1`: facturacion.
- `IPN2`: pagos.
- `IPN3`: notificaciones por correo.

Ejecuto estas migraciones sobre la base de integracion ya creada.
No incluyo `CREATE DATABASE`, credenciales, datos de prueba ni
configuracion SMTP en estos scripts.

## Mi orden de ejecucion

1. `001_crear_matricula.sql`
2. `002_crear_desglose_rubro.sql`
3. `003_crear_nota_rubro.sql`
4. `004_crear_factura.sql`
5. `005_crear_factura_detalle.sql`
6. `006_crear_pago.sql`

Para `IPN3` no creo una tabla en estas migraciones. Configuro el envio
mediante parametros o variables de ambiente y registro cada envio con
`GEN1`.

## Mis notas de integracion

- En estas migraciones mantengo `IdentificacionEstudiante` como texto.
  En PrograV tambien considero la relacion con el expediente incorporada
  por las migraciones de Persona 4.
- Referencio cursos, grupos y periodos del esquema `academico`.
- Valido en el servicio que los rubros sumen 100, porque la suma depende
  de varias filas.
