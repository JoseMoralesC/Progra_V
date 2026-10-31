const ruta = '/gateway/admin/profesor';
export const profesoresApi = cliente => ({
  buscar: ({ texto = '', page = 0, size = 10, sort = 'nombreCompleto', direction = 'asc' } = {}) => cliente.request(`${ruta}/buscar?texto=${encodeURIComponent(texto)}&page=${page}&size=${size}&sort=${sort}&direction=${direction}`),
  crear: value => cliente.request(ruta, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(value) }),
  modificar: value => cliente.request(`${ruta}/${value.id}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(value) }),
  eliminar: id => cliente.request(`${ruta}/${id}`, { method: 'DELETE' })
});
export function validarProfesor(value) {
  if (!value.nombreCompleto?.trim() || !value.identificacion?.trim() || !value.tipoIdentificacion?.trim()) return 'Nombre e identificación son requeridos.';
  if (!/^\S+@\S+\.\S+$/.test(value.email || '')) return 'Ingresá un correo válido.';
  const edad = new Date(); edad.setFullYear(edad.getFullYear() - 18);
  if (!value.fechaNacimiento || new Date(`${value.fechaNacimiento}T00:00:00`) > edad) return 'El profesor debe ser mayor de edad.';
  const telefonos = value.telefonos?.map(x => x.trim()).filter(Boolean) || [];
  if (!telefonos.length || new Set(telefonos).size !== telefonos.length) return 'Indicá teléfonos sin repetir.';
  return '';
}
