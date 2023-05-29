FROM --platform=linux/amd64 eclipse-temurin:17.0.1_12-jdk-alpine AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
COPY .mvn /usr/src/app/.mvn
COPY mvnw /usr/src/app/
RUN chmod +x /usr/src/app/mvnw
RUN cd /usr/src/app && ./mvnw -f pom.xml clean package

FROM --platform=linux/amd64 eclipse-temurin:17.0.1_12-jdk-alpine
COPY --from=build /usr/src/app/target/smarthome.jar /opt/smarthome/
#COPY docker/application-docker.properties /opt/smarthome/application.properties
WORKDIR /opt/smarthome
ENTRYPOINT ["java", "-jar", "/opt/smarthome/smarthome.jar"]
EXPOSE 8081