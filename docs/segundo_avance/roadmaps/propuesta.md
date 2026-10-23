## Criterio usado para los history points

Adopto la escala de Planning Poker de la imagen: **0, ½, 1, 2, 3, 5, 8, 13 y 21**, junto con las cartas especiales **∞, ? y café**. Estimo el incremento V2: interfaz, integración, validaciones, permisos, bitácora y evidencia de cada HU.

| Puntos | Complejidad | Criterio propuesto para el equipo |
|---:|---|---|
| 0 | Sin esfuerzo adicional | Criterio ya satisfecho que no requiere trabajo nuevo. |
| ½ | Mínima | Ajuste trivial y acotado, sin reglas nuevas. |
| 1 | Muy baja | Cambio pequeño o consulta sencilla con alcance claro. |
| 2 | Baja | Tarea sencilla con pocas validaciones e integración conocida. |
| 3 | Baja a media | Mantenimiento sencillo con servicio disponible. |
| 5 | Media | Formulario con relaciones, filtros, exportación o navegación dependiente. |
| 8 | Alta | Sesión, permisos, infraestructura compartida o flujo con varias reglas e integraciones. |
| 13 | Muy alta | Historia grande con varias integraciones o incertidumbre importante; revisar su división en tareas. |
| 21 | Excepcional | Historia demasiado amplia para planificar con confianza; aclarar alcance y descomponer antes de comprometerla. |

Las cartas especiales no suman puntos: **∞** indica que no podemos estimar razonablemente el alcance actual; **?** solicita aclaraciones para estimar; **café** propone una pausa en la sesión. Estos significados y criterios son el acuerdo de uso que propongo para el equipo.

Las estimaciones existentes de 3, 5 y 8 son válidas en esta escala. Por ello conservo los puntos de las 33 HU y la distribución de **50, 49, 49 y 49 puntos**. Ampliar la escala no obliga a utilizar todos sus valores ni a cambiar la complejidad estimada de las historias.

Los puntos expresan complejidad relativa, no horas ni fechas. Se revisarán al contrastar los criterios V2 con los contratos existentes. Las tareas técnicas se vinculan a su HU; no se contabilizan como historias adicionales.

## Propuesta de asignación y puntos

