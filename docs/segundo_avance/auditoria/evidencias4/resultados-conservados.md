# Resultados conservados en Markdown

Fecha de organización: 12 de noviembre de 2026.

Como equipo decidimos conservar los informes y las capturas para presentar la auditoría. Trasladé aquí los casos y sus estados antes de retirar los archivos auxiliares. Esta organización no representa una nueva ejecución ni cambia el avance de los aportes. Los antecedentes y las repeticiones siguen separados; no deben sumarse entre sí.

El [informe de evidencias](README.md) explica las condiciones, los fallos y los límites de aceptación. Ya no conservamos en esta carpeta las trazas completas ni los ejecutores temporales.

## resultados-actuales

Procedencia: `resultados_actuales.json`, retirado después de esta transcripción.

Fecha: 2026-11-10

Alcance: Segundo avance: 20 HU, 120 puntos

Limites: ["Una ejecucion final por caso; no se suman repeticiones ni resultados anteriores.", "Solo fixtures/H2/dobles y destinos controlados; sin SQL Server o SMTP reales.", "Atribucion por HU vigente; las pruebas no representan aporte ni cobertura integral."]

Resumen registrado por integrante (conservo las exclusiones del informe original):

| Integrante | Resultado registrado |
| --- | --- |
| Jose | {"aprobada": 155} |
| Ramses | {"aprobada": 78} |
| Alejandro | {"aprobada": 217} |
| Fabian | {"aprobada": 95, "fallida": 2} |
| Compartida | {"aprobada": 3} |

Transcribí **550 entradas**. Las entradas excluidas o de preparación no equivalen a casos incluidos en las barras.

