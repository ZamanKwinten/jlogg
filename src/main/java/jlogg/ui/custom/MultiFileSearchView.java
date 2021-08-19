package jlogg.ui.custom;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Label;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.MultiFileSearchEvent;
import jlogg.plugin.LogLine;
import jlogg.shared.SearchCriteria;
import jlogg.ui.FileTab;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainPane;
import jlogg.ui.custom.search.SearchBox;
import jlogg.ui.logview.LogFileView;
import jlogg.ui.popup.SearchPopup;

public class MultiFileSearchView extends FileSearchView {

	public MultiFileSearchView(MainPane mainPane, FileTab fileTab) {
		super(mainPane, fileTab, GlobalConstants.multiFileSearchResults);
	}

	@Override
	protected SearchBox initSearchBox(MainPane mainPane, FileTab fileTab) {
		return new SearchBox(this) {

			@Override
			protected void fireSearchImpl(Event event) {
				List<FileTab> fileTabs = mainPane.getFileTabs();

				if (fileTabs.size() == 1) {
					EventBusFactory.getInstance().getEventBus()
							.post(new MultiFileSearchEvent(Collections.singletonList(fileTabs.get(0).getFile()),
									new SearchCriteria(getSearch(), getSearchOptions())));
				} else if (fileTabs.size() > 1) {
					SearchPopup popup = new SearchPopup(
							fileTabs.stream().map(fileTab -> fileTab.getFile()).collect(Collectors.toList()));
					Optional<List<File>> result = popup.open();
					if (result.isPresent()) {
						EventBusFactory.getInstance().getEventBus().post(new MultiFileSearchEvent(result.get(),
								new SearchCriteria(getSearch(), getSearchOptions())));
					}
				}
			}

			@Override
			protected Label getCurrentFileSearchFilename() {
				Label currentFileLabel = new Label();
				GlobalConstants.multiFileSearchCurrentFilename.addListener((obs, ov, nv) -> {
					Platform.runLater(() -> {
						if (nv == null) {
							currentFileLabel.setText("");
						} else {
							currentFileLabel.setText(nv);
						}
					});
				});
				return currentFileLabel;
			}
		};
	}

	@Override
	protected LogFileView initLogFileView(MainPane mainPane, FileTab fileTab) {
		return new LogFileView(fileTab, this.searchResults, (line) -> {

			mainPane.findFileTab(line.getFile()).ifPresent(targetTab -> {
				if (!Objects.equals(mainPane.getCurrentSelectedTab(), targetTab)) {
					mainPane.selectTab(targetTab);
					targetTab.selectLogLine(line.getLineNumber());

					MultiFileSearchView targetSearchView = targetTab.getMultiFileSearchView();
					targetSearchView.show();

					int index = 0;
					for (LogLine ll : this.searchResults) {
						if (Objects.equals(line, ll)) {
							break;
						}
						index++;
					}
					targetSearchView.scrollTo(index);

					targetSearchView.setSearch(this.getSearch(), this.getSearchOptions());
				} else {
					targetTab.selectLogLine(line.getLineNumber());
				}
			});
		});
	}

}
