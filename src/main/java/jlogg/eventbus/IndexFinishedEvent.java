package jlogg.eventbus;

import java.util.List;

import jlogg.datahandlers.FileIndexer;
import jlogg.shared.LogLine;

public class IndexFinishedEvent extends IndexResultEvent {

	public IndexFinishedEvent(FileIndexer indexer, List<LogLine> loglines) {
		super(indexer, loglines, 1.0);
	}

}
