import javax.xml.ws.Endpoint;

import service.broker.LocalBrokerService;


public class Main {
    public static void main(String[] args) { Endpoint.publish("http://0.0.0.0:9000/broker", new LocalBrokerService());
        System.out.println("Broker Service is running at: http://0.0.0.0:9000/broker");
    }
}
