# Matriz de criterios y evidencias — Ramsés

Fecha de revisión: 12 de noviembre de 2026.

Esta matriz relaciona cada criterio oficial de las historias `ADM7`, `ADM9`,
`ADM10`, `ADM11`, `ADM12` y `ADM22` con evidencia real conservada en el
repositorio. Una prueba automatizada aprobada se identifica como tal; no se
presenta como una captura de interfaz. `Falta captura` significa que el criterio
tiene implementación o prueba relacionada, pero aún no posee una imagen
individual suficientemente clara para el documento maestro.

## ADM7 — Mantenimiento de módulos

| Criterio | Evidencia real conservada | Estado documental |
|---|---|---|
| CRUD de módulos con ID, nombre, estado y orden | `evidencias-reales/2026-11-11/ADM7-modulos-listado-real.png`, `ADM7-modulos-modal-real.png`; pruebas de navegador “muestra el registro guardado… y confirma su eliminación” y “muestra validaciones…” en `auditoria/evidencias4/resultados-conservados.md` | Cubierto por interfaz y automatización; falta una captura individual del mensaje de creación, modificación y eliminación exitosa |
| Asociación módulo–rol para construir el menú dinámico | `evidencias-reales/2026-11-11/MENU-administrador-real.png`; pruebas `MenuPropioTests` y serialización de roles en `auditoria/evidencias4/resultados-conservados.md` | Implementación y menú cubiertos; falta captura clara del diálogo con la asignación módulo–rol seleccionada y guardada |
| Bitácoras en todas las operaciones | Implementación descrita en `ADM7.md`; no se localizó una captura que muestre juntas las operaciones de alta, modificación, asignación y eliminación | Falta captura de bitácora |

## ADM9 — Mantenimiento de carreras

| Criterio | Evidencia real conservada | Estado documental |
|---|---|---|
| Listado con filtro por institución; CRUD y nombre válido | `2026-11-10/capturas-ui-parciales/ADM9-carreras-listado.png` y `ADM9-carreras-modal.png` | Cubierto visualmente para listado, filtro y formulario; los resultados funcionales del CRUD se documentan con la ejecución automatizada conservada |
| El director existe como profesor y se selecciona mediante búsqueda | `ADM9-carreras-modal.png`; prueba `validaInstitucionYProfesorEnSusServiciosPropietarios` aprobada | Cubierto por interfaz y automatización |
| Impedir eliminación cuando existen cursos vinculados | Prueba `CarreraServiceTest.bloqueaEliminarCuandoExistenCursos` aprobada en `auditoria/evidencias4/resultados-conservados.md` | Cubierto por resultado automatizado real |
| Bitácoras en todas las operaciones | Prueba `registraSnapshotsJsonEnModificacionYEliminacion` aprobada | Cubierto por resultado automatizado real |

## ADM10 — Mantenimiento de cursos

| Criterio | Evidencia real conservada | Estado documental |
|---|---|---|
| Listado con filtros por carrera y nivel; CRUD | `2026-11-10/capturas-ui-parciales/ADM10-cursos-listado.png` y `ADM10-cursos-modal.png`; prueba `CursoFiltrosTest.CombinaCarreraYNivelEnRepositorio` aprobada | Cubierto por interfaz y automatización |
| Nivel entre 1 y 12 y nombre con letras/espacios | Pruebas `CursoAceptaLimitesDelNivel` y `CursoRechazaNivelONombreInvalidos` aprobadas | Cubierto por resultado automatizado real |
| Bloquear eliminación si existen grupos asociados | La pantalla y el servicio contienen el flujo; no se localizó un resultado conservado cuyo nombre demuestre específicamente la referencia a Grupo | Falta evidencia individual del bloqueo |
| Bitácoras en todas las operaciones | Pruebas `CursoAuditoriaTest.AltaConservaElRegistroPersistidoEnJson`, `ModificacionConservaAnteriorYActual` y `EliminacionConservaElRegistroEliminado` aprobadas | Cubierto por resultado automatizado real |

## ADM11 — Mantenimiento de profesores

