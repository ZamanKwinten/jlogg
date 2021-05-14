package jlogg.ui.custom;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import jlogg.plugin.JLoggPlugin;

public class PluginViewWrapper extends ResizableView {

	private final StackPane pluginLocation;

	public PluginViewWrapper() {
		pluginLocation = new StackPane();

		getChildren().add(pluginLocation);
	}

	private void setPluginView(Node pluginView) {
		pluginLocation.getChildren().setAll(pluginView);
	}

	public void showPlugin(JLoggPlugin plugin) {
		setPluginView(plugin.getMainView());
		show();
	}
}