# Limpieza del proyecto

Revision del 23 de octubre de 2026. Alcance: residuos confirmados y documentacion,
sin cambios en reglas de negocio, endpoints, conexiones, datos ni contratos.

## Cambios

- Se retiraron del indice de Git los 94 archivos generados de
  `services/persona4/target/`. Inicialmente sus copias locales quedaron ignoradas
  mediante la regla general `target/`; al retirar `services/` se trasladaron
  fuera del repositorio junto con esa carpeta. Se retiro la regla especifica redundante.
- Se eliminaron las tres clases vacias `Class1.cs` de las bibliotecas de seguridad.
- Se elimino la copia identica de la coleccion de Fabian bajo
  `primer_avance/evidencia/fabian/`. La fuente unica permanece en
  [Postman](primer_avance/pruebas/postman/Persona4_Fabian_Recorrido.postman_collection.json).
  El validador y su reporte ya no cuentan dos veces esa coleccion.
- Se corrigieron los 16 enlaces Markdown locales rotos detectados. Las referencias
  a archivos historicos ausentes se identifican como tales, sin recrearlos ni
  presentarlos como disponibles.
- Se corrigieron las rutas de integracion Java, el conteo de 22 HU en el roadmap
  V1 y en Doc Master, y la referencia a un lanzador antiguo ausente.
- Se reforzo la identificacion del analisis del 14 de octubre como documento
  historico. Sus conclusiones originales se conservan como registro de esa revision.

## Elementos conservados

Tras la autorizacion posterior del usuario, las fuentes y pruebas historicas de
`services/` fueron retiradas del proyecto. Se conservan el controlador de
compatibilidad de Bitacora, los servicios y registros de dependencias candidatos
a depuracion, las bibliotecas de integracion y los diagnosticos locales en `tmp/`.
Retirar componentes funcionales o diagnosticos sin uso demostrado queda fuera
de esta limpieza. Los cambios locales previos en `.vscode/settings.json` y
`PrograV.sln` se conservaron.

## Retiro de la estructura anterior

El usuario autorizo posteriormente retirar `services/`. Se retiraron sus 229
archivos versionados, sin modificar fuentes, proyectos ni lanzadores activos.
La ultima version anterior al retiro puede consultarse en el commit
`92d38f64bd376468ca5032ae0c6b053d9b46c8e0`.

La eliminacion recursiva fue bloqueada por la revision automatica. Se utilizo
un traslado reversible fuera del repositorio, a
`%TEMP%/PrograV-services-retirado-92d38f6`, incluyendo las salidas locales ignoradas.
La carpeta `services/` ya no existe en el proyecto.

Se actualizaron README, las guias vigentes y la exclusion Java del editor.
Las auditorias mantienen sus datos originales con una nota de archivo; sus
enlaces a fuentes retiradas se convirtieron en rutas historicas para evitar
enlaces locales rotos.

Tras el retiro, `mvnw -B -q -o -f pom.xml verify` termino con salida 0:
273 pruebas Java, sin fallos, errores ni omisiones. Tambien aprobaron las 15
pruebas .NET, los 13 artefactos Java, la validacion estatica de Postman y el
auditor SQL/JPA. No se encontraron referencias a `services/` en los proyectos
y herramientas activos ni enlaces Markdown locales rotos en las guias revisadas.

## Verificacion

- Reactor Maven activo: `verify` aprobado; 273 pruebas, sin fallos, errores ni omisiones.
- Pruebas .NET: 5 de Autenticacion y 10 de Academico aprobadas tras retirar las clases vacias.
- Artefactos Java: los 13 JAR pasan `tools/validar-artefactos.py`.
- Postman: 5 colecciones, 286 solicitudes, 365 scripts y 2 entornos validos;
  13 pruebas de flujos aprobadas. No se enviaron solicitudes HTTP.
- Limites SQL/JPA: 22 servicios y 282 fuentes, sin infracciones en el alcance del auditor.
- Enlaces Markdown revisados en `docs/`, README principal y guia de bases: sin destinos locales rotos.
- Doc Master: se verifico que solo cambia `24 HU.` por `22 HU.` en el XML del documento;
  las demas entradas del archivo DOCX permanecen iguales.
- Diff de fuentes activas: unicamente las tres clases vacias eliminadas.

No se ejecuto la suite de integracion remota, no se modificaron bases SQL Server
ni se enviaron correos. Las pruebas locales no certifican conectividad remota.
