namespace NuevoAvatar.Curso.Repository;

public enum CursoRepositoryError
{
    CarreraNotFound,
    CursoReferenced
}

public sealed class CursoRepositoryException(
    CursoRepositoryError error,
    int id,
    Exception innerException) : Exception(innerException.Message, innerException)
{
    public CursoRepositoryError Error { get; } = error;
    public int Id { get; } = id;
}