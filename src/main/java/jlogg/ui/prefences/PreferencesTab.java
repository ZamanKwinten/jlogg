package jlogg.ui.prefences;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class PreferencesTab<T extends Node> extends TreeItem<String> {

	private final T node;

	public PreferencesTab(String title, T node) {
		super(title);
		this.node = node;
	}

	public T node() {
		return node;
	}
}
