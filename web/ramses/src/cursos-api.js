import { leerTamanoPagina, MAX_PAGINA, paginar } from './modulos-api.js';
const ruta = '/gateway/admin/curso';
export { MAX_PAGINA, paginar };
export const cursosApi = cliente => ({
  listar: ({ carreraId = '', nivel = '' } = {}) => { const p = new URLSearchParams(); if (carreraId) p.set('carreraId', carreraId); if (nivel) p.set('nivel', nivel); return cliente.request(ruta + (p.size ? `?${p}` : '')); },
  carreras: () => cliente.request('/gateway/admin/carrera'),
  crear: value => cliente.request(ruta, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(value) }),
  modificar: value => cliente.request(`${ruta}/${value.cursoId}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(value) }),
  eliminar: id => cliente.request(`${ruta}/${id}`, { method: 'DELETE' }),
  tamanoPagina: () => leerTamanoPagina(cliente)
});
export function validarCurso(value) { if (!(Number(value.carreraId) > 0)) return 'Seleccioná una carrera.'; if (!(Number(value.nivel) >= 1 && Number(value.nivel) <= 12)) return 'El nivel debe estar entre 1 y 12.'; if (!/^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$/.test(value.nombre?.trim() || '')) return 'El nombre solo puede contener letras y espacios.'; return ''; }
