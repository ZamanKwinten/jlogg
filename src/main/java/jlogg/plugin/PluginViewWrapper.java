package jlogg.plugin;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jlogg.ui.custom.ResizableView;

public class PluginViewWrapper extends ResizableView {

	private final Map<Class<? extends JLoggPlugin>, PluginView> map = new HashMap<>();

	private PluginView currentView;
	private final StackPane pluginLocation;

	public PluginViewWrapper() {
		pluginLocation = new StackPane();

		VBox.setVgrow(pluginLocation, Priority.ALWAYS);

		getChildren().add(pluginLocation);
	}

	private void setPluginView(PluginView pluginView) {
		pluginLocation.getChildren().setAll(pluginView.getView());
		currentView = pluginView;
	}

	public Node showPlugin(JLoggPlugin plugin) {
		PluginView pluginMain = map.computeIfAbsent(plugin.getClass(), key -> plugin.getMainView());
		setPluginView(pluginMain);
		show();
		return pluginMain.getView();
	}

	public void save() {
		if (currentView != null) {
			currentView.save();
		}
	}
}
