package jlogg.ui.custom;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ResizableView extends VBox {

	private final VerticalResizer resizer;

	public ResizableView() {
		resizer = new VerticalResizer(this);

		getChildren().add(resizer);
	}

	public void hide() {
		setVisibility(false);
	}

	public void show() {
		setVisibility(true);
	}

	private void setVisibility(boolean isVisible) {
		setVisibilityRecursive(this, isVisible);
	}

	private void setVisibilityRecursive(Pane p, boolean isVisible) {
		for (Node n : p.getChildren()) {
			if (n instanceof Pane) {
				setVisibilityRecursive((Pane) n, isVisible);
			} else {
				setVisibility(n, isVisible);
			}
		}
		setVisibility(p, isVisible);
	}

	private void setVisibility(Node n, boolean isVisible) {
		// make sure to only update if not bound together, it's not uncommon for
		// components to bind managed & visible properties
		if (!n.managedProperty().isBound()) {
			n.setManaged(isVisible);
		}
		if (!n.visibleProperty().isBound()) {
			n.setVisible(isVisible);
		}
	}
}
