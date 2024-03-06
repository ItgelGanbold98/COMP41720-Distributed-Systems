package service;

import akka.actor.*;
import service.broker.BrokerQuoter;

public class Main {
    static ActorSystem system = ActorSystem.create("BrokerSystem");

    public static void main(String[] args) {
        final Props BrokerProp = Props.create(BrokerQuoter.class);
        final ActorRef BrokerRef = system.actorOf(BrokerProp, "BrokerQuoter");

        ExtendedActorSystem extendedSystem = (ExtendedActorSystem) system;
        Address port = extendedSystem.provider().getDefaultAddress();
        System.out.println("The Broker system is bound to port: " + port);
    }
}
