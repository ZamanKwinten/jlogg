package jlogg.ui.custom;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.SearchEvent;
import jlogg.shared.SearchCriteria;

public class SearchRow extends HBox {

	private final Label textLabel;
	private final TextField textInput;
	private final Label searchButton;
	private final Label closeButton;

	private final FilteredView parent;

	public SearchRow(FilteredView parent) {
		super(5);
		this.parent = parent;
		textLabel = new Label("Text: ");
		textInput = new TextField();
		textInput.setOnAction(this::fireSearch);

		searchButton = new Label("Search");
		searchButton.setOnMouseClicked(this::fireSearch);
		searchButton.getStyleClass().add("hoverableButton");
		searchButton.getStyleClass().add("searchButton");

		closeButton = new Label("X");
		closeButton.getStyleClass().add("hoverableButton");
		closeButton.getStyleClass().add("searchCloseButton");
		closeButton.setOnMouseClicked((event) -> {
			parent.hide();
		});

		setHgrow(textInput, Priority.ALWAYS);
		getStyleClass().add("searchRowPadding");
		setAlignment(Pos.CENTER_LEFT);
		getChildren().addAll(textLabel, textInput, searchButton, closeButton);
	}

	public void setSearchText(String text) {
		textInput.setText(text);
		// textInput.positionCaret(textInput.getText().length());
	}

	public void focusSearchText() {
		textInput.requestFocus();
	}

	private void fireSearch(Event event) {
		EventBusFactory.getInstance().getEventBus()
				.post(new SearchEvent(new SearchCriteria(textInput.getText(), parent.getOptionRow().isIgnoreCase())));
	}
}
