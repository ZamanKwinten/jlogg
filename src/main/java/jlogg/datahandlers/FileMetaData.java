package jlogg.datahandlers;

import java.io.File;

/**
 * Helper class used by FileIndexer to be able to increment line numbers / keep
 * track of already read size
 * 
 * @author Kwinten
 *
 */
public class FileMetaData {

	private int lineNumber;
	private long read;

	private final File file;
	private final long percentMark;
	private long currentPercentTarget;

	public FileMetaData(File file) {
		this.file = file;
		percentMark = file.length() / 100;
		currentPercentTarget = percentMark;
	}

	/**
	 * Will return the percentage value if a threshhold is reached
	 * 
	 * @param size
	 * @param delimCount
	 * @return
	 */
	public double readNewLine(int size, int delimCount) {
		lineNumber++;
		read += size + delimCount;

		if (read >= currentPercentTarget) {
			currentPercentTarget += percentMark;
			// multiply by a double to handle the value as a double
			return 1.0 * read / file.length();
		} else {
			return -1;
		}
	}

	public int getCurrentLineNumber() {
		return lineNumber;
	}

	public long getStart() {
		return read;
	}

	public File getFile() {
		return file;
	}
}
