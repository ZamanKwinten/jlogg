package jlogg.ui.custom;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * A HBox of exactly 5 pixels that can be added anywhere to allow resizing by
 * using mouse drag
 * 
 * @author KWZA
 *
 */
public class VerticalResizer extends HBox {

	@SuppressWarnings("unused")
	private final Node child;

	private double initialHeight;
	private double initialY;

	public VerticalResizer(Pane child) {
		this.child = child;
		getStyleClass().add("vertical_resize");
		setMinHeight(5);
		setMaxHeight(5);

		setOnMousePressed((event) -> {
			initialHeight = child.getHeight();
			child.setMinHeight(initialHeight);
			child.setMaxHeight(initialHeight);
			initialY = event.getSceneY();
		});

		setOnMouseDragged((event) -> {
			double delta = initialY - event.getSceneY();
			if (delta != 0) {
				if (initialHeight + delta > 5) {
					child.setMinHeight(initialHeight + delta);
				}
				child.setMaxHeight(initialHeight + delta);
			}
		});
	}
}
