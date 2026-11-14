using Dapper;
using NuevoAvatar.Grupo.Nuevo.Entities;
using GrupoEntity = NuevoAvatar.Grupo.Nuevo.Entities.Grupo;

namespace NuevoAvatar.Grupo.Nuevo.Repository;

public class GrupoRepository : IGrupoRepository
{
    private readonly IDbConnectionFactory _connectionFactory;
    private readonly NuevoAvatar.Integracion.AuditoriaTransaccion auditoria;

    public GrupoRepository(IDbConnectionFactory connectionFactory, NuevoAvatar.Integracion.AuditoriaTransaccion auditoria)
    {
        _connectionFactory = connectionFactory;
        this.auditoria = auditoria;
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

        connection.Open();
        using var tx = connection.BeginTransaction();
        var id = await connection.ExecuteScalarAsync<int>(sql, grupo, tx);
        await auditoria.Registrar(connection, tx, "Creación de grupo", null, new { GrupoId = id, grupo });
        tx.Commit();
        return id;
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

        connection.Open();
        using var tx = connection.BeginTransaction();
        var anterior = await connection.QuerySingleOrDefaultAsync<GrupoEntity>("SELECT * FROM academico.Grupo WITH (UPDLOCK) WHERE GrupoId=@grupoId", new { grupoId }, tx);
        var filasAfectadas = await connection.ExecuteAsync(
            sql,
            parametros, tx);

        if (filasAfectadas > 0) await auditoria.Registrar(connection, tx, "Modificación de grupo", anterior, parametros);
        tx.Commit();

        return filasAfectadas > 0;
    }

    public async Task<bool> EliminarAsync(int grupoId)
    {
        const string sql = """
            DELETE FROM academico.Grupo
            WHERE GrupoId = @GrupoId;
            """;

        using var connection = _connectionFactory.CreateConnection();

        connection.Open();
        using var tx = connection.BeginTransaction();
        var anterior = await connection.QuerySingleOrDefaultAsync<GrupoEntity>("SELECT * FROM academico.Grupo WITH (UPDLOCK) WHERE GrupoId=@grupoId", new { grupoId }, tx);
        var filasAfectadas = await connection.ExecuteAsync(
            sql,
            new { GrupoId = grupoId }, tx);

        if (filasAfectadas > 0) await auditoria.Registrar(connection, tx, "Eliminación de grupo", anterior, null);
        tx.Commit();

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
