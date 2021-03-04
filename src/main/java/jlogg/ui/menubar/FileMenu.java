package jlogg.ui.menubar;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Lists;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import jlogg.datahandlers.FileLineReader;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.IndexStartEvent;
import jlogg.shared.LogLine;
import jlogg.ui.FileTab;
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

				FileTab currentTab = mainPane.getCurrentSelectedTab();

				fc.setInitialDirectory(currentTab.getFile().getParentFile());
				fc.setInitialFileName(getFileName(currentTab.getSearch()));
				File file = fc.showSaveDialog(null);
				if (file != null) {
					previousDir = file.getParentFile();
					try {
						LogLine[] lines = GlobalConstants.searchResults.toArray(new LogLine[0]);

						file.delete();
						file.createNewFile();
						try (FileWriter fw = new FileWriter(file)) {
							for (List<LogLine> linesBatch : Lists.partition(Arrays.asList(lines), 100)) {
								for (String textLines : FileLineReader.readLinesFromFile(linesBatch)) {
									fw.write(textLines);
									fw.write(System.lineSeparator());
								}
							}
						}

						// File was successfully written => open it
						mainPane.addTab(file);
						EventBusFactory.getInstance().getEventBus().post(new IndexStartEvent(file));

					} catch (Exception e) {
						logger.log(Level.SEVERE, "Error during saving", e);
					}
				}
			}
		});

		getItems().addAll(openMenuItem, closeMenuItem, closeAllMenuItem, new SeparatorMenuItem(), saveAsMenuItem);
	}

	private String getFileName(String search) {
		String name = search.replaceAll("\\W+", "");
		if (name.length() == 0) {
			return "jlogg" + System.currentTimeMillis() + ".log";
		}
		return name + ".log";
	}
}
