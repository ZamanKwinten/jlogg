package jlogg.ui.custom;

import javafx.collections.FXCollections;
import javafx.event.Event;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.SingleFileSearchEvent;
import jlogg.shared.LogLine;
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
			public void fireSearch(Event event) {
				EventBusFactory.getInstance().getEventBus().post(new SingleFileSearchEvent(fileTab.getFile(),
						new SearchCriteria(getSearch(), getSearchOptions())));
			}
		};
	}

	@Override
	protected LogFileView initLogFileView(MainPane mainPane, FileTab fileTab) {
		return new LogFileView(fileTab, this.searchResults, (view, index) -> {
			LogLine line = view.getItems().get(index);
			fileTab.selectLogLine(line.getLineNumber());
		});
	}

}
