FROM openjdk:23

ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} gachagacha.jar

ENTRYPOINT ["java","-jar","gachagacha.jar"]
