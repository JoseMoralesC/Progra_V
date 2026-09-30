using NuevoAvatar.Curso.Entities;

namespace NuevoAvatar.Curso.Services;

public class CursoValidator
{
    public List<string> Validate(CursoRequest curso)
    {
        var errores = new List<string>();

        if (curso is null)
        {
            errores.Add("El curso es requerido.");
            return errores;
        }

        if (curso.CarreraId <= 0)
        {
            errores.Add("CarreraId debe ser mayor que cero.");
        }

        if (curso.Nivel < 1 || curso.Nivel > 12)
        {
            errores.Add("El nivel debe estar entre 1 y 12.");
        }

        if (string.IsNullOrWhiteSpace(curso.Nombre))
        {
            errores.Add("El nombre del curso es requerido.");
        }
        else
        {
            if (curso.Nombre.Length > 150)
            {
                errores.Add("El nombre del curso no puede superar 150 caracteres.");
            }

            if (curso.Nombre.Any(caracter => !char.IsLetter(caracter) && caracter != ' '))
            {
                errores.Add("El nombre del curso solo puede contener letras y espacios.");
            }
        }

        return errores;
    }
}
