const fs = require('fs');

function fixDataInCollection(filePath) {
  const json = JSON.parse(fs.readFileSync(filePath, 'utf8'));
  let fixedCount = 0;

  function walk(item) {
    if (item.event) {
      item.event.forEach(e => {
        if (!e.script || !e.script.exec) return;
        const code = e.script.exec.join('\n');
        if (/\b(?:const|let)\s+data\s*=\s*/.test(code)) {
          // Replace 'const data =' or 'let data =' with 'const respData ='
          // and replace identifier 'data' references (data., data[, data)) with 'respData'
          let newCode = code.replace(/\b(const|let)\s+data\s*=\s*/g, '$1 respData = ');
          newCode = newCode.replace(/\bdata(\.|\b(?=\[)|(?=\s*\)))/g, 'respData$1');
          // also in expect(data, ...)
          newCode = newCode.replace(/\bpm\.expect\(data\b/g, 'pm.expect(respData');
          newCode = newCode.replace(/\bexpect\(data\b/g, 'expect(respData');
          e.script.exec = newCode.split('\n');
          fixedCount++;
        }
      });
    }
    if (item.item) item.item.forEach(walk);
  }

  walk(json);
  fs.writeFileSync(filePath, JSON.stringify(json, null, 2) + '\n');
  console.log(`Fixed ${fixedCount} script(s) in ${filePath}`);
}

fixDataInCollection('docs/pruebas/postman/PrograV_Persona3_Pruebas_POSTMAN_FINAL.json');
fixDataInCollection('docs/pruebas/postman/Persona5_Jose.postman_collection.json');
