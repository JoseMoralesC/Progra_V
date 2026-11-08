const ruta = '/gateway/admin/parametro'

export const parametrosApi = cliente => ({
    listar: () => cliente.request(ruta),
    crear: parametro => cliente.request(ruta, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(parametro)
    }),
    modificar: parametro => cliente.request(`${ruta}/${encodeURIComponent(parametro.idParametro)}`, {
        method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(parametro)
    }),
    eliminar: id => cliente.request(`${ruta}/${encodeURIComponent(id)}`, { method: 'DELETE' }),
    tamanoPagina: async () => {
        try {
            const parametro = await cliente.request(`${ruta}/TAMPAG`)
            const valor = Number(parametro?.valor)
            if (Number.isInteger(valor) && valor > 0) return Math.min(valor, 15)
        } catch {
            return 15
        }
        return 15
    }
})

export function validarParametro(parametro) {
    if (!/^[A-Z]{1,10}$/.test(parametro.idParametro)) return 'El ID debe tener hasta 10 letras mayúsculas.'
    if (!parametro.valor.trim()) return 'El valor es obligatorio.'
    if (parametro.valor.length > 500) return 'El valor no puede superar 500 caracteres.'
    return ''
}
