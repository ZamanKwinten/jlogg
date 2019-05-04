package jlogg.ui.logview;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.control.TableColumn;
import jlogg.shared.LogLine;

class LineTextColumn extends TableColumn<LogLine, String> {

	public LineTextColumn(ObservableDoubleValue width) {
		setCellValueFactory((cell) -> {
			return new SimpleStringProperty(cell.getValue().getLineString());
		});

		minWidthProperty().bind(width);
		maxWidthProperty().bind(width);
		prefWidthProperty().bind(width);

		setSortable(false);
	}
}
