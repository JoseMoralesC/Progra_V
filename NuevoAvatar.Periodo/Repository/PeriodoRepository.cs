using Dapper;
using NuevoAvatar.Periodo.Nuevo.Entities;

namespace NuevoAvatar.Periodo.Nuevo.Repository;

public class PeriodoRepository : IPeriodoRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public PeriodoRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<IEnumerable<PeriodoRequest>> ObtenerTodosAsync()
    {
        using var connection = _connectionFactory.CreateConnection();

        const string sql = """
            SELECT
                PeriodoId, Anio,
                NumeroPeriodo,
                FechaInicio,
                FechaFin
            FROM academico.Periodo
            ORDER BY Anio, NumeroPeriodo;
            """;

        return await connection.QueryAsync<PeriodoRequest>(sql);
    }

    public async Task<PeriodoRequest?> ObtenerPorIdAsync(int periodoId)
    {
        using var connection = _connectionFactory.CreateConnection();

        const string sql = """
            SELECT
                PeriodoId, Anio,
                NumeroPeriodo,
                FechaInicio,
                FechaFin
            FROM academico.Periodo
            WHERE PeriodoId = @PeriodoId;
            """;

        return await connection.QuerySingleOrDefaultAsync<PeriodoRequest>(
            sql,
            new { PeriodoId = periodoId });
    }

    public async Task<int> CrearAsync(PeriodoRequest request)
    {
        using var connection = _connectionFactory.CreateConnection();

        const string sql = """
            INSERT INTO academico.Periodo
            (
                Anio,
                NumeroPeriodo,
                FechaInicio,
                FechaFin
            )
            OUTPUT INSERTED.PeriodoId
            VALUES
            (
                @Anio,
                @NumeroPeriodo,
                @FechaInicio,
                @FechaFin
            );
            """;

        return await connection.ExecuteScalarAsync<int>(
            sql,
            request);
    }

    public async Task<bool> ModificarAsync(
        int periodoId,
        PeriodoRequest request)
    {
        using var connection = _connectionFactory.CreateConnection();

        const string sql = """
            UPDATE academico.Periodo
            SET
                Anio = @Anio,
                NumeroPeriodo = @NumeroPeriodo,
                FechaInicio = @FechaInicio,
                FechaFin = @FechaFin
            WHERE PeriodoId = @PeriodoId;
            """;

        var filasAfectadas = await connection.ExecuteAsync(
            sql,
            new
            {
                PeriodoId = periodoId,
                request.Anio,
                request.NumeroPeriodo,
                request.FechaInicio,
                request.FechaFin
            });

        return filasAfectadas > 0;
    }

    public async Task<bool> EliminarAsync(int periodoId)
    {
        using var connection = _connectionFactory.CreateConnection();

        const string sql = """
            DELETE FROM academico.Periodo
            WHERE PeriodoId = @PeriodoId;
            """;

        var filasAfectadas = await connection.ExecuteAsync(
            sql,
            new { PeriodoId = periodoId });

        return filasAfectadas > 0;
    }
    public async Task<bool> ExisteDuplicadoAsync(short anio, byte numero, int? excluirId = null) { using var c=_connectionFactory.CreateConnection(); return await c.ExecuteScalarAsync<bool>("SELECT CASE WHEN EXISTS(SELECT 1 FROM academico.Periodo WHERE Anio=@anio AND NumeroPeriodo=@numero AND (@excluirId IS NULL OR PeriodoId<>@excluirId)) THEN 1 ELSE 0 END",new{anio,numero,excluirId}); }
    public async Task<bool> ExisteSolapamientoAsync(short anio, DateTime inicio, DateTime fin, int? excluirId = null) { using var c=_connectionFactory.CreateConnection(); return await c.ExecuteScalarAsync<bool>("SELECT CASE WHEN EXISTS(SELECT 1 FROM academico.Periodo WHERE Anio=@anio AND FechaInicio<=@fin AND FechaFin>=@inicio AND (@excluirId IS NULL OR PeriodoId<>@excluirId)) THEN 1 ELSE 0 END",new{anio,inicio,fin,excluirId}); }
}
