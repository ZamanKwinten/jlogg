package jlogg.shared;

import aaatemporary.LoremIpsum;

public class LogLine {

	private final int lineNumber;

	public LogLine(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getLineString() {
		return LoremIpsum.getLines().get(lineNumber);
	}
}
