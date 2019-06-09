package jlogg.ui;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
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
		// TODO add a default screen

		setVgrow(tabPane, Priority.ALWAYS);

		getChildren().addAll(menuBar, tabPane);
	}

	/**
	 * Add a tab for the selected file if such a tab doesn't exist yet
	 * 
	 * @param file
	 */
	public void addTab(File file) {

		FileTab filetab = null;
		// Check to see whether a tab for that file already exists
		for (Tab tab : tabPane.getTabs()) {
			if (tab instanceof FileTab) {
				FileTab tmp = (FileTab) tab;
				if (Objects.equals(file, tmp.getFile())) {
					filetab = tmp;
				}
			}
		}

		if (filetab == null) {
			GlobalConstants.fileLogLines.put(file, FXCollections.observableArrayList());
			GlobalConstants.fileIndexProgress.put(file, new SimpleDoubleProperty(0.0));
			filetab = new FileTab(this, file, GlobalConstants.fileLogLines.get(file),
					GlobalConstants.fileIndexProgress.get(file));
			tabPane.getTabs().add(filetab);
		}
		tabPane.getSelectionModel().select(filetab);
	}

	public FileTab getCurrentSelectedTab() {
		return (FileTab) tabPane.getSelectionModel().getSelectedItem();
	}

	public List<FileTab> getFileTabs() {
		return tabPane.getTabs().stream().filter(tab -> tab instanceof FileTab).map(tab -> (FileTab) tab)
				.collect(Collectors.toList());
	}
}
