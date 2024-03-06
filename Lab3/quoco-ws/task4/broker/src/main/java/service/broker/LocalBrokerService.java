package service.broker;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * Implementation of the broker service that uses the Service Registry.
 *
 * @author Rem
 */

@WebService(name = "BrokerService",
        targetNamespace = "http://core.service/",
        serviceName = "BrokerService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class LocalBrokerService implements BrokerService {
    public static final List<String> SERVICE_URLs = Arrays.asList(
            "http://auldfellas:9001/quotations?wsdl",
            "http://dodgygeezers:9002/quotations?wsdl",
            "http://girlsallowed:9003/quotations?wsdl"
    );

//    The URLs are modified so that docker containers can communicate properly.

    @Override
    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info) {

        LinkedList<Quotation> quotations = new LinkedList<Quotation>();

        System.out.println("Entering LocalBrokerService");

        try {
            for (String serviceURL : SERVICE_URLs) {
                System.out.println("URL tried: " + serviceURL);

                URL wsdlUrl = new URL(serviceURL);
                QName serviceName = new QName("http://core.service/", "QuotationService");

                Service service = Service.create(wsdlUrl, serviceName);
                QName portName = new QName("http://core.service/", "QuotationServicePort");

                QuotationService quotationService = service.getPort(portName, QuotationService.class);

                Quotation quotation = quotationService.generateQuotation(info);
                quotations.add(quotation);
            }
        } catch (Exception e) {
            System.out.println("** Service does not exist. **");
        }

        return quotations;
    }
}