| Integrante | Grupo | Caso | Estado | Observación registrada |
| --- | --- | --- | --- | --- |
| Compartida | integracion | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.permiteArchivoLocalVacio | aprobada |  |
| Compartida | integracion | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.incluyeContratosEnClasspath | aprobada |  |
| Compartida | integracion | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.encuentraArchivoEnPadresYResuelveValoresEntreComillas | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.IntegracionCompartidaTest.cargaBibliotecaYContratoConSpringBootDelServicioSinBaseReal | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.NuevoAvatarPersona2ApplicationTests.contextLoads | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.aceptaBearerValidadoPorSeguridad | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.rechazaSolicitudSinBearer | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.obtieneElSujetoSoloDespuesDeValidarElBearer | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.registraAmbosEstadosYNoConfirmaCambiosFallidos | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaLasTresCredencialesDelCambioDeContrasena | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaSecretosConComillasEscapadasYCamposAnidados | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.conservaJsonSinSecretos | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaContrasenaYConservaDatosDelRegistro | aprobada |  |
| Alejandro | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.actualizaSoloElNombreDeLaCuentaDerivadaDelSujeto | aprobada |  |
| Alejandro | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.cambiaContrasenaDelSujetoYGuardaSoloHashBcrypt | aprobada |  |
| Alejandro | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.validaLongitudMayusculaMinusculaNumeroYCaracterEspecialEnServidor | aprobada |  |
| Alejandro | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.rechazaContrasenaActualIncorrectaSinPersistir | aprobada |  |
| Ramses | NuevoAvatar.Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.bloqueaEliminarCuandoExistenCursos | aprobada |  |
| Ramses | NuevoAvatar.Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.validaInstitucionYProfesorEnSusServiciosPropietarios | aprobada |  |
| Ramses | NuevoAvatar.Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.registraSnapshotsJsonEnModificacionYEliminacion | aprobada |  |
| Ramses | NuevoAvatar.Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.permiteCuandoTodosLosPeriodosEstanCerrados | aprobada |  |
| Ramses | NuevoAvatar.Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.registraSnapshotsJsonEnBitacoraDeProfesor | aprobada |  |
| Ramses | NuevoAvatar.Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.exigeFormatoLocalDeTelefonosYEvitaDuplicados | aprobada |  |
| Ramses | NuevoAvatar.Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.bloqueaCuandoHoyEstaDentroDelPeriodoInclusivo | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaDistritoDeOtroCanton | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaCantonDeOtraProvincia | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.comunicaFallaDelServicioSinTratarlaComoDireccionInvalida | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaProvinciaInexistente | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.consumeRutasYCamelCaseDelServicioDeAlejandro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[10] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.metodoNoAdmitidoNoSeReportaComoErrorInterno | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identidadEnQueryNoCambiaElExpedientePropio | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaCualquierCampoProtegidoAunqueElRestoSeaValido | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identificacionDuplicadaAlModificarNoPierdeDatos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaRolIncorrectoVinculoAusenteYAmbiguo | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposEditablesObligatoriosSeValidanAntesDePersistir | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identificacionReferenciadaNoSeCambiaEntreServicios | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.errorTecnicoRegistraBitacoraYRevierteEscritura | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.noEliminaEstudianteReferenciadoNiSusTelefonos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.noPermiteMismoNumeroAunqueCambieElTipo | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.estudiantesDiferentesSoloVenYActualizanSuPropioRegistro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.direccionInvalidaNoEscribeDatos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.conservaCerosInicialesYNoExigeCorreoOTelefonosUnicos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.falloGen1RevierteModificacionYEliminacion | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.dependenciasDeAutenticacionNoDisponiblesDevuelven503 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaDireccionIncoherenteYTokenInvalido | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.sqlNoInterpretaLaIdentificacionComoUnaInstruccion | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.dominioCambiaMedianteParametro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.falloGen1RevierteCreacionYTelefonos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.cicloCompletoDeCincoOperacionesYBitacoras | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[10] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[11] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[12] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[13] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[14] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[15] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.recursosInexistentesDevuelven404 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.nombreCorreoDominioYFechaInvalidosSeRechazanEnCrearYModificar | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.consultaYActualizaSoloSuExpedienteConPersistenciaYBitacoraMinima | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[10] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[11] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[12] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[13] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[14] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[15] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[16] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[17] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[18] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[19] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[20] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[21] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[22] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[23] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[24] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[25] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[26] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[27] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[28] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[29] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.noAgregaEdadMinimaNiFormatoDeTelefono | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.utilizaDominioConfiguradoYSinDiferenciarMayusculas | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaExpedienteNulo | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.propagaFalloDelParametro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.conservaUsuarioYTokenValidados | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.responde503CuandoSeguridadNoResponde | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noConfiaEnSubSinValidacionExterna | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.consultaRespetaTextoDelDocumento | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.conservaEstadoAnteriorAunqueElObjetoCambie | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.registraTipoDeErrorSinCopiarMensajeConCredenciales | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.creacionYEliminacionIncluyenRegistro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.validaContratoUsr5 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.comunicaFalloDeConexion | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.enviaBitacoraSinInventarFechaNiIdentificador | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.leeParametroConContratoUsr3 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.distingueFallaDeServicioDeTokenInvalido | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaTokenCon401 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.informaParametroInexistente | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noOcultaFalloAlGuardarBitacora | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.estudiantePuedeCrearConsultarModificarYCancelarSoloConIdentidadDeSesion | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.errorDespuesDeEscribirRevierteYSeRegistraComoMat1 | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.falloGen1RevierteCreacionYCursos | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.noImponeUnicidadNoSolicitadaEntreEstudianteYPeriodo | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.compruebaTodosLosNivelesDesdeLaBase | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.cincoOperacionesConDatosPersistidosYBitacoras | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[10] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[11] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[12] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[13] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[14] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[15] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.modificarPermiteCambiarEstudianteYCarrera | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.reevaluaLaFechaParaCadaEscrituraYPermiteConsultarOEliminar | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.referenciasInexistentesNoGuardanDatos | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.endpointsAdministrativosNoPermitenQueUnEstudianteElijaIdentidadAjena | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.falloGen1RevierteModificacionYEliminacion | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.listaVaciaOCursoNuloNoSeAceptan | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.servicioDePrematriculaNoExponeRutasDeExpediente | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.solicitudPropiaNoAceptaIdentidadElegidaYNoExponeSolicitudesDeOtroEstudiante | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesPuedenOmitirseAlCrear | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.recursoAusenteIdInvalidoYMetodoNoAdmitidoTienenRespuestasRest | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.rechazaPeriodoQueIniciaHoyOPasadoAunqueSolicitanteEnvieOtraFecha(int)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.rechazaPeriodoQueIniciaHoyOPasadoAunqueSolicitanteEnvieOtraFecha(int)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.opcionesPropiasSoloIncluyenCursosDePrimerNivelYPeriodosFuturos | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.necesitaFechaDelServidorParaEvaluarPeriodo | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosNulos | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.evaluaFechaDeCadaOperacionSinConservarUnHoyFijo | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.comparaFechasAlCambiarDeAnio | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaUnSoloCursoDePrimerNivel | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[2] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.rollbackDeRubrosNoEnviaBitacoraDeExito | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.reemplazoDeRubrosAuditaAntesYDespuesTrasCommit | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.altaDeRubrosAuditaJsonNuevoTrasCommit | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.rollbackNoEnviaBitacoraDeExito | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.enviaAntesYDespuesSoloTrasConfirmarTransaccion | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.fueraDeTransaccionNoIntentaEscribir | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[1] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[2] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[3] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[4] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[5] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[6] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.conexionEnAutocommitNoPuedeProtegerLaOperacion | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.primeraNotaConfirmadaImpideReemplazarElDesgloseConcurrente | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.dosPostConcurrentesNoSobrescribenNiDuplicanLaPrimeraNota | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.reemplazoConfirmadoRechazaLaNotaDelRubroQueYaNoExiste | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[1] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[2] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[3] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.putNoCreaUnaNotaInexistente | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.falloAlGuardarNoPublicaExitoEnBitacora | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.modificacionConservaSnapshotAnterior | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.postNoSobrescribeNotaExistente | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.creaEnLimitesYAuditaResultadoPersistido(String)[1] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.creaEnLimitesYAuditaResultadoPersistido(String)[2] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.sinTokenNoPuedeConsultarGrupo | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.grupoInvalidoDevuelve400 | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.profesorPropioUsaOperacionCorrecta | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.estudianteNoPuedeAsignarNotaPorHttp | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.profesorAjenoNoPuedeModificarNotaPorHttp | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.notaInvalidaNoLlegaALaEscritura | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.bloqueaCambioDeRubrosCuandoYaExistenNotas | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.rechazaDesgloseQueNoSumaCien | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.reemplazoAuditaSnapshotPrevioYResultadoPersistido | aprobada |  |
| Fabian | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.resumenConsultaSoloProfesorPropioYPeriodoActivo | aprobada |  |
| Fabian | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.gruposDePeriodosCerradosNoAparecenEnResumenActual | aprobada |  |
| Fabian | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.falloDeGrupoNoSePresentaComoResumenSinDatos | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.dependenciaCaidaNoSeConvierteEnGrupoVacio | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.interceptorUsaIdentidadValidadaYNoElPayloadDelNavegador | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.profesorPuedeAccederSoloAGrupoPropio | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.tokenExpiradoNoResuelveIdentidad | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.matriculaConPeriodoDiferenteEsRechazada | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.estudianteNoPuedeEscribirNiConsultarNotasDelProfesor | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.profesorNoPuedeConsultarOtroGrupo | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.headersDelClienteNoOtorganIdentidad | aprobada |  |
| Ramses | NuevoAvatar.Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.guardaHistorialEnErrorCuandoFallaTransporteSmtp | aprobada |  |
| Ramses | NuevoAvatar.Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.registraErrorEnHistorialCuandoFaltanParametrosSmtp | aprobada |  |
| Ramses | NuevoAvatar.Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.informaEnvioRealizadoAunqueFalleGuardadoDeHistorial | aprobada |  |
| Ramses | NuevoAvatar.Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.sanitizacionEliminaScriptsYAtributosPeligrosos | aprobada |  |
| Ramses | NuevoAvatar.Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.rechazaDestinatariosInvalidos | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 503, invalido: False) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DependenciaAuthCaidaDevuelve503 | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloDeBitacoraNoConfirmaElRegistroDelDashboard | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 403, invalido: False) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "estudiante", path: "/gateway/mat/obtenernotas?grupoId=1") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "admin") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/menu/ADMIN") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "GET", ruta: "/gateway/mat/prematricula/propio") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta del grupo 12. JSON: {\"ruta\":\"/usuario\"···) | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM7", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "POST", ruta: "/gateway/admin/institucion") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM10", metodo: "DELETE", ruta: "/gateway/admin/carrera/1") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.MenuPropioTests.SinIdentidadNoConsultaNingunRol | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuNormalNoRegistraVisitaAlDashboard | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/interno/datos", status: 404) | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM7", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM22", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "profesor") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial academico", modulo: "ADM18") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "invalido", esperado: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora/1?vista=dashboard", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo desconocido") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta de grupos y periodos") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ModuloInactivoNoConcedeDependencias | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "invalido", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "DELETE", status: 204) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notas") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "estudiante") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "PUT") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "profesor", estado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "{\"descripcion\":\"El usuario consulta /usuario\"}") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora?vista=dashboard&vista=glob"···, esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta /obtenernotasgrupo", modulo: "ADM17") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: null, ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/admin/expediente/123", status: 403) | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM7", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: True) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "PUT", ruta: "/gateway/admin/profesor/1") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "GET /usuario/ana", modulo: "ADM4") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "DELETE", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notificacion") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.DatosVaciosDevuelvenListasVacias | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/admin/usuario", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodos", modulo: "ADM12") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 200, invalido: True) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta listado de estudiantes", modulo: "ADM19") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta de todos los grupos.", modulo: "ADM13") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial académico", modulo: "ADM18") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM12", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "PUT", status: 200) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles/ADMIN") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: null, estado: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RoutingDeTodasLasFamiliasTieneDestinoYPolitica | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorVeAccionesPropiasYGruposAutorizadosSinDatosAdministrativos | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "DELETE", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/institucion") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "profesor") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "profesor", path: "/gateway/admin/usuario") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "admin") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta del grupo 12.", modulo: "ADM13") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "POST", status: 201) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "estudiante", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "POST", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuPropioNoAceptaRolDeQueryYNuncaRequierePermisoAdministrativo | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "GET", status: 200) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "PUT", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: False) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "invalido", ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo 1 de otro módulo") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/1?vista=dashboard", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "estudiante", estado: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "refresh") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.AdministradorRecibeDiezEventosDeModulosPermitidosOrdenadosDespuesDeFiltrar | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ModuloInactivoNiegaApiYSeRetiraDelMenu | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: null, esperado: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM6", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DestinoCaidoDevuelve502SinConfundirConAuth | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM10", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/fin/notificar/historial?vista=dashboard") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodo 15", modulo: "ADM12") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/%69nterno/datos", status: 404) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/admin/bitacora?vista=dashboard") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RetirarPermisoBloqueaLaSiguientePeticionInclusoParaAdministrador | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM11", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/historial", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.NotificacionesAdministrativasRequierenPermisoDelModulo | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/fin/notificar/historial?vista=dashboard", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "bitacora") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/mat/obtenernotas?grupoId=1", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.CorreosSeFiltranPorDestinatarioCompletoAntesDelLimite | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "GET") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.MenuPropioTests.UsaElIdRealDelRolDeLaCuentaYNuncaElRolDeLaQuery | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/DOMESTUD") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ServicioNuevoSinContratoDeModuloNoPuedeQuedarExentoAccidentalmente | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM10", metodo: "GET", ruta: "/gateway/admin/carrera") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "login") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/profesor/buscar") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/expediente/propio", status: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM17", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Referencia a GET /usuario") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "invalido", estado: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "POST", ruta: "/gateway/mat/prematricula/propio") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/mat/prematricula/propio", status: 401) | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/profesor") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Registro sin formato. JSON: {\"ruta\":\"/usuario\""···) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: False, status: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "ESTUDIANTE", email: "e@cuc.cr", profesor: null, estudiante: "123") | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_NoVinculaOtroExpedienteYNoAsumeRolPorId | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: True, status: 200) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer valido", status: 204) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "", status: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve201ParaCredencialesValidas | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/%69nterno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/refresh", metodo: "POST", status: 201) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Sesion_NoConfiaEnHeadersDeIdentidad | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer invalido", status: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.TokenExpiradoYFirmaAjenaNoSonValidos | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "ESTUDIANTE", permitido: "PROFESOR", valido: True, status: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_DependenciaCaidaNoPermiteLlegarAlDestino | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve401ParaCredencialesInvalidas | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "PROFESOR", email: "p@cuc.ac.cr", profesor: 9, estudiante: null) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_LeeBearerYDevuelveTrue | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/logout", metodo: "POST", status: 204) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 201) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_RechazaDatosVacios | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_RechazaTokenInvalido | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: null, valido: True, status: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/sesion", metodo: "GET", status: 200) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/interno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/refresh", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/obtenernotas", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.PeriodoExigeDatos | aprobada |  |
| Ramses | dotnet | PeriodoReglasTest.EstadoSeDerivaDeFechas | aprobada |  |
| Ramses | dotnet | PeriodoServiceTest.ContratoActualPermiteFechasIguales | aprobada |  |
| Ramses | dotnet | PeriodoReglasTest.EditarExcluyeElRegistroPropioYConservaOtrosNumeros | aprobada |  |
| Ramses | dotnet | CursoAuditoriaTest.EliminacionConservaElRegistroEliminado | aprobada |  |
| Ramses | dotnet | PeriodoReglasTest.PermiteFechasSuperpuestasSiElNumeroEsDistinto | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 1) | aprobada |  |
| Ramses | dotnet | PeriodoServiceTest.RechazaFechaFinalAnteriorAlInicioAntesDeGuardar | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 1, nombre: "Programación 5") | aprobada |  |
| Ramses | dotnet | CursoAuditoriaTest.ModificacionConservaAnteriorYActual | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 0, nivel: 1) | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Academico.Tests.CursoFiltrosTest.CombinaCarreraYNivelEnRepositorio | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 12) | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 13, nombre: "Programación") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 1, nivel: 13) | aprobada |  |
| Ramses | dotnet | PeriodoReglasTest.DosAltasEnLaMismaInstanciaNoDuplicanElPeriodo | aprobada |  |
| Ramses | dotnet | PeriodoReglasTest.DuplicadoProduceConflicto | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 0, nombre: "Programación") | aprobada |  |
| Ramses | dotnet | CursoAuditoriaTest.AltaConservaElRegistroPersistidoEnJson | aprobada |  |
| Jose | jose-node | login conserva contrato real y obtiene perfil del servidor, sin persistir password | aprobada |  |
| Jose | jose-node | solicitudes simultaneas con token vencido comparten una sola renovacion rotativa | aprobada |  |
| Jose | jose-node | refresh fallido limpia credenciales y notifica retorno a login | aprobada |  |
| Jose | jose-node | logout durante refresh no permite restaurar la sesion al finalizar la respuesta | aprobada |  |
| Jose | jose-node | 401 de negocio renueva una vez; segundo 401 limpia sesion sin bucles | aprobada |  |
| Jose | jose-node | caida de negocio no elimina sesion ni devuelve datos inventados | aprobada |  |
| Jose | jose-node | no envia tokens fuera del gateway | aprobada |  |
| Jose | jose-node | credenciales incorrectas y respuesta incompleta no dejan una sesion nueva | aprobada |  |
| Jose | jose-node | login sin identidad autorizada o con dependencia caida no conserva tokens parciales | aprobada |  |
| Jose | jose-node | logout con consulta inicial de identidad en vuelo impide completar el login tardio | aprobada |  |
| Jose | jose-ui | login invalido no entra y muestra mensaje requerido | aprobada |  |
| Jose | jose-ui | profesor crea y modifica nota con verbos distintos y rubros quedan bloqueados | aprobada |  |
| Jose | jose-ui | sin desglose permite carga confirmada y refresca resultado | aprobada |  |
| Jose | jose-ui | dependencia caida no muestra vacio ni permite guardar | aprobada |  |
| Jose | jose-ui | estudiante no obtiene controles de notas y logout limpia almacenamiento | aprobada |  |
| Jose | jose-ui | login utilizable en pantalla movil sin desbordamiento horizontal | aprobada |  |
| Ramses | ramses-node | exige nombre, institución y profesor | aprobada |  |
| Ramses | ramses-node | consume carrera, institución y profesor solo por gateway | aprobada |  |
| Ramses | ramses-node | valida carrera nivel y nombre | aprobada |  |
| Ramses | ramses-node | combina filtros usando gateway | aprobada |  |
| Ramses | ramses-node | normaliza el identificador y conserva el contrato del API | aprobada |  |
| Ramses | ramses-node | rechaza identificador, nombre y orden inválidos | aprobada |  |
| Ramses | ramses-node | solo utiliza rutas del gateway y serializa roles | aprobada |  |
| Ramses | ramses-node | lee tamaño de página desde parámetros con tope máximo de 15 | aprobada |  |
| Ramses | ramses-node | valida destinatarios y contenido | aprobada |  |
| Ramses | ramses-node | usa gateway para historial | aprobada |  |
| Ramses | ramses-node | valida fechas | aprobada |  |
| Ramses | ramses-node | combina filtros | aprobada |  |
| Ramses | ramses-node | valida mayoría de edad, formato local y teléfonos únicos | aprobada |  |
| Ramses | ramses-node | profesores usa únicamente el gateway con paginación | aprobada |  |
| Ramses | ramses-ui | el modal bloquea el fondo y restaura el foco al cerrar | aprobada |  |
| Ramses | ramses-ui | abre el mantenimiento de profesores en un modal bloqueante | aprobada |  |
| Ramses | ramses-ui | abre el mantenimiento de carreras con selectores reales | aprobada |  |
| Ramses | ramses-ui | abre el mantenimiento de cursos con carrera y nivel | aprobada |  |
| Ramses | ramses-ui | abre el mantenimiento de periodos con fechas | aprobada |  |
| Ramses | ramses-ui | abre composición de notificaciones en modal | aprobada |  |
| Fabian | portal-react | ADM3 incluye facturas pendientes de períodos cerrados en el indicador | aprobada |  |
| Fabian | portal-react | ADM3 presenta los grupos activos del profesor | aprobada |  |
| Fabian | portal-react | ADM3 informa el fallo de auditoría y de grupos sin ocultar soporte | aprobada |  |
| Fabian | portal-react | ADM3 muestra al profesor los resúmenes del servidor sin controles de filtrado | aprobada |  |
| Fabian | portal-react | ADM8 permite buscar y mantener instituciones desde la sesión | fallida | Error: expect(locator).toBeVisible() failed<br><br>Locator: getByText('Instituto Mejorado')<br>Expected: visible<br>Timeout: 5000ms<br>Error: element(s) not found<br><br>Call log:<br>  - Expect "toBeVisible" getByText('Instituto Mejorado') with timeout 5000ms<br>  - waiting for getByText('Instituto Mejorado')<br> |
| Fabian | portal-react | ADM8 muestra dependencia y no abre la pantalla sin permiso | fallida | Error: expect(locator).toContainText(expected) failed<br><br>Locator: getByRole('alert')<br>Expected substring: "Acceso restringido"<br>Timeout: 5000ms<br>Error: element(s) not found<br><br>Call log:<br>  - Expect "toContainText" getByRole('alert') with timeout 5000ms<br>  - waiting for getByRole('alert')<br> |
| Fabian | portal-react | ADM8 mantiene el listado detrás del modal, valida y cancela sin guardar | aprobada |  |
| Jose | portal-react | profesor navega a notas con layout, ruta activa, breadcrumbs y regreso | aprobada |  |
| Jose | portal-react | ADMINISTRADOR no ve acceso ni ejecuta consultas de notas mediante URL directa | aprobada |  |
| Jose | portal-react | ESTUDIANTE no ve acceso ni ejecuta consultas de notas mediante URL directa | aprobada |  |
| Jose | portal-react | asignar y modificar nota desde el layout conserva verbos y bloqueo del desglose | aprobada |  |
| Jose | portal-react | carga de desglose usa el cliente compartido y refresca el resultado en el layout | aprobada |  |
| Jose | portal-react | fallo de consulta no habilita escritura y permite reintentar sin perder sesión | aprobada |  |
| Jose | portal-react | profesor sin grupos ve el estado vacío dentro del layout | aprobada |  |
| Jose | portal-react | sesión rechazada al consultar notas abandona el layout y limpia credenciales | aprobada |  |
| Jose | portal-react | salir desde notas retira el módulo; volver atrás no recupera acceso | aprobada |  |
| Jose | portal-react | profesor sin ADM17 por URL directa no consulta notas y no ve el acceso | aprobada |  |
| Fabian | portal-react | ADM6 usa el servicio para crear, editar y eliminar parámetros | aprobada |  |
| Ramses | portal-react | ADM7 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | portal-react | ADM9 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | portal-react | ADM10 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | portal-react | ADM11 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | portal-react | ADM12 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | portal-react | ADM22 abre su mantenimiento y formulario en la sesión compartida | aprobada |  |
| Ramses | portal-react | ADM7 respeta TAMPAG y permite navegar sin mostrar más de quince filas | aprobada |  |
| Ramses | portal-react | sin ADM7 la ruta directa no consulta ni ofrece escritura de módulos | aprobada |  |
| Ramses | portal-react | ADM7 conserva el foco al escribir y muestra validaciones dentro del diálogo | aprobada |  |
| Ramses | portal-react | ADM7 muestra el registro guardado en su página y confirma su eliminación | aprobada |  |
| Fabian | portal-react | ADM5 guarda un cambio de permisos para un módulo y vuelve a leerlo | aprobada |  |
| Jose | portal-react | login ADMINISTRADOR llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | portal-react | login PROFESOR llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | portal-react | login ESTUDIANTE llega al layout con identidad real y restaura al recargar | aprobada |  |
| Jose | portal-react | rutas directas sin sesión vuelven al login sin mostrar identidad de ejemplo | aprobada |  |
| Jose | portal-react | credenciales inválidas no abren el layout | aprobada |  |
| Jose | portal-react | identidad rechazada después del login no conserva tokens ni abre el layout | aprobada |  |
| Jose | portal-react | renovación programada mantiene el layout y rota los tokens sin volver al login | aprobada |  |
| Jose | portal-react | refresh rechazado retira la identidad y vuelve al login con aviso de expiración | aprobada |  |
| Jose | portal-react | token vencido guardado se renueva una sola vez al restaurar bajo StrictMode | aprobada |  |
| Jose | portal-react | caída al restaurar conserva tokens, oculta el layout y permite reintentar | aprobada |  |
| Jose | portal-react | salir limpia la cuenta aunque Auth falle; volver atrás no recupera el layout | aprobada |  |
| Jose | portal-react | salir mientras se renueva impide que la respuesta tardía restaure la cuenta | aprobada |  |
| Jose | portal-react | perfil no reconocido en la identidad no accede al layout | aprobada |  |
| Jose | portal-react | el estudiante no abre una ruta administrativa de notas | aprobada |  |
| Jose | portal-react | login móvil conserva formulario visible sin desbordamiento horizontal | aprobada |  |
| Jose | portal-react | retiro de ADM17 se refleja en el menú al recargar | aprobada |  |
| Jose | portal-react | caída de permisos durante restauración oculta el layout y permite recuperar | aprobada |  |
| Fabian | portal-react | ADM4 crea y edita usuarios usando el ID real del rol | aprobada |  |

