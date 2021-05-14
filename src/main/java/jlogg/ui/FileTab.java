package jlogg.ui;

import java.io.File;
import java.util.Optional;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jlogg.plugin.JLoggPlugin;
import jlogg.plugin.PluginViewWrapper;
import jlogg.shared.LogLine;
import jlogg.ui.custom.MultiFileSearchView;
import jlogg.ui.custom.SingleFileSearchView;
import jlogg.ui.custom.search.ProgressBar;
import jlogg.ui.logview.LogFileView;

public class FileTab extends Tab {

	public class FileTabTreeItem extends TreeItem<String> {

		private final FileTab tab;

		public FileTabTreeItem(String label, FileTab tab) {
			super(label);
			this.tab = tab;
		}

		public FileTab getTab() {
			return tab;
		}
	}

	private final FileTabTreeItem treeItem;

	private final LogFileView mainView;
	private final SingleFileSearchView singleFileSearchView;
	private final MultiFileSearchView multiFileSearchView;
	private final PluginViewWrapper pluginViewWrapper;
	private final ProgressBar progressBar;

	/**
	 * Keep track of the LogFileView on which the last selection was done => needed
	 * for the selection fetching
	 */
	private LogFileView lastSelection;

	/**
	 * Keep track of the file that is represented by this tab
	 */
	private final File file;

	public FileTab(MainPane mainPane, File file, ObservableList<LogLine> lines, SimpleDoubleProperty progress,
			TreeItem<String> parentInTree) {
		super(file.getName());
		treeItem = new FileTabTreeItem(file.getName(), this);

		parentInTree.getChildren().add(treeItem);

		this.file = file;
		VBox logContent = new VBox();

		progressBar = new ProgressBar(progress);

		mainView = new LogFileView(this, lines);

		singleFileSearchView = new SingleFileSearchView(mainPane, this);
		multiFileSearchView = new MultiFileSearchView(mainPane, this);
		pluginViewWrapper = new PluginViewWrapper();
		// initialize it to something
		lastSelection = mainView;

		VBox.setVgrow(mainView, Priority.ALWAYS);
		VBox.setVgrow(singleFileSearchView, Priority.ALWAYS);
		VBox.setVgrow(multiFileSearchView, Priority.ALWAYS);
		VBox.setVgrow(pluginViewWrapper, Priority.ALWAYS);

		logContent.getChildren().addAll(progressBar, mainView, singleFileSearchView, multiFileSearchView,
				pluginViewWrapper);
		singleFileSearchView.hide();
		multiFileSearchView.hide();
		pluginViewWrapper.hide();

		singleFileSearchView.visibleProperty().addListener((obs, o, n) -> {
			if (n) {
				multiFileSearchView.hide();
				pluginViewWrapper.hide();
			}
		});

		multiFileSearchView.visibleProperty().addListener((obs, o, n) -> {
			if (n) {
				singleFileSearchView.hide();
				pluginViewWrapper.hide();
			}
		});

		pluginViewWrapper.visibleProperty().addListener((obs, o, n) -> {
			if (n) {
				singleFileSearchView.hide();
				multiFileSearchView.hide();
			}
		});

		setContent(logContent);

		setOnClosed((event) -> {
			int i = 0;
			TreeItem<String> parent = treeItem.getParent();
			for (TreeItem<String> item : parent.getChildren()) {
				if (item.equals(treeItem)) {
					break;
				}
				i++;
			}
			parent.getChildren().remove(i);
			if (treeItem.getChildren().size() > 0) {
				parent.getChildren().addAll(i, treeItem.getChildren());
			}
		});

		selectedProperty().addListener((obs, o, n) -> {
			if (n) {
				mainPane.selectTab(this);
			}
		});
	}

	public void setName(String name) {
		setText(name);
		treeItem.setValue(name);
	}

	public void setLastSelection(LogFileView lastSelectionControl) {
		this.lastSelection = lastSelectionControl;
	}

	public Optional<String> getSelection() {
		return lastSelection.getSelection();
	}

	public Optional<String> getSingleLineSelection() {
		return lastSelection.getSingleLineSelection();
	}

	public void showSingleFileSearchView() {
		getSingleLineSelection().ifPresent(string -> singleFileSearchView.setSearchText(string));
		singleFileSearchView.show();
	}

	public void showMultiFileSearchView() {
		getSingleLineSelection().ifPresent(string -> multiFileSearchView.setSearchText(string));
		multiFileSearchView.show();
	}

	public MultiFileSearchView getMultiFileSearchView() {
		return multiFileSearchView;
	}

	public File getFile() {
		return file;
	}

	public String getSearch() {
		return singleFileSearchView.getSearch();
	}

	public void selectLogLine(int index) {
		mainView.scrollTo(index);
		mainView.scrollToColumnIndex(0);
		mainView.getSelectionModel().clearAndSelect(index);
	}

	public LogLine[] getLinesToSave() {
		if (singleFileSearchView.isVisible()) {
			return GlobalConstants.singleFileSearchResults.get(file).toArray(new LogLine[0]);
		} else if (multiFileSearchView.isVisible()) {
			return GlobalConstants.multiFileSearchResults.toArray(new LogLine[0]);
		} else {
			return new LogLine[0];
		}
	}

	public TreeItem<String> getTreeItem() {
		return treeItem;
	}

	public SearchResults getSearchResult() {
		if (singleFileSearchView.isVisible()) {
			return new SearchResults(GlobalConstants.singleFileSearchResults.get(file).toArray(new LogLine[0]), false,
					singleFileSearchView.getSearch());
		} else if (multiFileSearchView.isVisible()) {
			return new SearchResults(GlobalConstants.multiFileSearchResults.toArray(new LogLine[0]), true,
					multiFileSearchView.getSearch());
		} else {
			return new SearchResults(new LogLine[0], false, null);
		}
	}

	public class SearchResults {
		public final boolean isMulti;
		public final LogLine[] lines;
		public final String search;

		public SearchResults(LogLine[] lines, boolean isMulti, String search) {
			this.lines = lines;
			this.isMulti = isMulti;
			this.search = search;
		}
	}

	public void openPlugin(JLoggPlugin plugin) {
		pluginViewWrapper.showPlugin(plugin);
	}

}
