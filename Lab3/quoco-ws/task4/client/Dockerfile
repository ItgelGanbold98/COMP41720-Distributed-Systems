FROM openjdk:8-jdk-alpine
COPY target/client-0.0.1-jar-with-dependencies.jar /client-0.0.1-jar-with-dependencies.jar
CMD sleep 10 && /usr/bin/java -jar /client-0.0.1-jar-with-dependencies.jar $WS_URL