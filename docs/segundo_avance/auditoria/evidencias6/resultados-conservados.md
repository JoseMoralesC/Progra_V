# Resultados conservados en Markdown

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


Fecha de organización: 12 de noviembre de 2026.

Como equipo decidimos conservar los informes y las capturas para presentar la auditoría. Trasladé aquí los casos y sus estados antes de retirar los archivos auxiliares. Esta organización no representa una nueva ejecución ni cambia el avance de los aportes. Los antecedentes y las repeticiones siguen separados; no deben sumarse entre sí.

El [informe de evidencias](README.md) explica las condiciones, los fallos y los límites de aceptación. Ya no conservamos en esta carpeta las trazas completas ni los ejecutores temporales.

## resultados

Procedencia: `resultados.json`, retirado después de esta transcripción.

Fecha: 2026-11-12T11:32:31.378985-06:00

Resumen registrado por integrante (conservo las exclusiones del informe original):

| Integrante | Resultado registrado |
| --- | --- |
| Alejandro | {"aprobada": 224, "fallida": 11} |
| Fabian | {"aprobada": 19, "fallida": 8} |
| Jose | {"aprobada": 253} |
| Ramses | {"aprobada": 52, "fallida": 9} |

Transcribí **576 entradas**. Las entradas excluidas o de preparación no equivalen a casos incluidos en las barras.

