# Use the official maven/Java 17 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /app/src/
RUN mvn package -DskipTests

# Use adoptopenjdk/openjdk17 image as a base image for the runtime.
# https://hub.docker.com/r/adoptopenjdk/openjdk17
FROM adoptopenjdk/openjdk17:alpine-jre

# Set the current working directory inside the container.
WORKDIR /app

# Copy the generated jar file from the builder stage to the current location.
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar .

# Expose port 8080 for the container to listen on.
EXPOSE 8080

# Start the application.
CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]

