using NuevoAvatar.Curso.Entities;
using CursoEntity = NuevoAvatar.Curso.Entities.Curso;

namespace NuevoAvatar.Curso.Services;

public interface ICursoService
{
    Task<IReadOnlyList<CursoEntity>> GetAllAsync();
    Task<CursoEntity?> GetByIdAsync(int id);
    Task<IReadOnlyList<CursoEntity>> GetByCarreraIdAsync(int carreraId);
    Task<int> CreateAsync(CursoRequest curso);
    Task<bool> UpdateAsync(int id, CursoRequest curso);
    Task<bool> DeleteAsync(int id);
}