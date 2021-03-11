package jlogg.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jlogg.ui.css.ResourceLoader;

public class MainStage extends Stage {

	private final MainPane root;

	public MainStage() {
		root = new MainPane();

		Scene s = new Scene(root);
		setScene(s);
		s.getStylesheets().addAll(ResourceLoader.loadResourceFile("Default.css"),
				ResourceLoader.loadResourceFile("color.css"));
		setMaximized(true);

		setTitle("JLogg");
	}

	public MainPane getMainPane() {
		return root;
	}
}
