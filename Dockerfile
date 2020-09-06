FROM openjdk:11.0.5

WORKDIR /code/
COPY ./build/libs/ms-event.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx1500m", "-jar", "ms-event.jar", "--server.port=8080"]
