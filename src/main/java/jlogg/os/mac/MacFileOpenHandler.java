package jlogg.os.mac;

import java.awt.desktop.OpenFilesEvent;
import java.awt.desktop.OpenFilesHandler;

import jlogg.os.FileOpenHandler;
import jlogg.ui.MainStage;

public class MacFileOpenHandler extends FileOpenHandler implements OpenFilesHandler {

	public MacFileOpenHandler(MainStage stage) {
		super(stage);
	}

	@Override
	public void release() {
	}

	@Override
	public void openFiles(OpenFilesEvent e) {
		addTabs(e.getFiles());
	}

}
