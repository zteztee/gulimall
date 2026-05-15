FROM eclipse-temurin:8-jre
EXPOSE 8080

VOLUME /tmp
ADD target/*.jar  /app.jar

ENTRYPOINT ["java","-jar","-Xms128m","-Xmx300m","/app.jar","--spring.profiles.active=prod"]
