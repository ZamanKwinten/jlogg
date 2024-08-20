package jlogg.ui.menubar;

import java.io.InputStream;
import java.util.ArrayList;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import jlogg.ConstantMgr;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainPane;
import jlogg.ui.popup.PluginManagerPopup;

public class PluginsMenu extends Menu implements InvalidationListener {

	private final MainPane mainPane;

	public PluginsMenu(MainPane mainPane) {
		super("Plugins");
		this.mainPane = mainPane;

		load();
		GlobalConstants.plugins.addListener(this);
	}

	private void load() {
		var menuItems = new ArrayList<MenuItem>();

		var pluginManagerItem = new MenuItem("Manage Plugins...");
		pluginManagerItem.setOnAction(event -> {
			var popup = new PluginManagerPopup();

			popup.open().ifPresent(result -> {

			});
		});
		menuItems.add(pluginManagerItem);

		if (!GlobalConstants.plugins.isEmpty()) {
			menuItems.add(new SeparatorMenuItem());
		}

		for (var pluginMetaData : GlobalConstants.sortedPlugins()) {
			MenuItem pluginItem = new MenuItem(pluginMetaData.name());

			var plugin = pluginMetaData.plugin();
			pluginItem.setOnAction(event -> {
				mainPane.getCurrentSelectedTab().openPlugin(plugin);
			});

			plugin.getCSSStylesheet().ifPresent(cssStream -> {
				try (InputStream is = cssStream) {
					mainPane.getStylesheets().add(ConstantMgr.instance().writePluginCSS(pluginMetaData, is)
							.getAbsoluteFile().toURI().toURL().toExternalForm());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			menuItems.add(pluginItem);
		}

		getItems().setAll(menuItems);
	}

	@Override
	public void invalidated(Observable observable) {
		load();
	}
}