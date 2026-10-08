using System.Data;

namespace NuevoAvatar.Curso.Repository;

public interface IDbConnectionFactory
{
    IDbConnection CreateConnection();
}