FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY target/Find-A-Dice-Thymeleaf-0.10.0.jar /app/Find-A-Dice-Thymeleaf-0.10.0.jar
COPY src/main/resources/aiven-truststore.jks /app/aiven-truststore.jks
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "Find-A-Dice-Thymeleaf-0.10.0.jar"]