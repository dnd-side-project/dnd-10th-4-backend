FROM amazoncorretto:17.0.9
ARG JAR_FILE=builds/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
