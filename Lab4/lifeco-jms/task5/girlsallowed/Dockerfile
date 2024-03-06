FROM openjdk
ENV SERVER_MOM activemq
COPY target/girlsallowed-0.0.1-jar-with-dependencies.jar /girlsallowed-0.0.1-jar-with-dependencies.jar
CMD sleep 1 && /usr/bin/java -jar /girlsallowed-0.0.1-jar-with-dependencies.jar $SERVER_MOM