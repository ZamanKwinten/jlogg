package jlogg;

import java.util.Map;

import org.json.JSONObject;

import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import jlogg.plugin.Theme;
import jlogg.ui.GlobalConstants.ShortCut;

public class Preferences {
	public static class JSON {
		public static String FONT = "font";
		public static String FAMILY = "family";
		public static String SIZE = "size";
		public static String SHORTCUTS = "shortcut";
		public static String THEME = "theme";
	}

	private final Font font;
	private final Map<ShortCut, KeyCombination> keyMap;
	private final Theme theme;

	@SuppressWarnings("exports")
	public Preferences(Font font, Map<ShortCut, KeyCombination> keyMap, Theme theme) {
		this.font = font;
		this.keyMap = keyMap;
		this.theme = theme;
	}

	public JSONObject toJSON() {
		JSONObject preferences = new JSONObject();

		JSONObject fontJSON = new JSONObject();
		fontJSON.put(JSON.FAMILY, font.getFamily());
		fontJSON.put(JSON.SIZE, font.getSize());
		preferences.put(JSON.FONT, fontJSON);

		JSONObject keyMap = new JSONObject();
		for (ShortCut key : this.keyMap.keySet()) {
			KeyCombination combo = this.keyMap.get(key);
			keyMap.put(key.name(), combo.getName());
		}
		preferences.put(JSON.SHORTCUTS, keyMap);

		preferences.put(JSON.THEME, theme.name());

		return preferences;
	}
}
