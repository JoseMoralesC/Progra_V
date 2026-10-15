# Nuevo Avatar

Backend academico de Programacion V: 22 servicios REST en Java 21 y .NET 8, con una base SQL Server por servicio en la misma instancia accesible mediante Tailscale.

## Ejecutar

La configuracion local se encuentra en `.env`. Las aplicaciones seleccionan sus variables `SERVICE_DB_USER_<SERVICIO>` y `SERVICE_DB_PASSWORD_<SERVICIO>`; las credenciales administrativas `DB_USERNAME` / `DB_PASSWORD` se usan para preparar la infraestructura, no para ejecutar los servicios.

Para una instalacion nueva, con la base original disponible y una cuenta SQL administradora:

```powershell
python tools/migrar-bases.py --aplicar
python tools/preparar-cuentas.py --aplicar
powershell -NoProfile -File tools/compilar-servicios.ps1
```

Para esta copia de trabajo, las bases y cuentas ya fueron preparadas. Los servicios se pueden iniciar individualmente:

```powershell
powershell -NoProfile -File tools/ejecutar-servicio.ps1 -Servicio Autenticacion
```

Tambien estan disponibles `ejecutar_persona1.bat` hasta `ejecutar_persona5.bat`. Cada archivo abre los servicios de ese integrante. Para flujos completos deben estar disponibles sus dependencias; puertos y bases estan en [tools/servicios.json](tools/servicios.json).

Java arranca desde el jar compilado. Al cambiar codigo, volver a ejecutar `tools/compilar-servicios.ps1`, opcionalmente con `-Servicio Nombre`. La compilacion limpia y la validacion de artefactos evitan reutilizar clases incompletas generadas por el IDE.

## Comprobar

```powershell
python tools/auditar-limites.py
python tools/validar-artefactos.py
python tools/probar-integracion.py
python tools/migrar-bases.py --verificar
```

La suite de integracion utiliza bases temporales y cuentas de servicio reales, sin escribir en las bases de negocio ni enviar correos. Detiene sus procesos y elimina las bases que crea al finalizar. Requiere Python con `pyodbc` y `bcrypt`, Microsoft ODBC Driver 18 y permisos administrativos para preparar esos entornos temporales.

## Referencias

- [Integracion, contratos internos y recuperacion de pagos](docs/integracion-servicios.md).
- [Distribucion y migracion de las bases](db/por_servicio/README.md).
- [Pruebas de integracion](docs/pruebas/integracion-separada.json).
- [Auditoria de accesos SQL y entidades JPA](docs/pruebas/limites-servicios.json).
- [Requisitos originales](docs/requerimientos/Proyecto%20Nuevo%20Avatar%20V1.pdf).

Las aplicaciones activas estan en las carpetas `NuevoAvatar.*` de la raiz. Los proyectos agrupados bajo `services/` y sus pruebas corresponden a la estructura anterior y se conservan como referencia.
