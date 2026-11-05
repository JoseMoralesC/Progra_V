import { test } from 'node:test';
import assert from 'node:assert/strict';
import { profesoresApi, validarProfesor } from '../src/profesores-api.js';
const valido = { tipoIdentificacion: 'Cédula', identificacion: '1', nombreCompleto: 'Ana Mora', email: 'ana@cuc.ac.cr', fechaNacimiento: '1990-01-01', telefonos: ['8888'] };
test('valida mayoría de edad y teléfonos únicos', () => { assert.equal(validarProfesor(valido), ''); assert.match(validarProfesor({ ...valido, fechaNacimiento: '2020-01-01' }), /mayor/); assert.match(validarProfesor({ ...valido, telefonos: ['1', '1'] }), /repetir/); });
test('profesores usa únicamente el gateway con paginación', async () => { const calls = []; const api = profesoresApi({ request: async (...x) => { calls.push(x); return {}; } }); await api.buscar({ texto: 'Ana', page: 2 }); assert.match(calls[0][0], /^\/gateway\/admin\/profesor\/buscar/); assert.match(calls[0][0], /page=2/); });
