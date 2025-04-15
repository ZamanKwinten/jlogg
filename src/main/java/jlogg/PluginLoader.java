package jlogg;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import jlogg.plugin.JLoggPlugin;
import jlogg.plugin.loader.PluginManifestData;
import jlogg.plugin.loader.PluginManifestReader;
import jlogg.ui.GlobalConstants;

public class PluginLoader {
	private static final Logger logger = Logger.getLogger(PluginLoader.class.toString());

	public static class PluginLoadingException extends Exception {
		private static final long serialVersionUID = 1L;

		private PluginLoadingException(Exception cause) {
			super(cause);
		}

	}

	public static PluginWithMetadata tryLoad(File jarFile) throws PluginLoadingException {
		try {
			PluginLoader loader = new PluginLoader(jarFile);
			Platform.runLater(() -> {
				GlobalConstants.plugins.add(loader.getPlugin());
			});
			return loader.getPlugin();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while loading plugin: " + jarFile, e);
			throw new PluginLoadingException(e);
		}
	}

	private final File pluginJAR;
	private final PluginManifestData manifestData;

	private final URLClassLoader classLoader;
	private final JLoggPlugin plugin;

	private PluginLoader(File pluginJAR) throws Exception {
		this.pluginJAR = pluginJAR;
		manifestData = PluginManifestReader.getManifestDataFromJar(pluginJAR);

		classLoader = new URLClassLoader(new URL[] { pluginJAR.toURI().toURL() }, this.getClass().getClassLoader());

		Class<?> pluginClass = classLoader.loadClass(manifestData.mainClass());

		if (JLoggPlugin.class.isAssignableFrom(pluginClass)) {
			plugin = (JLoggPlugin) pluginClass.getConstructor().newInstance();
		} else {
			throw new RuntimeException("Plugin main class is not a JLoggPlugin");
		}
	}

	public PluginWithMetadata getPlugin() {
		return new PluginWithMetadata(plugin, manifestData.name(), manifestData.serverURL().orElse(null),
				manifestData.jloggPluginVersion(), pluginJAR, classLoader);
	}

}
