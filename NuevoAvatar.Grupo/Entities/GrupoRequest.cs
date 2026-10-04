namespace NuevoAvatar.Grupo.Nuevo.Entities;

public class GrupoRequest
{
    public int NumeroGrupo { get; set; }
    public int CursoId { get; set; }
    public int ProfesorId { get; set; }
    public string Horario { get; set; } = string.Empty;
    public int Cupo { get; set; }
    public int PeriodoId { get; set; }
}