| Integrante | Grupo | Caso | Estado | Observación registrada |
| --- | --- | --- | --- | --- |
| Jose | Java integracion | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.permiteArchivoLocalVacio | aprobada |  |
| Jose | Java integracion | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.incluyeContratosEnClasspath | aprobada |  |
| Jose | Java integracion | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.encuentraArchivoEnPadresYResuelveValoresEntreComillas | aprobada |  |
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
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.cambiaContrasenaDelSujetoYGuardaSoloHashBcrypt | fallida | 503 SERVICE_UNAVAILABLE "Falta el parámetro institucional requerido PASSMIN" |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.validaLongitudMayusculaMinusculaNumeroYCaracterEspecialEnServidor | fallida | Ab1! ==> expected: <400 BAD_REQUEST> but was: <503 SERVICE_UNAVAILABLE> |
| Alejandro | Java Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.rechazaContrasenaActualIncorrectaSinPersistir | fallida | expected: <400 BAD_REQUEST> but was: <503 SERVICE_UNAVAILABLE> |
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
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.guardaHistorialEnErrorCuandoFallaTransporteSmtp | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.registraErrorEnHistorialCuandoFaltanParametrosSmtp | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.informaEnvioRealizadoAunqueFalleGuardadoDeHistorial | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.sanitizacionEliminaScriptsYAtributosPeligrosos | aprobada |  |
| Ramses | Java Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.rechazaDestinatariosInvalidos | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.EstadoSeDerivaDeFechas | aprobada |  |
| Ramses | .NET | PeriodoServiceTest.ContratoActualPermiteFechasIguales | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 1) | aprobada |  |
| Ramses | .NET | CursoAuditoriaTest.EliminacionConservaElRegistroEliminado | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.PermiteFechasSuperpuestasSiElNumeroEsDistinto | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 0, nombre: "Programación") | aprobada |  |
| Ramses | .NET | CursoAuditoriaTest.ModificacionConservaAnteriorYActual | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 13, nombre: "Programación") | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.PeriodoExigeDatos | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 1, nombre: "Programación 5") | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.DosAltasEnLaMismaInstanciaNoDuplicanElPeriodo | aprobada |  |
| Ramses | .NET | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 1, nivel: 13) | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.DuplicadoProduceConflicto | aprobada |  |
| Ramses | .NET | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 0, nivel: 1) | aprobada |  |
| Ramses | .NET | PeriodoReglasTest.EditarExcluyeElRegistroPropioYConservaOtrosNumeros | aprobada |  |
| Ramses | .NET | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 12) | aprobada |  |
| Ramses | .NET | PeriodoServiceTest.RechazaFechaFinalAnteriorAlInicioAntesDeGuardar | aprobada |  |
| Ramses | .NET | CursoAuditoriaTest.AltaConservaElRegistroPersistidoEnJson | aprobada |  |
| Ramses | .NET | NuevoAvatar.Academico.Tests.CursoFiltrosTest.CombinaCarreraYNivelEnRepositorio | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/interno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 201) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve401ParaCredencialesInvalidas | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/obtenernotas", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/logout", metodo: "POST", status: 204) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_DependenciaCaidaNoPermiteLlegarAlDestino | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.TokenExpiradoYFirmaAjenaNoSonValidos | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "PROFESOR", email: "p@cuc.ac.cr", profesor: 9, estudiante: null) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer invalido", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "ESTUDIANTE", permitido: "PROFESOR", valido: True, status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/%69nterno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer valido", status: 204) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_RechazaTokenInvalido | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/refresh", metodo: "POST", status: 201) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/refresh", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: False, status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_NoVinculaOtroExpedienteYNoAsumeRolPorId | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_LeeBearerYDevuelveTrue | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/sesion", metodo: "GET", status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "ESTUDIANTE", email: "e@cuc.cr", profesor: null, estudiante: "123") | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve201ParaCredencialesValidas | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_RechazaDatosVacios | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: null, valido: True, status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Sesion_NoConfiaEnHeadersDeIdentidad | aprobada |  |
| Jose | .NET | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: True, status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "admin") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "GET", status: 200) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "GET /usuario/ana", modulo: "ADM4") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM7", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloDeBitacoraNoConfirmaElRegistroDelDashboard | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial académico", modulo: "ADM18") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM7", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Registro sin formato. JSON: {\"ruta\":\"/usuario\""···) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.CorreosSeFiltranPorDestinatarioCompletoAntesDelLimite | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "DELETE", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodos", modulo: "ADM12") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "estudiante") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "estudiante", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.Adm3NoConcedeOtrasConsultasNiEscriturasDeFacturas(metodo: "GET", ruta: "/gateway/fin/factura?periodoId=20") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "PUT", ruta: "/gateway/admin/profesor/1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuNormalNoRegistraVisitaAlDashboard | aprobada |  |
| Fabian | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.Adm3NoConcedeOtrasConsultasNiEscriturasDeFacturas(metodo: "POST", ruta: "/gateway/fin/factura?periodoId=20&vista=dashboard") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta de todos los grupos.", modulo: "ADM13") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ModuloInactivoNiegaApiYSeRetiraDelMenu | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "GET", ruta: "/gateway/mat/prematricula/propio") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "profesor") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "estudiante", estado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "invalido", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM10", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RetirarPermisoBloqueaLaSiguientePeticionInclusoParaAdministrador | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM6", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.Adm3NoConcedeOtrasConsultasNiEscriturasDeFacturas(metodo: "GET", ruta: "/gateway/fin/factura?periodoId=20&vista=dashboard&"···) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: null, ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "PUT", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: True) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM11", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "admin") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "PUT", status: 200) | aprobada |  |
| Fabian | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.Adm3NoConcedeOtrasConsultasNiEscriturasDeFacturas(metodo: "GET", ruta: "/gateway/fin/factura/1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorVeAccionesPropiasYGruposAutorizadosSinDatosAdministrativos | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/profesor") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo 1 de otro módulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo desconocido") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "profesor", estado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/mat/obtenernotas?grupoId=1", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta listado de estudiantes", modulo: "ADM19") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: null, esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta del grupo 12.", modulo: "ADM13") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "POST", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora/1?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta del grupo 12. JSON: {\"ruta\":\"/usuario\"···) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/DOMESTUD") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "profesor", path: "/gateway/admin/usuario") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/expediente/propio", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DependenciaAuthCaidaDevuelve503 | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "invalido", ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta /obtenernotasgrupo", modulo: "ADM17") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora?vista=dashboard&vista=glob"···, esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.AdministradorRecibeDiezEventosDeModulosPermitidosOrdenadosDespuesDeFiltrar | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "{\"descripcion\":\"El usuario consulta /usuario\"}") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "GET") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial academico", modulo: "ADM18") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Referencia a GET /usuario") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 403, invalido: False) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "POST", status: 201) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "refresh") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/interno/datos", status: 404) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notificacion") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "estudiante", path: "/gateway/mat/obtenernotas?grupoId=1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "invalido", esperado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.NotificacionesAdministrativasRequierenPermisoDelModulo | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/profesor/buscar") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "POST", ruta: "/gateway/mat/prematricula/propio") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notas") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/institucion") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "POST", ruta: "/gateway/admin/institucion") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/admin/expediente/123", status: 403) | aprobada |  |
| Fabian | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.Adm3NoConcedeOtrasConsultasNiEscriturasDeFacturas(metodo: "GET", ruta: "/gateway/fin/factura?periodoId=0&vista=dashboard") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: False) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 503, invalido: False) | aprobada |  |
| Fabian | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.IndicadorDeFacturasExigePerfilAdministrador | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/1?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/historial", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/admin/bitacora?vista=dashboard") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "PUT") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM7", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 200, invalido: True) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.MenuPropioTests.UsaElIdRealDelRolDeLaCuentaYNuncaElRolDeLaQuery | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM10", metodo: "GET", ruta: "/gateway/admin/carrera") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DestinoCaidoDevuelve502SinConfundirConAuth | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "login") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/admin/usuario", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuPropioNoAceptaRolDeQueryYNuncaRequierePermisoAdministrativo | aprobada |  |
| Fabian | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.Adm3ConsultaSoloElConteoDeFacturasDeUnPeriodo | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "profesor") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/fin/notificar/historial?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ModuloInactivoNoConcedeDependencias | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "bitacora") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.DatosVaciosDevuelvenListasVacias | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta de grupos y periodos") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "invalido", estado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodo 15", modulo: "ADM12") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "DELETE", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM17", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RoutingDeTodasLasFamiliasTieneDestinoYPolitica | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/mat/prematricula/propio", status: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "DELETE", status: 204) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM22", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/fin/notificar/historial?vista=dashboard") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM12", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM10", metodo: "DELETE", ruta: "/gateway/admin/carrera/1") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.MenuPropioTests.SinIdentidadNoConsultaNingunRol | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/%69nterno/datos", status: 404) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/menu/ADMIN") | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: null, estado: 401) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Jose | .NET | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ServicioNuevoSinContratoDeModuloNoPuedeQuedarExentoAccidentalmente | aprobada |  |
| Fabian | Navegador principal | dashboard.spec.js: ADM3 incluye facturas pendientes de períodos cerrados en el indicador | fallida | Error: expect(locator).toContainText(expected) failed<br><br>Locator: getByRole('region', { name: 'Bitácoras recientes' })<br>Expected substring: "Consulta · Usuarios"<br>Received string:    "Bitácoras recientesMóduloAcciónUsuarioFechaUsuariosConsultaadmin@cuc.ac.cr8/11/2026, 6:00:00 a. m."<br>Timeout: 5000ms<br><br>Call log:<br>  - Expect "toContainText" getByRole('region', { name: 'Bitácoras recientes' }) with timeout 5000ms<br>  - waiting for getByRole('region', { name: 'Bitácoras recientes' })<br>    14 × locator resolved to <section class="panel" aria-label="Bitácoras recientes">…</section><br>       - unexpected value "Bitácoras recientesMóduloAcciónUsuarioFechaUsuariosConsultaadmin@cuc.ac.cr8/11/2026, 6:00:00 a. m."<br><br><br>  43 &#124;     await expect(metricas.getByRole('heading', { name: 'Facturas pendientes' }).locator('..')).toContainText('2')<br>  44 &#124;     await expect(page.getByText('Aviso académico', { exact: false })).toBeVisible()<br>> 45 &#124;     await expect(page.getByRole('region', { name: 'Bitácoras recientes' })).toContainText('Consulta · Usuarios')<br>     &#124;                                                                             ^<br>  46 &#124;     expect(visitas).toBe(1)<br>  47 &#124;     await expect(page.getByRole('link', { name: 'Ayuda/soporte' })).toHaveAttribute('href', 'mailto:soportenuevoavatar@gmail.com')<br>  48 &#124;     await expect(page.getByRole('link', { name: 'Mi perfil' })).toHaveAttribute('href', '/perfil')<br>    at C:\Users\Personal\Desktop\PrograV\NuevoAvatar.Estudiante.React\tests\ui\dashboard.spec.js:45:77 |
| Fabian | Navegador principal | dashboard.spec.js: ADM3 presenta los grupos activos del profesor | aprobada |  |
| Fabian | Navegador principal | dashboard.spec.js: ADM3 informa el fallo de auditoría y de grupos sin ocultar soporte | aprobada |  |
| Fabian | Navegador principal | dashboard.spec.js: ADM3 muestra al profesor los resúmenes del servidor sin controles de filtrado | fallida | Error: expect(locator).toHaveCount(expected) failed<br><br>Locator:  getByRole('region', { name: 'Bitácoras recientes' }).getByRole('listitem')<br>Expected: 10<br>Received: 0<br>Timeout:  5000ms<br><br>Call log:<br>  - Expect "toHaveCount" getByRole('region', { name: 'Bitácoras recientes' }).getByRole('listitem') with timeout 5000ms<br>  - waiting for getByRole('region', { name: 'Bitácoras recientes' }).getByRole('listitem')<br>    13 × locator resolved to 0 elements<br>       - unexpected value "0"<br><br><br>  129 &#124;     const bitacoras = page.getByRole('region', { name: 'Bitácoras recientes' })<br>  130 &#124;     const notificaciones = page.getByRole('region', { name: 'Notificaciones recientes' })<br>> 131 &#124;     await expect(bitacoras.getByRole('listitem')).toHaveCount(10)<br>      &#124;                                                   ^<br>  132 &#124;     await expect(notificaciones).toContainText('Aviso para el profesor')<br>  133 &#124;     await expect(bitacoras.locator('input, select, button')).toHaveCount(0)<br>  134 &#124;     await expect(notificaciones.locator('input, select, button')).toHaveCount(0)<br>    at C:\Users\Personal\Desktop\PrograV\NuevoAvatar.Estudiante.React\tests\ui\dashboard.spec.js:131:51 |
| Fabian | Navegador principal | instituciones.spec.js: ADM8 permite buscar y mantener instituciones desde la sesión | fallida | Test timeout of 30000ms exceeded. |
| Fabian | Navegador principal | instituciones.spec.js: ADM8 muestra dependencia y no abre la pantalla sin permiso | fallida | Test timeout of 30000ms exceeded.<br>Error: locator.click: Test timeout of 30000ms exceeded.<br>Call log:<br>  - waiting for getByRole('row').filter({ hasText: 'Instituto Norte' }).getByRole('button', { name: 'Eliminar' })<br><br><br>  82 &#124;     page.on('dialog', dialog => dialog.accept())<br>  83 &#124;     await page.goto('/instituciones')<br>> 84 &#124;     await page.getByRole('row').filter({ hasText: 'Instituto Norte' }).getByRole('button', { name: 'Eliminar' }).click()<br>     &#124;                                                                                                                  ^<br>  85 &#124;     await page.getByRole('dialog').getByRole('button', { name: 'Sí', exact: true }).click()<br>  86 &#124;     await expect(page.getByRole('alert')).toContainText('La institución tiene carreras relacionadas')<br>  87 &#124;     await preparar(page, false)<br>    at C:\Users\Personal\Desktop\PrograV\NuevoAvatar.Estudiante.React\tests\ui\instituciones.spec.js:84:114 |
| Fabian | Navegador principal | instituciones.spec.js: ADM8 mantiene el listado detrás del modal, valida y cancela sin guardar | fallida | Test timeout of 30000ms exceeded. |
| Jose | Navegador principal | notas.spec.js: profesor navega a notas con layout, ruta activa, breadcrumbs y regreso | aprobada |  |
| Jose | Navegador principal | notas.spec.js: ADMINISTRADOR no ve acceso ni ejecuta consultas de notas mediante URL directa | aprobada |  |
| Jose | Navegador principal | notas.spec.js: ESTUDIANTE no ve acceso ni ejecuta consultas de notas mediante URL directa | aprobada |  |
| Jose | Navegador principal | notas.spec.js: asignar y modificar nota desde el layout conserva verbos y bloqueo del desglose | aprobada |  |
| Jose | Navegador principal | notas.spec.js: carga de desglose usa el cliente compartido y refresca el resultado en el layout | aprobada |  |
| Jose | Navegador principal | notas.spec.js: fallo de consulta no habilita escritura y permite reintentar sin perder sesión | aprobada |  |
| Jose | Navegador principal | notas.spec.js: profesor sin grupos ve el estado vacío dentro del layout | aprobada |  |
| Jose | Navegador principal | notas.spec.js: sesión rechazada al consultar notas abandona el layout y limpia credenciales | aprobada |  |
| Jose | Navegador principal | notas.spec.js: salir desde notas retira el módulo; volver atrás no recupera acceso | aprobada |  |
| Jose | Navegador principal | notas.spec.js: profesor sin ADM17 por URL directa no consulta notas y no ve el acceso | aprobada |  |
| Fabian | Navegador principal | parametros.spec.js: ADM6 usa el servicio para crear, editar y eliminar parámetros | fallida | Test timeout of 30000ms exceeded. |
| Ramses | Navegador principal | ramses.spec.js: ADM7 abre su mantenimiento y formulario en la sesión compartida | fallida | Test timeout of 30000ms exceeded. |
| Ramses | Navegador principal | ramses.spec.js: ADM9 abre su mantenimiento y formulario en la sesión compartida | fallida | Test timeout of 30000ms exceeded. |
| Ramses | Navegador principal | ramses.spec.js: ADM10 abre su mantenimiento y formulario en la sesión compartida | fallida | Test timeout of 30000ms exceeded. |
| Ramses | Navegador principal | ramses.spec.js: ADM11 abre su mantenimiento y formulario en la sesión compartida | fallida | Test timeout of 30000ms exceeded. |
| Ramses | Navegador principal | ramses.spec.js: ADM12 abre su mantenimiento y formulario en la sesión compartida | fallida | Test timeout of 30000ms exceeded. |
| Ramses | Navegador principal | ramses.spec.js: ADM22 abre su mantenimiento y formulario en la sesión compartida | fallida | Test timeout of 30000ms exceeded. |
| Ramses | Navegador principal | ramses.spec.js: ADM7 respeta TAMPAG y permite navegar sin mostrar más de quince filas | fallida | Test timeout of 30000ms exceeded. |
| Ramses | Navegador principal | ramses.spec.js: sin ADM7 la ruta directa no consulta ni ofrece escritura de módulos | aprobada |  |
| Ramses | Navegador principal | ramses.spec.js: ADM7 conserva el foco al escribir y muestra validaciones dentro del diálogo | fallida | Test timeout of 30000ms exceeded. |
| Ramses | Navegador principal | ramses.spec.js: ADM7 muestra el registro guardado en su página y confirma su eliminación | fallida | Test timeout of 30000ms exceeded.<br>Error: locator.click: Test timeout of 30000ms exceeded.<br>Call log:<br>  - waiting for getByRole('button', { name: 'Nuevo módulo', exact: true })<br>    - locator resolved to <button class="btn btn-primary">Nuevo módulo</button><br>  - attempting click action<br>    2 × waiting for element to be visible, enabled and stable<br>      - element is visible, enabled and stable<br>      - scrolling into view if needed<br>      - done scrolling<br>      - <dialog open="" aria-labelledby="_r_0_" class="panel mantenimiento-modal">…</dialog> intercepts pointer events<br>    - retrying click action<br>    - waiting 20ms<br>    2 × waiting for element to be visible, enabled and stable<br>      - element is visible, enabled and stable<br>      - scrolling into view if needed<br>      - done scrolling<br>      - <dialog open="" aria-labelledby="_r_0_" class="panel mantenimiento-modal">…</dialog> intercepts pointer events<br>    - retrying click action<br>      - waiting 100ms<br>    55 × waiting for element to be visible, enabled and stable<br>       - element is visible, enabled and stable<br>       - scrolling into view if needed<br>       - done scrolling<br>       - <dialog open="" aria-labelledby="_r_0_" class="panel mantenimiento-modal">…</dialog> intercepts pointer events<br>     - retrying click action<br>       - waiting 500ms<br>    - waiting for element to be visible, enabled and stable<br>    - element is visible, enabled and stable<br>    - scrolling into view if needed<br>    - done scrolling<br><br><br>   99 &#124;     await preparar(p… (traza extensa retirada; consultar el hallazgo en el informe) |
| Fabian | Navegador principal | roles.spec.js: ADM5 guarda un cambio de permisos para un módulo y vuelve a leerlo | fallida | Test timeout of 30000ms exceeded. |
| Jose | Navegador principal | sesion.spec.js: login ADMINISTRADOR llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: login PROFESOR llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: login ESTUDIANTE llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: rutas directas sin sesión vuelven al login sin mostrar identidad de ejemplo | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: credenciales inválidas no abren el layout | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: identidad rechazada después del login no conserva tokens ni abre el layout | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: renovación programada mantiene el layout y rota los tokens sin volver al login | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: refresh rechazado retira la identidad y vuelve al login con aviso de expiración | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: token vencido guardado se renueva una sola vez al restaurar bajo StrictMode | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: caída al restaurar conserva tokens, oculta el layout y permite reintentar | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: salir limpia la cuenta aunque Auth falle; volver atrás no recupera el layout | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: salir mientras se renueva impide que la respuesta tardía restaure la cuenta | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: perfil no reconocido en la identidad no accede al layout | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: el estudiante no abre una ruta administrativa de notas | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: login móvil conserva formulario visible sin desbordamiento horizontal | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: retiro de ADM17 se refleja en el menú al recargar | aprobada |  |
| Jose | Navegador principal | sesion.spec.js: caída de permisos durante restauración oculta el layout y permite recuperar | aprobada |  |
| Fabian | Navegador principal | usuarios.spec.js: ADM4 crea y edita usuarios usando el ID real del rol | fallida | Test timeout of 30000ms exceeded. |
| Jose | Navegador aislado | jose.spec.js: login invalido no entra y muestra mensaje requerido | aprobada |  |
| Jose | Navegador aislado | jose.spec.js: profesor crea y modifica nota con verbos distintos y rubros quedan bloqueados | aprobada |  |
| Jose | Navegador aislado | jose.spec.js: sin desglose permite carga confirmada y refresca resultado | aprobada |  |
| Jose | Navegador aislado | jose.spec.js: dependencia caida no muestra vacio ni permite guardar | aprobada |  |
| Jose | Navegador aislado | jose.spec.js: estudiante no obtiene controles de notas y logout limpia almacenamiento | aprobada |  |
| Jose | Navegador aislado | jose.spec.js: login utilizable en pantalla movil sin desbordamiento horizontal | aprobada |  |
| Ramses | Navegador aislado | modulos.spec.js: el modal bloquea el fondo y restaura el foco al cerrar | aprobada |  |
| Ramses | Navegador aislado | modulos.spec.js: abre el mantenimiento de profesores en un modal bloqueante | aprobada |  |
| Ramses | Navegador aislado | modulos.spec.js: abre el mantenimiento de carreras con selectores reales | aprobada |  |
| Ramses | Navegador aislado | modulos.spec.js: abre el mantenimiento de cursos con carrera y nivel | aprobada |  |
| Ramses | Navegador aislado | modulos.spec.js: abre el mantenimiento de periodos con fechas | aprobada |  |
| Ramses | Navegador aislado | modulos.spec.js: abre composición de notificaciones en modal | aprobada |  |
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
| Alejandro | Recorrido estudiantil | A-WEB1: Inicio con expediente y solicitudes propias | fallida | locator.waitFor: Timeout 6000ms exceeded.<br>Call log:<br>  - waiting for getByText('Expediente vinculado', { exact: true }) to be visible<br>    - found getByRole('heading', { name: 'Bienvenido a Nuevo Avatar' }), intercepting action to run the handler<br>    - locator handler has finished, waiting for getByRole('heading', { name: 'Bienvenido a Nuevo Avatar' }) to be hidden<br>    - interception handler has finished, continuing<br> |
| Alejandro | Recorrido estudiantil | A-WEB1-sin-vinculo: Inicio informa sin-vinculo | fallida | La tarjeta no comunica el estado sin-vinculo: ▤<br>Mi expediente<br>Abrir mi expediente<br>→ |
| Alejandro | Recorrido estudiantil | A-WEB1-vacio: Inicio informa vacio | fallida | La tarjeta no comunica el estado vacio: ▧<br>Mis prematrículas<br>Abrir mis prematrículas<br>→ |
| Alejandro | Recorrido estudiantil | A-WEB1-dependencia-caida: Inicio informa dependencia-caida | fallida | La tarjeta no comunica el estado dependencia-caida: ▤<br>Mi expediente<br>Abrir mi expediente<br>→ |
| Alejandro | Recorrido estudiantil | A-ADM1-ESTUDIANTE-390: Plantilla ESTUDIANTE sin desbordamiento a 390px | aprobada |  |
| Alejandro | Recorrido estudiantil | A-ADM1-ESTUDIANTE-768: Plantilla ESTUDIANTE sin desbordamiento a 768px | aprobada |  |
| Alejandro | Recorrido estudiantil | A-ADM1-ESTUDIANTE-1366: Plantilla ESTUDIANTE sin desbordamiento a 1366px | aprobada |  |
| Alejandro | Recorrido estudiantil | A-ADM1-ADMINISTRADOR-390: Plantilla ADMINISTRADOR sin desbordamiento a 390px | aprobada |  |
| Alejandro | Recorrido estudiantil | A-ADM1-ADMINISTRADOR-768: Plantilla ADMINISTRADOR sin desbordamiento a 768px | aprobada |  |
| Alejandro | Recorrido estudiantil | A-ADM1-ADMINISTRADOR-1366: Plantilla ADMINISTRADOR sin desbordamiento a 1366px | aprobada |  |
| Alejandro | Recorrido estudiantil | A-WEB2-confirmacion: Expediente espera confirmación y permite volver sin PUT | aprobada |  |
| Alejandro | Recorrido estudiantil | A-ADM1-ruta-activa: Solo una opción del menú estudiantil queda activa en expediente | fallida | Opciones activas: ⌂Inicio, ▤Mi expediente<br><br>2 !== 1<br> |
| Alejandro | Recorrido estudiantil | A-ADM1-identidad-movil: La barra de usuario muestra nombre y rol en móvil | fallida | El CSS oculta .user-info en anchos de hasta 600px; nombre y rol no son visibles en la barra. |
| Alejandro | Recorrido estudiantil | A-ADM1-pie: La plantilla conserva su pie de página en Mi expediente | fallida | StudentLayout desactiva mostrarFooter para expediente y perfil; en expediente no se renderiza el pie común.<br><br>0 !== 1<br> |
| Alejandro | Recorrido estudiantil | A-WEB4-confirmacion: Cancelar solicitud requiere confirmar antes de DELETE | aprobada |  |
| Alejandro | Recorrido estudiantil | A-WEB4-TAMPAG: Prematrícula respeta TAMPAG=2 | fallida | TAMPAG=2 preparado; se muestran 10 solicitudes. El componente usa una constante de 10.<br><br>10 !== 2<br> |
| Alejandro | Recorrido estudiantil | A-ADM24-salida: Salir limpia la sesión de las pestañas del mismo navegador | aprobada |  |

