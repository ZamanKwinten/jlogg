module jlogg {
	requires java.base;
	requires java.desktop;
	requires java.logging;
	requires jdk.unsupported;
	requires transitive javafx.controls;
	requires javafx.base;

	requires javafx.graphics;
	requires java.net.http;
	requires jdk.crypto.ec;
	requires transitive com.google.gson;

	opens jlogg.os.windows to java.rmi;
	opens jlogg.eventbus to com.google.common;

	exports jlogg;
	exports jlogg.ui.table;
	exports jlogg.ui.utils;
	exports jlogg.type;
	exports jlogg.plugin;
}
