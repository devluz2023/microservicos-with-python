FROM openjdk:17-alpine

# Install necessary packages
RUN apk update && apk add \
    net-tools \
    iputils \ 
    wget 
VOLUME /tmp
ARG JAR_FILE=target/gateway-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
