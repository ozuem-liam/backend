#
# Build stage
#
FROM maven:3.9.1-jdk-11 AS build
COPY . .
RUN apt-get update && apt-get install -y git \
    && mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]
