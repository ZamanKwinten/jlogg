package jlogg.ui.logview;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.control.TableColumn;
import jlogg.datahandlers.FileLineReader;
import jlogg.plugin.LogLine;

class LineTextColumn extends TableColumn<LogLine, String> {

	public LineTextColumn(ObservableDoubleValue width) {
		setCellValueFactory((cell) -> {
			return new SimpleStringProperty(FileLineReader.readLineFromFile(cell.getValue()));
		});

		minWidthProperty().bind(width);
		maxWidthProperty().bind(width);
		prefWidthProperty().bind(width);

		setSortable(false);
	}
}
