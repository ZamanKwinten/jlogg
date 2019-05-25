package jlogg.ui;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.shared.LogLine;
import jlogg.ui.css.ResourceLoader;
import jlogg.ui.menubar.MenuBarWrapper;

public class MainPane extends VBox {

	private final MenuBar menuBar;
	private final TabPane tabPane;

	public MainPane() {
		getStylesheets().add(ResourceLoader.loadResourceFile("Default.css"));

		menuBar = new MenuBarWrapper(this);
		tabPane = new TabPane();
		// TODO add a default screen

		setVgrow(tabPane, Priority.ALWAYS);

		getChildren().addAll(menuBar, tabPane);
	}

	public void addTab(File file, ObservableList<LogLine> lines) {
		FileTab filetab = new FileTab(file, lines);
		tabPane.getTabs().add(filetab);
		tabPane.getSelectionModel().select(filetab);
	}

	public FileTab getCurrentSelectedTab() {
		return (FileTab) tabPane.getSelectionModel().getSelectedItem();
	}
}
