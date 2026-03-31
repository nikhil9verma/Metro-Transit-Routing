# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the jar file, skipping tests to speed up deployment
RUN mvn clean package -DskipTests

# Stage 2: Run the application in a lightweight container
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# SECURITY: Expose standard Spring Boot port
EXPOSE 8080

# CRITICAL: Limit memory to ~300MB and use the Serial Garbage Collector
ENTRYPOINT ["java", "-Xmx300m", "-XX:+UseSerialGC", "-jar", "app.jar"]