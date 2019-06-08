package jlogg.eventbus;

import java.util.Arrays;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import jlogg.datahandlers.FileIndexer;
import jlogg.datahandlers.FileSearcher;
import jlogg.shared.LogLine;
import jlogg.ui.GlobalConstants;

public class EventListener {
	private FileSearcher currentSearch;

	@Subscribe
	private void onSearchEvent(SearchEvent searchEvent) {
		if (currentSearch != null) {
			currentSearch.stop();
		}

		GlobalConstants.searchResults.clear();
		GlobalConstants.searchProgress.setValue(0.0);
		currentSearch = new FileSearcher(Arrays.asList(searchEvent.getFile()), searchEvent.getCriteria());
		currentSearch.doIt();
	}

	@Subscribe
	private void onSearchResultEvent(SearchResultEvent searchResult) {
		handleSearchEvent(searchResult);
	}

	@Subscribe
	private void onSearchFinishedEvent(SearchFinishedEvent searchResult) {
		handleSearchEvent(searchResult);
	}

	/**
	 * Generic method to handle any kind of search evenets
	 * 
	 * @param event
	 */
	private void handleSearchEvent(SearchResultEvent event) {
		GlobalConstants.searchResults.addAll(event.getLogLines());
		GlobalConstants.searchProgress.set(event.getPrecentage());
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
