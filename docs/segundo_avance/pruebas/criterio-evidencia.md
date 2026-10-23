# Evidencia por criterio y responsabilidad individual

Plantilla de trabajo para A2 y A3. No contiene ejecuciones ni notas ficticias.
Cada persona mantiene la evidencia de sus historias segun el
[backlog unico](../planificacion/backlog.md).

## Organizacion al ejecutar

| Entrega | Matriz por responsable | Capturas |
|---|---|---|
| A2 | `docs/segundo_avance/pruebas/personaN.md` | `docs/segundo_avance/evidencia/personaN/HU/` |
| A3 | `docs/tercer_avance/pruebas/personaN.md` | `docs/tercer_avance/evidencia/personaN/HU/` |

Personas: 1 Jose, 2 Ramses, 3 Alejandro y 4 Fabian. El ID de persona conserva
la asignacion V2 del equipo; no se confunde con el antiguo reparto V1.
Crear matrices y carpetas de capturas cuando haya trabajo y ejecuciones reales.

## Ficha de cada HU

Registrar ID, titulo, origen (pagina/criterio del PDF o WEB), responsable unico,
revisor, puntos acordados, entrega, dependencias, contratos afectados, datos de
prueba, version/commit y estado. Para ADM/MOB/GTW transcribir o enumerar todos
los criterios del PDF; el resumen del backlog no sustituye esa lista.

Los criterios comunes de bitacora, seguridad y flujo de mantenimiento tambien
se vinculan a las HU donde apliquen. La ficha debe permitir al profesor identificar
que se evalua y quien responde sin deducirlo de un archivo compartido.

| HU | Criterio y fuente | Responsable | Precondiciones y pasos | Esperado | Observado | Captura/registro | Version | Estado |
|---|---|---|---|---|---|---|---|---|
| WEB2 | C5 y condiciones comunes | Fabian | Dos estudiantes; intentar cambiar identificacion o un expediente ajeno | Rechazo en servidor; datos intactos | Por ejecutar | Pendiente | Pendiente | Pendiente |

La fila es un ejemplo de plan de prueba. No acredita que el rechazo ya exista.
Usar al menos una fila por criterio; una misma captura solo cubre varios si
demuestra cada resultado y queda enlazada explicitamente.

## Seguimiento de carga y calificacion

| HU | Responsable | Entrega | Puntos acordados | Criterios totales | Criterios verificados | Estado | Nota del profesor | Observacion |
|---|---|---|---|---|---|---|---|---|
| ID de backlog | Nombre | 2 o 3 | Estimacion vigente | Al preparar la ficha | Al ejecutar | Pendiente | Sin asignar | Bloqueo o decision |

No se calcula una nota ponderada sin conocer la rubrica. Los puntos de complejidad
no son una nota, y cerrar muchas tareas tecnicas no compensa automaticamente
un criterio incumplido de otra HU. Las correcciones se vinculan a la historia
afectada y a su responsable, registrando dependencias cuando corresponda.

## Pruebas minimas de integracion por historia

1. Flujo positivo completo desde la interfaz exigida con servicios reales.
2. Validaciones de campos, relaciones y estados segun la fuente del requisito.
3. Usuario sin token y token expirado; permisos por rol y propiedad donde aplique.
4. Dos estudiantes: intentar acceder o escribir datos ajenos modificando la peticion.
5. Servicio dependiente caido: mensaje controlado, sin falso exito ni datos inventados.
6. Bitacora del servicio con usuario, fecha y detalle de la operacion requerido.
7. Captura de resultado, comprobacion de datos y version usada; datos sensibles ocultos.

Las pruebas unitarias y Postman son complementos. Una coleccion importable o un
mock no demuestra una pantalla integrada. No registrar contrasenas, tokens o claves
internas en capturas. Los datos de demostracion son identificables y su limpieza
se limita a lo creado para esas pruebas.

## Evidencia de subhistorias

ADM3, ADM17, MOB1 y MOB5 tienen dos unidades evaluables cada una, con criterios
y responsables en [subhistorias](../planificacion/subhistorias.md). Registrar
ficha, evidencia y nota por ID completo (por ejemplo ADM17-N). El padre conserva
la cobertura del PDF y su prueba conjunta, pero no suma otros 8 puntos ni otra
nota. Hay 45 unidades evaluables sobre 41 historias de origen.

## Revision y cierre

Estados: pendiente, en desarrollo, bloqueada, en verificacion y cerrada. Una HU
cerrada conserva todos sus criterios, evidencia real y revision de apoyo. La
calificacion del profesor se registra por separado cuando exista.

En A3 se agrega evidencia de regresion de las HU A2. No se copian capturas antiguas
como si fueran ejecuciones nuevas ni se vuelven a sumar sus puntos. Si el alcance,
responsable o puntos cambia, se actualiza primero el backlog y se registra la
decision, sin trasladar silenciosamente carga evaluable entre personas.