## resultados

Procedencia: `resultados.json`, retirado después de esta transcripción.

Fecha: 2026-11-10

Alcance: Segundo avance: 20 HU, 120 puntos; servicios heredados solo como apoyo.

Limites: ["SQL Server y SMTP reales no ejecutados.", "API de navegador interceptada; HTTP de gateway con destinos simulados.", "Una ejecución final por caso; intentos interrumpidos y repeticiones excluidos.", "Atribución por HU vigente; no por nombre de carpeta personaN.", "Las barras de pruebas son éxito entre casos ejecutados; no cobertura de requisitos ni aporte."]

Resumen registrado por integrante (conservo las exclusiones del informe original):

| Integrante | Resultado registrado |
| --- | --- |
| Jose | {"aprobada": 131, "fallida": 29} |
| Ramses | {"aprobada": 40} |
| Alejandro | {"aprobada": 218, "fallida": 5} |
| Fabian | {"aprobada": 93, "fallida": 11} |
| Compartida | {"aprobada": 3} |

Transcribí **530 entradas**. Las entradas excluidas o de preparación no equivalen a casos incluidos en las barras.

| Integrante | Grupo | Caso | Estado | Observación registrada |
| --- | --- | --- | --- | --- |
| Compartida | integracion | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.permiteArchivoLocalVacio | aprobada |  |
| Compartida | integracion | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.incluyeContratosEnClasspath | aprobada |  |
| Compartida | integracion | cr.ac.cuc.nuevoavatar.integracion.ConfiguracionLocalTest.encuentraArchivoEnPadresYResuelveValoresEntreComillas | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.IntegracionCompartidaTest.cargaBibliotecaYContratoConSpringBootDelServicioSinBaseReal | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.NuevoAvatarPersona2ApplicationTests.contextLoads | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.aceptaBearerValidadoPorSeguridad | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.rechazaSolicitudSinBearer | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.AutorizacionInterceptorTest.obtieneElSujetoSoloDespuesDeValidarElBearer | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.registraAmbosEstadosYNoConfirmaCambiosFallidos | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaLasTresCredencialesDelCambioDeContrasena | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaSecretosConComillasEscapadasYCamposAnidados | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.conservaJsonSinSecretos | aprobada |  |
| Fabian | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilterTest.ocultaContrasenaYConservaDatosDelRegistro | aprobada |  |
| Alejandro | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.actualizaSoloElNombreDeLaCuentaDerivadaDelSujeto | aprobada |  |
| Alejandro | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.cambiaContrasenaDelSujetoYGuardaSoloHashBcrypt | aprobada |  |
| Alejandro | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.validaLongitudMayusculaMinusculaNumeroYCaracterEspecialEnServidor | aprobada |  |
| Alejandro | NuevoAvatar.Usuario | cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioPerfilPropioTest.rechazaContrasenaActualIncorrectaSinPersistir | aprobada |  |
| Ramses | NuevoAvatar.Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.bloqueaEliminarCuandoExistenCursos | aprobada |  |
| Ramses | NuevoAvatar.Carrera | cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraServiceTest.validaInstitucionYProfesorEnSusServiciosPropietarios | aprobada |  |
| Ramses | NuevoAvatar.Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.permiteCuandoTodosLosPeriodosEstanCerrados | aprobada |  |
| Ramses | NuevoAvatar.Profesor | cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorServiceTest.bloqueaCuandoHoyEstaDentroDelPeriodoInclusivo | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaDistritoDeOtroCanton | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaCantonDeOtraProvincia | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.comunicaFallaDelServicioSinTratarlaComoDireccionInvalida | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.rechazaProvinciaInexistente | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.DireccionClientTest.consumeRutasYCamelCaseDelServicioDeAlejandro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposRequeridosEnCrearYModificar(String)[10] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.metodoNoAdmitidoNoSeReportaComoErrorInterno | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identidadEnQueryNoCambiaElExpedientePropio | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaCualquierCampoProtegidoAunqueElRestoSeaValido | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identificacionDuplicadaAlModificarNoPierdeDatos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaRolIncorrectoVinculoAusenteYAmbiguo | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.camposEditablesObligatoriosSeValidanAntesDePersistir | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.identificacionReferenciadaNoSeCambiaEntreServicios | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.errorTecnicoRegistraBitacoraYRevierteEscritura | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.noEliminaEstudianteReferenciadoNiSusTelefonos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.noPermiteMismoNumeroAunqueCambieElTipo | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.estudiantesDiferentesSoloVenYActualizanSuPropioRegistro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.direccionInvalidaNoEscribeDatos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.conservaCerosInicialesYNoExigeCorreoOTelefonosUnicos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.falloGen1RevierteModificacionYEliminacion | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.dependenciasDeAutenticacionNoDisponiblesDevuelven503 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.rechazaDireccionIncoherenteYTokenInvalido | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.sqlNoInterpretaLaIdentificacionComoUnaInstruccion | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.dominioCambiaMedianteParametro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.falloGen1RevierteCreacionYTelefonos | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.cicloCompletoDeCincoOperacionesYBitacoras | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[10] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[11] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[12] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[13] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[14] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.protegeTodasLasOperaciones(String, String, String)[15] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.recursosInexistentesDevuelven404 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.nombreCorreoDominioYFechaInvalidosSeRechazanEnCrearYModificar | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteApiTest.consultaYActualizaSoloSuExpedienteConPersistenciaYBitacoraMinima | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[10] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[11] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[12] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[13] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[14] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[15] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[16] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[17] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[18] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[19] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[20] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[21] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[22] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[23] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[24] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[25] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[26] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[27] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[28] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDatosAusentesAntesDeConsultarParametro(String, DatosExpediente)[29] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.noAgregaEdadMinimaNiFormatoDeTelefono | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.utilizaDominioConfiguradoYSinDiferenciarMayusculas | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaExpedienteNulo | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.propagaFalloDelParametro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaNombreConOtrosCaracteres(String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaDominioDistinto(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.rechazaFormatoCorreo(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteValidadorTest.aceptaLetrasAcentuadas(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.conservaUsuarioYTokenValidados | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.responde503CuandoSeguridadNoResponde | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noConfiaEnSubSinValidacionExterna | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.rechazaHeaderAusenteOInvalido(String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptorTest.noRegistraUsuarioInventadoSiPayloadInvalido(String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.consultaRespetaTextoDelDocumento | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.conservaEstadoAnteriorAunqueElObjetoCambie | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.registraTipoDeErrorSinCopiarMensajeConCredenciales | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraServiceTest.creacionYEliminacionIncluyenRegistro | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.validaContratoUsr5 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.comunicaFalloDeConexion | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.enviaBitacoraSinInventarFechaNiIdentificador | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.leeParametroConContratoUsr3 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.distingueFallaDeServicioDeTokenInvalido | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaTokenCon401 | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.informaParametroInexistente | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noAutorizaSinTrue(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.rechazaParametroSinValor(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClientTest.noOcultaFalloAlGuardarBitacora | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Expediente | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.estudiantePuedeCrearConsultarModificarYCancelarSoloConIdentidadDeSesion | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.errorDespuesDeEscribirRevierteYSeRegistraComoMat1 | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.falloGen1RevierteCreacionYCursos | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.noImponeUnicidadNoSolicitadaEntreEstudianteYPeriodo | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.compruebaTodosLosNivelesDesdeLaBase | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.cincoOperacionesConDatosPersistidosYBitacoras | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[10] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[11] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[12] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[13] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[14] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.protegeLasCincoOperaciones(String, String, String)[15] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.modificarPermiteCambiarEstudianteYCarrera | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.reevaluaLaFechaParaCadaEscrituraYPermiteConsultarOEliminar | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesOpcionalesEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.referenciasInexistentesNoGuardanDatos | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.endpointsAdministrativosNoPermitenQueUnEstudianteElijaIdentidadAjena | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.falloGen1RevierteModificacionYEliminacion | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.listaVaciaOCursoNuloNoSeAceptan | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.servicioDePrematriculaNoExponeRutasDeExpediente | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.solicitudPropiaNoAceptaIdentidadElegidaYNoExponeSolicitudesDeOtroEstudiante | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.observacionesPuedenOmitirseAlCrear | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.recursoAusenteIdInvalidoYMetodoNoAdmitidoTienenRespuestasRest | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.rechazaPeriodoQueIniciaHoyOPasadoAunqueSolicitanteEnvieOtraFecha(int)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.rechazaPeriodoQueIniciaHoyOPasadoAunqueSolicitanteEnvieOtraFecha(int)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.identificacionSinContenidoNoSeAcepta(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.datosRequeridosEnCrearYModificar(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaApiTest.opcionesPropiasSoloIncluyenCursosDePrimerNivelYPeriodosFuturos | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaPeriodoPasadoOQueIniciaHoy(int)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[5] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[6] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[7] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[8] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosAusentesDeCarreraCursosOPeriodo(String, DatosPrematricula)[9] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.necesitaFechaDelServidorParaEvaluarPeriodo | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.rechazaDatosNulos | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.todosLosCursosDebenSerDePrimerNivel(int)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.evaluaFechaDeCadaOperacionSinConservarUnHoyFijo | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.comparaFechasAlCambiarDeAnio | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.observacionesSonOpcionales(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaUnSoloCursoDePrimerNivel | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.identificacionEsRequerida(String)[4] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[2] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.prematricula.PrematriculaValidadorTest.aceptaPeriodoFuturoSinAgregarPlazoMaximo(int)[3] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[1] | aprobada |  |
| Alejandro | NuevoAvatar.Prematricula | cr.ac.cuc.nuevoavatar.persona4.SeguridadRutasTest.rechazaRutasSinToken(String)[2] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.rollbackDeRubrosNoEnviaBitacoraDeExito | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.reemplazoDeRubrosAuditaAntesYDespuesTrasCommit | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.altaDeRubrosAuditaJsonNuevoTrasCommit | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.rollbackNoEnviaBitacoraDeExito | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.AuditoriaNotasTest.enviaAntesYDespuesSoloTrasConfirmarTransaccion | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.fueraDeTransaccionNoIntentaEscribir | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[1] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[2] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[3] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[4] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[5] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int)[6] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.BloqueoGrupoNotasTest.conexionEnAutocommitNoPuedeProtegerLaOperacion | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.primeraNotaConfirmadaImpideReemplazarElDesgloseConcurrente | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.dosPostConcurrentesNoSobrescribenNiDuplicanLaPrimeraNota | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ConcurrenciaNotasTest.reemplazoConfirmadoRechazaLaNotaDelRubroQueYaNoExiste | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[1] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[2] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.notaFueraDeRangoNoEscribeNiBloqueaRubros(String)[3] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.putNoCreaUnaNotaInexistente | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.falloAlGuardarNoPublicaExitoEnBitacora | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.modificacionConservaSnapshotAnterior | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.postNoSobrescribeNotaExistente | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.creaEnLimitesYAuditaResultadoPersistido(String)[1] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.EscrituraNotasTest.creaEnLimitesYAuditaResultadoPersistido(String)[2] | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.sinTokenNoPuedeConsultarGrupo | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.grupoInvalidoDevuelve400 | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.profesorPropioUsaOperacionCorrecta | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.estudianteNoPuedeAsignarNotaPorHttp | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.profesorAjenoNoPuedeModificarNotaPorHttp | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasHttpTest.notaInvalidaNoLlegaALaEscritura | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.bloqueaCambioDeRubrosCuandoYaExistenNotas | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.rechazaDesgloseQueNoSumaCien | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.NotasServiceTest.reemplazoAuditaSnapshotPrevioYResultadoPersistido | aprobada |  |
| Fabian | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.resumenConsultaSoloProfesorPropioYPeriodoActivo | aprobada |  |
| Fabian | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.gruposDePeriodosCerradosNoAparecenEnResumenActual | aprobada |  |
| Fabian | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.ResumenProfesorTest.falloDeGrupoNoSePresentaComoResumenSinDatos | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.dependenciaCaidaNoSeConvierteEnGrupoVacio | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.interceptorUsaIdentidadValidadaYNoElPayloadDelNavegador | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.profesorPuedeAccederSoloAGrupoPropio | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.tokenExpiradoNoResuelveIdentidad | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.matriculaConPeriodoDiferenteEsRechazada | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.estudianteNoPuedeEscribirNiConsultarNotasDelProfesor | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.profesorNoPuedeConsultarOtroGrupo | aprobada |  |
| Jose | NuevoAvatar.Notas | cr.ac.cuc.nuevoavatar.persona5.notas.SeguridadNotasTest.headersDelClienteNoOtorganIdentidad | aprobada |  |
| Ramses | NuevoAvatar.Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.sanitizacionEliminaScriptsYAtributosPeligrosos | aprobada |  |
| Ramses | NuevoAvatar.Notificacion | cr.ac.cuc.nuevoavatar.persona5.notificacion.NotificacionServiceTest.rechazaDestinatariosInvalidos | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer valido", status: 204) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "", status: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: null, valido: True, status: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/obtenernotas", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/refresh", metodo: "POST", status: 201) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/refresh", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 201) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/logout", metodo: "POST", status: 204) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: False, status: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/%69nterno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_RechazaDatosVacios | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/login", metodo: "POST", status: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_NoVinculaOtroExpedienteYNoAsumeRolPorId | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/mat/interno/datos/NotaRubro", verbo: "GET", status: 404, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve201ParaCredencialesValidas | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "PROFESOR", email: "p@cuc.ac.cr", profesor: 9, estudiante: null) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "PROFESOR", permitido: "PROFESOR", valido: True, status: 200) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.AuditoriaNoIncluyeCredenciales(ruta: "/sesion", metodo: "GET", status: 200) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_DependenciaCaidaNoPermiteLlegarAlDestino | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_LeeBearerYDevuelveTrue | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.LogoutExigeTokenYConservaUsuarioParaAuditoria(header: "Bearer invalido", status: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Sesion_NoConfiaEnHeadersDeIdentidad | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Login_Devuelve401ParaCredencialesInvalidas | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Identidad_VinculaPorEmailTipoEIdentificacion(rol: "ESTUDIANTE", email: "e@cuc.cr", profesor: null, estudiante: "123") | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.AuthControllerTests.Validate_RechazaTokenInvalido | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ValidaTokenYRolAntesDelDestino(rol: "ESTUDIANTE", permitido: "PROFESOR", valido: True, status: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "POST", status: 200, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.TokenYAuditoriaTests.TokenExpiradoYFirmaAjenaNoSonValidos | aprobada |  |
| Jose | dotnet | NuevoAvatarSeguridad.Tests.SesionYGatewayTests.Gateway_ExcepcionesSonExplicitas(ruta: "/gateway/auth/login", verbo: "GET", status: 401, llamadas: 0) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta /obtenernotasgrupo", modulo: "ADM17") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notificacion") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.NotificacionesAdministrativasRequierenPermisoDelModulo | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "profesor", path: "/gateway/admin/usuario") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 503, invalido: False) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Referencia a GET /usuario") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "invalido", esperado: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: null, esperado: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "GET", ruta: "/gateway/mat/prematricula/propio") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial académico", modulo: "ADM18") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: null, estado: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.DatosVaciosDevuelvenListasVacias | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "GET /usuario/ana", modulo: "ADM4") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/%69nterno/datos", status: 404) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "GET") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/admin/usuario", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta del grupo 12.", modulo: "ADM13") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardNoRegistraVisitasSinPerfilPermitido(token: "estudiante", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DestinoCaidoDevuelve502SinConfundirConAuth | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuPropioNoAceptaRolDeQueryYNuncaRequierePermisoAdministrativo | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/admin/expediente/123", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "admin") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "DELETE", status: 204) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "estudiante", ruta: "/gateway/mat/obtenernotas?grupoId=1", status: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "estudiante") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "profesor") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: null, ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.CorreosSeFiltranPorDestinatarioCompletoAntesDelLimite | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RoutingDeTodasLasFamiliasTieneDestinoYPolitica | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/1?vista=dashboard", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM7", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: False) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RetirarPermisoBloqueaLaSiguientePeticionInclusoParaAdministrador | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/DOMESTUD") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/mat/prematricula/propio", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "POST", ruta: "/gateway/admin/rol") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/fin/notificar/historial?vista=dashboard") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "bitacora") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(ruta: "/gateway/admin/bitacora?vista=dashboard") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/menu/ADMIN") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.MatrizAdm5ConservaCuerpoYConsultaAsignaciones(vaciar: True) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "PUT", status: 200) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "invalido", estado: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "refresh") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/mat/prematricula/propio", status: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DependenciaAuthCaidaDevuelve503 | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ModuloInactivoNoConcedeDependencias | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "DELETE", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(servicio: "notas") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo 1 de otro módulo") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "El usuario consulta periodo desconocido") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "invalido", ruta: "/gateway/admin/bitacora?vista=dashboard", esperado: 401) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.SoloOperacionesPublicasAtraviesanSinToken(operacion: "login") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.MenuNormalNoRegistraVisitaAlDashboard | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta del grupo 12. JSON: {\"ruta\":\"/usuario\"···) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodos", modulo: "ADM12") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Registro sin formato. JSON: {\"ruta\":\"/usuario\""···) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "estudiante", ruta: "/gateway/fin/notificar/historial?vista=dashboard", esperado: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 200, invalido: True) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "POST", ruta: "/gateway/mat/prematricula/propio") | fallida | Assert.Equal() Failure: Values differ<br>Expected: OK<br>Actual:   Created |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloDeBitacoraNoConfirmaElRegistroDelDashboard | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "profesor", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "estudiante", estado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta periodo 15", modulo: "ADM12") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "POST", status: 201) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta listado de estudiantes", modulo: "ADM19") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ServicioNuevoSinContratoDeModuloNoPuedeQuedarExentoAccidentalmente | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "Consulta de grupos y periodos") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.AdministradorRecibeDiezEventosDeModulosPermitidosOrdenadosDespuesDeFiltrar | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: null, ruta: "/gateway/expediente/propio", status: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM9", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "DELETE", ruta: "/gateway/admin/modulo/ADM8") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.MenuPropioTests.SinIdentidadNoConsultaNingunRol | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora?vista=dashboard&vista=glob"···, esperado: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(metodo: "PUT") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/fin/notificar/historial", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM4", metodo: "GET", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ModuloInactivoNiegaApiYSeRetiraDelMenu | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.CambioContrasenaPerfilPropioReenviaPutYBearer(token: "admin") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/expediente/propio", status: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "PUT", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "admin", ruta: "/gateway/admin/usuario/interno/datos", status: 404) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles/ADMIN") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(token: "profesor") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(status: 403, invalido: False) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.AsignacionDeModuloNoAmpliaElPerfilPermitido(token: "estudiante", path: "/gateway/mat/obtenernotas?grupoId=1") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora", esperado: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "DELETE", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ProfesorVeAccionesPropiasYGruposAutorizadosSinDatosAdministrativos | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.OmiteTextosAmbiguosYReferenciasDentroDeDatos(descripcion: "{\"descripcion\":\"El usuario consulta /usuario\"}") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM8", metodo: "GET", ruta: "/gateway/admin/rol") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "Consulta de todos los grupos.", modulo: "ADM13") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.PermiteDependenciasExactasDelMantenimiento(modulo: "ADM5", metodo: "GET", ruta: "/gateway/admin/modulo/ADM8/roles") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.NoConcedeOtrasOperaciones(modulo: "ADM5", metodo: "POST", ruta: "/gateway/admin/modulo") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM5", metodo: "PUT", ruta: "/gateway/admin/rol/ADMIN") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ResumenDashboardTests.ResumenNoAbreLaConsultaGlobalNiElDetalle(token: "profesorA", ruta: "/gateway/admin/bitacora/1?vista=dashboard", esperado: 403) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.ConservaPermisosPropiosDeCadaModulo(modulo: "ADM6", metodo: "PUT", ruta: "/gateway/admin/parametro/TAMPAG") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.PrematriculaPropiaReenviaOperacionAutenticadaDeEstudiante(metodo: "PUT", ruta: "/gateway/mat/prematricula/propio/17") | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.PermisosAuxiliaresTests.DependenciasMantienenAutenticacionYRolAdministrador(token: "profesor", estado: 403) | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.RechazaAntesDeLlegarAlNegocio(token: "invalido", ruta: "/gateway/admin/usuario", status: 401) | aprobada |  |
| Fabian | dotnet | NuevoAvatar.Gateway.Tests.ClasificacionBitacoraTests.ClasificaConsultasEmitidasPorLosServicios(descripcion: "El usuario consulta historial academico", modulo: "ADM18") | aprobada |  |
| Jose | dotnet | NuevoAvatar.Gateway.Tests.ProxyHttpTests.ConservaVerboCuerpoQueryRespuestaYLocation(metodo: "GET", status: 200) | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Gateway.Tests.MenuPropioTests.UsaElIdRealDelRolDeLaCuentaYNuncaElRolDeLaQuery | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 1, nivel: 13) | aprobada |  |
| Ramses | dotnet | PeriodoServiceTest.ContratoActualPermiteFechasIguales | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 1) | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 13, nombre: "Programación") | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.PeriodoExigeDatos | aprobada |  |
| Ramses | dotnet | PeriodoReglasTest.DuplicadoProduceConflicto | aprobada |  |
| Ramses | dotnet | PeriodoServiceTest.RechazaFechaFinalAnteriorAlInicioAntesDeGuardar | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Academico.Tests.CursoFiltrosTest.RechazaFiltrosFueraDeRango(carrera: 0, nivel: 1) | aprobada |  |
| Ramses | dotnet | PeriodoReglasTest.EstadoSeDerivaDeFechas | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoAceptaLimitesDelNivel(nivel: 12) | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 0, nombre: "Programación") | aprobada |  |
| Ramses | dotnet | ValidacionesRequestTest.CursoRechazaNivelONombreInvalidos(nivel: 1, nombre: "Programación 5") | aprobada |  |
| Ramses | dotnet | NuevoAvatar.Academico.Tests.CursoFiltrosTest.CombinaCarreraYNivelEnRepositorio | aprobada |  |
| Jose | jose-node | login conserva contrato real y obtiene perfil del servidor, sin persistir password | aprobada |  |
| Jose | jose-node | solicitudes simultaneas con token vencido comparten una sola renovacion rotativa | aprobada |  |
| Jose | jose-node | refresh fallido limpia credenciales y notifica retorno a login | aprobada |  |
| Jose | jose-node | logout durante refresh no permite restaurar la sesion al finalizar la respuesta | aprobada |  |
| Jose | jose-node | 401 de negocio renueva una vez; segundo 401 limpia sesion sin bucles | aprobada |  |
| Jose | jose-node | caida de negocio no elimina sesion ni devuelve datos inventados | aprobada |  |
| Jose | jose-node | no envia tokens fuera del gateway | aprobada |  |
| Jose | jose-node | credenciales incorrectas y respuesta incompleta no dejan una sesion nueva | aprobada |  |
| Jose | jose-node | login sin identidad autorizada o con dependencia caida no conserva tokens parciales | aprobada |  |
| Jose | jose-node | logout con consulta inicial de identidad en vuelo impide completar el login tardio | aprobada |  |
| Jose | jose-ui | login invalido no entra y muestra mensaje requerido | aprobada |  |
| Jose | jose-ui | profesor crea y modifica nota con verbos distintos y rubros quedan bloqueados | aprobada |  |
| Jose | jose-ui | sin desglose permite carga confirmada y refresca resultado | aprobada |  |
| Jose | jose-ui | dependencia caida no muestra vacio ni permite guardar | aprobada |  |
| Jose | jose-ui | estudiante no obtiene controles de notas y logout limpia almacenamiento | aprobada |  |
| Jose | jose-ui | login utilizable en pantalla movil sin desbordamiento horizontal | aprobada |  |
| Ramses | ramses-node | exige nombre, institución y profesor | aprobada |  |
| Ramses | ramses-node | consume carrera, institución y profesor solo por gateway | aprobada |  |
| Ramses | ramses-node | valida carrera nivel y nombre | aprobada |  |
| Ramses | ramses-node | combina filtros usando gateway | aprobada |  |
| Ramses | ramses-node | normaliza el identificador y conserva el contrato del API | aprobada |  |
| Ramses | ramses-node | rechaza identificador, nombre y orden inválidos | aprobada |  |
| Ramses | ramses-node | solo utiliza rutas del gateway y serializa roles | aprobada |  |
| Ramses | ramses-node | valida destinatarios y contenido | aprobada |  |
| Ramses | ramses-node | usa gateway para historial | aprobada |  |
| Ramses | ramses-node | valida fechas | aprobada |  |
| Ramses | ramses-node | combina filtros | aprobada |  |
| Ramses | ramses-node | valida mayoría de edad y teléfonos únicos | aprobada |  |
| Ramses | ramses-node | profesores usa únicamente el gateway con paginación | aprobada |  |
| Ramses | ramses-ui | el modal bloquea el fondo y restaura el foco al cerrar | aprobada |  |
| Ramses | ramses-ui | abre el mantenimiento de profesores en un modal bloqueante | aprobada |  |
| Ramses | ramses-ui | abre el mantenimiento de carreras con selectores reales | aprobada |  |
| Ramses | ramses-ui | abre el mantenimiento de cursos con carrera y nivel | aprobada |  |
| Ramses | ramses-ui | abre el mantenimiento de periodos con fechas | aprobada |  |
| Ramses | ramses-ui | abre composición de notificaciones en modal | aprobada |  |
| Fabian | portal-react | ADM3 incluye facturas pendientes de períodos cerrados en el indicador | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Fabian | portal-react | ADM3 presenta los grupos activos del profesor | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Fabian | portal-react | ADM3 informa el fallo de auditoría y de grupos sin ocultar soporte | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Fabian | portal-react | ADM3 muestra al profesor los resúmenes del servidor sin controles de filtrado | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Fabian | portal-react | ADM8 permite buscar y mantener instituciones desde la sesión | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Fabian | portal-react | ADM8 muestra dependencia y no abre la pantalla sin permiso | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Fabian | portal-react | ADM8 mantiene el listado detrás del modal, valida y cancela sin guardar | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | profesor navega a notas con layout, ruta activa, breadcrumbs y regreso | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | ADMINISTRADOR no ve acceso ni ejecuta consultas de notas mediante URL directa | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | ESTUDIANTE no ve acceso ni ejecuta consultas de notas mediante URL directa | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | asignar y modificar nota desde el layout conserva verbos y bloqueo del desglose | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | carga de desglose usa el cliente compartido y refresca el resultado en el layout | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | fallo de consulta no habilita escritura y permite reintentar sin perder sesión | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | profesor sin grupos ve el estado vacío dentro del layout | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | sesión rechazada al consultar notas abandona el layout y limpia credenciales | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | salir desde notas retira el módulo; volver atrás no recupera acceso | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | profesor sin ADM17 por URL directa no consulta notas y no ve el acceso | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Fabian | portal-react | ADM6 usa el servicio para crear, editar y eliminar parámetros | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Fabian | portal-react | ADM5 guarda un cambio de permisos para un módulo y vuelve a leerlo | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | login ADMINISTRADOR llega al layout con identidad real y restaura al recargar | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | login PROFESOR llega al layout con identidad real y restaura al recargar | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | login ESTUDIANTE llega al layout con identidad real y restaura al recargar | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | rutas directas sin sesión vuelven al login sin mostrar identidad de ejemplo | aprobada |  |
| Jose | portal-react | credenciales inválidas no abren el layout | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | identidad rechazada después del login no conserva tokens ni abre el layout | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | renovación programada mantiene el layout y rota los tokens sin volver al login | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | refresh rechazado retira la identidad y vuelve al login con aviso de expiración | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | token vencido guardado se renueva una sola vez al restaurar bajo StrictMode | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | caída al restaurar conserva tokens, oculta el layout y permite reintentar | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | salir limpia la cuenta aunque Auth falle; volver atrás no recupera el layout | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | salir mientras se renueva impide que la respuesta tardía restaure la cuenta | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | perfil no reconocido en la identidad no accede al layout | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | ruta estudiantil pendiente no se presenta como un trámite disponible | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | login móvil conserva formulario visible sin desbordamiento horizontal | aprobada |  |
| Jose | portal-react | retiro de ADM17 se refleja en el menú al recargar | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | portal-react | caída de permisos durante restauración oculta el layout y permite recuperar | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Fabian | portal-react | ADM4 crea y edita usuarios usando el ID real del rol | fallida | Contrato de sesión anterior: revisar fixture y regresión de integración. |
| Jose | diagnostico-actual | D-login-ADMINISTRADOR: Ingreso con respuestas del contrato actual: ADMINISTRADOR | aprobada | {"ruta": "/inicio", "llamadas": [{"path": "/api/auth/login", "method": "POST"}, {"path": "/api/users/usuario/ana%40cuc.ac.cr", "method": "GET"}, {"path": "/api/roles/rol/ROL_PRUEBA", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/admin/usuario", "method": "GET"}, {"path": "/gateway/admin/carrera", "method": "GET"}, {"path": "/gateway/admin/curso", "method": "GET"}, {"path": "/gateway/admin/periodo", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/admin/usuario", "method": "GET"}, {"path": "/gateway/admin/carrera", "method": "GET"}, {"path": "/gateway/admin/curso", "method": "GET"}, {"path": "/gateway/admin/periodo", "method": "GET"}]} |
| Jose | diagnostico-actual | D-login-PROFESOR: Ingreso con respuestas del contrato actual: PROFESOR | aprobada | {"ruta": "/inicio", "llamadas": [{"path": "/api/auth/login", "method": "POST"}, {"path": "/api/users/usuario/ana%40cuc.ac.cr", "method": "GET"}, {"path": "/api/roles/rol/ROL_PRUEBA", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/mat/profesor/grupos", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/mat/profesor/grupos", "method": "GET"}]} |
| Jose | diagnostico-actual | D-login-ESTUDIANTE: Ingreso con respuestas del contrato actual: ESTUDIANTE | aprobada | {"ruta": "/estudiante", "llamadas": [{"path": "/api/auth/login", "method": "POST"}, {"path": "/api/users/usuario/ana%40cuc.cr", "method": "GET"}, {"path": "/api/roles/rol/ROL_PRUEBA", "method": "GET"}, {"path": "/gateway/expediente/propio", "method": "GET"}, {"path": "/gateway/mat/prematricula/propio", "method": "GET"}, {"path": "/gateway/expediente/propio", "method": "GET"}, {"path": "/gateway/mat/prematricula/propio", "method": "GET"}]} |
| Jose | diagnostico-actual | D-recarga: Conservar la sesión al recargar una cuenta ingresada | fallida | La recarga envía a /login: AuthContext mantiene la sesión solamente en memoria. |
| Jose | diagnostico-actual | D-formato-login: Rechazar un correo sin formato institucional antes de llamar a login | fallida | El formulario envía texto-sin-correo a /api/auth/login. |
| Jose | diagnostico-actual | D-notas: Abrir la pantalla funcional de Notas desde el portal del profesor | fallida | La ruta /notas muestra un mensaje provisional en lugar del componente Notas. |
| Alejandro | diagnostico-actual | D-inicio-estudiante: Inicio muestra expediente y cantidad de solicitudes simuladas | aprobada | Estado disponible y dos solicitudes |
| Alejandro | diagnostico-actual | D-confirmar-expediente: Guardar expediente exige confirmación antes de enviar PUT | fallida | Guardar cambios envió PUT de inmediato, sin confirmación. |
| Alejandro | diagnostico-actual | D-confirmar-cancelacion: Cancelar una prematrícula exige confirmación antes de DELETE | fallida | Cancelar solicitud envió DELETE de inmediato, sin confirmación. |
| Alejandro | diagnostico-actual | D-paginacion-prematricula: Mostrar como máximo 15 solicitudes por página | fallida | Se muestran 16 solicitudes simultáneamente; no hay paginación. |
| Alejandro | diagnostico-actual | D-cambio-clave-salida: Cambio de contraseña retira la sesión y solicita login | fallida | Tras cambiar contraseña permanece abierta /estudiante/perfil con la sesión activa. |
| Fabian | diagnostico-actual | D-admin-sin-permiso-ui: Una URL administrativa sin permiso de módulo bloquea sus controles | fallida | El profesor abre /usuarios y ve Nuevo usuario; AdminRoleGate solo distingue el perfil general y no el permiso del módulo. El gateway real conserva su rechazo 403. |
| Fabian | diagnostico-actual | D-mantenimiento-usuarios: Abrir formulario administrativo con sesión actual: /usuarios | aprobada | Formulario disponible con catálogos simulados; no se guardaron datos. |
| Fabian | diagnostico-actual | D-mantenimiento-roles: Abrir formulario administrativo con sesión actual: /roles | aprobada | Formulario disponible con catálogos simulados; no se guardaron datos. |
| Fabian | diagnostico-actual | D-mantenimiento-parametros: Abrir formulario administrativo con sesión actual: /parametros | aprobada | Formulario disponible con catálogos simulados; no se guardaron datos. |
| Fabian | diagnostico-actual | D-mantenimiento-instituciones: Abrir formulario administrativo con sesión actual: /instituciones | aprobada | Formulario disponible con catálogos simulados; no se guardaron datos. |
| Alejandro | diagnostico-actual | D-layout-compartido: Las dos experiencias reutilizan el layout y no desbordan en 390 px | fallida | Desbordamiento horizontal: {"administracion":{"ancho":390,"contenido":429},"estudiante":{"ancho":390,"contenido":421}} |

