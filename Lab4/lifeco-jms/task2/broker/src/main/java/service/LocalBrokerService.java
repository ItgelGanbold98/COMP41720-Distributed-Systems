package service;

import service.core.ClientInfo;
import service.core.Quotation;
//import service.registry.ServiceRegistry;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
public class LocalBrokerService {
	public List<Quotation> getQuotations(ClientInfo info) {
		List<Quotation> quotations = new LinkedList<Quotation>();
		
//		for (String name : ServiceRegistry.list()) {
//			if (name.startsWith("qs-")) {
//				QuotationService service = ServiceRegistry.lookup(name, QuotationService.class);
//				quotations.add(service.generateQuotation(info));
//			}
//		}

		return quotations;
	}
}
