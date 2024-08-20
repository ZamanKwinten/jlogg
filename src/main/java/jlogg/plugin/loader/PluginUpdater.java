package jlogg.plugin.loader;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jlogg.ConstantMgr;
import jlogg.ui.GlobalConstants;
import jlogg.version.VersionUtil;

public class PluginUpdater {
	private static final Logger logger = Logger.getLogger(PluginUpdater.class.getName());
	private static final Gson gson = new GsonBuilder().create();
	private static final String LAST_TRY_EXTENSION = ".lastsuccess";
	private static final long UPDATE_CHECK_DELTA = 24 * 60 * 60 * 1000; // try once a day

	public static void tryUpdate(PluginManifestData manifestData, File pluginLocation) {
		if (recentPluginUpdate(pluginLocation)) {
			return;
		}

		try {
			var pluginServerURL = manifestData.serverURL().get();

			var optResponse = getLatestPluginData(pluginServerURL);
			if (!optResponse.isPresent()) {
				return;
			}

			var response = optResponse.get();
			var manifest = response.manifest();

			if (VersionUtil.isLaterJLoggVersion(manifest.jloggVersion())
					|| !VersionUtil.isLaterVersion(manifestData.jloggPluginVersion(), manifest.jloggPluginVersion())) {
				// No need to update since either the plugin is not runnable on the current
				// JLogg version OR the version of the plugin is not later than the current one
				updatePluginLock(pluginLocation);
				return;
			}

			var downloadStream = sendGETRequest(
					PluginServerEndPoints.constructDownloadURI(pluginServerURL, response.filename()),
					BodyHandlers.ofInputStream());

			if (downloadStream.statusCode() == 200) {
				Files.copy(downloadStream.body(), pluginLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			updatePluginLock(pluginLocation);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to update plugin: " + pluginLocation.getName(), e);
		}

	}

	public static File tryDownload(String pluginServerURL) throws DownloadException, IOException {
		var response = getLatestPluginData(pluginServerURL).orElseThrow(() -> new DownloadException(
				"Could not access Plugin Update Server or the response from the Plugin Update Server was not expected."));
		var manifest = response.manifest();

		var name = manifest.name();

		if (GlobalConstants.plugins.stream().anyMatch(p -> Objects.equals(name, p.name()))) {
			throw new DownloadException(
					"Plugin with name '" + name + "' already exists, the name of the plugin must be unique.");
		}

		if (VersionUtil.isLaterJLoggVersion(manifest.jloggVersion())) {
			throw new DownloadException(
					"Your current version of JLogg is not compatible with the version required by the plugin. Please update JLogg to the latest version");
		}

		var downloadStream = sendGETRequest(
				PluginServerEndPoints.constructDownloadURI(pluginServerURL, response.filename()),
				BodyHandlers.ofInputStream());

		File pluginLocation = ConstantMgr.instance().findLocationForPlugin(response.filename());

		if (downloadStream.statusCode() == 200) {
			Files.copy(downloadStream.body(), pluginLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} else {
			throw new DownloadException(
					"Received unexpected status code during downloading of the plugin: " + downloadStream.statusCode());
		}
		updatePluginLock(pluginLocation);
		return pluginLocation;
	}

	public static class DownloadException extends Exception {
		private static final long serialVersionUID = 1L;

		private DownloadException(String reason) {
			super(reason);
		}
	}

	private static boolean recentPluginUpdate(File pluginLocation) {
		File lastSuccess = getPluginUpdateLock(pluginLocation);
		return lastSuccess.exists() && System.currentTimeMillis() - lastSuccess.lastModified() < UPDATE_CHECK_DELTA;
	}

	private static void updatePluginLock(File pluginLocation) throws IOException {
		File lockFile = getPluginUpdateLock(pluginLocation);
		if (!lockFile.exists()) {
			lockFile.createNewFile();
		} else {
			lockFile.setLastModified(System.currentTimeMillis());
		}
	}

	private static File getPluginUpdateLock(File pluginLocation) {
		return new File(pluginLocation.getParentFile(), pluginLocation.getName() + LAST_TRY_EXTENSION);
	}

	private static Optional<PluginServerLatestResponse> getLatestPluginData(String serverURL)
			throws IOException, DownloadException {

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
			throws DownloadException, IOException {
		var client = HttpClient.newBuilder().build();
		var getLatest = HttpRequest.newBuilder(URI.create(uri)).GET().build();
		try {
			return client.send(getLatest, bodyHandler);
		} catch (InterruptedException e) {
			// This should really never happen since we're not interrupting anyway.
			throw new RuntimeException(e);
		} catch (ConnectException e) {
			throw new DownloadException("Could not connect to URL: " + uri);
		}

	}
}
