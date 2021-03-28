package jlogg.ui.popup;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import jlogg.ui.popup.utils.SearchFileWrapper;
import jlogg.ui.popup.utils.UpDownBox;

public class SearchPopup extends PopupWithReturn<List<File>> {

	private final ObservableList<SearchFileWrapper> fileList;

	public SearchPopup(List<File> files) {
		super();
		setTitle("Search configuration");
		Label info = new Label("Please specify the files and order in which has to be searched");

		fileList = FXCollections
				.observableArrayList(files.stream().map(f -> new SearchFileWrapper(f)).collect(Collectors.toList()));
		ListView<SearchFileWrapper> openedFiles = new ListView<>(fileList);
		openedFiles.setCellFactory((value) -> {
			ListCell<SearchFileWrapper> cell = new ListCell<>() {
				@Override
				protected void updateItem(SearchFileWrapper file, boolean empty) {
					super.updateItem(file, empty);
					if (!empty) {
						HBox box = new HBox(5.0);
						CheckBox check = new CheckBox();
						check.setSelected(file.shouldIncludeInSearch());

						check.selectedProperty().addListener((val, oldVal, newVal) -> {
							file.shouldIncludeInSearch(newVal);
						});
						Label label = new Label(file.getFile().getName());
						box.getChildren().addAll(check, label);

						setGraphic(box);
					} else {
						setGraphic(null);
					}
				}
			};
			return cell;
		});

		UpDownBox upDownBox = new UpDownBox(openedFiles.getSelectionModel(), fileList);
		upDownBox.setAlignment(Pos.CENTER_LEFT);

		CancelableFooter footer = getCancelableFooterBox("Search", null);
		content.getChildren().addAll(info, openedFiles, upDownBox, footer);
		footer.requestFocus();

		setMinWidth(360);
		setMinHeight(250);
	}

	/**
	 * Open the popup + return a list of the selected files
	 * 
	 * @return
	 */
	@Override
	public List<File> getReturnValue() {
		return fileList.stream().filter(searchWrapper -> searchWrapper.shouldIncludeInSearch())
				.map(searchWrapper -> searchWrapper.getFile()).collect(Collectors.toList());
	}

}
