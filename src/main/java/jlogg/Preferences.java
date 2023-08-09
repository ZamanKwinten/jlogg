package jlogg;

import java.util.Map;

import com.google.gson.JsonObject;

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

	public JsonObject toJSON() {
		JsonObject preferences = new JsonObject();

		JsonObject fontJSON = new JsonObject();
		fontJSON.addProperty(JSON.FAMILY, font.getFamily());
		fontJSON.addProperty(JSON.SIZE, font.getSize());
		preferences.add(JSON.FONT, fontJSON);

		JsonObject keyMap = new JsonObject();
		for (ShortCut key : this.keyMap.keySet()) {
			KeyCombination combo = this.keyMap.get(key);
			keyMap.addProperty(key.name(), combo.getName());
		}
		preferences.add(JSON.SHORTCUTS, keyMap);

		preferences.addProperty(JSON.THEME, theme.name());

		return preferences;
	}
}
