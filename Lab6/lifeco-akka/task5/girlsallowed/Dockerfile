FROM openjdk

# args[0]
ENV HOSTNAME=localhost

# args[1]
ENV BROKER=broker

COPY target/girlsallowed-0.0.1-allinone.jar /girlsallowed-0.0.1-allinone.jar
CMD java -jar /girlsallowed-0.0.1-allinone.jar $HOSTNAME $BROKER