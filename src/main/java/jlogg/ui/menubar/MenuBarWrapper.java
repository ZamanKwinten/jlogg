package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import jlogg.ui.MainPane;

public class MenuBarWrapper extends MenuBar {

	private final Menu fileMenu;
	private final Menu editMenu;
	private final Menu viewMenu;
	private final Menu toolsMenu;
	private final Menu encodingMenu;
	private final Menu helpMenu;

	public MenuBarWrapper(MainPane mainPane) {
		fileMenu = new FileMenu(mainPane);
		editMenu = new EditMenu(mainPane);
		viewMenu = new ViewMenu();
		toolsMenu = new ToolsMenu(mainPane);
		encodingMenu = new EncodingMenu();
		helpMenu = new HelpMenu();
		getMenus().addAll(fileMenu, editMenu, viewMenu, toolsMenu, encodingMenu, helpMenu);
	}
}
