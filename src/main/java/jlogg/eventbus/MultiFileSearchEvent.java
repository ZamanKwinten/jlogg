package jlogg.eventbus;

import java.io.File;
import java.util.List;

import jlogg.shared.LogLine;
import jlogg.shared.SearchCriteria;
import jlogg.ui.GlobalConstants;

public class MultiFileSearchEvent extends SearchEvent {

	public MultiFileSearchEvent(List<File> files, SearchCriteria criteria) {
		super(files, criteria);
	}

	@Override
	public void clearGlobalConstants() {
		GlobalConstants.multiFileSearchResults.clear();
	}

	@Override
	public void setGlobalConstants(List<LogLine> lines) {
		GlobalConstants.multiFileSearchResults.addAll(lines);
	}

}
