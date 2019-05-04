package jlogg.ui;

import java.util.Optional;

import aaatemporary.LoremIpsum;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.ui.css.ResourceLoader;
import jlogg.ui.custom.FilteredView;
import jlogg.ui.logview.LogFileView;
import jlogg.ui.menubar.MenuBarWrapper;

public class MainPane extends VBox {

	private final MenuBar menuBar;
	private final LogFileView mainView;
	private final FilteredView filteredView;

	/**
	 * Keep track of the LogFileView on which the last selection was done => needed
	 * for the selection fetching
	 */
	private LogFileView lastSelection;

	public MainPane() {
		getStylesheets().add(ResourceLoader.loadResourceFile("Default.css"));

		menuBar = new MenuBarWrapper(this);
		mainView = new LogFileView(this, LoremIpsum.getLogLines());

		filteredView = new FilteredView(this);
		// initialize it to something
		lastSelection = mainView;

		getChildren().addAll(menuBar, mainView, filteredView);
		setVgrow(mainView, Priority.ALWAYS);
		setVgrow(filteredView, Priority.ALWAYS);
		filteredView.hide();
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
