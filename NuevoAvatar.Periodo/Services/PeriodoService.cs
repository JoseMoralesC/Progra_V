using NuevoAvatar.Periodo.Nuevo.Entities;
using NuevoAvatar.Periodo.Nuevo.Repository;
using Microsoft.Data.SqlClient;
using NuevoAvatar.Integracion;

namespace NuevoAvatar.Periodo.Nuevo.Services;

public class PeriodoService : IPeriodoService
{
    private readonly IPeriodoRepository _repository;
    private readonly IPeriodoValidator _validator;
    private readonly DatosRemotos? _datos;

    public PeriodoService(
        IPeriodoRepository repository,
        IPeriodoValidator validator, DatosRemotos? datos = null)
    {
        _repository = repository;
        _validator = validator;
        _datos = datos;
    }

    public async Task<IEnumerable<PeriodoRequest>> ObtenerTodosAsync(short? anio = null, string? estado = null)
    {
        var r=await _repository.ObtenerTodosAsync(); if(anio is not null)r=r.Where(x=>x.Anio==anio); if(!string.IsNullOrWhiteSpace(estado)){var e=estado.ToUpperInvariant();if(e is not("FUTURO" or "ACTIVO" or "CERRADO"))throw new ArgumentException("Estado inválido.");r=r.Where(x=>x.Estado==e);}return r;
    }

    public async Task<PeriodoRequest?> ObtenerPorIdAsync(int periodoId)
    {
        if (periodoId <= 0)
            throw new ArgumentException(
                "El identificador del periodo debe ser mayor que cero.");

        return await _repository.ObtenerPorIdAsync(periodoId);
    }

    public async Task<int> CrearAsync(PeriodoRequest request)
    {
        _validator.Validar(request);
        await ValidarConflictos(request,null); try{return await _repository.CrearAsync(request);}catch(SqlException ex)when(ex.Number is 2601 or 2627){throw new PeriodoConflictoException("Ya existe el año y número de periodo.",ex);}
    }

    public async Task<bool> ModificarAsync(
        int periodoId,
        PeriodoRequest request)
    {
        if (periodoId <= 0)
            throw new ArgumentException(
                "El identificador del periodo debe ser mayor que cero.");

        _validator.Validar(request);
        await ValidarConflictos(request,periodoId);try{return await _repository.ModificarAsync(periodoId,request);}catch(SqlException ex)when(ex.Number is 2601 or 2627){throw new PeriodoConflictoException("Ya existe el año y número de periodo.",ex);}
    }

    public async Task<bool> EliminarAsync(int periodoId)
    {
        if (periodoId <= 0)
            throw new ArgumentException(
                "El identificador del periodo debe ser mayor que cero.");

        if(_datos is null)throw new InvalidOperationException("Integración no configurada.");await _datos.SinReferencias("Periodo","Periodo",periodoId);return await _repository.EliminarAsync(periodoId);
    }
    private async Task ValidarConflictos(PeriodoRequest r,int? excluir){if(await _repository.ExisteDuplicadoAsync(r.Anio,r.NumeroPeriodo,excluir))throw new PeriodoConflictoException("Ya existe el año y número de periodo.");if(await _repository.ExisteSolapamientoAsync(r.Anio,r.FechaInicio,r.FechaFin,excluir))throw new PeriodoConflictoException("El rango se solapa con otro periodo del mismo año.");}
}
public sealed class PeriodoConflictoException(string message,Exception? inner=null):Exception(message,inner);
