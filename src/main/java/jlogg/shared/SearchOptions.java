package jlogg.shared;

import com.google.gson.JsonObject;

public class SearchOptions {
	private static final class JSONKeys {
		private static final String IGNORECASE = "ignoreCase";
	}

	public static SearchOptions fromJSON(JsonObject json) {
		return new SearchOptions(json.get(JSONKeys.IGNORECASE).getAsBoolean());
	}

	private boolean ignoreCase;

	public SearchOptions(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public boolean ignoreCase() {
		return this.ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public JsonObject toJSON() {
		JsonObject json = new JsonObject();
		json.addProperty(JSONKeys.IGNORECASE, ignoreCase);

		return json;
	}

}
