package service;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.OfferMessage;

import java.text.NumberFormat;
import java.util.LinkedList;

public class ClientQuoter extends AbstractActor {

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

    @Override
    public Receive createReceive() {
        System.out.println("**CLIENT ACTOR STARTED** " + getSelf());
        return new ReceiveBuilder()
                .match(OfferMessage.class,
                        msg -> {
                            displayProfile(msg.getInfo());

                            LinkedList<Quotation> quotations = msg.getQuotations();
                            for (Quotation quotation : quotations){
                                System.out.println(
                                        "| Company: " + String.format("%1$-26s", quotation.company) +
                                                " | Reference: " + String.format("%1$-24s", quotation.reference) +
                                                " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
                                System.out.println("|=================================================================================================================|");

                            }
                            System.out.println("\n");
                           })
                .build();
    }
}