import org.apache.activemq.ActiveMQConnectionFactory;
import service.AFQService;
import service.core.Quotation;
import service.message.ClientMessage;
import service.message.QuotationMessage;

import javax.jms.*;


public class Main {
    private static AFQService service = new AFQService();

    public static void main(String[] args) throws JMSException {
        System.out.println("Auldfellas main class called.");

        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.setClientID("auldfellas");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);


        Queue queue = session.createQueue("QUOTATIONS");
        Topic topic = session.createTopic("APPLICATIONS");

//      The service listens for messages that are published on topic APPLICATIONS
        MessageConsumer consumer = session.createConsumer(topic);

//      The output of the service is then sent to QUOTATIONS queue
        MessageProducer producer = session.createProducer(queue);

        connection.start();

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("Message received from APPLICATIONS topic");

                try {
                    ClientMessage request = (ClientMessage) ((ObjectMessage) message).getObject();

                    Quotation quotation = service.generateQuotation(request.getInfo());

                    Message response = session.createObjectMessage(new QuotationMessage(request.getToken(), quotation));

//                  Send the quotation to the QUOTATIONS queue
                    producer.send(response);

                    message.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
