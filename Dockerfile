FROM openjdk:17 as build
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ARG DB_URL
ENV DB=${DB_URL}
ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-DprodDbUrl=${DB}", "-jar","/app.jar"]