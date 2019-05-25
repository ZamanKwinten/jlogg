package jlogg.eventbus;

import java.io.File;

public class IndexStartEvent {

	private final File file;

	public IndexStartEvent(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}
}
