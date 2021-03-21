package jlogg.eventbus;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javafx.collections.ObservableList;
import jlogg.shared.LogLine;
import jlogg.shared.SearchCriteria;
import jlogg.ui.GlobalConstants;

public class SingleFileSearchEvent extends SearchEvent {

	private final File file;

	public SingleFileSearchEvent(File file, SearchCriteria criteria) {
		super(Collections.singletonList(file), criteria);
		this.file = file;
	}

	@Override
	public void clearGlobalConstants() {
		ObservableList<LogLine> fileSearch = GlobalConstants.singleFileSearchResults.get(file);
		if (fileSearch != null) {
			fileSearch.clear();
		}
	}

	@Override
	public void setGlobalConstants(List<LogLine> lines) {
		GlobalConstants.singleFileSearchResults.get(file).addAll(lines);
	}

}
