package jlogg.eventbus;

import jlogg.shared.LogLine;

public class SearchResultEvent {

	private final LogLine logline;

	public SearchResultEvent(LogLine logline) {
		this.logline = logline;
	}

	public LogLine getLogLine() {
		return logline;
	}
}
