package jlogg.ui.menubar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.IndexStartEvent;
import jlogg.ui.GlobalConstants;
import jlogg.ui.GlobalConstants.ShortCut;
import jlogg.ui.MainPane;

public class FileMenu extends Menu {
	private final Logger logger = Logger.getLogger(FileMenu.class.getName());

	private File previousDir;

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
			fc.setInitialDirectory(previousDir);
			fc.setTitle("Open log file");
			List<File> files = fc.showOpenMultipleDialog(null);
			if (files != null) {
				// user selected something => we need to open at least 1 file
				mainPane.addTabs(files);
				previousDir = files.get(0).getParentFile();
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
			if (mainPane.getCurrentSelectedTab() != null && !GlobalConstants.searchResults.isEmpty()) {
				FileChooser fc = new FileChooser();
				// Probably the most logical thing to try & save it in the same dir as the
				// parent file?
				fc.setInitialDirectory(mainPane.getCurrentSelectedTab().getFile().getParentFile());
				fc.setInitialFileName("jlogg - " + System.currentTimeMillis() + ".log");
				File file = fc.showSaveDialog(null);
				if (file != null) {
					previousDir = file.getParentFile();
					try {
						Files.write(Paths.get(file.getAbsolutePath()), ((Iterable<String>) GlobalConstants.searchResults
								.stream().map(logline -> logline.getLineString())::iterator));

						// File was successfully written => open it
						mainPane.addTab(file);
						EventBusFactory.getInstance().getEventBus().post(new IndexStartEvent(file));

					} catch (IOException e) {
						logger.log(Level.SEVERE, "Error during saving", e);
					}
				}
			}
		});

		getItems().addAll(openMenuItem, closeMenuItem, closeAllMenuItem, new SeparatorMenuItem(), saveAsMenuItem);
	}

}
