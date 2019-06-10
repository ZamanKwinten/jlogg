package jlogg.ui;

import java.io.File;
import java.util.Optional;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.shared.LogLine;
import jlogg.ui.custom.FilteredView;
import jlogg.ui.custom.ProgressBar;
import jlogg.ui.logview.LogFileView;

public class FileTab extends Tab {

	private final LogFileView mainView;
	private final FilteredView filteredView;
	private final ProgressBar progressBar;

	/**
	 * Keep track of the LogFileView on which the last selection was done => needed
	 * for the selection fetching
	 */
	private LogFileView lastSelection;

	/**
	 * Keep track of the file that is represented by this tab
	 */
	private final File file;

	public FileTab(MainPane mainPane, File file, ObservableList<LogLine> lines, SimpleDoubleProperty progress) {
		super(file.getName());
		this.file = file;
		VBox content = new VBox();

		progressBar = new ProgressBar(progress, "index-progress");

		mainView = new LogFileView(this, lines);

		filteredView = new FilteredView(mainPane, this);
		// initialize it to something
		lastSelection = mainView;

		VBox.setVgrow(mainView, Priority.ALWAYS);
		VBox.setVgrow(filteredView, Priority.ALWAYS);

		content.getChildren().addAll(progressBar, mainView, filteredView);
		filteredView.hide();

		selectedProperty().addListener((event) -> {
			if (!GlobalConstants.searchResults.isEmpty()) {
				// TODO this should only display if this file was in the search
				filteredView.show();
			}
		});

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

	public File getFile() {
		return file;
	}

	public void selectLogLine(int index) {
		mainView.scrollTo(index);
		mainView.getSelectionModel().clearAndSelect(index);
	}
}
