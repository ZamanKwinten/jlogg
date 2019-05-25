package jlogg.ui.menubar;

import java.io.File;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.IndexStartEvent;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainPane;

public class FileMenu extends Menu {

	private final MenuItem openMenuItem;
	private final MenuItem closeMenuItem;
	private final MenuItem closeAllMenuItem;
	private final MenuItem exitMenuItem;

	public FileMenu(MainPane mainPane) {
		super("File");
		// Creation of the file menu
		openMenuItem = new MenuItem("Open...");
		openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		openMenuItem.setOnAction((event) -> {
			FileChooser fc = new FileChooser();
			fc.setTitle("Open log file");
			List<File> files = fc.showOpenMultipleDialog(null);
			if (files != null) {
				// user selected something => we need to open at least 1 file
				for (File file : files) {
					GlobalConstants.fileLogLines.put(file, FXCollections.observableArrayList());
					EventBusFactory.getInstance().getEventBus().post(new IndexStartEvent(file));

					mainPane.addTab(file, GlobalConstants.fileLogLines.get(file));
				}
			}
		});

		closeMenuItem = new MenuItem("Close");
		closeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));

		closeAllMenuItem = new MenuItem("Close All");

		exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

		getItems().addAll(openMenuItem, closeMenuItem, closeAllMenuItem, new SeparatorMenuItem(), exitMenuItem);
	}

}
