package jlogg.ui.logview;

import javafx.scene.Node;
import javafx.scene.control.TableCell;
import jlogg.plugin.LogLine;
import jlogg.ui.interfaces.DragSelectableContent;

/**
 * A table cell that is optimized to allow dragging via selection
 * 
 * @author KWZA
 *
 */
abstract class DragSelectionCell extends TableCell<LogLine, String> {

	protected final DragSelectableContent selectableContent;

	public DragSelectionCell(final DragSelectableContent logFileView) {
		this.selectableContent = logFileView;
		initDragAction(this);
	}

	protected void initDragAction(Node node) {
		node.setOnMousePressed((event) -> {
			// handle the same as a drag
			selectableContent.setDragStart(getIndex());
			selectableContent.selectRange(getIndex(), getIndex());
		});

		node.setOnDragDetected((event) -> {
			selectableContent.setDragStart(getIndex());
			startFullDrag();
			selectableContent.selectRange(getIndex(), getIndex());
		});

		node.setOnMouseDragEntered((event) -> {
			selectableContent.selectRange(selectableContent.getDragStart(), getIndex());
		});

	}
}