FROM openjdk:17-jdk
VOLUME /tmp
ARG JAR_FILE=target/ads-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ads-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ads-0.0.1-SNAPSHOT.jar"]