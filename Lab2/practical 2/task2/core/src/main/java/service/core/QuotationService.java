package service.core;

// import service.registry.Service;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface to define the behaviour of a quotation service.
 * 
 * @author Rem
 *
 */
public interface QuotationService extends Remote
// extends Service 
{
	public Quotation generateQuotation(ClientInfo info) 
	throws java.rmi.RemoteException;
}
