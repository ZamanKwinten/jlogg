package jlogg.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStage extends Stage {

	private final MainPane root;

	public MainStage(String... fileNames) {

		root = new MainPane();

		Scene s = new Scene(root);
		setScene(s);
		setMaximized(true);

		if (fileNames.length == 0) {
			setTitle("jlogg - Untitled");
		} else {
			if (fileNames.length == 1) {
				// do something
			} else {
				// show tabular file browser
			}
		}
	}

	public MainPane getMainPane() {
		return root;
	}
}
