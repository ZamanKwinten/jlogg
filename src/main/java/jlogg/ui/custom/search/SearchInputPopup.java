package jlogg.ui.custom.search;

import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import jlogg.shared.SearchCriteria;
import jlogg.ui.GlobalConstants;
import jlogg.ui.css.ResourceLoader;

public class SearchInputPopup extends Popup {

	private final SearchInputField inputField;
	private final VBox content;
	private final ScrollPane scroll;

	public SearchInputPopup(SearchInputField inputField) {
		this.inputField = inputField;
		content = new VBox();
		content.getStylesheets().add(ResourceLoader.loadResourceFile("SearchInputPopup.css"));
		content.getStyleClass().add("content");

		scroll = new ScrollPane(content);
		scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);
		scroll.setMaxHeight(150);
		scroll.minWidthProperty().bind(inputField.widthProperty());

		getContent().add(scroll);

		GlobalConstants.searchHistory.get().addListener(new ListChangeListener<SearchCriteria>() {
			@Override
			public void onChanged(Change<? extends SearchCriteria> c) {
				content.getChildren().clear();
				for (SearchCriteria sc : c.getList()) {
					content.getChildren().add(new SearchCriteriaLabel(sc));
				}
			}
		});
	}

	public void open() {
		Point2D point = inputField.localToScreen(0, inputField.getHeight());
		setAnchorLocation(AnchorLocation.WINDOW_TOP_LEFT);
		show(inputField, point.getX(), point.getY());
	}

	private class SearchCriteriaLabel extends Label {
		public SearchCriteriaLabel(SearchCriteria criteria) {
			super(criteria.getPatternString());
			getStyleClass().add("searchCriteria");
			setMaxWidth(Double.MAX_VALUE);

			setOnMouseClicked((event) -> {
				inputField.setValue(criteria);
				// request focus to prevent the scroll pane to get focus (has strange space
				// behavior)
				requestFocus();
			});
		}
	}
}