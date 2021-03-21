package jlogg.ui;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.IndexStartEvent;
import jlogg.ui.menubar.MenuBarWrapper;

public class MainPane extends VBox {

	private final MenuBar menuBar;
	private final TabPane tabPane;

	public MainPane() {
		menuBar = new MenuBarWrapper(this);
		tabPane = new TabPane();

		setVgrow(tabPane, Priority.ALWAYS);

		getChildren().addAll(menuBar, tabPane);
	}

	/**
	 * Add a list of files as tab, will select the first file added as the active
	 * tab
	 * 
	 * @param files
	 */
	public void addTabs(List<File> files) {
		boolean first = true;
		for (File file : files) {
			addTab(file, first);
			EventBusFactory.getInstance().getEventBus().post(new IndexStartEvent(file));
			first = false;
		}
	}

	/**
	 * Add a tab for the selected file if such a tab doesn't exist yet
	 * 
	 * @param file
	 */
	public void addTab(File file) {
		addTab(file, true);
	}

	/**
	 * Add a file as a tab + potentially select the new tab
	 * 
	 * @param file
	 * @param shouldSelect
	 */
	private void addTab(File file, boolean shouldSelect) {
		// Check to see whether a tab for that file already exists
		FileTab filetab = findFileTab(file).orElse(null);

		if (filetab == null) {
			GlobalConstants.fileLogLines.put(file, FXCollections.observableArrayList());
			GlobalConstants.fileIndexProgress.put(file, new SimpleDoubleProperty(0.0));
			filetab = new FileTab(this, file, GlobalConstants.fileLogLines.get(file),
					GlobalConstants.fileIndexProgress.get(file));
			tabPane.getTabs().add(filetab);
		}
		if (shouldSelect) {
			tabPane.getSelectionModel().select(filetab);
		}
	}

	public void closeCurrentSelectTab() {
		tabPane.getTabs().remove(getCurrentSelectedTab());
	}

	public FileTab getCurrentSelectedTab() {
		return (FileTab) tabPane.getSelectionModel().getSelectedItem();
	}

	public List<FileTab> getFileTabs() {
		return tabPane.getTabs().stream().filter(tab -> tab instanceof FileTab).map(tab -> (FileTab) tab)
				.collect(Collectors.toList());
	}

	public Optional<FileTab> findFileTab(File file) {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			if (tabPane.getTabs().get(i) instanceof FileTab) {
				FileTab fileTab = (FileTab) tabPane.getTabs().get(i);
				if (Objects.equals(file, fileTab.getFile())) {
					return Optional.of(fileTab);
				}
			}
		}
		return Optional.empty();
	}

	public void closeAllTabs() {
		tabPane.getTabs().clear();
	}

	public void selectTab(FileTab tab) {
		tabPane.getSelectionModel().select(tab);
	}
}
