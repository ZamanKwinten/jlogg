package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import jlogg.ui.MainPane;

public class ToolsMenu extends Menu {

	private final MenuItem filterMenuItem;
	private final MenuItem optionMenuItem;

	public ToolsMenu(MainPane mainPane) {
		super("Tools");

		filterMenuItem = new MenuItem("Filters...");
		filterMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCodeCombination.CONTROL_DOWN));
		filterMenuItem.setOnAction((event) -> {
			FilterPopup popup = new FilterPopup(mainPane.getSingleLineSelection());
			popup.showAndWait();
		});

		optionMenuItem = new MenuItem("Options...");
		getItems().addAll(filterMenuItem, new SeparatorMenuItem(), optionMenuItem);
	}
}
