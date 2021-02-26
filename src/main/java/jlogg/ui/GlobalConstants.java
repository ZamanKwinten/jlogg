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
import jlogg.ui.custom.search.SearchHistoryList;
import jlogg.ui.logview.ObservableFont;

/**
 * Globally accessible lists used throughout the application
 * 
 * @author KWZA
 *
 */
public class GlobalConstants {

	/**
	 * A history list of recently searched strings
	 */
	public static final SearchHistoryList searchHistory = new SearchHistoryList();

	/**
	 * A map of <full file path, observable logline list>
	 */
	public static final Map<File, ObservableList<LogLine>> fileLogLines = new HashMap<>();

	/**
	 * A map of <full file path, indexing progress>
	 */
	public static final Map<File, SimpleDoubleProperty> fileIndexProgress = new HashMap<>();

	/**
	 * All known filters
	 */
	public static final ObservableList<Filter> filters = FXCollections.observableArrayList();

	/**
	 * Results of the current search
	 */
	public static final ObservableList<LogLine> searchResults = FXCollections.observableArrayList();

	/**
	 * Progress of the current search
	 */
	public static final SimpleDoubleProperty searchProgress = new SimpleDoubleProperty(0.0);

	public static final ObservableFont defaultFont = new ObservableFont(Font.font("Arial", 15));
}
