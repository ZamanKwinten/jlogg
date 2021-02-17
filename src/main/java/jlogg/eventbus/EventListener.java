package jlogg.eventbus;

import com.google.common.eventbus.Subscribe;

import jlogg.datahandlers.FileIndexer;
import jlogg.datahandlers.FileSearcher;
import jlogg.shared.SearchCriteria;
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
		SearchCriteria criteria = searchEvent.getCriteria();

		GlobalConstants.searchHistory.add(criteria);
		currentSearch = new FileSearcher(searchEvent.getFiles(), criteria);
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
		GlobalConstants.fileIndexProgress.get(event.getFile()).set(0.0);

		new FileIndexer(event.getFile()).doIt();
	}

	@Subscribe
	public void onIndexResultEvent(IndexResultEvent event) {
		handleIndexEvent(event);
	}

	@Subscribe
	public void onIndexDoneEvent(IndexFinishedEvent event) {
		handleIndexEvent(event);
	}

	private void handleIndexEvent(IndexResultEvent event) {
		FileIndexer indexer = event.getFileIndexer();
		if (GlobalConstants.fileLogLines.containsKey(indexer.getFile())) {
			GlobalConstants.fileLogLines.get(indexer.getFile()).addAll(event.getLogLines());
			GlobalConstants.fileIndexProgress.get(indexer.getFile()).set(event.getPercentage());
		} else {
			// stop the indexer! (safety measure)
			indexer.stop();
		}
	}
}
