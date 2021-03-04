package jlogg.ui.custom.search;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jlogg.eventbus.EventBusFactory;
import jlogg.eventbus.SearchEvent;
import jlogg.shared.SearchCriteria;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.popup.SearchPopup;

public class SearchRow extends HBox {

	private final Label textLabel;
	private final SearchInputField searchInput;
	private final Label searchButton;
	private final Label closeButton;

	private final SearchBox parent;
	private final FileTab filetab;
	private final MainPane mainPane;

	public SearchRow(MainPane mainPane, FileTab filetab, SearchBox parent) {
		super(5);
		this.parent = parent;
		this.filetab = filetab;
		this.mainPane = mainPane;
		textLabel = new Label("Text: ");
		searchInput = new SearchInputField(parent);
		searchInput.setOnAction(this::fireSearch);

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

		setHgrow(searchInput, Priority.ALWAYS);
		getStyleClass().add("searchRowPadding");
		setAlignment(Pos.CENTER_LEFT);
		getChildren().addAll(textLabel, searchInput, searchButton, closeButton);
	}

	public void setSearchText(String text) {
		searchInput.setText(text);
	}

	public String getSearch() {
		return searchInput.getText();
	}

	public void focusSearchText() {
		searchInput.requestFocus();
	}

	private void fireSearch(Event event) {
		List<File> fileList = null;
		if (parent.optionRow.isAllFilesSearch()) {
			// When all file search is enabled + more than one tab we need to determine
			// which file tabs have to be searched
			List<FileTab> fileTabs = mainPane.getFileTabs();
			if (fileTabs.size() > 1) {

				SearchPopup popup = new SearchPopup(
						fileTabs.stream().map(fileTab -> fileTab.getFile()).collect(Collectors.toList()));
				Optional<List<File>> result = popup.open();
				if (result.isPresent()) {
					fileList = result.get();
				} else {
					// no files were selected => do nothing
					return;
				}
			}
		}

		if (fileList == null) {
			fileList = Arrays.asList(filetab.getFile());
		}

		EventBusFactory.getInstance().getEventBus().post(
				new SearchEvent(fileList, new SearchCriteria(searchInput.getText(), parent.optionRow.isIgnoreCase())));
	}
}
