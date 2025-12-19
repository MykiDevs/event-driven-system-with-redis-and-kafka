FROM eclipse-temurin:17-jdk-jammy AS builder
LABEL authors="lolfa"

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre-jammy
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]