package jlogg.os.windows;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

class RMITool {
	private static final int RMI_PORT = Integer.getInteger("rmi.port", 1099);
	private static final String RMI_NAME = "jlogg";

	private static Registry getRegistry() {
		try {
			try {
				return LocateRegistry.createRegistry(RMI_PORT);
			} catch (ExportException e) {
				return LocateRegistry.getRegistry(RMI_PORT);
			}
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public static RMIInterface lookUpJLoggInstance() throws NotBoundException {
		try {
			return (RMIInterface) getRegistry().lookup(RMI_NAME);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public static void registerJLoggInstance(RMIInterface instance) {
		Registry registry = getRegistry();
		RMIInterface stub;
		try {
			stub = (RMIInterface) UnicastRemoteObject.exportObject(instance, 0);
			registry.bind(RMI_NAME, stub);
		} catch (RemoteException | AlreadyBoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static void unregisterJLoggInstance(RMIInterface instance) {
		try {
			UnicastRemoteObject.unexportObject(instance, true);
		} catch (NoSuchObjectException e) {
			throw new RuntimeException(e);
		}
	}
}
