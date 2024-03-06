package service.broker;

import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;

import service.core.*;

import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class LocalBrokerService implements BrokerService, Serializable, RmiRegistryService {

	//Create registry variable
	Registry rmi_registry;

	public LocalBrokerService(Registry initialize_service_registry) {
		this.rmi_registry = initialize_service_registry;
	}

	@Override
	public List<Quotation> getQuotations(ClientInfo info) throws RemoteException {
		List<Quotation> quotations = new LinkedList<>();

		for (String name : rmi_registry.list()) {
			if (name.startsWith("qs-")) {
				try {
					QuotationService service = (QuotationService) rmi_registry.lookup(name);
					quotations.add(service.generateQuotation(info));
				} catch (Exception error) {
					error.printStackTrace();
					System.out.println("Something went wrong with Broker");
				}
			}
		}

		return quotations;
	}

	@Override
	public void registerService(String name, Remote service) throws RemoteException {
		try {
			this.rmi_registry.bind(name, service);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
}
