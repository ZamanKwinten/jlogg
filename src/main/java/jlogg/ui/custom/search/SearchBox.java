package jlogg.ui.custom.search;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.layout.VBox;
import jlogg.shared.LogLine;
import jlogg.shared.SearchOptions;
import jlogg.ui.custom.FileSearchView;

public abstract class SearchBox extends VBox {

	final FileSearchView parent;
	final OptionRow optionRow;
	final SearchRow searchRow;

	public SearchBox(FileSearchView parent) {
		this.parent = parent;
		optionRow = new OptionRow(this);
		searchRow = new SearchRow(this);

		getChildren().addAll(searchRow, optionRow);
	}

	public void focusSearchText() {
		searchRow.focusSearchText();
	}

	public void setSearchText(String text, SearchOptions searchOptions) {
		searchRow.setSearchText(text);
		optionRow.setSearchOptions(searchOptions);
	}

	void hide() {
		parent.hide();
	}

	public String getSearch() {
		return searchRow.getSearch();
	}

	public SearchOptions getSearchOptions() {
		return optionRow.getSearchOptions();
	}

	ObservableList<LogLine> getSearchResultList() {
		return parent.getSearchResultList();
	}

	public abstract void fireSearch(Event event);
}
