package service;

import akka.actor.*;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.ClientMessage;
import service.message.RegisterMessage;


import java.text.NumberFormat;

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

	static ActorSystem system = ActorSystem.create("ClientSystem");

	static String broker = "localhost";


	public static void main(String[] args) {
		long Token = 1;
		final Props ClientProp = Props.create(ClientQuoter.class);
		final ActorRef ClientRef = system.actorOf(ClientProp, "ClientQuoter");

		if (args.length > 0){
			broker = args[1];
		}

		String BrokerPath = "akka.tcp://BrokerSystem@"+broker+":2550/user/BrokerQuoter";
		ActorSelection remoteActor = system.actorSelection(BrokerPath);

		System.out.println("remoteActor: " + remoteActor);
		remoteActor.tell(new RegisterMessage(ClientRef, "CService"), ClientRef);
		remoteActor.tell(new Identify(BrokerPath), ClientRef);

		ExtendedActorSystem extendedSystem = (ExtendedActorSystem) system;
		Address port = extendedSystem.provider().getDefaultAddress();
		System.out.println("The Client system is bound to port: " + port);

		// Create the broker and run the test data
		for (ClientInfo info : clients) {
			remoteActor.tell(new ClientMessage(Token, info), ClientRef);
			Token++;
		}
	}

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
