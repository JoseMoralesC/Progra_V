"""Rechaza jars con clases incompletas del IDE y contratos compartidos ausentes."""
import argparse
import json
from pathlib import Path
import zipfile

root=Path(__file__).resolve().parent.parent
parser=argparse.ArgumentParser();parser.add_argument("--servicio");args=parser.parse_args()
services=json.loads((root/"tools/servicios.json").read_text())
for service in services:
    if service["kind"]!="java" or (args.servicio and service["name"]!=args.servicio):continue
    folder=root/("NuevoAvatar."+service["name"])
    artifacts=list((folder/"target").glob("*.jar"))
    if len(artifacts)!=1:raise SystemExit("Falta un jar unico de "+service["name"])
    with zipfile.ZipFile(artifacts[0]) as archive:
        names=set(archive.namelist())
        prefix="BOOT-INF/classes/"
        required=["contratos.json","cr/ac/cuc/nuevoavatar/integracion/DatosRemotos.class","cr/ac/cuc/nuevoavatar/integracion/IntegracionBootstrap.class"]
        if any(prefix+name not in names for name in required):raise SystemExit("Contrato interno ausente en "+service["name"])
        for name in names:
            if name.startswith(prefix) and name.endswith(".class") and b"Unresolved compilation problem" in archive.read(name):
                raise SystemExit("Clase incompleta del IDE en "+service["name"]+": "+name+". Reconstruir con tools/compilar-servicios.ps1.")
    print("Artefacto verificado: "+service["name"])
