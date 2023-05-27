FROM eclipse-temurin:17.0.1_12-jre-alpine AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
COPY .mvn /usr/src/app/.mvn
COPY mvnw /usr/src/app/
RUN cd /usr/src/app && ./mvnw -f pom.xml clean package

FROM eclipse-temurin:17.0.1_12-jre-alpine
MAINTAINER gerviba@sch.bme.hu
COPY --from=build /usr/src/app/target/smarthome.jar /opt/smarthome/
#COPY docker/application-docker.properties /opt/smarthome/application.properties
WORKDIR /opt/smarthome
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=98", "-jar", "/opt/smarthome/schpincer.jar"]
EXPOSE 80