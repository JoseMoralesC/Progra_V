"""Comprueba accesos SQL escritos y entidades JPA de las aplicaciones activas."""
import json
from pathlib import Path
import re

root=Path(__file__).resolve().parent.parent
manifest=json.loads((root/"db/por_servicio/distribucion.json").read_text())
owners={table.lower():service for service,tables in manifest.items() for table in tables}
failures=[];checked=0
for service in manifest:
    folder=root/("NuevoAvatar."+service)
    for file in folder.rglob("*"):
        if file.relative_to(folder).parts[:2] == ("src", "test"):continue
        if file.suffix not in {".cs",".java"} or set(file.parts)&{"bin","obj","target"}:continue
        checked+=1
        try:source=file.read_text(encoding="utf-8-sig")
        except UnicodeDecodeError:source=file.read_text(encoding="cp1252")
        for table in set(re.findall(r"(?i)\b(?:FROM|JOIN|UPDATE|INTO)\s+([a-z]+\.[a-z_]+)",source)):
            if table.lower() in owners and owners[table.lower()]!=service:
                failures.append({"service":service,"file":str(file.relative_to(root)),"foreign_table":table})
        if re.search(r"(?m)^@Entity\s*$",source):
            table=re.search(r'@Table\(name\s*=\s*"([^"]+)",\s*schema\s*=\s*"([^"]+)"',source)
            if table and owners.get((table[2]+"."+table[1]).lower()) not in {None,service}:
                failures.append({"service":service,"file":str(file.relative_to(root)),"foreign_entity":table[1]})
report={"passed":not failures,"services":len(manifest),"source_files":checked,"failures":failures,
        "scope":"Accesos SQL explicitos y entidades JPA; complementa las cuentas SQL y las pruebas HTTP."}
(root/"docs/pruebas/limites-servicios.json").write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding="utf-8")
print(f"Limites SQL/JPA: {len(manifest)} servicios, {checked} fuentes, {len(failures)} infracciones.")
raise SystemExit(1 if failures else 0)
