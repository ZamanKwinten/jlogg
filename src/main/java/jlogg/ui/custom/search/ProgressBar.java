package jlogg.ui.custom.search;

import javafx.beans.property.SimpleDoubleProperty;

public class ProgressBar extends javafx.scene.control.ProgressBar {

	public ProgressBar(SimpleDoubleProperty property) {
		super();
		// Bind the progress to the constant
		progressProperty().bind(property);

		setMaxWidth(Double.MAX_VALUE);
	}
}
