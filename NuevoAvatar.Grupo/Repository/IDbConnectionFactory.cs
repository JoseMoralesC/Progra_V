using System.Data;

namespace NuevoAvatar.Grupo.Nuevo.Repository;

public interface IDbConnectionFactory
{
    IDbConnection CreateConnection();
}