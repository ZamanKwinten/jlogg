package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import jlogg.ui.MainPane;

public class EditMenu extends Menu {
	private final MenuItem copyMenuItem;
	private final MenuItem selectAllMenuItem;
	private final MenuItem findMenuItem;

	public EditMenu(MainPane mainPane) {
		super("Edit");

		copyMenuItem = new MenuItem("Copy");
		copyMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN));
		copyMenuItem.setOnAction((event) -> {
			mainPane.getCurrentSelectedTab().getSelection().ifPresent((selection) -> {
				final ClipboardContent content = new ClipboardContent();
				content.putString(selection);
				Clipboard.getSystemClipboard().setContent(content);
				event.consume();
			});
		});

		selectAllMenuItem = new MenuItem("Select All");
		selectAllMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCodeCombination.CONTROL_DOWN));

		findMenuItem = new MenuItem("Find...");
		findMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN));
		findMenuItem.setOnAction((event) -> {
			if (mainPane.getCurrentSelectedTab() != null) {
				mainPane.getCurrentSelectedTab().showFilteredView();
			}
		});

		getItems().addAll(copyMenuItem, selectAllMenuItem, new SeparatorMenuItem(), findMenuItem);
	}
}
