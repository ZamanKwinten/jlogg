package jlogg.datahandlers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import jlogg.ConstantMgr;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.IndexFinishedEvent;
import jlogg.eventbus.IndexResultEvent;
import jlogg.plugin.LogLine;

public class FileIndexer extends FileIterator {
	private static final Logger log = Logger.getLogger(FileIndexer.class.getName());

	/**
	 * Define the Executor service used for indexing
	 */
	private static final ExecutorService indexService = Executors.newFixedThreadPool(
			ConstantMgr.instance().indexServiceThreadCount,
			new ThreadFactoryBuilder().setDaemon(true).setNameFormat("index-thread-%d").build());

	/**
	 * File indexer can only run on 1 file at a time
	 * 
	 * @param file
	 */
	public FileIndexer(File file) {
		super(Arrays.asList(file));
	}

	@Override
	protected ExecutorService getExecutorService() {
		return indexService;
	}

	@Override
	protected void logStart(File file) {
		log.log(Level.INFO, this + " Indexing of " + file.getAbsolutePath() + " started");
	}

	@Override
	protected void logEnd(File file, long duration) {
		log.log(Level.INFO, this + " Indexing of " + file.getAbsolutePath() + " finished in " + duration + "ms");
	}

	@Override
	protected void handleIOException(IOException e) {
		log.log(Level.SEVERE, this + " FileIndex::doIt", e);
	}

	@Override
	protected boolean shouldAddToTempResult(String s, LogLine l) {
		return true;
	}

	@Override
	protected void submitPercentEvent(File file, List<LogLine> lines, double percentage) {
		EventBusFactory.getInstance().getEventBus().post(new IndexResultEvent(this, lines, percentage));
		// make sure to clear the list of cached log lines to prevent subsequent submits
		// to include these
		lines.clear();
	}

	@Override
	protected void submitFinishedEvent(File file, List<LogLine> lines) {
		EventBusFactory.getInstance().getEventBus().post(new IndexFinishedEvent(this, lines));
	}

	/**
	 * Get the file on which this indexer is running
	 * 
	 * @return
	 */
	public File getFile() {
		return files.get(0).getFile();
	}
}
