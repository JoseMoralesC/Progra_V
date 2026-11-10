export function esEstudiante(user) {
    const role = `${user?.nombreRol || ''} ${user?.idRol || ''}`
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .toLocaleUpperCase('es')
    return role.split(/[^A-Z0-9]+/).includes('ESTUDIANTE')
}
