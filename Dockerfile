FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# 1. Copia os arquivos de build primeiro
COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .

# 2. Roda o Maven PARA GERAR O JAR
RUN chmod +x mvnw && ./mvnw clean package

# 3. Copia APENAS o JAR gerado
COPY target/moinho-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
