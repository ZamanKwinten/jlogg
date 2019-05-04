package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;

public class EncodingMenu extends Menu {

	private final MenuItem autoMenuItem;
	private final MenuItem asciiMenuItem;
	private final MenuItem utf8MenuItem;
	private final ToggleGroup toggleGroup;

	public EncodingMenu() {
		super("Encoding");

		toggleGroup = new ToggleGroup();
		autoMenuItem = new RadioMenuItem("Auto");
		((RadioMenuItem) autoMenuItem).setToggleGroup(toggleGroup);
		((RadioMenuItem) autoMenuItem).setSelected(true);

		asciiMenuItem = new RadioMenuItem("ASCII/ISO-8859-1");
		((RadioMenuItem) asciiMenuItem).setToggleGroup(toggleGroup);

		utf8MenuItem = new RadioMenuItem("UTF-8");
		((RadioMenuItem) utf8MenuItem).setToggleGroup(toggleGroup);

		getItems().addAll(autoMenuItem, new SeparatorMenuItem(), asciiMenuItem, utf8MenuItem);
	}
}
