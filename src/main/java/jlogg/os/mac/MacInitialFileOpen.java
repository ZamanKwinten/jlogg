package jlogg.os.mac;

import java.awt.desktop.OpenFilesEvent;
import java.awt.desktop.OpenFilesHandler;

import jlogg.os.FileOpenHandler;

public class MacInitialFileOpen implements OpenFilesHandler {

	@Override
	public void openFiles(OpenFilesEvent e) {
		FileOpenHandler.initialFiles = e.getFiles();
	}

}
