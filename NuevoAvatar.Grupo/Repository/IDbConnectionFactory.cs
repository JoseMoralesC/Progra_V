using Microsoft.Data.SqlClient;

namespace NuevoAvatar.Grupo.Repository;

public interface IDbConnectionFactory
{
    SqlConnection CreateConnection();
}
