package jlogg.ui.popup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jlogg.shared.Filter;
import jlogg.ui.GlobalConstants;
import jlogg.ui.css.ResourceLoader;
import jlogg.ui.popup.utils.UpDownBox;

public class FilterPopup extends Stage {

	private TextField matchingPatternInput;
	private CheckBox ignoreCase;
	private ColorPicker foreColorPicker;
	private ColorPicker backColorPicker;

	private ListView<Filter> registeredFilters;

	private final ObservableList<Filter> filterList;

	private Filter currentFilter = null;
	private final SimpleBooleanProperty disabledControls = new SimpleBooleanProperty(true);

	private final String defaultFilter;

	public FilterPopup(Optional<String> selection) {
		List<Filter> beforeChangesFilters = new ArrayList<>();
		for (Filter f : GlobalConstants.filters) {
			beforeChangesFilters.add(f.clone());
		}
		filterList = FXCollections.observableArrayList(beforeChangesFilters);

		if (selection.isPresent()) {
			defaultFilter = selection.get();
		} else {
			defaultFilter = "New Filter";
		}

		Pane leftVBox = initLeftPane();
		Pane rightVBox = initRightPane();

		HBox buttonFooter = new HBox(5.0);
		buttonFooter.setAlignment(Pos.CENTER_RIGHT);
		Button okButton = new Button("OK");
		okButton.setOnAction((event) -> {
			// submit filter changes + close
			submit();
			close();
		});

		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction((event) -> {
			close();
		});

		Button applyButton = new Button("Apply");
		applyButton.setOnAction((event) -> {
			// just submit
			submit();
		});

		buttonFooter.getChildren().addAll(okButton, cancelButton, applyButton);

		GridPane root = new GridPane();
		root.add(leftVBox, 0, 0);
		root.add(rightVBox, 1, 0);
		root.add(buttonFooter, 0, 2, 2, 1);
		root.setVgap(5.0);

		/*
		 * Make sure that both parts grow equally when resizing
		 */
		for (int i = 0; i < 2; i++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setPercentWidth(100.0 / 2.0);
			cc.setHgrow(Priority.ALWAYS);
			root.getColumnConstraints().add(cc);
		}
		RowConstraints rc = new RowConstraints();
		rc.setVgrow(Priority.ALWAYS);
		root.getRowConstraints().add(rc);

		root.setPadding(new Insets(5));

		setMinWidth(520);
		setMinHeight(250);

		Scene s = new Scene(root, 550, 350);
		s.getStylesheets().addAll(ResourceLoader.loadResourceFile("color.css"));
		s.getRoot().setStyle("-fx-base:" + GlobalConstants.theme.getValue().getFXBase());
		setScene(s);

		setTitle("Filters");
		initModality(Modality.APPLICATION_MODAL);
	}

