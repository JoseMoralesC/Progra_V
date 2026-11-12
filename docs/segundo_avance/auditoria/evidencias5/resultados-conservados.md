# Resultados conservados en Markdown

Fecha de organización: 12 de noviembre de 2026.

Como equipo decidimos conservar los informes y las capturas para presentar la auditoría. Trasladé aquí los casos y sus estados antes de retirar los archivos auxiliares. Esta organización no representa una nueva ejecución ni cambia el avance de los aportes. Los antecedentes y las repeticiones siguen separados; no deben sumarse entre sí.

El [informe de evidencias](README.md) explica las condiciones, los fallos y los límites de aceptación. Ya no conservamos en esta carpeta las trazas completas ni los ejecutores temporales.

## resultados-actualizados

Procedencia: `resultados_actualizados.json`, retirado después de esta transcripción.

Fecha: 2026-11-12

Criterio: Sustituyo las ejecuciones anteriores de Notas/contratos y navegador de Jose por las nuevas. Conservo .NET y Node de Jose y el último resultado disponible de los demás integrantes. Los recorridos reales se informan aparte, sin duplicar casos de navegador incluidos en su comprobación global.

Resumen registrado por integrante (conservo las exclusiones del informe original):

| Integrante | Resultado registrado |
| --- | --- |
| Alejandro | {"aprobada": 225, "fallida": 6, "omitida": 0, "total": 231, "porcentaje_aprobado": 97.4} |
| Fabian | {"aprobada": 14, "fallida": 6, "omitida": 0, "total": 20, "porcentaje_aprobado": 70.0} |
| Jose | {"aprobada": 253, "fallida": 0, "omitida": 0, "total": 253, "porcentaje_aprobado": 100.0} |
| Ramses | {"aprobada": 61, "fallida": 0, "omitida": 0, "total": 61, "porcentaje_aprobado": 100.0} |

Transcribí **567 entradas**. Las entradas excluidas o de preparación no equivalen a casos incluidos en las barras.

