package jlogg.ui.custom;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.scene.layout.Priority;
import jlogg.eventbus.EventBus;
import jlogg.eventbus.IndexStartEvent;
import jlogg.plugin.JLogg;
import jlogg.plugin.LogLine;
import jlogg.shared.SearchOptions;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.custom.search.SearchBox;
import jlogg.ui.logview.LogFileView;

public abstract class FileSearchView extends ResizableView {
	private final SearchBox searchBox;
	private final LogFileView logfileView;

	protected final MainPane mainPane;
	protected final FileTab fileTab;

	protected final ObservableList<LogLine> searchResults;

	public FileSearchView(MainPane mainPane, FileTab fileTab, ObservableList<LogLine> searchResults) {
		super();
		this.mainPane = mainPane;
		this.fileTab = fileTab;
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

	public void save() {
		if (!searchResults.isEmpty()) {
			String search = getSearch();

			JLogg.saveLogLinesToFile(getFileName(search), searchResults.toArray(new LogLine[0])).ifPresent(file -> {
				addSearchResultTab(file, search, fileTab);

				EventBus.get().submit(new IndexStartEvent(file));
			});
		}
	}

	protected abstract void addSearchResultTab(File file, String search, FileTab currentTab);

	private String getFileName(String search) {
		String name = search.replaceAll("\\W+", "");
		if (name.length() == 0) {
			return "jlogg" + System.currentTimeMillis() + ".log";
		}
		return name + ".log";
	}
}
