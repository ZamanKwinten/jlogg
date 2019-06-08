package jlogg.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;
import jlogg.shared.Filter;
import jlogg.shared.LogLine;

/**
 * Globally accessible lists used throughout the application
 * 
 * @author KWZA
 *
 */
public class GlobalConstants {

	/**
	 * A map of <full file path, observable logline list>
	 */
	public static final Map<File, ObservableList<LogLine>> fileLogLines = new HashMap<>();

	/**
	 * All known filters
	 */
	public static final ObservableList<Filter> filters = FXCollections.observableArrayList();

	/**
	 * Results of the current search
	 */
	public static final ObservableList<LogLine> searchResults = FXCollections.observableArrayList();

	public static final SimpleDoubleProperty searchProgress = new SimpleDoubleProperty(0.0);

	public static final Font defaultFont = Font.font("Arial", 15);
}
