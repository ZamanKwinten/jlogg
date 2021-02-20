package jlogg.os;

import java.io.File;
import java.util.List;

import jlogg.ui.MainStage;

public abstract class FileOpenHandler {

	protected final MainStage stage;

	public FileOpenHandler(MainStage stage) {
		this.stage = stage;
	}

	public abstract void open(List<File> files);

	public abstract void release();
}
