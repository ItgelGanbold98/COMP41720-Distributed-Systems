FROM openjdk:8-jdk-alpine
COPY target/dodgygeezers-0.0.1-jar-with-dependencies.jar /dodgygeezers-0.0.1-jar-with-dependencies.jar
ENV CONTAINER_NAME dodgygeezers
CMD sleep 3 && /usr/bin/java -jar /dodgygeezers-0.0.1-jar-with-dependencies.jar ${CONTAINER_NAME}