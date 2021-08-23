package jlogg.datahandlers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jlogg.ConstantMgr;
import jlogg.eventbus.EventBus;
import jlogg.eventbus.SearchEvent;
import jlogg.eventbus.SearchFinishedEvent;
import jlogg.eventbus.SearchResultEvent;
import jlogg.plugin.LogLine;

public class FileSearcher extends FileIterator {

	private static final Logger log = Logger.getLogger(FileSearcher.class.getName());

	/**
	 * Define the Executor service used for searching
	 * 
	 */
	private static final ExecutorService searchService = Executors.newFixedThreadPool(
			ConstantMgr.instance().searchServiceThreadCount, new NamedThreadFactory("search-thread-"));

	private final SearchEvent searchEvent;

	public FileSearcher(SearchEvent searchEvent) {
		super(searchEvent.getFiles());
		this.searchEvent = searchEvent;
	}

	@Override
	protected ExecutorService getExecutorService() {
		return searchService;
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
	protected void submitFinishedEvent(File file, List<LogLine> lines) {
		EventBus.get().submit(new SearchFinishedEvent(searchEvent, file, lines));
	}
}
