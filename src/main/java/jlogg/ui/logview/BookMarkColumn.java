package jlogg.ui.logview;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import jlogg.shared.LogLine;
import jlogg.ui.utils.FXUtils;

class BookMarkColumn extends TableColumn<LogLine, Boolean> {

	public BookMarkColumn() {

		setCellFactory((value) -> {
			return new BookMarkCell();
		});

		setSortable(false);
	}

	private final class BookMarkCell extends TableCell<LogLine, Boolean> {
		public BookMarkCell() {
			super();
			FXUtils.columnFreezeHack(this);
		}

		@Override
		protected void updateItem(Boolean value, boolean isEmpty) {
			super.updateItem(value, isEmpty);
			setText(null);
			setPrefWidth(20);
			getStyleClass().add("lineim");

			if (isEmpty) {
				setGraphic(null);
			} else {
				Circle circle = new Circle(2.5, Color.WHITE);
				setGraphic(circle);
				setOnMouseClicked((event) -> {
					if (circle.getFill().equals(Color.WHITE)) {
						circle.setFill(Color.BLUE);
					} else {
						circle.setFill(Color.WHITE);
					}
				});
			}

		}
	}
}