| Integrante | Grupo | Caso | Estado | Observación registrada |
| --- | --- | --- | --- | --- |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.IntegracionCompartidaTest.cargaBibliotecaYContratoConSpringBootDelServicioSinBaseReal | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.NuevoAvatarPersona2ApplicationTests.contextLoads | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.aceptaBearerValidadoPorSeguridad | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.rechazaSolicitudSinBearer | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.obtieneElSujetoSoloDespuesDeValidarElBearer | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.registraAmbosEstadosYNoConfirmaCambiosFallidos | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaLasTresCredencialesDelCambioDeContrasena | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaSecretosConComillasEscapadasYCamposAnidados | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.conservaJsonSinSecretos | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaContrasenaYConservaDatosDelRegistro | aprobada |  |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.actualizaSoloElNombreDeLaCuentaDerivadaDelSujeto | aprobada |  |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.cambiaContrasenaDelSujetoYGuardaSoloHashBcrypt | fallida |  |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.validaLongitudMayusculaMinusculaNumeroYCaracterEspecialEnServidor | fallida |  |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.rechazaContrasenaActualIncorrectaSinPersistir | fallida |  |
| Ramses | Java Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.permiteCuandoTodosLosPeriodosEstanCerrados | aprobada |  |
| Ramses | Java Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.registraSnapshotsJsonEnBitacoraDeProfesor | aprobada |  |
| Ramses | Java Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.exigeFormatoLocalDeTelefonosYEvitaDuplicados | aprobada |  |
| Ramses | Java Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.bloqueaCuandoHoyEstaDentroDelPeriodoInclusivo | aprobada |  |
| Ramses | Java Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.bloqueaEliminarCuandoExistenCursos | aprobada |  |
| Ramses | Java Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.validaInstitucionYProfesorEnSusServiciosPropietarios | aprobada |  |
| Ramses | Java Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.registraSnapshotsJsonEnModificacionYEliminacion | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaDistritoDeOtroCanton | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaCantonDeOtraProvincia | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.comunicaFallaDelServicioSinTratarlaComoDireccionInvalida | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaProvinciaInexistente | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.consumeRutasYCamelCaseDelServicioDeAlejandro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[6] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[7] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[8] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[9] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[10] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.metodoNoAdmitidoNoSeReportaComoErrorInterno | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identidadEnQueryNoCambiaElExpedientePropio | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaCualquierCampoProtegidoAunqueElRestoSeaValido | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identificacionDuplicadaAlModificarNoPierdeDatos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaRolIncorrectoVinculoAusenteYAmbiguo | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposEditablesObligatoriosSeValidanAntesDePersistir | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identificacionReferenciadaNoSeCambiaEntreServicios | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.errorTecnicoRegistraBitacoraYRevierteEscritura | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.noEliminaEstudianteReferenciadoNiSusTelefonos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.noPermiteMismoNumeroAunqueCambieElTipo | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.estudiantesDiferentesSoloVenYActualizanSuPropioRegistro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.direccionInvalidaNoEscribeDatos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.conservaCerosInicialesYNoExigeCorreoOTelefonosUnicos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.falloGen1RevierteModificacionYEliminacion | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.dependenciasDeAutenticacionNoDisponiblesDevuelven503 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaDireccionIncoherenteYTokenInvalido | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.sqlNoInterpretaLaIdentificacionComoUnaInstruccion | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.dominioCambiaMedianteParametro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.falloGen1RevierteCreacionYTelefonos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.cicloCompletoDeCincoOperacionesYBitacoras | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[6] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[7] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[8] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[9] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[10] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[11] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[12] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[13] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[14] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[15] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.recursosInexistentesDevuelven404 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.nombreCorreoDominioYFechaInvalidosSeRechazanEnCrearYModificar | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.consultaYActualizaSoloSuExpedienteConPersistenciaYBitacoraMinima | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[6] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[7] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[8] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[9] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[10] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[11] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[12] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[13] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[14] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[15] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[16] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[17] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[18] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[19] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[20] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[21] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[22] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[23] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[24] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[25] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[26] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[27] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[28] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[29] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.noAgregaEdadMinimaNiFormatoDeTelefono | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.utilizaDominioConfiguradoYSinDiferenciarMayusculas | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaExpedienteNulo | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.propagaFalloDelParametro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.conservaUsuarioYTokenValidados | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.responde503CuandoSeguridadNoResponde | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noConfiaEnSubSinValidacionExterna | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.consultaRespetaTextoDelDocumento | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.conservaEstadoAnteriorAunqueElObjetoCambie | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.registraTipoDeErrorSinCopiarMensajeConCredenciales | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.creacionYEliminacionIncluyenRegistro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.validaContratoUsr5 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.comunicaFalloDeConexion | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.enviaBitacoraSinInventarFechaNiIdentificador | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.leeParametroConContratoUsr3 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.distingueFallaDeServicioDeTokenInvalido | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaTokenCon401 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.informaParametroInexistente | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noOcultaFalloAlGuardarBitacora | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.estudiantePuedeCrearConsultarModificarYCancelarSoloConIdentidadDeSesion | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.errorDespuesDeEscribirRevierteYSeRegistraComoMat1 | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.falloGen1RevierteCreacionYCursos | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.noImponeUnicidadNoSolicitadaEntreEstudianteYPeriodo | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.compruebaTodosLosNivelesDesdeLaBase | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.cincoOperacionesConDatosPersistidosYBitacoras | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[5] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[6] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[7] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[8] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[9] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[10] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[11] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[12] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[13] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[14] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[15] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.modificarPermiteCambiarEstudianteYCarrera | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.reevaluaLaFechaParaCadaEscrituraYPermiteConsultarOEliminar | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.referenciasInexistentesNoGuardanDatos | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.endpointsAdministrativosNoPermitenQueUnEstudianteElijaIdentidadAjena | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.falloGen1RevierteModificacionYEliminacion | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.listaVaciaOCursoNuloNoSeAceptan | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.servicioDePrematriculaNoExponeRutasDeExpediente | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.solicitudPropiaNoAceptaIdentidadElegidaYNoExponeSolicitudesDeOtroEstudiante | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesPuedenOmitirseAlCrear | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.recursoAusenteIdInvalidoYMetodoNoAdmitidoTienenRespuestasRest | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.rechazaPeriodoQueIniciaHoyOPasadoAunqueSolicitanteEnvieOtraFecha(int)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.rechazaPeriodoQueIniciaHoyOPasadoAunqueSolicitanteEnvieOtraFecha(int)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.opcionesPropiasSoloIncluyenCursosDePrimerNivelYPeriodosFuturos | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[5] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[6] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[7] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[8] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[9] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[10] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.necesitaFechaDelServidorParaEvaluarPeriodo | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosNulos | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.evaluaFechaDeCadaOperacionSinConservarUnHoyFijo | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.comparaFechasAlCambiarDeAnio | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaUnSoloCursoDePrimerNivel | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[2] | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.guardaHistorialEnErrorCuandoFallaTransporteSmtp | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.registraErrorEnHistorialCuandoFaltanParametrosSmtp | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.informaEnvioRealizadoAunqueFalleGuardadoDeHistorial | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.sanitizacionEliminaScriptsYAtributosPeligrosos | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.rechazaDestinatariosInvalidos | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 13, nombre: "Programación") | aprobada |  |
| Ramses | .NET | PeriodoServiceTest.ContratoActualPermiteFechasIguales | aprobada |  |
| Ramses | .NET | CursoAuditoriaTest.AltaConservaElRegistroPersistidoEnJson | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.EstadoSeDerivaDeFechas | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 0, nombre: "Programación") | aprobada |  |
| Ramses | .NET | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 1, nivel: 13) | aprobada |  |
| Ramses | .NET | NuevoAvatar.Academico.Tests.CursoFiltrosTest.CombinaCarreraYNivelEnRepositorio | aprobada |  |
| Ramses | .NET | CursoAuditoriaTest.EliminacionConservaElRegistroEliminado | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.DosAltasEnLaMismaInstanciaNoDuplicanElPeriodo | aprobada |  |
| Apoyo heredado | .NET | ValidacionesRequestTest.GrupoExigeIdentificadoresHorarioYCupo | Passed |  |
| Ramses | .NET | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 1) | aprobada |  |
| Ramses | .NET | PeriodoServiceTest.RechazaFechaFinalAnteriorAlInicioAntesDeGuardar | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.EditarExcluyeElRegistroPropioYConservaOtrosNumeros | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 12) | aprobada |  |
| Ramses | .NET | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 0, nivel: 1) | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.PeriodoExigeDatos | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 1, nombre: "Programación 5") | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.PermiteFechasSuperpuestasSiElNumeroEsDistinto | aprobada |  |
| Ramses | .NET | CursoAuditoriaTest.ModificacionConservaAnteriorYActual | aprobada |  |
| Apoyo heredado | .NET | GrupoServiceTest.RechazaCursoInexistenteAntesDeGuardar | Passed |  |
| Ramses | .NET | PeriodoReglasTest.DuplicadoProduceConflicto | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "ESTUDIANTE", email: "e@cuc.cr", profesor: null, estudiante: "123") | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/%69nterno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/refresh", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_LeeBearerYDevuelveTrue | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.TokenExpiradoYFirmaAjenaNoSonValidos | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_RechazaDatosVacios | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_RechazaTokenInvalido | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/interno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_DependenciaCaidaNoPermiteLlegarAlDestino | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Sesion_NoConfiaEnHeadersDeIdentidad | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve401ParaCredencialesInvalidas | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/obtenernotas", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: null, valido: True, status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve201ParaCredencialesValidas | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/logout", metodo: "POST", status: 204) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/refresh", metodo: "POST", status: 201) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 201) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "PROFESOR", email: "p@cuc.ac.cr", profesor: 9, estudiante: null) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "ESTUDIANTE", permitido: "PROFESOR", valido: True, status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: False, status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_NoVinculaOtroExpedienteYNoAsumeRolPorId | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer valido", status: 204) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: True, status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/sesion", metodo: "GET", status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer invalido", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta de grupos y periodos") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuNormalNoRegistraVisitaAlDashboard | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/mat/prematricula/propio", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/fin/notificar/historial?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: null, ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Registro sin formato. JSON: {\"ruta\":\"/usuario\""···) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "GET") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/mat/obtenernotas?grupoId=1", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "PUT", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodos", modulo: "ADM12") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: False) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: null, esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: True) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial academico", modulo: "ADM18") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "bitacora") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "estudiante", path: "/gateway/mat/obtenernotas?grupoId=1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "profesor") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "POST", ruta: "/gateway/mat/prematricula/propio") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/interno/datos", status: 404) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 200, invalido: True) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM10", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Referencia a GET /usuario") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "GET", ruta: "/gateway/mat/prematricula/propio") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RoutingDeTodasLasFamiliasTieneDestinoYPolitica | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/historial", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "invalido", ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM22", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/admin/expediente/123", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.AdministradorRecibeDiezEventosDeModulosPermitidosOrdenadosDespuesDeFiltrar | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodo 15", modulo: "ADM12") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM10", metodo: "GET", ruta: "/gateway/admin/carrera") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM11", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM17", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM7", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "DELETE", status: 204) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "profesor", path: "/gateway/admin/usuario") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "DELETE", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM7", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "GET", status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ModuloInactivoNiegaApiYSeRetiraDelMenu | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta de todos los grupos.", modulo: "ADM13") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo 1 de otro módulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora?vista=dashboard&vista=glob"···, esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo desconocido") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "refresh") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.NotificacionesAdministrativasRequierenPermisoDelModulo | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/%69nterno/datos", status: 404) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM12", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "estudiante") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 403, invalido: False) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "POST", status: 201) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/admin/bitacora?vista=dashboard") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 503, invalido: False) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "POST", ruta: "/gateway/admin/institucion") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/fin/notificar/historial?vista=dashboard") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DependenciaAuthCaidaDevuelve503 | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "profesor") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notas") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/expediente/propio", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ServicioNuevoSinContratoDeModuloNoPuedeQuedarExentoAccidentalmente | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta /obtenernotasgrupo", modulo: "ADM17") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "estudiante", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/DOMESTUD") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DestinoCaidoDevuelve502SinConfundirConAuth | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "GET /usuario/ana", modulo: "ADM4") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "login") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notificacion") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta del grupo 12.", modulo: "ADM13") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "PUT") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "admin") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/1?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "{\"descripcion\":\"El usuario consulta /usuario\"}") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/profesor/buscar") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "invalido", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "invalido", estado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "estudiante", estado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM6", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora/1?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "admin") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM7", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "PUT", ruta: "/gateway/admin/profesor/1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "profesor", estado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial académico", modulo: "ADM18") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/profesor") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.MenuPropioTests.UsaElIdRealDelRolDeLaCuentaYNuncaElRolDeLaQuery | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.MenuPropioTests.SinIdentidadNoConsultaNingunRol | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ModuloInactivoNoConcedeDependencias | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/menu/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM10", metodo: "DELETE", ruta: "/gateway/admin/carrera/1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta del grupo 12. JSON: {\"ruta\":\"/usuario\"···) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloDeBitacoraNoConfirmaElRegistroDelDashboard | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "POST", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuPropioNoAceptaRolDeQueryYNuncaRequierePermisoAdministrativo | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "DELETE", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/admin/usuario", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: null, estado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RetirarPermisoBloqueaLaSiguientePeticionInclusoParaAdministrador | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "invalido", esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "PUT", status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta listado de estudiantes", modulo: "ADM19") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.DatosVaciosDevuelvenListasVacias | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/institucion") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.CorreosSeFiltranPorDestinatarioCompletoAntesDelLimite | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorVeAccionesPropiasYGruposAutorizadosSinDatosAdministrativos | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | Node | login conserva contrato real y obtiene perfil del servidor, sin persistir password | aprobada |  |
| Jose | Node | solicitudes simultaneas con token vencido comparten una sola renovacion rotativa | aprobada |  |
| Jose | Node | refresh fallido limpia credenciales y notifica retorno a login | aprobada |  |
| Jose | Node | logout durante refresh no permite restaurar la sesion al finalizar la respuesta | aprobada |  |
| Jose | Node | 401 de negocio renueva una vez; segundo 401 limpia sesion sin bucles | aprobada |  |
| Jose | Node | caida de negocio no elimina sesion ni devuelve datos inventados | aprobada |  |
| Jose | Node | no envia tokens fuera del gateway | aprobada |  |
| Jose | Node | credenciales incorrectas y respuesta incompleta no dejan una sesion nueva | aprobada |  |
| Jose | Node | login sin identidad autorizada o con dependencia caida no conserva tokens parciales | aprobada |  |
| Jose | Node | logout con consulta inicial de identidad en vuelo impide completar el login tardio | aprobada |  |
| Ramses | Node | exige nombre, institución y profesor | aprobada |  |
| Ramses | Node | consume carrera, institución y profesor solo por gateway | aprobada |  |
| Ramses | Node | valida carrera nivel y nombre | aprobada |  |
| Ramses | Node | combina filtros usando gateway | aprobada |  |
| Ramses | Node | normaliza el identificador y conserva el contrato del API | aprobada |  |
| Ramses | Node | rechaza identificador, nombre y orden inválidos | aprobada |  |
| Ramses | Node | solo utiliza rutas del gateway y serializa roles | aprobada |  |
| Ramses | Node | lee tamaño de página desde parámetros con tope máximo de 15 | aprobada |  |
| Ramses | Node | valida destinatarios y contenido | aprobada |  |
| Ramses | Node | usa gateway para historial | aprobada |  |
| Ramses | Node | valida fechas | aprobada |  |
| Ramses | Node | combina filtros | aprobada |  |
| Ramses | Node | valida mayoría de edad, formato local y teléfonos únicos | aprobada |  |
| Ramses | Node | profesores usa únicamente el gateway con paginación | aprobada |  |
| Fabian | principal-ui-final.json | ADM3 incluye facturas pendientes de períodos cerrados en el indicador | aprobada |  |
| Fabian | principal-ui-final.json | ADM3 presenta los grupos activos del profesor | aprobada |  |
| Fabian | principal-ui-final.json | ADM3 informa el fallo de auditoría y de grupos sin ocultar soporte | fallida |  |
| Fabian | principal-ui-final.json | ADM3 muestra al profesor los resúmenes del servidor sin controles de filtrado | aprobada |  |
| Fabian | principal-ui-final.json | ADM8 permite buscar y mantener instituciones desde la sesión | fallida |  |
| Fabian | principal-ui-final.json | ADM8 muestra dependencia y no abre la pantalla sin permiso | fallida |  |
| Fabian | principal-ui-final.json | ADM8 mantiene el listado detrás del modal, valida y cancela sin guardar | fallida |  |
| Fabian | principal-ui-final.json | ADM6 usa el servicio para crear, editar y eliminar parámetros | fallida |  |
| Ramses | principal-ui-final.json | ADM7 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM9 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM10 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM11 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM12 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM22 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM7 respeta TAMPAG y permite navegar sin mostrar más de quince filas | aprobada |  |
| Ramses | principal-ui-final.json | sin ADM7 la ruta directa no consulta ni ofrece escritura de módulos | aprobada |  |
| Ramses | principal-ui-final.json | ADM7 conserva el foco al escribir y muestra validaciones dentro del diálogo | aprobada |  |
| Ramses | principal-ui-final.json | ADM7 muestra el registro guardado en su página y confirma su eliminación | aprobada |  |
| Fabian | principal-ui-final.json | ADM5 guarda un cambio de permisos para un módulo y vuelve a leerlo | aprobada |  |
| Fabian | principal-ui-final.json | ADM4 crea y edita usuarios usando el ID real del rol | fallida |  |
| Ramses | ramses-ui-final.json | el modal bloquea el fondo y restaura el foco al cerrar | aprobada |  |
| Ramses | ramses-ui-final.json | abre el mantenimiento de profesores en un modal bloqueante | aprobada |  |
| Ramses | ramses-ui-final.json | abre el mantenimiento de carreras con selectores reales | aprobada |  |
| Ramses | ramses-ui-final.json | abre el mantenimiento de cursos con carrera y nivel | aprobada |  |
| Ramses | ramses-ui-final.json | abre el mantenimiento de periodos con fechas | aprobada |  |
| Ramses | ramses-ui-final.json | abre composición de notificaciones en modal | aprobada |  |
| Alejandro | Recorrido de auditoría | A-WEB1: Inicio con expediente y solicitudes propias | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ESTUDIANTE-390: Plantilla ESTUDIANTE sin desbordamiento a 390px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ESTUDIANTE-768: Plantilla ESTUDIANTE sin desbordamiento a 768px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ESTUDIANTE-1366: Plantilla ESTUDIANTE sin desbordamiento a 1366px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ADMINISTRADOR-390: Plantilla ADMINISTRADOR sin desbordamiento a 390px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ADMINISTRADOR-768: Plantilla ADMINISTRADOR sin desbordamiento a 768px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ADMINISTRADOR-1366: Plantilla ADMINISTRADOR sin desbordamiento a 1366px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-WEB2-confirmacion: Expediente espera confirmación y permite volver sin PUT | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ruta-activa: Solo una opción del menú estudiantil queda activa en expediente | fallida |  |
| Alejandro | Recorrido de auditoría | A-ADM1-identidad-movil: La barra de usuario muestra nombre y rol en móvil | fallida |  |
| Alejandro | Recorrido de auditoría | A-WEB4-confirmacion: Cancelar solicitud requiere confirmar antes de DELETE | aprobada |  |
| Alejandro | Recorrido de auditoría | A-WEB4-TAMPAG: Prematrícula respeta TAMPAG=2 | fallida |  |
| Alejandro | Recorrido de auditoría | A-ADM24-salida: Salir limpia la sesión de las pestañas del mismo navegador | aprobada |  |
| Jose | Contratos compartidos | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.permiteArchivoLocalVacio | aprobada |  |
| Jose | Contratos compartidos | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.incluyeContratosEnClasspath | aprobada |  |
| Jose | Contratos compartidos | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.encuentraArchivoEnPadresYResuelveValoresEntreComillas | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.rollbackDeRubrosNoEnviaBitacoraDeExito | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.reemplazoDeRubrosAuditaAntesYDespuesTrasCommit | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.altaDeRubrosAuditaJsonNuevoTrasCommit | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.rollbackNoEnviaBitacoraDeExito | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.enviaAntesYDespuesSoloTrasConfirmarTransaccion | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasSqlServerTest.primeraSentenciaAdquiereBloqueoHastaRollbackSinTransaccionesAnidadas | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.fueraDeTransaccionNoIntentaEscribir | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[1] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[2] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[3] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[4] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[5] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[6] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.conexionEnAutocommitNoPuedeProtegerLaOperacion | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.primeraNotaConfirmadaImpideReemplazarElDesgloseConcurrente | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.dosPostConcurrentesNoSobrescribenNiDuplicanLaPrimeraNota | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.reemplazoConfirmadoRechazaLaNotaDelRubroQueYaNoExiste | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[1] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[2] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[3] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.putNoCreaUnaNotaInexistente | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.falloAlGuardarNoPublicaExitoEnBitacora | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.modificacionConservaSnapshotAnterior | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.postNoSobrescribeNotaExistente | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.creaEnLimitesYAuditaResultadoPersistido(String)[1] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.creaEnLimitesYAuditaResultadoPersistido(String)[2] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.sinTokenNoPuedeConsultarGrupo | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.grupoInvalidoDevuelve400 | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.profesorPropioUsaOperacionCorrecta | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.estudianteNoPuedeAsignarNotaPorHttp | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.profesorAjenoNoPuedeModificarNotaPorHttp | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.notaInvalidaNoLlegaALaEscritura | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.bloqueaCambioDeRubrosCuandoYaExistenNotas | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.rechazaDesgloseQueNoSumaCien | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.reemplazoAuditaSnapshotPrevioYResultadoPersistido | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.resumenConsultaSoloProfesorPropioYPeriodoActivo | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.gruposDePeriodosCerradosNoAparecenEnResumenActual | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.falloDeGrupoNoSePresentaComoResumenSinDatos | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.dependenciaCaidaNoSeConvierteEnGrupoVacio | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.interceptorUsaIdentidadValidadaYNoElPayloadDelNavegador | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.profesorPuedeAccederSoloAGrupoPropio | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.tokenExpiradoNoResuelveIdentidad | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.matriculaConPeriodoDiferenteEsRechazada | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.estudianteNoPuedeEscribirNiConsultarNotasDelProfesor | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.profesorNoPuedeConsultarOtroGrupo | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.headersDelClienteNoOtorganIdentidad | aprobada |  |
| Jose | ui-sesion-notas-final.json | profesor navega a notas con layout, ruta activa, breadcrumbs y regreso | aprobada |  |
| Jose | ui-sesion-notas-final.json | ADMINISTRADOR no ve acceso ni ejecuta consultas de notas mediante URL directa | aprobada |  |
| Jose | ui-sesion-notas-final.json | ESTUDIANTE no ve acceso ni ejecuta consultas de notas mediante URL directa | aprobada |  |
| Jose | ui-sesion-notas-final.json | asignar y modificar nota desde el layout conserva verbos y bloqueo del desglose | aprobada |  |
| Jose | ui-sesion-notas-final.json | carga de desglose usa el cliente compartido y refresca el resultado en el layout | aprobada |  |
| Jose | ui-sesion-notas-final.json | fallo de consulta no habilita escritura y permite reintentar sin perder sesión | aprobada |  |
| Jose | ui-sesion-notas-final.json | profesor sin grupos ve el estado vacío dentro del layout | aprobada |  |
| Jose | ui-sesion-notas-final.json | sesión rechazada al consultar notas abandona el layout y limpia credenciales | aprobada |  |
| Jose | ui-sesion-notas-final.json | salir desde notas retira el módulo; volver atrás no recupera acceso | aprobada |  |
| Jose | ui-sesion-notas-final.json | profesor sin ADM17 por URL directa no consulta notas y no ve el acceso | aprobada |  |
| Jose | ui-sesion-notas-final.json | login ADMINISTRADOR llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | ui-sesion-notas-final.json | login PROFESOR llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | ui-sesion-notas-final.json | login ESTUDIANTE llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | ui-sesion-notas-final.json | rutas directas sin sesión vuelven al login sin mostrar identidad de ejemplo | aprobada |  |
| Jose | ui-sesion-notas-final.json | credenciales inválidas no abren el layout | aprobada |  |
| Jose | ui-sesion-notas-final.json | identidad rechazada después del login no conserva tokens ni abre el layout | aprobada |  |
| Jose | ui-sesion-notas-final.json | renovación programada mantiene el layout y rota los tokens sin volver al login | aprobada |  |
| Jose | ui-sesion-notas-final.json | refresh rechazado retira la identidad y vuelve al login con aviso de expiración | aprobada |  |
| Jose | ui-sesion-notas-final.json | token vencido guardado se renueva una sola vez al restaurar bajo StrictMode | aprobada |  |
| Jose | ui-sesion-notas-final.json | caída al restaurar conserva tokens, oculta el layout y permite reintentar | aprobada |  |
| Jose | ui-sesion-notas-final.json | salir limpia la cuenta aunque Auth falle; volver atrás no recupera el layout | aprobada |  |
| Jose | ui-sesion-notas-final.json | salir mientras se renueva impide que la respuesta tardía restaure la cuenta | aprobada |  |
| Jose | ui-sesion-notas-final.json | perfil no reconocido en la identidad no accede al layout | aprobada |  |
| Jose | ui-sesion-notas-final.json | el estudiante no abre una ruta administrativa de notas | aprobada |  |
| Jose | ui-sesion-notas-final.json | login móvil conserva formulario visible sin desbordamiento horizontal | aprobada |  |
| Jose | ui-sesion-notas-final.json | retiro de ADM17 se refleja en el menú al recargar | aprobada |  |
| Jose | ui-sesion-notas-final.json | caída de permisos durante restauración oculta el layout y permite recuperar | aprobada |  |
| Jose | ui-jose-final.json | login invalido no entra y muestra mensaje requerido | aprobada |  |
| Jose | ui-jose-final.json | profesor crea y modifica nota con verbos distintos y rubros quedan bloqueados | aprobada |  |
| Jose | ui-jose-final.json | sin desglose permite carga confirmada y refresca resultado | aprobada |  |
| Jose | ui-jose-final.json | dependencia caida no muestra vacio ni permite guardar | aprobada |  |
| Jose | ui-jose-final.json | estudiante no obtiene controles de notas y logout limpia almacenamiento | aprobada |  |
| Jose | ui-jose-final.json | login utilizable en pantalla movil sin desbordamiento horizontal | aprobada |  |

