using NuevoAvatar.Grupo.Nuevo.Entities;

namespace NuevoAvatar.Grupo.Nuevo.Services;

public static class GrupoValidator
{
    public static List<string> Validate(GrupoRequest grupo)
    {
        var errors = new List<string>();

        if (grupo.NumeroGrupo <= 0)
            errors.Add("El número de grupo debe ser mayor que cero.");

        if (grupo.CursoId <= 0)
            errors.Add("El curso es requerido.");

        if (grupo.ProfesorId <= 0)
            errors.Add("El profesor es requerido.");

        if (string.IsNullOrWhiteSpace(grupo.Horario))
            errors.Add("El horario es requerido.");

        if (grupo.Cupo <= 0)
            errors.Add("El cupo debe ser mayor que cero.");

        if (grupo.PeriodoId <= 0)
            errors.Add("El periodo es requerido.");

        return errors;
    }
}