using NuevoAvatar.Curso.Entities;
using NuevoAvatar.Curso.Services;
using NuevoAvatar.Grupo.Nuevo.Entities;
using NuevoAvatar.Grupo.Nuevo.Services;
using NuevoAvatar.Periodo.Nuevo.Entities;
using NuevoAvatar.Periodo.Nuevo.Services;
using Xunit;

public class ValidacionesRequestTest
{
    [Theory]
    [InlineData(1)]
    [InlineData(12)]
    public void CursoAceptaLimitesDelNivel(byte nivel) => Assert.Empty(new CursoValidator().Validate(
        new CursoRequest {CarreraId=1,Nivel=nivel,Nombre="Programación"}));

    [Theory]
    [InlineData(0,"Programación")]
    [InlineData(13,"Programación")]
    [InlineData(1,"Programación 5")]
    public void CursoRechazaNivelONombreInvalidos(byte nivel,string nombre) => Assert.NotEmpty(new CursoValidator().Validate(
        new CursoRequest {CarreraId=1,Nivel=nivel,Nombre=nombre}));

    [Fact]
    public void PeriodoExigeDatos() => Assert.Throws<ArgumentException>(()=>new PeriodoValidator().Validar(new PeriodoRequest()));

    [Fact]
    public void GrupoExigeIdentificadoresHorarioYCupo() => Assert.Equal(6,GrupoValidator.Validate(new GrupoRequest {
        NumeroGrupo=0,CursoId=-1,ProfesorId=0,Horario=" ",Cupo=0,PeriodoId=-2
    }).Count);
}
