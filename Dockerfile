#Dockerfile teste para o CI - pode sobrescrever esse arquivo se quiser
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# copia o jar gerado pelo maven
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]