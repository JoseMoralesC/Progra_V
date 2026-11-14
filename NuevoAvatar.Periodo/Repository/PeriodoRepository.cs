using Dapper;
using NuevoAvatar.Periodo.Nuevo.Entities;

namespace NuevoAvatar.Periodo.Nuevo.Repository;

public class PeriodoRepository : IPeriodoRepository
{
    private readonly IDbConnectionFactory _connectionFactory;
    private readonly NuevoAvatar.Integracion.AuditoriaTransaccion auditoria;

    public PeriodoRepository(IDbConnectionFactory connectionFactory, NuevoAvatar.Integracion.AuditoriaTransaccion auditoria)
    {
        _connectionFactory = connectionFactory;
        this.auditoria = auditoria;
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
        connection.Open();
        using var tx = connection.BeginTransaction(System.Data.IsolationLevel.Serializable);

        const string sqlSolapado = """
            SELECT CASE WHEN EXISTS(
                SELECT 1 FROM academico.Periodo WITH (UPDLOCK, HOLDLOCK)
                WHERE Anio = @Anio AND NumeroPeriodo = @NumeroPeriodo AND FechaInicio <= @FechaFin AND FechaFin >= @FechaInicio
            ) THEN 1 ELSE 0 END;
            """;
        if (await connection.ExecuteScalarAsync<bool>(sqlSolapado, request, tx))
        {
            throw new InvalidOperationException("El rango se solapa con otro periodo del mismo año y número.");
        }

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

        var id = await connection.ExecuteScalarAsync<int>(
            sql,
            request,
            tx);
        await auditoria.Registrar(connection, tx, "Creación de periodo", null, new { PeriodoId = id, request });
        tx.Commit();
        return id;
    }

    public async Task<bool> ModificarAsync(
        int periodoId,
        PeriodoRequest request)
    {
        using var connection = _connectionFactory.CreateConnection();
        connection.Open();
        using var tx = connection.BeginTransaction(System.Data.IsolationLevel.Serializable);

        const string sqlSolapado = """
            SELECT CASE WHEN EXISTS(
                SELECT 1 FROM academico.Periodo WITH (UPDLOCK, HOLDLOCK)
                WHERE Anio = @Anio AND NumeroPeriodo = @NumeroPeriodo AND PeriodoId <> @PeriodoId AND FechaInicio <= @FechaFin AND FechaFin >= @FechaInicio
            ) THEN 1 ELSE 0 END;
            """;
        var parametros = new
        {
            PeriodoId = periodoId,
            request.Anio,
            request.NumeroPeriodo,
            request.FechaInicio,
            request.FechaFin
        };
        if (await connection.ExecuteScalarAsync<bool>(sqlSolapado, parametros, tx))
        {
            throw new InvalidOperationException("El rango se solapa con otro periodo del mismo año y número.");
        }

        const string sql = """
            UPDATE academico.Periodo
            SET
                Anio = @Anio,
                NumeroPeriodo = @NumeroPeriodo,
                FechaInicio = @FechaInicio,
                FechaFin = @FechaFin
            WHERE PeriodoId = @PeriodoId;
            """;

        var anterior = await connection.QuerySingleOrDefaultAsync<PeriodoRequest>("SELECT * FROM academico.Periodo WITH (UPDLOCK) WHERE PeriodoId=@periodoId", new { periodoId }, tx);
        var filasAfectadas = await connection.ExecuteAsync(
            sql,
            parametros,
            tx);
        if (filasAfectadas > 0) await auditoria.Registrar(connection, tx, "Modificación de periodo", anterior, parametros);
        tx.Commit();

        return filasAfectadas > 0;
    }

    public async Task<bool> EliminarAsync(int periodoId)
    {
        using var connection = _connectionFactory.CreateConnection();
        connection.Open();
        using var tx = connection.BeginTransaction();
        var anterior = await connection.QuerySingleOrDefaultAsync<PeriodoRequest>("SELECT * FROM academico.Periodo WITH (UPDLOCK) WHERE PeriodoId=@periodoId", new { periodoId }, tx);

        const string sql = """
            DELETE FROM academico.Periodo
            WHERE PeriodoId = @PeriodoId;
            """;

        var filasAfectadas = await connection.ExecuteAsync(
            sql,
            new { PeriodoId = periodoId }, tx);

        if (filasAfectadas > 0) await auditoria.Registrar(connection, tx, "Eliminación de periodo", anterior, null);
        tx.Commit();

        return filasAfectadas > 0;
    }
    public async Task<bool> ExisteDuplicadoAsync(short anio, byte numero, int? excluirId = null) { using var c=_connectionFactory.CreateConnection(); return await c.ExecuteScalarAsync<bool>("SELECT CASE WHEN EXISTS(SELECT 1 FROM academico.Periodo WHERE Anio=@anio AND NumeroPeriodo=@numero AND (@excluirId IS NULL OR PeriodoId<>@excluirId)) THEN 1 ELSE 0 END",new{anio,numero,excluirId}); }
    public async Task<bool> ExisteSolapamientoAsync(short anio, byte numero, DateTime inicio, DateTime fin, int? excluirId = null) { using var c=_connectionFactory.CreateConnection(); return await c.ExecuteScalarAsync<bool>("SELECT CASE WHEN EXISTS(SELECT 1 FROM academico.Periodo WHERE Anio=@anio AND NumeroPeriodo=@numero AND FechaInicio<=@fin AND FechaFin>=@inicio AND (@excluirId IS NULL OR PeriodoId<>@excluirId)) THEN 1 ELSE 0 END",new{anio,numero,inicio,fin,excluirId}); }
}
