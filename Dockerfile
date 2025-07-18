FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Configura locale e encoding
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Copia os arquivos do Maven Wrapper primeiro
COPY .mvn ./.mvn
COPY mvnw .
COPY pom.xml .

# Torna o mvnw executável
RUN chmod +x mvnw

# Baixa as dependências primeiro (para cache)
RUN ./mvnw dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila o projeto
RUN ./mvnw clean package -DskipTests -Dfile.encoding=UTF-8

# Verifica se o JAR foi criado
RUN ls -la target/

# Move o JAR para a raiz (mais seguro)
RUN cp target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]