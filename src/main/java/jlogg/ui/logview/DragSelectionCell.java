package jlogg.ui.logview;

import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseButton;
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

	public DragSelectionCell(DragSelectableContent selectableContent) {
		this.selectableContent = selectableContent;
		initDragAction(this);
	}

	protected void initDragAction(Node node) {
		node.setOnMousePressed((event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				// handle the same as a drag
				selectableContent.setDragStart(getIndex());
				selectableContent.selectRange(getIndex(), getIndex());
			}
		});

		node.setOnDragDetected((event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				selectableContent.setDragStart(getIndex());
				startFullDrag();
				selectableContent.selectRange(getIndex(), getIndex());
			}
		});

		node.setOnMouseDragEntered((event) -> {
			selectableContent.selectRange(selectableContent.getDragStart(), getIndex());
		});

	}
}