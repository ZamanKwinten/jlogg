package jlogg.ui;

import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.ui.css.ResourceLoader;
import jlogg.ui.menubar.MenuBarWrapper;

public class MainPane extends VBox {

	private final MenuBar menuBar;
	private final TabPane tabPane;

	public MainPane() {
		getStylesheets().add(ResourceLoader.loadResourceFile("Default.css"));

		menuBar = new MenuBarWrapper(this);
		tabPane = new TabPane();
		addTab("default");

		setVgrow(tabPane, Priority.ALWAYS);

		getChildren().addAll(menuBar, tabPane);
	}

	public void addTab(String title) {
		tabPane.getTabs().add(new FileTab(title));
	}

	public FileTab getCurrentSelectedTab() {
		return (FileTab) tabPane.getSelectionModel().getSelectedItem();
	}
}
