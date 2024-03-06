FROM openjdk
ENV SERVER_MOM activemq
COPY target/dodgygeezers-0.0.1-jar-with-dependencies.jar /dodgygeezers-0.0.1-jar-with-dependencies.jar
CMD sleep 1 && /usr/bin/java -jar /dodgygeezers-0.0.1-jar-with-dependencies.jar $SERVER_MOM