| Criterio | Evidencia real conservada | Estado documental |
|---|---|---|
| ABM de profesores | `2026-11-10/capturas-ui-parciales/ADM11-profesores-listado.png` y `ADM11-profesores-modal.png` | Cubierto visualmente para listado y formulario; el resultado funcional se respalda con las pruebas conservadas |
| Mayoría de edad | Prueba Node “valida mayoría de edad…” aprobada | Cubierto por resultado automatizado real |
| Correo con dominio parametrizado `cuc.ac.cr` | Implementado y descrito en `ADM11.md`; la evidencia conservada no identifica una ejecución individual del dominio parametrizado | Falta evidencia individual |
| Nombre válido y teléfonos locales requeridos | Pruebas `exigeFormatoLocalDeTelefonosYEvitaDuplicados` y Node de validaciones aprobadas | Cubierto por resultado automatizado real |
| Búsqueda por identificación, nombre o email; paginación y orden | `ADM11-profesores-listado.png` muestra búsqueda y orden; paginación está integrada en la pantalla | Cubierto visualmente |
| Impedir eliminar si dicta grupos activos | Pruebas `bloqueaCuandoHoyEstaDentroDelPeriodoInclusivo` y `permiteCuandoTodosLosPeriodosEstanCerrados` aprobadas | Cubierto por resultado automatizado real |
| Bitácoras en todas las operaciones | Prueba `registraSnapshotsJsonEnBitacoraDeProfesor` aprobada | Cubierto por resultado automatizado real |

## ADM12 — Mantenimiento de periodos

| Criterio | Evidencia real conservada | Estado documental |
|---|---|---|
| CRUD con año, número, fechas y fin mayor o igual al inicio | `2026-11-10/capturas-ui-parciales/ADM12-periodos-listado.png` y `ADM12-periodos-modal.png`; pruebas `PeriodoExigeDatos`, `ContratoActualPermiteFechasIguales` y `RechazaFechaFinalAnteriorAlInicioAntesDeGuardar` aprobadas | Cubierto por interfaz y automatización |
| Estados derivados futuro, activo y cerrado | Prueba `PeriodoReglasTest.EstadoSeDerivaDeFechas` aprobada; la columna de estado aparece en el listado | Cubierto por interfaz y automatización |
| Impedir solapamientos con mismo año y número | Pruebas `DuplicadoProduceConflicto`, `DosAltasEnLaMismaInstanciaNoDuplicanElPeriodo` y `EditarExcluyeElRegistroPropioYConservaOtrosNumeros` aprobadas | Cubierto por resultado automatizado real |
| Bitácoras en todas las operaciones | Hay clasificación de consultas de ADM12 conservada, pero no una evidencia individual de alta, modificación y eliminación | Falta evidencia completa de bitácora |

## ADM22 — Notificaciones

| Criterio | Evidencia real conservada | Estado documental |
|---|---|---|
| Emails validados, asunto y cuerpo HTML sanitizado | `2026-11-11/ADM22-notificaciones-modal-real.png`; pruebas `rechazaDestinatariosInvalidos` y `sanitizacionEliminaScriptsYAtributosPeligrosos` aprobadas | Cubierto por interfaz y automatización |
| Confirmación previa y resultado de éxito o error | `ADM22-notificaciones-modal-real.png` y `2026-11-12/ADM22-correo-recibido-real.png` | Cubierto con evidencia real de recepción; falta solo si se desea documentar también el caso de error visual |
| Historial con fecha, destinatarios, asunto y estado | `2026-11-11/ADM22-notificaciones-historial-real.png`; pruebas de historial ante error aprobadas | Cubierto por interfaz y automatización |
| Servidor y cuenta obtenidos de Parámetros | Pruebas `registraErrorEnHistorialCuandoFaltanParametrosSmtp` y flujo real de correo recibido | Cubierto funcionalmente; no se expone ni captura el valor secreto |

## Pendientes de captura estrictamente necesarios

1. ADM7: confirmaciones visibles de creación, modificación y eliminación.
2. ADM7: rechazo visible de un nombre inválido.
3. ADM7: asignación módulo–rol visible dentro del diálogo y resultado guardado.
4. ADM7: bitácora con creación, modificación, asignación y eliminación.
5. ADM10: bloqueo de eliminación por un grupo asociado.
6. ADM11: rechazo del dominio de correo incorrecto y aceptación del dominio parametrizado.
7. ADM12: bitácora de alta, modificación y eliminación.

Las demás condiciones cuentan con una captura real, una ejecución automatizada
aprobada conservada, o ambas. Al preparar el documento maestro se incluirá en
cada fila la evidencia que realmente le corresponde, sin crear registros falsos
ni convertir texto de auditoría en una supuesta captura del sistema.
