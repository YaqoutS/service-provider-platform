FROM amazoncorretto:17

WORKDIR /app

COPY target/service-provider-platform-0.0.1-SNAPSHOT.jar /app/service-provider-platform.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "service-provider-platform.jar"]