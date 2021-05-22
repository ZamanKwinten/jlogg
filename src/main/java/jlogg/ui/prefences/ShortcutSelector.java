package jlogg.ui.prefences;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import jlogg.ui.GlobalConstants;
import jlogg.ui.GlobalConstants.ShortCut;
import jlogg.ui.popup.KeyInputPopup;

public class ShortcutSelector extends GridPane {

	private final Map<ShortCut, KeyCombination> keyMap;

	public ShortcutSelector() {
		keyMap = new HashMap<>();
		setHgap(15);

		int i = 0;
		for (ShortCut key : GlobalConstants.ShortCut.values()) {
			add(new Label(key.uiName()), 0, i);
			Label keyDisplay = new Label(key.getKeyDisplayText());

			keyDisplay.setOnMouseClicked((event) -> {
				KeyInputPopup popup = new KeyInputPopup(key);
				popup.open().ifPresent(keyCombo -> {
					keyDisplay.setText(keyCombo.getDisplayText());

					keyMap.put(key, keyCombo);
				});
			});

			add(keyDisplay, 1, i);
			i++;
		}
	}

	public Map<ShortCut, KeyCombination> getKeyMap() {
		return keyMap;
	}
}
