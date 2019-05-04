package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class FileMenu extends Menu {

	private final MenuItem openMenuItem;
	private final MenuItem closeMenuItem;
	private final MenuItem closeAllMenuItem;
	private final MenuItem exitMenuItem;

	public FileMenu() {
		super("File");
		// Creation of the file menu
		openMenuItem = new MenuItem("Open...");
		openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		openMenuItem.setOnAction((event) -> {
			System.out.println("Clicked");
		});

		closeMenuItem = new MenuItem("Close");
		closeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));

		closeAllMenuItem = new MenuItem("Close All");

		exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

		getItems().addAll(openMenuItem, closeMenuItem, closeAllMenuItem, new SeparatorMenuItem(), exitMenuItem);
	}

}
