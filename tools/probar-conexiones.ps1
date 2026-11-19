param(
    [string]$Usuario = "jose@cuc.cr",
    [string]$Password = "password"
)

$ErrorActionPreference = 'Stop'

Write-Host "=== 1. Probando Autenticacion (login) ===" -ForegroundColor Cyan
try {
    $login = Invoke-RestMethod -Method Post -Uri "http://localhost:5158/login" -Headers @{ "usuario" = $Usuario; "contrasena" = $Password } -TimeoutSec 10
    $token = $login.access_token
    Write-Host "Login exitoso. Token recibido (longitud $($token.Length))." -ForegroundColor Green
} catch {
    Write-Host "Error en login: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

$headers = @{ "Authorization" = "Bearer $token" }
$headersJson = @{ "Authorization" = "Bearer $token"; "Content-Type" = "application/json" }

$tests = @(
    [PSCustomObject]@{ Persona=1; Servicio="Autenticacion"; Metodo="GET"; Url="http://localhost:5158/validate"; Body=$null },
    [PSCustomObject]@{ Persona=1; Servicio="Bitacora";      Metodo="GET"; Url="http://localhost:5159/bitacora"; Body=$null },
    [PSCustomObject]@{ Persona=1; Servicio="Rol";           Metodo="GET"; Url="http://localhost:5160/rol"; Body=$null },
    [PSCustomObject]@{ Persona=1; Servicio="Parametro";     Metodo="GET"; Url="http://localhost:5161/parametro"; Body=$null },
    [PSCustomObject]@{ Persona=1; Servicio="Modulo";        Metodo="GET"; Url="http://localhost:5162/modulo"; Body=$null },
    [PSCustomObject]@{ Persona=2; Servicio="Usuario";       Metodo="GET"; Url="http://localhost:8082/usuario"; Body=$null },
    [PSCustomObject]@{ Persona=2; Servicio="Institucion";   Metodo="GET"; Url="http://localhost:8086/institucion"; Body=$null },
    [PSCustomObject]@{ Persona=2; Servicio="Profesor";      Metodo="GET"; Url="http://localhost:8087/profesor"; Body=$null },
    [PSCustomObject]@{ Persona=2; Servicio="Carrera";       Metodo="GET"; Url="http://localhost:8088/carrera"; Body=$null },
    [PSCustomObject]@{ Persona=3; Servicio="Curso";         Metodo="GET"; Url="http://localhost:5255/curso"; Body=$null },
    [PSCustomObject]@{ Persona=3; Servicio="Grupo";         Metodo="GET"; Url="http://localhost:5256/grupo"; Body=$null },
    [PSCustomObject]@{ Persona=3; Servicio="Periodo";       Metodo="GET"; Url="http://localhost:5262/periodo"; Body=$null },
    [PSCustomObject]@{ Persona=3; Servicio="Direccion";     Metodo="GET"; Url="http://localhost:5227/provincias"; Body=$null },
    [PSCustomObject]@{ Persona=4; Servicio="Expediente";    Metodo="GET"; Url="http://localhost:8084/expediente"; Body=$null },
    [PSCustomObject]@{ Persona=4; Servicio="Prematricula";  Metodo="GET"; Url="http://localhost:8089/prematricula"; Body=$null },
    [PSCustomObject]@{ Persona=4; Servicio="HistorialAcademico"; Metodo="GET"; Url="http://localhost:8090/historialacademico?tipoIdentificacion=LOCAL&identificacion=P5-JOSE-ESTUDIANTE-M"; Body=$null },
    [PSCustomObject]@{ Persona=4; Servicio="ListadoEstudiantes"; Metodo="GET"; Url="http://localhost:8091/listadoestudiantes?periodoId=1"; Body=$null },
    [PSCustomObject]@{ Persona=5; Servicio="Matricula";     Metodo="GET"; Url="http://localhost:8085/matricula?cursoId=1&grupoId=1"; Body=$null },
    [PSCustomObject]@{ Persona=5; Servicio="Notas";         Metodo="GET"; Url="http://localhost:8092/obtenerdesglose?grupoId=1"; Body=$null },
    [PSCustomObject]@{ Persona=5; Servicio="Factura";       Metodo="GET"; Url="http://localhost:8093/factura?periodoId=1"; Body=$null },
    [PSCustomObject]@{ Persona=5; Servicio="Pago";          Metodo="GET"; Url="http://localhost:8094/pago?periodoId=1"; Body=$null },
    [PSCustomObject]@{ Persona=5; Servicio="Notificacion";  Metodo="POST"; Url="http://localhost:8095/notificar"; Body=(@{email="jose@cuc.cr"; asunto="Prueba conexion"; cuerpoHtml="<p>OK</p>"} | ConvertTo-Json) }
)

Write-Host "`n=== 2. Verificando conectividad HTTP de los microservicios ===" -ForegroundColor Cyan
$resultados = foreach ($t in $tests) {
    try {
        if ($t.Metodo -eq "POST") {
            $resp = Invoke-RestMethod -Uri $t.Url -Headers $headersJson -Method Post -Body $t.Body -TimeoutSec 6
        } else {
            $resp = Invoke-RestMethod -Uri $t.Url -Headers $headers -Method Get -TimeoutSec 6
        }
        [PSCustomObject]@{
            Persona = $t.Persona
            Servicio = $t.Servicio
            Puerto = ([Uri]$t.Url).Port
            Metodo = $t.Metodo
            EstadoHTTP = "200 OK"
            Conectividad = "CONECTADO"
        }
    } catch {
        $code = if ($_.Exception.Response) { [int]$_.Exception.Response.StatusCode } else { "ERR" }
        [PSCustomObject]@{
            Persona = $t.Persona
            Servicio = $t.Servicio
            Puerto = ([Uri]$t.Url).Port
            Metodo = $t.Metodo
            EstadoHTTP = "$code"
            Conectividad = "FALLO: $($_.Exception.Message)"
        }
    }
}

$resultados | Format-Table -AutoSize
