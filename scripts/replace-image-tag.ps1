param(

    [Parameter(Mandatory = $true)]
    [string]$Manifest,

    [Parameter(Mandatory = $true)]
    [string]$Registry,

    [Parameter(Mandatory = $true)]
    [string]$Image,

    [Parameter(Mandatory = $true)]
    [string]$Tag

)

Write-Host ""
Write-Host "======================================="
Write-Host "Updating Deployment Image Tag..."
Write-Host "======================================="

if (!(Test-Path $Manifest)) {

    Write-Error "Manifesto não encontrado: $Manifest"

    exit 1

}

$content = Get-Content $Manifest -Raw

$imagePattern = "image:\s*$Registry/$Image:[^\r\n]+"

$newImage = "image: $Registry/$Image:$Tag"

if ($content -notmatch $imagePattern) {

    Write-Error "Imagem '$Registry/$Image' não encontrada no manifesto."

    exit 1

}

$content = $content -replace $imagePattern, $newImage

Set-Content `
    -Path $Manifest `
    -Value $content `
    -Encoding UTF8

Write-Host ""
Write-Host "Imagem atualizada:"
Write-Host $newImage
Write-Host ""
Write-Host "Manifesto atualizado com sucesso."