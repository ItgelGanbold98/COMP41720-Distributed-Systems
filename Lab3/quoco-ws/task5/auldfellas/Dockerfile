FROM openjdk:8-jdk-alpine
ENV CONTAINER_NAME auldfellas
COPY target/auldfellas-0.0.1-jar-with-dependencies.jar /auldfellas-0.0.1-jar-with-dependencies.jar
CMD sleep 2 && /usr/bin/java -jar /auldfellas-0.0.1-jar-with-dependencies.jar ${CONTAINER_NAME}