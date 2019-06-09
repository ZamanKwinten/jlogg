package jlogg.eventbus;

import java.io.File;
import java.util.List;

import jlogg.shared.SearchCriteria;

public class SearchEvent {

	private final SearchCriteria criteria;
	private final List<File> files;

	public SearchEvent(List<File> files, SearchCriteria criteria) {
		this.criteria = criteria;
		this.files = files;
	}

	public List<File> getFiles() {
		return files;
	}

	public SearchCriteria getCriteria() {
		return criteria;
	}
}
