package jlogg.eventbus;

import java.io.File;
import java.util.List;

import jlogg.shared.LogLine;

public class IndexDoneEvent {

	private final List<LogLine> loglines;
	private final File file;

	public IndexDoneEvent(File file, List<LogLine> loglines) {
		this.file = file;
		this.loglines = loglines;
	}

	public List<LogLine> getLogLines() {
		return loglines;
	}

	public File getFile() {
		return file;
	}
}
