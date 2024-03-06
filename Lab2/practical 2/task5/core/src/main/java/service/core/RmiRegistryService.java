package service.core;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiRegistryService extends Remote {
    void registerService(String name, Remote service) throws RemoteException;
}
