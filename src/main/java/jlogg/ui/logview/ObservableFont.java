package jlogg.ui.logview;

import javafx.beans.value.ObservableValueBase;
import javafx.scene.text.Font;

public class ObservableFont extends ObservableValueBase<Font> {

	private Font font;

	public ObservableFont(Font font) {
		this.font = font;
	}

	@Override
	public Font getValue() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
		fireValueChangedEvent();
	}

}
