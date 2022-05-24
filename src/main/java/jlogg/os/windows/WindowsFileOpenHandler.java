package jlogg.os.windows;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import jlogg.os.FileOpenHandler;
import jlogg.ui.MainStage;

public class WindowsFileOpenHandler extends FileOpenHandler {

	public WindowsFileOpenHandler(MainStage stage) {
		super(stage);
		WindowsDomainSocketServer.openHandler = this;
	}

	void open(String msg) {
		addTabs(Arrays.stream(msg.split(",")).map(File::new).collect(Collectors.toList()));
	}

	@Override
	public void release() {
		WindowsDomainSocketServer.clearSocketFile();
	}

}
