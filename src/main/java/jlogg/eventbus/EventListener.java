package jlogg.eventbus;

import java.util.Arrays;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import jlogg.datahandlers.FileIndexer;
import jlogg.datahandlers.FileSearcher;
import jlogg.shared.LogLine;
import jlogg.ui.GlobalConstants;

public class EventListener {
	@Subscribe
	private void onSearchEvent(SearchEvent searchEvent) {
		GlobalConstants.searchResults.clear();
		new FileSearcher(Arrays.asList(searchEvent.getFile()), searchEvent.getCriteria()).doIt();
	}

	@Subscribe
	private void onSearchResultEvent(SearchResultEvent searchResult) {
		GlobalConstants.searchResults.addAll(searchResult.getLogLines());
	}

	@Subscribe
	public void onIndexEvent(IndexStartEvent event) {
		GlobalConstants.fileLogLines.get(event.getFile()).clear();
		new FileIndexer(event.getFile()).doIt();
	}

	@Subscribe
	public void onIndexDoneEvent(IndexDoneEvent event) {
		if (GlobalConstants.fileLogLines.containsKey(event.getFile())) {
			ObservableList<LogLine> logLines = GlobalConstants.fileLogLines.get(event.getFile());
			logLines.clear();
			logLines.addAll(event.getLogLines());
		} else {
			// ignore for now
		}
	}
}