## resultados

Procedencia: `resultados.json`, retirado después de esta transcripción.

Fecha: 2026-11-12

Limite: Casos locales ejecutados; no equivale a criterios completos, cobertura ni aceptación real. Apoyo heredado no suma a las barras.

Resumen registrado por integrante (conservo las exclusiones del informe original):

| Integrante | Resultado registrado |
| --- | --- |
| Alejandro | {"aprobada": 225, "fallida": 6, "omitida": 0, "total": 231, "porcentaje_aprobado": 97.4} |
| Fabian | {"aprobada": 14, "fallida": 6, "omitida": 0, "total": 20, "porcentaje_aprobado": 70.0} |
| Jose | {"aprobada": 240, "fallida": 12, "omitida": 0, "total": 252, "porcentaje_aprobado": 95.24} |
| Ramses | {"aprobada": 61, "fallida": 0, "omitida": 0, "total": 61, "porcentaje_aprobado": 100.0} |

Transcribí **566 entradas**. Las entradas excluidas o de preparación no equivalen a casos incluidos en las barras.

| Integrante | Grupo | Caso | Estado | Observación registrada |
| --- | --- | --- | --- | --- |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.IntegracionCompartidaTest.cargaBibliotecaYContratoConSpringBootDelServicioSinBaseReal | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.NuevoAvatarPersona2ApplicationTests.contextLoads | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.aceptaBearerValidadoPorSeguridad | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.rechazaSolicitudSinBearer | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.obtieneElSujetoSoloDespuesDeValidarElBearer | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.registraAmbosEstadosYNoConfirmaCambiosFallidos | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaLasTresCredencialesDelCambioDeContrasena | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaSecretosConComillasEscapadasYCamposAnidados | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.conservaJsonSinSecretos | aprobada |  |
| Fabian | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaContrasenaYConservaDatosDelRegistro | aprobada |  |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.actualizaSoloElNombreDeLaCuentaDerivadaDelSujeto | aprobada |  |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.cambiaContrasenaDelSujetoYGuardaSoloHashBcrypt | fallida |  |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.validaLongitudMayusculaMinusculaNumeroYCaracterEspecialEnServidor | fallida |  |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.rechazaContrasenaActualIncorrectaSinPersistir | fallida |  |
| Ramses | Java Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.permiteCuandoTodosLosPeriodosEstanCerrados | aprobada |  |
| Ramses | Java Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.registraSnapshotsJsonEnBitacoraDeProfesor | aprobada |  |
| Ramses | Java Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.exigeFormatoLocalDeTelefonosYEvitaDuplicados | aprobada |  |
| Ramses | Java Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.bloqueaCuandoHoyEstaDentroDelPeriodoInclusivo | aprobada |  |
| Ramses | Java Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.bloqueaEliminarCuandoExistenCursos | aprobada |  |
| Ramses | Java Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.validaInstitucionYProfesorEnSusServiciosPropietarios | aprobada |  |
| Ramses | Java Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.registraSnapshotsJsonEnModificacionYEliminacion | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaDistritoDeOtroCanton | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaCantonDeOtraProvincia | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.comunicaFallaDelServicioSinTratarlaComoDireccionInvalida | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaProvinciaInexistente | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.consumeRutasYCamelCaseDelServicioDeAlejandro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[6] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[7] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[8] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[9] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[10] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.metodoNoAdmitidoNoSeReportaComoErrorInterno | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identidadEnQueryNoCambiaElExpedientePropio | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaCualquierCampoProtegidoAunqueElRestoSeaValido | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identificacionDuplicadaAlModificarNoPierdeDatos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaRolIncorrectoVinculoAusenteYAmbiguo | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposEditablesObligatoriosSeValidanAntesDePersistir | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identificacionReferenciadaNoSeCambiaEntreServicios | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.errorTecnicoRegistraBitacoraYRevierteEscritura | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.noEliminaEstudianteReferenciadoNiSusTelefonos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.noPermiteMismoNumeroAunqueCambieElTipo | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.estudiantesDiferentesSoloVenYActualizanSuPropioRegistro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.direccionInvalidaNoEscribeDatos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.conservaCerosInicialesYNoExigeCorreoOTelefonosUnicos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.falloGen1RevierteModificacionYEliminacion | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.dependenciasDeAutenticacionNoDisponiblesDevuelven503 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaDireccionIncoherenteYTokenInvalido | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.sqlNoInterpretaLaIdentificacionComoUnaInstruccion | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.dominioCambiaMedianteParametro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.falloGen1RevierteCreacionYTelefonos | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.cicloCompletoDeCincoOperacionesYBitacoras | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[6] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[7] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[8] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[9] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[10] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[11] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[12] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[13] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[14] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[15] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.recursosInexistentesDevuelven404 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.nombreCorreoDominioYFechaInvalidosSeRechazanEnCrearYModificar | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.consultaYActualizaSoloSuExpedienteConPersistenciaYBitacoraMinima | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[6] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[7] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[8] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[9] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[10] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[11] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[12] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[13] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[14] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[15] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[16] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[17] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[18] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[19] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[20] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[21] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[22] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[23] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[24] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[25] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[26] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[27] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[28] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[29] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.noAgregaEdadMinimaNiFormatoDeTelefono | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.utilizaDominioConfiguradoYSinDiferenciarMayusculas | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaExpedienteNulo | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.propagaFalloDelParametro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.conservaUsuarioYTokenValidados | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.responde503CuandoSeguridadNoResponde | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noConfiaEnSubSinValidacionExterna | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[5] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.consultaRespetaTextoDelDocumento | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.conservaEstadoAnteriorAunqueElObjetoCambie | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.registraTipoDeErrorSinCopiarMensajeConCredenciales | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.creacionYEliminacionIncluyenRegistro | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.validaContratoUsr5 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.comunicaFalloDeConexion | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.enviaBitacoraSinInventarFechaNiIdentificador | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.leeParametroConContratoUsr3 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.distingueFallaDeServicioDeTokenInvalido | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaTokenCon401 | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.informaParametroInexistente | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[2] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[3] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[4] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noOcultaFalloAlGuardarBitacora | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[1] | aprobada |  |
| Alejandro | Java Expediente | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.estudiantePuedeCrearConsultarModificarYCancelarSoloConIdentidadDeSesion | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.errorDespuesDeEscribirRevierteYSeRegistraComoMat1 | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.falloGen1RevierteCreacionYCursos | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.noImponeUnicidadNoSolicitadaEntreEstudianteYPeriodo | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.compruebaTodosLosNivelesDesdeLaBase | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.cincoOperacionesConDatosPersistidosYBitacoras | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[5] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[6] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[7] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[8] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[9] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[10] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[11] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[12] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[13] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[14] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[15] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.modificarPermiteCambiarEstudianteYCarrera | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.reevaluaLaFechaParaCadaEscrituraYPermiteConsultarOEliminar | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.referenciasInexistentesNoGuardanDatos | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.endpointsAdministrativosNoPermitenQueUnEstudianteElijaIdentidadAjena | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.falloGen1RevierteModificacionYEliminacion | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.listaVaciaOCursoNuloNoSeAceptan | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.servicioDePrematriculaNoExponeRutasDeExpediente | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.solicitudPropiaNoAceptaIdentidadElegidaYNoExponeSolicitudesDeOtroEstudiante | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesPuedenOmitirseAlCrear | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.recursoAusenteIdInvalidoYMetodoNoAdmitidoTienenRespuestasRest | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.rechazaPeriodoQueIniciaHoyOPasadoAunqueSolicitanteEnvieOtraFecha(int)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.rechazaPeriodoQueIniciaHoyOPasadoAunqueSolicitanteEnvieOtraFecha(int)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.opcionesPropiasSoloIncluyenCursosDePrimerNivelYPeriodosFuturos | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[5] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[6] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[7] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[8] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[9] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[10] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.necesitaFechaDelServidorParaEvaluarPeriodo | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosNulos | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.evaluaFechaDeCadaOperacionSinConservarUnHoyFijo | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.comparaFechasAlCambiarDeAnio | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaUnSoloCursoDePrimerNivel | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[4] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[2] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[3] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[1] | aprobada |  |
| Alejandro | Java Prematricula | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[2] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.rollbackDeRubrosNoEnviaBitacoraDeExito | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.reemplazoDeRubrosAuditaAntesYDespuesTrasCommit | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.altaDeRubrosAuditaJsonNuevoTrasCommit | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.rollbackNoEnviaBitacoraDeExito | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.enviaAntesYDespuesSoloTrasConfirmarTransaccion | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.fueraDeTransaccionNoIntentaEscribir | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[1] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[2] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[3] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[4] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[5] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[6] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.conexionEnAutocommitNoPuedeProtegerLaOperacion | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.primeraNotaConfirmadaImpideReemplazarElDesgloseConcurrente | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.dosPostConcurrentesNoSobrescribenNiDuplicanLaPrimeraNota | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.reemplazoConfirmadoRechazaLaNotaDelRubroQueYaNoExiste | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[1] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[2] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[3] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.putNoCreaUnaNotaInexistente | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.falloAlGuardarNoPublicaExitoEnBitacora | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.modificacionConservaSnapshotAnterior | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.postNoSobrescribeNotaExistente | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.creaEnLimitesYAuditaResultadoPersistido(String)[1] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.creaEnLimitesYAuditaResultadoPersistido(String)[2] | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.sinTokenNoPuedeConsultarGrupo | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.grupoInvalidoDevuelve400 | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.profesorPropioUsaOperacionCorrecta | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.estudianteNoPuedeAsignarNotaPorHttp | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.profesorAjenoNoPuedeModificarNotaPorHttp | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.notaInvalidaNoLlegaALaEscritura | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.bloqueaCambioDeRubrosCuandoYaExistenNotas | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.rechazaDesgloseQueNoSumaCien | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.reemplazoAuditaSnapshotPrevioYResultadoPersistido | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.resumenConsultaSoloProfesorPropioYPeriodoActivo | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.gruposDePeriodosCerradosNoAparecenEnResumenActual | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.falloDeGrupoNoSePresentaComoResumenSinDatos | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.dependenciaCaidaNoSeConvierteEnGrupoVacio | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.interceptorUsaIdentidadValidadaYNoElPayloadDelNavegador | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.profesorPuedeAccederSoloAGrupoPropio | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.tokenExpiradoNoResuelveIdentidad | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.matriculaConPeriodoDiferenteEsRechazada | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.estudianteNoPuedeEscribirNiConsultarNotasDelProfesor | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.profesorNoPuedeConsultarOtroGrupo | aprobada |  |
| Jose | Java Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.headersDelClienteNoOtorganIdentidad | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.guardaHistorialEnErrorCuandoFallaTransporteSmtp | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.registraErrorEnHistorialCuandoFaltanParametrosSmtp | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.informaEnvioRealizadoAunqueFalleGuardadoDeHistorial | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.sanitizacionEliminaScriptsYAtributosPeligrosos | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.rechazaDestinatariosInvalidos | aprobada |  |
| Jose | Contratos compartidos | permiteArchivoLocalVacio | aprobada |  |
| Jose | Contratos compartidos | incluyeContratosEnClasspath | aprobada |  |
| Jose | Contratos compartidos | encuentraArchivoEnPadresYResuelveValoresEntreComillas | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 13, nombre: "Programación") | aprobada |  |
| Ramses | .NET | PeriodoServiceTest.ContratoActualPermiteFechasIguales | aprobada |  |
| Ramses | .NET | CursoAuditoriaTest.AltaConservaElRegistroPersistidoEnJson | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.EstadoSeDerivaDeFechas | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 0, nombre: "Programación") | aprobada |  |
| Ramses | .NET | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 1, nivel: 13) | aprobada |  |
| Ramses | .NET | NuevoAvatar.Academico.Tests.CursoFiltrosTest.CombinaCarreraYNivelEnRepositorio | aprobada |  |
| Ramses | .NET | CursoAuditoriaTest.EliminacionConservaElRegistroEliminado | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.DosAltasEnLaMismaInstanciaNoDuplicanElPeriodo | aprobada |  |
| Apoyo heredado | .NET | ValidacionesRequestTest.GrupoExigeIdentificadoresHorarioYCupo | Passed |  |
| Ramses | .NET | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 1) | aprobada |  |
| Ramses | .NET | PeriodoServiceTest.RechazaFechaFinalAnteriorAlInicioAntesDeGuardar | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.EditarExcluyeElRegistroPropioYConservaOtrosNumeros | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 12) | aprobada |  |
| Ramses | .NET | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 0, nivel: 1) | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.PeriodoExigeDatos | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 1, nombre: "Programación 5") | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.PermiteFechasSuperpuestasSiElNumeroEsDistinto | aprobada |  |
| Ramses | .NET | CursoAuditoriaTest.ModificacionConservaAnteriorYActual | aprobada |  |
| Apoyo heredado | .NET | GrupoServiceTest.RechazaCursoInexistenteAntesDeGuardar | Passed |  |
| Ramses | .NET | PeriodoReglasTest.DuplicadoProduceConflicto | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "ESTUDIANTE", email: "e@cuc.cr", profesor: null, estudiante: "123") | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/%69nterno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/refresh", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_LeeBearerYDevuelveTrue | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.TokenExpiradoYFirmaAjenaNoSonValidos | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_RechazaDatosVacios | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_RechazaTokenInvalido | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/interno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_DependenciaCaidaNoPermiteLlegarAlDestino | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Sesion_NoConfiaEnHeadersDeIdentidad | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve401ParaCredencialesInvalidas | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/obtenernotas", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: null, valido: True, status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve201ParaCredencialesValidas | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/logout", metodo: "POST", status: 204) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/refresh", metodo: "POST", status: 201) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 201) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "PROFESOR", email: "p@cuc.ac.cr", profesor: 9, estudiante: null) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "ESTUDIANTE", permitido: "PROFESOR", valido: True, status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: False, status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_NoVinculaOtroExpedienteYNoAsumeRolPorId | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer valido", status: 204) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: True, status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/sesion", metodo: "GET", status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer invalido", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta de grupos y periodos") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuNormalNoRegistraVisitaAlDashboard | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/mat/prematricula/propio", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/fin/notificar/historial?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: null, ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Registro sin formato. JSON: {\"ruta\":\"/usuario\""···) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "GET") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/mat/obtenernotas?grupoId=1", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "PUT", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodos", modulo: "ADM12") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: False) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: null, esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: True) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial academico", modulo: "ADM18") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "bitacora") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "estudiante", path: "/gateway/mat/obtenernotas?grupoId=1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "profesor") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "POST", ruta: "/gateway/mat/prematricula/propio") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/interno/datos", status: 404) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 200, invalido: True) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM10", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Referencia a GET /usuario") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "GET", ruta: "/gateway/mat/prematricula/propio") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RoutingDeTodasLasFamiliasTieneDestinoYPolitica | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/historial", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "invalido", ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM22", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/admin/expediente/123", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.AdministradorRecibeDiezEventosDeModulosPermitidosOrdenadosDespuesDeFiltrar | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodo 15", modulo: "ADM12") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM10", metodo: "GET", ruta: "/gateway/admin/carrera") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM11", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM17", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM7", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "DELETE", status: 204) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "profesor", path: "/gateway/admin/usuario") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "DELETE", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM7", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "GET", status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ModuloInactivoNiegaApiYSeRetiraDelMenu | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta de todos los grupos.", modulo: "ADM13") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo 1 de otro módulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora?vista=dashboard&vista=glob"···, esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo desconocido") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "refresh") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.NotificacionesAdministrativasRequierenPermisoDelModulo | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/%69nterno/datos", status: 404) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM12", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "estudiante") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 403, invalido: False) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "POST", status: 201) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/admin/bitacora?vista=dashboard") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 503, invalido: False) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "POST", ruta: "/gateway/admin/institucion") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/fin/notificar/historial?vista=dashboard") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DependenciaAuthCaidaDevuelve503 | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "profesor") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notas") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/expediente/propio", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ServicioNuevoSinContratoDeModuloNoPuedeQuedarExentoAccidentalmente | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta /obtenernotasgrupo", modulo: "ADM17") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "estudiante", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/DOMESTUD") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DestinoCaidoDevuelve502SinConfundirConAuth | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "GET /usuario/ana", modulo: "ADM4") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "login") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notificacion") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta del grupo 12.", modulo: "ADM13") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "PUT") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "admin") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/1?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "{\"descripcion\":\"El usuario consulta /usuario\"}") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/profesor/buscar") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "invalido", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "invalido", estado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "estudiante", estado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM6", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora/1?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "admin") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM7", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "PUT", ruta: "/gateway/admin/profesor/1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "profesor", estado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial académico", modulo: "ADM18") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/profesor") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.MenuPropioTests.UsaElIdRealDelRolDeLaCuentaYNuncaElRolDeLaQuery | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.MenuPropioTests.SinIdentidadNoConsultaNingunRol | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ModuloInactivoNoConcedeDependencias | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/menu/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM10", metodo: "DELETE", ruta: "/gateway/admin/carrera/1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta del grupo 12. JSON: {\"ruta\":\"/usuario\"···) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloDeBitacoraNoConfirmaElRegistroDelDashboard | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "POST", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuPropioNoAceptaRolDeQueryYNuncaRequierePermisoAdministrativo | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "DELETE", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/admin/usuario", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: null, estado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RetirarPermisoBloqueaLaSiguientePeticionInclusoParaAdministrador | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "invalido", esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "PUT", status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta listado de estudiantes", modulo: "ADM19") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.DatosVaciosDevuelvenListasVacias | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/institucion") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.CorreosSeFiltranPorDestinatarioCompletoAntesDelLimite | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorVeAccionesPropiasYGruposAutorizadosSinDatosAdministrativos | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | Node | login conserva contrato real y obtiene perfil del servidor, sin persistir password | aprobada |  |
| Jose | Node | solicitudes simultaneas con token vencido comparten una sola renovacion rotativa | aprobada |  |
| Jose | Node | refresh fallido limpia credenciales y notifica retorno a login | aprobada |  |
| Jose | Node | logout durante refresh no permite restaurar la sesion al finalizar la respuesta | aprobada |  |
| Jose | Node | 401 de negocio renueva una vez; segundo 401 limpia sesion sin bucles | aprobada |  |
| Jose | Node | caida de negocio no elimina sesion ni devuelve datos inventados | aprobada |  |
| Jose | Node | no envia tokens fuera del gateway | aprobada |  |
| Jose | Node | credenciales incorrectas y respuesta incompleta no dejan una sesion nueva | aprobada |  |
| Jose | Node | login sin identidad autorizada o con dependencia caida no conserva tokens parciales | aprobada |  |
| Jose | Node | logout con consulta inicial de identidad en vuelo impide completar el login tardio | aprobada |  |
| Ramses | Node | exige nombre, institución y profesor | aprobada |  |
| Ramses | Node | consume carrera, institución y profesor solo por gateway | aprobada |  |
| Ramses | Node | valida carrera nivel y nombre | aprobada |  |
| Ramses | Node | combina filtros usando gateway | aprobada |  |
| Ramses | Node | normaliza el identificador y conserva el contrato del API | aprobada |  |
| Ramses | Node | rechaza identificador, nombre y orden inválidos | aprobada |  |
| Ramses | Node | solo utiliza rutas del gateway y serializa roles | aprobada |  |
| Ramses | Node | lee tamaño de página desde parámetros con tope máximo de 15 | aprobada |  |
| Ramses | Node | valida destinatarios y contenido | aprobada |  |
| Ramses | Node | usa gateway para historial | aprobada |  |
| Ramses | Node | valida fechas | aprobada |  |
| Ramses | Node | combina filtros | aprobada |  |
| Ramses | Node | valida mayoría de edad, formato local y teléfonos únicos | aprobada |  |
| Ramses | Node | profesores usa únicamente el gateway con paginación | aprobada |  |
| Fabian | principal-ui-final.json | ADM3 incluye facturas pendientes de períodos cerrados en el indicador | aprobada |  |
| Fabian | principal-ui-final.json | ADM3 presenta los grupos activos del profesor | aprobada |  |
| Fabian | principal-ui-final.json | ADM3 informa el fallo de auditoría y de grupos sin ocultar soporte | fallida |  |
| Fabian | principal-ui-final.json | ADM3 muestra al profesor los resúmenes del servidor sin controles de filtrado | aprobada |  |
| Fabian | principal-ui-final.json | ADM8 permite buscar y mantener instituciones desde la sesión | fallida |  |
| Fabian | principal-ui-final.json | ADM8 muestra dependencia y no abre la pantalla sin permiso | fallida |  |
| Fabian | principal-ui-final.json | ADM8 mantiene el listado detrás del modal, valida y cancela sin guardar | fallida |  |
| Jose | principal-ui-final.json | profesor navega a notas con layout, ruta activa, breadcrumbs y regreso | fallida |  |
| Jose | principal-ui-final.json | ADMINISTRADOR no ve acceso ni ejecuta consultas de notas mediante URL directa | fallida |  |
| Jose | principal-ui-final.json | ESTUDIANTE no ve acceso ni ejecuta consultas de notas mediante URL directa | fallida |  |
| Jose | principal-ui-final.json | asignar y modificar nota desde el layout conserva verbos y bloqueo del desglose | fallida |  |
| Jose | principal-ui-final.json | carga de desglose usa el cliente compartido y refresca el resultado en el layout | fallida |  |
| Jose | principal-ui-final.json | fallo de consulta no habilita escritura y permite reintentar sin perder sesión | fallida |  |
| Jose | principal-ui-final.json | profesor sin grupos ve el estado vacío dentro del layout | fallida |  |
| Jose | principal-ui-final.json | sesión rechazada al consultar notas abandona el layout y limpia credenciales | fallida |  |
| Jose | principal-ui-final.json | salir desde notas retira el módulo; volver atrás no recupera acceso | fallida |  |
| Jose | principal-ui-final.json | profesor sin ADM17 por URL directa no consulta notas y no ve el acceso | fallida |  |
| Fabian | principal-ui-final.json | ADM6 usa el servicio para crear, editar y eliminar parámetros | fallida |  |
| Ramses | principal-ui-final.json | ADM7 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM9 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM10 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM11 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM12 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM22 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | principal-ui-final.json | ADM7 respeta TAMPAG y permite navegar sin mostrar más de quince filas | aprobada |  |
| Ramses | principal-ui-final.json | sin ADM7 la ruta directa no consulta ni ofrece escritura de módulos | aprobada |  |
| Ramses | principal-ui-final.json | ADM7 conserva el foco al escribir y muestra validaciones dentro del diálogo | aprobada |  |
| Ramses | principal-ui-final.json | ADM7 muestra el registro guardado en su página y confirma su eliminación | aprobada |  |
| Fabian | principal-ui-final.json | ADM5 guarda un cambio de permisos para un módulo y vuelve a leerlo | aprobada |  |
| Jose | principal-ui-final.json | login ADMINISTRADOR llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | principal-ui-final.json | login PROFESOR llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | principal-ui-final.json | login ESTUDIANTE llega al layout con identidad real y restaura al recargar | fallida |  |
| Jose | principal-ui-final.json | rutas directas sin sesión vuelven al login sin mostrar identidad de ejemplo | aprobada |  |
| Jose | principal-ui-final.json | credenciales inválidas no abren el layout | aprobada |  |
| Jose | principal-ui-final.json | identidad rechazada después del login no conserva tokens ni abre el layout | aprobada |  |
| Jose | principal-ui-final.json | renovación programada mantiene el layout y rota los tokens sin volver al login | aprobada |  |
| Jose | principal-ui-final.json | refresh rechazado retira la identidad y vuelve al login con aviso de expiración | aprobada |  |
| Jose | principal-ui-final.json | token vencido guardado se renueva una sola vez al restaurar bajo StrictMode | aprobada |  |
| Jose | principal-ui-final.json | caída al restaurar conserva tokens, oculta el layout y permite reintentar | aprobada |  |
| Jose | principal-ui-final.json | salir limpia la cuenta aunque Auth falle; volver atrás no recupera el layout | aprobada |  |
| Jose | principal-ui-final.json | salir mientras se renueva impide que la respuesta tardía restaure la cuenta | aprobada |  |
| Jose | principal-ui-final.json | perfil no reconocido en la identidad no accede al layout | aprobada |  |
| Jose | principal-ui-final.json | el estudiante no abre una ruta administrativa de notas | fallida |  |
| Jose | principal-ui-final.json | login móvil conserva formulario visible sin desbordamiento horizontal | aprobada |  |
| Jose | principal-ui-final.json | retiro de ADM17 se refleja en el menú al recargar | aprobada |  |
| Jose | principal-ui-final.json | caída de permisos durante restauración oculta el layout y permite recuperar | aprobada |  |
| Fabian | principal-ui-final.json | ADM4 crea y edita usuarios usando el ID real del rol | fallida |  |
| Jose | jose-ui-final.json | login invalido no entra y muestra mensaje requerido | aprobada |  |
| Jose | jose-ui-final.json | profesor crea y modifica nota con verbos distintos y rubros quedan bloqueados | aprobada |  |
| Jose | jose-ui-final.json | sin desglose permite carga confirmada y refresca resultado | aprobada |  |
| Jose | jose-ui-final.json | dependencia caida no muestra vacio ni permite guardar | aprobada |  |
| Jose | jose-ui-final.json | estudiante no obtiene controles de notas y logout limpia almacenamiento | aprobada |  |
| Jose | jose-ui-final.json | login utilizable en pantalla movil sin desbordamiento horizontal | aprobada |  |
| Ramses | ramses-ui-final.json | el modal bloquea el fondo y restaura el foco al cerrar | aprobada |  |
| Ramses | ramses-ui-final.json | abre el mantenimiento de profesores en un modal bloqueante | aprobada |  |
| Ramses | ramses-ui-final.json | abre el mantenimiento de carreras con selectores reales | aprobada |  |
| Ramses | ramses-ui-final.json | abre el mantenimiento de cursos con carrera y nivel | aprobada |  |
| Ramses | ramses-ui-final.json | abre el mantenimiento de periodos con fechas | aprobada |  |
| Ramses | ramses-ui-final.json | abre composición de notificaciones en modal | aprobada |  |
| Alejandro | Recorrido de auditoría | A-WEB1: Inicio con expediente y solicitudes propias | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ESTUDIANTE-390: Plantilla ESTUDIANTE sin desbordamiento a 390px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ESTUDIANTE-768: Plantilla ESTUDIANTE sin desbordamiento a 768px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ESTUDIANTE-1366: Plantilla ESTUDIANTE sin desbordamiento a 1366px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ADMINISTRADOR-390: Plantilla ADMINISTRADOR sin desbordamiento a 390px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ADMINISTRADOR-768: Plantilla ADMINISTRADOR sin desbordamiento a 768px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ADMINISTRADOR-1366: Plantilla ADMINISTRADOR sin desbordamiento a 1366px | aprobada |  |
| Alejandro | Recorrido de auditoría | A-WEB2-confirmacion: Expediente espera confirmación y permite volver sin PUT | aprobada |  |
| Alejandro | Recorrido de auditoría | A-ADM1-ruta-activa: Solo una opción del menú estudiantil queda activa en expediente | fallida |  |
| Alejandro | Recorrido de auditoría | A-ADM1-identidad-movil: La barra de usuario muestra nombre y rol en móvil | fallida |  |
| Alejandro | Recorrido de auditoría | A-WEB4-confirmacion: Cancelar solicitud requiere confirmar antes de DELETE | aprobada |  |
| Alejandro | Recorrido de auditoría | A-WEB4-TAMPAG: Prematrícula respeta TAMPAG=2 | fallida |  |
| Alejandro | Recorrido de auditoría | A-ADM24-salida: Salir limpia la sesión de las pestañas del mismo navegador | aprobada |  |

