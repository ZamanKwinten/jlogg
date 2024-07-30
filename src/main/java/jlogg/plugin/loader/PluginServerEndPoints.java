package jlogg.plugin.loader;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PluginServerEndPoints {
	private PluginServerEndPoints() {

	}

	public static final String LATEST = "latest";
	public static final String DOWNLOAD = "download";

	public static String constructLatestURI(String updateURL) {
		return safeConcatTrailingSlash(updateURL) + LATEST;
	}

	public static String constructDownloadURI(String updateURL, String filename) {
		return safeConcatTrailingSlash(updateURL) + DOWNLOAD + "/"
				+ URLEncoder.encode(filename, StandardCharsets.UTF_8);
	}

	private static String safeConcatTrailingSlash(String updateURL) {
		if (updateURL.endsWith("/")) {
			return updateURL;
		}

		return updateURL + "/";
	}
}
