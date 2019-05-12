package jlogg.ui;

import java.util.Optional;

import aaatemporary.LoremIpsum;
import javafx.scene.control.Tab;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.ui.custom.FilteredView;
import jlogg.ui.logview.LogFileView;

public class FileTab extends Tab {

	private final LogFileView mainView;
	private final FilteredView filteredView;

	/**
	 * Keep track of the LogFileView on which the last selection was done => needed
	 * for the selection fetching
	 */
	private LogFileView lastSelection;

	public FileTab(String title) {
		super(title);

		VBox content = new VBox();

		mainView = new LogFileView(this, LoremIpsum.getLogLines());

		filteredView = new FilteredView(this);
		// initialize it to something
		lastSelection = mainView;

		VBox.setVgrow(mainView, Priority.ALWAYS);
		VBox.setVgrow(filteredView, Priority.ALWAYS);

		content.getChildren().addAll(mainView, filteredView);
		filteredView.hide();

		setContent(content);
	}

	public void setLastSelection(LogFileView lastSelectionControl) {
		this.lastSelection = lastSelectionControl;
	}

	public Optional<String> getSelection() {
		return lastSelection.getSelection();
	}

	public Optional<String> getSingleLineSelection() {
		return lastSelection.getSingleLineSelection();
	}

	public void showFilteredView() {
		getSingleLineSelection().ifPresent(string -> filteredView.getSearchRow().setSearchText(string));
		filteredView.show();
	}

	public void applyFilters() {
		mainView.refresh();
	}
}
