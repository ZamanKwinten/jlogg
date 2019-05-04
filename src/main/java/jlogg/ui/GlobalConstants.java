package jlogg.ui;

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

	public static final ObservableList<Filter> filters = FXCollections.observableArrayList();

	public static final ObservableList<LogLine> searchResults = FXCollections.observableArrayList();

	public static final Font defaultFont = Font.font("Arial", 15);
}
