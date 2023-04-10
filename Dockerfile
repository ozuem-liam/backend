FROM openjdk:11-jdk-slim
RUN apt-get update && apt-get install -y maven
ENV PATH="/usr/share/maven/bin:${PATH}"
COPY . .
RUN mvn clean package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]
