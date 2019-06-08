package jlogg.eventbus;

import java.util.List;

import jlogg.datahandlers.FileIndexer;
import jlogg.shared.LogLine;

public class IndexResultEvent {

	private final List<LogLine> loglines;
	private final FileIndexer indexer;
	private final double percentage;

	public IndexResultEvent(FileIndexer indexer, List<LogLine> loglines, double percentage) {
		this.indexer = indexer;
		this.loglines = loglines;
		this.percentage = percentage;
	}

	public List<LogLine> getLogLines() {
		return loglines;
	}

	public FileIndexer getFileIndexer() {
		return indexer;
	}

	public double getPercentage() {
		return percentage;
	}
}
