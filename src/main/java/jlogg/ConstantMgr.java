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

import jlogg.shared.Filter;
import jlogg.ui.GlobalConstants;

/**
 * Class to define some constants used throughout the application TODO =>
 * Memento pattern
 * 
 * @author Kwinten
 *
 */
public class ConstantMgr {

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

	private ConstantMgr() {
		String homeDir = System.getProperty("user.home");
		File jloggDir = new File(homeDir, ".jlogg");
		if (!jloggDir.exists()) {
			jloggDir.mkdir();
		}

		jloggConfig = new File(jloggDir, "config.json");
		if (!jloggConfig.exists()) {
			try {
				jloggConfig.createNewFile();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error creating configuration file", e);
			}
		}

		readConfigFile();

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

	private void readConfigFile() {
		try {

			JSONObject jsonConfig = getCurrentJSON().orElseGet(JSONObject::new);

			JSONArray filters = jsonConfig.optJSONArray(JSONKeys.FILTERS);
			if (filters != null) {
				for (int i = 0; i < filters.length(); i++) {
					GlobalConstants.filters.add(Filter.fromJSON(filters.getJSONObject(i)));
				}
			}

			JSONObject preferencesJSON = jsonConfig.optJSONObject(JSONKeys.PREFERENCES);
			if (preferencesJSON != null) {
				Preferences preferences = Preferences.fromJSON(preferencesJSON);
				GlobalConstants.defaultFont.setFont(preferences.font());
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
}
