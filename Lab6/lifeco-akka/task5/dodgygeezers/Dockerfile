FROM openjdk

ENV HOSTNAME=localhost

ENV BROKER=broker
COPY target/dodgygeezers-0.0.1-allinone.jar /dodgygeezers-0.0.1-allinone.jar
CMD java -jar /dodgygeezers-0.0.1-allinone.jar $HOSTNAME $BROKER