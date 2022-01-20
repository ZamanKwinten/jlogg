package jlogg;

import java.awt.Desktop;
import java.util.Arrays;
import java.util.Objects;

import javafx.application.Application;
import jlogg.os.mac.MacInitialFileOpen;
import jlogg.os.windows.WindowsInitialOpen;

public class Launcher {
	public static final String NO_GUI = "NO_UI";

	public static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
	public static final boolean isMac = System.getProperty("os.name").startsWith("Mac");

	public static void main(String[] args) {
		if (args.length > 0 && Objects.equals(args[0], NO_GUI)) {
			ScriptRunner.run(Arrays.copyOfRange(args, 1, args.length));
		} else {
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
}
