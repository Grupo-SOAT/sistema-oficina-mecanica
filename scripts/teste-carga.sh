#!/bin/bash

set -e

##############################################
# Configurações
##############################################

NAMESPACE="oficina-mecanica"

BASE_URL="http://localhost:8080"

POSTGRES_USER="admin"
POSTGRES_PASSWORD="admin"
POSTGRES_DB="workshop"

##############################################
# Descobrir pod do postgres
##############################################

echo "========================================="
echo "Localizando pod do PostgreSQL..."
echo "========================================="

POSTGRES_POD=$(kubectl get pods -n "$NAMESPACE" \
    --no-headers \
    | awk '/^postgres-/ {print $1}' \
    | head -n 1)

if [ -z "$POSTGRES_POD" ]; then
    echo "ERRO: Pod do postgres não encontrado."
    exit 1
fi

echo "Pod encontrado:"
echo "$POSTGRES_POD"

##############################################
# Inserir usuário admin
##############################################

echo
echo "========================================="
echo "Inserindo usuário ADMIN..."
echo "========================================="

kubectl exec -n "$NAMESPACE" "$POSTGRES_POD" -- \
env PGPASSWORD="$POSTGRES_PASSWORD" \
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" <<EOF
INSERT INTO users (user_name, password, role)
VALUES (
    'admin',
    '\$2y\$10\$ctCsqKe9zwz1AOIQtY0tWOFGGINVPB8Vr/7Jd.UMYaFndnGAxMfvW',
    ARRAY['ADMIN']
)
ON CONFLICT (user_name) DO NOTHING;
EOF

echo "Usuário inserido."

##############################################
# Login
##############################################

echo
echo "========================================="
echo "Realizando login..."
echo "========================================="

LOGIN_RESPONSE=$(curl -s \
    -X POST \
    "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{
        "username":"admin",
        "password":"admin"
    }')

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
    "ownerId":2,
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
echo "Criando 100 Service Orders..."
echo "========================================="

for i in $(seq 1 100)
do
(
    curl -s \
    -X POST \
    "$BASE_URL/service-orders" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "clientId":2,
        "vehicleId":2,
        "description":"Teste de carga"
    }' > /dev/null
) &
done

wait

echo
echo "========================================="
echo "Teste concluído!"
echo "========================================="