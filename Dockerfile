FROM gradle:6.7.1-jre15

RUN mkdir -p /home/gradle/src
COPY . /home/gradle/src
USER root
RUN chown -R gradle:gradle /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon 

FROM openjdk:15.0.1

EXPOSE 8080

RUN mkdir /app

COPY /build/libs/ms-event.jar /app/ms-event.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/ms-event.jar", "--server.port=8080"]
