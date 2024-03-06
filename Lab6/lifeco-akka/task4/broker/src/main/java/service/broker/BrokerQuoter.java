package service.broker;

import service.core.ClientInfo;
import service.core.Quotation;
import service.message.*;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BrokerQuoter extends AbstractActor {
    Map<Long, ClientInfo> clientRequests = new HashMap<>();
    Map<Long, LinkedList<Quotation>> receivedQuotations = new HashMap<>();
    ArrayList<ActorRef> registeredServiceActors = new ArrayList<>();
    Map<Long, ActorRef> infoSender = new HashMap<>();

    @Override
    public Receive createReceive() {
        System.out.println("**BROKERQUOTER ACTOR HAS STARTED** " + getSelf());
        return receiveBuilder()
                .match(RegisterMessage.class, msg -> {
                    if (!msg.actorType.equals("CService")){
                    // Register the actors that send RegisterMessage to Broker
                    System.out.println("\n**Register request received from: " + msg.actorRef);
                    registeredServiceActors.add(msg.actorRef);
                    System.out.println(">>Service registered: " + registeredServiceActors);
                    System.out.println("=>Current Broker instance: " + getSelf());
                }})

                .match(ClientMessage.class, msg -> {
                    System.out.println("Broker received ClientMessage" + msg.getInfo().toString() + "\n at " + LocalDateTime.now());
                    System.out.println("Broker instance:" + getSelf() + " sender: " + getSender());
                    System.out.println("List of registered services: " + registeredServiceActors);
                    //Capture the sender reference
                    final ActorRef originalSender = getSender();
                    System.out.println("\nClient Message Sender: " + originalSender + "\n");
                    //Keep the message Token and ClientInfo together to look up later
                    clientRequests.put(msg.getToken(), msg.getInfo());
                    //Keep track of Token and Sender reference
                    infoSender.put(msg.getToken(), originalSender);
                    //Send the ClientInfo to each Quoter
                    for (ActorRef service : registeredServiceActors){
                        System.out.println("Broker sending ClientMessage to registered services");
                        service.tell(msg, getSelf());
                    };
                    //Wait 2 seconds before sending out the OfferMessage
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        TimeoutMessage timeOutMsg = new TimeoutMessage(msg.getToken());
                        getSelf().tell(timeOutMsg, getSelf());
                    }).start();
                })
                .match(TimeoutMessage.class, msg ->{
                    System.out.println("Broker received TimeoutMessage");
                    //Get the ClientInfo for the associated Token
                    LinkedList<Quotation> associatedQuotations = receivedQuotations.get(msg.getToken());
                    //Construct the OfferMessage
                    OfferMessage offerMsg = new OfferMessage(clientRequests.get(msg.getToken()), associatedQuotations);
                    //Send out the message back
                    System.out.println("Sending OfferMessage back at " + LocalDateTime.now());
                    infoSender.get(msg.getToken()).tell(offerMsg, getSelf());
                })

                .match(QuotationMessage.class, msg -> {
                    System.out.println("Received quotation at Broker from service : Token - " + msg.getToken() + " " + msg.getQuotation());
                    if (receivedQuotations.containsKey(msg.getToken())){
                        if (receivedQuotations.get(msg.getToken()).size() >= 3){
                            receivedQuotations.get(msg.getToken()).clear();
                        }
                        receivedQuotations.get(msg.getToken()).add(msg.getQuotation());
                        System.out.println("Received quotations: " + receivedQuotations.get(msg.getToken()));
                    } else {
                        receivedQuotations.put(msg.getToken(), new LinkedList<>());
                        receivedQuotations.get(msg.getToken()).add(msg.getQuotation());
                    };
                })
                .build();
    }
}
