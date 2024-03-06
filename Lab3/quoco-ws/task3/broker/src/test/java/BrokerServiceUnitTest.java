import org.junit.BeforeClass;
import org.junit.Test;
import service.broker.LocalBrokerService;
import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;


public class BrokerServiceUnitTest {
    @BeforeClass
    public static void setup() {
        Endpoint.publish("http://0.0.0.0:9000/broker", new LocalBrokerService());
    }

    @Test
    public void connectionTest() throws Exception {
        URL wsdlUrl = new URL("http://localhost:9000/broker");

        QName serviceName = new QName("http://core.service/", "BrokerService");

        Service service = Service.create(wsdlUrl, serviceName);

        QName portName = new QName("http://core.service/", "BrokerServicePort");

        BrokerService brokerService = service.getPort(portName, BrokerService.class);

        LinkedList<Quotation> quotation = brokerService.getQuotations(new ClientInfo(
                        "Niki Collier", ClientInfo.FEMALE, 49,
                        1.5494, 80, false, false));

        assertNotNull(quotation);
    }
}