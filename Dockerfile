FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# 1. Copia APENAS o necessário para o build (otimizado)
COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .

# 2. Build otimizado com tratamento de erros
RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests && \  # Pula testes no build da imagem
    ls -la target/ && \
    mv target/*.jar app.jar && \
    rm -rf ~/.m2  # Limpa cache do Maven para reduzir tamanho da imagem

# 3. Configuração final otimizada
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]