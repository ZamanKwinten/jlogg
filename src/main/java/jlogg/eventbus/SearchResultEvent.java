package jlogg.eventbus;

import java.util.List;

import jlogg.shared.LogLine;

public class SearchResultEvent {

	protected final SearchEvent searchEvent;
	protected final List<LogLine> logLines;
	protected final double percentage;

	public SearchResultEvent(SearchEvent searchEvent, List<LogLine> logLines, double percentage) {
		this.searchEvent = searchEvent;
		this.logLines = logLines;
		this.percentage = percentage;
	}

	public List<LogLine> getLogLines() {
		return logLines;
	}

	public double getPrecentage() {
		return percentage;
	}

	public void setGlobalConstants() {
		searchEvent.setGlobalConstants(logLines);
	}
}
