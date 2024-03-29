package jlogg.eventbus;

import java.io.File;
import java.util.List;

import jlogg.plugin.LogLine;
import jlogg.shared.SearchCriteria;

public abstract class SearchEvent {

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

	public abstract void clearGlobalConstants();

	public abstract void setGlobalConstants(File currentFile, List<LogLine> lines);
}
