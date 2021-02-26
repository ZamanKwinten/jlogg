module jlogg {
	requires java.base;
	requires java.desktop;
	requires java.logging;
	requires java.rmi;
	requires jdk.unsupported;
	requires transitive javafx.controls;
	requires javafx.base;

	requires com.google.common;
	requires transitive org.json;

	opens jlogg.os.windows to java.rmi;
	opens jlogg.eventbus to com.google.common;

	exports jlogg;
}