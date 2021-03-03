package jlogg.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import jlogg.shared.Filter;
import jlogg.shared.LogLine;
import jlogg.ui.custom.search.SearchHistoryList;
import jlogg.ui.logview.ObservableFont;
import jlogg.ui.menubar.ObservableKeyCombination;

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

	public enum ShortCut {
		SELECT_ALL("Select All",
				new ObservableKeyCombination(new KeyCodeCombination(KeyCode.A, KeyCodeCombination.SHORTCUT_DOWN))),
		COPY("Copy", new ObservableKeyCombination(new KeyCodeCombination(KeyCode.C, KeyCodeCombination.SHORTCUT_DOWN))),
		OPEN_SEARCH("Find...",
				new ObservableKeyCombination(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.SHORTCUT_DOWN))),
		OPEN_GO_TO_LINE("Go To Line...",
				new ObservableKeyCombination(new KeyCodeCombination(KeyCode.L, KeyCodeCombination.SHORTCUT_DOWN))),
		OPEN_FILTERS("Filters...",
				new ObservableKeyCombination(new KeyCodeCombination(KeyCode.H, KeyCodeCombination.SHORTCUT_DOWN))),
		OPEN_FILES("Open...",
				new ObservableKeyCombination(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN))),
		CLOSE_TAB("Close Tab",
				new ObservableKeyCombination(new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN))),
		SAVE_SEARCH_RESULTS("Save Search Results...",
				new ObservableKeyCombination(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN)));

		private final String uiName;
		private final ObservableKeyCombination keyCombo;

		private ShortCut(String uiName, ObservableKeyCombination keyCombo) {
			this.uiName = uiName;
			this.keyCombo = keyCombo;
		}

		public String uiName() {
			return uiName;
		}

		public ObservableKeyCombination observable() {
			return keyCombo;
		}

		public String getKeyDisplayText() {
			return keyCombo.getValue().getDisplayText();
		}

		public void update(KeyCombination value) {
			keyCombo.setValue(value);
		}
	}
}
