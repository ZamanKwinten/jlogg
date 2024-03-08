package jlogg.version;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class VersionUtil {
	private static final Logger logger = Logger.getLogger(VersionUtil.class.getName());

	private static final String currentVersion = Version.version;
	private static final Gson gson = new GsonBuilder().create();

	private record ReleaseLatestResponse(String tag_name) {

	}

	public static void checkForUpdates() {
		try {
			String latestVersion = getLatestVersion();

			if (!Objects.equals(latestVersion, currentVersion)) {
				showNewVersionAvailablePopup();
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "VersionUtil::checkForUpdates", e);
		}
	}

	private static String getLatestVersion() throws IOException, InterruptedException {
		var client = HttpClient.newBuilder().build();
		var getLatest = HttpRequest
				.newBuilder(URI.create("https://api.github.com/repos/ZamanKwinten/jlogg/releases/latest")).GET()
				.build();

		var response = client.send(getLatest, BodyHandlers.ofString());

		if (response.statusCode() == 200) {
			return gson.fromJson(response.body(), ReleaseLatestResponse.class).tag_name();
		}

		throw new RuntimeException("Unexpected response code of github api call: " + response.statusCode());
	}

	private static void showNewVersionAvailablePopup() {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("New version available");
			alert.setHeaderText("A new version of JLogg is available for download");

			ButtonType downloadButton = new ButtonType("Download Now");
			ButtonType laterButton = new ButtonType("Later");

			alert.getButtonTypes().setAll(downloadButton, laterButton);

			alert.showAndWait().ifPresent(type -> {
				if (type == downloadButton) {
					try {
						Desktop.getDesktop()
								.browse(URI.create("https://github.com/ZamanKwinten/jlogg/releases/latest"));
					} catch (IOException e) {
						logger.log(Level.SEVERE, "VersionUtil::showNewVersionAvailablePopup", e);
					}
				}
			});

		});
	}
}