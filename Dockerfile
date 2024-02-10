FROM openjdk:17
ARG JAR_FILE=build/libs/myOcean-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dfile.encoding=UTF-8", "-jar","/app.jar"]
