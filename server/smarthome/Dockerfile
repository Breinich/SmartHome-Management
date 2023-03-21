FROM gradle:7.6.1-jdk17-alpine AS build
WORKDIR /home
COPY --chown=gradle:gradle . .
RUN ./gradlew build --refresh-dependencies

FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /home/build/libs/*.jar application.jar

ENTRYPOINT ["java", "/app/application.jar"]