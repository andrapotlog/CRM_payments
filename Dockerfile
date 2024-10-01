# Stage 1: Build the application
#FROM maven:3.8.5-openjdk-17 AS builder
#WORKDIR /app
#COPY . .
#RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/payments-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]