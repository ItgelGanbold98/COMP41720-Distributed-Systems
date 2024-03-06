import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.broker.LocalBrokerService;

import service.core.BrokerService;
import service.core.Constants;

public class Main {
    public static void main(String[] args) {
        try {
            // Connect to the RMI Registry - creating the registry will be the responsibility of the broker.
            Registry registry = null;

            if (args.length == 0) {
                registry = LocateRegistry.createRegistry(1099);
            } else {
                registry = LocateRegistry.getRegistry(args[0], 1099);
            }

            BrokerService bqService = new LocalBrokerService(registry);

            // Create the Remote Object
            BrokerService brService = (BrokerService) UnicastRemoteObject.exportObject(bqService,0);

            // Register the object with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE, brService);

            System.out.println("(Broker) STOPPING SERVER SHUTDOWN");

            while (true) {Thread.sleep(1000); }

        } catch (Exception e) {
            System.out.println("Trouble with Broker: " + e);
        }
    }
}