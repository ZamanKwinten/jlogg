package jlogg;

import org.json.JSONObject;

import javafx.scene.text.Font;

public class Preferences {
	public static class JSON {
		public static String FONT = "font";
		public static String FAMILY = "family";
		public static String SIZE = "size";
	}

	public static Preferences fromJSON(JSONObject json) {
		JSONObject fontJSON = json.getJSONObject(JSON.FONT);
		Font font = new Font(fontJSON.getString(JSON.FAMILY), fontJSON.getDouble(JSON.SIZE));
		return new Preferences(font);
	}

	private final Font font;

	public Preferences(Font font) {
		this.font = font;
	}

	public Font font() {
		return font;
	}

	public JSONObject toJSON() {
		JSONObject preferences = new JSONObject();

		JSONObject fontJSON = new JSONObject();
		fontJSON.put(JSON.FAMILY, font.getFamily());
		fontJSON.put(JSON.SIZE, font.getSize());
		preferences.put(JSON.FONT, fontJSON);

		return preferences;

	}
}
