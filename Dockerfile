FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

ENTRYPOINT ["./mvnw", "spring-boot:run"]