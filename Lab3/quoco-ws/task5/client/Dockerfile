FROM openjdk:8-jdk-alpine
COPY target/client-0.0.1-jar-with-dependencies.jar /client-0.0.1-jar-with-dependencies.jar
ENV CONTAINER_NAME broker
CMD sleep 20 && /usr/bin/java -jar /client-0.0.1-jar-with-dependencies.jar ${CONTAINER_NAME}