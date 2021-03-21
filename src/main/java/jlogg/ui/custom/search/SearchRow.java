package jlogg.ui.custom.search;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SearchRow extends HBox {

	private final Label textLabel;
	private final SearchInputField searchInput;
	private final Label searchButton;
	private final Label closeButton;

	public SearchRow(SearchBox parent) {
		super(5);
		textLabel = new Label("Text: ");
		searchInput = new SearchInputField(parent);
		searchInput.setOnAction(parent::fireSearch);

		searchButton = new Label("Search");
		searchButton.setOnMouseClicked(parent::fireSearch);
		searchButton.getStyleClass().add("hoverableButton");
		searchButton.setPadding(new Insets(3));

		closeButton = new Label("X");
		closeButton.getStyleClass().add("hoverableButton");
		closeButton.setPadding(new Insets(3));
		closeButton.setOnMouseClicked((event) -> {
			parent.hide();
		});

		setHgrow(searchInput, Priority.ALWAYS);
		setPadding(new Insets(0, 5, 0, 5));

		setAlignment(Pos.CENTER_LEFT);
		getChildren().addAll(textLabel, searchInput, searchButton, closeButton);
	}

	public void setSearchText(String text) {
		searchInput.setText(text);
		searchInput.positionCaret(text.length());
	}

	public String getSearch() {
		return searchInput.getText();
	}

	public void focusSearchText() {
		searchInput.requestFocus();
	}
}
