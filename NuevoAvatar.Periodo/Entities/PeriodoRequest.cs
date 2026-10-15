namespace NuevoAvatar.Periodo.Nuevo.Entities;

public class PeriodoRequest
{
    public int PeriodoId { get; set; }

    public short Anio { get; set; }

    public byte NumeroPeriodo { get; set; }

    public DateTime FechaInicio { get; set; }

    public DateTime FechaFin { get; set; }
}