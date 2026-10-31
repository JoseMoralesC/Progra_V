import { test } from 'node:test';
import assert from 'node:assert/strict';
import { carrerasApi, validarCarrera } from '../src/carreras-api.js';
test('exige nombre, institución y profesor', () => { assert.equal(validarCarrera({ nombre: 'Ingeniería', institucionId: 1, directorProfesorId: 2 }), ''); assert.match(validarCarrera({ nombre: 'Ingeniería 2', institucionId: 1, directorProfesorId: 2 }), /nombre/); assert.match(validarCarrera({ nombre: 'Ingeniería', institucionId: '', directorProfesorId: 2 }), /institución/); });
test('consume carrera, institución y profesor solo por gateway', async () => { const calls = []; const api = carrerasApi({ request: async path => { calls.push(path); return []; } }); await api.listar(4); await api.instituciones(); await api.profesores('Ana'); assert.equal(calls[0], '/gateway/admin/carrera?institucionId=4'); assert.equal(calls[1], '/gateway/admin/institucion'); assert.match(calls[2], /^\/gateway\/admin\/profesor\/buscar/); });
