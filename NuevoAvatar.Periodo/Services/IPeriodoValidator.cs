using NuevoAvatar.Periodo.Nuevo.Entities;

namespace NuevoAvatar.Periodo.Nuevo.Services;

public interface IPeriodoValidator
{
    void Validar(PeriodoRequest request);
}