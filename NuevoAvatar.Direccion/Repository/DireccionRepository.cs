using Dapper;
using Microsoft.AspNetCore.Connections;
using NuevoAvatar.Direccion.Nuevo;
using NuevoAvatar.Direccion.Nuevo.Entities;

namespace NuevoAvatar.Direccion.Nuevo.Repository;

public class DireccionRepository : IDireccionRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public DireccionRepository(
        IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<IEnumerable<Provincia>> ObtenerProvinciasAsync()
    {
        using var connection = _connectionFactory.CreateConnection();

        const string sql = """
            SELECT
                ProvinciaId,
                Nombre
            FROM matricula.Provincia
            ORDER BY Nombre;
            """;

        return await connection.QueryAsync<Provincia>(sql);
    }

    public async Task<IEnumerable<Canton>> ObtenerCantonesAsync(
        int provinciaId)
    {
        using var connection = _connectionFactory.CreateConnection();

        const string sql = """
            SELECT
                CantonId,
                ProvinciaId,
                Nombre
            FROM matricula.Canton
            WHERE ProvinciaId = @ProvinciaId
            ORDER BY Nombre;
            """;

        return await connection.QueryAsync<Canton>(
            sql,
            new
            {
                ProvinciaId = provinciaId
            });
    }

    public async Task<IEnumerable<Distrito>> ObtenerDistritosAsync(
        int provinciaId,
        int cantonId)
    {
        using var connection = _connectionFactory.CreateConnection();

        const string sql = """
            SELECT
                d.DistritoId,
                d.CantonId,
                d.Nombre
            FROM matricula.Distrito AS d
            INNER JOIN matricula.Canton AS c
                ON c.CantonId = d.CantonId
            WHERE c.ProvinciaId = @ProvinciaId
              AND d.CantonId = @CantonId
            ORDER BY d.Nombre;
            """;

        return await connection.QueryAsync<Distrito>(
            sql,
            new
            {
                ProvinciaId = provinciaId,
                CantonId = cantonId
            });
    }
}