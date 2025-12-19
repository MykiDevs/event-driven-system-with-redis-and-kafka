FROM eclipse-temurin:17-jdk-jammy AS builder

LABEL authors="lolfa"

WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew build --no-daemon -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]