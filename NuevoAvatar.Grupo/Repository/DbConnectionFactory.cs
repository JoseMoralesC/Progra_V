using Microsoft.Data.SqlClient;

namespace NuevoAvatar.Grupo.Repository;

public sealed class DbConnectionFactory(IConfiguration configuration) : IDbConnectionFactory
{
    private readonly string _connectionString = configuration.GetConnectionString("DefaultConnection")
        ?? throw new InvalidOperationException("No se configuró ConnectionStrings:DefaultConnection.");

    public SqlConnection CreateConnection() => new(_connectionString);
}
