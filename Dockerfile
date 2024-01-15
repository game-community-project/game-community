FROM openjdk:17 as build
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY ./src/main/resources/application.yml application.yml
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=file:/application.yml"]
