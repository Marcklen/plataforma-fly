FROM eclipse-temurin:17-jdk-jammy
LABEL authors="Marcklen"

WORKDIR /app

COPY target/*.jar app.jar
COPY wait-for-it.sh .
RUN chmod +x ./wait-for-it.sh

RUN apt-get update && apt-get install -y netcat --no-install-recommends && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["java", "-jar", "app.jar"]