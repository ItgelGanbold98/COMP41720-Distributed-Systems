import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import service.core.Constants;
import service.core.QuotationService;
import service.girlsallowed.GAQService;
import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import service.core.ClientInfo;
import service.core.Quotation;

public class GirlsallowedUnitTest {
    private static Registry registry;
    private static QuotationService gaqService;

    @BeforeClass
    
    public static void setup() {
        gaqService = new GAQService();



        try {
            registry = LocateRegistry.createRegistry(1099);

            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(gaqService,0);

            registry.bind(Constants.GIRLS_ALLOWED_SERVICE, quotationService);

            } catch (Exception e) {
                System.out.println("Trouble: " + e);
        }
    }

            @Test
            public void connectionTest() throws Exception {
        QuotationService service = (QuotationService) registry.lookup(Constants.GIRLS_ALLOWED_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generatequotationTest() throws Exception {
        ClientInfo clientInfo = new ClientInfo();
        Object result = gaqService.generateQuotation(clientInfo);

        assertNotNull(result);
        assertTrue(result instanceof Quotation);
    }
}
