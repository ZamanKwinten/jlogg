package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import jlogg.ui.MainPane;

public class MenuBarWrapper extends MenuBar {

	private final Menu fileMenu;
	private final Menu editMenu;
	private final Menu toolsMenu;
	private final Menu settingsMenu;
	private final Menu pluginsMenu;

	public MenuBarWrapper(MainPane mainPane) {
		fileMenu = new FileMenu(mainPane);
		editMenu = new EditMenu(mainPane);
		toolsMenu = new ToolsMenu(mainPane);
		settingsMenu = new SettingsMenu(mainPane);
		pluginsMenu = new PluginsMenu(mainPane);

		getMenus().addAll(fileMenu, editMenu, toolsMenu, settingsMenu, pluginsMenu);
	}
}
