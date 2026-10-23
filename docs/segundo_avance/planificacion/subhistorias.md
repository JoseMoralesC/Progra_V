# Subhistorias evaluables para el reparto de 30 puntos

El 23 de octubre de 2026 Jose confirmo que el profesor permite subhistorias
evaluables con criterios y responsables distintos, manteniendo la trazabilidad
a la HU original. Esta descomposicion conserva los 240 puntos y todo el alcance.
La posibilidad de dividir no significa que las estimaciones ya tengan nota.

## Por que se divide

Con las historias indivisibles actuales no se pueden formar ocho grupos de
30 puntos: solo hay dos historias de 3 puntos y trece de 8. Con esos valores,
un grupo de 30 solo puede ser seis historias de 5 o tres de 8 mas dos de 3.
Las dos historias de 3 no alcanzan para distribuir las trece de 8 de esa forma.

Dividir cuatro historias de 8 en bloques funcionales de 5 + 3 permite el balance
sin modificar estimaciones. Es el minimo si se conserva el corte A2/A3 y solo
se usa esa descomposicion: dos divisiones en A2 y dos en A3.

| Padre | Avance | Subhistoria de 5 puntos | Subhistoria de 3 puntos | Coordinador de integracion |
|---|---:|---|---|---|
| ADM3 | 2 | ADM3-A: dashboard administrativo y componentes comunes; Fabian. | ADM3-P: dashboard del profesor; Jose. | Fabian |
| ADM17 | 2 | ADM17-R: rubros y desglose; Fabian. | ADM17-N: asignacion y consulta de notas; Jose. | Fabian |
| MOB1 | 3 | MOB1-A: formulario y autenticacion; Jose. | MOB1-S: almacenamiento seguro y ciclo de sesion; Alejandro. | Jose |
| MOB5 | 3 | MOB5-S: seleccion de curso/grupo y resumen; Fabian. | MOB5-C: confirmacion y registro de matricula; Alejandro. | Fabian |

Los coordinadores no reciben otros puntos por integrar. Cada subhistoria tiene
un autor responsable, criterios y evidencia propios. El padre se cierra cuando
ambas subhistorias y su prueba conjunta cumplen el criterio original completo.

## ADM3-A - Dashboard administrativo y componentes comunes (5)

Fuente: ADM3, PDF pp. 5-6. Responsable: Fabian.

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

Los componentes comunes se desarrollan aqui una vez. ADM3-P los consume y
acredita su comportamiento con un profesor, sin volver a sumar su construccion.

## ADM3-P - Dashboard del profesor (3)

Fuente: la vista de profesor requerida por ADM3, PDF p. 6. Responsable: Jose.

1. Presenta saludo/rol y grupos/cursos impartidos por ese profesor en el periodo.
2. Permite acceder al desglose/notas de sus grupos mediante ADM17-R/ADM17-N.
3. Integra notificaciones y bitacoras relevantes, ayuda/soporte y perfil sobre
   los componentes de ADM3-A; prueba la visualizacion y su registro.
4. Rechaza consultas de grupos o datos restringidos de otro profesor mediante
   URL/API y muestra correctamente vacio y dependencia caida.

Esta vista pertenece a la web administrativa con acceso por rol que describe
el PDF. No crea una tercera web ni cambia el portal exclusivo de estudiantes.

## ADM17-R - Rubros y desglose (5)

Fuente: ADM17, PDF p. 11. Responsable: Fabian.

1. Carga rubros para un grupo autorizado mediante el contrato de `/cargardesglose`.
2. Exige una suma de 100 y rechaza datos invalidos sin cambios parciales.
3. Permite reemplazar un desglose existente solo cuando no tiene notas.
4. Consulta `/obtenerdesglose`, presenta los estados sin desglose/con desglose
   e impide modificar rubros cuando existen notas.
5. Registra consultas, cambios y errores, incluyendo el JSON exigido; prueba
   permisos sobre el grupo y respuestas de dependencias.

## ADM17-N - Asignacion y consulta de notas (3)

Fuente: ADM17, PDF p. 11. Responsable: Jose.

1. Permite crear o modificar notas por rubro con el verbo correspondiente de
   `/asignarnotarubro` y el contrato validado del servicio.
2. Valida nota entre 1 y 100, matricula/rubro/grupo correspondientes y permisos
   del profesor; no permite escribir notas a un estudiante autenticado.
3. Consulta `/obtenernotas` y presenta las vistas por grupo y estudiante,
   incluyendo sin notas y cierres de edicion segun las reglas acordadas.
