FROM eclipse-temurin:17-jdk-jammy
LABEL authors="Marcklen"

WORKDIR /app
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

RUN apt-get update && apt-get install -y netcat --no-install-recommends && rm -rf /var/lib/apt/lists/*
COPY target/*.jar app.jar

ENTRYPOINT ["/wait-for-it.sh", "eureka-server", "8761", "--", "/wait-for-it.sh", "gateway-api", "8080", "--", "java", "-jar", "app.jar"]