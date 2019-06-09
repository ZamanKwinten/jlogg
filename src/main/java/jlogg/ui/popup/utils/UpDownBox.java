package jlogg.ui.popup.utils;

import java.util.Collections;

import javafx.application.Platform;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.HBox;

public class UpDownBox extends HBox {
	private final Button up;
	private final Button down;

	public UpDownBox(SelectionModel<?> selectionModel, ObservableList<?> observableList) {
		super(5.0);
		setAlignment(Pos.CENTER_RIGHT);

		up = new Button("Up");
		up.setOnAction((event) -> {
			int startIndex = selectionModel.getSelectedIndex();
			if (startIndex > 0) {
				// make sure that it is not yet the first item
				int endIndex = startIndex - 1;
				swap(selectionModel, observableList, startIndex, endIndex);
			}
		});

		down = new Button("Down");
		down.setOnAction((event) -> {
			int startIndex = selectionModel.getSelectedIndex();
			if (startIndex != -1 && startIndex < observableList.size() - 1) {
				// make sure that this is not the last item
				int endIndex = startIndex + 1;
				swap(selectionModel, observableList, startIndex, endIndex);
			}
		});

		getChildren().addAll(up, down);
	}

	public void bindDisabledProperty(ObservableBooleanValue disabledProperty) {
		up.disableProperty().bind(disabledProperty);
		down.disableProperty().bind(disabledProperty);
	}

	private void swap(SelectionModel<?> selection, ObservableList<?> list, int start, int end) {
		Platform.runLater(() -> {
			Collections.swap(list, start, end);
			selection.select(end);
		});
	}
}
