package jlogg.ui.logview;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.control.TableColumn;
import jlogg.shared.LogLine;

class LineNumberColumn extends TableColumn<LogLine, String> {

	public LineNumberColumn(ObservableDoubleValue width) {
		setCellValueFactory((cell) -> {
			return new SimpleStringProperty(cell.getValue().getLineNumber() + "");
		});

		minWidthProperty().bind(width);
		maxWidthProperty().bind(width);
		prefWidthProperty().bind(width);

		setSortable(false);
	}
}
