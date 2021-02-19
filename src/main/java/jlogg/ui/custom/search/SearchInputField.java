package jlogg.ui.custom.search;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jlogg.shared.SearchCriteria;

public class SearchInputField extends TextField {
	private final SearchBox searchBox;

	private final SearchInputPopup popup;

	public SearchInputField(SearchBox searchBox) {
		this.searchBox = searchBox;
		this.popup = new SearchInputPopup(this);

		setOnMouseClicked(this::togglePopup);

		sceneProperty().addListener((obs, oldVal, newVal) -> {
			registerResizerListeners();
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
	}

	public void setValue(SearchCriteria criteria) {
		String pattern = criteria.getPatternString();

		setText(pattern);
		positionCaret(pattern.length());
		searchBox.optionRow.setIgnoreCase(criteria.isIgnoreCase());

		fireEvent(new ActionEvent());

		popup.hide();
	}

	private void registerResizerListeners() {

		Stage stage = (Stage) getScene().getWindow();

		stage.maximizedProperty().addListener((obs, oldVal, newVal) -> {
			if (popup.isShowing()) {
				popup.hide();
			}
		});

		stage.widthProperty().addListener((obs, oldVal, newVal) -> {
			if (popup.isShowing()) {
				popup.open();
			}
		});

		stage.heightProperty().addListener((obs, oldVal, newVal) -> {
			if (popup.isShowing()) {
				popup.hide();
			}
		});

		stage.focusedProperty().addListener((obs, oldV, newV) -> {
			if (!newV && popup.isShowing()) {
				popup.open();
			}
		});
	}

	private void togglePopup(MouseEvent event) {
		if (popup.isShowing()) {
			popup.hide();
		} else {
			popup.open();
		}
	}

}