## diagnostico

Procedencia: `diagnostico.json`, retirado después de esta transcripción.

Limite: API interceptada, sin SQL ni SMTP

Transcribí **17 entradas**. Las entradas excluidas o de preparación no equivalen a casos incluidos en las barras.

| Integrante | Grupo | Caso | Estado | Observación registrada |
| --- | --- | --- | --- | --- |
| Jose | Recorrido | Ingreso con respuestas del contrato actual: ADMINISTRADOR | aprobada | {"ruta": "/inicio", "llamadas": [{"path": "/api/auth/login", "method": "POST"}, {"path": "/api/users/usuario/ana%40cuc.ac.cr", "method": "GET"}, {"path": "/api/roles/rol/ROL_PRUEBA", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/admin/usuario", "method": "GET"}, {"path": "/gateway/admin/carrera", "method": "GET"}, {"path": "/gateway/admin/curso", "method": "GET"}, {"path": "/gateway/admin/periodo", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/admin/usuario", "method": "GET"}, {"path": "/gateway/admin/carrera", "method": "GET"}, {"path": "/gateway/admin/curso", "method": "GET"}, {"path": "/gateway/admin/periodo", "method": "GET"}]} |
| Jose | Recorrido | Ingreso con respuestas del contrato actual: PROFESOR | aprobada | {"ruta": "/inicio", "llamadas": [{"path": "/api/auth/login", "method": "POST"}, {"path": "/api/users/usuario/ana%40cuc.ac.cr", "method": "GET"}, {"path": "/api/roles/rol/ROL_PRUEBA", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/menu", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/mat/profesor/grupos", "method": "GET"}, {"path": "/gateway/admin/bitacora", "method": "GET"}, {"path": "/gateway/fin/notificar/historial", "method": "GET"}, {"path": "/gateway/mat/profesor/grupos", "method": "GET"}]} |
| Jose | Recorrido | Ingreso con respuestas del contrato actual: ESTUDIANTE | aprobada | {"ruta": "/estudiante", "llamadas": [{"path": "/api/auth/login", "method": "POST"}, {"path": "/api/users/usuario/ana%40cuc.cr", "method": "GET"}, {"path": "/api/roles/rol/ROL_PRUEBA", "method": "GET"}, {"path": "/gateway/expediente/propio", "method": "GET"}, {"path": "/gateway/mat/prematricula/propio", "method": "GET"}, {"path": "/gateway/expediente/propio", "method": "GET"}, {"path": "/gateway/mat/prematricula/propio", "method": "GET"}]} |
| Jose | Recorrido | Conservar la sesión al recargar una cuenta ingresada | fallida | La recarga envía a /login: AuthContext mantiene la sesión solamente en memoria. |
| Jose | Recorrido | Rechazar un correo sin formato institucional antes de llamar a login | fallida | El formulario envía texto-sin-correo a /api/auth/login. |
| Jose | Recorrido | Abrir la pantalla funcional de Notas desde el portal del profesor | fallida | La ruta /notas muestra un mensaje provisional en lugar del componente Notas. |
| Alejandro | Recorrido | Inicio muestra expediente y cantidad de solicitudes simuladas | aprobada | Estado disponible y dos solicitudes |
| Alejandro | Recorrido | Guardar expediente exige confirmación antes de enviar PUT | fallida | Guardar cambios envió PUT de inmediato, sin confirmación. |
| Alejandro | Recorrido | Cancelar una prematrícula exige confirmación antes de DELETE | fallida | Cancelar solicitud envió DELETE de inmediato, sin confirmación. |
| Alejandro | Recorrido | Mostrar como máximo 15 solicitudes por página | fallida | Se muestran 16 solicitudes simultáneamente; no hay paginación. |
| Alejandro | Recorrido | Cambio de contraseña retira la sesión y solicita login | fallida | Tras cambiar contraseña permanece abierta /estudiante/perfil con la sesión activa. |
| Fabian | Recorrido | Una URL administrativa sin permiso de módulo bloquea sus controles | fallida | El profesor abre /usuarios y ve Nuevo usuario; AdminRoleGate solo distingue el perfil general y no el permiso del módulo. El gateway real conserva su rechazo 403. |
| Fabian | Recorrido | Abrir formulario administrativo con sesión actual: /usuarios | aprobada | Formulario disponible con catálogos simulados; no se guardaron datos. |
| Fabian | Recorrido | Abrir formulario administrativo con sesión actual: /roles | aprobada | Formulario disponible con catálogos simulados; no se guardaron datos. |
| Fabian | Recorrido | Abrir formulario administrativo con sesión actual: /parametros | aprobada | Formulario disponible con catálogos simulados; no se guardaron datos. |
| Fabian | Recorrido | Abrir formulario administrativo con sesión actual: /instituciones | aprobada | Formulario disponible con catálogos simulados; no se guardaron datos. |
| Alejandro | Recorrido | Las dos experiencias reutilizan el layout y no desbordan en 390 px | fallida | Desbordamiento horizontal: {"administracion":{"ancho":390,"contenido":429},"estudiante":{"ancho":390,"contenido":421}} |

## Observaciones

Los nombres de archivos auxiliares citados en el informe identifican la procedencia histórica. Sus resultados esenciales permanecen en las tablas anteriores y en el informe de evidencias. Retiré los logs, JSON, TRX y guiones temporales; las pruebas mantenidas en el código del proyecto siguen en sus carpetas originales. Para repetir el recorrido propio de auditoría será necesario preparar un nuevo ejecutor o seguir sus pasos documentados.
