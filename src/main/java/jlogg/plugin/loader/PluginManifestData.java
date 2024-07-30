package jlogg.plugin.loader;

import java.io.IOException;
import java.util.Optional;
import java.util.jar.JarFile;

import jlogg.plugin.loader.PluginManifestReader.JLoggPluginManifestAttributes;

public class PluginManifestData {

	private final String mainClass;
	private final String serverURL;
	private final String jloggVersion;
	private final String jloggPluginVersion;

	PluginManifestData(JarFile jar) throws IOException {
		var attributes = jar.getManifest().getMainAttributes();
		mainClass = attributes.getValue(JLoggPluginManifestAttributes.MainClass);
		serverURL = attributes.getValue(JLoggPluginManifestAttributes.JLoggPluginServerURI);
		jloggVersion = attributes.getValue(JLoggPluginManifestAttributes.JLoggVersion);
		jloggPluginVersion = attributes.getValue(JLoggPluginManifestAttributes.JLoggPluginVersion);
		if (mainClass == null) {
			throw new RuntimeException("Could not find mainclass in manifest");
		}

		if (serverURL != null) {
			if (jloggVersion == null || jloggPluginVersion == null) {
				throw new RuntimeException("Plugin has update url but no version management");
			}
		}
	}

	public String mainClass() {
		return mainClass;
	}

	public Optional<String> serverURL() {
		return Optional.ofNullable(serverURL);
	}

	public String jloggVersion() {
		return jloggVersion;
	}

	public String jloggPluginVersion() {
		return jloggPluginVersion;
	}
}