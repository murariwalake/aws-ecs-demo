# Use the official OpenJDK 17 base image
FROM eclipse-temurin:17-jdk-alpine

ADD build/libs/product-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 80

ENTRYPOINT ["java","-jar","/app.jar"]
