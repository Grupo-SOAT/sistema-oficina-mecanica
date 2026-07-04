param (
    [string]$SecretFile = ".\k8s\secret.yaml"
)

Write-Host ""
Write-Host "======================================"
Write-Host "Replacing Kubernetes Secrets..."
Write-Host "======================================"

if (!(Test-Path $SecretFile)) {
    Write-Error "Arquivo $SecretFile não encontrado."
    exit 1
}

$content = Get-Content $SecretFile -Raw

$variables = @(
    "JWT_SECRET",
    "API_KEY_CHATBOT",
    "SPRING_DATASOURCE_PASSWORD",
    "POSTGRES_PASSWORD",
    "DEFAULT_USER_PASSWORD",
    "SPRING_DATASOURCE_USERNAME",
    "POSTGRES_USER"
)

foreach ($variable in $variables) {

    $value = [Environment]::GetEnvironmentVariable($variable)

    if ([string]::IsNullOrWhiteSpace($value)) {
        Write-Error "Secret '$variable' não encontrada."
        exit 1
    }

    $placeholder = '${' + $variable + '}'

    $content = $content.Replace($placeholder, $value)

    Write-Host "$variable OK"
}

Set-Content `
    -Path $SecretFile `
    -Value $content `
    -Encoding UTF8

Write-Host ""
Write-Host "Secrets substituídas com sucesso."