package service.broker;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.jmdns.*;


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
    private final List<String> discoveredServices = Collections.synchronizedList(new ArrayList<>());

    public LocalBrokerService(){
        System.out.println("LocalBrokerService invoked");

        try {
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            jmdns.addServiceListener("_quote._tcp.local.", new ServiceDiscovery());
            System.out.println("Looking for service type in LocalBrokerService: _quote._tcp.local.");
            Thread.sleep(2000);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info) {
        LinkedList<Quotation> quotations = new LinkedList<Quotation>();

//        System.out.println("getQuotation is invoked");
//        System.out.println(discoveredServices);

        synchronized (discoveredServices) {
            for (String serviceURL : discoveredServices) {
                try {
//                    System.out.println("URL tried: " + serviceURL);
                    URL wsdlUrl = new URL(serviceURL);
                    QName serviceName = new QName("http://core.service/", "QuotationService");
                    Service service = Service.create(wsdlUrl, serviceName);
                    QName portName = new QName("http://core.service/", "QuotationServicePort");
                    QuotationService quotationService = service.getPort(portName, QuotationService.class);
                    Quotation quotation = quotationService.generateQuotation(info);
                    quotations.add(quotation);
                } catch (Exception e) {
                    System.out.println("** Service does not exist or problem invoking it. **");
                }
            }
        }

        return quotations;
    }

    public class ServiceDiscovery implements ServiceListener {
        public ServiceDiscovery(){
            System.out.println("** Service Discovery is running... **");
        };

        @Override
        public void serviceAdded(ServiceEvent event) {
            System.out.println("Service added: " + event.getInfo());
        }

        @Override
        public void serviceRemoved(ServiceEvent event){
            System.out.println("Service removed: " + event.getInfo());
            String path = event.getInfo().getPropertyString("path");
            if (path != null) {
                try {
                    discoveredServices.remove(path);
                } catch (Exception e) {
                    System.out.println("Problem with service: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void serviceResolved(ServiceEvent event){
            System.out.println("Service resolved: " + event.getInfo());
            String path = event.getInfo().getPropertyString("path");

            //Make sure duplicate URLs arent added
            if (path != null && !discoveredServices.contains(path)) {
                discoveredServices.add(path);
            }
        }
    }
}
