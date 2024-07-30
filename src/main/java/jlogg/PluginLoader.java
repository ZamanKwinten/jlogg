package jlogg;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import jlogg.plugin.JLoggPlugin;
import jlogg.plugin.loader.PluginManifestData;
import jlogg.plugin.loader.PluginManifestReader;
import jlogg.plugin.loader.PluginUpdater;

public class PluginLoader {

	private final PluginManifestData manifestData;
	private final URLClassLoader classLoader;

	private final JLoggPlugin plugin;

	public PluginLoader(File pluginJAR) throws Exception {
		manifestData = PluginManifestReader.getManifestDataFromJar(pluginJAR);

		manifestData.serverURL().ifPresent(str -> {
			PluginUpdater.tryUpdate(manifestData, pluginJAR);
		});

		classLoader = new URLClassLoader(new URL[] { pluginJAR.toURI().toURL() }, this.getClass().getClassLoader());

		Class<?> pluginClass = classLoader.loadClass(manifestData.mainClass());

		if (JLoggPlugin.class.isAssignableFrom(pluginClass)) {
			plugin = (JLoggPlugin) pluginClass.getConstructor().newInstance();
		} else {
			throw new RuntimeException("Plugin main class is not a JLoggPlugin");
		}
	}

	public JLoggPlugin getPlugin() {
		return plugin;
	}
}
