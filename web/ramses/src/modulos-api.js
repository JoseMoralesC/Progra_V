const ruta = '/gateway/admin/modulo';
export const normalizarModulo = value => ({
  idModulo: value.idModulo.trim().toUpperCase(), nombreModulo: value.nombreModulo.trim(),
  activo: Boolean(value.activo), orden: Number(value.orden)
});
export function validarModulo(value) {
  const modulo = normalizarModulo(value);
  if (!/^[A-Z0-9_]{2,20}$/.test(modulo.idModulo)) return 'El identificador debe tener de 2 a 20 letras, números o guion bajo.';
  if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/.test(modulo.nombreModulo)) return 'El nombre solo puede contener letras y espacios.';
  if (!Number.isInteger(modulo.orden) || modulo.orden < 0) return 'El orden debe ser un número entero positivo.';
  return '';
}
export const moduloApi = cliente => ({
  listar: () => cliente.request(ruta),
  crear: modulo => cliente.request(ruta, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(normalizarModulo(modulo)) }),
  modificar: modulo => cliente.request(`${ruta}/${encodeURIComponent(modulo.idModulo)}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(normalizarModulo(modulo)) }),
  eliminar: id => cliente.request(`${ruta}/${encodeURIComponent(id)}`, { method: 'DELETE' }),
  roles: id => cliente.request(`${ruta}/${encodeURIComponent(id)}/roles`),
  guardarRoles: (id, roles) => cliente.request(`${ruta}/${encodeURIComponent(id)}/roles`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(roles) }),
  listarRoles: () => cliente.request('/gateway/admin/rol')
});
