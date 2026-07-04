param (

    [string]$TerraformDirectory = ".\infra",

    [string]$BackupDirectory = "C:\terraform-state\sistema-oficina"

)

Write-Host ""
Write-Host "======================================="
Write-Host "Backing Up Terraform State..."
Write-Host "======================================="

if (!(Test-Path $BackupDirectory)) {

    New-Item `
        -ItemType Directory `
        -Path $BackupDirectory `
        -Force | Out-Null

}

$source = Join-Path $TerraformDirectory "terraform.tfstate"
$destination = Join-Path $BackupDirectory "terraform.tfstate"

if (!(Test-Path $source)) {

    Write-Error "terraform.tfstate não encontrado."

    exit 1

}

Copy-Item `
    $source `
    $destination `
    -Force

Write-Host "Terraform State atualizado."