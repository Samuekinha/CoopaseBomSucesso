FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copia APENAS o JAR (já buildado localmente)
COPY target/moinho-0.0.1-SNAPSHOT.jar app.jar

# Configurações essenciais
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]