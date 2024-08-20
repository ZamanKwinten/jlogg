package jlogg.ui.popup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.animation.PauseTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jlogg.PluginLoader;
import jlogg.PluginLoader.PluginLoadingException;
import jlogg.PluginWithMetadata;
import jlogg.plugin.loader.PluginUpdater;
import jlogg.plugin.loader.PluginUpdater.DownloadException;
import jlogg.ui.GlobalConstants;

public class PluginManagerPopup extends PopupWithReturn<String> implements InvalidationListener {

	private final ScrollPane pluginScroll;

	public PluginManagerPopup() {
		super("Plugins");

		var vbox = new VBox();
		var errorText = new ErrorLabel(vbox);
		errorText.managedProperty().addListener((obs, ov, nv) -> {
			sizeToScene();
		});

		var grid = new GridPane();
		grid.setVgap(5);
		grid.setHgap(5);

		var urlLabel = new Label("Plugin URL:");

		grid.add(urlLabel, 0, 0);
		var urlInput = new TextField();
		GridPane.setHgrow(urlInput, Priority.ALWAYS);
		var addButton = new Button("Add");
		addButton.setDisable(true);

		urlInput.textProperty().addListener((obs, ov, nv) -> {
			if (nv != null && !nv.isBlank()) {
				addButton.setDisable(false);
			} else {
				addButton.setDisable(true);
			}
		});
		addButton.setOnMouseClicked(event -> {
			try {
				new URL(urlInput.getText());
				PluginLoader.tryLoad(PluginUpdater.tryDownload(urlInput.getText()));
				urlInput.setText("");
			} catch (MalformedURLException e) {
				errorText.show("The URL '" + urlInput.getText() + "' is not a valid URL");
			} catch (DownloadException e) {
				errorText.show(e.getMessage());
			} catch (IOException e) {
				errorText.show("Unexpected Error: " + e.getMessage());
			} catch (PluginLoadingException e) {
				errorText.show("Unexpected Error during loading of the plugin: " + e.getMessage());
			}
		});

		grid.add(urlInput, 0, 1);
		grid.add(addButton, 1, 1);

		pluginScroll = new ScrollPane();

		GridPane.setHgrow(pluginScroll, Priority.ALWAYS);
		GridPane.setVgrow(pluginScroll, Priority.ALWAYS);
		pluginScroll.setStyle("-fx-background-color: white");
		grid.add(pluginScroll, 0, 2, 2, 1);

		pluginScroll.setContent(new PluginSection());

		vbox.getChildren().setAll(errorText, grid);
		content.getChildren().addAll(vbox, getCloseFooterBox());
		// setResizable(false);

		GlobalConstants.plugins.addListener(this);
	}

	private static class PluginSection extends VBox {

		private final ErrorLabel errorText;

		public PluginSection() {
			errorText = new ErrorLabel(this);

			var grid = new GridPane();

			grid.setHgap(10);
			grid.setPadding(new Insets(5));
			var hName = new Label("Name");
			hName.setPadding(new Insets(0, 0, 5, 0));
			var hVersion = new Label("Version");
			hVersion.setPadding(new Insets(0, 0, 5, 0));
			var hURL = new Label("Url");
			hURL.setPadding(new Insets(0, 0, 5, 0));

			GridPane.setHgrow(hURL, Priority.ALWAYS);

			grid.addRow(0, hName, hVersion, hURL);

			int i = 1;
			for (var plugin : GlobalConstants.sortedPlugins()) {
				addRow(grid, i, plugin);
				i++;
			}

			setPrefHeight(250);
			setPrefWidth(450);

			getChildren().setAll(errorText, grid);

		}

		private void addRow(GridPane grid, int rowNumber, PluginWithMetadata plugin) {
			var url = new TextField(plugin.updateURL() == null ? "N/A" : plugin.updateURL());
			url.setEditable(false);
			url.setStyle(
					"-fx-background-color: transparent; -fx-background-insets: 0; -fx-background-radius: 0; -fx-padding: 0;");

			var deleteButton = new Button("delete");
			deleteButton.setOnMouseClicked(event -> {
				try {
					plugin.destroy();
				} catch (IOException e) {
					errorText.show("Unable to remove plugin: " + plugin.name() + " with reason: " + e.getMessage());
					e.printStackTrace();
				}
			});

			grid.addRow(rowNumber, new Label(plugin.name()),
					new Label(plugin.pluginVersion() == null ? "Unknown" : plugin.pluginVersion()), url, deleteButton);
		}
	}

	private static class ErrorLabel extends TextArea {
		public ErrorLabel(Region parent) {
//			parent.widthProperty().addListener((obs, ov, nv) -> {
//				this.setMaxWidth(nv.doubleValue());
//				this.setMinWidth(nv.doubleValue());
//				this.setPrefWidth(nv.doubleValue());
//			});
			visibleProperty().bind(managedProperty());
			setEditable(false);
			setPrefHeight(45);
			setManaged(false);
			setWrapText(true);
			setStyle("-fx-text-fill: red;");
		}

		void show(String text) {
			setManaged(true);
			setText(text);
			setWrapText(true);
			var transition = new PauseTransition(Duration.seconds(5));
			transition.setOnFinished(x -> {
				setManaged(false);
			});
			transition.play();
		}
	}

	@Override
	protected String getReturnValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidated(Observable observable) {
		pluginScroll.setContent(new PluginSection());
	}

}
