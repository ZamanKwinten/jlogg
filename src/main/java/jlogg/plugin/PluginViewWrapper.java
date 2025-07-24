package jlogg.plugin;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jlogg.PluginWithMetadata;
import jlogg.plugin.loader.PluginUpdater;
import jlogg.ui.GlobalConstants;
import jlogg.ui.custom.ResizableView;
import jlogg.ui.popup.PluginManagerPopup;

public class PluginViewWrapper extends ResizableView {

	private final Map<Class<? extends JLoggPlugin>, PluginView> map = new HashMap<>();

	private PluginView currentView;
	private final StackPane updateAvailableLocation;
	private final StackPane pluginLocation;

	public PluginViewWrapper() {
		pluginLocation = new StackPane();
		updateAvailableLocation = new StackPane();

		VBox.setVgrow(pluginLocation, Priority.ALWAYS);

		getChildren().addAll(updateAvailableLocation, pluginLocation);
	}

	private void setPluginView(PluginView pluginView) {
		pluginLocation.getChildren().setAll(pluginView.getView());
		currentView = pluginView;
	}

	public Node showPlugin(Class<? extends JLoggPlugin> pluginClazz) {

		for (var plugin : GlobalConstants.plugins) {
			if (plugin.plugin().getClass() == pluginClazz) {
				// Intended to have a real address comparison here
				return showPluginWithMetadata(plugin);
			}
		}

		throw new RuntimeException("Unable to Show plugin for class: " + pluginClazz);
	}

	public Node showPluginWithMetadata(PluginWithMetadata pluginWithMetadata) {
		updateAvailableLocation.getChildren().clear();
		var plugin = pluginWithMetadata.plugin();
		PluginView pluginMain = map.computeIfAbsent(plugin.getClass(), key -> plugin.getMainView());
		setPluginView(pluginMain);
		show();

		if (pluginWithMetadata.updateURL() != null) {
			PluginManagerPopup.PLUGIN_SERVER_CALLS.submit(() -> {
				try {
					var manifest = PluginUpdater.getLatestPluginVersion(pluginWithMetadata.updateURL());
					if (PluginUpdater.compatibleUpdate(pluginWithMetadata, manifest)) {
						// Explicit address check is intentional here
						if (map.get(plugin.getClass()) == currentView) {
							Platform.runLater(() -> {
								var updateLabel = new Label("New update available! Click here to update");
								updateLabel.setCursor(Cursor.HAND);
								updateLabel.setStyle("-fx-text-fill: #1a0dab; -fx-underline: true;");

								updateLabel.setOnMouseClicked(event -> {
									PluginUpdater.tryUpdate(pluginWithMetadata).ifPresent(newPlugin -> {
										hide();
										showPluginWithMetadata(newPlugin);
									});
								});

								updateAvailableLocation.getChildren().setAll(updateLabel);
							});
						}
					}
				} catch (Exception e) {
					// log it but ignore it for the end user
					e.printStackTrace();
				}
			});
		}

		return pluginMain.getView();
	}

	public void save() {
		if (currentView != null) {
			currentView.save();
		}
	}
}
