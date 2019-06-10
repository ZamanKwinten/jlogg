package jlogg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

		// See globalconstants
	}

	/**
	 * Read the jlogg config file
	 */
	private void readConfigFile() {
		try {

			String content = Files.readString(Paths.get(jloggConfig.getAbsolutePath()));
			if (!content.trim().isEmpty()) {
				JSONObject jsonConfig = new JSONObject(content);

				JSONArray filters = jsonConfig.optJSONArray(JSONKeys.FILTERS);
				if (filters != null) {
					for (int i = 0; i < filters.length(); i++) {
						GlobalConstants.filters.add(Filter.fromJSON(filters.getJSONObject(i)));
					}
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error reading configuration file", e);
		}
	}

	/**
	 * Update the jlogg config file
	 */
	public void updateConfigFile() {
		try {
			JSONObject config = new JSONObject();

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
}
