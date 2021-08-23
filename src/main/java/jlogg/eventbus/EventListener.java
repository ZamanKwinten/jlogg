package jlogg.eventbus;

import jlogg.datahandlers.FileIndexer;
import jlogg.datahandlers.FileSearcher;
import jlogg.shared.SearchCriteria;
import jlogg.ui.GlobalConstants;

public class EventListener implements JLoggEventListener {
	private FileSearcher currentSearch;

	@Override
	public void on(SearchEvent event) {
		if (currentSearch != null) {
			currentSearch.stop();
		}

		event.clearGlobalConstants();
		GlobalConstants.searchProgress.setValue(0.0);
		GlobalConstants.multiFileSearchCurrentFilename.setValue(null);
		SearchCriteria criteria = event.getCriteria();

		GlobalConstants.searchHistory.add(criteria);
		currentSearch = new FileSearcher(event);
		currentSearch.doIt();
	}

	@Override
	public void on(SearchResultEvent event) {
		handleSearchEvent(event);
	}

	/**
	 * Generic method to handle any kind of search evenets
	 * 
	 * @param event
	 */
	private void handleSearchEvent(SearchResultEvent event) {
		event.setGlobalConstants();
		GlobalConstants.searchProgress.set(event.getPrecentage());
	}

	@Override
	public void on(IndexStartEvent event) {
		GlobalConstants.fileLogLines.get(event.getFile()).clear();
		GlobalConstants.fileIndexProgress.get(event.getFile()).set(0.0);

		new FileIndexer(event.getFile()).doIt();
	}

	@Override
	public void on(IndexResultEvent event) {
		handleIndexEvent(event);
	}

	@Override
	public void on(IndexFinishedEvent event) {
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
