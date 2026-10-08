namespace NuevoAvatar.Curso.Entities;

public sealed class CursoRequest
{
    public int CarreraId { get; init; }
    public byte Nivel { get; init; }
    public string Nombre { get; init; } = string.Empty;
}