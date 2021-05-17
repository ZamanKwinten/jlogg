package jlogg.plugin;

import java.util.Optional;

import javafx.scene.Node;

public interface JLoggPlugin {

	String getName();

	Node getMainView();

	Optional<String> getCSSStylesheet();
}
