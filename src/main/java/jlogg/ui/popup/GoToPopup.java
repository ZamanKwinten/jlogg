package jlogg.ui.popup;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GoToPopup extends PopupWithReturn<Integer> {

	private Integer value;

	public GoToPopup(int maxLine) {
		setTitle("Go to Line");

		Label l = new Label("Enter line number(0.." + maxLine + ")");

		TextField input = new TextField();
		Label errorLabel = new Label("");

		SimpleBooleanProperty actionDisabled = new SimpleBooleanProperty(false);
		input.textProperty().addListener((val, oldVal, newVal) -> {
			try {
				value = Integer.parseInt(newVal);
				if (value < 0 || value > maxLine) {
					actionDisabled.set(true);
					errorLabel.setText("Not a number");
				} else {
					actionDisabled.set(false);
					errorLabel.setText("");
				}
			} catch (NumberFormatException e) {
				actionDisabled.set(true);
				errorLabel.setText("Not a number");
			}
		});

		content.getChildren().addAll(l, input, errorLabel, getCancelableFooterBox("Go to Line", actionDisabled));

		setMinWidth(250);
		setResizable(false);
	}

	@Override
	public Integer getReturnValue() {
		return value;
	}
}
