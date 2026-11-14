namespace NuevoAvatar.Seguridad.Models;

public sealed record PaginaBitacora(List<Bitacora> Items, int Total, int Page, int Size, long HastaId = 0);
