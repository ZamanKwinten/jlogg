package jlogg.ui.custom;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.shared.LogLine;
import jlogg.ui.FileTab;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainPane;
import jlogg.ui.interfaces.HideableNode;
import jlogg.ui.logview.LogFileView;

public class FilteredView extends VBox implements HideableNode {
	private final SearchRow searchRow;
	private final OptionRow optionRow;
	private final LogFileView filteredView;

	private final Node resizer;

	public FilteredView(MainPane mainPane, FileTab fileTab) {
		searchRow = new SearchRow(mainPane, fileTab, this);
		optionRow = new OptionRow();

		filteredView = new LogFileView(fileTab, GlobalConstants.searchResults, (view, index) -> {
			LogLine line = view.getItems().get(index);
			mainPane.selectLine(line);
		});

		resizer = new Resizer(this);

		setVgrow(filteredView, Priority.ALWAYS);

		getChildren().addAll(resizer, searchRow, optionRow, filteredView);
	}

	public OptionRow getOptionRow() {
		return optionRow;
	}

	public SearchRow getSearchRow() {
		return searchRow;
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
		searchRow.focusSearchText();
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

}
