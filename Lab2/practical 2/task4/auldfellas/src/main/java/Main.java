import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import service.auldfellas.AFQService;
import service.core.QuotationService;
import service.core.Constants;
import service.core.BrokerService;

public class Main {
    public static void main(String[] args) {

        //This is our remote object
        QuotationService afqService = new AFQService();

        try {

            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.

            Registry registry = null;
            if (args.length == 0) {
                registry = LocateRegistry.createRegistry(1099);
            } else {
                registry = LocateRegistry.getRegistry(args[0], 1099);
            }

            // Create the Remote Object

            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService,0);

            BrokerService broker_service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

            broker_service.registerService(Constants.AULD_FELLAS_SERVICE, quotationService);

            System.out.println("(Auldfellas) STOPPING SERVER SHUTDOWN");
            while (true) {Thread.sleep(1000); }
        } catch (Exception e) {
            System.out.println("Trouble (auldfellas): " + e);
        }
    }
}