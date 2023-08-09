package jlogg.shared;

import com.google.gson.JsonObject;

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

	public static Filter fromJSON(JsonObject json) {
		String pattern = json.get(JSONKeys.PATTERN).getAsString();
		SearchOptions searchOptions = SearchOptions.fromJSON(json.get(JSONKeys.SEARCHOPTIONS).getAsJsonObject());
		Color foreground = getColorFromJSON(json.get(JSONKeys.FOREGROUND).getAsJsonObject());
		Color background = getColorFromJSON(json.get(JSONKeys.BACKGROUND).getAsJsonObject());

		return new Filter(pattern, searchOptions, foreground, background);
	}

	private static Color getColorFromJSON(JsonObject json) {
		double red = json.get(JSONKeys.RED).getAsDouble();
		double green = json.get(JSONKeys.GREEN).getAsDouble();
		double blue = json.get(JSONKeys.BLUE).getAsDouble();
		double opacity = json.get(JSONKeys.OPACITY).getAsDouble();
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

	public JsonObject toJSON() {
		JsonObject filter = new JsonObject();
		filter.addProperty(JSONKeys.PATTERN, pattern);
		filter.add(JSONKeys.SEARCHOPTIONS, searchOptions.toJSON());
		filter.add(JSONKeys.FOREGROUND, colorToJSON(foreGroundColor));
		filter.add(JSONKeys.BACKGROUND, colorToJSON(backGroundColor));
		return filter;
	}

	private JsonObject colorToJSON(Color color) {
		JsonObject json = new JsonObject();
		json.addProperty(JSONKeys.RED, color.getRed());
		json.addProperty(JSONKeys.GREEN, color.getGreen());
		json.addProperty(JSONKeys.BLUE, color.getBlue());
		json.addProperty(JSONKeys.OPACITY, color.getOpacity());
		return json;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		searchOptions.setIgnoreCase(ignoreCase);
	}

	public boolean isIgnoreCase() {
		return searchOptions.ignoreCase();
	}
}
