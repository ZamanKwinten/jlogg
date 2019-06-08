package jlogg.eventbus;

import java.util.List;

import jlogg.shared.LogLine;

public class SearchResultEvent {

	protected final List<LogLine> logLines;
	protected final double percentage;

	public SearchResultEvent(List<LogLine> logLines, double percentage) {
		this.logLines = logLines;
		this.percentage = percentage;
	}

	public List<LogLine> getLogLines() {
		return logLines;
	}

	public double getPrecentage() {
		return percentage;
	}
}