| Responsable | HU    | Historia resumida                      | Base V1                                        | Puntos | Justificación breve                                                    |
| ----------- | ----- | -------------------------------------- | ---------------------------------------------- | -----: | ----------------------------------------------------------------------- |
| Persona 1 (Jose)   | ADM1  | Plantilla web reutilizable             | USR2, USR4, GEN1                               |      8 | Layout responsive, menú por rol y navegación compartida.              |
| Persona 1 (Jose)   | ADM2  | Login, refresh y cierre de sesión web | USR5                                           |      8 | Sesión transversal, expiración y tratamiento de errores.              |
| Persona 1 (Jose)   | ADM5  | Roles y matriz de permisos             | USR2, USR4                                     |      8 | Asociación persistida rol–módulo y cambios auditados.                |
| Persona 1 (Jose)   | ADM6  | Parámetros                            | USR3                                           |      3 | CRUD paginado con restricciones conocidas.                              |
| Persona 1 (Jose)   | ADM7  | Módulos del menú                     | USR4, USR2                                     |      5 | Estado, orden y asociación con roles.                                  |
| Persona 1 (Jose)   | ADM23 | Consulta de bitácoras                 | GEN1                                           |      5 | Filtros, paginación y JSON antes/después, solo administrador.         |
| Persona 1 (Jose)   | ADM24 | Perfil y cambio de contraseña         | USR1, USR3, USR5                               |      5 | Política configurable y limpieza de sesiones en cliente.               |
| Persona 1 (Jose)   | GTW2  | Validación centralizada de token      | USR5                                           |      8 | Interceptación, excepciones públicas y rechazo 401.                   |
| Persona 2 (Ramses)   | ADM3  | Dashboard por rol                      | USR1, ACD2–ACD5, MAT2, MAT5, IPN1, IPN3, GEN1 |      8 | Métricas y paneles compuestos de varios servicios.                     |
| Persona 2 (Ramses)   | ADM4  | Usuarios                               | USR1, USR2, USR4                               |      8 | Filtros, dominios por rol, contraseña opcional al editar y auditoría. |
| Persona 2 (Ramses)   | ADM8  | Instituciones                          | ACD1                                           |      3 | CRUD, búsqueda y bloqueo por carreras relacionadas.                    |
| Persona 2 (Ramses)   | ADM9  | Carreras                               | ACD2, ACD1, ACD6                               |      5 | Selectores de institución/director y relaciones con cursos.            |
| Persona 2 (Ramses)   | ADM10 | Cursos                                 | ACD3, ACD2                                     |      5 | Carrera/nivel y bloqueo de eliminación por grupos.                     |
| Persona 2 (Ramses)   | ADM11 | Profesores                             | ACD6                                           |      5 | Edad, dominio, teléfonos, filtros y grupos activos.                    |
| Persona 2 (Ramses)   | ADM12 | Periodos                               | ACD5                                           |      5 | Fechas, estados derivados y validación de solapamientos.               |
| Persona 2 (Ramses)   | ADM13 | Grupos                                 | ACD4, ACD3, ACD5, ACD6                         |      5 | Curso/periodo/profesor, unicidad y matrícula asociada.                 |
| Persona 2 (Ramses)   | MOB6  | Mis cursos                             | MAT2, ACD3–ACD6                               |      5 | Listado, búsqueda, actualización y detalle del curso/grupo.           |
| Persona 3 (Alejandro)   | ADM14 | Prematrículas                         | MAT1                                           |      5 | Filtros, periodo futuro y duplicados.                                   |
| Persona 3 (Alejandro)   | ADM15 | Matrícula guiada                      | MAT2, MAT3, ACD3–ACD5                         |      8 | Selección encadenada, periodo activo, duplicados y CSV.                |
| Persona 3 (Alejandro)   | ADM16 | Expedientes                            | MAT3, MAT4                                     |      8 | Formulario extenso y selectores territoriales dependientes.             |
| Persona 3 (Alejandro)   | ADM18 | Historial académico                   | ACA1                                           |      5 | Promedios, filtros y exportación CSV/PDF.                              |
| Persona 3 (Alejandro)   | ADM19 | Listados por periodo                   | ACA2                                           |      5 | Filtros encadenados, orden, paginación y CSV/PDF.                      |
| Persona 3 (Alejandro)   | MOB3  | Mi expediente                          | MAT3, MAT4                                     |      5 | Campos editables limitados y dirección jerárquica.                    |
| Persona 3 (Alejandro)   | MOB4  | Prematrícula móvil                   | MAT1, ACD2, ACD3, ACD5, IPN3                   |      5 | Selección, confirmación y notificación opcional.                     |
| Persona 3 (Alejandro)   | MOB5  | Matrícula móvil                      | MAT2, ACD3–ACD5                               |      8 | Curso/grupo, periodo activo y confirmación sin duplicados.             |
| Persona 4 (Fabian)   | ADM17 | Desglose y notas                       | MAT5                                           |      8 | Suma 100, edición por rubro y bloqueo cuando hay notas.                |
| Persona 4 (Fabian)   | ADM20 | Facturas                               | IPN1, MAT2                                     |      5 | Cálculo, encabezado/detalle, estados y reverso con motivo.             |
| Persona 4 (Fabian)   | ADM21 | Pagos                                  | IPN2, IPN1                                     |      5 | Monto/estado, reverso y actualización de factura.                      |
| Persona 4 (Fabian)   | ADM22 | Notificaciones                         | IPN3, USR3                                     |      5 | HTML sanitizado, confirmación e historial de envíos.                  |
| Persona 4 (Fabian)   | MOB0  | Plantilla móvil reutilizable          | Navegación V2                                 |      5 | Barra superior, tabs, mensajes y confirmaciones comunes.                |
| Persona 4 (Fabian)   | MOB1  | Login móvil                           | USR5, USR3, GEN1                               |      8 | Credenciales seguras, recordar sesión y bitácora.                     |
| Persona 4 (Fabian)   | MOB2  | Resumen del estudiante                 | MAT1, MAT2, IPN1, IPN2                         |      5 | Tarjetas y accesos con datos del usuario autenticado.                   |
| Persona 4 (Fabian)   | GTW1  | Enrutamiento del gateway               | Servicios V1                                   |      8 | Entrada única, rutas y verbos de todos los dominios.                   |

