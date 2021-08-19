package jlogg.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import jlogg.plugin.JLoggPlugin;
import jlogg.plugin.LogLine;
import jlogg.plugin.Theme;
import jlogg.shared.Filter;
import jlogg.ui.custom.search.SearchHistoryList;
import jlogg.ui.utils.Observable;

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
	public static final ObservableList<LogLine> multiFileSearchResults = FXCollections.observableArrayList();

	public static final SimpleStringProperty multiFileSearchCurrentFilename = new SimpleStringProperty(null);

	public static final Map<File, ObservableList<LogLine>> singleFileSearchResults = new HashMap<>();

	/**
	 * Progress of the current search
	 */
	public static final SimpleDoubleProperty searchProgress = new SimpleDoubleProperty(0.0);

	public static final Observable<Font> defaultFont = new Observable<>(Font.font("Arial", 15));

	public enum ShortCut {
		COPY("Copy", new Observable<>(new KeyCodeCombination(KeyCode.C, KeyCodeCombination.SHORTCUT_DOWN))),
		OPEN_SEARCH("Find in File...",
				new Observable<>(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.SHORTCUT_DOWN))),
		OPEN_ALL_SEARCH("Find in all Files...",
				new Observable<>(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.SHORTCUT_DOWN,
						KeyCodeCombination.SHIFT_DOWN))),
		OPEN_GO_TO_LINE("Go To Line...",
				new Observable<>(new KeyCodeCombination(KeyCode.L, KeyCodeCombination.SHORTCUT_DOWN))),
		OPEN_FILTERS("Filters...",
				new Observable<>(new KeyCodeCombination(KeyCode.H, KeyCodeCombination.SHORTCUT_DOWN))),
		OPEN_FILES("Open...", new Observable<>(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN))),
		CLOSE_TAB("Close Tab", new Observable<>(new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN))),
		SAVE_SEARCH_RESULTS("Save Search Results...",
				new Observable<>(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN)));

		private final String uiName;
		private final Observable<KeyCombination> keyCombo;

		private ShortCut(String uiName, Observable<KeyCombination> keyCombo) {
			this.uiName = uiName;
			this.keyCombo = keyCombo;
		}

		public String uiName() {
			return uiName;
		}

		public Observable<KeyCombination> observable() {
			return keyCombo;
		}

		public String getKeyDisplayText() {
			return keyCombo.getValue().getDisplayText();
		}

		public void update(KeyCombination value) {
			keyCombo.setValue(value);
		}
	}

	public static Observable<Theme> theme = new Observable<>(Theme.LIGHT);

	public static ObservableList<JLoggPlugin> plugins = FXCollections.observableArrayList();
}