## recorrido-estudiante

Procedencia: `recorrido-estudiante.json`, retirado después de esta transcripción.

Transcribí **13 entradas**. Las entradas excluidas o de preparación no equivalen a casos incluidos en las barras.

| Integrante | Grupo | Caso | Estado | Observación registrada |
| --- | --- | --- | --- | --- |
| Alejandro | Recorrido | Inicio con expediente y solicitudes propias | aprobada | Expediente y 16 solicitudes de respuestas controladas. |
| Alejandro | Recorrido | Plantilla ESTUDIANTE sin desbordamiento a 390px | aprobada | {"contenido": 390, "ventana": 390} |
| Alejandro | Recorrido | Plantilla ESTUDIANTE sin desbordamiento a 768px | aprobada | {"contenido": 768, "ventana": 768} |
| Alejandro | Recorrido | Plantilla ESTUDIANTE sin desbordamiento a 1366px | aprobada | {"contenido": 1366, "ventana": 1366} |
| Alejandro | Recorrido | Plantilla ADMINISTRADOR sin desbordamiento a 390px | aprobada | {"contenido": 390, "ventana": 390} |
| Alejandro | Recorrido | Plantilla ADMINISTRADOR sin desbordamiento a 768px | aprobada | {"contenido": 768, "ventana": 768} |
| Alejandro | Recorrido | Plantilla ADMINISTRADOR sin desbordamiento a 1366px | aprobada | {"contenido": 1366, "ventana": 1366} |
| Alejandro | Recorrido | Expediente espera confirmación y permite volver sin PUT | aprobada | Confirmación y regreso sin enviar actualización. |
| Alejandro | Recorrido | Solo una opción del menú estudiantil queda activa en expediente | fallida | Opciones activas: ⌂Inicio, ▤Mi expediente<br><br>2 !== 1<br> |
| Alejandro | Recorrido | La barra de usuario muestra nombre y rol en móvil | fallida | El CSS oculta .user-info en anchos de hasta 600px; nombre y rol no son visibles en la barra. |
| Alejandro | Recorrido | Cancelar solicitud requiere confirmar antes de DELETE | aprobada | Se ofrece confirmación sin eliminar todavía. |
| Alejandro | Recorrido | Prematrícula respeta TAMPAG=2 | fallida | TAMPAG=2 preparado; se muestran 10 solicitudes. El componente usa una constante de 10.<br><br>10 !== 2<br> |
| Alejandro | Recorrido | Salir limpia la sesión de las pestañas del mismo navegador | aprobada | La segunda pestaña solicita login y limpia tokens. |

