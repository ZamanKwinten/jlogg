package jlogg.ui.custom;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.shared.LogLine;
import jlogg.shared.SearchOptions;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.custom.search.SearchBox;
import jlogg.ui.interfaces.HideableNode;
import jlogg.ui.logview.LogFileView;

public abstract class FileSearchView extends VBox implements HideableNode {
	private final SearchBox searchBox;
	private final LogFileView logfileView;

	protected final ObservableList<LogLine> searchResults;

	private final Node resizer;

	public FileSearchView(MainPane mainPane, FileTab fileTab, ObservableList<LogLine> searchResults) {
		this.searchResults = searchResults;

		searchBox = initSearchBox(mainPane, fileTab);
		logfileView = initLogFileView(mainPane, fileTab);

		resizer = new Resizer(this);

		setVgrow(logfileView, Priority.ALWAYS);

		getChildren().addAll(resizer, searchBox, logfileView);
	}

	protected abstract SearchBox initSearchBox(MainPane mainPane, FileTab fileTab);

	protected abstract LogFileView initLogFileView(MainPane mainPane, FileTab fileTab);

	public ObservableList<LogLine> getSearchResultList() {
		return searchResults;
	}

	@Override
	public void hide() {
		setVisibility(false);
	}

	@Override
	public void show() {
		setVisibility(true);
		searchBox.focusSearchText();
	}

	private void setVisibility(boolean isVisible) {
		setVisibilityRecursive(this, isVisible);
	}

	private void setVisibilityRecursive(Pane p, boolean isVisible) {
		for (Node n : p.getChildren()) {
			if (n instanceof Pane) {
				setVisibilityRecursive((Pane) n, isVisible);
			} else {
				n.setManaged(isVisible);
				n.setVisible(isVisible);
			}
		}
		p.setManaged(isVisible);
		p.setVisible(isVisible);
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
