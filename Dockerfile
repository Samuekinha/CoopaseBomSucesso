# Imagem base do Java 17 (inclui Maven)
FROM eclipse-temurin:17-jdk-jammy

# Diretório de trabalho
WORKDIR /app

# Copia TODOS os arquivos do projeto (incluindo .mvn/wrapper)
COPY . .

# Constrói o JAR usando o Maven Wrapper (Linux)
RUN chmod +x mvnw && ./mvnw clean package

# Copia apenas o JAR para evitar conflitos (opcional)
# RUN cp target/moinho12-0.0.1-SNAPSHOT.jar app.jar

# Porta exposta
EXPOSE 8080

# Comando de execução (usa o JAR já construído)
ENTRYPOINT ["java", "-jar", "target/moinho12-0.0.1-SNAPSHOT.jar"]