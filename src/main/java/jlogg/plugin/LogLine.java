package jlogg.plugin;

import java.io.File;

public class LogLine {

	private final int lineNumber;
	private final long start;
	private final int size;
	private final File file;

	public LogLine(int lineNumber, long start, int size, File file) {
		this.lineNumber = lineNumber;
		this.start = start;
		this.size = size;
		this.file = file;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public long getStart() {
		return start;
	}

	public int getSize() {
		return size;
	}

	public File getFile() {
		return file;
	}
}
