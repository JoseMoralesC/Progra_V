# Incremento de Jose — concurrencia ADM17 y permisos GTW2

Fecha: 4 de noviembre de 2026 (America/Mexico_City).
Base: `034ca8b13928ffa1709d69e0b04bd70ceb99d1f0` y los incrementos locales de
sesión/notas, sin commit nuevo al registrar esta ficha.

## Desarrollo incorporado

En Notas, las transacciones de reemplazo de desglose y asignación/modificación de
nota ahora adquieren un bloqueo exclusivo por grupo mediante `sp_getapplock`,
con propietario Transaction y espera máxima de cinco segundos. El recurso es
`NuevoAvatar.Notas.grupo:<grupoId>` en la base del servicio. No depende de un
semáforo local ni requiere crear tablas o modificar el esquema.

El reemplazo obtiene el bloqueo antes de leer rubros y comprobar si hay notas.
La asignación lo obtiene antes de leer el rubro y la nota existente. Así, si la
primera nota se confirma antes, el reemplazo encuentra notas y responde 409.
Si el reemplazo se confirma primero, una solicitud con el ID del rubro eliminado
se rechaza al volver a leerlo (422), sin crear la nota. Dos POST del mismo par
matrícula/rubro se serializan: el segundo detecta existencia y responde 409.

El bloqueo queda ligado al commit/rollback de la misma transacción de JPA.
`JdbcTemplate` utiliza el mismo DataSource; se rechaza adquirirlo si no hay
transacción activa o la conexión queda en autocommit. Un resultado negativo del
procedimiento produce 409 sin continuar la escritura. Un fallo técnico SQL se
mantiene como error de servidor. No se introducen reintentos automáticos de escritura.

La publicación de snapshots de bitácora después del commit se conserva; los
eventos de éxito no se envían al hacer rollback. Ese mecanismo no garantiza por
sí solo recuperación cuando Bitácora esté caída, que sigue como pendiente.

Referencias técnicas: [contrato de sp_getapplock de Microsoft](https://learn.microsoft.com/en-us/sql/relational-databases/system-stored-procedures/sp-getapplock-transact-sql?view=sql-server-ver17)
y [participación de JDBC en transacciones Spring](https://docs.spring.io/spring-framework/reference/data-access/jdbc/connections.html).

En GTW2 se implementa validación dinámica por módulo activo y rol persistido,
además del perfil máximo y de la propiedad validada por cada dominio. Menú propio
en `/gateway/menu`, consumo desde la plantilla y las instrucciones de integración
quedan definidos en el [contrato rol-módulo](../planificacion/permisos-rol-modulo.md).
Se aporta preparación SQL manual con confirmación explícita; no se ejecuta.

## Verificación

| Conjunto | Alcance de la comprobación |
| --- | --- |
| Concurrencia Notas | Tres escenarios deterministas con dos instancias de servicio y dos transacciones: nota antes de reemplazo, reemplazo antes de nota y dos POST simultáneos |
| Adaptador de bloqueo | Ocho casos de contrato JDBC: éxito inmediato/tras espera, resultados negativos, transacción ausente y conexión autocommit |
| Regresión Notas | Escrituras, restricciones, permisos de grupo, HTTP, snapshots y eventos de commit/rollback anteriores |
| Gateway HTTP | Retiro de asignación, inactividad, techo de perfil, fallo de permisos, identidad y contratos HTTP/YARP |
| Menú propio del servicio | Uso del ID real de rol de la cuenta, consulta auditada y rechazo sin identidad |
| React | Sesión/notas conservadas; permisos al entrar/navegar, menú oculto al revocar, URL directa sin permiso y caída/reintento |
| Cliente/formulario compartidos | Regresión de sesión Node y login/notas de la aplicación aislada de Jose |

Los escenarios concurrentes usan repositorios y proveedor de bloqueo simulados,
con sincronización ligada a las transacciones de prueba. Verifican el orden de
la lógica entre dos instancias; **no certifican una ejecución real del bloqueo
SQL Server**. El adaptador JDBC prueba la llamada y sus resultados con conexiones
simuladas. Gateway usa destinos simulados y el navegador intercepta las API.

Comandos desde la raíz:

```powershell
dotnet test PrograV.sln --no-restore --verbosity minimal
.\NuevoAvatar.Notas\mvnw.cmd -B -pl integracion,NuevoAvatar.Notas -am test
npm.cmd test --prefix web/jose
npm.cmd run test:ui --prefix web/jose -- --reporter=line
npm.cmd run test:ui --prefix NuevoAvatar.Estudiante.React -- --reporter=line
npm.cmd run build --prefix NuevoAvatar.Estudiante.React
npm.cmd run lint --prefix NuevoAvatar.Estudiante.React
```

Resultados finales de este incremento:

- Gateway: **27/27** pruebas .NET aprobadas, incluidas las dos del menú propio.
- Regresión .NET: Autenticación **29/29** y Académico **17/17**, aprobadas.
- Notas: **45/45** pruebas Java aprobadas; Integración **3/3**, aprobadas.
- Cliente compartido: **10/10** pruebas Node aprobadas.
- Navegador de la plantilla: **27/27** aprobadas.
- Navegador del cliente aislado de Jose: **6/6** aprobadas.
- Build de gateway/Módulo y de la plantilla aprobado. Lint sin errores;
  conserva una advertencia previa de Fast Refresh en `NotificationContext.jsx`.

Compilaciones y repeticiones de una suite no se cuentan como pruebas adicionales.
Estas cifras no equivalen a aceptación integral ni se suman a las del corte anterior.

## Pendientes de entorno y entrega

Comprobar `sp_getapplock` con dos conexiones reales contra `PrograV_Notas`, con
la cuenta de ejecución del servicio y el mismo DataSource/JpaTransactionManager.
Todas las instancias de Notas deben usar esta versión y la misma base de datos;
una instancia antigua o escritura SQL externa que omita el bloqueo no participa
del protocolo. Probar también timeout y rollback con datos de ensayo controlados.

Aplicar/verificar la preparación de permisos en el entorno correspondiente antes
de usar el gateway actualizado: ya no hay acceso por el solo perfil administrador.
Demostrar con propietarios/SQL reales que retirar ADM17/otro módulo cambia menú
y acceso API. Integrar la matriz y pantallas de Fabian/Ramses conforme al contrato.

No se aplicaron migraciones, no se ejecutó el script de asignaciones ni se escribieron
datos compartidos. No se modifican las barras históricas de auditoría ni se declaran
cerradas las HU por estas pruebas; son desarrollo posterior y evidencia local.
