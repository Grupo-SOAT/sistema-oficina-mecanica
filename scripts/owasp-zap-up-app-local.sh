#!/bin/bash
set -e

echo "🔧 Configurando variáveis de ambiente..."

export DEFAULT_USER_PASSWORD="dummypassword"
export JWT_SECRET="12345678901234567890123456789012"
export API_KEY_CHATBOT="dummy-api-key"

export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/workshop"
export SPRING_DATASOURCE_USERNAME="admin"
export SPRING_DATASOURCE_PASSWORD="admin"

export SPRING_PROFILES_ACTIVE=ci

echo "⏱️ Aguardando PostgreSQL subir..."

for i in {1..20}; do
  if pg_isready -h localhost -p 5432 -U admin; then
    echo "✅ PostgreSQL está pronto!"
    break
  fi
  sleep 3
done

echo "🚀 Subindo aplicação..."
echo "Active profile: $SPRING_PROFILES_ACTIVE"
nohup java -jar target/*.jar > app.log 2>&1 &

echo "⏱️ Aguardando aplicação subir..."

for i in {1..30}; do
  if curl -s http://localhost:8080/actuator/health | grep "UP"; then
    echo "✅ Aplicação está UP!"
    break
  fi
  sleep 5
done

echo "🔐 Login ADMIN..."

ADMIN_RESPONSE=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "dummyAdmin",
    "password": "dummyAdminPassword"
  }')

echo "ADMIN RESPONSE:"
echo $ADMIN_RESPONSE

ADMIN_TOKEN=$(echo $ADMIN_RESPONSE | jq -r '.token')

if [ -z "$ADMIN_TOKEN" ] || [ "$ADMIN_TOKEN" == "null" ]; then
  echo "❌ Falha ao obter token ADMIN"
  exit 1
fi

echo "✅ Token ADMIN obtido"

# =========================
# 👤 Criar usuários por role
# =========================

ROLES=("CHATBOT" "MECHANIC" "ATTENDANT" "STOREKEEPER")

for ROLE in "${ROLES[@]}"; do
  USERNAME="user_${ROLE,,}"

  echo "👤 Criando usuário $USERNAME com role $ROLE"

  curl -s -X POST http://localhost:8080/users \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
      \"username\": \"$USERNAME\",
      \"roles\": [\"$ROLE\"]
    }" > /dev/null

  echo "🔐 Login usuário $USERNAME"

  USER_RESPONSE=$(curl -s -X POST http://localhost:8080/auth/login \
    -H "Content-Type: application/json" \
    -d "{
      \"username\": \"$USERNAME\",
      \"password\": \"$DEFAULT_USER_PASSWORD\"
    }")

  USER_TOKEN=$(echo $USER_RESPONSE | jq -r '.token')

  if [ -z "$USER_TOKEN" ] || [ "$USER_TOKEN" == "null" ]; then
    echo "❌ Falha ao obter token para $USERNAME"
    exit 1
  fi

  echo "✅ Token gerado para $ROLE"

  ENV_NAME="ZAP_TOKEN_${ROLE}"
  echo "$ENV_NAME=$USER_TOKEN" >> $GITHUB_ENV
done

# export admin também
echo "ZAP_TOKEN_ADMIN=$ADMIN_TOKEN" >> $GITHUB_ENV

echo "🎯 Todos os tokens gerados com sucesso"