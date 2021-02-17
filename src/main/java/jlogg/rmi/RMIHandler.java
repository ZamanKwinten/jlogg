package jlogg.rmi;

import java.io.File;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import jlogg.ui.MainStage;

public class RMIHandler implements RMIInterface, Serializable {
	private static final Logger logger = Logger.getLogger(RMIHandler.class.getName());

	private static final long serialVersionUID = 1L;
	private static final int RMI_PORT = Integer.getInteger("rmi.port", 1099);
	private static final int RMI_STUB_PORT = Integer.getInteger("stub.port", 0);
	private static final String RMI_NAME = "jlogg";

	private transient final MainStage stage;

	public RMIHandler(MainStage stage, List<File> files) {
		this.stage = stage;
		Registry registry = getRegistry();
		try {
			try {
				RMIInterface stub = (RMIInterface) UnicastRemoteObject.exportObject(this, RMI_STUB_PORT);
				registry.bind(RMI_NAME, stub);
				open(files);
			} catch (AlreadyBoundException e) {
				RMIInterface stub = ((RMIInterface) registry.lookup(RMI_NAME));
				stub.open(files);
				System.exit(0);
			}
		} catch (RemoteException | NotBoundException e) {
			logger.log(Level.SEVERE, "RMIHandler", e);
			throw new RuntimeException(e);
		}

	}

	public void shutdown() {
		try {
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NoSuchObjectException e) {
			logger.log(Level.SEVERE, "RMIHandler::shutdown", e);
		}
	}

	private Registry getRegistry() {
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

	@Override
	public void open(List<File> absolutePath) throws RemoteException {
		Platform.runLater(() -> {
			stage.getMainPane().addTabs(absolutePath);
		});
	}
}
