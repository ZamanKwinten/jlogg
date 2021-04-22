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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.IndexStartEvent;
import jlogg.ui.FileTab.FileTabTreeItem;
import jlogg.ui.custom.HorizontalResizer;
import jlogg.ui.menubar.MenuBarWrapper;

public class MainPane extends VBox {
	private final MenuBar menuBar;
	private final TabPane tabPane;
	private final TreeView<String> treeView;
	private final TreeItem<String> rootItem;
	private final TreeItem<String> multiSearchItem;

	public MainPane() {
		menuBar = new MenuBarWrapper(this);
		tabPane = new TabPane();

		HBox hbox = new HBox();

		rootItem = new TreeItem<>();
		rootItem.setExpanded(true);

		multiSearchItem = new TreeItem<>("Searches in Multipe Files");
		multiSearchItem.setExpanded(true);
		rootItem.getChildren().add(multiSearchItem);

		treeView = new TreeView<>(rootItem);
		treeView.setShowRoot(false);

		treeView.setPrefWidth(0);

		treeView.setStyle(getAccessibleHelp());

		HorizontalResizer resizer = new HorizontalResizer(tabPane);

		HBox.setHgrow(tabPane, Priority.ALWAYS);
		HBox.setHgrow(treeView, Priority.SOMETIMES);
		hbox.getChildren().addAll(treeView, resizer, tabPane);

		setVgrow(hbox, Priority.ALWAYS);
		getChildren().addAll(menuBar, hbox);

		treeView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
			if (n instanceof FileTabTreeItem) {
				selectTab(((FileTabTreeItem) n).getTab());
			}
		});
	}

	public void openTabs(List<File> files) {
		boolean first = true;
		for (File file : files) {
			createAndAddFileTab(file, first, rootItem);

			EventBusFactory.getInstance().getEventBus().post(new IndexStartEvent(file));
			first = false;
		}
	}

	public void addMultiFileSearchResultTab(File file, String search) {
		FileTab current = createAndAddFileTab(file, true, multiSearchItem);
		current.setName(search);
	}

	public void addSingleFileSearchResultTab(File file, String search, FileTab parentTab) {
		FileTab current = createAndAddFileTab(file, true, parentTab.getTreeItem());
		current.setName(search);
	}

	/**
	 * Add a file as a tab + potentially select the new tab
	 * 
	 * @param file
	 * @param shouldSelect
	 */
	private FileTab createAndAddFileTab(File file, boolean shouldSelect, TreeItem<String> parentTreeItem) {
		// Check to see whether a tab for that file already exists
		FileTab filetab = findFileTab(file).orElse(null);

		if (filetab == null) {
			GlobalConstants.fileLogLines.put(file, FXCollections.observableArrayList());
			GlobalConstants.fileIndexProgress.put(file, new SimpleDoubleProperty(0.0));
			filetab = new FileTab(this, file, GlobalConstants.fileLogLines.get(file),
					GlobalConstants.fileIndexProgress.get(file), parentTreeItem);
			tabPane.getTabs().add(filetab);
		}
		if (shouldSelect) {
			tabPane.getSelectionModel().select(filetab);
		}
		return filetab;
	}

	public void closeCurrentSelectTab() {
		FileTab tab = getCurrentSelectedTab();
		tabPane.getTabs().remove(tab);
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
		rootItem.getChildren().clear();
		rootItem.getChildren().add(multiSearchItem);
	}

	public void selectTab(FileTab tab) {
		tabPane.getSelectionModel().select(tab);
		treeView.getSelectionModel().select(tab.getTreeItem());
	}
}
