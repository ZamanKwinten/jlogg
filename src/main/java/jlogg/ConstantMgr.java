package jlogg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
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

		jloggPluginDir = new File(jloggDir, "plugins");
		if (!jloggPluginDir.exists()) {
			jloggPluginDir.mkdir();
		}

		indexServiceThreadCount = 1;
		searchServiceThreadCount = 1;
	}

	private Optional<JSONObject> getCurrentJSON() throws IOException {
		String content = Files.readString(Paths.get(jloggConfig.getAbsolutePath()));
		if (!content.trim().isEmpty()) {
			return Optional.of(new JSONObject(content));
		}
		return Optional.empty();
	}

	public void setupGlobalConstants() {
		try {

			JSONObject jsonConfig = getCurrentJSON().orElseGet(JSONObject::new);

			JSONArray filters = jsonConfig.optJSONArray(JSONKeys.FILTERS);
			if (filters != null) {
				GlobalConstants.filters.clear();
				for (int i = 0; i < filters.length(); i++) {
					GlobalConstants.filters.add(Filter.fromJSON(filters.getJSONObject(i)));
				}
			}

			JSONObject preferencesJSON = jsonConfig.optJSONObject(JSONKeys.PREFERENCES);
			if (preferencesJSON != null) {
				JSONObject fontJSON = preferencesJSON.optJSONObject(Preferences.JSON.FONT);
				if (fontJSON != null) {
					GlobalConstants.defaultFont.setValue(new Font(fontJSON.getString(Preferences.JSON.FAMILY),
							fontJSON.getDouble(Preferences.JSON.SIZE)));
				}

				JSONObject keyMapJSON = preferencesJSON.optJSONObject(Preferences.JSON.SHORTCUTS);
				if (keyMapJSON != null) {
					for (ShortCut key : GlobalConstants.ShortCut.values()) {
						String val = keyMapJSON.optString(key.name(), null);
						if (val != null) {
							KeyCombination combo = KeyCombination.valueOf(val);
							key.update(combo);
						}
					}
				}

				String themeName = preferencesJSON.optString(Preferences.JSON.THEME, null);
				if (themeName != null) {
					GlobalConstants.theme.setValue(Theme.valueOf(themeName));
				}
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error reading configuration file", e);
		}
	}

	public void updateFilters() {
		try {
			JSONObject config = getCurrentJSON().orElseGet(JSONObject::new);

			JSONArray filters = new JSONArray();
			for (Filter filter : GlobalConstants.filters) {
				filters.put(filter.toJSON());
			}
			config.put(JSONKeys.FILTERS, filters);

			Files.writeString(Paths.get(jloggConfig.getAbsolutePath()), config.toString());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error writing configuration file", e);
		}
	}

	public void updatePreferences(Preferences preferences) {
		try {
			JSONObject config = getCurrentJSON().orElseGet(JSONObject::new);

			config.put(JSONKeys.PREFERENCES, preferences.toJSON());

			Files.writeString(Paths.get(jloggConfig.getAbsolutePath()), config.toString());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error writing configuration file", e);
		}
	}

	public void loadPlugins() {
		for (File jarFile : jloggPluginDir.listFiles()) {
			try {
				PluginLoader loader = new PluginLoader(jarFile);
				GlobalConstants.plugins.add(loader.getPlugin());
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error while loading plugin: " + jarFile, e);
			}
		}

	}
}
