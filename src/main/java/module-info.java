module jlogg {
	requires java.base;
	requires java.desktop;
	requires java.logging;
	requires java.net.http;

	requires jdk.crypto.ec;
	requires jdk.unsupported;

	requires javafx.base;
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires javafx.web;

	requires transitive com.google.gson;

	opens jlogg.os.windows to java.rmi;
	opens jlogg.eventbus to com.google.common;
	opens jlogg.build to com.google.gson;
	opens jlogg.plugin.loader to com.google.gson;

	exports jlogg;
	exports jlogg.ui.table;
	exports jlogg.ui.utils;
	exports jlogg.type;
	exports jlogg.plugin;
}
