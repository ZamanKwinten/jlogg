package jlogg.ui.menubar;

import javafx.scene.control.MenuItem;
import jlogg.ui.GlobalConstants.ShortCut;

public class MenuItemWithAccelerator extends MenuItem {

	public MenuItemWithAccelerator(ShortCut key) {
		super(key.uiName());
		acceleratorProperty().bind(key.observable());
	}
}
