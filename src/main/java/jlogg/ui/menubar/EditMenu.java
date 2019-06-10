package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainPane;
import jlogg.ui.popup.GoToPopup;

public class EditMenu extends Menu {
	private final MenuItem copyMenuItem;
	private final MenuItem selectAllMenuItem;
	private final MenuItem findMenuItem;
	private final MenuItem goToMenuItem;

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

		goToMenuItem = new MenuItem("Go To Line...");
		goToMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCodeCombination.CONTROL_DOWN));
		goToMenuItem.setOnAction((event) -> {
			if (mainPane.getCurrentSelectedTab() != null) {

				new GoToPopup(GlobalConstants.fileLogLines.get(mainPane.getCurrentSelectedTab().getFile()).size() - 1)
						.open().ifPresent(index -> {
							mainPane.getCurrentSelectedTab().selectLogLine(index);
						});
			}
		});

		getItems().addAll(copyMenuItem, selectAllMenuItem, new SeparatorMenuItem(), findMenuItem, goToMenuItem);
	}
}
