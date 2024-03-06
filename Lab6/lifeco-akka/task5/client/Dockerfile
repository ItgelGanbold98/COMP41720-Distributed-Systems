FROM openjdk

ENV HOSTNAME=localhost

ENV BROKER=broker

COPY target/client-0.0.1-allinone.jar /client-0.0.1-allinone.jar
CMD java -jar /client-0.0.1-allinone.jar $HOSTNAME $BROKER