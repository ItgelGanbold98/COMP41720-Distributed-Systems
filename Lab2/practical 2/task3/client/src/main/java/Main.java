package client;

import java.text.NumberFormat;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.Constants;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;


public class Main {
	/**
	 * This is the starting point for the application. Here, we must
	 * get a reference to the Broker Service and then invoke the
	 * getQuotations() method on that service.
	 *
	 * Finally, you should print out all quotations returned
	 * by the service.
	 *
	 * @param args
	 */

	public static void main(String[] args) {

        try {
            Registry registry = null;

        if (args.length == 0) {
            registry = LocateRegistry.createRegistry(1099);
        } else {
            registry = LocateRegistry.getRegistry(args[0], 1099);
        }

		BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

		// Create the broker and run the test data
		for (ClientInfo info : clients) {
			displayProfile(info);

			// Retrieve quotations from the broker and display them...
			for(Quotation quotation : brokerService.getQuotations(info)) {
				displayQuotation(quotation);
			}

			// Print a couple of lines between each client
			System.out.println("\n");
		}
	} catch (RemoteException | NotBoundException ex) {
            System.out.println("Error in Client...");
        }
    }

	/**
	 * Display the client info nicely.
	 *
	 * @param info
	 */
	public static void displayProfile(ClientInfo info) {
		System.out.println("|=================================================================================================================|");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println(
				"| Name: " + String.format("%1$-29s", info.name) +
				" | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.age)+" |");
		System.out.println(
				"| Weight/Height: " + String.format("%1$-20s", info.weight+"kg/"+info.height+"m") +
				" | Smoker: " + String.format("%1$-27s", info.smoker?"YES":"NO") +
				" | Medical Problems: " + String.format("%1$-17s", info.medicalIssues?"YES":"NO")+" |");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Display a quotation nicely - note that the assumption is that the quotation will follow
	 * immediately after the profile (so the top of the quotation box is missing).
	 *
	 * @param quotation
	 */
	public static void displayQuotation(Quotation quotation) {
		System.out.println(
				"| Company: " + String.format("%1$-26s", quotation.company) +
				" | Reference: " + String.format("%1$-24s", quotation.reference) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
		System.out.println("|=================================================================================================================|");
	};

	/**
	 * Test Data
	 */
	public static final ClientInfo[] clients = {
		new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false),
		new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 1.6, 100, true, true),
		new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 21, 1.78, 65, false, false),
		new ClientInfo("Rem Collier", ClientInfo.MALE, 49, 1.8, 120, false, true),
		new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 1.9, 75, true, false),
		new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 0.45, 1.6, false, false)
	};
}