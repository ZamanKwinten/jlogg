package jlogg.os.windows;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jlogg.os.FileOpenHandler;

public class WindowsInitialOpen {
	public static List<File> initialFiles = new ArrayList<>();

	public static void loadInitial(String[] args) {
		List<File> files = Arrays.stream(args).map(path -> new File(path)).collect(Collectors.toList());
		try {
			RMITool.lookUpJLoggInstance().open(files);
			System.exit(0);
		} catch (NotBoundException e) {
			FileOpenHandler.initialFiles = files;
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
}