Entradas excluidas del corte:

- {"caso": "GrupoServiceTest.RechazaCursoInexistenteAntesDeGuardar", "estado": "Passed", "motivo": "Comprobación heredada fuera de las veinte historias del corte"}
- {"caso": "ValidacionesRequestTest.GrupoExigeIdentificadoresHorarioYCupo", "estado": "Passed", "motivo": "Comprobación heredada fuera de las veinte historias del corte"}

## recorrido-estudiante

Procedencia: `recorrido-estudiante.json`, retirado después de esta transcripción.

Transcribí **17 entradas**. Las entradas excluidas o de preparación no equivalen a casos incluidos en las barras.

| Integrante | Grupo | Caso | Estado | Observación registrada |
| --- | --- | --- | --- | --- |
| Alejandro | Recorrido | Inicio con expediente y solicitudes propias | fallida | locator.waitFor: Timeout 6000ms exceeded.<br>Call log:<br>[2m  - waiting for getByText('Expediente vinculado', { exact: true }) to be visible[22m<br>[2m    - found getByRole('heading', { name: 'Bienvenido a Nuevo Avatar' }), intercepting action to run the handler[22m<br>[2m    - locator handler has finished, waiting for getByRole('heading', { name: 'Bienvenido a Nuevo Avatar' }) to be hidden[22m<br>[2m    - interception handler has finished, continuing[22m<br> |
| Alejandro | Recorrido | Inicio informa sin-vinculo | fallida | La tarjeta no comunica el estado sin-vinculo: ▤<br>Mi expediente<br>Abrir mi expediente<br>→ |
| Alejandro | Recorrido | Inicio informa vacio | fallida | La tarjeta no comunica el estado vacio: ▧<br>Mis prematrículas<br>Abrir mis prematrículas<br>→ |
| Alejandro | Recorrido | Inicio informa dependencia-caida | fallida | La tarjeta no comunica el estado dependencia-caida: ▤<br>Mi expediente<br>Abrir mi expediente<br>→ |
| Alejandro | Recorrido | Plantilla ESTUDIANTE sin desbordamiento a 390px | aprobada | {"contenido": 390, "ventana": 390} |
| Alejandro | Recorrido | Plantilla ESTUDIANTE sin desbordamiento a 768px | aprobada | {"contenido": 768, "ventana": 768} |
| Alejandro | Recorrido | Plantilla ESTUDIANTE sin desbordamiento a 1366px | aprobada | {"contenido": 1366, "ventana": 1366} |
| Alejandro | Recorrido | Plantilla ADMINISTRADOR sin desbordamiento a 390px | aprobada | {"contenido": 390, "ventana": 390} |
| Alejandro | Recorrido | Plantilla ADMINISTRADOR sin desbordamiento a 768px | aprobada | {"contenido": 768, "ventana": 768} |
| Alejandro | Recorrido | Plantilla ADMINISTRADOR sin desbordamiento a 1366px | aprobada | {"contenido": 1366, "ventana": 1366} |
| Alejandro | Recorrido | Expediente espera confirmación y permite volver sin PUT | aprobada | Confirmación y regreso sin enviar actualización. |
| Alejandro | Recorrido | Solo una opción del menú estudiantil queda activa en expediente | fallida | Opciones activas: ⌂Inicio, ▤Mi expediente<br><br>2 !== 1<br> |
| Alejandro | Recorrido | La barra de usuario muestra nombre y rol en móvil | fallida | El CSS oculta .user-info en anchos de hasta 600px; nombre y rol no son visibles en la barra. |
| Alejandro | Recorrido | La plantilla conserva su pie de página en Mi expediente | fallida | StudentLayout desactiva mostrarFooter para expediente y perfil; en expediente no se renderiza el pie común.<br><br>0 !== 1<br> |
| Alejandro | Recorrido | Cancelar solicitud requiere confirmar antes de DELETE | aprobada | Se ofrece confirmación sin eliminar todavía. |
| Alejandro | Recorrido | Prematrícula respeta TAMPAG=2 | fallida | TAMPAG=2 preparado; se muestran 10 solicitudes. El componente usa una constante de 10.<br><br>10 !== 2<br> |
| Alejandro | Recorrido | Salir limpia la sesión de las pestañas del mismo navegador | aprobada | La segunda pestaña solicita login y limpia tokens. |

## Observaciones

Los nombres de archivos auxiliares citados en el informe identifican la procedencia histórica. Sus resultados esenciales permanecen en las tablas anteriores y en el informe de evidencias. Retiré los logs, JSON, TRX y guiones temporales; las pruebas mantenidas en el código del proyecto siguen en sus carpetas originales. Para repetir el recorrido propio de auditoría será necesario preparar un nuevo ejecutor o seguir sus pasos documentados.

### Consultas de solo lectura

| Servicio | Consulta | Resultado |
| --- | --- | --- |
| Parametro | politica_y_paginacion | [{"id_parametro": "PASSMAX", "valor": "30"}, {"id_parametro": "PASSMAY", "valor": "true"}, {"id_parametro": "PASSMIN", "valor": "8"}, {"id_parametro": "PASSNUM", "valor": "true"}] |
| Parametro | smtp_presencia_sin_valores | [{"id_parametro": "MAILFROM", "configurado": 1}, {"id_parametro": "MAILHOST", "configurado": 1}, {"id_parametro": "MAILPASS", "configurado": 1}, {"id_parametro": "MAILPORT", "configurado": 1}, {"id_parametro": "MAILTLS", "configurado": 1}, {"id_parametro": "MAILUSER", "configurado": 1}] |
| Modulo | permisos_fuera_del_compromiso | [{"asignaciones": 1}] |
| Notificacion | tabla_historial | [{"tablas": 1}] |
| Rol | unicidad_roles | [{"indices": 1}] |
| Prematricula | indices_duplicados | [{"tabla": "Prematricula", "indice": "PK_Prematricula", "columna": "PrematriculaId"}, {"tabla": "PrematriculaCurso", "indice": "PK_PrematriculaCurso", "columna": "PrematriculaCursoId"}] |

Los puertos estaban detenidos durante esta inspección inicial. El recorrido real posterior se documenta por separado.

### Ejecuciones registradas

| Ejecución | Resultado o código de salida |
| --- | --- |
| dotnet | 0 |
| java | 1 |
| ui-principal-inicial | 11 aprobadas, 5 fallidas y 31 omitidas; límite global agotado. Sustituida para el conteo por la ejecución completa. |
| ui-principal-completa-diagnostico | 30 aprobadas, 17 fallidas; se confirmó después con tiempo normal de 30 segundos por caso. No sumo ambos intentos. |
| ui-principal-confirmada | {"startTime": "2026-11-12T17:24:26.788Z", "duration": 301492.184, "expected": 30, "skipped": 0, "unexpected": 17, "flaky": 0} |
| recorrido-estudiante | {"fallida": 8, "aprobada": 9} |
| inspeccion | Seis consultas SELECT a SQL compartido, inspección de enlaces y puertos; sin cambios de datos. |
| limites-servicios | {"passed": true, "services": 22, "source_files": 298, "failures": [], "scope": "Accesos SQL explicitos y entidades JPA; complementa las cuentas SQL y las pruebas HTTP."} |
| ui-jose | 0 |
| ui-ramses | 0 |
| node-jose | 0 |
| node-ramses | 0 |
| build-principal | 0 |
| lint-principal | 0 |
| build-jose | 0 |
| build-ramses | 0 |
| artefactos | 0 |

La inspección de límites encontró 22 servicios y 298 fuentes sin infracciones detectadas. El validador aceptó trece JAR; las tres compilaciones React aprobaron y el lint terminó con 18 advertencias. Estas verificaciones no acreditan por sí solas aceptación integral.
