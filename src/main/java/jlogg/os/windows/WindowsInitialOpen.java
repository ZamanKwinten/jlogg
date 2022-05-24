package jlogg.os.windows;

import java.io.File;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jlogg.os.FileOpenHandler;

public class WindowsInitialOpen {

	public static void loadInitial(String[] args) {
		List<File> files = Arrays.stream(args).map(path -> new File(path)).collect(Collectors.toList());

		try {
			WindowsDomainSocketServer
					.writeMessage(files.stream().map(File::getAbsolutePath).collect(Collectors.joining(",")));

			System.exit(0);
		} catch (ConnectException e) {
			WindowsDomainSocketServer.startup();
			FileOpenHandler.initialFiles = files;
		}
	}
}
