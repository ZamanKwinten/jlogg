package jlogg.shared;

import org.json.JSONObject;

import javafx.scene.paint.Color;

public class Filter extends SearchCriteria implements Cloneable {
	private Color foreGroundColor;
	private Color backGroundColor;

	private static final class JSONKeys {
		private static final String PATTERN = "pattern";
		private static final String SEARCHOPTIONS = "searchOptions";
		private static final String FOREGROUND = "foreground";
		private static final String BACKGROUND = "background";
		private static final String RED = "r";
		private static final String GREEN = "g";
		private static final String BLUE = "b";
		private static final String OPACITY = "o";
	}

	public static Filter fromJSON(JSONObject json) {
		String pattern = json.getString(JSONKeys.PATTERN);
		SearchOptions searchOptions = SearchOptions.fromJSON(json.getJSONObject(JSONKeys.SEARCHOPTIONS));
		Color foreground = getColorFromJSON(json.getJSONObject(JSONKeys.FOREGROUND));
		Color background = getColorFromJSON(json.getJSONObject(JSONKeys.BACKGROUND));
		return new Filter(pattern, searchOptions, foreground, background);
	}

	private static Color getColorFromJSON(JSONObject json) {
		double red = json.getDouble(JSONKeys.RED);
		double green = json.getDouble(JSONKeys.GREEN);
		double blue = json.getDouble(JSONKeys.BLUE);
		double opacity = json.getDouble(JSONKeys.OPACITY);
		return new Color(red, green, blue, opacity);
	}

	public Filter(String pattern, SearchOptions searchOptions, Color foreGround, Color backGround) {
		super(pattern, searchOptions);
		this.foreGroundColor = foreGround;
		this.backGroundColor = backGround;
	}

	public String getForeGroundRGB() {
		return getInRGB(foreGroundColor);
	}

	public Color getForeGroundColor() {
		return foreGroundColor;
	}

	public void setForeGroundColor(Color foreGroundColor) {
		this.foreGroundColor = foreGroundColor;
	}

	public String getBackGroundRGB() {
		return getInRGB(backGroundColor);
	}

	public Color getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(Color backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	private String getInRGB(Color color) {
		return "rgb(" + color.getRed() * 255 + "," + color.getGreen() * 255 + "," + color.getBlue() * 255 + ")";
	}

	@Override
	public Filter clone() {
		return new Filter(pattern, searchOptions, foreGroundColor, backGroundColor);
	}

	public JSONObject toJSON() {
		JSONObject filter = new JSONObject();
		filter.put(JSONKeys.PATTERN, pattern);
		filter.put(JSONKeys.SEARCHOPTIONS, searchOptions.toJSON());
		filter.put(JSONKeys.FOREGROUND, colorToJSON(foreGroundColor));
		filter.put(JSONKeys.BACKGROUND, colorToJSON(backGroundColor));
		return filter;
	}

	private JSONObject colorToJSON(Color color) {
		JSONObject json = new JSONObject();
		json.put(JSONKeys.RED, color.getRed());
		json.put(JSONKeys.GREEN, color.getGreen());
		json.put(JSONKeys.BLUE, color.getBlue());
		json.put(JSONKeys.OPACITY, color.getOpacity());
		return json;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		searchOptions.setIgnoreCase(ignoreCase);
	}

	public boolean isIgnoreCase() {
		return searchOptions.ignoreCase();
	}
}
