package jlogg.ui.popup;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import jlogg.ui.GlobalConstants.ShortCut;

public class KeyInputPopup extends PopupWithReturn<KeyCombination> {

	private final Label current;

	private KeyCombination keyCombo;

	public KeyInputPopup(ShortCut key) {
		keyCombo = key.observable().getValue();

		VBox vbox = new VBox();

		Label text = new Label("Please enter the preferred keycombination for " + key.uiName());
		current = new Label();
		setCurrentLabel();

		vbox.getChildren().addAll(text, current);
		content.getChildren().addAll(vbox, getCancelableFooterBox("Save", null));

		content.setOnKeyPressed(event -> {
			switch (event.getCode()) {
			case ALT:
			case SHIFT:
			case CONTROL:
			case SHORTCUT:
			case META:
			case UNDEFINED:
				// ignore these keys
				break;
			default:

				List<KeyCombination.Modifier> modifiers = new ArrayList<>();
				if (event.isAltDown()) {
					modifiers.add(KeyCombination.ALT_DOWN);
				}
				if (event.isShiftDown()) {
					modifiers.add(KeyCombination.SHIFT_DOWN);
				}
				if (event.isShortcutDown()) {
					modifiers.add(KeyCombination.SHORTCUT_DOWN);
				}

				keyCombo = new KeyCodeCombination(event.getCode(), modifiers.toArray(new KeyCombination.Modifier[0]));
				setCurrentLabel();
			}
		});
	}

	public void setCurrentLabel() {
		current.setText("Current key combination: " + keyCombo.getDisplayText());
	}

	@Override
	protected KeyCombination getReturnValue() {
		return keyCombo;
	}

}
