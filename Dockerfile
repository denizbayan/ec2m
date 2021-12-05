FROM openjdk:11-jre-slim-buster
MAINTAINER deniz.bayan
COPY target/ec2m-0.0.1-SNAPSHOT.jar ec2m.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar","/ec2m.jar"]