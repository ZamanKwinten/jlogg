package jlogg;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;

import jlogg.plugin.JLoggPlugin;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainStage;

public record PluginWithMetadata(JLoggPlugin plugin, String name, String updateURL, String pluginVersion,
		File pluginJAR, URLClassLoader classLoader) {

	public void destroy() throws IOException {
		plugin.getCSSStylesheet().ifPresent(cssPath -> {
			var stylesheets = MainStage.getInstance().getMainPane().getStylesheets();
			System.out.println(stylesheets);
		});

		classLoader.close();

		Files.delete(pluginJAR.toPath());
		GlobalConstants.plugins.remove(this);

	}
}
