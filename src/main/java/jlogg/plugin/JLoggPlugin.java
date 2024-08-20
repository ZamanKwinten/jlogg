package jlogg.plugin;

import java.io.InputStream;
import java.util.Optional;

public interface JLoggPlugin {

	PluginView getMainView();

	Optional<InputStream> getCSSStylesheet();
}
