module jlogg {
	requires java.base;
	requires java.desktop;
	requires java.logging;
	requires jdk.unsupported;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires com.google.common;
	requires org.json;

	exports jlogg;
}