# Postman con bases por servicio

Las colecciones usan ahora una variable de URL para cada servicio. Las rutas,
metodos, cuerpos, identificadores de prueba y aserciones existentes se conservan.
La coleccion de Fabian tiene una unica copia en
`Persona4_Fabian_Recorrido.postman_collection.json`, en esta carpeta. Se retiro
su duplicado de `docs/primer_avance/evidencia/fabian`; las capturas se conservan.

## Importar y ejecutar

1. Compilar desde la raiz con `powershell -NoProfile -File tools/compilar-servicios.ps1` si hubo cambios de codigo.
2. Iniciar los servicios con los cinco archivos `ejecutar_persona*.bat`. Los flujos requieren las APIs de sus dependencias; iniciar solamente los servicios de una persona puede resultar insuficiente.
3. Reimportar las colecciones actualizadas en Postman y actualizar las copias existentes cuando corresponda.
4. Para Persona 1 y Persona 2, importar y seleccionar `Persona1_Persona2.postman_environment.json`. Completar `usuario` y `password` con una cuenta de la aplicacion. El login guarda los tokens en ese entorno.
5. Para las otras colecciones, usar `NuevoAvatar_Microservicios.postman_environment.json` o las URLs predeterminadas de la propia coleccion. Este entorno general contiene solamente URLs y no oculta los tokens que guardan las colecciones. Persona 3 usa `loginUsuario`/`loginContrasena`; los recorridos de Persona 4 y Persona 5 conservan sus variables de autenticacion. Las colecciones individuales MAT/ACA requieren completar su variable `token` con un JWT vigente.
6. Revisar las variables activas: Postman puede conservar valores locales anteriores al reimportar. Ejecutar las solicitudes en su orden original y completar la limpieza prevista por cada recorrido.

Las credenciales de la aplicacion son distintas de los usuarios SQL. Postman no
necesita las claves de SQL Server ni `SERVICES_INTERNAL_KEY`.

Los servicios se conectan a SQL Server por Tailscale usando su `.env`. Si las APIs
corren en otra computadora, reemplazar `localhost` en las variables del entorno
por la direccion Tailscale de esa computadora, manteniendo el puerto de cada API.
La direccion del servidor de SQL no necesariamente es la direccion de las APIs.
Las llamadas entre servicios tambien deben apuntar a las maquinas correspondientes.

## URLs locales

| Variable | Puerto |
|---|---:|
| autenticacionUrl | 5158 |
| bitacoraUrl | 5159 |
| rolUrl | 5160 |
| parametroUrl | 5161 |
| moduloUrl | 5162 |
| usuarioUrl | 8082 |
| institucionUrl | 8086 |
| profesorUrl | 8087 |
| carreraUrl | 8088 |
| cursoUrl | 5255 |
| grupoUrl | 5256 |
| periodoUrl | 5262 |
| direccionUrl | 5227 |
| expedienteUrl | 8084 |
| prematriculaUrl | 8089 |
| historialAcademicoUrl | 8090 |
| listadoEstudiantesUrl | 8091 |
| matriculaUrl | 8085 |
| notasUrl | 8092 |
| facturaUrl | 8093 |
| pagoUrl | 8094 |
| notificacionUrl | 8095 |

Todas las URLs predeterminadas usan `http://localhost:<puerto>`, segun
`tools/servicios.json`. Se reemplazaron las URLs agrupadas `baseUrl`,
`seguridadUrl`, `persona2Url` y las variantes `*BaseUrl`.

## Datos y resultados esperados

Los escenarios conservan sus datos y sus aserciones; para repetirlos deben existir
los estudiantes, cursos, grupos, roles y parametros que referencian. Las pruebas
de prematricula requieren los periodos futuro, actual y pasado configurados en
sus variables. Una fecha que era futura cuando se creo la coleccion puede haber
dejado de serlo. Las altas y bajas del Runner escriben en las bases de negocio;
las peticiones `/notificar` envian correo segun la configuracion del servicio.

Con las dependencias disponibles, se mantienen las aserciones funcionales
existentes. Si una API requerida esta caida, puede responder 503; los borrados de
registros referenciados pueden responder 409. Pago conserva automaticamente las
operaciones pendientes para recuperarlas cuando vuelva Factura. No se cambiaron
las aserciones para aceptar esos fallos como resultados satisfactorios.

En Persona 5 configuro `cursoId`, `grupoId` y `periodoId` antes de ejecutar;
el alta de matrícula conserva esos valores. Para modificar desgloses, primero
compruebo que el grupo está dedicado a estas pruebas y establezco manualmente
`grupoPruebasConfirmado=true` en el entorno seleccionado. La colección no activa
esa confirmación automáticamente. Los IDs de matrícula, rubros, factura y pago
se obtienen de sus altas; si falta alguno, la petición se detiene con un mensaje
que indica el paso requerido. Para solicitudes individuales puedo configurar
explícitamente un ID existente adecuado al escenario.

## Validacion de esta actualizacion

Ejecutar desde la raiz:

```powershell
node docs/primer_avance/pruebas/postman/validar-colecciones.cjs
node --check docs/primer_avance/pruebas/postman/ejecutar-http.cjs
node --test docs/primer_avance/pruebas/postman/verificar-flujos.cjs
```

El validador revisa el JSON, la correspondencia de cada ruta con su servicio,
los puertos contra `tools/servicios.json`, las variables de ambos entornos y la
sintaxis JavaScript de los eventos. El reporte esta en
`docs/primer_avance/pruebas/postman-validacion.json`. Esta comprobacion es estatica: no ejecuta
los runners, no envia solicitudes HTTP ni correo y no modifica datos de negocio.
Los resultados historicos que figuran en las descripciones de las colecciones
corresponden a la arquitectura anterior.
