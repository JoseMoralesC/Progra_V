using Dapper;
using Microsoft.Data.SqlClient;
using NuevoAvatar.Curso.Entities;
using CursoEntity = NuevoAvatar.Curso.Entities.Curso;

namespace NuevoAvatar.Curso.Repository;

public sealed class CursoRepository(IDbConnectionFactory connectionFactory) : ICursoRepository
{
    private const string SelectColumns = "CursoId, CarreraId, Nivel, Nombre";

    public async Task<IReadOnlyList<CursoEntity>> GetAllAsync()
    {
        const string sql = $"""
            SELECT {SelectColumns}
            FROM academico.Curso
            ORDER BY CursoId;
            """;

        using var connection = connectionFactory.CreateConnection();
        var cursos = await connection.QueryAsync<CursoEntity>(sql);

        return cursos.AsList();
    }

    public async Task<CursoEntity?> GetByIdAsync(int id)
    {
        const string sql = $"""
            SELECT {SelectColumns}
            FROM academico.Curso
            WHERE CursoId = @Id;
            """;

        using var connection = connectionFactory.CreateConnection();

        return await connection.QuerySingleOrDefaultAsync<CursoEntity>(
            sql,
            new { Id = id });
    }

    public async Task<IReadOnlyList<CursoEntity>> GetByCarreraIdAsync(int carreraId)
    {
        const string sql = $"""
            SELECT {SelectColumns}
            FROM academico.Curso
            WHERE CarreraId = @CarreraId
            ORDER BY CursoId;
            """;

        using var connection = connectionFactory.CreateConnection();

        var cursos = await connection.QueryAsync<CursoEntity>(
            sql,
            new { CarreraId = carreraId });

        return cursos.AsList();
    }

    public async Task<int> CreateAsync(CursoRequest curso)
    {
        const string sql = """
            INSERT INTO academico.Curso (CarreraId, Nivel, Nombre)
            VALUES (@CarreraId, @Nivel, @Nombre);

            SELECT CAST(SCOPE_IDENTITY() AS int);
            """;

        using var connection = connectionFactory.CreateConnection();

        try
        {
            return await connection.ExecuteScalarAsync<int>(sql, curso);
        }
        catch (SqlException exception) when (IsForeignKeyViolation(exception))
        {
            throw new CursoRepositoryException(
                CursoRepositoryError.CarreraNotFound,
                curso.CarreraId,
                exception);
        }
    }

    public async Task<bool> UpdateAsync(int id, CursoRequest curso)
    {
        const string sql = """
            UPDATE academico.Curso
            SET CarreraId = @CarreraId,
                Nivel = @Nivel,
                Nombre = @Nombre
            WHERE CursoId = @Id;
            """;

        using var connection = connectionFactory.CreateConnection();

        try
        {
            var affectedRows = await connection.ExecuteAsync(
                sql,
                new
                {
                    Id = id,
                    curso.CarreraId,
                    curso.Nivel,
                    curso.Nombre
                });

            return affectedRows == 1;
        }
        catch (SqlException exception) when (IsForeignKeyViolation(exception))
        {
            throw new CursoRepositoryException(
                CursoRepositoryError.CarreraNotFound,
                curso.CarreraId,
                exception);
        }
    }

    public async Task<bool> DeleteAsync(int id)
    {
        const string sql = """
            DELETE FROM academico.Curso
            WHERE CursoId = @Id;
            """;

        using var connection = connectionFactory.CreateConnection();

        try
        {
            var affectedRows = await connection.ExecuteAsync(
                sql,
                new { Id = id });

            return affectedRows == 1;
        }
        catch (SqlException exception) when (IsForeignKeyViolation(exception))
        {
            throw new CursoRepositoryException(
                CursoRepositoryError.CursoReferenced,
                id,
                exception);
        }
    }

    private static bool IsForeignKeyViolation(SqlException exception) =>
        exception.Number == 547;
}