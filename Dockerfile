FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Configura locale e encoding
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8

COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .

# Adicione encoding UTF-8 ao Maven
RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests -Dfile.encoding=UTF-8 && \
    ls -la target/ && \
    mv target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]