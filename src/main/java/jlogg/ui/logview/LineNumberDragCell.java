package jlogg.ui.logview;

import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import jlogg.ui.GlobalConstants;
import jlogg.ui.utils.FXUtils;

class LineNumberDragCell extends DragSelectionCell {
	public LineNumberDragCell(LogFileView logFileView) {
		super(logFileView);

		fontProperty().bind(GlobalConstants.defaultFont);
		fontProperty().addListener((o, a, b) -> {
			setPrefWidth();
		});
		setAlignment(Pos.CENTER_RIGHT);
		setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
		FXUtils.columnFreezeHack(this);
	}

	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
		} else {
			setText(item);
			setPrefWidth();
		}
	}

	public void setPrefWidth() {
		double width = FXUtils.calculateTextControlWidth(getFont(), " " + getText(), 15);

		((LogFileView) selectableContent).resetLineNumberColumnWidth();
		((LogFileView) selectableContent).updateLineNumberColumnWidth(width);
	}
}