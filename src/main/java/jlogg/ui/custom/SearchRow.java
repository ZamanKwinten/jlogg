package jlogg.ui.custom;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	private final TextField textInput;
	private final Label searchButton;
	private final Label closeButton;

	private final FilteredView parent;
	private final FileTab filetab;
	private final MainPane mainPane;

	public SearchRow(MainPane mainPane, FileTab filetab, FilteredView parent) {
		super(5);
		this.parent = parent;
		this.filetab = filetab;
		this.mainPane = mainPane;
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
	}

	public void focusSearchText() {
		textInput.requestFocus();
	}

	private void fireSearch(Event event) {
		List<File> fileList = null;
		if (parent.getOptionRow().isAllFilesSearch()) {
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

		EventBusFactory.getInstance().getEventBus().post(new SearchEvent(fileList,
				new SearchCriteria(textInput.getText(), parent.getOptionRow().isIgnoreCase())));
	}
}
