using Dapper;
using NuevoAvatar.Grupo.Nuevo.Entities;
using GrupoEntity = NuevoAvatar.Grupo.Nuevo.Entities.Grupo;

namespace NuevoAvatar.Grupo.Nuevo.Repository;

public class GrupoRepository : IGrupoRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public GrupoRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<int> CrearAsync(GrupoRequest grupo)
    {
        const string sql = """
            INSERT INTO academico.Grupo
                (NumeroGrupo, CursoId, ProfesorId, Horario, Cupo, PeriodoId)
            OUTPUT INSERTED.GrupoId
            VALUES
                (@NumeroGrupo, @CursoId, @ProfesorId, @Horario, @Cupo, @PeriodoId);
            """;

        using var connection = _connectionFactory.CreateConnection();

        return await connection.ExecuteScalarAsync<int>(sql, grupo);
    }

    public async Task<bool> ModificarAsync(
        int grupoId,
        GrupoRequest grupo)
    {
        const string sql = """
            UPDATE academico.Grupo
            SET
                NumeroGrupo = @NumeroGrupo,
                CursoId = @CursoId,
                ProfesorId = @ProfesorId,
                Horario = @Horario,
                Cupo = @Cupo,
                PeriodoId = @PeriodoId
            WHERE GrupoId = @GrupoId;
            """;

        using var connection = _connectionFactory.CreateConnection();

        var parametros = new
        {
            GrupoId = grupoId,
            grupo.NumeroGrupo,
            grupo.CursoId,
            grupo.ProfesorId,
            grupo.Horario,
            grupo.Cupo,
            grupo.PeriodoId
        };

        var filasAfectadas = await connection.ExecuteAsync(
            sql,
            parametros);

        return filasAfectadas > 0;
    }

    public async Task<bool> EliminarAsync(int grupoId)
    {
        const string sql = """
            DELETE FROM academico.Grupo
            WHERE GrupoId = @GrupoId;
            """;

        using var connection = _connectionFactory.CreateConnection();

        var filasAfectadas = await connection.ExecuteAsync(
            sql,
            new { GrupoId = grupoId });

        return filasAfectadas > 0;
    }

    public async Task<IEnumerable<GrupoEntity>> ObtenerTodosAsync()
    {
        const string sql = """
            SELECT
                GrupoId,
                NumeroGrupo,
                CursoId,
                ProfesorId,
                Horario,
                Cupo,
                PeriodoId
            FROM academico.Grupo;
            """;

        using var connection = _connectionFactory.CreateConnection();

        return await connection.QueryAsync<GrupoEntity>(sql);
    }

    public async Task<GrupoEntity?> ObtenerPorIdAsync(int grupoId)
    {
        const string sql = """
            SELECT
                GrupoId,
                NumeroGrupo,
                CursoId,
                ProfesorId,
                Horario,
                Cupo,
                PeriodoId
            FROM academico.Grupo
            WHERE GrupoId = @GrupoId;
            """;

        using var connection = _connectionFactory.CreateConnection();

        return await connection.QuerySingleOrDefaultAsync<GrupoEntity>(
            sql,
            new { GrupoId = grupoId });
    }
}