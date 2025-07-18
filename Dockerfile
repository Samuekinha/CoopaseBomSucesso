FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# 1. Copia APENAS o necessário para o build (otimizado)
COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .

# 2. Build com tratamento de erros
RUN chmod +x mvnw && \
    ./mvnw clean package -X && \
    ls -la target/ && \
    mv target/*.jar app.jar

# 3. Configuração final
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]