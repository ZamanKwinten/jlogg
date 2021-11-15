package jlogg.plugin;

import java.util.Optional;

public interface JLoggPlugin {

	String getName();

	PluginView getMainView();

	Optional<String> getCSSStylesheet();
}
