package jlogg.ui.menubar;

import javafx.beans.value.ObservableValueBase;
import javafx.scene.input.KeyCombination;

public class ObservableKeyCombination extends ObservableValueBase<KeyCombination> {

	private KeyCombination value;

	public ObservableKeyCombination(KeyCombination defaultValue) {
		value = defaultValue;
	}

	@Override
	public KeyCombination getValue() {
		return value;
	}

	public void setValue(KeyCombination value) {
		this.value = value;
		fireValueChangedEvent();
	}

}
