package jlogg.eventbus;

import java.io.File;
import java.util.List;

import jlogg.plugin.LogLine;

public class SearchResultEvent {

	protected final SearchEvent searchEvent;
	protected final File file;
	protected final List<LogLine> logLines;
	protected final double percentage;

	public SearchResultEvent(SearchEvent searchEvent, File file, List<LogLine> logLines, double percentage) {
		this.searchEvent = searchEvent;
		this.file = file;
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
		searchEvent.setGlobalConstants(file, logLines);
	}
}
