package jlogg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import jlogg.PluginLoader.PluginLoadingException;
import jlogg.plugin.Theme;
import jlogg.shared.Filter;
import jlogg.ui.GlobalConstants;
import jlogg.ui.GlobalConstants.ShortCut;

/**
 * Class to define some constants used throughout the application TODO =>
 * Memento pattern
 * 
 * @author Kwinten
 *
 */
public class ConstantMgr {
	public static final File JLoggConfigDir;
	static {
		JLoggConfigDir = new File(System.getProperty("user.home"), ".jlogg");
		JLoggConfigDir.mkdirs();
	}

	private static final class JSONKeys {
		private static final String FILTERS = "filters";
		private static final String PREFERENCES = "preferences";
	}

	private static final ConstantMgr instance = new ConstantMgr();

	public static ConstantMgr instance() {
		return instance;
	}

	private final Logger logger = Logger.getLogger(ConstantMgr.class.toString());

	public final int indexServiceThreadCount;
	public final int searchServiceThreadCount;

	private final File jloggConfig;
	private final File jloggPluginCSSDir;
	private final File jloggPluginDir;

	private ConstantMgr() {
		File jloggDir = JLoggConfigDir;
		if (!jloggDir.exists()) {
			jloggDir.mkdirs();
		}

		jloggConfig = new File(jloggDir, "config.json");
		if (!jloggConfig.exists()) {
			try {
				jloggConfig.createNewFile();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error creating configuration file", e);
			}
		}

		jloggPluginDir = new File(jloggDir, "pluginmanager");
		if (!jloggPluginDir.exists()) {
			jloggPluginDir.mkdir();
		}

		jloggPluginCSSDir = new File(jloggDir, "plugincss");
		if (!jloggPluginCSSDir.exists()) {
			jloggPluginCSSDir.mkdir();
		}

		indexServiceThreadCount = 1;
		searchServiceThreadCount = 1;
	}

	private Optional<JsonObject> getCurrentJSON() throws IOException {
		String content = Files.readString(Paths.get(jloggConfig.getAbsolutePath()));
		if (!content.trim().isEmpty()) {
			return Optional.of(JsonParser.parseString(content).getAsJsonObject());
		}
		return Optional.empty();
	}

	public void setupGlobalConstants() {
		try {
			JsonObject jsonConfig = getCurrentJSON().orElseGet(JsonObject::new);

			optionalGet(jsonConfig, JSONKeys.FILTERS, JsonElement::getAsJsonArray).ifPresent(filters -> {
				GlobalConstants.filters.clear();
				for (int i = 0; i < filters.size(); i++) {
					GlobalConstants.filters.add(Filter.fromJSON(filters.get(i).getAsJsonObject()));
				}
			});

			optionalGet(jsonConfig, JSONKeys.PREFERENCES, JsonElement::getAsJsonObject).ifPresent(preferencesJSON -> {
				optionalGet(preferencesJSON, Preferences.JSON.FONT, JsonElement::getAsJsonObject)
						.ifPresent(fontJSON -> {
							GlobalConstants.defaultFont
									.setValue(new Font(fontJSON.get(Preferences.JSON.FAMILY).getAsString(),
											fontJSON.get(Preferences.JSON.SIZE).getAsDouble()));
						});

				optionalGet(preferencesJSON, Preferences.JSON.SHORTCUTS, JsonElement::getAsJsonObject)
						.ifPresent(keyMapJSON -> {
							for (ShortCut key : GlobalConstants.ShortCut.values()) {
								optionalGet(keyMapJSON, key.name(), JsonElement::getAsString).ifPresent(val -> {
									KeyCombination combo = KeyCombination.valueOf(val);
									key.update(combo);
								});
							}
						});

				optionalGet(preferencesJSON, Preferences.JSON.THEME, JsonElement::getAsString).ifPresent(themeName -> {
					GlobalConstants.theme.setValue(Theme.valueOf(themeName));
				});
			});

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error reading configuration file", e);
		}
	}

	private <T> Optional<T> optionalGet(JsonObject json, String key, Function<JsonElement, T> converter) {
		if (json.has(key)) {
			return Optional.of(converter.apply(json.get(key)));
		}

		return Optional.empty();
	}

	public void updateFilters() {
		try {
			JsonObject config = getCurrentJSON().orElseGet(JsonObject::new);

			JsonArray filters = new JsonArray();
			for (Filter filter : GlobalConstants.filters) {
				filters.add(filter.toJSON());
			}

			config.add(JSONKeys.FILTERS, filters);
			Files.writeString(Paths.get(jloggConfig.getAbsolutePath()), config.toString());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error writing configuration file", e);
		}
	}

	public void updatePreferences(Preferences preferences) {
		try {
			JsonObject config = getCurrentJSON().orElseGet(JsonObject::new);

			config.add(JSONKeys.PREFERENCES, preferences.toJSON());

			Files.writeString(Paths.get(jloggConfig.getAbsolutePath()), config.toString());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error writing configuration file", e);
		}
	}

	public void loadPlugins() {
		for (File jarFile : jloggPluginDir.listFiles((dir, name) -> name.endsWith(".jar"))) {
			try {
				PluginLoader.tryLoad(jarFile);
			} catch (PluginLoadingException e) {
				// ignore
			}
		}

	}

	public File findLocationForPlugin(String filename) {
		if (!filename.endsWith(".jar")) {
			filename = filename + ".jar";
		}

		return new File(jloggPluginDir, filename);
	}

	public File writePluginCSS(PluginWithMetadata plugin, InputStream is) throws FileNotFoundException, IOException {
		var file = new File(jloggPluginCSSDir, plugin.name());
		if (!file.exists()) {
			file.createNewFile();
		}

		try (FileOutputStream fos = new FileOutputStream(file)) {
			byte[] buffer = new byte[1024];
			for (int i = is.read(buffer); i > 0; i = is.read(buffer)) {
				fos.write(buffer, 0, i);
			}
		}

		return file;
	}
}
