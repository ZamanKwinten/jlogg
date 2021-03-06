package jlogg.ui.menubar;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import jlogg.plugin.JLoggPlugin;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainPane;

public class PluginsMenu extends Menu implements InvalidationListener {

	private final MainPane mainPane;

	public PluginsMenu(MainPane mainPane) {
		super("Plugins");
		this.mainPane = mainPane;

		load();
		GlobalConstants.plugins.addListener(this);
	}

	private void load() {
		List<MenuItem> menuItems = new ArrayList<>();
		for (JLoggPlugin plugin : GlobalConstants.plugins.sorted((a, b) -> a.getName().compareTo(b.getName()))) {
			MenuItem pluginItem = new MenuItem(plugin.getName());

			pluginItem.setOnAction(event -> {
				mainPane.getCurrentSelectedTab().openPlugin(plugin);
			});

			plugin.getCSSStylesheet().ifPresent(mainPane.getStylesheets()::add);

			menuItems.add(pluginItem);
		}
		getItems().setAll(menuItems);
		setVisible(GlobalConstants.plugins.size() > 0);
	}

	@Override
	public void invalidated(Observable observable) {
		load();
	}
}