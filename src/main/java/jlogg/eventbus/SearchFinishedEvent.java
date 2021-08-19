package jlogg.eventbus;

import java.io.File;
import java.util.List;

import jlogg.plugin.LogLine;
import jlogg.ui.GlobalConstants;

public class SearchFinishedEvent extends SearchResultEvent {

	public SearchFinishedEvent(SearchEvent searchEvent, File file, List<LogLine> loglines) {
		super(searchEvent, file, loglines, 1.0);
	}

	@Override
	public void setGlobalConstants() {
		super.setGlobalConstants();
		GlobalConstants.multiFileSearchCurrentFilename.set(null);
	}
}
