FROM openjdk

ENV HOSTNAME=localhost

ENV BROKER=broker

COPY target/auldfellas-0.0.1-allinone.jar /auldfellas-0.0.1-allinone.jar
CMD java -jar /auldfellas-0.0.1-allinone.jar $HOSTNAME $BROKER