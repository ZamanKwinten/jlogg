package jlogg.ui.custom;

import javafx.beans.property.SimpleDoubleProperty;

public class ProgressBar extends javafx.scene.control.ProgressBar {

	public ProgressBar(SimpleDoubleProperty property, String cssClass) {
		super();
		// Bind the progress to the constant
		progressProperty().bind(property);

		setMaxWidth(Double.MAX_VALUE);

		getStyleClass().add(cssClass);
	}
}
