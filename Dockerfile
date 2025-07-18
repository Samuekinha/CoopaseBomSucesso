FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# 1. Copia tudo necessário para o build
COPY . .

# 2. Constrói o projeto (garanta que o nome do JAR está correto)
RUN chmod +x mvnw && \
    ./mvnw clean package && \
    mv target/*.jar app.jar

# 3. Configuração final
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]