## Resumen por integrante

| Persona         | Historias asignadas                                      |     Cantidad | Total de puntos |
| --------------- | -------------------------------------------------------- | -----------: | --------------: |
| Persona 1 (Jose)       | ADM1, ADM2, ADM5, ADM6, ADM7, ADM23, ADM24, GTW2         |            8 |              50 |
| Persona 2 (Ramses)       | ADM3, ADM4, ADM8, ADM9, ADM10, ADM11, ADM12, ADM13, MOB6 |            9 |              49 |
| Persona 3 (Alejandro)       | ADM14, ADM15, ADM16, ADM18, ADM19, MOB3, MOB4, MOB5      |            8 |              49 |
| Persona 4 (Fabian)       | ADM17, ADM20, ADM21, ADM22, MOB0, MOB1, MOB2, GTW1       |            8 |              49 |
| **Total** | **24 ADM + 7 MOB + 2 GTW**                         | **33** |   **197** |

La propuesta distribuye el trabajo V2 entre **Persona 1–4**, con nombres asignados por el equipo. Las asignaciones del primer avance se conservan como antecedente histórico.

El promedio es de **49,25 puntos por persona**. Las cargas quedan en **50, 49, 49 y 49 puntos**, con una diferencia máxima de **1 punto**, la menor posible con las estimaciones actuales de las HU completas. Mantengo las estimaciones de cada HU y agrupo por afinidad: seguridad y plantilla web; oferta académica y dashboard; gestión del estudiante; evaluación, finanzas y base móvil.

Persona 1 (Jose) coordina sesión web, permisos y seguridad del gateway; Persona 4 (Fabian) coordina routing, plantilla y sesión móvil. Ambas acuerdan temprano el contrato común. Todas las personas documentan y prueban sus HU; la igualdad de puntos estima complejidad, no garantiza igual duración.

## Decisiones técnicas que propongo

- Web: **React con TypeScript**, una de las opciones permitidas en el PDF. La alternativa permitida es Blazor WebAssembly; la elección propuesta aún debe acordarse con el equipo.
- Móvil: **React Native con TypeScript**, como propuesta multiplataforma para compartir conocimiento con web. Las pantallas móviles se estiman y prueban por separado.
- Gateway: **YARP**, permitido junto con Ocelot por el PDF. GTW1 y GTW2 comparten una sola aplicación y configuración de rutas.
- Servicios y datos: continuar las tecnologías entregadas. La guía de integración documenta 22 aplicaciones y bases independientes; esta planificación no propone volver a una base compartida ni migrar todos los servicios a un único lenguaje.
- Consumo de clientes: web y móvil llaman al gateway; las rutas internas y claves de servicio permanecen fuera de los clientes.

Estas elecciones son propuestas de planificación basadas en las restricciones del PDF (p. 21), no una verificación de versiones o compatibilidad de librerías.

## Adaptaciones V2 que debemos contrastar

Una pantalla nueva no garantiza que el servicio V1 ya entregue todos sus datos. Antes de implementar, propongo registrar por contrato qué se reutiliza y qué falta, sin declarar ausencias por una auditoría antigua.

