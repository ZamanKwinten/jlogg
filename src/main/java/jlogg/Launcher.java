package jlogg;

import java.awt.Desktop;

import javafx.application.Application;
import jlogg.os.mac.MacInitialFileOpen;
import jlogg.os.windows.WindowsInitialOpen;

public class Launcher {
	public static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
	public static final boolean isMac = System.getProperty("os.name").startsWith("Mac");

	public static void main(String[] args) {
		if (isWindows) {
			WindowsInitialOpen.loadInitial(args);
		} else if (isMac) {
			Desktop.getDesktop().setOpenFileHandler(new MacInitialFileOpen());
		} else {
			throw new IllegalArgumentException("Operating System not supported");
		}

		Application.launch(App.class);
	}
}
