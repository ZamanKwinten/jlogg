package jlogg.ui.utils;

import javafx.beans.value.ObservableValueBase;

public class Observable<T> extends ObservableValueBase<T> {
	private T obs;

	public Observable(T font) {
		this.obs = font;
	}

	@Override
	public T getValue() {
		return obs;
	}

	public void setValue(T font) {
		this.obs = font;
		fireValueChangedEvent();
	}

}
