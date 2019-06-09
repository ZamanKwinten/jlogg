package jlogg.ui.popup.utils;

import java.io.File;

/**
 * A wrapper around file to add extra properties useable in ui components
 * 
 * @author Kwinten
 *
 */
public class SearchFileWrapper {

	private boolean shouldIncludeInSearch;
	private final File file;

	public SearchFileWrapper(File file) {
		shouldIncludeInSearch = true;
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public boolean shouldIncludeInSearch() {
		return shouldIncludeInSearch;
	}

	public void shouldIncludeInSearch(boolean shouldIncludeInSearch) {
		this.shouldIncludeInSearch = shouldIncludeInSearch;
	}
}
