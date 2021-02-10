package jlogg.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStage extends Stage {

	private final MainPane root;

	public MainStage() {
		root = new MainPane();

		Scene s = new Scene(root);
		setScene(s);
		setMaximized(true);
	}

	public MainPane getMainPane() {
		return root;
	}
}
