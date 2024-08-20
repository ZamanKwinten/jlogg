package jlogg.plugin.loader;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;

public class PluginManifestReader {

	static final class JLoggPluginManifestAttributes {
		static final Name JLoggPluginName = new Name("JLogg-Plugin-Name");
		static final Name MainClass = Attributes.Name.MAIN_CLASS;
		// URI pointing to the JLogg Plugin Server endpoint
		static final Name JLoggPluginServerURI = new Name("JLogg-Plugin-Server-URI");
		static final Name JLoggVersion = new Name("JLogg-Version");
		static final Name JLoggPluginVersion = new Name("JLogg-Plugin-Version");
	}

	public static PluginManifestData getManifestDataFromJar(File pluginJAR) {
		try (JarFile jar = new JarFile(pluginJAR)) {
			return new PluginManifestData(jar);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