## recorrido-estudiante-inicial

Procedencia: `recorrido-estudiante-inicial.json`, retirado después de esta transcripción.

Transcribí **11 entradas**. Las entradas excluidas o de preparación no equivalen a casos incluidos en las barras.

| Integrante | Grupo | Caso | Estado | Observación registrada |
| --- | --- | --- | --- | --- |
| Alejandro | Recorrido | Inicio con expediente y solicitudes propias | aprobada | Expediente y 16 solicitudes de respuestas controladas. |
| Alejandro | Recorrido | Plantilla ESTUDIANTE sin desbordamiento a 390px | fallida | locator.waitFor: Timeout 6000ms exceeded.<br>Call log:<br>[2m  - waiting for locator('.user-name') to be visible[22m<br>[2m    14 × locator resolved to hidden <div class="user-name">Ana Prueba</div>[22m<br> |
| Alejandro | Recorrido | Plantilla ESTUDIANTE sin desbordamiento a 768px | aprobada | {"contenido": 768, "ventana": 768} |
| Alejandro | Recorrido | Plantilla ESTUDIANTE sin desbordamiento a 1366px | aprobada | {"contenido": 1366, "ventana": 1366} |
| Alejandro | Recorrido | Plantilla ADMINISTRADOR sin desbordamiento a 390px | fallida | locator.waitFor: Timeout 6000ms exceeded.<br>Call log:<br>[2m  - waiting for locator('.user-name') to be visible[22m<br>[2m    14 × locator resolved to hidden <div class="user-name">Ana Prueba</div>[22m<br> |
| Alejandro | Recorrido | Plantilla ADMINISTRADOR sin desbordamiento a 768px | aprobada | {"contenido": 768, "ventana": 768} |
| Alejandro | Recorrido | Plantilla ADMINISTRADOR sin desbordamiento a 1366px | aprobada | {"contenido": 1366, "ventana": 1366} |
| Alejandro | Recorrido | Expediente espera confirmación y permite volver sin PUT | aprobada | Confirmación y regreso sin enviar actualización. |
| Alejandro | Recorrido | Cancelar solicitud requiere confirmar antes de DELETE | aprobada | Se ofrece confirmación sin eliminar todavía. |
| Alejandro | Recorrido | Prematrícula respeta TAMPAG=2 | fallida | TAMPAG=2 preparado; se muestran 10 solicitudes. El componente usa una constante de 10.<br><br>10 !== 2<br> |
| Alejandro | Recorrido | Salir limpia la sesión de las pestañas del mismo navegador | aprobada | La segunda pestaña solicita login y limpia tokens. |

## Observaciones

Los nombres de archivos auxiliares citados en el informe identifican la procedencia histórica. Sus resultados esenciales permanecen en las tablas anteriores y en el informe de evidencias. Retiré los logs, JSON, TRX y guiones temporales; las pruebas mantenidas en el código del proyecto siguen en sus carpetas originales. Para repetir el recorrido propio de auditoría será necesario preparar un nuevo ejecutor o seguir sus pasos documentados.
