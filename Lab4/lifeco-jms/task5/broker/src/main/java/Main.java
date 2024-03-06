import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.*;

import javax.jms.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.time.Instant;

public class Main {


//  We need to keep track of ClientMessages tokens and then match that with QuotationMessage with the same token
//  then that's put to OFFERS queue to be sent to Client

    static HashMap<Long, LinkedList<Quotation>> tokenToQuotationMap = new HashMap<>();
    static HashMap<Long, ClientInfo> tokenToInfoMap = new HashMap<>();

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    public static void main(String[] args) throws JMSException {

        System.out.println("Broker Main class is called.");

        String host = "localhost";

        if (args.length > 0) {
            host = args[0];
        }

        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");

        Connection connection = factory.createConnection();
        connection.setClientID("broker");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

//      Listen on APPLICATIONS topic
        Topic topic = session.createTopic("APPLICATIONS");
        MessageConsumer applicationTopic = session.createConsumer(topic);

//      Listen on QUOTATIONS queue
        Queue quotationQueue = session.createQueue("QUOTATIONS");
        MessageConsumer quotationsQueueConsumer = session.createConsumer(quotationQueue);

//      Add to OFFERS queue
        Queue offerQueue = session.createQueue("OFFERS");
        MessageProducer offerQueueProducer = session.createProducer(offerQueue);

        connection.start();

//      Listen on APPLICATION topic for a ClientMessage type
        new Thread(() -> {
            while (true) {
                try {
                    Message applicationTopicMsg = applicationTopic.receive();
                    // If the message type is ClientMessage, then it was sent by the Client, so take out the token and make new key
                    if (((ObjectMessage) applicationTopicMsg).getObject() instanceof ClientMessage) {

                        ClientMessage clientMsg = (ClientMessage) ((ObjectMessage) applicationTopicMsg).getObject();

                        Long requestToken = clientMsg.getToken();
                        ClientInfo info = clientMsg.getInfo();

                        System.out.println("** Received ClientMessage from APPLICATIONS TOPIC with Token: " + requestToken + " at " + Instant.now());

                        tokenToQuotationMap.put(requestToken, new LinkedList<Quotation>());
                        tokenToInfoMap.put(requestToken, info);

                        executorService.submit(() -> {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            LinkedList<Quotation> accumulatedQuotations = tokenToQuotationMap.get(requestToken);
                            OfferMessage offer = new OfferMessage(info, accumulatedQuotations);

                            try {
                                Message response = session.createObjectMessage(offer);
                                offerQueueProducer.send(response);
                                System.out.println(">> OfferMessage for the ClientMessage with token " + requestToken + " is sent at " + Instant.now());
                                applicationTopicMsg.acknowledge();
                            } catch (JMSException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                } catch (JMSException e) {
                    System.out.println("Error in thread 1");
                    throw new RuntimeException(e);
                }
            }
        }).start();


        new Thread(() -> {
            while (true) {
                try {
                    Message quotationsQueueMsg = quotationsQueueConsumer.receive();
                    // If the message in QUOTATIONS queue is of type QuotationMessage, we need to put it in our hashmap as its a service response
                    if (((ObjectMessage) quotationsQueueMsg).getObject() instanceof QuotationMessage) {
                        QuotationMessage quoteMsg = (QuotationMessage) ((ObjectMessage) quotationsQueueMsg).getObject();
                        Long requestToken = quoteMsg.getToken();
                        Quotation requestQuotationResponse = quoteMsg.getQuotation();
                        try {
                            synchronized (tokenToQuotationMap.get(requestToken)){
                            tokenToQuotationMap.get(requestToken).add(requestQuotationResponse);
                            quotationsQueueMsg.acknowledge();
                            }

                        } catch (Exception e) {
                        }
                    }
                } catch (JMSException e) {
                    System.out.println("Error in thread 2");
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
