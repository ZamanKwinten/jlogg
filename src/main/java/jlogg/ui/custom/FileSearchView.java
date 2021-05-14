package jlogg.ui.custom;

import javafx.collections.ObservableList;
import javafx.scene.layout.Priority;
import jlogg.plugin.LogLine;
import jlogg.shared.SearchOptions;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.custom.search.SearchBox;
import jlogg.ui.logview.LogFileView;

public abstract class FileSearchView extends ResizableView {
	private final SearchBox searchBox;
	private final LogFileView logfileView;

	protected final ObservableList<LogLine> searchResults;

	public FileSearchView(MainPane mainPane, FileTab fileTab, ObservableList<LogLine> searchResults) {
		super();
		this.searchResults = searchResults;

		searchBox = initSearchBox(mainPane, fileTab);
		logfileView = initLogFileView(mainPane, fileTab);

		setVgrow(logfileView, Priority.ALWAYS);

		getChildren().addAll(searchBox, logfileView);
	}

	protected abstract SearchBox initSearchBox(MainPane mainPane, FileTab fileTab);

	protected abstract LogFileView initLogFileView(MainPane mainPane, FileTab fileTab);

	public ObservableList<LogLine> getSearchResultList() {
		return searchResults;
	}

	@Override
	public void show() {
		super.show();
		searchBox.focusSearchText();
	}

	public void setSearchText(String text) {
		searchBox.setSearchText(text, new SearchOptions(true));
	}

	public void setSearch(String text, SearchOptions options) {
		searchBox.setSearchText(text, options);
	}

	public String getSearch() {
		return searchBox.getSearch();
	}

	public SearchOptions getSearchOptions() {
		return searchBox.getSearchOptions();
	}

	public void scrollTo(int index) {
		logfileView.scrollTo(index);
		logfileView.scrollToColumnIndex(0);
		logfileView.getSelectionModel().clearAndSelect(index);
	}
}
