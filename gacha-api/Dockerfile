FROM openjdk:23

ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} gacha-api.jar

ENTRYPOINT ["java","-jar","gacha-api.jar"]
