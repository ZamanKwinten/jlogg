package jlogg.ui.custom;

import jlogg.ui.GlobalConstants;

public class ProgressBar extends javafx.scene.control.ProgressBar {

	public ProgressBar() {
		super();
		// Bind the progress to the constant
		progressProperty().bind(GlobalConstants.searchProgress);

		setMaxWidth(Double.MAX_VALUE);
	}
}
