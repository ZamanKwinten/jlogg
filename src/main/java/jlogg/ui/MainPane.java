package jlogg.ui;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import jlogg.ui.custom.HorizontalResizer;
import jlogg.ui.menubar.MenuBarWrapper;

public class MainPane extends VBox {

	private final Map<FileTab, TreeItem<String>> drilldownSearch;

	private final MenuBar menuBar;
	private final TabPane tabPane;

	private class DrillDownSearchItem extends TreeItem<String> {

		private final FileTab tab;

		public DrillDownSearchItem(String label, FileTab tab) {
			super(label);
			this.tab = tab;
		}
	}

	public MainPane() {
		menuBar = new MenuBarWrapper(this);
		tabPane = new TabPane();
		drilldownSearch = new HashMap<>();

		HBox hbox = new HBox();

		TreeItem<String> dummyRoot = new TreeItem<>();
		dummyRoot.setExpanded(true);
		drilldownSearch.put(null, dummyRoot);

		TreeView<String> tree = new TreeView<>(dummyRoot);
		tree.setShowRoot(false);

		tree.setPrefWidth(0);

		tree.setStyle(getAccessibleHelp());

		HorizontalResizer resizer = new HorizontalResizer(tabPane);

		HBox.setHgrow(tabPane, Priority.ALWAYS);
		HBox.setHgrow(tree, Priority.SOMETIMES);
		hbox.getChildren().addAll(tree, resizer, tabPane);

		setVgrow(hbox, Priority.ALWAYS);
		getChildren().addAll(menuBar, hbox);

		tree.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			if (newV instanceof DrillDownSearchItem) {
				selectTab(((DrillDownSearchItem) newV).tab);
			}
		});
	}

	public void openTabs(List<File> files) {
		boolean first = true;
		for (File file : files) {
			FileTab current = createAndAddFileTab(file, first);

			DrillDownSearchItem searchItem = new DrillDownSearchItem(file.getName(), current);
			searchItem.setExpanded(true);
			drilldownSearch.get(null).getChildren().add(searchItem);
			drilldownSearch.put(current, searchItem);

			EventBusFactory.getInstance().getEventBus().post(new IndexStartEvent(file));
			first = false;
		}
	}

	public void addSearchResultTab(File file, FileTab parentTab) {
		FileTab current = createAndAddFileTab(file, true);
		DrillDownSearchItem searchItem = new DrillDownSearchItem(parentTab.getSearch(), current);
		searchItem.setExpanded(true);
		drilldownSearch.get(parentTab).getChildren().add(searchItem);
		drilldownSearch.put(current, searchItem);
	}

	/**
	 * Add a file as a tab + potentially select the new tab
	 * 
	 * @param file
	 * @param shouldSelect
	 */
	private FileTab createAndAddFileTab(File file, boolean shouldSelect) {
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
		drilldownSearch.get(null).getChildren().clear();
		for (FileTab tab : drilldownSearch.keySet()) {
			drilldownSearch.remove(tab);
		}
	}

	public void selectTab(FileTab tab) {
		tabPane.getSelectionModel().select(tab);
	}
}
