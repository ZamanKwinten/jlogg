package jlogg.shared;

import java.io.File;

import jlogg.datahandlers.FileLineReader;

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

	public String getLineString() {
		return FileLineReader.readLineFromFile(this);
	}
}
