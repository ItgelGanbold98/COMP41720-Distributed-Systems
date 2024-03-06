import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import service.core.Constants;
import service.core.QuotationService;
import service.dodgygeezers.DGQService;
import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import service.core.ClientInfo;
import service.core.Quotation;

public class DodgygeezersUnitTest {
    private static Registry registry;
    private static QuotationService dgqService;

    @BeforeClass
    
    public static void setup() {
        dgqService = new DGQService();



        try {
            registry = LocateRegistry.createRegistry(1098);

            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(dgqService,0);

            registry.bind(Constants.DODGY_GEEZERS_SERVICE, quotationService);

            } catch (Exception e) {
                System.out.println("Trouble: " + e);
        }
    }

            @Test
            public void connectionTest() throws Exception {
            QuotationService service = (QuotationService) registry.lookup(Constants.DODGY_GEEZERS_SERVICE);
            assertNotNull(service);
    }

    @Test
    public void generatequotationTest() throws Exception {
        ClientInfo clientInfo = new ClientInfo();
        Object result = dgqService.generateQuotation(clientInfo);

        assertNotNull(result);
        assertTrue(result instanceof Quotation);
    }
}
