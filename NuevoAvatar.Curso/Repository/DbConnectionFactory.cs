using System.Data;
using Microsoft.Data.SqlClient;

namespace NuevoAvatar.Curso.Repository;

public sealed class DbConnectionFactory(
    IConfiguration configuration) : IDbConnectionFactory
{
    private readonly string _connectionString =
        configuration.GetConnectionString("DefaultConnection")
        ?? throw new InvalidOperationException(
            "No se configuró ConnectionStrings:DefaultConnection.");

    public IDbConnection CreateConnection()
    {
        return new SqlConnection(_connectionString);
    }
}