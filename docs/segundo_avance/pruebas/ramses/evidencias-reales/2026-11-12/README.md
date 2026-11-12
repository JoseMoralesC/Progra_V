# Evidencia real de Ramsés — 12 de noviembre de 2026

Versión ejecutada: `fb76dbe`. Entorno: gateway local y servicios conectados a las bases compartidas por Tailscale; Notificación conserva su historial en la base local acordada mientras la tabla remota no esté disponible.

## ADM7, ADM9, ADM10, ADM11 y ADM12

Ejecuté un recorrido temporal identificado con la marca `RPQGSB`. El flujo obtuvo **26/26 verificaciones correctas**:

- Inicio de sesión administrativo: `201`.
- Módulo: alta `201`, modificación `200`, asignación de rol `204`, consulta de roles `200` y baja `200`.
- Profesor: alta `201`, modificación `200`, búsqueda paginada/ordenada `200` y baja `204`.
- Carrera: alta `201`, modificación `200`, filtro por institución `200` y baja `204`.
- Curso: alta `201`, modificación `200`, filtros combinados de carrera/nivel `200` y baja `204`.
- Periodo: alta `201`, modificación `204`, filtro por año/estado `200` y baja `204`.
- Bitácora: consulta `200` y nueve registros asociados a la marca temporal.

Curso y Periodo necesitan que sus servicios propietarios de referencias estén activos para eliminar. Con Matricula, Factura y los demás propietarios levantados, ambas eliminaciones terminaron correctamente. Esto confirma también el comportamiento `503` cuando una dependencia necesaria no está disponible.

Después del recorrido comprobé individualmente que Módulo, Profesor, Carrera, Curso y los dos periodos temporales devolvieran `404`. No quedaron datos de prueba.

## ADM12 — concurrencia

Envié simultáneamente dos altas con el mismo año, número y rango:

- Primera respuesta observada: `201`.
- Segunda respuesta observada: `409`.
- Consulta directa del registro ganador: `200`.
- Eliminación del registro ganador: `204`.

El resultado demuestra que solo una operación persiste y que el conflicto no deja un segundo periodo parcial.

## ADM22 — configuración ausente e historial

Ejecuté una notificación autorizada con HTML que contenía etiquetas y atributos peligrosos. Como los parámetros `MAILHOST`, `MAILPORT`, `MAILUSER`, `MAILPASS`, `MAILFROM` y `MAILTLS` no están preparados en el servicio remoto de Parametro, el resultado esperado y observado fue:

- Envío: `503`.
- Historial con estado `ERROR`: encontrado.
- HTML almacenado sin `<script>` ni `onerror`: comprobado.
- Error controlado sin contraseña ni secreto: comprobado.

No se inventaron ni publicaron credenciales SMTP. Queda pendiente únicamente preparar esos seis parámetros por un responsable autorizado y comprobar envío, recepción e historial `ENVIADA`.

## Ejecutor reproducible

El recorrido CRUD está en `tools/recorrido-real-ramses.ps1`. Recibe usuario y contraseña como parámetros, no contiene credenciales y elimina sus datos en orden inverso dentro de un bloque `finally`.

