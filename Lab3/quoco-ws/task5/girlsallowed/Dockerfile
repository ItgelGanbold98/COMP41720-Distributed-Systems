FROM openjdk:8-jdk-alpine
COPY target/girlsallowed-0.0.1-jar-with-dependencies.jar /girlsallowed-0.0.1-jar-with-dependencies.jar
ENV CONTAINER_NAME girlsallowed
CMD sleep 4 && /usr/bin/java -jar /girlsallowed-0.0.1-jar-with-dependencies.jar ${CONTAINER_NAME}