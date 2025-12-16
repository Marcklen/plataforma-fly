FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

ENV JAVA_OPTS="-XX:+UseContainerSupport \
 -XX:MaxRAMPercentage=70 \
 -XX:+UseG1GC \
 -XX:+ExitOnOutOfMemoryError \
 -Djava.security.egd=file:/dev/./urandom"

COPY target/*.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]