# Gradle Cache Stage
FROM eclipse-temurin:21-jdk-jammy AS cache
RUN mkdir -p /app/.gradle_cache
ENV GRADLE_USER_HOME /app/.gradle_cache

WORKDIR /app
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x ./gradlew && ./gradlew -g /app/.gradle_cache clean build -i --stacktrace -x test -x integrationTest

# Build Stage
FROM eclipse-temurin:21-jdk-jammy AS builder
RUN mkdir -p /app/.gradle_cache
COPY --from=cache /app/.gradle_cache /app/.gradle_cache

WORKDIR /app
COPY gradle gradle
COPY gradlew build.gradle settings.gradle lombok.config ./
COPY src src

RUN chmod +x ./gradlew && ./gradlew -g /app/.gradle_cache bootJar -x test -x integrationTest -i --stacktrace

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENV JAVA_OPTS="-Xmx512m -Xms256m"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]