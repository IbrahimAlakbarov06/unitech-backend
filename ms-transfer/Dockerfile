FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/ms-transfer-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]