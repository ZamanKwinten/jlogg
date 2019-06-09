package jlogg.ui.popup;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jlogg.ui.css.ResourceLoader;
import jlogg.ui.popup.utils.SearchFileWrapper;
import jlogg.ui.popup.utils.UpDownBox;

public class SearchPopup extends Stage {

	private final ObservableList<SearchFileWrapper> fileList;

	private boolean isCanceled = false;

	public SearchPopup(List<File> files) {
		setTitle("Search configuration");
		Label info = new Label("Please specify the files and order in which has to be searched");

		VBox content = new VBox(5.0);

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

		HBox footerBox = new HBox(5.0);
		footerBox.setAlignment(Pos.CENTER_RIGHT);
		Button search = new Button("Search");
		search.setOnAction(event -> {
			// redundant
			isCanceled = false;
			super.close();
		});
		Button cancel = new Button("Cancel");
		cancel.setOnAction(event -> {
			isCanceled = true;
			super.close();
		});
		footerBox.getChildren().addAll(search, cancel);

		content.getChildren().addAll(info, openedFiles, upDownBox, footerBox);
		content.getStylesheets().add(ResourceLoader.loadResourceFile("SearchPopup.css"));
		content.getStyleClass().add("content");

		Scene scene = new Scene(content);
		setMinWidth(360);
		setMinHeight(250);
		setScene(scene);
	}

	/**
	 * Open the popup + return a list of the selected files
	 * 
	 * @return
	 */
	public Optional<List<File>> open() {
		super.showAndWait();

		return isCanceled ? Optional.empty()
				: Optional.of(fileList.stream().filter(searchWrapper -> searchWrapper.shouldIncludeInSearch())
						.map(searchWrapper -> searchWrapper.getFile()).collect(Collectors.toList()));
	}

	@Override
	public void showAndWait() {
		throw new UnsupportedOperationException("Use the open method instead");
	}

}
