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
import jlogg.PluginLoader;
import jlogg.PluginLoader.PluginLoadingException;
import jlogg.build.BuildDetailsUtil;
import jlogg.PluginWithMetadata;
import jlogg.ui.GlobalConstants;

public class PluginUpdater {
	private static final Logger logger = Logger.getLogger(PluginUpdater.class.getName());
	private static final Gson gson = new GsonBuilder().create();

	public static class DownloadException extends Exception {
		private static final long serialVersionUID = 1L;

		private DownloadException(String reason) {
			super(reason);
		}
	}

	public static PluginManifestData getLatestPluginVersion(String pluginServerURL)
			throws DownloadException, IOException {
		var response = getLatestPluginData(pluginServerURL).orElseThrow(() -> new DownloadException(
				"Could not access Plugin Update Server or the response from the Plugin Update Server was not expected."));

		return response.manifest();
	}

	public static boolean compatibleUpdate(PluginWithMetadata installedVersion, PluginManifestData remoteManifest) {
		return !BuildDetailsUtil.isLaterJLoggVersion(remoteManifest.jloggVersion())
				&& BuildDetailsUtil.isLaterVersion(installedVersion.pluginVersion(), remoteManifest.jloggPluginVersion());
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

		if (BuildDetailsUtil.isLaterJLoggVersion(manifest.jloggVersion())) {
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
		return pluginLocation;
	}

	public static Optional<PluginWithMetadata> tryUpdate(PluginWithMetadata plugin) {
		try {
			var pluginServerURL = plugin.updateURL();

			var optResponse = getLatestPluginData(pluginServerURL);
			if (!optResponse.isPresent()) {
				return Optional.empty();
			}

			var response = optResponse.get();
			var manifest = response.manifest();

			if (!compatibleUpdate(plugin, manifest)) {
				return Optional.empty();
			}

			var downloadStream = sendGETRequest(
					PluginServerEndPoints.constructDownloadURI(pluginServerURL, response.filename()),
					BodyHandlers.ofInputStream());

			if (downloadStream.statusCode() == 200) {

				plugin.destroy();
				Files.copy(downloadStream.body(), plugin.pluginJAR().toPath(), StandardCopyOption.REPLACE_EXISTING);
				return Optional.of(PluginLoader.tryLoad(plugin.pluginJAR()));
			}
			return Optional.of(plugin);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to update plugin: " + plugin.name(), e);
			try {
				return Optional.of(PluginLoader.tryLoad(plugin.pluginJAR()));
			} catch (PluginLoadingException ex) {
				ex.printStackTrace();
				return Optional.empty();
			}
		}
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
