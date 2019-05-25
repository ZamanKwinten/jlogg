package jlogg.datahandlers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import jlogg.shared.LogLine;

/**
 * Abstract class usable by any logic that wants to handle all lines of a file
 * exactly once
 * 
 * @author Kwinten
 *
 */
public abstract class FileIterator {

	protected final List<FileMetaData> files;

	public FileIterator(Collection<File> files) {
		this.files = new ArrayList<>();
		for (File f : files) {
			this.files.add(new FileMetaData(f));
		}
	}

	/** Perform the logic on the content of the file */

	public void doIt() {
		getExecutorService().execute(() -> {
			for (FileMetaData fileMetaData : files) {
				File file = fileMetaData.getFile();
				try (Stream<JLoggReaderLine> lines = JLoggReader.lines(file.toPath(), StandardCharsets.ISO_8859_1)) {
					// Log + Keep track of when the operation actually started logStart(file);
					long startTS = System.currentTimeMillis();
					logStart(file);

					List<LogLine> logLines = new ArrayList<>();
					lines.forEach((jloggline) -> {
						// Gather the needed information for the LogLine object
						String text = jloggline.lineText();
						int lineNumber = fileMetaData.getCurrentLineNumber();
						long start = fileMetaData.getStart();
						int size = text.length();

						if (shouldAddToTempResult(text)) {
							logLines.add(new LogLine(lineNumber, start, size, file));
						}
						if (fileMetaData.readNewLine(size, jloggline.amountOfDelimiterCharacters())) {
							submitPercentEvent(file, logLines);
						}

					});

					logEnd(file, System.currentTimeMillis() - startTS);
					submitFinishedEvent(file, logLines);
				} catch (IOException e) {
					handleIOException(e);
				}
			}

		});
	}

	protected abstract ExecutorService getExecutorService();

	protected abstract void logStart(File file);

	protected abstract void logEnd(File file, long duration);

	protected abstract void handleIOException(IOException e);

	protected abstract boolean shouldAddToTempResult(String s);

	protected abstract void submitPercentEvent(File file, List<LogLine> lines);

	protected abstract void submitFinishedEvent(File file, List<LogLine> lines);

}
