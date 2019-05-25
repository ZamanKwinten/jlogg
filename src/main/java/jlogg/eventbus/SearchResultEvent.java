package jlogg.eventbus;

import java.util.List;

import jlogg.shared.LogLine;

public class SearchResultEvent {

	private final List<LogLine> logLines;

	public SearchResultEvent(List<LogLine> logLines) {
		this.logLines = logLines;
	}

	public List<LogLine> getLogLines() {
		return logLines;
	}
}
