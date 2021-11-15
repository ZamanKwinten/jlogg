package jlogg.ui.custom;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.event.Event;
import jlogg.eventbus.EventBus;
import jlogg.eventbus.SingleFileSearchEvent;
import jlogg.shared.SearchCriteria;
import jlogg.ui.FileTab;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainPane;
import jlogg.ui.custom.search.SearchBox;
import jlogg.ui.logview.LogFileView;

public class SingleFileSearchView extends FileSearchView {

	public SingleFileSearchView(MainPane mainPane, FileTab fileTab) {
		super(mainPane, fileTab, GlobalConstants.singleFileSearchResults.computeIfAbsent(fileTab.getFile(),
				k -> FXCollections.observableArrayList()));
	}

	@Override
	protected SearchBox initSearchBox(MainPane mainPane, FileTab fileTab) {
		return new SearchBox(this) {
			@Override
			protected void fireSearchImpl(Event event) {
				EventBus.get().submit(new SingleFileSearchEvent(fileTab.getFile(),
						new SearchCriteria(getSearch(), getSearchOptions())));
			}
		};
	}

	@Override
	protected LogFileView initLogFileView(MainPane mainPane, FileTab fileTab) {
		return new LogFileView(fileTab, this.searchResults, (line) -> {
			fileTab.selectLogLine(line.getLineNumber());
		});
	}

	@Override
	protected void addSearchResultTab(File file, String search, FileTab currentTab) {
		mainPane.addSingleFileSearchResultTab(file, search, currentTab);
	}
}
