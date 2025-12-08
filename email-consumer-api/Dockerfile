FROM eclipse-temurin:17-jdk-jammy
LABEL authors="Marcklen"

WORKDIR /app

RUN sed -i 's|http://archive.ubuntu.com|http://mirror.ufscar.br|g' /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y netcat --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]