package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class HelpMenu extends Menu {

	private final MenuItem aboutMenuItem;

	public HelpMenu() {
		super("Help");

		aboutMenuItem = new MenuItem("About");
		getItems().addAll(aboutMenuItem);
	}
}
