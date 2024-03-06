package service;

import akka.actor.*;
import service.message.RegisterMessage;
import service.quoter.AFQuoter;

public class Main {
    static ActorSystem system = ActorSystem.create("ServiceSystem");
    static String broker = "localhost";

    public static void main(String[] args) {
        final Props AFQuoterProp = Props.create(AFQuoter.class);
        final ActorRef AFQuoterRef = system.actorOf(AFQuoterProp, "AFQuoter");

        if (args.length > 0){
            broker = args[1];
        }

        String BrokerPath = "akka.tcp://BrokerSystem@"+broker+":2550/user/BrokerQuoter";
        ActorSelection remoteActor = system.actorSelection(BrokerPath);

        System.out.println("remoteActor: " + remoteActor);
        remoteActor.tell(new RegisterMessage(AFQuoterRef, "QService"), AFQuoterRef);

        ExtendedActorSystem extendedSystem = (ExtendedActorSystem) system;
        Address port = extendedSystem.provider().getDefaultAddress();
        System.out.println("The AF Service system is bound to port: " + port);
    }
}
