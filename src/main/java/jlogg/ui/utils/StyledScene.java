package jlogg.ui.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import jlogg.ui.GlobalConstants;
import jlogg.ui.css.ResourceLoader;

public class StyledScene extends Scene {

	public StyledScene(Parent root) {
		super(root);
		getStylesheets().addAll(ResourceLoader.loadResourceFile("color.css"));
		getRoot().setStyle("-fx-base:" + GlobalConstants.theme.getValue().getFXBase());

		GlobalConstants.theme.addListener((obs, o, n) -> {
			getRoot().setStyle("-fx-base:" + n.getFXBase());
		});
	}

}
