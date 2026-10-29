# Criterios de las historias indivisibles

Reajuste del 28 de octubre de 2026. El profesor no permite dividir historias ni
asignar una historia entre dos personas. Este documento sustituye la propuesta
de subhistorias del 23 de octubre; conserva la ruta para mantener los enlaces.
Los IDs con sufijo dejan de ser unidades evaluables. El [backlog](backlog.md)
gobierna los responsables, avances y puntos.

Los apartados internos son criterios de una misma historia, sin puntos ni
responsables separados. Se conservan las validaciones, permisos, bitacoras y
pruebas del alcance anterior; el PDF sigue siendo la fuente de los criterios
oficiales completos.

## ADM3 - Historia completa (8 puntos; avance 2)

Responsable unico: Fabian. Todos los apartados siguientes pertenecen a esta HU.

### Dashboard administrativo y componentes comunes

1. Muestra saludo, rol y atajos administrativos autorizados.
2. Obtiene conteos reales de usuarios/carreras/cursos, periodos activos y
   facturas pendientes de los servicios propietarios.
3. Implementa los componentes y contrato de datos para notificaciones recientes
   e informacion de bitacora pertinente al rol; no expone la consulta global
   administrativa de bitacoras a otros perfiles.
4. Ofrece accesos funcionales a ayuda/soporte y perfil (ADM24), y registra la
   visualizacion del dashboard.
5. Distingue carga, vacio y fallo de dependencia; aplica permisos por rol al
   obtener los datos, ademas de construir el menu.

### Dashboard del profesor

1. Presenta saludo/rol y grupos/cursos impartidos por ese profesor en el periodo.
2. Permite acceder al desglose/notas de sus grupos mediante ADM17.
3. Integra notificaciones y bitacoras relevantes, ayuda/soporte y perfil sobre
   los componentes de ADM3; prueba la visualizacion y su registro.
4. Rechaza consultas de grupos o datos restringidos de otro profesor mediante
   URL/API y muestra correctamente vacio y dependencia caida.

## ADM17 - Historia completa (8 puntos; avance 2)

Responsable unico: Jose. Todos los apartados siguientes pertenecen a esta HU.

### Rubros y desglose

1. Carga rubros para un grupo autorizado mediante el contrato de `/cargardesglose`.
2. Exige una suma de 100 y rechaza datos invalidos sin cambios parciales.
3. Permite reemplazar un desglose existente solo cuando no tiene notas.
4. Consulta `/obtenerdesglose`, presenta los estados sin desglose/con desglose
   e impide modificar rubros cuando existen notas.
5. Registra consultas, cambios y errores, incluyendo el JSON exigido; prueba
   permisos sobre el grupo y respuestas de dependencias.

### Asignacion y consulta de notas

1. Permite crear o modificar notas por rubro con el verbo correspondiente de
   `/asignarnotarubro` y el contrato validado del servicio.
2. Valida nota entre 1 y 100, matricula/rubro/grupo correspondientes y permisos
   del profesor; no permite escribir notas a un estudiante autenticado.
3. Consulta `/obtenernotas` y presenta las vistas por grupo y estudiante,
   incluyendo sin notas y cierres de edicion segun las reglas acordadas.
4. Registra consultas/altas/cambios/errores con su detalle requerido.
5. Prueba que asignar la primera nota bloquea el reemplazo
   posterior de rubros y que una nota invalida no produce ese bloqueo.

## MOB1 - Historia completa (8 puntos; avance 3)

Responsable unico: Jose. Todos los apartados siguientes pertenecen a esta HU.

### Formulario y autenticacion movil

1. Muestra el formulario, valida email y dominio parametrizable (por defecto
   cuc.cr), y exige contrasena no vacia.
2. Consume POST `/login`, interpreta access_token, expires_in, refresh_token y
   usuarioID, y entrega esos datos al manejo de sesion de la misma historia.
3. Ante credenciales invalidas muestra el mensaje requerido por el PDF; controla
   errores de red y no navega como autenticado sin respuesta valida.
4. Registra intento de login exitoso/fallido y demuestra el ingreso al resumen
   con la sesion segura de la misma historia.

### Persistencia segura y ciclo de sesion

1. Recibe del login de la misma historia los tokens/identidad y los almacena de forma segura
   mediante Keychain/Keystore o mecanismo seguro equivalente de la plataforma.
2. Implementa Recordarme y su restauracion de acuerdo con la vigencia de sesion;
   comprueba que otra cuenta no herede credenciales ni datos de la anterior.
3. Cerrar sesion limpia credenciales locales y estado en memoria, impide volver
   a pantallas protegidas desde el historial y registra logout.
4. Prueba login -> persistencia -> reapertura -> salida, sin exponer
   tokens en logs, capturas o almacenamiento comun sin proteccion.

## MOB5 - Historia completa (8 puntos; avance 3)

Responsable unico: Alejandro. Todos los apartados siguientes pertenecen a esta HU.

### Seleccion movil y resumen de matricula

1. Ofrece cursos y grupos disponibles en periodos activos mediante servicios reales.
2. La seleccion encadena curso/grupo y muestra sus datos antes de confirmar.
3. Comprueba correspondencia curso/grupo/periodo y muestra duplicados conocidos
   o errores de consulta; la validacion definitiva permanece en servidor.
4. Prepara un resumen identificado para confirmar, permite regresar y
   corregir la seleccion, y registra las consultas que correspondan.

### Confirmacion y registro de matricula movil

1. Confirma expresamente el resumen de seleccion antes de enviar la inscripcion.
2. El servidor usa la identidad propia, valida periodo activo y evita duplicados
   de curso/grupo aunque cambie el estado entre seleccion y confirmacion.
3. Controla repeticion de envio, error funcional, expiracion y dependencia caida
   sin mostrar exito ni generar una matricula ajena.
4. Muestra el resultado persistido, navega al detalle de matricula y registra
   la operacion con el detalle de bitacora requerido.
5. Prueba el flujo completo y su fallo tras elegir un grupo que deja
   de ser elegible; no se acredita la matricula solo con un resumen en pantalla.

## Reutilizacion y limites del alcance

La vista del profesor pertenece a la web administrativa por rol; no crea una
tercera web. Los componentes comunes de ADM3 se construyen una vez. ADM17
reutiliza los servicios MAT5 y mantiene integrados los rubros y las notas.
MOB1 reutiliza el backend de sesion y no presupone un endpoint de revocacion
adicional. MOB5 reutiliza las reglas y escritura propia de WEB5/MAT2, sin
contar dos veces esa API; conserva interfaz y pruebas moviles propias.

## Registro y cierre

- 41 historias evaluables: 20 en A2 y 21 nuevas en A3, por 120 puntos cada avance.
- Registrar una ficha, un responsable y evidencia por ID original: ADM3, ADM17,
  MOB1 o MOB5. No sumar otra vez sus antiguos bloques de 5 + 3.
- Cada responsable demuestra el flujo completo, sus errores e integracion.
  Una parte implementada no cierra la historia.
- Los aportes tecnicos previos conservan su autoria historica; no convierten la
  HU en una responsabilidad compartida ni acreditan por si solos su cierre.
- La nota se registra cuando el profesor comunique su rubrica.
