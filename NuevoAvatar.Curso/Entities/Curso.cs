namespace NuevoAvatar.Curso.Entities;

public sealed class Curso
{
    public int CursoId { get; init; }
    public int CarreraId { get; init; }
    public byte Nivel { get; init; }
    public string Nombre { get; init; } = string.Empty;
}