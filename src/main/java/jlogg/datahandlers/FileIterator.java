package jlogg.datahandlers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import jlogg.plugin.LogLine;

/**
 * Abstract class usable by any logic that wants to handle all lines of a file
 * exactly once
 * 
 * @author Kwinten
 *
 */
public abstract class FileIterator {

	private final Logger logger = Logger.getLogger(FileIterator.class.getName());

	protected final List<FileMetaData> files;

	private Future<Void> future;

	public FileIterator(Collection<File> files) {
		this.files = new ArrayList<>();
		for (File f : files) {
			this.files.add(new FileMetaData(f));
		}
	}

	/** Perform the logic on the content of the file */

	public void doIt() {
		future = getExecutorService().submit(() -> {
			for (FileMetaData fileMetaData : files) {
				File file = fileMetaData.getFile();
				try (Stream<JLoggReaderLine> lines = JLoggReader.lines(file.toPath(), StandardCharsets.ISO_8859_1)) {
					// Log + Keep track of when the operation actually started logStart(file);
					long startTS = System.currentTimeMillis();
					logStart(file);

					List<LogLine> logLines = new ArrayList<>();
					lines.forEach((jloggline) -> {
						if (Thread.interrupted()) {
							throw new FileIteratorInterruptedException();
						}

						// Gather the needed information for the LogLine object
						String text = jloggline.lineText();
						int lineNumber = fileMetaData.getCurrentLineNumber();
						long start = fileMetaData.getStart();
						int size = text.length();

						LogLine logline = new LogLine(lineNumber, start, size, file);

						if (shouldAddToTempResult(text, logline)) {
							logLines.add(logline);
						}

						double percentage = fileMetaData.readNewLine(size, jloggline.amountOfDelimiterCharacters());
						if (percentage > 0) {
							submitPercentEvent(file, logLines, percentage);
						}

					});

					logEnd(file, System.currentTimeMillis() - startTS);
					submitFileFinishedEvent(file, logLines);
				} catch (IOException e) {
					handleIOException(e);
				} catch (FileIteratorInterruptedException e) {
					logger.log(Level.INFO, "Execution of fileiterator: " + this + " was interrupted");
				}
			}

			submitFinishedEvent();
			return null;
		});
	}

	protected abstract ExecutorService getExecutorService();

	protected abstract void logStart(File file);

	protected abstract void logEnd(File file, long duration);

	protected abstract void handleIOException(IOException e);

	protected abstract boolean shouldAddToTempResult(String text, LogLine line);

	protected abstract void submitPercentEvent(File file, List<LogLine> lines, double percentage);

	protected abstract void submitFileFinishedEvent(File file, List<LogLine> lines);

	protected void submitFinishedEvent() {

	}

	public void waitFor() {
		try {
			future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Stop executing this iterator
	public void stop() {
		future.cancel(true);
	}

}
