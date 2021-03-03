package jlogg;

import java.util.Map;

import org.json.JSONObject;

import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import jlogg.ui.GlobalConstants.ShortCut;

public class Preferences {
	public static class JSON {
		public static String FONT = "font";
		public static String FAMILY = "family";
		public static String SIZE = "size";
		public static String SHORTCUTS = "shortcut";
	}

	public static Preferences fromJSON(JSONObject json) {
		JSONObject fontJSON = json.getJSONObject(JSON.FONT);
		Font font = new Font(fontJSON.getString(JSON.FAMILY), fontJSON.getDouble(JSON.SIZE));
		return new Preferences(font, null);
	}

	private final Font font;
	private final Map<ShortCut, KeyCombination> keyMap;

	public Preferences(Font font, @SuppressWarnings("exports") Map<ShortCut, KeyCombination> keyMap) {
		this.font = font;
		this.keyMap = keyMap;
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

		return preferences;
	}
}
