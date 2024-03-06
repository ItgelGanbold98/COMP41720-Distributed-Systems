package service;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.ClientMessage;
import service.message.OfferMessage;

import javax.jms.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {

    /**
     * This is the starting point for the application. Here, we must
     * get a reference to the Broker Service and then invoke the
     * getQuotations() method on that service.
     * <p>
     * Finally, you should print out all quotations returned
     * by the service.
     *
     * @param args
     */

    private static HashMap<ClientInfo, LinkedList<Quotation>> clientInfoQuotationList = new HashMap<>();

    public static void main(String[] args) throws JMSException {

        System.out.println("Client Main class is called.");

        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.setClientID("client");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Queue offerQueue = session.createQueue("OFFERS");
        MessageConsumer offerQueueConsumer = session.createConsumer(offerQueue);

        Topic topic = session.createTopic("APPLICATIONS");
        MessageProducer topicProducer = session.createProducer(topic);

        connection.start();

        Long token = 1L;

        for (ClientInfo info : clients) {
            System.out.println("Publishing to topic APPLICATIONS");

//          For each client info, create a key-value pair in the linked-list
            clientInfoQuotationList.put(info, new LinkedList<>());

            topicProducer.send(
                    session.createObjectMessage(
                            new ClientMessage(token++, info)
                    )
            );
        }


        new Thread (() -> {
            while(true) {
                Message offerQueueMsg = null;

                try {
                    offerQueueMsg = offerQueueConsumer.receive();
                    if (offerQueueMsg != null){
                        offerQueueMsg.acknowledge();
                    }
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }

                OfferMessage receivedMessage = null;
                try {
                    receivedMessage = (OfferMessage) ((ObjectMessage) offerQueueMsg).getObject();
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }

                LinkedList<Quotation> existingQuotations = clientInfoQuotationList.get(receivedMessage.getInfo());
                if (existingQuotations != null) {
                    existingQuotations.addAll(receivedMessage.getQuotations());
                } else {
                    System.out.println("Warning: Received an offer for an unknown client.");
                }

                displayProfile(receivedMessage.getInfo());
                for (Quotation quotation : receivedMessage.getQuotations()){
                    displayQuotation(quotation);
                }


            }
        }).start();


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
                        " | Gender: " + String.format("%1$-27s", (info.gender == ClientInfo.MALE ? "Male" : "Female")) +
                        " | Age: " + String.format("%1$-30s", info.age) + " |");
        System.out.println(
                "| Weight/Height: " + String.format("%1$-20s", info.weight + "kg/" + info.height + "m") +
                        " | Smoker: " + String.format("%1$-27s", info.smoker ? "YES" : "NO") +
                        " | Medical Problems: " + String.format("%1$-17s", info.medicalIssues ? "YES" : "NO") + " |");
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
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price)) + " |");
        System.out.println("|=================================================================================================================|");
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
