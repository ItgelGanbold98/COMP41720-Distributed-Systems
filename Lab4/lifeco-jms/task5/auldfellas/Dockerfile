FROM openjdk
ENV SERVER_MOM activemq
COPY target/auldfellas-0.0.1-jar-with-dependencies.jar /auldfellas-0.0.1-jar-with-dependencies.jar
CMD sleep 1 && /usr/bin/java -jar /auldfellas-0.0.1-jar-with-dependencies.jar $SERVER_MOM