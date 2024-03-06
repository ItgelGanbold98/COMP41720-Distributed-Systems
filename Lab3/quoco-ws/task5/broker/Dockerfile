FROM openjdk:8-jdk-alpine
COPY target/broker-0.0.1-jar-with-dependencies.jar /broker-0.0.1-jar-with-dependencies.jar
EXPOSE 9000
ENV CONTAINER_NAME broker
CMD /usr/bin/java -jar /broker-0.0.1-jar-with-dependencies.jar ${CONTAINER_NAME}