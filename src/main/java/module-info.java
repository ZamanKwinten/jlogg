module jlogg {
	requires java.base;
	requires java.desktop;
	requires java.logging;
	requires java.rmi;
	requires jdk.unsupported;
	requires javafx.controls;

	requires com.google.common;
	requires org.json;
	requires javafx.base;

	opens jlogg.os.windows to java.rmi;
	opens jlogg.eventbus to com.google.common;

	exports jlogg;
}