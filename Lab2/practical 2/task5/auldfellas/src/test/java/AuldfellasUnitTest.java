import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import service.core.Constants;
import service.core.QuotationService;
import service.auldfellas.AFQService;
import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import service.core.ClientInfo;
import service.core.Quotation;

public class AuldfellasUnitTest {
    private static Registry registry;
    private static QuotationService afqService;

    @BeforeClass
    
    public static void setup() {
        afqService = new AFQService();



        try {
            registry = LocateRegistry.createRegistry(1097);

            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService,0);

            registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);

            } catch (Exception e) {
                System.out.println("Trouble: " + e);
        }
    }

            @Test
            public void connectionTest() throws Exception {
        QuotationService service = (QuotationService) registry.lookup(Constants.AULD_FELLAS_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generatequotationTest() throws Exception {
        ClientInfo clientInfo = new ClientInfo();
        Object result = afqService.generateQuotation(clientInfo);

        assertNotNull(result);
        assertTrue(result instanceof Quotation);
    }
}
