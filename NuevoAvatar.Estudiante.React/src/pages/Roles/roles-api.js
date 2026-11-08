const rol = '/gateway/admin/rol'
const modulo = '/gateway/admin/modulo'

export const rolesApi = cliente => ({
    listar: () => cliente.request(rol),
    tamanoPagina: async () => {
        try {
            const parametro = await cliente.request('/gateway/admin/parametro/TAMPAG')
            const valor = Number(parametro?.valor)
            if (Number.isInteger(valor) && valor > 0) return Math.min(valor, 15)
        } catch { return 15 }
        return 15
    },
    modulos: () => cliente.request(modulo),
    asignados: idModulo => cliente.request(`${modulo}/${encodeURIComponent(idModulo)}/roles`),
    guardarAsignados: (idModulo, ids) => cliente.request(`${modulo}/${encodeURIComponent(idModulo)}/roles`, {
        method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(ids)
    }),
    menuPropio: () => cliente.request('/gateway/menu'),
    crear: datos => cliente.request(rol, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(datos)
    }),
    modificar: datos => cliente.request(`${rol}/${encodeURIComponent(datos.idRol)}`, {
        method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(datos)
    }),
    eliminar: idRol => cliente.request(`${rol}/${encodeURIComponent(idRol)}`, { method: 'DELETE' })
})

export function validarRol(datos, roles) {
    if (!datos.idRol.trim()) return 'El ID es obligatorio.'
    const nombre = datos.nombreRol.trim()
    if (!nombre || !/^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/.test(nombre)) return 'El nombre solo puede contener letras y espacios.'
    if (roles.some(item => item.idRol !== datos.idRol && item.nombreRol.trim().toLocaleLowerCase() === nombre.toLocaleLowerCase()))
        return 'Ya existe un rol con ese nombre.'
    return ''
}
