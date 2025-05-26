FROM eclipse-temurin:17-jdk-jammy
LABEL authors="Marcklen"
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]