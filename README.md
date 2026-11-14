# Nuevo Avatar

Backend academico de Programacion V: 22 servicios REST en Java 25 y .NET 8, con una base SQL Server por servicio en la misma instancia accesible mediante Tailscale.

## Ejecutar

La configuracion local se encuentra en `.env`. Las aplicaciones seleccionan sus variables `SERVICE_DB_USER_<SERVICIO>` y `SERVICE_DB_PASSWORD_<SERVICIO>`; las credenciales administrativas `DB_USERNAME` / `DB_PASSWORD` se usan para preparar la infraestructura, no para ejecutar los servicios.

Las direcciones de escucha y los destinos HTTP se configuran con `<SERVICIO>_BASE_URL`, `GATEWAY_BASE_URL` y las variables de cada cliente web. Consulta [configuracion de URLs](docs/configuracion-urls.md) para los nombres y consumidores. Los destinos locales de respaldo se retiraron: cada proceso necesita las variables que consume.

Para una instalacion nueva, con la base original disponible y una cuenta SQL administradora:

```powershell
python tools/migrar-bases.py --aplicar
python tools/preparar-cuentas.py --aplicar
powershell -NoProfile -File tools/compilar-servicios.ps1
```

Para esta copia de trabajo, las bases y cuentas ya fueron preparadas. Los dos accesos de operacion son:

```powershell
.\iniciar.bat
.\detener.bat
```

`iniciar.bat` compila y abre los 22 servicios, Gateway y frontend en segundo plano. Omite los servicios propios que ya escuchan y rechaza puertos ocupados por procesos ajenos. Los destinos externos se muestran como tales y no se inician localmente. `detener.bat` identifica los procesos propios mediante su registro y los puertos configurados. Detalles y opciones: [arranque y parada](docs/arranque-parada.md).

Para revisar sin compilar, arrancar ni detener procesos:

```powershell
.\iniciar.bat -Verificar
.\detener.bat -Verificar
```

Los logs y el registro local de procesos se guardan en `outputs/ejecucion/`, excluido de Git. El arranque no exige un equipo especifico de Tailscale; la conectividad de las bases y dependencias debe corresponder con tu `.env`.

Java arranca desde el jar compilado. Al cambiar codigo, volver a ejecutar `tools/compilar-servicios.ps1`, opcionalmente con `-Servicio Nombre`. La compilacion limpia y la validacion de artefactos evitan reutilizar clases incompletas generadas por el IDE.

## Comprobar

```powershell
python tools/validar-artefactos.py
python tools/migrar-bases.py --verificar
```

Estas herramientas verifican los artefactos compilados y la estructura de las bases existentes. La configuracion y migracion de SQL requiere Python con `pyodbc` y Microsoft ODBC Driver 18.

## Referencias

- [Plan conjunto de los avances 2 y 3: alcance, historias y reparto](docs/segundo_avance/README.md).
- [Mapa de archivos y registro de limpieza](docs/mapeo-pruebas/MAPEO.md).
- [Integracion, contratos internos y recuperacion de pagos](docs/primer_avance/integracion-servicios.md).
- [Distribucion y migracion de las bases](db/por_servicio/README.md).
- [Pruebas de integracion](docs/primer_avance/pruebas/integracion-separada.json).
- [Auditoria de accesos SQL y entidades JPA](docs/primer_avance/pruebas/limites-servicios.json).
- [Requisitos originales](docs/primer_avance/requerimientos/Proyecto%20Nuevo%20Avatar%20V1.pdf).

Las aplicaciones activas estan en las carpetas `NuevoAvatar.*` de la raiz. Las pruebas automatizadas y sus ejecutores se retiraron del arbol de trabajo; las fuentes versionadas anteriores permanecen en el historial de Git. Los informes de ejecuciones anteriores se conservan como evidencia historica.
