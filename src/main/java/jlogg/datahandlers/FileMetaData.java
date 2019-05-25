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

	public boolean readNewLine(int size, int delimCount) {
		lineNumber++;
		read += size + delimCount;

		if (read >= currentPercentTarget) {
			currentPercentTarget += percentMark;
			return true;
		} else {
			return false;
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
