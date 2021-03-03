package jlogg.ui.menubar;

import java.util.Optional;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import jlogg.ConstantMgr;
import jlogg.ui.FileTab;
import jlogg.ui.GlobalConstants.ShortCut;
import jlogg.ui.MainPane;
import jlogg.ui.popup.FilterPopup;

public class ToolsMenu extends Menu {

	private final MenuItem filterMenuItem;

	public ToolsMenu(MainPane mainPane) {
		super("Tools");

		filterMenuItem = new MenuItemWithAccelerator(ShortCut.OPEN_FILTERS);
		filterMenuItem.setOnAction((event) -> {
			FileTab filetab = mainPane.getCurrentSelectedTab();

			FilterPopup popup = null;
			if (filetab == null) {
				popup = new FilterPopup(Optional.empty());
			} else {
				popup = new FilterPopup(mainPane.getCurrentSelectedTab().getSingleLineSelection());
			}

			popup.showAndWait();
			ConstantMgr.instance().updateFilters();
		});

		getItems().addAll(filterMenuItem);
	}
}
