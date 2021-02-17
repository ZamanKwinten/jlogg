package jlogg.ui.custom.search;

import javafx.scene.layout.VBox;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.custom.FilteredView;

public class SearchBox extends VBox {

	final FilteredView parent;
	final OptionRow optionRow;
	final SearchRow searchRow;

	public SearchBox(MainPane mainPane, FileTab filetab, FilteredView parent) {
		this.parent = parent;
		optionRow = new OptionRow();
		searchRow = new SearchRow(mainPane, filetab, this);

		getChildren().addAll(searchRow, optionRow);
	}

	public void focusSearchText() {
		searchRow.focusSearchText();
	}

	public void setSearchText(String text) {
		searchRow.setSearchText(text);
	}

	void hide() {
		parent.hide();
	}

	boolean isIgnoreCase() {
		return optionRow.isIgnoreCase();
	}

}
