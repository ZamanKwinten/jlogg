package jlogg.ui.menubar;

import java.io.File;
import java.util.List;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import jlogg.ui.FileTab;
import jlogg.ui.GlobalConstants.ShortCut;
import jlogg.ui.MainPane;

public class FileMenu extends Menu {
	private final MenuItem openMenuItem;
	private final MenuItem closeMenuItem;
	private final MenuItem closeAllMenuItem;
	private final MenuItem saveAsMenuItem;

	public FileMenu(MainPane mainPane) {
		super("File");
		// Creation of the file menu
		openMenuItem = new MenuItemWithAccelerator(ShortCut.OPEN_FILES);
		openMenuItem.setOnAction((event) -> {

			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(mainPane.getCurrentSelectedTab().getFile().getParentFile());
			fc.setTitle("Open log file");
			List<File> files = fc.showOpenMultipleDialog(null);
			if (files != null) {
				// user selected something => we need to open at least 1 file
				mainPane.openTabs(files);
			}
		});

		closeMenuItem = new MenuItemWithAccelerator(ShortCut.CLOSE_TAB);
		closeMenuItem.setOnAction(event -> {
			mainPane.closeCurrentSelectTab();
		});

		closeAllMenuItem = new MenuItem("Close All");
		closeAllMenuItem.setOnAction(event -> {
			mainPane.closeAllTabs();
		});

		saveAsMenuItem = new MenuItemWithAccelerator(ShortCut.SAVE_SEARCH_RESULTS);
		saveAsMenuItem.setOnAction((event) -> {
			// File must be opened + search must have results
			FileTab currentTab = mainPane.getCurrentSelectedTab();
			if (currentTab != null) {
				currentTab.save();
			}
		});

		getItems().addAll(openMenuItem, closeMenuItem, closeAllMenuItem, new SeparatorMenuItem(), saveAsMenuItem);
	}

}
