function Cargar-EntornoLocal {
    $urlEnvFile = Join-Path (Split-Path $PSScriptRoot -Parent) '.env'
    if (!(Test-Path -LiteralPath $urlEnvFile)) { return }
    foreach ($urlEnvLine in Get-Content -LiteralPath $urlEnvFile -Encoding UTF8) {
        if ($urlEnvLine -notmatch '^\s*(?:export\s+)?([A-Za-z_][A-Za-z0-9_]*)\s*=\s*(.*?)\s*$') { continue }
        $urlEnvName = $matches[1]; $urlEnvValue = $matches[2]
        if ($null -ne [Environment]::GetEnvironmentVariable($urlEnvName, 'Process')) { continue }
        if ($urlEnvValue.StartsWith('"') -or $urlEnvValue.StartsWith("'")) {
            $urlEnvEnd = $urlEnvValue.IndexOf($urlEnvValue[0], 1)
            $urlEnvValue = if ($urlEnvEnd -gt 0) { $urlEnvValue.Substring(1, $urlEnvEnd - 1) } else { '' }
        } else { $urlEnvValue = ($urlEnvValue -split '#', 2)[0].Trim() }
        [Environment]::SetEnvironmentVariable($urlEnvName, $urlEnvValue, 'Process')
    }
}

function Obtener-OrigenServicio([string]$Servicio, [switch]$Https) {
    $urlEnvName = $Servicio.ToUpperInvariant() + $(if ($Https) { '_HTTPS_BASE_URL' } else { '_BASE_URL' })
    $urlEnvValue = [Environment]::GetEnvironmentVariable($urlEnvName, 'Process')
    [uri]$urlEnvUri = $null
    if ([string]::IsNullOrWhiteSpace($urlEnvValue) -or ![uri]::TryCreate([string]$urlEnvValue, [UriKind]::Absolute, [ref]$urlEnvUri) -or
        $urlEnvUri.Scheme -notin @('http', 'https') -or !$urlEnvUri.Host -or $urlEnvUri.Port -le 0 -or
        $urlEnvUri.UserInfo -or $urlEnvUri.AbsolutePath -ne '/' -or $urlEnvUri.Query -or $urlEnvUri.Fragment) {
        throw "Configura $urlEnvName en el .env raiz con un origen HTTP(S) valido, sin rutas, parametros ni credenciales."
    }
    return $urlEnvUri
}
