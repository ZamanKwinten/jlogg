package jlogg.ui.menubar;

import java.util.Optional;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import jlogg.ConstantMgr;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.popup.FilterPopup;

public class ToolsMenu extends Menu {

	private final MenuItem filterMenuItem;
	private final MenuItem optionMenuItem;

	public ToolsMenu(MainPane mainPane) {
		super("Tools");

		filterMenuItem = new MenuItem("Filters...");
		filterMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCodeCombination.CONTROL_DOWN));
		filterMenuItem.setOnAction((event) -> {
			FileTab filetab = mainPane.getCurrentSelectedTab();

			FilterPopup popup = null;
			if (filetab == null) {
				popup = new FilterPopup(Optional.empty());
			} else {
				popup = new FilterPopup(mainPane.getCurrentSelectedTab().getSingleLineSelection());
			}

			popup.showAndWait();
			ConstantMgr.instance().updateConfigFile();
		});

		optionMenuItem = new MenuItem("Options...");
		getItems().addAll(filterMenuItem, new SeparatorMenuItem(), optionMenuItem);
	}
}
