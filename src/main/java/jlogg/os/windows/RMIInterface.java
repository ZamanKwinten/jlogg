package jlogg.os.windows;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote {
	void open(List<File> files) throws RemoteException;
}
