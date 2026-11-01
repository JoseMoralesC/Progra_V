import { test } from 'node:test'; import assert from 'node:assert/strict'; import { cursosApi, validarCurso } from '../src/cursos-api.js';
test('valida carrera nivel y nombre',()=>{assert.equal(validarCurso({carreraId:1,nivel:12,nombre:'Programación'}),'');assert.match(validarCurso({carreraId:1,nivel:13,nombre:'Programación'}),/nivel/)});
test('combina filtros usando gateway',async()=>{const c=[];const api=cursosApi({request:async p=>{c.push(p);return[]}});await api.listar({carreraId:2,nivel:4});assert.equal(c[0],'/gateway/admin/curso?carreraId=2&nivel=4')});
