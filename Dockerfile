FROM openjdk:17-jdk-slim

COPY /target/*.jar saga-order-service-app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "saga-order-service-app.jar"]