package jlogg.ui;

import javafx.scene.Scene;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import jlogg.ui.css.ResourceLoader;

public class MainStage extends Stage {

	public static final MainStage instance = new MainStage();

	public static final MainStage getInstance() {
		return instance;
	}

	private final MainPane root;

	private MainStage() {
		root = new MainPane();

		Scene s = new Scene(root);
		setScene(s);
		s.getStylesheets().addAll(ResourceLoader.loadResourceFile("Default.css"),
				ResourceLoader.loadResourceFile("color.css"), ResourceLoader.loadResourceFile("cellstyles.css"));
		GlobalConstants.theme.addListener((obs, o, n) -> {
			s.getRoot().setStyle("-fx-base:" + n.getFXBase());
		});
		s.getRoot().setStyle("-fx-base:" + GlobalConstants.theme.getValue().getFXBase());

		setMaximized(true);

		setTitle("JLogg");

		root.setOnDragOver((event) -> {
			if (event.getDragboard().hasFiles()) {
				event.acceptTransferModes(TransferMode.LINK);
			}

			event.consume();
		});

		root.setOnDragDropped((event) -> {
			if (event.getDragboard().hasFiles()) {
				var files = event.getDragboard().getFiles();
				files.sort((a, b) -> a.getName().compareTo(b.getName()));
				root.openTabs(files);
			}

			event.setDropCompleted(true);
			event.consume();
		});
	}

	public MainPane getMainPane() {
		return root;
	}
}
