FROM openjdk:8-jdk-alpine
COPY ./target/school-0.0.1-SNAPSHOT.jar /usr/app
WORKDIR /usr/app
ENTRYPOINT ["java", "-jar", "/school-0.0.1-SNAPSHOT.jar"]