#!/bin/bash

set -e

##############################################
# Configurações
##############################################

NAMESPACE="oficina-mecanica"

BASE_URL="http://localhost:8080"

echo
echo "========================================="
echo "Realizando login..."
echo "========================================="

LOGIN_RESPONSE=$(curl -X 'POST' \
  "$BASE_URL/auth/chatbot" \
  -H 'accept: application/json' \
  -H 'X-API-Key: teste' \
  -d '')

echo "$LOGIN_RESPONSE"

##############################################
# Extrair Token
##############################################

TOKEN=$(echo "$LOGIN_RESPONSE" | sed -n 's/.*"token":"\([^"]*\)".*/\1/p')

if [ -z "$TOKEN" ]; then
    echo
    echo "ERRO: Não foi possível obter o token."
    exit 1
fi

echo
echo "Token obtido com sucesso."

##############################################
# Criar Owner
##############################################

echo
echo "========================================="
echo "Criando Owner..."
echo "========================================="

curl -s \
-X POST \
"$BASE_URL/owners" \
-H "accept: application/json" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
    "name":"string",
    "document":"02763525059",
    "documentType":"CPF",
    "phone":"string",
    "email":"string@email"
}'

echo
echo "Owner criado."

##############################################
# Criar Vehicle
##############################################

echo
echo "========================================="
echo "Criando Vehicle..."
echo "========================================="

curl -s \
-X POST \
"$BASE_URL/vehicles" \
-H "accept: application/json" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
    "licensePlate":"KFG6947",
    "ownerId":1,
    "brand":"string",
    "model":"string",
    "year":2000,
    "color":"string"
}'

echo
echo "Vehicle criado."

##############################################
# Criar 100 Ordens de Serviço
##############################################

echo
echo "========================================="
echo "Criando 3000 Service Orders..."
echo "========================================="

for i in $(seq 1 3000)
do
(
    curl -s \
    -X POST \
    "$BASE_URL/service-orders" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "clientId":1,
        "vehicleId":1,
        "description":"Teste de carga"
    }' > /dev/null
) &
done

wait

echo
echo "========================================="
echo "Teste concluído!"
echo "========================================="