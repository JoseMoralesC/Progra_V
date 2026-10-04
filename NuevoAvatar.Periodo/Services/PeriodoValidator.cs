using NuevoAvatar.Periodo.Nuevo.Entities;

namespace NuevoAvatar.Periodo.Nuevo.Services;

public class PeriodoValidator : IPeriodoValidator
{
    public void Validar(PeriodoRequest request)
    {
        if (request.Anio <= 0)
        {
            throw new ArgumentException(
                "El año debe ser mayor que cero.");
        }

        if (request.NumeroPeriodo <= 0)
        {
            throw new ArgumentException(
                "El número de periodo debe ser mayor que cero.");
        }

        if (request.FechaInicio == default)
        {
            throw new ArgumentException(
                "La fecha de inicio es requerida.");
        }

        if (request.FechaFin == default)
        {
            throw new ArgumentException(
                "La fecha de fin es requerida.");
        }

        if (request.FechaFin < request.FechaInicio)
        {
            throw new ArgumentException(
                "La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
    }
}