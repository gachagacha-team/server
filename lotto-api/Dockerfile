FROM openjdk:23

ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} lotto-api.jar

ENTRYPOINT ["java","-jar","lotto-api.jar"]
