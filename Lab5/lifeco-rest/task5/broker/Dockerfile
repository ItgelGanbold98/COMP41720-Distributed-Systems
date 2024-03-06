FROM openjdk
ENV SERVER localhost
COPY target/broker-0.0.1.jar /broker-0.0.1.jar
CMD sleep 1 && /usr/bin/java -jar /broker-0.0.1.jar $SERVER