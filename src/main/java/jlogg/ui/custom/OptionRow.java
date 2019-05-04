package jlogg.ui.custom;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class OptionRow extends HBox {

	private final Button filterTypeButton;
	private final ProgressBar progressLabel;
	private CheckBox ignoreCaseCheck;
	private CheckBox autoRefreshCheck;

	public OptionRow() {
		super(5);
		filterTypeButton = new Button();
		progressLabel = new ProgressBar();

		ignoreCaseCheck = new CheckBox("Ignore Case");
		ignoreCaseCheck.setSelected(true);

		autoRefreshCheck = new CheckBox("Auto-refresh");

		setHgrow(progressLabel, Priority.ALWAYS);
		getStyleClass().add("optionRowPadding");
		setAlignment(Pos.CENTER_LEFT);

		getChildren().addAll(filterTypeButton, progressLabel, ignoreCaseCheck, autoRefreshCheck);
	}

	public boolean isIgnoreCase() {
		return ignoreCaseCheck.isSelected();
	}
}
