package jlogg.ui.custom;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.shared.LogLine;
import jlogg.ui.FileTab;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainPane;
import jlogg.ui.custom.search.SearchBox;
import jlogg.ui.interfaces.HideableNode;
import jlogg.ui.logview.LogFileView;

public class FilteredView extends VBox implements HideableNode {
	private final SearchBox searchBox;
	private final LogFileView filteredView;

	private final Node resizer;

	public FilteredView(MainPane mainPane, FileTab fileTab) {
		searchBox = new SearchBox(mainPane, fileTab, this);

		filteredView = new LogFileView(fileTab, GlobalConstants.searchResults, (view, index) -> {
			LogLine line = view.getItems().get(index);
			mainPane.selectLine(line);
		});

		resizer = new Resizer(this);

		setVgrow(filteredView, Priority.ALWAYS);

		getChildren().addAll(resizer, searchBox, filteredView);
	}

	public LogFileView getLogFileView() {
		return filteredView;
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
		searchBox.setSearchText(text);
	}

	public String getSearch() {
		return searchBox.getSearch();
	}

}
