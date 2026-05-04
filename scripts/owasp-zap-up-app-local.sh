#!/bin/bash
set -e

echo "🔧 Configurando variáveis..."

export DEFAULT_USER_PASSWORD="dummypassword"

APP_URL="http://app:8080"

echo "⏱️ Aguardando aplicação subir..."

for i in {1..30}; do
  if curl -s $APP_URL/actuator/health | grep "UP"; then
    echo "✅ Aplicação está UP!"
    break
  fi
  sleep 5
done

echo "🔐 Login ADMIN..."

ADMIN_RESPONSE=$(curl -s -X POST $APP_URL/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "dummyAdmin",
    "password": "dummyAdminPassword"
  }')

echo $ADMIN_RESPONSE

ADMIN_TOKEN=$(echo $ADMIN_RESPONSE | jq -r '.token')

if [ -z "$ADMIN_TOKEN" ] || [ "$ADMIN_TOKEN" == "null" ]; then
  echo "❌ Falha ao obter token ADMIN"
  exit 1
fi

echo "✅ Token ADMIN obtido"

ROLES=("CHATBOT" "MECHANIC" "ATTENDANT" "STOREKEEPER")

for ROLE in "${ROLES[@]}"; do
  USERNAME="user_${ROLE,,}"

  echo "👤 Criando usuário $USERNAME"

  curl -s -X POST $APP_URL/users \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
      \"username\": \"$USERNAME\",
      \"roles\": [\"$ROLE\"]
    }" > /dev/null

  USER_RESPONSE=$(curl -s -X POST $APP_URL/auth/login \
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

  ENV_NAME="ZAP_TOKEN_${ROLE}"
  echo "$ENV_NAME=$USER_TOKEN" >> /app/env-vars

  echo "✅ Token $ROLE gerado"
done

echo "ZAP_TOKEN_ADMIN=$ADMIN_TOKEN" >> /app/env-vars

echo "🎯 Tokens gerados com sucesso"