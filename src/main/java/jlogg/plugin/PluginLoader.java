package jlogg.plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class PluginLoader {
	private final String mainClass;
	private final URLClassLoader classLoader;

	private final JLoggPlugin plugin;

	public PluginLoader(File pluginJAR) throws Exception {
		mainClass = getMainClassFromJar(pluginJAR);
		classLoader = new URLClassLoader(new URL[] { pluginJAR.toURI().toURL() }, this.getClass().getClassLoader());

		Class<?> pluginClass = classLoader.loadClass(mainClass);

		if (JLoggPlugin.class.isAssignableFrom(pluginClass)) {
			plugin = (JLoggPlugin) pluginClass.getConstructor().newInstance();
		} else {
			throw new RuntimeException("Plugin main class is not a JLoggPlugin");
		}
	}

	private String getMainClassFromJar(File pluginJAR) throws Exception {
		try (JarFile jar = new JarFile(pluginJAR)) {
			String mainClass = jar.getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
			if (mainClass == null) {
				throw new RuntimeException("Could not find mainclass in manifest");
			}
			return mainClass;
		}
	}

	public JLoggPlugin getPlugin() {
		return plugin;
	}
}
