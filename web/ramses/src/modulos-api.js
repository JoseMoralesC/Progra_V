const ruta = '/gateway/admin/modulo';
export const MAX_PAGINA = 15;
export async function leerTamanoPagina(cliente) {
  try {
    const p = await cliente.request('/gateway/admin/parametro/TAMPAG');
    const n = Number(p?.valor ?? p);
    if (Number.isInteger(n) && n >= 1) return Math.min(MAX_PAGINA, n);
  } catch { /* Usa el máximo permitido si el parámetro no está definido. */ }
  return MAX_PAGINA;
}
export function paginar(items = [], page = 0, size = MAX_PAGINA) {
  const lim = Math.min(MAX_PAGINA, Math.max(1, Number(size) || MAX_PAGINA));
  const total = items.length;
  const maxPage = Math.max(0, Math.ceil(total / lim) - 1);
  const actual = Math.min(Math.max(0, page), maxPage);
  return { items: items.slice(actual * lim, (actual + 1) * lim), page: actual, size: lim, total };
}
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
  listarRoles: () => cliente.request('/gateway/admin/rol'),
  tamanoPagina: () => leerTamanoPagina(cliente)
});
