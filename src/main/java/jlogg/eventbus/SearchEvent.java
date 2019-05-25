package jlogg.eventbus;

import java.io.File;

import jlogg.shared.SearchCriteria;

public class SearchEvent {

	private final SearchCriteria criteria;
	private final File file;

	public SearchEvent(File file, SearchCriteria criteria) {
		this.criteria = criteria;
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public SearchCriteria getCriteria() {
		return criteria;
	}
}