| Área            | Necesidad V2 que debemos contrastar                                                        | Coordinación                                |
| ---------------- | ------------------------------------------------------------------------------------------ | -------------------------------------------- |
| Menú y roles    | Asociación rol–módulo/opción, permisos, estado y orden; lectura al iniciar sesión.    | Persona 1 (Jose); ADM1, ADM5, ADM7.                 |
| Sesión y perfil | Datos de usuario/rol, cambio de contraseña y política parametrizable.                    | Personas 1 (Jose) y 4 (Fabian); ADM2, ADM24, MOB1.           |
| Dashboard        | Métricas por rol, grupos propios, notificaciones recientes y últimas bitácoras.         | Persona 2 (Ramses) con propietarios de datos; ADM3.   |
| Mantenimientos   | Filtros, paginación, orden, metadatos de última modificación y bloqueos por relaciones. | Cada responsable V2 por dominio.             |
| Académico       | Filtros por periodo/año, exportaciones y vínculo usuario–profesor/estudiante.           | Personas 2 (Ramses), 3 (Alejandro) y 4 (Fabian); ADM18, ADM19, MOB2, MOB6. |
| Finanzas         | Motivos de reverso, filtros y estados coherentes entre pago/factura.                       | Persona 4 (Fabian); ADM20, ADM21.                     |
| Correo           | Historial y configuración desde parámetros, además de sanitización de HTML.            | Personas 4 (Fabian), 1 (Jose) y 2 (Ramses); ADM22, ADM3.              |
| Bitácora        | Filtros por fecha/usuario/acción/módulo y detalle antes/después.                        | Persona 1 (Jose) con cada servicio; ADM23.          |

Cada ampliación debe vincularse al criterio V2 que la solicita, documentar su contrato y conservar la propiedad de datos por servicio. Si requiere cambios de BD, el PDF pide gestionar su aprobación en Teams, preparar el script y solicitar al profesor su aplicación (p. 21); esta propuesta no aplica cambios de BD.

## Puntos que dejo pendientes de definición

| Punto                                                                             | Tratamiento inicial propuesto                                                                                                                                                    |
| --------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Separación segundo/tercer avance                                                 | Confirmar con el profesor qué HU entran en cada entrega; mantener visibles las 33 y sus responsables. No fijar una fecha calendario a partir de las semanas del PDF.            |
| GTW2 cita SRV1–SRV19 y una excepción SRV7 no definidas en V2                    | Elaborar tabla explícita de rutas públicas/protegidas. Proponer login/refresh públicos y recursos de negocio protegidos; confirmar qué representa SRV7 antes de cerrar GTW2. |
| Rutas de direcciones V2 por query frente a rutas V1 por segmentos                 | Documentar el contrato externo del gateway y su correspondencia con el servicio, sin cambiar silenciosamente a los consumidores.                                                 |
| ADM6 menciona cambios de permisos en parámetros                                  | Proponer auditoría antes/después del parámetro y confirmar la redacción con el profesor.                                                                                     |
| ADM4 permite modal y el flujo general describe formularios en pantalla            | Adoptar listado → formulario crear/editar → listado, con confirmación de eliminación (pp. 17–19).                                                                           |
| Tabs/accesos móviles de Pagos, Perfil, Notas e Historial sin HU móvil detallada | Confirmar el comportamiento esperado; no inventar operaciones nuevas ni marcar esos destinos como entregados con enlaces vacíos.                                                |
| Formato local de teléfonos y formato estándar de horario                        | Acordar ejemplos válidos/inválidos para las pruebas de ADM11 y ADM13.                                                                                                          |

## Nota final

Presento esta estimación como punto de partida para la reunión del equipo. El siguiente paso de planificación es confirmar el corte de entrega, validar los contratos necesarios y revisar el reparto con base en las dependencias del [roadmap 2](<roadmap%202.md>). Mantengo el primer avance como antecedente cerrado y toda la nueva planificación dentro de `docs/segundo_avance/`.