4. Registra consultas/altas/cambios/errores con su detalle requerido.
5. Prueba junto con ADM17-R que asignar la primera nota bloquea el reemplazo
   posterior de rubros y que una nota invalida no produce ese bloqueo.

La estimacion de 3 aprovecha los servicios MAT5 ya construidos y los componentes
de desglose de ADM17-R. Si aparecen adaptaciones de backend no previstas, se
reestima con el equipo; no se ocultan dentro de la otra subhistoria.

## MOB1-A - Formulario y autenticacion movil (5)

Fuente: MOB1, PDF pp. 13-14. Responsable: Jose.

1. Muestra el formulario, valida email y dominio parametrizable (por defecto
   cuc.cr), y exige contrasena no vacia.
2. Consume POST `/login`, interpreta access_token, expires_in, refresh_token y
   usuarioID, y entrega esos datos al contrato de sesion de MOB1-S.
3. Ante credenciales invalidas muestra el mensaje requerido por el PDF; controla
   errores de red y no navega como autenticado sin respuesta valida.
4. Registra intento de login exitoso/fallido y demuestra el ingreso al resumen
   con la sesion de MOB1-S.

## MOB1-S - Persistencia segura y ciclo de sesion (3)

Fuente: MOB1, PDF p. 14. Responsable: Alejandro.

1. Recibe del contrato MOB1-A los tokens/identidad y los almacena de forma segura
   mediante Keychain/Keystore o mecanismo seguro equivalente de la plataforma.
2. Implementa Recordarme y su restauracion de acuerdo con la vigencia de sesion;
   comprueba que otra cuenta no herede credenciales ni datos de la anterior.
3. Cerrar sesion limpia credenciales locales y estado en memoria, impide volver
   a pantallas protegidas desde el historial y registra logout.
4. Integra con MOB1-A login -> persistencia -> reapertura -> salida, sin exponer
   tokens en logs, capturas o almacenamiento comun sin proteccion.

Se reutilizan el backend de sesion y el flujo de login. Esta subhistoria no
desarrolla otro formulario ni promete un endpoint de revocacion no exigido.

## MOB5-S - Seleccion movil y resumen de matricula (5)

Fuente: MOB5, PDF p. 15. Responsable: Fabian.

1. Ofrece cursos y grupos disponibles en periodos activos mediante servicios reales.
2. La seleccion encadena curso/grupo y muestra sus datos antes de confirmar.
3. Comprueba correspondencia curso/grupo/periodo y muestra duplicados conocidos
   o errores de consulta; la validacion definitiva permanece en servidor.
4. Entrega a MOB5-C un resumen identificado para confirmar, permite regresar y
   corregir la seleccion, y registra las consultas que correspondan.

## MOB5-C - Confirmacion y registro de matricula movil (3)

Fuente: MOB5, PDF p. 15. Responsable: Alejandro.

1. Confirma expresamente el resumen de MOB5-S antes de enviar la inscripcion.
2. El servidor usa la identidad propia, valida periodo activo y evita duplicados
   de curso/grupo aunque cambie el estado entre seleccion y confirmacion.
3. Controla repeticion de envio, error funcional, expiracion y dependencia caida
   sin mostrar exito ni generar una matricula ajena.
4. Muestra el resultado persistido, navega al detalle de matricula y registra
   la operacion con el detalle de bitacora requerido.
5. Prueba con MOB5-S el flujo completo y su fallo tras elegir un grupo que deja
   de ser elegible; no se acredita la matricula solo con un resumen en pantalla.

Esta parte reutiliza las reglas y la escritura propia que Alejandro entrega en
WEB5, junto con MAT2. MOB5-S conserva su interfaz de seleccion; no se cuenta dos
veces la misma API de inscripcion.

## Registro y calificacion

- El tablero contiene 41 padres de origen; cuatro son contenedores con dos
  unidades hijas evaluables. Las 37 historias no divididas siguen evaluandose enteras.
- Se planifican 45 unidades: 22 en A2 y 23 nuevas en A3, por 120 puntos cada avance.
- Registrar responsable y evidencia por ID completo, por ejemplo ADM17-N.
- No sumar 8 del padre ademas de los 5 + 3 de sus hijos, ni repetir su nota.
- La formula de nota se aplica segun la rubrica que comunique el profesor;
  permitir subhistorias no autoriza a inventar esa formula.
- Las pruebas de integracion se enlazan desde ambos hijos. Si uno bloquea al
  otro, registrar causa y evidencia individual; no declarar cerrado el padre.
