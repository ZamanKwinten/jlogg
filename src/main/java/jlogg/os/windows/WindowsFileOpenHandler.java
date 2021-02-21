package jlogg.os.windows;

import java.io.File;
import java.util.List;

import jlogg.os.FileOpenHandler;
import jlogg.ui.MainStage;

public class WindowsFileOpenHandler extends FileOpenHandler implements RMIInterface {

	public WindowsFileOpenHandler(MainStage stage) {
		super(stage);
		RMITool.registerJLoggInstance(this);
	}

	@Override
	public void open(List<File> files) {
		addTabs(files);
	}

	@Override
	public void release() {
		RMITool.unregisterJLoggInstance(this);
	}

}
