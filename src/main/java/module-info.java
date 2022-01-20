module jlogg {
	requires java.base;
	requires java.desktop;
	requires java.logging;
	requires java.rmi;
	requires jdk.unsupported;
	requires transitive javafx.controls;
	requires javafx.base;

	requires transitive org.json;
	requires javafx.graphics;

	opens jlogg.os.windows to java.rmi;
	opens jlogg.eventbus to com.google.common;

	exports jlogg;
	exports jlogg.ui.table;
	exports jlogg.ui.utils;
	exports jlogg.type;
	exports jlogg.plugin;
}