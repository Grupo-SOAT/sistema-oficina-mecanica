Write-Host ""
Write-Host "======================================="
Write-Host "Creating Kafka Topics..."
Write-Host "======================================="

kubectl exec deployment/kafka `
    -n oficina-mecanica `
    -- kafka-topics `
    --bootstrap-server kafka-service:29092 `
    --create `
    --if-not-exists `
    --topic budget-approval-request `
    --partitions 1 `
    --replication-factor 1

kubectl exec deployment/kafka `
    -n oficina-mecanica `
    -- kafka-topics `
    --bootstrap-server kafka-service:29092 `
    --create `
    --if-not-exists `
    --topic budget-decision `
    --partitions 1 `
    --replication-factor 1

Write-Host ""
Write-Host "Kafka Topics created successfully."