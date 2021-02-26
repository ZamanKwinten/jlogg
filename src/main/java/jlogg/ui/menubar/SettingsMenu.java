package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import jlogg.ConstantMgr;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainPane;
import jlogg.ui.popup.PreferencesPopup;

public class SettingsMenu extends Menu {
	private final MenuItem filterMenuItem;

	public SettingsMenu(MainPane mainPane) {
		super("Settings");

		filterMenuItem = new MenuItem("Preferences...");
		filterMenuItem.setOnAction((event) -> {
			PreferencesPopup popup = new PreferencesPopup();

			popup.open().ifPresent(preferences -> {
				ConstantMgr.instance().updatePreferences(preferences);

				GlobalConstants.defaultFont.setFont(preferences.font());
			});

		});

		getItems().addAll(filterMenuItem);
	}
}
