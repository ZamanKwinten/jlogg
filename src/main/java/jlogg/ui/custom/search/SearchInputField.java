package jlogg.ui.custom.search;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jlogg.shared.SearchCriteria;

public class SearchInputField extends TextField {
	private final SearchBox searchBox;

	private final ChangeListener<Boolean> maximizedListener;
	private final ChangeListener<Number> widthListener;
	private final ChangeListener<Number> heightListener;
	private final ChangeListener<Boolean> focussedListener;

	private final SearchInputPopup popup;

	public SearchInputField(SearchBox searchBox) {
		this.searchBox = searchBox;
		this.popup = new SearchInputPopup(this);

		setOnMouseClicked(this::togglePopup);

		sceneProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				registerResizerListeners((Stage) newVal.getWindow());
			} else {
				unregisterResizerListeners((Stage) oldVal.getWindow());
			}
		});

		setOnKeyPressed((event) -> {
			if (event.isShortcutDown() && event.getCode() == KeyCode.SPACE) {
				popup.open();
				event.consume();
			}

			if (event.getCode() == KeyCode.ENTER) {
				popup.hide();
			}
		});

		maximizedListener = (obs, oldVal, newVal) -> {
			if (popup.isShowing()) {
				popup.hide();
			}
		};

		widthListener = (obs, oldVal, newVal) -> {
			if (popup.isShowing()) {
				popup.open();
			}
		};

		heightListener = (obs, oldVal, newVal) -> {
			if (popup.isShowing()) {
				popup.hide();
			}
		};

		focussedListener = (obs, oldV, newV) -> {
			if (!newV && popup.isShowing()) {
				popup.hide();
			}
		};

	}

	public void setValue(SearchCriteria criteria) {
		String pattern = criteria.getPatternString();

		setText(pattern);
		positionCaret(pattern.length());
		searchBox.optionRow.setIgnoreCase(criteria.isIgnoreCase());

		fireEvent(new ActionEvent());

		popup.hide();
	}

	private void registerResizerListeners(Stage stage) {
		stage.maximizedProperty().addListener(maximizedListener);
		stage.widthProperty().addListener(widthListener);
		stage.heightProperty().addListener(heightListener);
		focusedProperty().addListener(focussedListener);
	}

	private void unregisterResizerListeners(Stage stage) {
		stage.maximizedProperty().removeListener(maximizedListener);
		stage.widthProperty().removeListener(widthListener);
		stage.heightProperty().removeListener(heightListener);
		focusedProperty().removeListener(focussedListener);
	}

	private void togglePopup(MouseEvent event) {
		if (popup.isShowing()) {
			popup.hide();
		} else {
			popup.open();
		}
	}

}
