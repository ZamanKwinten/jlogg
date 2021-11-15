package jlogg.plugin;

import java.io.File;

public interface PluginAction {
	/**
	 * This method will be called for every line in the log file. Plugins should
	 * parse the text to determine whether the line is useful or not, but only keep
	 * the LogLine object in memory (to limit the memory footprint)
	 * 
	 * @param text
	 * @param line
	 */
	public void handleLine(String text, LogLine line);

	/**
	 * This method is called whenever a percentage point of the log file is handled.
	 * Plugins could use this method to give some kind of progress indicator
	 * 
	 * @param file
	 * @param percentage
	 */
	public void consumePercentEvent(File file, double percentage);

	/**
	 * This method is called whenever the file is fully parsed.
	 * 
	 * @param file
	 */
	public void consumeFileFinishedEvent(File file);

	/**
	 * This method is called whenever everything is finished
	 */
	public void consumeFinishedEvent();
}
