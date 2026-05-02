#!/bin/bash

set -e

echo "🔧 Configurando variáveis de ambiente..."

export DEFAULT_USER_PASSWORD="dummypassword"
export JWT_SECRET="12345678901234567890123456789012"
export API_KEY_CHATBOT="dummy-api-key"

# CONFIG DO POSTGRES
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/workshop"
export SPRING_DATASOURCE_USERNAME="admin"
export SPRING_DATASOURCE_PASSWORD="admin"

echo "⏱️ Aguardando PostgreSQL subir..."

# postgre client instalado no proprio workflow gh actions
for i in {1..20}; do
  if pg_isready -h localhost -p 5432 -U admin; then
    echo "✅ PostgreSQL está pronto!"
    break
  fi
  sleep 3
done

echo "🚀 Subindo aplicação..."
nohup java -jar target/*.jar > app.log 2>&1 &

APP_PID=$!

echo "⏱️ Aguardando aplicação subir..."

for i in {1..30}; do
  if curl -s http://localhost:8080/actuator/health | grep "UP"; then
    echo "✅ Aplicação está UP!"
    break
  fi
  sleep 5
done

if ! curl -s http://localhost:8080/actuator/health | grep "UP"; then
  echo "❌ Aplicação não subiu"
  cat app.log
  exit 1
fi

echo "🔐 Obtendo token JWT..."

RESPONSE=$(curl -s -X POST http://localhost:8080/auth/chatbot \
  -H "X-API-Key: $API_KEY_CHATBOT" \
  -H "Content-Type: application/json")

TOKEN=$(echo $RESPONSE | jq -r '.token')

if [ "$TOKEN" == "null" ] || [ -z "$TOKEN" ]; then
  echo "❌ Falha ao obter token"
  exit 1
fi

echo "✅ Token obtido"

echo "ZAP_AUTH_TOKEN=$TOKEN" >> $GITHUB_ENV