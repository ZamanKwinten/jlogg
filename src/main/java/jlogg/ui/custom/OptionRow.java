package jlogg.ui.custom;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jlogg.ui.GlobalConstants;

public class OptionRow extends HBox {

	// private final Button filterTypeButton; -> maybe in the future (search vs
	// bookmarks)
	private final ProgressBar progressBar;
	private CheckBox ignoreCaseCheck;
	private CheckBox searchAllFiles;

	// private CheckBox autoRefreshCheck; -> maybe in the future currently no auto
	// refresh support

	public OptionRow() {
		super(5);
		progressBar = new ProgressBar(GlobalConstants.searchProgress, "search-progress");

		ignoreCaseCheck = new CheckBox("Ignore Case");
		ignoreCaseCheck.setSelected(true);
		ignoreCaseCheck.setMinWidth(85);

		// by default off since this will/should open another window (extra click)
		searchAllFiles = new CheckBox("Search all files");
		searchAllFiles.setSelected(false);
		searchAllFiles.setMinWidth(110);

		setHgrow(progressBar, Priority.ALWAYS);
		getStyleClass().add("optionRowPadding");
		setAlignment(Pos.CENTER_LEFT);

		getChildren().addAll(progressBar, ignoreCaseCheck, searchAllFiles);
	}

	public boolean isIgnoreCase() {
		return ignoreCaseCheck.isSelected();
	}

	public boolean isAllFilesSearch() {
		return searchAllFiles.isSelected();
	}
}
