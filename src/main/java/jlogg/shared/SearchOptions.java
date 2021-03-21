package jlogg.shared;

import org.json.JSONObject;

public class SearchOptions {
	private static final class JSONKeys {
		private static final String IGNORECASE = "ignoreCase";
	}

	public static SearchOptions fromJSON(JSONObject json) {
		return new SearchOptions(json.getBoolean(JSONKeys.IGNORECASE));
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

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(JSONKeys.IGNORECASE, ignoreCase);

		return json;
	}

}
