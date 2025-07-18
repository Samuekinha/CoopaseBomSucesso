FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copia apenas o necessário para o build
COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .

# Executa com debug para ver o erro real
RUN chmod +x mvnw && \
    ./mvnw clean package -X && \  # -X ativa o debug completo
    ls -la target/ && \           # Lista o conteúdo da pasta target
    mv target/*.jar app.jar
