package jlogg.datahandlers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import jlogg.eventbus.EventBus;
import jlogg.eventbus.SearchEvent;
import jlogg.eventbus.SearchFinishedEvent;
import jlogg.eventbus.SearchResultEvent;
import jlogg.plugin.LogLine;

public class FileSearcher extends FileIterator {

	private static final Logger log = Logger.getLogger(FileSearcher.class.getName());

	private final SearchEvent searchEvent;

	public FileSearcher(SearchEvent searchEvent) {
		super(searchEvent.getFiles());
		this.searchEvent = searchEvent;
	}

	@Override
	protected ExecutorService getExecutorService() {
		return ThreadGroups.searchService;
	}

	@Override
	protected void logStart(File file) {
		log.log(Level.INFO, this + " Searching in " + file.getAbsolutePath() + " started");
	}

	@Override
	protected void logEnd(File file, long duration) {
		log.log(Level.INFO, this + " Searching in " + file.getAbsolutePath() + " finished in " + duration + "ms");
	}

	@Override
	protected void handleIOException(IOException e) {
		log.log(Level.SEVERE, this + " FileSearcher::doIt", e);
	}

	@Override
	protected boolean shouldAddToTempResult(String s, LogLine l) {
		return searchEvent.getCriteria().matches(s);
	}

	@Override
	protected void submitPercentEvent(File file, List<LogLine> lines, double percentage) {
		EventBus.get().submit(new SearchResultEvent(searchEvent, file, lines, percentage));
		// make sure to clear the list of cached log lines to prevent subsequent submits
		// to include these
		lines.clear();
	}

	@Override
	protected void submitFileFinishedEvent(File file, List<LogLine> lines) {
		EventBus.get().submit(new SearchFinishedEvent(searchEvent, file, lines));
	}
}
