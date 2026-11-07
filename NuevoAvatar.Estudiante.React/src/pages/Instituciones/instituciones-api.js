const ruta = '/gateway/admin/institucion'

export const institucionesApi = cliente => ({
    listar: nombre => cliente.request(nombre ? `${ruta}?nombre=${encodeURIComponent(nombre)}` : ruta),
    crear: nombre => cliente.request(ruta, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nombre })
    }),
    modificar: (id, nombre) => cliente.request(`${ruta}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nombre })
    }),
    eliminar: id => cliente.request(`${ruta}/${id}`, { method: 'DELETE' }),
    tamanoPagina: async () => {
        try {
            const parametro = await cliente.request('/gateway/admin/parametro/TAMPAG')
            const valor = Number(parametro?.valor)
            if (Number.isInteger(valor) && valor > 0) return Math.min(valor, 15)
        } catch {
            return 15
        }
        return 15
    }
})

export function validarNombre(nombre) {
    const limpio = nombre.trim()
    if (!limpio) return 'El nombre es obligatorio.'
    if (limpio.length > 150) return 'El nombre no puede superar 150 caracteres.'
    if (!/^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$/.test(limpio)) return 'El nombre solo puede contener letras y espacios.'
    return ''
}
