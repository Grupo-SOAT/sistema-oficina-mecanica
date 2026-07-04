Write-Host ""
Write-Host "Waiting for Kafka deployment..."

kubectl wait `
    --namespace oficina-mecanica `
    --for=condition=Ready `
    pod `
    -l app=kafka `
    --timeout=300s

Write-Host "Kafka is Ready."