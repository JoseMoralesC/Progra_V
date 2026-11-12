const ruta = '/gateway/admin/expediente'

export const expedientesApi = cliente => ({
    listar: () => cliente.request(ruta),
    obtener: identificacion => cliente.request(`${ruta}/${encodeURIComponent(identificacion)}`),
    crear: datos => cliente.request(ruta, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(datos)
    }),
    modificar: (identificacion, datos) => cliente.request(`${ruta}/${encodeURIComponent(identificacion)}`, {
        method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(datos)
    }),
    eliminar: identificacion => cliente.request(`${ruta}/${encodeURIComponent(identificacion)}`, { method: 'DELETE' }),
    estudiantes: () => cliente.request('/gateway/admin/usuario'),
    roles: () => cliente.request('/gateway/admin/rol'),
    provincias: () => cliente.request('/gateway/mat/provincias'),
    cantones: provinciaId => cliente.request(`/gateway/mat/cantones/${encodeURIComponent(provinciaId)}`),
    distritos: (provinciaId, cantonId) => cliente.request(
        `/gateway/mat/distritos/${encodeURIComponent(provinciaId)}/${encodeURIComponent(cantonId)}`)
})

export function validarExpediente(datos, estudiantes, usuarioSeleccionado) {
    if (!usuarioSeleccionado) return 'Selecciona la cuenta de estudiante asociada al expediente.'
    if (!datos.tipoIdentificacion?.trim() || !datos.identificacion?.trim()) return 'La identificación es obligatoria.'
    if (!datos.email?.trim() || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(datos.email.trim())) return 'Ingresa un correo válido.'
    if (!datos.nombreCompleto?.trim() || !/^[\p{L}\p{M} ]+$/u.test(datos.nombreCompleto.trim()))
        return 'El nombre solo puede contener letras y espacios.'
    if (!datos.fechaNacimiento) return 'La fecha de nacimiento es obligatoria.'
    if (!datos.provinciaId || !datos.cantonId || !datos.distritoId) return 'Completa la provincia, el cantón y el distrito.'
    if (!datos.otrasSenas?.trim()) return 'Las otras señas son obligatorias.'
    if (!datos.telefonos.length || datos.telefonos.some(telefono => !telefono.trim()))
        return 'Ingresa al menos un teléfono y completa todos los campos.'
    const estudiante = estudiantes.find(usuario => usuario.email === usuarioSeleccionado)
    if (!estudiante || estudiante.email !== datos.email || estudiante.identificacion !== datos.identificacion ||
        estudiante.tipoIdentificacion !== datos.tipoIdentificacion)
        return 'Los datos de identidad deben corresponder a la cuenta de estudiante seleccionada.'
    return ''
}
