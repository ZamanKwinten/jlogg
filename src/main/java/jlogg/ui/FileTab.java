package jlogg.ui;

import java.io.File;
import java.util.Optional;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.shared.LogLine;
import jlogg.ui.custom.MultiFileSearchView;
import jlogg.ui.custom.SingleFileSearchView;
import jlogg.ui.custom.search.ProgressBar;
import jlogg.ui.logview.LogFileView;

public class FileTab extends Tab {

	private final LogFileView mainView;
	private final SingleFileSearchView singleFileSearchView;
	private final MultiFileSearchView multiFileSearchView;
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

		progressBar = new ProgressBar(progress);

		mainView = new LogFileView(this, lines);

		singleFileSearchView = new SingleFileSearchView(mainPane, this);
		multiFileSearchView = new MultiFileSearchView(mainPane, this);
		// initialize it to something
		lastSelection = mainView;

		VBox.setVgrow(mainView, Priority.ALWAYS);
		VBox.setVgrow(singleFileSearchView, Priority.ALWAYS);
		VBox.setVgrow(multiFileSearchView, Priority.ALWAYS);

		content.getChildren().addAll(progressBar, mainView, singleFileSearchView, multiFileSearchView);
		singleFileSearchView.hide();
		multiFileSearchView.hide();

		singleFileSearchView.visibleProperty().addListener((obs, o, n) -> {
			if (n) {
				multiFileSearchView.hide();
			}
		});

		multiFileSearchView.visibleProperty().addListener((obs, o, n) -> {
			if (n) {
				singleFileSearchView.hide();
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

	public void showSingleFileSearchView() {
		getSingleLineSelection().ifPresent(string -> singleFileSearchView.setSearchText(string));
		singleFileSearchView.show();
	}

	public void showMultiFileSearchView() {
		getSingleLineSelection().ifPresent(string -> multiFileSearchView.setSearchText(string));
		multiFileSearchView.show();
	}

	public MultiFileSearchView getMultiFileSearchView() {
		return multiFileSearchView;
	}

	public File getFile() {
		return file;
	}

	public String getSearch() {
		return singleFileSearchView.getSearch();
	}

	public void selectLogLine(int index) {
		mainView.scrollTo(index);
		mainView.getSelectionModel().clearAndSelect(index);
	}

	public LogLine[] getLinesToSave() {
		if (singleFileSearchView.isVisible()) {
			return GlobalConstants.singleFileSearchResults.get(file).toArray(new LogLine[0]);
		} else if (multiFileSearchView.isVisible()) {
			return GlobalConstants.multiFileSearchResults.toArray(new LogLine[0]);
		} else {
			return new LogLine[0];
		}
	}
}
