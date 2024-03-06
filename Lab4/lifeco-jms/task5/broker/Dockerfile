FROM openjdk
ENV SERVER_MOM activemq
COPY target/broker-0.0.1-jar-with-dependencies.jar /broker-0.0.1-jar-with-dependencies.jar
CMD sleep 1 && /usr/bin/java -jar /broker-0.0.1-jar-with-dependencies.jar $SERVER_MOM