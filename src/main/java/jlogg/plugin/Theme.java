package jlogg.plugin;

public enum Theme {
	LIGHT("Light", "#d0d0d0"), DARK("Dark", "#121212");

	private final String uiName;
	private final String fxBase;

	private Theme(String uiName, String fxBase) {
		this.uiName = uiName;
		this.fxBase = fxBase;
	}

	public String getUIName() {
		return uiName;
	}

	public String getFXBase() {
		return fxBase;
	}
}