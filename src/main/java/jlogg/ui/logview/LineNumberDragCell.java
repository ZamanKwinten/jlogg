package jlogg.ui.logview;

import javafx.geometry.Pos;
import jlogg.ui.GlobalConstants;
import jlogg.ui.interfaces.DragSelectableContent;
import jlogg.ui.utils.FXUtils;

class LineNumberDragCell extends DragSelectionCell {
	public LineNumberDragCell(DragSelectableContent logFileView) {
		super(logFileView);

		fontProperty().bind(GlobalConstants.defaultFont);
		setAlignment(Pos.CENTER_LEFT);

		setStyle(
				"-fx-background-color: -fx-jlogg-background-color; -fx-border-style: solid; -fx-border-width: 0 1 0 0; -fx-border-color: -fx-jlogg-text-fill-color");
		FXUtils.columnFreezeHack(this);
	}

	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
		} else {
			setText(item);
		}
	}
}