	private Pane initLeftPane() {
		VBox vbox = new VBox(5.0);
		registeredFilters = new ListView<>(filterList);
		registeredFilters.setCellFactory((value) -> {
			ListCell<Filter> cell = new ListCell<Filter>() {
				@Override
				protected void updateItem(Filter filter, boolean empty) {
					super.updateItem(filter, empty);
					String background;
					String foreground;
					if (!empty) {

						setText(filter.getPatternString());
						background = filter.getBackGroundRGB();
						foreground = filter.getForeGroundRGB();

					} else {
						setText(null);
						background = "-fx-jlogg-background-color";
						foreground = "-fx-jlogg-text-fill-color";
					}

					setStyle("-fx-background-color:" + background + ";" + "-fx-text-fill:" + foreground);
					getStyleClass().add("selectable");
				}
			};
			return cell;
		});

		registeredFilters.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		registeredFilters.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			disabledControls.set(false);

			if (newValue != null) {
				// Clicked on an actual item
				// propagate newfilter to the right pane
				currentFilter = newValue;
				matchingPatternInput.setText(newValue.getPatternString());
				ignoreCase.setSelected(newValue.isIgnoreCase());
				foreColorPicker.setValue(newValue.getForeGroundColor());
				backColorPicker.setValue(newValue.getBackGroundColor());
			}

		});

		registeredFilters.setStyle("-fx-background-color:-fx-jlogg-background-color");

		AnchorPane anchor = new AnchorPane();
		HBox leftButtonBox = new HBox(5.0);
		Button addButton = new Button("Add");
		addButton.setOnMouseClicked((event) -> {
			Filter newFilter = new Filter(defaultFilter, ignoreCase.isSelected(), foreColorPicker.getValue(),
					backColorPicker.getValue());
			filterList.add(newFilter);
			registeredFilters.getSelectionModel().select(newFilter);
		});

		Button deleteButton = new Button("Remove");
		deleteButton.setOnAction((event) -> {
			filterList.remove(currentFilter);
			if (filterList.size() == 0) {
				disabledControls.setValue(true);
			}

		});

		leftButtonBox.getChildren().addAll(addButton, deleteButton);
		deleteButton.disableProperty().bind(disabledControls);

		UpDownBox upDownBox = new UpDownBox(registeredFilters.getSelectionModel(), filterList);
		upDownBox.bindDisabledProperty(disabledControls);

		AnchorPane.setLeftAnchor(leftButtonBox, 0.0);
		AnchorPane.setRightAnchor(upDownBox, 0.0);
		anchor.getChildren().addAll(leftButtonBox, upDownBox);

		VBox.setVgrow(registeredFilters, Priority.ALWAYS);
		vbox.getChildren().addAll(registeredFilters, anchor);

		vbox.setMinWidth(250);
		return vbox;
	}

	private Pane initRightPane() {
		GridPane grid = new GridPane();

		grid.setHgap(5);

		Label matchingPatternText = new Label("Matching Pattern:");
		matchingPatternText.setStyle("-fx-text-fill:-fx-jlogg-text-fill-color");
		matchingPatternInput = new TextField();
		matchingPatternInput.disableProperty().bind(disabledControls);
		matchingPatternInput.textProperty().addListener((observable, oldValue, newValue) -> {
			if (currentFilter != null) {
				currentFilter.setPattern(newValue);
				// Also update the name in the list
				registeredFilters.refresh();
			}
		});

		ignoreCase = new CheckBox("Ignore case");
		ignoreCase.setStyle("-fx-text-fill:-fx-jlogg-text-fill-color");
		ignoreCase.disableProperty().bind(disabledControls);
		ignoreCase.setOnAction((event) -> {
			if (currentFilter != null) {
				currentFilter.setIgnoreCase(ignoreCase.isSelected());
			}
		});
		ignoreCase.setMaxWidth(Double.MAX_VALUE);

		Label textColor = new Label("Fore Color:");
		textColor.setStyle("-fx-text-fill:-fx-jlogg-text-fill-color");
		foreColorPicker = new ColorPicker(Color.BLACK);
		foreColorPicker.disableProperty().bind(disabledControls);
		foreColorPicker.setOnAction((event) -> {
			if (currentFilter != null) {
				currentFilter.setForeGroundColor(foreColorPicker.getValue());
				// Also update the font color in the list
				registeredFilters.refresh();
			}
		});

		Label backColor = new Label("Back Color:");
		backColor.setStyle("-fx-text-fill:-fx-jlogg-text-fill-color");
		backColorPicker = new ColorPicker();
		backColorPicker.disableProperty().bind(disabledControls);
		backColorPicker.setOnAction((event) -> {
			if (currentFilter != null) {
				currentFilter.setBackGroundColor(backColorPicker.getValue());
				// Also update the background color in the lists
				registeredFilters.refresh();
			}
		});

		grid.setVgap(5.0);
		// 1st Row
		grid.add(matchingPatternText, 0, 0);
		grid.add(matchingPatternInput, 1, 0);
		// 2nd Row
		grid.add(ignoreCase, 0, 1);

		// 3rd Row
		grid.add(textColor, 0, 2);
		grid.add(foreColorPicker, 1, 2);

		// 4th Row
		grid.add(backColor, 0, 3);
		grid.add(backColorPicker, 1, 3);

		grid.setPadding(new Insets(0, 0, 0, 10));

		ColumnConstraints cc = new ColumnConstraints();
		cc.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().addAll(new ColumnConstraints(), cc);

		return grid;
	}

	private void submit() {
		GlobalConstants.filters.clear();
		GlobalConstants.filters.addAll(filterList);
	}

	@Override
	public void close() {
		// We have to repaint all the lines to apply the filters :(
		super.close();
	}

}
