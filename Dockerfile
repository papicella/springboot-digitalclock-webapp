# Build the JAR locally first: mvn clean package
FROM eclipse-temurin:17-jre-alpine

RUN addgroup -g 1001 -S app \
    && adduser -u 1001 -S -G app -h /home/app app

WORKDIR /app

COPY target/digitalclock-1.0.0.jar app.jar

RUN chown app:app app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
