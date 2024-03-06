import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import service.core.Constants;
import service.core.QuotationService;

import java.rmi.RemoteException;
import java.util.List;
import java.util.LinkedList;

import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.BrokerService;

import service.broker.LocalBrokerService;

public class BrokerUnitTest {
    private static Registry registry;
//    private static QuotationService afqService;

    @BeforeClass

    public static void setup() {
        try {

            registry = null;

            registry = LocateRegistry.createRegistry(1099);

            BrokerService brokerService = new LocalBrokerService(registry);

            BrokerService brService = (BrokerService) UnicastRemoteObject.exportObject(brokerService,0);

            registry.bind(Constants.BROKER_SERVICE, brokerService);

            } catch (Exception e) {
                System.out.println("Trouble with Broker unit test: " + e);
        }
    }

            @Test
            public void connectionTest() throws Exception {
                BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

                //Check that service is not null
                assertNotNull(service);
            }

            @Test
            public void generateBrokerTest() throws RemoteException {
                ClientInfo testClientInfo = new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 1.9, 75, true, false);
                Registry registry = LocateRegistry.getRegistry(1099);

                try {
                    BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

                    List<Quotation> quotations = new LinkedList<Quotation>();

                    quotations = brokerService.getQuotations(testClientInfo);

                    assertTrue(quotations.isEmpty());

                } catch(Exception error) {
                    System.out.println("Error with Broker test...");
                }

    }
}
