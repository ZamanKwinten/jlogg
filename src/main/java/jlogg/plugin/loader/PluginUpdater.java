package jlogg.plugin.loader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PluginUpdater {
	private static final Logger logger = Logger.getLogger(PluginUpdater.class.getName());
	private static final Gson gson = new GsonBuilder().create();
	private static final String LAST_TRY_EXTENSION = ".lastsuccess";
	private static final long UPDATE_CHECK_DELTA = 24 * 60 * 60 * 1000; // try once a day

	public static void tryUpdate(PluginManifestData manifestData, File pluginLocation) {
		File lastSuccess = new File(pluginLocation.getParentFile(), pluginLocation.getName() + LAST_TRY_EXTENSION);
		if (lastSuccess.exists() && System.currentTimeMillis() - lastSuccess.lastModified() < UPDATE_CHECK_DELTA) {
			return;
		}
		try {
			var pluginServerURL = manifestData.serverURL().get();

			var optResponse = getLatestPluginData(pluginServerURL);
			if (!optResponse.isPresent()) {
				return;
			}

			var response = optResponse.get();

			var downloadStream = sendGETRequest(
					PluginServerEndPoints.constructDownloadURI(pluginServerURL, response.filename()),
					BodyHandlers.ofInputStream());

			if (downloadStream.statusCode() == 200) {
				Files.copy(downloadStream.body(), pluginLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}

			if (!lastSuccess.exists()) {
				lastSuccess.createNewFile();
			} else {
				lastSuccess.setLastModified(System.currentTimeMillis());
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to update plugin: " + pluginLocation.getName(), e);
		}

	}

	private static Optional<PluginServerLatestResponse> getLatestPluginData(String serverURL)
			throws IOException, InterruptedException {

		var response = sendGETRequest(PluginServerEndPoints.constructLatestURI(serverURL), BodyHandlers.ofString());

		if (response.statusCode() == 200) {
			var body = response.body();
			if (body.isBlank()) {
				return Optional.empty();
			}
			return Optional.of(gson.fromJson(body, PluginServerLatestResponse.class));
		}

		return Optional.empty();
	}

	private static <T> HttpResponse<T> sendGETRequest(String uri, BodyHandler<T> bodyHandler)
			throws IOException, InterruptedException {
		var client = HttpClient.newBuilder().build();
		var getLatest = HttpRequest.newBuilder(URI.create(uri)).GET().build();

		return client.send(getLatest, bodyHandler);
	}
}
