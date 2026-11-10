const ruta = '/gateway/admin/carrera';
export const carrerasApi = cliente => ({
  listar: institucionId => cliente.request(ruta + (institucionId ? `?institucionId=${institucionId}` : '')),
  instituciones: () => cliente.request('/gateway/admin/institucion'),
  profesores: texto => cliente.request(`/gateway/admin/profesor/buscar?texto=${encodeURIComponent(texto)}&page=0&size=100&sort=nombreCompleto&direction=asc`),
  crear: value => cliente.request(ruta, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(value) }),
  modificar: value => cliente.request(`${ruta}/${value.id}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(value) }),
  eliminar: id => cliente.request(`${ruta}/${id}`, { method: 'DELETE' })
});
export function validarCarrera(value) {
  if (!/^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$/.test(value.nombre?.trim() || '')) return 'El nombre solo puede contener letras y espacios.';
  if (!(Number(value.institucionId) > 0)) return 'Seleccioná una institución.';
  if (!(Number(value.directorProfesorId) > 0)) return 'Seleccioná un profesor director.';
  return '';
}
