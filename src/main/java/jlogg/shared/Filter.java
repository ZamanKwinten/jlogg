package jlogg.shared;

import javafx.scene.paint.Color;

public class Filter extends SearchCriteria implements Cloneable {
	private Color foreGroundColor;
	private Color backGroundColor;

	public Filter(String pattern, boolean ignoreCase, Color foreGround, Color backGround) {
		super(pattern, ignoreCase);
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
		return new Filter(pattern, ignoreCase, foreGroundColor, backGroundColor);
	}
}
