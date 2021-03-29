package jlogg.ui.custom;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HorizontalResizer extends VBox {

	private double initialWidth;
	private double initialX;

	public HorizontalResizer(Region child) {
		getStyleClass().add("horizontal_resize");
		setMinWidth(5);
		setMaxWidth(5);

		setOnMousePressed((event) -> {
			initialWidth = child.getWidth();
			child.setMinWidth(initialWidth);
			child.setMaxWidth(initialWidth);
			initialX = event.getSceneX();
		});

		setOnMouseDragged((event) -> {
			double delta = initialX - event.getSceneX();
			if (delta != 0) {
				if (initialWidth + delta > 5) {
					child.setMinWidth(initialWidth + delta);
				}
				child.setMaxWidth(initialWidth + delta);
			}
		});
	}
}