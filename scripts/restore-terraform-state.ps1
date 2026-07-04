param (

    [string]$BackupDirectory = "C:\terraform-state\sistema-oficina",

    [string]$TerraformDirectory = ".\infra"

)

Write-Host ""
Write-Host "======================================="
Write-Host "Restoring Terraform State..."
Write-Host "======================================="

$backupFile = Join-Path $BackupDirectory "terraform.tfstate"
$destination = Join-Path $TerraformDirectory "terraform.tfstate"

if (Test-Path $backupFile) {

    Copy-Item `
        $backupFile `
        $destination `
        -Force

    Write-Host "Terraform State restaurado."

}
else {

    Write-Host "Nenhum Terraform State encontrado."
    Write-Host "Primeira execução da infraestrutura."

}