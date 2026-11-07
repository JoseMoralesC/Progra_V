const ruta = '/gateway/admin/usuario'

export const usuariosApi = cliente => ({
    listar: filtros => {
        const params = new URLSearchParams()
        for (const campo of ['identificacion', 'nombre', 'tipo']) {
            if (filtros[campo]?.trim()) params.set(campo, filtros[campo].trim())
        }
        return cliente.request(params.size ? `${ruta}?${params}` : ruta)
    },
    roles: () => cliente.request('/gateway/admin/rol'),
    obtener: email => cliente.request(`${ruta}/${encodeURIComponent(email)}`),
    tamanoPagina: async () => {
        try {
            const parametro = await cliente.request('/gateway/admin/parametro/TAMPAG')
            const valor = Number(parametro?.valor)
            if (Number.isInteger(valor) && valor > 0) return Math.min(valor, 15)
        } catch { return 15 }
        return 15
    },
    crear: datos => cliente.request(ruta, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(datos)
    }),
    modificar: (email, datos) => cliente.request(`${ruta}/${encodeURIComponent(email)}`, {
        method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(datos)
    }),
    eliminar: email => cliente.request(`${ruta}/${encodeURIComponent(email)}`, { method: 'DELETE' })
})

export function validarUsuario(datos, roles, creando) {
    const email = datos.email.trim().toLowerCase()
    const rol = roles.find(item => item.idRol === datos.idRol)
    if (!/^[^\s@]+@(?:cuc\.cr|cuc\.ac\.cr)$/.test(email)) return 'El correo debe ser institucional.'
    if (!datos.tipoIdentificacion.trim() || !datos.identificacion.trim()) return 'La identificación es obligatoria.'
    if (!datos.nombre.trim()) return 'El nombre es obligatorio.'
    if (!rol) return 'Selecciona un rol válido.'
    const perfil = rol.nombreRol.trim().toUpperCase()
    if (email.endsWith('@cuc.cr') && perfil !== 'ESTUDIANTE') return 'El dominio cuc.cr requiere el rol Estudiante.'
    if (email.endsWith('@cuc.ac.cr') && !['PROFESOR', 'ADMIN', 'ADMINISTRADOR'].includes(perfil))
        return 'El dominio cuc.ac.cr requiere Profesor o Administrador.'
    if (creando && !datos.contrasena) return 'La contraseña es obligatoria al crear.'
    return ''
}
