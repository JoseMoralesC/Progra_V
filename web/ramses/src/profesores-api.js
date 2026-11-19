import { leerTamanoPagina, MAX_PAGINA } from './modulos-api.js';
const ruta = '/gateway/admin/profesor';
export { MAX_PAGINA };
export const profesoresApi = cliente => ({
  buscar: ({ texto = '', page = 0, size = MAX_PAGINA, sort = 'nombreCompleto', direction = 'asc', carga = false } = {}) => {
    const lim = Math.min(MAX_PAGINA, Math.max(1, Number(size) || MAX_PAGINA));
    return cliente.request(`${carga ? '/gateway/admin/carga/profesores' : `${ruta}/buscar`}?texto=${encodeURIComponent(texto)}&page=${page}&size=${lim}&sort=${sort}&direction=${direction}`);
  },
  crear: value => cliente.request(ruta, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(value) }),
  modificar: value => cliente.request(`${ruta}/${value.id}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(value) }),
  eliminar: id => cliente.request(`${ruta}/${id}`, { method: 'DELETE' }),
  tamanoPagina: () => leerTamanoPagina(cliente),
  dominio: async () => {
    const parametro = await cliente.request('/gateway/admin/parametro/DOMPROF');
    return String(parametro?.valor ?? parametro ?? '').trim().replace(/^@/, '');
  }
});
export function validarProfesor(value, dominio = '') {
  if (!value.nombreCompleto?.trim() || !value.identificacion?.trim() || !value.tipoIdentificacion?.trim()) return 'Nombre e identificación son requeridos.';
  const email = value.email?.trim() || '';
  if (!/^\S+@\S+\.\S+$/.test(email)) return 'Ingresá un correo válido.';
  const dominioNormalizado = dominio.trim().replace(/^@/, '').toLowerCase();
  if (dominioNormalizado && email.toLowerCase().split('@')[1] !== dominioNormalizado) {
    return `El correo debe pertenecer al dominio ${dominioNormalizado}.`;
  }
  const edad = new Date(); edad.setFullYear(edad.getFullYear() - 18);
  if (!value.fechaNacimiento || new Date(`${value.fechaNacimiento}T00:00:00`) > edad) return 'El profesor debe ser mayor de edad.';
  const telefonos = value.telefonos?.map(x => x.trim()).filter(Boolean) || [];
  const canonicos = telefonos;
  if (!telefonos.length || new Set(canonicos).size !== telefonos.length) return 'Indicá teléfonos sin repetir.';
  if (telefonos.some(t => !/^[2-8]\d{7}$/.test(t))) return 'Indicá teléfonos con solo 8 dígitos, sin espacios, comas ni guiones.';
  return '';
}
