package jlogg.datahandlers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jlogg.plugin.LogLine;
import jlogg.plugin.PluginAction;

/**
 * A special kind of FileIterator which makes it possible for a Plugin to
 * perform any action it desires on a LogLine
 * 
 * @author Kwinten
 *
 */
public class PluginFileIterator extends FileIterator {
	private static final Logger log = Logger.getLogger(FileIndexer.class.getName());

	private static final ExecutorService pluginIterationService = Executors.newFixedThreadPool(1,
			new NamedThreadFactory("plugin-iterate-"));

	private final PluginAction action;

	public PluginFileIterator(File file, PluginAction action) {
		this(Collections.singleton(file), action);
	}

	public PluginFileIterator(Collection<File> files, PluginAction action) {
		super(files);
		this.action = action;
	}

	@Override
	protected ExecutorService getExecutorService() {
		return pluginIterationService;
	}

	@Override
	protected void logStart(File file) {
		log.log(Level.INFO, this + " Plugin interation of " + file.getAbsolutePath() + " started");
	}

	@Override
	protected void logEnd(File file, long duration) {
		log.log(Level.INFO,
				this + " Plugin interation of " + file.getAbsolutePath() + " finished in " + duration + "ms");
	}

	@Override
	protected void handleIOException(IOException e) {
		log.log(Level.SEVERE, this + " PluginFileIterator::doIt", e);
	}

	@Override
	protected boolean shouldAddToTempResult(String s, LogLine l) {
		action.handleLine(s, l);
		return false;
	}

	@Override
	protected void submitPercentEvent(File file, List<LogLine> lines, double percentage) {
		action.consumePercentEvent(file, percentage);
	}

	@Override
	protected void submitFileFinishedEvent(File file, List<LogLine> lines) {
		action.consumeFileFinishedEvent(file);
	}

	@Override
	protected void submitFinishedEvent() {
		action.consumeFinishedEvent();
	}
}
