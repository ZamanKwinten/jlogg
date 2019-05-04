package jlogg.ui.menubar;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

public class ViewMenu extends Menu {

	private final MenuItem matchesOverviewMenuItem;
	private final MenuItem lineNumberMainMenuItem;
	private final MenuItem lineNumberFilteredMenuItem;
	private final MenuItem followFile;
	private final MenuItem reload;

	public ViewMenu() {
		super("View");

		matchesOverviewMenuItem = new CheckMenuItem("Matches overview");
		lineNumberMainMenuItem = new CheckMenuItem("Line numbers in main view");
		lineNumberFilteredMenuItem = new CheckMenuItem("Line numbers in filtered view");

		followFile = new CheckMenuItem("Follow file");
		followFile.setAccelerator(new KeyCodeCombination(KeyCode.F));

		reload = new MenuItem("Reload");
		reload.setAccelerator(new KeyCodeCombination(KeyCode.F5));

		getItems().addAll(matchesOverviewMenuItem, lineNumberMainMenuItem, lineNumberFilteredMenuItem,
				new SeparatorMenuItem(), followFile, new SeparatorMenuItem(), reload);
	}
}
